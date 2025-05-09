package app.admin.alcohols.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

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
    private List<Whisky> alcohol = new ArrayList<>();
}
