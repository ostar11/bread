package freshbread.bread.controller.dto;

import freshbread.bread.domain.Address;
import freshbread.bread.domain.Member;
import lombok.Data;

@Data
public class MemberInfoDto {

    private String loginId;
    private String password;
    private String name;
    private String phoneNumber;
    private Address address;

    public MemberInfoDto(Member member) {
        loginId = member.getLoginId();
        password = member.getPassword();
        name = member.getName();
        phoneNumber = member.getPhoneNumber();
        address = member.getAddress();
    }
}
