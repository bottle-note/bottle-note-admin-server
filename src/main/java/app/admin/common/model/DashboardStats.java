package app.admin.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private int totalWhiskyCount;
    private int pendingReportCount;
    private int totalReviewCount;
    private int pendingInquiryCount;
    
    // 차트 데이터는 실제로는 별도 클래스로 분리할 수 있지만 간단한 구현을 위해 여기에 포함
    private CategoryDistribution categoryDistribution;
    private ReviewTrend reviewTrend;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDistribution {
        private int singleMalt;
        private int blended;
        private int bourbon;
        private int rye;
        private int irish;
        private int other;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewTrend {
        private String[] dates;
        private int[] counts;
    }
}
