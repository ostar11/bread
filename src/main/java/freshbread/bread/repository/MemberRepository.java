package freshbread.bread.repository;

import freshbread.bread.domain.Member;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    // 이름 전화번호 -> 아이디
    public List<Member> findByNamePhoneNumber(String name, String phoneNumber) {
        return em.createQuery(
                        "select m from Member m where m.name = :name and m.phoneNumber = :phoneNumber", Member.class)
                .setParameter("name", name)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }
}
