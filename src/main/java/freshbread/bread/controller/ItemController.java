package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.item.Item;
import freshbread.bread.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {

    private final ItemService itemService;

    /**
     * 전체 메뉴 조회
     */
    @GetMapping
    public String itemHome(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "item/itemList";
    }

    @GetMapping("/{itemId}")
    public String itemView(@PathVariable("itemId") Long itemId, Authentication auth, Model model) {
        auth.getAuthorities();
        Item item = itemService.findOne(itemId);
        model.addAttribute(item);
        return "item/itemDetails";
    }
}
