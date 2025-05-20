package app.admin.alcohols.presentation;

import app.admin.alcohols.application.DistilleryService;
import app.admin.alcohols.application.RegionService;
import app.admin.alcohols.application.TastingTagService;
import app.admin.alcohols.application.WhiskyService;
import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import app.admin.alcohols.constant.SearchSortType;
import app.admin.alcohols.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/whisky")
@RequiredArgsConstructor
public class WhiskyPageRoute {

    private final WhiskyService whiskyService;
    private final RegionService regionService;
    private final DistilleryService distilleryService;
    private final TastingTagService tastingTagService;
    private final TastingTagRepository tastingTagRepository;
    private final WhiskysTastingTagsRepository whiskysTastingTagsRepository;


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

        Page<Whisky> whiskies = whiskyService.getFilteredWhiskies(
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
    public String getWhiskyDetailPage(
            @PathVariable Long id,
            Model model
    ) {
        Optional<Whisky> whiskyOpt = whiskyService.getWhiskyById(id);
        if (whiskyOpt.isEmpty()) {
            return "redirect:/whisky";
        }

        Whisky whisky = whiskyOpt.get();
        model.addAttribute("whisky", whisky);
        model.addAttribute("pageTitle", "위스키 상세");

        // regionId → korName 조회
        if (whisky.getRegionId() != null) {
            Region region = regionService.getRegionById(whisky.getRegionId());
            model.addAttribute("regionKorName", region.getKorName());

        }

        // distilleryId → korName 조회
        if (whisky.getDistilleryId() != null) {
            Distillery distillery = distilleryService.getDistilleryById(whisky.getDistilleryId());
            model.addAttribute("distilleryKorName", distillery.getKorName());
        }

        return "whisky/detail";
    }

    //    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("whisky", new Whisky());
//        model.addAttribute("regions", regionService.getAllRegions());
//        model.addAttribute("distilleries", distilleryService.getAllDistilleries());
//        model.addAttribute("tastingTags", tastingTagRepository.findAll());
//        model.addAttribute("selectedTastingTagIds", List.of()); // 신규 등록 시 빈 리스트
//        return "whisky/form";
//    }
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("whisky", new Whisky());
        model.addAttribute("regions", regionService.getAllRegions());
        // ← 이 줄이 빠져 있으면 뷰에서 distilleries 변수가 없어서 폼이 렌더되지 않습니다.
        model.addAttribute("distilleries", distilleryService.getAllDistilleries());
        model.addAttribute("tastingTags", tastingTagRepository.findAll());
        model.addAttribute("selectedTastingTagIds", Collections.emptyList());
        return "whisky/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        //Whisky whisky = whiskyRepository.findById(id).orElseThrow();
        Whisky whisky = whiskyService.getWhiskyById(id).orElseThrow();
        model.addAttribute("whisky", whisky);
        model.addAttribute("regions", regionService.getAllRegions());
        model.addAttribute("distilleries", distilleryService.getAllDistilleries());
        model.addAttribute("tastingTags", tastingTagRepository.findAll());
        model.addAttribute("selectedTastingTagIds",
                whisky.getAlcoholsTastingTags().stream()
                        .map(rel -> rel.getTastingTag().getId())
                        .toList()
        );
        return "whisky/form";
    }

    /**
     * 새 위스키를 추가합니다.
     */
    @Transactional
    @PostMapping("/add")
    public String addWhisky(
            @ModelAttribute Whisky whisky,
            @RequestParam(value = "tastingTagIds", required = false) List<String> tastingTagIds,
            RedirectAttributes redirectAttributes) {

        System.out.println("whisky.getDistilleryId() = " + whisky.getDistilleryId());
        System.out.println("whisky.getRegionId() = " + whisky.getRegionId());

        whiskyService.createWhiskyWithTags(whisky, tastingTagIds);

        redirectAttributes.addFlashAttribute("successMessage", "위스키가 성공적으로 추가되었습니다.");
        return "redirect:/whisky";
    }

    /**
     * 위스키 정보를 수정합니다.
     */
    @Transactional
    @PostMapping("/edit/{id}")
    public String updateWhisky(
            @PathVariable Long id,
            @ModelAttribute Whisky whiskyData,
            @RequestParam(value = "tastingTagIds", required = false) List<String> tastingTagIds,
            RedirectAttributes redirectAttributes) {

        Optional<Whisky> existingWhiskyOpt = whiskyService.getWhiskyById(id);

        if (existingWhiskyOpt.isEmpty()) {
            return "redirect:/whisky";
        }

        // 기존 위스키 정보를 가져와서 새 데이터로 업데이트된 위스키 객체 생성
        Whisky updatedWhisky = Whisky.builder()
                .id(id)
                .korName(whiskyData.getKorName())
                .engName(whiskyData.getEngName())
                .abv(whiskyData.getAbv())
                .type(whiskyData.getType())
                .korCategory(whiskyData.getKorCategory())
                .engCategory(whiskyData.getEngCategory())
                .categoryGroup(whiskyData.getCategoryGroup())
                .regionId(whiskyData.getRegionId())
                .distilleryId(whiskyData.getDistilleryId())
                .cask(whiskyData.getCask())
                .imageUrl(whiskyData.getImageUrl())
                .build();

        // 위스키 정보 업데이트
        Whisky savedWhisky = whiskyService.updateWhisky(updatedWhisky);

        // 기존 테이스팅 태그 연관관계 삭제
        whiskysTastingTagsRepository.deleteByWhiskyId(id);

        // 새로운 테이스팅 태그 연관관계 생성
        if (tastingTagIds != null && !tastingTagIds.isEmpty()) {
            for (String tagIdStr : tastingTagIds) {
                try {
                    // 태그 ID가 숫자인 경우 (기존 태그)
                    if (tagIdStr.matches("\\d+")) {
                        Long tagId = Long.parseLong(tagIdStr);
                        tastingTagRepository.findById(tagId).ifPresent(tastingTag -> {
                            WhiskysTastingTags whiskysTastingTags = WhiskysTastingTags.createWhiskyTastingTag(savedWhisky, tastingTag);
                            whiskysTastingTagsRepository.save(whiskysTastingTags);
                        });
                    } else {
                        // 태그 ID가 숫자가 아닌 경우 (새로운 태그 텍스트)
                        // 현재 구현에서는 새 태그를 생성하지 않고 무시합니다.
                        // 필요하다면 여기에 새 태그 생성 로직을 추가할 수 있습니다.
                    }
                } catch (NumberFormatException e) {
                    // 숫자 변환 실패 시 무시
                }
            }
        }

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
