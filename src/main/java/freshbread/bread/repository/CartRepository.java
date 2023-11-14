package freshbread.bread.repository;

import freshbread.bread.domain.Cart;
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

    public List<Cart> findByMemberLoginIdWithCartItem(String loginId) {
        return em.createQuery("select distinct c from Cart c"
                        + " join fetch c.cartItems ci"
                        + " where c.member.loginId = :loginId", Cart.class)
                .setParameter("loginId", loginId)
                .getResultList();
    }

    public List<Cart> findByMemberLoginIdWithMember(String loginId) {
        List<Cart> carts = em.createQuery("select c from Cart c"
                        + " join fetch c.member m"
                        + " where m.loginId = :loginId", Cart.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return carts;
    }

    /**
     * 파라미터로 member 를 받는 것은 memberRepository를 통해 member 를 조회하기 때문에 총 2번의 쿼리를 날리는 반면
     * member의 loginId를 통한 페치조인을 사용하면 지연로딩 없이 쿼리 한번에 데이터를 가져올 수 있다.
     */
//    public Cart findByMemberV1(Member member) {
//        Cart cart = em.createQuery("select c from Cart c "
//                        + " join fetch c.cartItems ci"
//                        + " join fetch ci.item i"
//                        + " where c.member = :member", Cart.class)
//                .setParameter("member", member)
//                .getSingleResult();
//        return cart;
//    }
}
