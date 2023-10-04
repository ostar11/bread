package freshbread.bread.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String comment;

    private String url;

    @Column(nullable = false)
    private Boolean checked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    private LocalDateTime createdDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private Notification(String comment, String url, NotificationType notificationType, Member member) {
        this.comment = comment;
        this.url = url;
        this.checked = false;
        this.notificationType = notificationType;
        this.createdDate = LocalDateTime.now();
        this.member = member;
    }

    public static Notification createNotification(String comment, String url, NotificationType notificationType, Member member) {
        Notification notification = new Notification(comment, url, notificationType, member);
        member.getNotifications().add(notification);
        return notification;
    }

    public void read() {
        this.checked = true;
    }

    public enum NotificationType {
        REST5, REST10, NEW
    }

}
