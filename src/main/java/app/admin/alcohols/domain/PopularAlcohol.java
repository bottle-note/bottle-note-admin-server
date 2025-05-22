package app.admin.alcohols.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "popular_alcohols")
@Table(
        name = "popular_alcohol",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uniq_alcohol_year_month",
                        columnNames = {"alcohol_id", "year", "month", "day"})
        })
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PopularAlcohol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Comment("술 ID")
    @Column(name = "alcohol_id", nullable = false)
    private Long alcoholId;

    @Comment("년도")
    @Column(name = "year", nullable = false, columnDefinition = "smallint")
    private Integer year;

    @Comment("월")
    @Column(name = "month", nullable = false, columnDefinition = "tinyint")
    private Integer month;

    @Comment("일")
    @Column(name = "day", nullable = false, columnDefinition = "tinyint")
    private Integer day;

    @Comment("리뷰 점수")
    @Column(name = "review_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal reviewScore;

    @Comment("평점 점수")
    @Column(name = "rating_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal ratingScore;

    @Comment("찜하기 점수")
    @Column(name = "pick_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal pickScore;

    @Comment("인기도 점수")
    @Column(name = "popular_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal popularScore;

    @Comment("생성일시")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
