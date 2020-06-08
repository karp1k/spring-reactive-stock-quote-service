package guru.springframework.springreactivestockquoteservice.services;

import guru.springframework.springreactivestockquoteservice.domain.Quote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author kas
 */
@Service
public class QuoteGeneratorServiceImpl implements QuoteGeneratorService {

    private final MathContext mathContext = new MathContext(2);
    private final Random random = new Random();
    private final List<Quote> prices = new ArrayList<>();


    public QuoteGeneratorServiceImpl() {
        this.prices.add(new Quote("AAPL", 160.16));
        this.prices.add(new Quote("NSFT", 70.60));
        this.prices.add(new Quote("GOOG", 847.25));
        this.prices.add(new Quote("ORCL", 49.51));
        this.prices.add(new Quote("IBM", 159.34));
        this.prices.add(new Quote("VMW", 92.21));
        this.prices.add(new Quote("RNT", 12.56));
    }

    @Override
    public Flux<Quote> fetchQuoteStream(Duration period) {
        // Flux.generate to create Quotes, iterating on each stock starting at 0 index
        return Flux.generate(() -> 0, (index, sink) -> {
          Quote updatedQuote = updateQuote(this.prices.get(index));
          sink.next(updatedQuote);
          return ++index % this.prices.size();
        })
                // emit Flux<Quote> with a specific period
                // zipping Flux with Flux.interval
                .zipWith(Flux.interval(period))
                .map(tuple -> (Quote) tuple.getT1())
                // set values timestamp after creation
                .map(q -> {
                    q.setInstant(Instant.now());
                    return q;
                })
                .log("guru.springframework.springreactivestockquoteservice.services.QuoteGeneratorServiceImpl");
    }

    private Quote updateQuote(Quote quote) {
        BigDecimal priceChange = quote.getPrice().multiply(new BigDecimal(0.04 * this.random.nextDouble(), this.mathContext));
        return new Quote(quote.getTicker(), quote.getPrice().add(priceChange));
    }
}
