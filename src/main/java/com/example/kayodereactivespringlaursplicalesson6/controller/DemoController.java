package com.example.kayodereactivespringlaursplicalesson6.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kayode.Ogunrinde on 3/17/2023.
 */

@RestController
public class DemoController {

    @GetMapping(value = "/demo", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> demo() {
        Flux<String> flux = Flux.just("AAA", "BB", "C", "DDDDD");
        Flux<String> flux2 = Flux.just("RRR", "TYA", "KOA");

        return flux
//                .concatWith(flux2)
                .flatMap(s -> Flux.just(s.split("")))
                .filter(s -> s.length() % 2 == 0)
                .thenMany(flux2)
                .filter(s -> s.length() % 2 == 0)
                .log();
    }

    /**
     * Never EVER do this
     * List<String> list = new ArrayList<>();
     *
     *         flux
     *                 .doOnNext(s -> list.add(s))
     *                 .log();
     *         return list;
     *
     * What was done here is blocking, instead the collectors should be used, such as
     */
    @GetMapping(value = "/demo/list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<List> demoList() {
        Flux<String> f1 = Flux.just("AAA", "BB", "C", "DDDDD");
        return f1.collect(Collectors.toList());
    }

    @GetMapping(value = "zipwith", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> greeting() {
        Flux<String> flux = Flux.just("AAA", "BB", "C", "DDDDD");
        Flux<String> flux2 = Flux.just("RRR", "TYA", "KOA");
        // in this case, the 4th value of the first flux is lost,
        // cos there's no matching value in flux2
        return flux.zipWith(flux2, (x, y) -> x + "=" + y);
//        return computeMessage()
//                .zipWith(getName())
//                .map(value -> {
//                    return value.getT1() + value.getT2();
//                });
    }

    private Mono<String> computeMessage() {
        return Mono.just("Hello");
    }
    private Mono<String> getName() {
        return Mono.just("Kayode");
    }
}
