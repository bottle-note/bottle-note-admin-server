package app.admin.alcohols.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Set;
import lombok.Getter;

@Getter
public enum AlcoholCategoryGroup {
	SINGLE_MALT("싱글몰트 알코올", Set.of("Single Malts")),
	BLEND("블렌디드 알코올", Set.of("Blends")),
	BLENDED_MALT("블렌디드 몰트 알코올", Set.of("Blended Malts")),
	BOURBON("버번 알코올", Set.of("Bourbon")),
	RYE("라이 알코올", Set.of("Rye")),
	OTHER("기타 알코올", Set.of("Single Pot Still", "Single Grain", "Spirit", "Tennessee", "Wheat", "Corn"));

	private final String description;
	private final Set<String> categories;

	AlcoholCategoryGroup(String description, Set<String> categories) {
		this.description = description;
		this.categories = categories;
	}

	@JsonCreator
	public static AlcoholCategoryGroup fromCategory(String categoryGroup) {
		if (categoryGroup == null) return null;

		return Arrays.stream(values())
			.filter(group -> group.toString().equals(categoryGroup.toUpperCase()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("category group이 적절하지 않습니다. : " + categoryGroup));
	}

	public Boolean containsCategory(String category) {
		return Arrays.stream(values())
			.anyMatch(group -> group.categories.contains(category));
	}
}
