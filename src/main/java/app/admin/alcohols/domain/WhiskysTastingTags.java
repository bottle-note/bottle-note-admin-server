package app.admin.alcohols.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Comment("알코올과 테이스팅 태그 연관관계 해소 테이블 ")
@Table(name = "alcohols_tasting_tags")
@Entity(name = "whisky_tasting_tags")
public class WhiskysTastingTags {

    @Id
    @Comment("알코올 테이스팅 태그 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("알코올 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_id", nullable = false)
    private Whisky whisky;

    @Comment("테이스팅 태그 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_tag_id", nullable = false)
    private TastingTag tastingTag;

    /**
     * 위스키와 테이스팅 태그 연관관계를 생성합니다.
     */
    public static WhiskysTastingTags createWhiskyTastingTag(Whisky whisky, TastingTag tastingTag) {
        return WhiskysTastingTags.builder()
                .whisky(whisky)
                .tastingTag(tastingTag)
                .build();
    }
}
