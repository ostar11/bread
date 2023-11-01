package freshbread.bread.service;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.Cart;
import freshbread.bread.domain.CartItem;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.Order;
import freshbread.bread.domain.OrderItem;
import freshbread.bread.domain.OrderSearch;
import freshbread.bread.domain.OrderStatus;
import freshbread.bread.domain.Pickup;
import freshbread.bread.domain.PickupStatus;
import freshbread.bread.domain.item.Item;
import freshbread.bread.exception.NoStockQuantityException;
import freshbread.bread.repository.CartItemRepository;
import freshbread.bread.repository.CartRepository;
import freshbread.bread.repository.ItemRepository;
import freshbread.bread.repository.MemberRepository;
import freshbread.bread.repository.OrderRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Long order(String loginId, Long itemId, int count) {
        // 로그인 정보에서 회원 아이디 조회
//        String loginId = memberDetails.getUsername();
        // 회원 엔티티 조회
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
        // 상품 엔티티 조회
//        Item item = itemRepository.findOne(itemId);
        Item item = itemRepository.findItemWithOnSale(itemId)
                .orElseThrow(() -> new NoStockQuantityException("재고가 소진되었습니다."));

        // 픽업정보 생성
//        Pickup pickup = new Pickup(PickupStatus.WAIT);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, orderItem);

        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public List<Long> orderCartItem(String loginId) {
        log.info("장바구니 상품 주문");
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
//        Cart cart = cartRepository.findByMemberV1(member);
        List<Cart> carts = cartRepository.findByMemberV2(loginId);
        if(carts.size() == 0) {
            throw new IllegalStateException("Cart entity 가 없습니다.");
        }

        Cart cart = carts.get(0);
        List<CartItem> cartItems = cart.getCartItems();
        List<Long> cartItemIdList = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), cartItem.getCount());
            Order order = Order.createOrder(member, orderItem);
            orderRepository.save(order);
            cartItemIdList.add(cartItem.getId());
        }
        return cartItemIdList;
    }

    public List<Order> findAllOrders(MemberDetails memberDetails) {
        String loginId = memberDetails.getUsername();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
        List<Order> orders = orderRepository.findAll(member);
        return orders;
    }

    public List<Order> findOrders(MemberDetails memberDetails, OrderSearch orderSearch) {
        String loginId = memberDetails.getUsername();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
//        List<Order> orders = orderRepository.findAllByMemberStatus(member, orderStatus);
        List<Order> orders = orderRepository.findAllByMemberStatus(member, orderSearch.getOrderStatus());

        return orders;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

}
