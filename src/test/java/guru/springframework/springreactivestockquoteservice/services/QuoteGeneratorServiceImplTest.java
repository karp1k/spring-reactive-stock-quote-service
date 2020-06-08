package guru.springframework.springreactivestockquoteservice.services;

import guru.springframework.springreactivestockquoteservice.domain.Quote;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class QuoteGeneratorServiceImplTest {

    QuoteGeneratorService generatorService = new QuoteGeneratorServiceImpl();

    @Test
    void fetchQuoteStreamWithCountDown() throws InterruptedException {
        Flux<Quote> quoteFlux = generatorService.fetchQuoteStream(Duration.ofMillis(100L));
        // tell the test to wait until the countDown is hit
        CountDownLatch countDownLatch = new CountDownLatch(1);

        quoteFlux.take(10).subscribe(q -> log.info(q.toString()), e -> log.error("Some error"), () -> countDownLatch.countDown());

        countDownLatch.await();
    }

}