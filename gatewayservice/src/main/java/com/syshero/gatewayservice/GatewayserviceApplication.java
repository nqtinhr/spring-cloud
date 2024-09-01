package com.syshero.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserviceApplication.class, args);
	}


	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder, LoggingGatewayFilterFactory loggingFactory) {
		return builder.routes()
				.route("user-route", r -> r.path("/user/**")
						.filters(f -> f.stripPrefix(1)
								.filter(loggingFactory.apply(new LoggingGatewayFilterFactory.Config()))
								.circuitBreaker(c -> c.setName("CircuitBreaker").getFallbackUri()))
						.uri("lb://account-service"))

				.route("report-route", r -> r.path("/report/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://statistic-service"))

				.route("notification-route", r -> r.path("/notification/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://notification-service"))

				/// swagger ui
				.route("openapi", r -> r.path("/v3/api-docs/**")
						.filters(f -> f.rewritePath("/v3/api-docs/(?<service>.*)", "/${service}/v3/api-docs"))
						.uri("lb://gateway-service"))

				.build();
	}

	// @Bean
	// public CorsFilter corsFilter() {
	// 	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 	final CorsConfiguration config = new CorsConfiguration();
	// 	config.setAllowCredentials(true);
	// 	config.addAllowedOrigin("*");
	// 	config.addAllowedHeader("*");
	// 	config.addAllowedMethod("OPTIONS");
	// 	config.addAllowedMethod("HEAD");
	// 	config.addAllowedMethod("GET");
	// 	config.addAllowedMethod("PUT");
	// 	config.addAllowedMethod("POST");
	// 	config.addAllowedMethod("DELETE");
	// 	config.addAllowedMethod("PATCH");
	// 	source.registerCorsConfiguration("/**", config);
	// 	return new CorsFilter(source);
	// }
}
