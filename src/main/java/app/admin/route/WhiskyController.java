package app.admin.route;

import java.util.List;
import java.util.Optional;

import app.admin.model.Whisky;
import app.admin.service.WhiskyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/whisky")
public class WhiskyController {

  private final WhiskyService whiskyService;

  @Autowired
  public WhiskyController(WhiskyService whiskyService) {
    this.whiskyService = whiskyService;
  }

  @GetMapping
  public String list(
      Model model,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "name") String sortBy) {
    model.addAttribute("pageTitle", "위스키 관리");
    model.addAttribute("whiskies", whiskyService.getFilteredWhiskies(category, search, sortBy));
    return "whisky/list";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("pageTitle", "위스키 추가");
    model.addAttribute("whisky", new Whisky());
    return "whisky/form";
  }

  @PostMapping("/add")
  public String add(@ModelAttribute Whisky whisky) {
    whiskyService.addWhisky(whisky);
    return "redirect:/whisky";
  }

  @GetMapping("/edit/{id}")
  public String editForm(@PathVariable Long id, Model model) {
    Optional<Whisky> whisky = whiskyService.getWhiskyById(id);

    if (whisky.isPresent()) {
      model.addAttribute("pageTitle", "위스키 수정");
      model.addAttribute("whisky", whisky.get());
      return "whisky/form";
    } else {
      return "redirect:/whisky";
    }
  }

  @PostMapping("/edit/{id}")
  public String update(@PathVariable Long id, @ModelAttribute Whisky whisky) {
    whiskyService.updateWhisky(id, whisky);
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
  public List<Whisky> getWhiskyList(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) String search,
      @RequestParam(required = false, defaultValue = "name") String sortBy) {
    return whiskyService.getFilteredWhiskies(category, search, sortBy);
  }

  @GetMapping("/api/{id}")
  @ResponseBody
  public ResponseEntity<Whisky> getWhisky(@PathVariable Long id) {
    Optional<Whisky> whisky = whiskyService.getWhiskyById(id);

    return whisky.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
