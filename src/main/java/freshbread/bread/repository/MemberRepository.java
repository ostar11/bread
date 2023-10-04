package freshbread.bread.repository;

import freshbread.bread.domain.Member;
import freshbread.bread.domain.Role;
import java.util.List;
import java.util.Optional;
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

    public Member findById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public Optional<Member> findByLoginId(String loginId) {
        // getSingleResult() -> NoResultException 발생
        List<Member> members = em.createQuery("select m from Member m where loginId = :loginId", Member.class)
                .setParameter("loginId", loginId)
                .getResultList();
        return members.stream().findAny();
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

    public boolean existsByPhoneNumber(String phoneNumber) {
        List<Member> members = em.createQuery("select m from Member m where m.phoneNumber = :phoneNumber",
                        Member.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();

        return !members.isEmpty();
    }

    public List<Member> findByRole(Role role) {
        return em.createQuery("select m from Member m where m.role = :role", Member.class)
                .setParameter("role", role)
                .getResultList();
    }
}
