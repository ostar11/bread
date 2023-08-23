package freshbread.bread.service;

import freshbread.bread.config.CustomUserDetails;
import freshbread.bread.domain.Member;
import freshbread.bread.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(username);
        if(member == null) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다.");
        }
        return new CustomUserDetails(member);
    }
}
