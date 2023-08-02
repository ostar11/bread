package freshbread.bread.controller;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;
    @NotEmpty(message = "비밀번호는 필수 입니다.")
    private String password;
    @NotEmpty(message = "전화번호는 필수 입니다.")
    private String phoneNumber;

    private String city;
    private String street;
    private String zipcode;
}
