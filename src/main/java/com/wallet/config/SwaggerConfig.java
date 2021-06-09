package com.wallet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.wallet.security.utils.JwtTokenUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration //anotação para identificar que se trata de uma classe de confgiração  
@Profile("dev")  //deixa disponivel apenas em ambiente de desenvolvimento
@EnableSwagger2
public class SwaggerConfig {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.wallet.controller")) //indicar o caminho onde estão as classes de controller com os cmainhos dos nossos end-points
				.paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	//informações sobre as nossas APIS
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Wallet API")
				.description("Wallet API - Documentação de acesso aos endpoints.").version("1.0")
				.build();
	}
	
	@Bean
	public SecurityConfiguration security() { //aqui pegamos o usuário swagger para que o mesmo tenha um token para ter acesso aos nosso end-points
		String token;
		try {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername("development@swagger.user");
			token = this.jwtTokenUtil.getToken(userDetails);
		} catch (Exception e) {
			token = "";
		}

		return new SecurityConfiguration(null, null, null, null, "Bearer " + token, ApiKeyVehicle.HEADER,
				"Authorization", ",");
	}
}
