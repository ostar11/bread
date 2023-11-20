package freshbread.bread.service;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.Notification;
import freshbread.bread.domain.Notification.NotificationType;
import freshbread.bread.domain.Role;
import freshbread.bread.domain.item.Item;
import freshbread.bread.dto.NotificationDto;
import freshbread.bread.dto.NotificationDto.Response;
import freshbread.bread.repository.EmitterRepository;
import freshbread.bread.repository.ItemRepository;
import freshbread.bread.repository.MemberRepository;
import freshbread.bread.repository.NotificationRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public SseEmitter subscribe(String username, String lastEventId) {
        String emitterId = makeTimeIncludeId(username); // 로그인 아이디과 현재시간을 합친 문자열로 emitterId 생성
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT)); // 생성한 emitterId와 SseEmitter 객체를 저장한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); // SseEmitter 의 완료로 인한 전송 불가 시 SseEmitter 삭제
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId)); // SseEmitter 의 시간 만료시 전송 불가 시 SseEmitter 삭제

        // 연결직후, 데이터전송이 없을 시 503에러가 발생. 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(username); // 로그인한 사용자의 아이디와 현재시간을 합친 문자열 eventId 생성
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userEmail=" + username + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, username, emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(String loginId) {
        return loginId + "_" + System.currentTimeMillis();
    }

    /**
     * 알림 전송
     */
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    /**
     * 유실된 알림 전송
     */
    private void sendLostData(String lastEventId, String loginId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(
                String.valueOf(loginId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    /**
     * 다른 사용자가 알림을 보내는 기능
     * 알림을 생성한 후 알림에 대한 이벤트를 발생
     * 이벤트를 발생시키기 전 어떤 회원에게 알림을 보낼지에 대해 찾고 알림을 받을 회원의 Emitter를 모두 찾아 해당 Emitter로 Send 시켜주면 된다.
     */
    public void send(Member receiver, NotificationType notificationType, String content, String url) {
        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content + "개 남았습니다.", url));
        String receiverLoginId = receiver.getLoginId();
        String eventId = receiverLoginId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(
                receiverLoginId);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotificationDto.Response.createResponse(notification));
                }
        );
    }

    /**
     * 고객인 Member 엔티티만 찾아서 알림 전송
     * @param content : 재고 현황을 알려주는 메시지
     * @param url : 상품 상세 페이지 url
     */
    public void sendStockNotification(NotificationType notificationType, String content, String url) {
        List<Member> members = memberRepository.findByRole(Role.CUSTOMER);
        for (Member member : members) {
            send(member, NotificationType.REST5, content, url);
        }
    }

    public void sendStockNotificationV2(NotificationType notificationType, Long itemId) {
        List<Member> members = memberRepository.findByRole(Role.CUSTOMER);
        Item item = itemRepository.findOne(itemId);
        log.info("재고량은 충분? = {}", item.checkStockQuantity());
        if (item.checkStockQuantity()) {
            for (Member member : members) {
                String content = item.getName() + " 남은 수량 : " + item.getStockQuantity();
                String url = String.valueOf(item.getId());
                send(member, notificationType, content, url);
            }
        }
    }

    public void sendStockNotificationV3(NotificationType notificationType, List<Long> itemIdList) {
        List<Member> members = memberRepository.findByRole(Role.CUSTOMER);
        for (Long itemId : itemIdList) {
            Item item = itemRepository.findOne(itemId);
            if (item.checkStockQuantity()) {
                for (Member member : members) {
                    String content = item.getName() + " 남은 수량 : " + item.getStockQuantity();
                    String url = String.valueOf(item.getId());
                    send(member, notificationType, content, url);
                }
            }
        }

    }

    private Notification createNotification(Member receiver, Notification.NotificationType notificationType, String content, String url) {
        return Notification.createNotification(content, url, notificationType, receiver);
    }

    /**
     * notifyRepository 에서 member 의 id로 조회
     * NotifyDto.Response 로 변경하여 responseEntity 로 리턴
     */
    public List<Response> getNotificationsForUser(Member receiver) {
        List<Notification> notifies = notificationRepository.findByMember(receiver.getId());
        List<Response> responses = new ArrayList<>();
        for (Notification notification : notifies) {
            responses.add(Response.createResponse(notification));
        }
        return responses;
    }

//    public List<Notification> getNotifications(Member receiver) {
//        return notificationRepository.findByMember(receiver.getId());
//    }

    public List<Response> getNotReadNotifications(String loginId) {
        List<Notification> notifies = notificationRepository.findByLoginIdAndChecked(loginId, false);
        List<Response> responses = new ArrayList<>();
        for (Notification notification : notifies) {
            responses.add(Response.createResponse(notification));
        }
        return responses;
    }

    public Notification findOne(Long id) {
        return notificationRepository.findById(id);
    }

    public void markAsCheck(Notification notification) {
        notification.read();
    }
}
