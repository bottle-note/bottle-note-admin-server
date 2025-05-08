package app.admin.alcohols.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiskysTastingTagsRepository extends JpaRepository<WhiskysTastingTags, Long> {
}