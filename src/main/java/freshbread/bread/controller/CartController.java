package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.Cart;
import freshbread.bread.domain.CartItem;
import freshbread.bread.service.CartService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Validated
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;

    @PostMapping("/items/{id}/cart")
    public String addItemCart(@AuthenticationPrincipal MemberDetails memberDetails,
                              @PathVariable("id") Long itemId,
                              @RequestParam("count") @Range(min = 1, max = 10, message = "주문은 1개 이상 10개 이하까지만 가능합니다.") int count) {
        cartService.addItemToCart(memberDetails.getUsername(), itemId, count);
        return "redirect:/items";
    }

    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal MemberDetails memberDetails,
                       Model model) {
        Cart cart = cartService.findCart(memberDetails.getUsername());
        List<CartItem> cartItems = cart.getCartItems();
//        for (CartItem cartItem : cartItems) {
//            totalPrice += cartItem.getTotalPrice();
//        }
        int cartTotalPrice = cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
        List<CartItemDto> results = cartItems.stream()
                .map(c -> new CartItemDto(c))
                .collect(Collectors.toList());
        model.addAttribute("results", results);
        model.addAttribute("cartTotalPrice", cartTotalPrice);
        return "cart/cartItemList";
    }

    @Data
    static class CartItemDto {
        private String itemName;
        private int itemCount;
        private int itemTotalPrice;

        public CartItemDto(CartItem cartItem) {
            itemName = cartItem.getItem().getName();
            itemCount = cartItem.getCount();
            itemTotalPrice = cartItem.getTotalPrice();
        }
    }
}
