package app.admin.alcohols.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity(name = "tasting_tags")
public class TastingTag {
    @Id
    @Comment("태그 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("태그 영어 이름")
    @Column(name = "eng_name", nullable = false)
    private String engName;

    @Comment("태그 한글 이름")
    @Column(name = "kor_name", nullable = false)
    private String korName;

    @Comment("아이콘")
    @Column(name = "icon")
    private String icon;

    @Comment("태그 설명")
    @Column(name = "description")
    private String description;

    @Comment("태그를 가진 위스키들")
    @OneToMany(mappedBy = "tastingTag")
    private List<WhiskysTastingTags> whiskysTastingTags = new ArrayList<>();
}
