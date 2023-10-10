package freshbread.bread.domain;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import freshbread.bread.domain.item.Item;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class CartItem {

    @Id @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int cartPrice;
    private int count;

    private CartItem(Item item, int cartPrice, int count) {
        this.item = item;
        this.cartPrice = cartPrice;
        this.count = count;
    }

    public static CartItem createCartItem(Item item, int price, int count) {
        return new CartItem(item, price, count);
    }

    public void registerCart(Cart cart) {
        this.cart = cart;
    }

    public int getTotalPrice() {
        return getCartPrice() * getCount();
    }
}
