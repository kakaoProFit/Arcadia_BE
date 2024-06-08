package profit.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://arcadiaprofit.shop", "https://arcadia.p-e.kr","http://localhost:3000") // “*“같은 와일드카드를 사용
                // .allowedOriginPatterns("*")
//                .allowedOrigins("https://arcadia.p-e.kr", "https://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP method
                .allowedHeaders("*") 
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);


    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(converter);
    }
}
