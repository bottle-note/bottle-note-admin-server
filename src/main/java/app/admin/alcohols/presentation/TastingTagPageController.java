package app.admin.alcohols.presentation;

import app.admin.alcohols.application.TastingTagService;
import app.admin.alcohols.domain.TastingTag;
import app.admin.alcohols.dto.TastingTagRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/whisky/tasting-tag/add")
@RequiredArgsConstructor
public class TastingTagPageController {

    private final TastingTagService tastingTagService;

    @GetMapping
    public String showTagListPage(Model model,
                                  @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<TastingTag> tagPage = tastingTagService.getFilteredTastingTags(pageable);

        model.addAttribute("tags", tagPage.getContent());
        model.addAttribute("currentPage", tagPage.getNumber());
        model.addAttribute("totalPages", tagPage.getTotalPages());
        model.addAttribute("size", tagPage.getSize());
        model.addAttribute("newTag", TastingTagRequestDto.createEmpty());

        return "tastingtag/list";
    }

    /**
     * 태그 수정 페이지
     */
    @GetMapping("/edit/{id}")
    public String showEditTagPage(@PathVariable Long id, Model model) {
        TastingTag tag = tastingTagService.findById(id);
        model.addAttribute("tag", tag);
        return "tastingtag/edit";
    }

    @PostMapping
    public String addTag(
            @Valid @ModelAttribute("newTag") TastingTagRequestDto newTagDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            List<TastingTag> tags = tastingTagService.getAllTastingTags();
            model.addAttribute("tags", tags);
            return "tastingtag/list";
        }

        tastingTagService.addTag(newTagDto);

        return "redirect:/whisky/tasting-tag/add";
    }
}
