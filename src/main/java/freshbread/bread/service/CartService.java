package freshbread.bread.service;

import freshbread.bread.domain.Cart;
import freshbread.bread.domain.CartItem;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.item.Item;
import freshbread.bread.repository.CartItemRepository;
import freshbread.bread.repository.CartRepository;
import freshbread.bread.repository.ItemRepository;
import freshbread.bread.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void assignCart(Member member) {
        Cart cart = new Cart();
        cart.enrollMember(member);
        cartRepository.save(cart);
    }

    @Transactional
    public void addItemToCart(String loginId, Long itemId, int count) {
//        Member member = memberRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요"));
        Item item = itemRepository.findOne(itemId);

        CartItem cartItem = CartItem.createCartItem(item, item.getPrice(), count);

        List<Cart> carts = cartRepository.findByMemberLoginId(loginId);

        if(carts.size() == 0) {
            throw new IllegalStateException("Cart Entity 가 없습니다.");
        }
        Cart cart = carts.get(0);

        cart.addCartItem(cartItem);

        cartItemRepository.save(cartItem);
    }

    public Cart findCart(String loginId) {
        log.info("회원아이디로 cart 찾기. 회원 아이디 ={}", loginId);
//        Member member = memberRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
//        Cart cart = cartRepository.findByMemberV1(member);
        List<Cart> carts = cartRepository.findByMemberV2(loginId);
        if(carts.size() == 0) {
            log.info("cart 찾기 실패");
//            throw new IllegalStateException("Cart entity 가 없습니다.");
        }
        return carts.get(0);
    }

    @Transactional
    public void deleteCartItem(List<Long> cartItemIdList) {
        log.info("장바구니 삭제");
        for (Long id : cartItemIdList) {
            CartItem cartItem = cartItemRepository.findById(id);
            cartItemRepository.deleteOne(cartItem);
        }

    }
}
