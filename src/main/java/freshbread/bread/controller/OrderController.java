package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.Notification.NotificationType;
import freshbread.bread.domain.Order;
import freshbread.bread.domain.OrderItem;
import freshbread.bread.domain.OrderSearch;
import freshbread.bread.domain.OrderStatus;
import freshbread.bread.domain.item.Item;
import freshbread.bread.service.CartService;
import freshbread.bread.service.ItemService;
import freshbread.bread.service.NotificationService;
import freshbread.bread.service.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Validated // query string, query parameter, request body 검증시 사용
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final NotificationService notificationService;
    private final CartService cartService;

    /**
     * HTML form 으로 itemId, count 를 받아 주문 수행 주문은 최소 1개 최대 10개까지만 가능하도록 validation 진행
     * @param memberDetails : 회원 정보
     * @param itemId        : 상품 아이디로 상품 찾기
     * @param count         : 주문 수량 (1개 이상 10개 이하로만 메소드 수행)
     */
//    @PostMapping("/items/{id}/order")
//    public String order(@AuthenticationPrincipal MemberDetails memberDetails,
//                        @PathVariable("id") Long itemId,
//                        @RequestParam("count") @Range(min = 1, max = 10, message = "주문은 1개 이상 10개 이하까지만 가능합니다.") int count) {
//        orderService.order(memberDetails, itemId, count);
//        return "redirect:/items";
//    }

    @PostMapping("/items/{id}/order")
    public String orderWithNotification(@AuthenticationPrincipal MemberDetails memberDetails,
                        @PathVariable("id") Long itemId,
                        @RequestParam("count") @Range(min = 1, max = 10, message = "주문은 1개 이상 10개 이하까지만 가능합니다.") int count) {
        orderService.order(memberDetails.getUsername(), itemId, count);
//        Item item = itemService.findOne(itemId);
//        if(item.checkStockQuantity()) {
//            notificationService.sendStockNotification(NotificationType.REST5, item.getName() + " " + item.getStockQuantity(),
//                    String.valueOf(item.getId()));
//        }
        notificationService.sendStockNotificationV2(NotificationType.REST5, itemId);
        return "redirect:/items";
    }

    @PostMapping("/cart/order")
    public String orderCartItemWithNotification(@AuthenticationPrincipal MemberDetails memberDetails) {
        List<Long> cartItemIdList = orderService.orderCartItem(memberDetails.getUsername());
//        notificationService.sendStockNotifications(memberDetails.getUsername());
        cartService.deleteCartItem(cartItemIdList);
        return "redirect:/items";
    }



    /**
     * orderId, orderStatus, OrderDate, memberName 정보를 가지는 orderResponseDto 를 반환
     * 주문상품과 주문가격, 주문개수에 대한 정보가 없다. 밑에서 추가
     */
//    @ResponseBody
//    @GetMapping("/orders")
//    public List<OrderResponseDto> orderList(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
//        List<Order> orders = orderService.findAllOrders(memberDetails);
//        List<OrderResponseDto> result = orders.stream()
//                .map(o -> new OrderResponseDto(o))
//                .collect(Collectors.toList());
//        return result;
//    }

    /**
     * 전체 주문 조회
     * @param memberDetails : 회원정보를 가져온다.
     * @param model
     * @return
     */
    @GetMapping("/orders")
    public String orderItemList(@AuthenticationPrincipal MemberDetails memberDetails,
                                @ModelAttribute OrderSearch orderSearch,
                                Model model) {
        List<Order> orders = orderService.findOrders(memberDetails, orderSearch);
        List<OrderDto> results = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        model.addAttribute("results", results);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable("id") Long orderId, Model model) {
        Order order = orderService.findOrder(orderId);
        OrderDto orderDto = new OrderDto(order);
        model.addAttribute("order", orderDto);
        return "order/orderDetail";
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private List<OrderItemDto> orderItems;
        private int orderTotalPrice;

        public OrderDto(Order order) {
            orderId = order.getId();
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
            orderTotalPrice += order.getOrderItems().stream()
                    .mapToInt(OrderItem::getTotalPrice)
                    .sum();
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName; // 상품 명
        private int orderPrice; // 주문 가격
        private int orderCount; // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            orderCount = orderItem.getCount();
        }
    }
}
