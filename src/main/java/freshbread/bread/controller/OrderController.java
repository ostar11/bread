package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.item.Item;
import freshbread.bread.service.ItemService;
import freshbread.bread.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    /**
     * 상품별 주문 페이지 로드
     */
//    @GetMapping("/items/{id}/order")
//    public String createOrderForm(@PathVariable("id") Long itemId, Model model) {
//        Item item = itemService.findOne(itemId);
//        model.addAttribute("item", item);
////        model.addAttribute("itemId", itemId);
//        return "order/orderForm";
//    }

    /**
     * HTML form 으로 itemId, count 를 받아 주문 수행
     * @param memberDetails : 회원 정보
     * @param itemId : 상품 아이디로 상품 찾기
     * @param count : 주문 수량
     * @return
     */
    @PostMapping("/items/{id}/order")
    public String order(@AuthenticationPrincipal MemberDetails memberDetails,
                        @PathVariable("id") Long itemId,
                        @RequestParam("count") int count) {
        orderService.order(memberDetails, itemId, count);
        return "redirect:/items";
    }
}
