package freshbread.bread.controller;

import freshbread.bread.domain.item.Item;
import freshbread.bread.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("menu")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public String menuHome(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "item/menuList";
    }
}
