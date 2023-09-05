package freshbread.bread.controller;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter @Setter
@NoArgsConstructor
public class ItemForm {

    @NotBlank(message = "상품 이름을 입력해주세요.")
    private String name;
    @Range(min = 0, max = 100000, message = "가격을 입력해주세요.")
    private int price;
    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String details;

}
