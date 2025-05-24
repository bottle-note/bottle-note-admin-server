package app.admin.alcohols.application;

import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import app.admin.alcohols.constant.SearchSortType;
import app.admin.alcohols.domain.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhiskyService {

    private final WhiskyRepository whiskyRepository;
    private final TastingTagRepository tastingTagRepository;
    private final WhiskysTastingTagsRepository whiskysTastingTagsRepository;

    /**
     * 위스키를 저장하고, 전달된 tastingTagIds에 따라
     * 기존 태그는 조회·연결, (숫자가 아닌 경우) 새 태그 생성 후 연결합니다.
     */
    @Transactional
    public Whisky createWhiskyWithTags(Whisky whisky, List<String> tastingTagIds) {
        // 1) Region / Distillery 조회·세팅
        // 2) 기존 태그 관계 초기화
        whisky.clearTastingTagRelations();

        // 3) 먼저 Whisky 저장
        Whisky savedWhisky = whiskyRepository.save(whisky);

        // 4) 폼에서 넘어온 태그 아이디/텍스트로 관계 재구성
        if (tastingTagIds != null) {
            for (String idOrText : tastingTagIds) {
                if (idOrText.matches("\\d+")) {
                    // 기존 태그 조회
                    Long tagId = Long.parseLong(idOrText);
                    TastingTag tag = tastingTagRepository.findById(tagId)
                            .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + tagId));
                    WhiskysTastingTags rel = WhiskysTastingTags.createWhiskyTastingTag(savedWhisky, tag);
                    whiskysTastingTagsRepository.save(rel);
                } else {
                    // TODO: 새 태그 생성 로직 (엔티티에 Builder/Setter 추가 필요)
                    // TastingTag newTag = new TastingTag(...);
                    // tastingTagRepository.save(newTag);
                    // WhiskysTastingTags rel = WhiskysTastingTags.createWhiskyTastingTag(savedWhisky, newTag);
                    // whiskysTastingTagsRepository.save(rel);
                }
            }
        }

        return savedWhisky;
    }

    /**
     * WhiskysTastingTags 저장 & 연관관계 세팅
     **/
    private void saveRelation(Whisky whisky, TastingTag tag) {
        WhiskysTastingTags rel = WhiskysTastingTags.createWhiskyTastingTag(whisky, tag);
        whiskysTastingTagsRepository.save(rel);
        // 양방향이 필요하면:
        // whisky.getAlcoholsTastingTags().add(rel);
        // tag.getWhiskysTastingTags().add(rel);
    }

    /**
     * 모든 위스키 목록을 조회합니다.
     */
    public List<Whisky> getAllWhiskies() {
        return whiskyRepository.findAll();
    }

    /**
     * 페이지네이션이 적용된 위스키 목록을 조회합니다.
     */
    public Page<Whisky> getPagedWhiskies(Pageable pageable) {
        return whiskyRepository.findAll(pageable);
    }

    /**
     * 필터링된 위스키 목록을 조회합니다.
     */
    public Page<Whisky> getFilteredWhiskies(
            AlcoholType type,
            AlcoholCategoryGroup categoryGroup,
            String searchKeyword,
            SearchSortType sortBy,
            Pageable pageable) {

        List<Whisky> filtered = new ArrayList<>(whiskyRepository.findAll());

        // 타입 필터링
        if (type != null) {
            filtered = filtered.stream()
                    .filter(whisky -> whisky.getType() == type)
                    .collect(Collectors.toList());
        }

        // 카테고리 그룹 필터링
        if (categoryGroup != null) {
            filtered = filtered.stream()
                    .filter(whisky -> whisky.getCategoryGroup() == categoryGroup)
                    .collect(Collectors.toList());
        }

        // 검색어 필터링
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            String keyword = searchKeyword.toLowerCase();
            filtered = filtered.stream()
                    .filter(whisky ->
                            whisky.getKorName().toLowerCase().contains(keyword) ||
                                    whisky.getEngName().toLowerCase().contains(keyword) ||
                                    whisky.getKorCategory().toLowerCase().contains(keyword) ||
                                    whisky.getEngCategory().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // 정렬
        if (sortBy != null) {
            switch (sortBy) {
                case POPULAR:
                    // 인기순 정렬 로직 (추후 구현)
                    break;
                case RATING:
                    // 별점순 정렬 로직 (추후 구현)
                    break;
                case PICK:
                    // 찜순 정렬 로직 (추후 구현)
                    break;
                case REVIEW:
                    // 리뷰순 정렬 로직 (추후 구현)
                    break;
                case LAST_CREATED:
                    // 최신 등록순 정렬 로직
                    filtered.sort((w1, w2) -> {
                        if (w1.getCreateAt() == null && w2.getCreateAt() == null) {
                            return 0;
                        } else if (w1.getCreateAt() == null) {
                            return 1;
                        } else if (w2.getCreateAt() == null) {
                            return -1;
                        }
                        return w2.getCreateAt().compareTo(w1.getCreateAt()); // 내림차순 (최신순)
                    });
                    break;
                case LAST_MODIFIED:
                    // 최신 수정 순 정렬 로직
                    filtered.sort((w1, w2) -> {
                        if (w1.getLastModifyAt() == null && w2.getLastModifyAt() == null) {
                            return 0;
                        } else if (w1.getLastModifyAt() == null) {
                            return 1;
                        } else if (w2.getLastModifyAt() == null) {
                            return -1;
                        }
                        return w2.getLastModifyAt().compareTo(w1.getLastModifyAt()); // 내림차순 (최신순)
                    });
                    break;
            }
        }

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());

        if (start > filtered.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, filtered.size());
        }

        return new PageImpl<>(filtered.subList(start, end), pageable, filtered.size());
    }

    /**
     * ID로 위스키를 조회합니다.
     */
    public Optional<Whisky> getWhiskyById(Long id) {
        return whiskyRepository.findById(id);
    }

    /**
     * 새 위스키를 생성합니다.
     */
    @Transactional
    public Whisky createWhisky(Whisky whisky) {
        return whiskyRepository.save(whisky);
    }

    /**
     * 위스키 정보를 업데이트합니다.
     */
    @Transactional
    public Whisky updateWhisky(Whisky whisky) {
        return whiskyRepository.save(whisky);
    }

    /**
     * 위스키를 삭제합니다.
     */
    @Transactional
    public void deleteWhisky(Long id) {
        whiskyRepository.deleteById(id);
    }
}
