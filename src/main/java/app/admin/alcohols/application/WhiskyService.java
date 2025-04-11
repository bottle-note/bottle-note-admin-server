package app.admin.alcohols.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import app.admin.alcohols.model.WhiskyItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhiskyService {

  // 모킹 데이터를 저장할 리스트
  private final List<WhiskyItem> whiskyItemList = new ArrayList<>();

  public Long getWhiskyItemListSize() {
    return (long) whiskyItemList.size();
  }

  /** 모든 위스키 목록을 조회합니다. */
  public List<WhiskyItem> getAllWhiskies() {
    return new ArrayList<>(whiskyItemList);
  }

  /** 필터링된 위스키 목록을 조회합니다. */
  public List<WhiskyItem> getFilteredWhiskies(
      String category, String searchKeyword, String sortBy) {
    List<WhiskyItem> filtered = new ArrayList<>(whiskyItemList);

    // 카테고리 필터링
    if (category != null && !category.isEmpty()) {
      filtered =
          filtered.stream()
              .filter(w -> w.getCategory().equalsIgnoreCase(category))
              .collect(Collectors.toList());
    }

    // 검색어 필터링
    if (searchKeyword != null && !searchKeyword.isEmpty()) {
      filtered =
          filtered.stream()
              .filter(
                  w ->
                      w.getName().toLowerCase().contains(searchKeyword.toLowerCase())
                          || (w.getDescription() != null
                              && w.getDescription()
                                  .toLowerCase()
                                  .contains(searchKeyword.toLowerCase())))
              .collect(Collectors.toList());
    }

    // 정렬
    if (sortBy != null) {
      switch (sortBy) {
        case "name":
          filtered.sort((w1, w2) -> w1.getName().compareToIgnoreCase(w2.getName()));
          break;
        case "date-desc":
          filtered.sort((w1, w2) -> w2.getCreatedAt().compareTo(w1.getCreatedAt()));
          break;
        case "date-asc":
          filtered.sort((w1, w2) -> w1.getCreatedAt().compareTo(w2.getCreatedAt()));
          break;
        case "reviews-desc":
          filtered.sort((w1, w2) -> w2.getReviewCount().compareTo(w1.getReviewCount()));
          break;
        default:
          // 기본 정렬은 이름순
          filtered.sort((w1, w2) -> w1.getName().compareToIgnoreCase(w2.getName()));
      }
    }

    return filtered;
  }

  /** ID로 위스키를 조회합니다. */
  public Optional<WhiskyItem> getWhiskyById(Long id) {
    return whiskyItemList.stream().filter(w -> w.getId().equals(id)).findFirst();
  }

  /** 새 위스키를 추가합니다. */
  public WhiskyItem addWhisky(WhiskyItem whiskyItem) {
    // 새 ID 생성
    Long newId = whiskyItemList.stream().mapToLong(WhiskyItem::getId).max().orElse(0) + 1;

    whiskyItem.setId(newId);
    whiskyItem.setCreatedAt(LocalDateTime.now());
    whiskyItem.setUpdatedAt(LocalDateTime.now());
    whiskyItem.setReviewCount(0);

    whiskyItemList.add(whiskyItem);
    return whiskyItem;
  }

  /** 위스키 정보를 수정합니다. */
  public Optional<WhiskyItem> updateWhisky(Long id, WhiskyItem updatedWhiskyItem) {
    Optional<WhiskyItem> existingWhisky = getWhiskyById(id);

    if (existingWhisky.isPresent()) {
      WhiskyItem whiskyItem = existingWhisky.get();

      // 기존 정보 업데이트
      whiskyItem.setName(updatedWhiskyItem.getName());
      whiskyItem.setCategory(updatedWhiskyItem.getCategory());
      whiskyItem.setAlcoholPercentage(updatedWhiskyItem.getAlcoholPercentage());
      whiskyItem.setVolume(updatedWhiskyItem.getVolume());
      whiskyItem.setOrigin(updatedWhiskyItem.getOrigin());
      whiskyItem.setManufacturer(updatedWhiskyItem.getManufacturer());
      whiskyItem.setDescription(updatedWhiskyItem.getDescription());
      whiskyItem.setAgingPeriod(updatedWhiskyItem.getAgingPeriod());
      whiskyItem.setCaskType(updatedWhiskyItem.getCaskType());
      whiskyItem.setTasting(updatedWhiskyItem.getTasting());

      // 이미지가 제공된 경우에만 업데이트
      if (updatedWhiskyItem.getImageUrl() != null) {
        whiskyItem.setImageUrl(updatedWhiskyItem.getImageUrl());
      }
      if (updatedWhiskyItem.getAdditionalImages() != null) {
        whiskyItem.setAdditionalImages(updatedWhiskyItem.getAdditionalImages());
      }

      whiskyItem.setUpdatedAt(LocalDateTime.now());

      return Optional.of(whiskyItem);
    }

    return Optional.empty();
  }

  /** 위스키를 삭제합니다. */
  public boolean deleteWhisky(Long id) {
    Optional<WhiskyItem> whisky = getWhiskyById(id);

    if (whisky.isPresent()) {
      whiskyItemList.remove(whisky.get());
      return true;
    }

    return false;
  }
}
