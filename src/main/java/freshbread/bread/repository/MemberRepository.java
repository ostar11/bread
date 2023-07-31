package freshbread.bread.repository;

import freshbread.bread.domain.Member;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    // 이름 전화번호 -> 아이디
    public List<Member> findByName(String name) {
        return em.createQuery(
                        "select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
