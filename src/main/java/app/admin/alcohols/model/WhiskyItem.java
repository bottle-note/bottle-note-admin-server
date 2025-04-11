package app.admin.alcohols.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhiskyItem {
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
