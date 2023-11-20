package freshbread.bread.service;

import freshbread.bread.domain.Cart;
import freshbread.bread.domain.CartItem;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.item.Item;
import freshbread.bread.exception.NoCartEntityException;
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
        List<Cart> carts = cartRepository.findByMemberLoginIdWithMember(loginId);
        if(carts.size() == 0) {
            log.info("cart 찾기 실패");
            throw new NoCartEntityException("관리자에게 문의하세요.");
        }
        return carts.get(0);
    }

    @Transactional
    public void deleteCartItem(String loginId) {
        List<Cart> carts = cartRepository.findByMemberLoginIdWithCartItem(loginId);
        if(carts.size() == 0) {
            throw new IllegalStateException("Cart Entity 가 없습니다.");
        }
        Cart cart = carts.get(0);
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            cartItemRepository.deleteOne(cartItem);
        }
    }
}
