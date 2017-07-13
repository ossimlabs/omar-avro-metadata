package io.ossim.omaravrometadata

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
class SwaggerConfig
{
    @Bean
    Docket avroMetadataApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.ossim.omaravrometadata"))
                .paths(regex("/avroMetadata.*"))
                .build()
                .pathMapping("/api")
                .apiInfo(apiInfo(  ))
    }

    static ApiInfo apiInfo()
    {
        ApiInfo apiInfo = new ApiInfo(
                "AvroMetadata REST API",
                "This API allows access and manipulation of AvroMetadata objects stored in DynamoDB",
                "",
                "",
                "Stephen L'Allier",
                "",
                "")
        return apiInfo
    }
}
