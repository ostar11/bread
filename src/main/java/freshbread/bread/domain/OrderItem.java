package freshbread.bread.domain;

import static javax.persistence.FetchType.LAZY;

import freshbread.bread.domain.item.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order orders;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item items;

    private int orderPrice;
    private int count;
}
