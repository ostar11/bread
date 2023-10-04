package freshbread.bread.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); // 동시성 고료
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    /**
     * 어떤 회원에게 어떤 Emitter 가 연결되었는지를 저장
     * @param emitterId : 로그인 아이디과 현재시간을 합친 문자열로 emitterId
     * @param sseEmitter : DEFAULT_TIMEOUT 으로 설정하여 새로 만든 SseEmitter
     */
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    /**
     * 이벤트를 저장
     * @param eventCacheId
     * @param event
     */
    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    /**
     * 알림을 보내기 위해 해당 회원과 관련된 모든 이벤트를 찾움
     */
    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId)) // 저장할 때 구분자로 회원의 ID를 사용
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 해당 회원과 관련된 모든 이벤트를 찾는다.
     */
    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(memberId)) // 저장할 때 구분자로 회원의 ID를 사용
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * emitter 지움
     */
    @Override
    public void deleteById(String id) {
        emitters.remove(id);
    }

    /**
     * 해당 회원과 관련된 모든 emitter 를 지움
     */
    @Override
    public void deleteAllEmitterStartWithId(String memberId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    /**
     * 해당 회원과 관련된 모든 이벤트를 지움
     */
    @Override
    public void deleteAllEventCacheStartWithId(String memberId) {
        eventCache.forEach(
                (key, emitter) -> {
                    if (key.startsWith(memberId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}
