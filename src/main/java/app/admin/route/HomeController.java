package app.admin.route;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/not-implemented")
    public String notImplemented(Model model) {
        model.addAttribute("pageTitle", "준비 중");
        return "common/not-implemented";
    }
}
