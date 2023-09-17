package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.item.Item;
import freshbread.bread.service.ItemService;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class adminController {

    private final ItemService itemService;

    @GetMapping("/items")
    public String manageHome(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "item/itemList";
    }

    @GetMapping("/items/new")
    public String createItem(Model model) {
        model.addAttribute("itemForm", new ItemForm());
        return "item/createItemForm";
    }

    @PostMapping("/items/new")
    public String addItem(@Valid @ModelAttribute ItemForm itemForm, BindingResult bindingResult) {

        if (itemService.checkItemNameDuplicate(itemForm.getName())) {
            bindingResult.addError(new FieldError("itemForm", "name", "이미 사용중인 상품명입니다."));
        }

        if (bindingResult.hasErrors()) {
            return "item/createItemForm";
        }

        itemService.saveItemForm(itemForm);

        return "redirect:/admin/items";
    }
}
