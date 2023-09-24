package freshbread.bread.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Orders")
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    protected Order() {
        orderDate = LocalDateTime.now();
        orderStatus = OrderStatus.READY;
    }

    //=== 연관관계 메서드 ===//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.registerOrder(this);
    }

    // == 생성 메서드 == //
    public static Order createOrder(Member member, OrderItem... orderItems) {
//        Order order = new Order(pickup);
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderitem : orderItems) {
            order.addOrderItem(orderitem);
        }
        return order;
    }

    public void cancel() {
        if (orderStatus != OrderStatus.READY) {
            throw new IllegalStateException("이미 준비된 상품은 취소가 불가능합니다.");
        }
        this.cancelOrder();
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    private void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
    }
}
