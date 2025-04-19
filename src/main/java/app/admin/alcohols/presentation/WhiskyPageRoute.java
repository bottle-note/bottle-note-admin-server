package app.admin.alcohols.presentation;

import app.admin.alcohols.application.WhiskyService;
import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import app.admin.alcohols.constant.SearchSortType;
import app.admin.alcohols.domain.Whisky;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/whisky")
@RequiredArgsConstructor
public class WhiskyPageRoute {

    private final WhiskyService whiskyService;

    /**
     * 위스키 목록 페이지를 조회합니다.
     */
    @GetMapping
    public String getWhiskyListPage(
            @RequestParam(required = false) AlcoholType type,
            @RequestParam(required = false) AlcoholCategoryGroup categoryGroup,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) SearchSortType sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Alcohol> whiskies = whiskyService.getFilteredWhiskies(
                type,
                categoryGroup,
                search,
                sortBy,
                PageRequest.of(page, size)
        );

        model.addAttribute("whiskies", whiskies.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", whiskies.getTotalPages());
        model.addAttribute("alcoholTypes", AlcoholType.values());
        model.addAttribute("categoryGroups", AlcoholCategoryGroup.values());
        model.addAttribute("sortTypes", SearchSortType.values());
        model.addAttribute("type", type);
        model.addAttribute("categoryGroup", categoryGroup);
        model.addAttribute("search", search);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("pageTitle", "위스키 목록");

        return "whisky/list";
    }

    /**
     * 위스키 상세 페이지를 조회합니다.
     */
    @GetMapping("/{id}")
    public String getWhiskyDetailPage(@PathVariable Long id, Model model) {
        Optional<Alcohol> whiskyOpt = whiskyService.getWhiskyById(id);

        if (whiskyOpt.isEmpty()) {
            return "redirect:/whisky";
        }

        model.addAttribute("whisky", whiskyOpt.get());
        model.addAttribute("pageTitle", "위스키 상세");
        return "whisky/detail";
    }

    /**
     * 위스키 추가 폼 페이지를 조회합니다.
     */
    @GetMapping("/add")
    public String getWhiskyAddForm(Model model) {
        model.addAttribute("pageTitle", "위스키 추가");
        return "whisky/form";
    }

    /**
     * 위스키 수정 폼 페이지를 조회합니다.
     */
    @GetMapping("/edit/{id}")
    public String getWhiskyEditForm(@PathVariable Long id, Model model) {
        Optional<Alcohol> whiskyOpt = whiskyService.getWhiskyById(id);

        if (whiskyOpt.isEmpty()) {
            return "redirect:/whisky";
        }

        model.addAttribute("whisky", whiskyOpt.get());
        model.addAttribute("pageTitle", "위스키 수정");
        return "whisky/form";
    }

    /**
     * 새 위스키를 추가합니다.
     */
    @PostMapping("/add")
    public String addWhisky(@ModelAttribute Alcohol whisky, RedirectAttributes redirectAttributes) {
        whiskyService.createWhisky(whisky);
        redirectAttributes.addFlashAttribute("successMessage", "위스키가 성공적으로 추가되었습니다.");
        return "redirect:/whisky";
    }

    /**
     * 위스키 정보를 수정합니다.
     */
    @PostMapping("/edit/{id}")
    public String updateWhisky(@PathVariable Long id, @ModelAttribute Alcohol whiskyData, RedirectAttributes redirectAttributes) {
        Optional<Alcohol> existingWhiskyOpt = whiskyService.getWhiskyById(id);

        if (existingWhiskyOpt.isEmpty()) {
            return "redirect:/whisky";
        }

        // 기존 위스키 정보를 가져와서 새 데이터로 업데이트된 위스키 객체 생성
        Alcohol updatedWhisky = Alcohol.builder()
                .id(id)
                .korName(whiskyData.getKorName())
                .engName(whiskyData.getEngName())
                .abv(whiskyData.getAbv())
                .type(whiskyData.getType())
                .korCategory(whiskyData.getKorCategory())
                .engCategory(whiskyData.getEngCategory())
                .categoryGroup(whiskyData.getCategoryGroup())
                .region(whiskyData.getRegion())
                .distillery(whiskyData.getDistillery())
                .cask(whiskyData.getCask())
                .imageUrl(whiskyData.getImageUrl())
                .alcoholsTastingTags(whiskyData.getAlcoholsTastingTags())
                .build();

        whiskyService.updateWhisky(updatedWhisky);
        redirectAttributes.addFlashAttribute("successMessage", "위스키가 성공적으로 수정되었습니다.");
        return "redirect:/whisky/" + id;
    }

    /**
     * 위스키를 삭제합니다.
     */
    @PostMapping("/delete/{id}")
    public String deleteWhisky(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        whiskyService.deleteWhisky(id);
        redirectAttributes.addFlashAttribute("successMessage", "위스키가 성공적으로 삭제되었습니다.");
        return "redirect:/whisky";
    }
}
