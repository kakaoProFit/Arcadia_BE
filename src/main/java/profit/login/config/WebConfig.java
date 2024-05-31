package profit.login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://arcadia.p-e.kr", "https://localhost:3000") // “*“같은 와일드카드를 사용
//                .allowedOrigins("https://arcadia.p-e.kr", "https://localhost:3000")
                .allowedMethods("GET", "POST") // 허용할 HTTP method
                .allowCredentials(true)
                .maxAge(3600);


    }
}
