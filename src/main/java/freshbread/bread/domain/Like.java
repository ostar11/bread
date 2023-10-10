package freshbread.bread.domain;

import static javax.persistence.FetchType.*;

import freshbread.bread.domain.item.Item;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Like {
//
//    @Id @GeneratedValue
//    @Column(name = "like_id")
//    private Long id;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "item_id")
//    private Item item;
//
//    private LocalDateTime createdDate;
//
//    public Like(Member member, Item item) {
//        this.member = member;
//        this.item = item;
//        this.createdDate = LocalDateTime.now();
//    }
//
//}