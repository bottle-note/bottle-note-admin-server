package app.admin.config;

import app.admin.alcohols.constant.AlcoholCategoryGroup;
import app.admin.alcohols.constant.AlcoholType;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 루트 경로를 대시보드로 리다이렉트
        registry.addRedirectViewController("/", "/dashboard");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToAlcoholTypeConverter());
        registry.addConverter(new StringToAlcoholCategoryGroupConverter());
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    private static class StringToAlcoholTypeConverter implements Converter<String, AlcoholType> {
        @Override
        public AlcoholType convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }
            return AlcoholType.valueOf(source);
        }
    }

    private static class StringToAlcoholCategoryGroupConverter implements Converter<String, AlcoholCategoryGroup> {
        @Override
        public AlcoholCategoryGroup convert(String source) {
            if (source == null || source.isEmpty()) {
                return null;
            }
            return AlcoholCategoryGroup.valueOf(source);
        }
    }
}
