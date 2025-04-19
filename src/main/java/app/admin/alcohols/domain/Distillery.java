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
@Comment("증류소")
@Entity(name = "distillery")
public class Distillery {

  @Id
  @Comment("증류소 ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Comment("증류소 영어 이름")
  @Column(name = "eng_name", nullable = false)
  private String engName;

  @Comment("증류소 한글 이름")
  @Column(name = "kor_name", nullable = false)
  private String korName;

  @Comment("증류소 로고 이미지 경로")
  @Column(name = "logo_img_url")
  private String logoImgPath;

  @Comment("증류소 설명")
  @OneToMany(mappedBy = "distillery")
  private List<Alcohol> alcohol = new ArrayList<>();
}
