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
                .allowedOriginPatterns("https://arcadiaprofit.shop", "https://arcadia.p-e.kr","http://localhost:3000") // “*“같은 와일드카드를 사용
                // .allowedOriginPatterns("*")
//                .allowedOrigins("https://arcadia.p-e.kr", "https://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP method
                .allowedHeaders("*") 
                .exposedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);


    }
}
