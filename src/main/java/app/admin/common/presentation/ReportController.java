package app.admin.common.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {

    @GetMapping
    public String reportList(Model model) {
        model.addAttribute("pageTitle", "신고 관리");
        return "common/not-implemented";
    }
}
