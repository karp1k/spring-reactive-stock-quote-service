package guru.springframework.springreactivestockquoteservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;


/**
 * @author kas
 */
@Configuration
public class QuoteRouter {

    @Bean
    RouterFunction<ServerResponse> route(QuoteHandler quoteHandler) {
        return RouterFunctions
        .route(RequestPredicates.GET("/quote").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), quoteHandler::fetchQoutes)
        .andRoute(RequestPredicates.GET("/quote").and(RequestPredicates.accept(MediaType.APPLICATION_STREAM_JSON)), quoteHandler::streamQuotes);
    }
}
