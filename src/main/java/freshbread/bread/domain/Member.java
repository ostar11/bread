package freshbread.bread.domain;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdDate;

    public Member(String loginId, String password, String name, String phoneNumber, Address address) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = Role.CUSTOMER;
        this.createdDate = LocalDateTime.now();
    }

//    private Member(String loginId, String password, String name, String phoneNumber, Address address) {
//        this.loginId = loginId;
//        this.password = password;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//        this.createdDate = LocalDateTime.now();
//    }

    public static Member createAdmin(String loginId, String password, String name, String phoneNumber, Address address) {
        Member admin = new Member(loginId, password, name, phoneNumber, address);
        admin.roleUp();
        return admin;
    }

    private void roleUp() {
        this.role = Role.ADMIN;
    }

}
