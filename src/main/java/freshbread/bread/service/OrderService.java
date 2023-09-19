package freshbread.bread.service;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.Order;
import freshbread.bread.domain.OrderItem;
import freshbread.bread.domain.Pickup;
import freshbread.bread.domain.PickupStatus;
import freshbread.bread.domain.item.Item;
import freshbread.bread.exception.NoStockQuantityException;
import freshbread.bread.repository.ItemRepository;
import freshbread.bread.repository.MemberRepository;
import freshbread.bread.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(MemberDetails memberDetails, Long itemId, int count) {
        // 로그인 정보에서 회원 아이디 조회
        String loginId = memberDetails.getUsername();
        // 회원 엔티티 조회
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다. 다시 로그인 해주세요."));
        // 상품 엔티티 조회
//        Item item = itemRepository.findOne(itemId);
        Item item = itemRepository.findItemWithOnSale(itemId)
                .orElseThrow(() -> new NoStockQuantityException("재고가 소진되었습니다."));

        // 픽업정보 생성
        Pickup pickup = new Pickup(PickupStatus.WAIT);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, pickup, orderItem);

        orderRepository.save(order);

        return order.getId();
    }

    public boolean checkOrderCount(int count) {
        return (count >= 1 && count <= 10) ? true : false;
    }
}
