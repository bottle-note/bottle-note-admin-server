package app.admin.alcohols.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "tasting_tag")
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
  private List<AlcoholsTastingTags> alcoholsTastingTags = new ArrayList<>();
}
