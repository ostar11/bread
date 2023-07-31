package freshbread.bread.service;

import static org.junit.jupiter.api.Assertions.*;

import freshbread.bread.domain.item.Item;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ItemServiceTest {

    @Autowired ItemService itemService;

    @Test
    void 상품_등록() throws Exception {
        //given
        Item item = createItem("크로와상", 1000, 20, "고급 버터를 사용한 크로와상");
        itemService.saveItem(item);

        //when
        Item findItem = itemService.findOne(item.getId());

        //then
        assertEquals(item.getName(), findItem.getName());
    }

    private Item createItem(String itemName, int price, int stockQuantity, String itemDetails) {
        return new Item(itemName, price, stockQuantity, itemDetails);
    }

}