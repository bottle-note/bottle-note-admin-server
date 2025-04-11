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
    return DashboardStats.builder()
        .totalWhiskyCount(248)
        .pendingReportCount(12)
        .totalReviewCount(1524)
        .pendingInquiryCount(8)
        .build();
  }
}
