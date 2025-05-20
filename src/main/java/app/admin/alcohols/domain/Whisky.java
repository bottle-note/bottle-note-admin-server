package app.admin.alcohols.domain;

import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@Entity(name = "whisky")
@Table(name = "alcohol")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Whisky {

    @Id
    @Comment("위스키 ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("위스키 한글 이름")
    @Column(name = "kor_name", nullable = false)
    private String korName;

    @Comment("위스키 영어 이름")
    @Column(name = "eng_name", nullable = false)
    private String engName;

    @Comment("도수")
    @Column(name = "abv")
    private String abv;

    @Comment("타입")
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlcoholType type;

    @Comment("하위 카테고리 한글명 ( ex. 위스키, 럼 )")
    @Column(name = "kor_category", nullable = false)
    private String korCategory;

    @Comment("하위 카테고리 영문명 ( ex. 위스키, 럼 )")
    @Column(name = "eng_category", nullable = false)
    private String engCategory;

    @Comment("하위 카테고리 그룹")
    @Enumerated(EnumType.STRING)
    @Column(name = "category_group", nullable = false)
    private AlcoholCategoryGroup categoryGroup;

    @Comment("국가")
    private Long regionId;

    @Comment("증류소")
    private Long distilleryId;

    @Comment("캐스트 타입")
    @Column(name = "cask")
    private String cask;

    @Comment("썸네일 이미지")
    @Column(name = "image_url")
    private String imageUrl;

    @Builder.Default
    @Comment("해당 위스키의 테이스팅 태그")
    @OneToMany(mappedBy = "whisky", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WhiskysTastingTags> alcoholsTastingTags = new HashSet<>();

    /**
     * 새 테이스팅 태그 연관관계를 추가합니다.
     */
    public void addTastingTagRelation(WhiskysTastingTags rel) {
        alcoholsTastingTags.add(rel);
        rel.setWhisky(this);
    }

    /**
     * 특정 테이스팅 태그 연관관계를 제거합니다.
     */
    public void removeTastingTagRelation(WhiskysTastingTags rel) {
        alcoholsTastingTags.remove(rel);
        rel.setWhisky(null);
    }

    /**
     * 모든 테이스팅 태그 연관관계를 초기화합니다.
     */
    public void clearTastingTagRelations() {
        alcoholsTastingTags.forEach(rel -> rel.setWhisky(null));
        alcoholsTastingTags.clear();
    }
}
