package app.admin.alcohols.dto;

import jakarta.validation.constraints.NotBlank;

public record TastingTagRequestDto(
        @NotBlank(message = "한글 이름은 필수입니다.")
        String korName,

        @NotBlank(message = "영문 이름은 필수입니다.")
        String engName,

        String description
) {

    public static TastingTagRequestDto createEmpty() {
        return new TastingTagRequestDto(null, null, null);
    }
}