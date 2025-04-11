package app.admin.global;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

  @ModelAttribute
  public void addAttributes(Model model, HttpServletRequest request) {
    model.addAttribute("currentUrl", request.getRequestURI());
    model.addAttribute("currentPath", request.getServletPath());
    model.addAttribute("currentQueryString", request.getQueryString());
    model.addAttribute("currentMethod", request.getMethod());
  }

  @ModelAttribute
  public void addPageTitle(Model model) {
    String pageTitle = (String) model.getAttribute("pageTitle");
    if (pageTitle == null) {
      pageTitle = "관리자 페이지";
    }
    model.addAttribute("pageTitle", pageTitle);
  }
}
