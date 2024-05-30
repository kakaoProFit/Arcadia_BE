package profit.login.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "API 명세서";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "Project API 명세서";


    @Bean
    public OpenAPI OpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_NAME)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION));
    }
}
