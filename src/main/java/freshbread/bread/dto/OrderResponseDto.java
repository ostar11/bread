package freshbread.bread.dto;

import freshbread.bread.domain.Order;
import freshbread.bread.domain.OrderStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderResponseDto {
    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String name;

    public OrderResponseDto(Order order) {
        orderId = order.getId();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        name = order.getMember().getName();
    }
}
