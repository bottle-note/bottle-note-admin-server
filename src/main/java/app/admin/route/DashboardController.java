package app.admin.route;

import app.admin.model.DashboardStats;
import app.admin.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  private final DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @GetMapping
  public String dashboard(Model model) {
    model.addAttribute("pageTitle", "");
    model.addAttribute("stats", dashboardService.getDashboardStats());
    return "dashboard/index";
  }

  @GetMapping("/api/stats")
  @ResponseBody
  public DashboardStats getDashboardStats() {
    return dashboardService.getDashboardStats();
  }
}
