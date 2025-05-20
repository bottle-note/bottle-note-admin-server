package app.admin.alcohols.application;

import app.admin.alcohols.domain.Distillery;
import app.admin.alcohols.domain.DistilleryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistilleryService {

    private final DistilleryRepository distilleryRepository;

    /**
     * 모든 증류소 목록을 조회합니다.
     */
    public List<Distillery> getAllDistilleries() {
        return distilleryRepository.findAll();
    }

    /**
     * ID로 증류소를 조회합니다.
     */
    public Distillery getDistilleryById(Long id) {
        return distilleryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Distillery not found: " + id));
    }
}