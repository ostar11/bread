package freshbread.bread.controller;

import freshbread.bread.domain.Address;
import freshbread.bread.domain.Member;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberForm {

    @NotBlank(message = "아이디는 필수입니다.")
    private String loginId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    private String city;
    private String street;
    private String zipcode;

    // 비밀번호 암호화
    public Member toEntity(String encodedPassword) {
        return new Member(loginId, encodedPassword, name, phoneNumber, new Address(city, street, zipcode));
    }

}
