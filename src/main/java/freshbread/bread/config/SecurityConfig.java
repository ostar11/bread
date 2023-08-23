package freshbread.bread.config;

import freshbread.bread.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/member", "/admin").authenticated()
                .antMatchers("/member/new", "/member/login").permitAll()
                .antMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                .antMatchers("/member/**").hasAuthority(Role.CUSTOMER.name())
                .and()
                .formLogin()
                .loginPage("/member/login")
                .usernameParameter("loginId")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/login");



        return http.build();
    }
}