package freshbread.bread.repository;

import static org.junit.jupiter.api.Assertions.*;

import freshbread.bread.domain.item.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품 추가")
    void addItem() {
        //given
        Item item = new Item("bread", 1000, "bread detail");
        //when
        itemRepository.save(item);
        //then
    }

}