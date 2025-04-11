package app.admin.common.presentation;

import app.admin.common.application.DashboardService;
import app.admin.common.model.DashboardStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardApiController {
  private final DashboardService dashboardService;

  @GetMapping("/api/stats")
  @ResponseBody
  public DashboardStats getDashboardStats() {
    return dashboardService.getDashboardStats();
  }
}
