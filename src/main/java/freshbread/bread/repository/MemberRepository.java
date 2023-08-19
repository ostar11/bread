package freshbread.bread.repository;

import freshbread.bread.domain.Member;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByLoginId(Long memberId) {
        return em.find(Member.class, memberId);
    }

    // 이름 -> 아이디
    public List<Member> findByName(String name) {
        return em.createQuery(
                        "select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public boolean existsByLoginId(String loginId) {
        List<Member> members = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();

        return !members.isEmpty();
    }

    public boolean existsByPhoneNumber(String phoneNUmber) {
        List<Member> members = em.createQuery("select m from Member m where m.phoneNumber = :phoneNumber",
                        Member.class)
                .setParameter("phoneNumber", phoneNUmber)
                .getResultList();

        return !members.isEmpty();
    }
}
