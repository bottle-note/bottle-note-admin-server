package app.admin.alcohols.application;

import app.admin.alcohols.domain.TastingTag;
import app.admin.alcohols.domain.TastingTagRepository;
import app.admin.alcohols.dto.TastingTagRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TastingTagService {

    private final TastingTagRepository tastingTagRepository;

    /**
     * 모든 테이스팅 태그 목록을 조회합니다.
     */
    public Page<TastingTag> getFilteredTastingTags(Pageable pageable) {
        return tastingTagRepository.findAll(pageable);
    }

    public List<TastingTag> getAllTastingTags() {
        return tastingTagRepository.findAll();
    }

    /**
     * ID로 테이스팅 태그를 조회합니다.
     */
    public TastingTag findById(Long id) {
        return tastingTagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 태그가 존재하지 않습니다: " + id));
    }

    /**
     * 테이스팅 태그를 추가합니다.
     *
     * @param requestDto 추가할 태그의 정보가 담긴 DTO
     */
    @Transactional
    public void addTag(TastingTagRequestDto requestDto) {
        TastingTag newTag = TastingTag.builder()
                .engName(requestDto.engName())
                .korName(requestDto.korName())
                .icon(null)
                .description(requestDto.description())
                .build();
        tastingTagRepository.save(newTag);
    }

}