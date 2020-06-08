package guru.springframework.springreactivestockquoteservice.services;

import guru.springframework.springreactivestockquoteservice.domain.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author kas
 */
public interface QuoteGeneratorService {

    Flux<Quote> fetchQuoteStream(Duration period);
}
