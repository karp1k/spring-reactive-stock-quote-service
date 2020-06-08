package guru.springframework.springreactivestockquoteservice.web;

import guru.springframework.springreactivestockquoteservice.domain.Quote;
import guru.springframework.springreactivestockquoteservice.services.QuoteGeneratorService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;


/**
 * @author kas
 */
@Component
public class QuoteHandler {

    private final QuoteGeneratorService quoteGeneratorService;

    public QuoteHandler(QuoteGeneratorService quoteGeneratorService) {
        this.quoteGeneratorService = quoteGeneratorService;
    }

    // return fixed number of Quotes
    public Mono<ServerResponse> fetchQoutes(ServerRequest request) {
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L)).take(size), Quote.class);
    }

    // return unlimited number of Quotes
    public Mono<ServerResponse> streamQuotes(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L)), Quote.class);
    }
}
