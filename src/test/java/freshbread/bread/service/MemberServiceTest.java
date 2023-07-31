package freshbread.bread.service;

import static org.junit.jupiter.api.Assertions.*;

import freshbread.bread.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() throws Exception {
        Member member = new Member("member1", "1234", "010-1111", "busan", "12", "13");

        Long saveId = memberService.join(member);

        assertEquals(member.getId(), saveId);
    }

    @Test
    void 중복_이름_회원가입() throws Exception {
        Member member1 = new Member("member1", "1234", "010-1111", "busan", "12", "13");
        memberService.join(member1);

        Member member2 = new Member("member1", "1234", "010-2222", "busan", "12", "13");

        assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
    }
}