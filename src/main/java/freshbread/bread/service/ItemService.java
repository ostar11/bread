package freshbread.bread.service;

import freshbread.bread.controller.ItemForm;
import freshbread.bread.domain.item.Item;
import freshbread.bread.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void saveItemForm(ItemForm itemForm) {
        Item item = new Item(itemForm.getName(), itemForm.getPrice(), itemForm.getDetails());
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public boolean checkItemNameDuplicate(String itemName) {
        return itemRepository.existsByItemName(itemName);
    }
}
