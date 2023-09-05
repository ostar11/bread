package freshbread.bread.repository;

import freshbread.bread.domain.item.Item;
import java.util.List;
import javax.persistence.EntityManager;
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
