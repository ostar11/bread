package freshbread.bread.exception;

import freshbread.bread.exception.NotEnoughStockException;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public String handleNotEnoughStockException(NotEnoughStockException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "재고 부족");
        model.addAttribute("message", e.getMessage());
        return "error/error";
    }

    @ExceptionHandler
    public String handleConstraintViolationException(ConstraintViolationException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "주문 수량 예외");
        model.addAttribute("message", e.getMessage());
        return "error/error";
    }

    @ExceptionHandler
    public String handleNoStockQuantityException(NoStockQuantityException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "재고 소진");
        model.addAttribute("message", e.getMessage());
        return "error/error";
    }

    @ExceptionHandler
    public String handleNoCartEntityException(NoCartEntityException e, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "시스템 오류");
        model.addAttribute("message", e.getMessage());
        return "error/error";
    }
}
