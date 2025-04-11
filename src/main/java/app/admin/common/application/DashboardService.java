package app.admin.common.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

import app.admin.common.model.DashboardStats;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

  /** 대시보드 통계 데이터를 가져옵니다. 실제 구현에서는 데이터베이스에서 데이터를 조회하지만, 현재는 모킹 데이터를 반환합니다. */
  public DashboardStats getDashboardStats() {
    // 모킹 데이터 생성
    return DashboardStats.builder()
        .totalWhiskyCount(248)
        .pendingReportCount(12)
        .totalReviewCount(1524)
        .pendingInquiryCount(8)
        .categoryDistribution(createCategoryDistribution())
        .reviewTrend(createReviewTrend())
        .build();
  }

  /** 위스키 카테고리별 분포 모킹 데이터를 생성합니다. */
  private DashboardStats.CategoryDistribution createCategoryDistribution() {
    return DashboardStats.CategoryDistribution.builder()
        .singleMalt(120)
        .blended(80)
        .bourbon(25)
        .rye(15)
        .irish(5)
        .other(3)
        .build();
  }

  /** 일별 리뷰 작성 추이 모킹 데이터를 생성합니다. */
  private DashboardStats.ReviewTrend createReviewTrend() {
    // 최근 7일 날짜 생성
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

    String[] dates =
        IntStream.range(0, 7)
            .mapToObj(i -> today.minusDays(6 - i).format(formatter))
            .toArray(String[]::new);

    // 모킹 리뷰 수 데이터
    int[] counts = {12, 19, 15, 17, 22, 24, 18};

    return DashboardStats.ReviewTrend.builder().dates(dates).counts(counts).build();
  }
}
