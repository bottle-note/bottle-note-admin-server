package app.admin.common.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {

    @GetMapping
    public String inquiryList(Model model) {
        model.addAttribute("pageTitle", "문의 관리");
        return "common/not-implemented";
    }
}
