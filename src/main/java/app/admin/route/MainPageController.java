package app.admin.route;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageController {
  @GetMapping("/")
  public String dashboard(Model model) {
    model.addAttribute("whiskeyCount", 150);
    model.addAttribute("userCount", 1250);
    model.addAttribute("reviewCount", 4320);
    return "main";
  }

  @GetMapping("/whisky/list")
  public String whiskyList(
      @RequestParam(required = false) String searchType,
      @RequestParam(required = false) String keyword,
      Model model) {
    // 여기에 실제 페이지네이션, 검색, 정렬 로직 구현
    // 임시 데이터로 예시
    //Page<WhiskyDto> whiskyPage = whiskyService.getWhiskyList(searchType, keyword, pageable);
    //model.addAttribute("whiskyPage", whiskyPage);
    return "whisky/list";
  }
}
