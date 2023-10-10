package freshbread.bread.service;

import freshbread.bread.domain.Cart;
import freshbread.bread.domain.CartItem;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.item.Item;
import freshbread.bread.repository.CartRepository;
import freshbread.bread.repository.ItemRepository;
import freshbread.bread.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public void assignCart(Member member) {
        Cart cart = new Cart();
        cart.enrollMember(member);
        cartRepository.save(cart);
    }

    public void addItemToCart(String loginId, Long itemId, int count) {
//        Member member = memberRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요"));
        Item item = itemRepository.findOne(itemId);

        CartItem cartItem = CartItem.createCartItem(item, item.getPrice(), count);

        Cart cart = cartRepository.findByMemberLoginId(loginId);
//        Cart cart = member.getCart();
        cart.addCartItem(cartItem);
    }


    public Cart findCart(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
        Cart cart = cartRepository.findByMember(member);
        return cart;
    }
}
