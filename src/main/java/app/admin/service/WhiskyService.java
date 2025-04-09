package app.admin.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import app.admin.model.Whisky;
import org.springframework.stereotype.Service;

@Service
public class WhiskyService {

  // 모킹 데이터를 저장할 리스트
  private final List<Whisky> whiskyList = new ArrayList<>();

  // 생성자에서 모킹 데이터 초기화
  public WhiskyService() {
    initMockData();
  }

  /** 모든 위스키 목록을 조회합니다. */
  public List<Whisky> getAllWhiskies() {
    return new ArrayList<>(whiskyList);
  }

  /** 필터링된 위스키 목록을 조회합니다. */
  public List<Whisky> getFilteredWhiskies(String category, String searchKeyword, String sortBy) {
    List<Whisky> filtered = new ArrayList<>(whiskyList);

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
  public Optional<Whisky> getWhiskyById(Long id) {
    return whiskyList.stream().filter(w -> w.getId().equals(id)).findFirst();
  }

  /** 새 위스키를 추가합니다. */
  public Whisky addWhisky(Whisky whisky) {
    // 새 ID 생성
    Long newId = whiskyList.stream().mapToLong(Whisky::getId).max().orElse(0) + 1;

    whisky.setId(newId);
    whisky.setCreatedAt(LocalDateTime.now());
    whisky.setUpdatedAt(LocalDateTime.now());
    whisky.setReviewCount(0);

    whiskyList.add(whisky);
    return whisky;
  }

  /** 위스키 정보를 수정합니다. */
  public Optional<Whisky> updateWhisky(Long id, Whisky updatedWhisky) {
    Optional<Whisky> existingWhisky = getWhiskyById(id);

    if (existingWhisky.isPresent()) {
      Whisky whisky = existingWhisky.get();

      // 기존 정보 업데이트
      whisky.setName(updatedWhisky.getName());
      whisky.setCategory(updatedWhisky.getCategory());
      whisky.setAlcoholPercentage(updatedWhisky.getAlcoholPercentage());
      whisky.setVolume(updatedWhisky.getVolume());
      whisky.setOrigin(updatedWhisky.getOrigin());
      whisky.setManufacturer(updatedWhisky.getManufacturer());
      whisky.setDescription(updatedWhisky.getDescription());
      whisky.setAgingPeriod(updatedWhisky.getAgingPeriod());
      whisky.setCaskType(updatedWhisky.getCaskType());
      whisky.setTasting(updatedWhisky.getTasting());

      // 이미지가 제공된 경우에만 업데이트
      if (updatedWhisky.getImageUrl() != null) {
        whisky.setImageUrl(updatedWhisky.getImageUrl());
      }
      if (updatedWhisky.getAdditionalImages() != null) {
        whisky.setAdditionalImages(updatedWhisky.getAdditionalImages());
      }

      whisky.setUpdatedAt(LocalDateTime.now());

      return Optional.of(whisky);
    }

    return Optional.empty();
  }

  /** 위스키를 삭제합니다. */
  public boolean deleteWhisky(Long id) {
    Optional<Whisky> whisky = getWhiskyById(id);

    if (whisky.isPresent()) {
      whiskyList.remove(whisky.get());
      return true;
    }

    return false;
  }

  /** 모킹 데이터를 초기화합니다. */
  private void initMockData() {
    whiskyList.add(
        Whisky.builder()
            .id(1L)
            .name("글렌피딕 12년")
            .category("싱글 몰트")
            .alcoholPercentage(40.0)
            .volume(700)
            .origin("스코틀랜드")
            .manufacturer("글렌피딕 증류소")
            .description(
                "글렌피딕 12년은 스코틀랜드 스페이사이드 지역의 대표적인 싱글 몰트 위스키입니다. 배 향과 사과 향이 특징적이며, 부드러운 맛과 균형 잡힌 피니시가 일품입니다.")
            .agingPeriod(12)
            .caskType("아메리칸 오크, 쉐리 오크")
            .tasting("향: 배, 사과, 꿀\n맛: 과일, 몰트, 오크\n피니시: 부드럽고 균형 잡힌 여운")
            .imageUrl("https://via.placeholder.com/500")
            .additionalImages(
                new String[] {"https://via.placeholder.com/500", "https://via.placeholder.com/500"})
            .createdAt(LocalDateTime.now().minusDays(60))
            .updatedAt(LocalDateTime.now().minusDays(30))
            .reviewCount(128)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(2L)
            .name("발베니 12년 더블우드")
            .category("싱글 몰트")
            .alcoholPercentage(43.0)
            .volume(700)
            .origin("스코틀랜드")
            .manufacturer("발베니 증류소")
            .description(
                "발베니 12년 더블우드는 아메리칸 오크 캐스크와 쉐리 캐스크에서 숙성되어 복합적인 풍미를 가진 위스키입니다. 달콤한 과일 향과 스파이시한 풍미가 조화롭게 어우러집니다.")
            .agingPeriod(12)
            .caskType("아메리칸 오크, 쉐리 오크")
            .tasting("향: 꿀, 바닐라, 시나몬\n맛: 과일, 견과류, 스파이스\n피니시: 따뜻하고 오래 지속되는 여운")
            .imageUrl("https://via.placeholder.com/500")
            .additionalImages(new String[] {"https://via.placeholder.com/500"})
            .createdAt(LocalDateTime.now().minusDays(55))
            .updatedAt(LocalDateTime.now().minusDays(25))
            .reviewCount(96)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(3L)
            .name("맥켈란 12년")
            .category("싱글 몰트")
            .alcoholPercentage(40.0)
            .volume(700)
            .origin("스코틀랜드")
            .manufacturer("맥켈란 증류소")
            .description(
                "맥켈란 12년은 쉐리 캐스크에서 숙성되어 풍부한 과일 향과 스파이시한 풍미가 특징인 위스키입니다. 쉐리 캐스크 숙성으로 인한 달콤함과 깊은 풍미가 일품입니다.")
            .agingPeriod(12)
            .caskType("쉐리 오크")
            .tasting("향: 건포도, 바닐라, 생강\n맛: 쉐리, 오크, 스파이스\n피니시: 달콤하고 따뜻한 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(50))
            .updatedAt(LocalDateTime.now().minusDays(20))
            .reviewCount(112)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(4L)
            .name("조니워커 블루라벨")
            .category("블렌디드")
            .alcoholPercentage(40.0)
            .volume(700)
            .origin("스코틀랜드")
            .manufacturer("조니워커")
            .description(
                "조니워커 블루라벨은 희귀한 몰트와 그레인 위스키를 블렌딩한 프리미엄 블렌디드 위스키입니다. 부드러운 질감과 복합적인 풍미가 특징입니다.")
            .caskType("다양한 오크 캐스크")
            .tasting("향: 꿀, 헤이즐넛, 다크 초콜릿\n맛: 몰트, 스모키, 스파이스\n피니시: 길고 부드러운 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(45))
            .updatedAt(LocalDateTime.now().minusDays(15))
            .reviewCount(87)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(5L)
            .name("라프로익 10년")
            .category("싱글 몰트")
            .alcoholPercentage(43.0)
            .volume(700)
            .origin("스코틀랜드")
            .manufacturer("라프로익 증류소")
            .description(
                "라프로익 10년은 아일라 지역의 강한 피트 향이 특징인 싱글 몰트 위스키입니다. 해풍과 피트 스모크의 조화가 독특한 풍미를 만들어냅니다.")
            .agingPeriod(10)
            .caskType("아메리칸 오크")
            .tasting("향: 피트 스모크, 해풍, 요오드\n맛: 스모키, 바다 소금, 달콤함\n피니시: 길고 스모키한 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(40))
            .updatedAt(LocalDateTime.now().minusDays(10))
            .reviewCount(76)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(6L)
            .name("메이커스 마크")
            .category("버번")
            .alcoholPercentage(45.0)
            .volume(700)
            .origin("미국")
            .manufacturer("메이커스 마크 증류소")
            .description("메이커스 마크는 밀을 사용한 부드러운 버번 위스키입니다. 붉은 왁스 씰로 유명하며, 달콤한 바닐라와 캐러멜 풍미가 특징입니다.")
            .caskType("신규 아메리칸 오크")
            .tasting("향: 바닐라, 캐러멜, 밀\n맛: 달콤함, 스파이스, 과일\n피니시: 부드럽고 달콤한 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(35))
            .updatedAt(LocalDateTime.now().minusDays(5))
            .reviewCount(65)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(7L)
            .name("잭 다니엘스")
            .category("테네시")
            .alcoholPercentage(40.0)
            .volume(700)
            .origin("미국")
            .manufacturer("잭 다니엘스 증류소")
            .description("잭 다니엘스는 차콜 여과 공법으로 만들어진 테네시 위스키입니다. 부드러운 맛과 달콤한 풍미가 특징입니다.")
            .caskType("아메리칸 오크")
            .tasting("향: 바닐라, 캐러멜, 오크\n맛: 달콤함, 오크, 가벼운 스모크\n피니시: 부드럽고 달콤한 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(30))
            .updatedAt(LocalDateTime.now().minusDays(3))
            .reviewCount(92)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(8L)
            .name("제임슨")
            .category("아이리시")
            .alcoholPercentage(40.0)
            .volume(700)
            .origin("아일랜드")
            .manufacturer("미들턴 증류소")
            .description(
                "제임슨은 세계에서 가장 유명한 아이리시 위스키 중 하나로, 부드럽고 가벼운 맛이 특징입니다. 세 번 증류하여 더욱 부드러운 풍미를 가집니다.")
            .caskType("오크 캐스크")
            .tasting("향: 꽃, 바닐라, 나무\n맛: 견과류, 스파이스, 달콤함\n피니시: 부드럽고 깔끔한 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(25))
            .updatedAt(LocalDateTime.now().minusDays(2))
            .reviewCount(58)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(9L)
            .name("히비키 하모니")
            .category("블렌디드")
            .alcoholPercentage(43.0)
            .volume(700)
            .origin("일본")
            .manufacturer("산토리")
            .description(
                "히비키 하모니는 산토리의 프리미엄 블렌디드 위스키로, 조화로운 풍미와 부드러운 질감이 특징입니다. 일본 위스키의 정교함을 잘 보여주는 제품입니다.")
            .caskType("미즈나라 오크, 아메리칸 오크, 쉐리 캐스크")
            .tasting("향: 꿀, 오렌지 필, 화이트 초콜릿\n맛: 과일, 꿀, 가벼운 오크\n피니시: 깔끔하고 부드러운 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(20))
            .updatedAt(LocalDateTime.now().minusDays(1))
            .reviewCount(72)
            .build());

    whiskyList.add(
        Whisky.builder()
            .id(10L)
            .name("아벨라워 12년")
            .category("싱글 몰트")
            .alcoholPercentage(40.0)
            .volume(700)
            .origin("스코틀랜드")
            .manufacturer("아벨라워 증류소")
            .description("아벨라워 12년은 쉐리 캐스크에서 숙성된 스페이사이드 싱글 몰트로, 풍부한 과일 향과 달콤한 풍미가 특징입니다.")
            .agingPeriod(12)
            .caskType("쉐리 오크")
            .tasting("향: 쉐리, 건포도, 시나몬\n맛: 과일 케이크, 스파이스, 초콜릿\n피니시: 따뜻하고 달콤한 여운")
            .imageUrl("https://via.placeholder.com/500")
            .createdAt(LocalDateTime.now().minusDays(15))
            .updatedAt(LocalDateTime.now())
            .reviewCount(45)
            .build());
  }
}
