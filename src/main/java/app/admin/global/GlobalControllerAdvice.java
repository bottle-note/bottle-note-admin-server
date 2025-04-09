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
  }
}
