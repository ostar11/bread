package freshbread.bread.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdDate;

    public Member(String name, String password, String phoneNumber, String city, String street, String zipcode) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = new Address(city, street, zipcode);
        this.role = Role.CUSTOMER;
        this.createdDate = LocalDateTime.now();
    }
}
