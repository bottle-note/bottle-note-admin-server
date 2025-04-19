package app.admin.alcohols.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlcoholType {
	WHISKY("위스키"),
	RUM("럼"),
	VODKA("보드카"),
	GIN("진"),
	TEQUILA("데킬라"),
	BRANDY("브랜디"),
	BEER("맥주"),
	WINE("와인"),
	ETC("기타");

	private final String type;

	@JsonCreator
	public static AlcoholType parsing(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}
		return Stream.of(AlcoholType.values())
			.filter(sortType -> sortType.toString().equals(source.toUpperCase()))
			.findFirst()
			.orElse(WHISKY);
	}
}
