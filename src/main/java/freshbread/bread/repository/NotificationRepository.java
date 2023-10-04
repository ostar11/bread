package freshbread.bread.repository;

import freshbread.bread.domain.Notification;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {

    private final EntityManager em;

    public List<Notification> findByMember(Long id) {
        return em.createQuery("select n from Notification n where n.member.id = :id",
                        Notification.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Notification save(Notification notification) {
        em.persist(notification);
        return notification;
    }

    public List<Notification> findByLoginIdAndChecked(String loginId, boolean checked) {
        List<Notification> notifications = em.createQuery("select n from Notification n where n.member.loginId = :loginId and n.checked = :checked",
                        Notification.class)
                .setParameter("loginId", loginId)
                .setParameter("checked", checked)
                .getResultList();
        return notifications;
    }

    public Notification findById(Long id) {
        return em.find(Notification.class, id);
    }
}
