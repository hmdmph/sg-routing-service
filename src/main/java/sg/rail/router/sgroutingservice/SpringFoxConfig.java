package sg.rail.router.sgroutingservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select()
//        .apis(RequestHandlerSelectors.basePackage("sg.rail.router.sgroutingservice"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(info());
  }

  private ApiInfo info() {
    return new ApiInfoBuilder().title("SG Rail Router project")
        .description("Singapore rail routing project")
        .contact(new Contact("Manoj Prasanna", "https://github.com/hmdmph", "hmdmph@gmail.com"))
        .version("1.0.0")
        .build();
  }
}
