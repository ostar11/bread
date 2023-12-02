package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.controller.dto.LoginForm;
import freshbread.bread.controller.dto.MemberForm;
import freshbread.bread.controller.dto.MemberInfoDto;
import freshbread.bread.domain.Member;
import freshbread.bread.service.CartService;
import freshbread.bread.service.MemberService;
import freshbread.bread.service.NotificationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final NotificationService notificationService;
    private final CartService cartService;

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

//        memberService.join2(form);
        Member member = memberService.join3(form);
//        cartService.assignCart(member);

        return "redirect:/";
    }

    @GetMapping("/member/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "exception", required = false) String exception,
                            Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginForm", new LoginForm());
        return "member/loginForm";
    }

    @GetMapping({"", "/"})
    public String home(Model model, Authentication auth) {
        if (auth != null) {
            Member member = memberService.getLoginUserByLoginId(auth.getName());
            if (member != null) {
                model.addAttribute("loginId", member.getLoginId());
            }
        }
        return "home";
    }

    @GetMapping("/member")
    public String memberHome(@AuthenticationPrincipal MemberDetails memberDetails,
                             @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                             Model model) {
        if (memberDetails != null) {
            Member member = memberService.getLoginUserByLoginId(memberDetails.getUsername());
            if (member != null) {
                model.addAttribute("loginId", member.getLoginId());
                model.addAttribute("role", member.getRole());
            }
        }
        log.info("로그인 완료 - MemberController::memberHome");
        SseEmitter sse = notificationService.subscribe(memberDetails.getUsername(), lastEventId);
        log.info("sse 연결 완료 - MemberController::memberHome");
        return "member/memberHome";
    }

    @GetMapping("/admin")
    public String adminHome(Model model, Authentication auth) {
        if (auth != null) {
            Member member = memberService.getLoginUserByLoginId(auth.getName());
            if (member != null) {
                model.addAttribute("role", member.getRole());
                model.addAttribute("loginId", member.getLoginId());
            }
        }
        return "member/adminHome";
    }

    @GetMapping("/member/info")
    public String memberInfo(Model model, @AuthenticationPrincipal MemberDetails memberDetails) {
        String loginId = memberDetails.getUsername();
        MemberInfoDto memberInfoDto = memberService.findMemberInfo(loginId);
        model.addAttribute("form", memberInfoDto);
        return "member/info";
    }
}
