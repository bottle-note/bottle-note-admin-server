package app.admin.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Whisky {
    private Long id;
    private String name;
    private String category;
    private Double alcoholPercentage;
    private Integer volume;
    private String origin;
    private String manufacturer;
    private String description;
    private Integer agingPeriod;
    private String caskType;
    private String tasting;
    private String imageUrl;
    private String[] additionalImages;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer reviewCount;
}
