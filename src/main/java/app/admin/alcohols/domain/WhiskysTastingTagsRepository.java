package app.admin.alcohols.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiskysTastingTagsRepository extends JpaRepository<WhiskysTastingTags, Long> {

    /**
     * 위스키 ID로 테이스팅 태그 연관관계를 조회합니다.
     */
    List<WhiskysTastingTags> findByWhiskyId(Long whiskyId);

    /**
     * 위스키 ID로 테이스팅 태그 연관관계를 삭제합니다.
     */
    void deleteByWhiskyId(Long whiskyId);
}
