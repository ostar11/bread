package freshbread.bread.domain.item;

import freshbread.bread.domain.ItemStatus;
import freshbread.bread.exception.NotEnoughStockException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    private String details;
    private LocalDateTime created_date;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }

}
