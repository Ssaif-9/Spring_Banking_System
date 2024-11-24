package com.luv2code.springboot.banking;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Bank App",
                description = "Backend Rest APIS for Bank",
                version = "v1.0",
                contact = @Contact(
                        name = "saif sultan",
                        email = "saifsoltan18@gmail.com",
                        url ="https://github.com/Ssaif-9/Spring-Boot-luv2code/tree/main/Banking"
                ),
                license = @License(
                        name = "saif sultan",
                        url = "https://github.com/Ssaif-9/Spring-Boot-luv2code/tree/main/Banking"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Saif Sultan Banking App Documentation",
                url = "https://github.com/Ssaif-9/Spring-Boot-luv2code/tree/main/Banking"
        )
)
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

}
