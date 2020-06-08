package guru.springframework.springreactivestockquoteservice.web;

import guru.springframework.springreactivestockquoteservice.domain.Quote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuoteRouterTest {

    private static final int SIZE = 20;

    @Autowired
    WebTestClient client;

    @Test
    void testFetchQuotes() {

        client
                .get()
                .uri("/quote?size=" + SIZE)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Quote.class).hasSize(SIZE)
                .consumeWith(quotes -> {
                    assertNotNull(quotes.getResponseBody());
                    quotes.getResponseBody().forEach(quote -> assertTrue(quote.getPrice().doubleValue() > 0));
                });

    }

    @Test
    void testStreamQuotes() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        client
                .get()
                .uri("/quote")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .returnResult(Quote.class)
                .getResponseBody()
                .take(10)
                .subscribe(quote -> {
                    assertTrue(quote.getPrice().doubleValue() > 0);
                    countDownLatch.countDown();
                });
        countDownLatch.await();
    }

}