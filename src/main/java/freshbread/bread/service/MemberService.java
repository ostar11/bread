package freshbread.bread.service;

import freshbread.bread.controller.dto.MemberForm;
import freshbread.bread.domain.Cart;
import freshbread.bread.domain.Member;
import freshbread.bread.repository.CartRepository;
import freshbread.bread.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder encoder;

    /**
     * 회원가입1 : 비밀번호 암호화 x
     * 화면에서 MemberForm(loginId, password, name, phoneNumber, city, street, zipcode)을 입력받아 Member로 변환
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원가입 2 : 비밀번호 암호화 O
     * 화면에서 MemberForm(loginId, password, name, phoneNumber, city, street, zipcode)을 입력받아 Member로 변환
     * 추가적으로 password 를 암호화해서 저장한다.
     * loginId, phoneNumber 중복체크는 에러메시지를 표시해야하므로 Controller 에서 진행한다.
     */
    @Transactional
    public Long join2(MemberForm memberForm) {
        Member member = memberForm.toEntity(encoder.encode(memberForm.getPassword()));
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public Member join3(MemberForm memberForm) {
        Member member = memberForm.toEntity(encoder.encode(memberForm.getPassword()));
        Cart cart = new Cart();
        cart.enrollMember(member);
        memberRepository.save(member);
        cartRepository.save(cart);
        return member;
    }

    /**
     * loginId 중복체크
     */
    public boolean checkLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    /**
     * phoneNumber 중복체크
     */
    public boolean checkPhoneNumberDuplicate(String phoneNUmber) {
        return memberRepository.existsByPhoneNumber(phoneNUmber);
    }

    public Member getLoginUserByLoginId(String loginId) {
        if (loginId == null) {
            return null;
        }

        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if (member.isEmpty()) {
            return null;
        }

        return member.get();
    }
}
