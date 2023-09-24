package freshbread.bread.repository;

import freshbread.bread.domain.Member;
import freshbread.bread.domain.Order;
import freshbread.bread.domain.OrderStatus;
import freshbread.bread.dto.OrderResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }


    public List<Order> findAll(Member member) {
        List<Order> orders = em.createQuery(
                "select o from Order o "
                        + " join fetch o.orderItems oi"
                        + " join fetch oi.item i"
                        + " where o.member = :member", Order.class)
                .setParameter("member", member)
                .getResultList();
        return orders;
    }

    public List<Order> findAllByMemberStatus(Member member, OrderStatus orderStatus) {
        String jpql = "select o from Order o "
                + " join fetch o.orderItems oi"
                + " join fetch oi.item i"
                + " where o.member = :member";
        if (orderStatus != null) {
            jpql += " and o.orderStatus = :orderStatus";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건

        query = query.setParameter("member", member);
        if (orderStatus != null) {
            query = query.setParameter("orderStatus", orderStatus);
        }

        return query.getResultList();
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }
}
