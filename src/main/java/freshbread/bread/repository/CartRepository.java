package freshbread.bread.repository;

import freshbread.bread.domain.Cart;
import freshbread.bread.domain.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final EntityManager em;

    public void save(Cart cart) {
        em.persist(cart);
    }

    public List<Cart> findByMemberLoginId(String loginId) {
        List<Cart> carts = em.createQuery("select c from Cart c where c.member.loginId = :loginId", Cart.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return carts;
    }

    public Cart findByMemberV1(Member member) {
        Cart cart = em.createQuery("select c from Cart c "
                        + " join fetch c.cartItems ci"
                        + " join fetch ci.item i"
                        + " where c.member = :member", Cart.class)
                .setParameter("member", member)
                .getSingleResult();
        return cart;
    }

    public List<Cart> findByMemberV2(String loginId) {
        List<Cart> carts = em.createQuery("select c from Cart c"
                        + " join fetch c.member m"
//                        + " join fetch c.cartItems ci"
//                        + " join fetch ci.item i"
                        + " where m.loginId = :loginId", Cart.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return carts;
    }

}
