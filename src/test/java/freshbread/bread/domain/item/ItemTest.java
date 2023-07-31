package freshbread.bread.domain.item;

import static org.junit.jupiter.api.Assertions.*;

import freshbread.bread.exception.NotEnoughStockException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ItemTest {

    @Test
    void 재고_증가() throws Exception {
        //given
        Item item = createItem("식빵", 2000, 10, "우유로 만든 기본식빵");
        //when
        item.addStock(15);
        //then
        assertEquals(item.getStockQuantity(), 25);
    }

    @Test
    void 재고_감소() throws Exception {
        //given
        Item item = createItem("식빵", 2000, 10, "우유로 만든 기본식빵");
        //when
        item.removeStock(5);
        //then
        assertEquals(item.getStockQuantity(), 5);
    }

    @Test
    void 재고_감소_예외() throws Exception {
        Item item = createItem("식빵", 2000, 10, "우유로 만든 기본식빵");
        assertThrows(NotEnoughStockException.class, () -> item.removeStock(20));
    }

    private Item createItem(String itemName, int price, int stockQuantity, String itemDetails) {
        return new Item(itemName, price, stockQuantity, itemDetails);
    }
}