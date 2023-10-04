package freshbread.bread;

import freshbread.bread.domain.Address;
import freshbread.bread.domain.Member;
import freshbread.bread.domain.item.Item;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.createAdminAndCustomer();
        initService.createItems();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final PasswordEncoder encoder;

        public void createAdminAndCustomer() {
            Address adminAddress = createAddress("지금 나온 도시", "지금 나온 길", "지금 나온 집");
            Member admin = Member.createAdmin("admin", encoder.encode("1234"), "관리자", "010", adminAddress);
            em.persist(admin);

            Address memberAddress1 = createAddress("jw 도시", "jw 길", "jw 집");
            Member member1 = new Member("jjw", encoder.encode("1234"), "jw", "111", memberAddress1);
            em.persist(member1);

            Address memberAddress2 = createAddress("cj 도시", "cj 길", "cj 집");
            Member member2 = new Member("cjw", encoder.encode("1234"), "cj", "222", memberAddress2);
            em.persist(member2);
        }

        public void createItems() {
            Item item1 = createItem("먹물소세지빵", 2300, "먹물 반죽을 이용해 더 고소한 소세지빵");
            item1.addStock(10);
            item1.startSale();
            em.persist(item1);
            Item item2 = createItem("메론빵", 1700, "메론 향을 입힌 달콤하고 촉촉한 빵");
            item2.addStock(10);
            item2.startSale();
            em.persist(item2);
            Item item3 = createItem("크림단팥빵", 1800, "단팥의 텁텁함을 크림의 촉촉함으로 더 부드럽고 달달한 빵'");
            item3.addStock(8);
            item3.startSale();
            em.persist(item3);
            Item item4 = createItem("앙버터크로와상", 3000, "앙버터는 내가 싫어하는 빵'");
            item4.addStock(6);
            item4.startSale();
            em.persist(item4);
        }

        private Address createAddress(String city, String street, String zipcode) {
            return new Address(city, street, zipcode);
        }

        private Item createItem(String itemName, int price, String details) {
            return new Item(itemName, price, details);
        }
    }
}
