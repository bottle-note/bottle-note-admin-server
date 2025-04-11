package app.admin.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record DashboardStats(
    int totalWhiskyCount, int pendingReportCount, int totalReviewCount, int pendingInquiryCount) {}
