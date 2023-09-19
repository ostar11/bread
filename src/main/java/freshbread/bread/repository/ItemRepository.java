package freshbread.bread.repository;

import freshbread.bread.domain.ItemStatus;
import freshbread.bread.domain.item.Item;
import freshbread.bread.exception.NoStockQuantityException;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public Optional<Item> findItemWithOnSale(Long id) {
        List<Item> item = em.createQuery("select i from Item i where i.id = :id and i.status = :status",
                        Item.class)
                .setParameter("id", id)
                .setParameter("status", ItemStatus.ON_SALE).
                getResultList();
        return item.stream().findAny();
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    public boolean existsByItemName(String itemName) {
        List<Item> items = em.createQuery("select i from Item i where i.name = :itemName", Item.class).
                setParameter("itemName", itemName)
                .getResultList();
        return !items.isEmpty();
    }
}
