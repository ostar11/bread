package freshbread.bread.controller;

import freshbread.bread.domain.Address;
import freshbread.bread.domain.Member;
import freshbread.bread.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/member/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping("/member/new")
    public String addMember(@Valid @ModelAttribute MemberForm form, BindingResult bindingResult) {

        if (memberService.checkLoginIdDuplicate(form.getLoginId())) {
            bindingResult.addError(new FieldError("memberForm", "loginId", "이미 사용중인 아이디입니다."));
        }

        if (memberService.checkPhoneNumberDuplicate(form.getPhoneNumber())) {
            bindingResult.addError(new FieldError("memberForm", "phoneNumber", "이미 사용중인 전화번호 입니다."));
        }

        if (bindingResult.hasErrors()) {
            return "member/createMemberForm";
        }

        memberService.join2(form);

        return "redirect:/";
    }

    @GetMapping("/member/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

}
