package app.admin.alcohols.domain;

import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Builder
@Getter
@Entity(name = "whisky")
@Table(name = "alcohol")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id")
  private Region region;

  @Comment("증류소")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "distillery_id")
  private Distillery distillery;

  @Comment("캐스트 타입")
  @Column(name = "cask", nullable = true)
  private String cask;

  @Comment("썸네일 이미지")
  @Column(name = "image_url", nullable = true)
  private String imageUrl;

  @Builder.Default
  @Comment("해당 위스키의 테이스팅 태그")
  @OneToMany(mappedBy = "alcohol", fetch = FetchType.LAZY)
  private Set<AlcoholsTastingTags> alcoholsTastingTags = new HashSet<>();
}
