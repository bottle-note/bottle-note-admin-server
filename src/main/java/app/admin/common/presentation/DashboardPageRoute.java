package app.admin.common.presentation;

import app.admin.common.model.DashboardStats;
import app.admin.common.application.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardPageRoute {

  private final DashboardService dashboardService;

  @GetMapping
  public String dashboard(Model model) {
    model.addAttribute("pageTitle", "");
    model.addAttribute("stats", dashboardService.getDashboardStats());
    return "dashboard/index";
  }

}
