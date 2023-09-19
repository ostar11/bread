package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.service.ItemService;
import freshbread.bread.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Validated // query string, query parameter, request body 검증시 사용
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    /**
     * HTML form 으로 itemId, count 를 받아 주문 수행
     * 주문은 최소 1개 최대 10개까지만 가능하도록 validation 진행
     * @param memberDetails : 회원 정보
     * @param itemId        : 상품 아이디로 상품 찾기
     * @param count         : 주문 수량
     * @return
     */
    @PostMapping("/items/{id}/order")
    public String order(@AuthenticationPrincipal MemberDetails memberDetails,
                        @PathVariable("id") Long itemId,
                        @RequestParam("count") @Range(min = 1, max = 10, message = "주문은 1개 이상 10개 이하까지만 가능합니다.") int count) {
        orderService.order(memberDetails, itemId, count);
        return "redirect:/items";
    }
}
