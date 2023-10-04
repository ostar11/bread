package freshbread.bread.controller;

import freshbread.bread.config.MemberDetails;
import freshbread.bread.domain.Notification;
import freshbread.bread.dto.NotificationDto;
import freshbread.bread.dto.NotificationDto.Response;
import freshbread.bread.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String NotReadNotifications(@AuthenticationPrincipal MemberDetails memberDetails, Model model) {
        String username = memberDetails.getUsername();
//        List<Notification> notifications = notificationService.getNotifications(username);
        List<Response> notifications = notificationService.getNotReadNotifications(username);
        model.addAttribute("notifications", notifications);
        return "notification/notificationList";
    }

//    @GetMapping("notifications/{id}")
//    public String NotificationDetail(@PathVariable("id") Long id, Model model) {
//        Notification notification = notificationService.findOne(id);
//        notificationService.markAsCheck(notification);
//        model.addAttribute("notification", notification);
//        return "notification/notificationDetail";
//    }

    @GetMapping("notifications/{id}")
    public String NotificationDetailV2(@PathVariable("id") Long id, Model model) {
        Notification notification = notificationService.findOne(id);
        notificationService.markAsCheck(notification);
        Response response = NotificationDto.Response.createResponse(notification);
        model.addAttribute("response", response);
        return "notification/notificationDetail";
    }


}
