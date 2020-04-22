package de.gecbu.projectreactor.examples;

import reactor.core.publisher.Flux;

public class GeneratorWithSimpleBiFunctionLambda extends AbstractExample {

    public static final String NAME = "GENERATOR_WITH_SIMPLE_BIFUNCTION_LAMBDA";

    @Override
    protected void run() {

        /**
         * Create a new stream with a generator and subscribe to the emitted items.
         */
        Flux<Integer> customIntFlux = Flux.generate(
                () -> 0, // The first element to be emitted
                (state, sink) -> {
                    sink.next(state + 1);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        System.out.println("Subscribing to the custom generated stream...");
        customIntFlux.subscribe(number -> System.out.println("Generated number: " + number));
    }
}
