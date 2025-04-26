package app.admin.alcohols.application;

import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import app.admin.alcohols.constant.SearchSortType;
import app.admin.alcohols.domain.Whisky;
import app.admin.alcohols.domain.WhiskyRepository;
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
