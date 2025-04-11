package app.admin.alcohols.presentation;

import java.util.List;
import java.util.Optional;

import app.admin.alcohols.model.WhiskyItem;
import app.admin.alcohols.application.WhiskyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/whisky")
public class WhiskyPageRoute {
  private final WhiskyService whiskyService;

  @GetMapping
  public String list(
      Model model,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "name") String sortBy) {

    model.addAttribute("whiskyItems", whiskyService.getFilteredWhiskies(category, search, sortBy));
    return "whisky/list";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("pageTitle", "위스키 추가");
    model.addAttribute("whisky", new WhiskyItem());
    return "whisky/form";
  }

  @PostMapping("/add")
  public String add(@ModelAttribute WhiskyItem whiskyItem) {
    whiskyService.addWhisky(whiskyItem);
    return "redirect:/whisky";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Optional<WhiskyItem> whisky = whiskyService.getWhiskyById(id);

    if (whisky.isPresent()) {
      model.addAttribute("pageTitle", "위스키 수정");
      model.addAttribute("whisky", whisky.get());
      return "whisky/form";
    } else {
      return "redirect:/whisky";
    }
  }

  @PostMapping("/edit/{id}")
  public String update(@PathVariable Long id, @ModelAttribute WhiskyItem whiskyItem) {
    whiskyService.updateWhisky(id, whiskyItem);
    return "redirect:/whisky";
  }

  @DeleteMapping("/delete/{id}")
  @ResponseBody
  public ResponseEntity<?> delete(@PathVariable Long id) {
    boolean deleted = whiskyService.deleteWhisky(id);

    if (deleted) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/api/list")
  @ResponseBody
  public List<WhiskyItem> getWhiskyList(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "name") String sortBy) {
    return whiskyService.getFilteredWhiskies(category, search, sortBy);
  }

  @GetMapping("/api/{id}")
  @ResponseBody
  public ResponseEntity<WhiskyItem> getWhisky(@PathVariable Long id) {
    Optional<WhiskyItem> whisky = whiskyService.getWhiskyById(id);

    return whisky.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
