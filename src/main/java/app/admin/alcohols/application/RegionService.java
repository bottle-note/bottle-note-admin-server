package app.admin.alcohols.application;

import app.admin.alcohols.domain.Region;
import app.admin.alcohols.domain.RegionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    /**
     * 모든 지역 목록을 조회합니다.
     */
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    /**
     * ID로 지역을 조회합니다.
     */
    public Region getRegionById(Long id) {
        return regionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Region not found: " + id));
    }
}