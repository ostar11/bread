package freshbread.bread.repository;

import freshbread.bread.domain.CartItem;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemRepository {

    private final EntityManager em;

    public void save(CartItem cartItem) {
        em.persist(cartItem);
    }

    public void deleteOne(CartItem cartItem) {
        em.remove(cartItem);
    }

    public List<CartItem> findByMemberLoginId(String loginId) {
        List<CartItem> cartItems = em.createQuery("select ci from CartItem ci"
                        + " join ci.cart c"
                        + " where c.member.loginId = :loginId", CartItem.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return cartItems;
    }

    public CartItem findById(Long id) {
        return em.find(CartItem.class, id);
    }

}
