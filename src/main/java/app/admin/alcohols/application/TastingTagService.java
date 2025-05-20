package app.admin.alcohols.application;

import app.admin.alcohols.domain.TastingTag;
import app.admin.alcohols.domain.TastingTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TastingTagService {

    private final TastingTagRepository tastingTagRepository;

    /**
     * 모든 테이스팅 태그 목록을 조회합니다.
     */
    public List<TastingTag> getAllTastingTags() {
        return tastingTagRepository.findAll();
    }
}