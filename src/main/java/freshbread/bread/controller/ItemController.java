package freshbread.bread.controller;

import freshbread.bread.domain.item.Item;
import freshbread.bread.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("menu")
public class ItemController {

    private final ItemService itemService;

    /**
     * 전체 메뉴 조회
     */
    @GetMapping
    public String menuHome(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "item/menuList";
    }

    @GetMapping("/{itemId}")
    public String itemView(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemService.findOne(itemId);
        model.addAttribute(item);
        return "item/itemDetails";
    }
}
