package freshbread.bread.dto;

import freshbread.bread.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class NotificationDto {

    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {

        String id;
        String name;
        String content;
        String url;
        String type;
        String createdAt;

        public static Response createResponse(Notification notification) {
            return Response.builder()
                    .content(notification.getComment())
                    .id(notification.getId().toString())
                    .name(notification.getMember().getLoginId())
                    .url(notification.getUrl())
                    .type(notification.getNotificationType().name())
                    .createdAt(notification.getCreatedDate().toString())
                    .build();
        }
    }
}
