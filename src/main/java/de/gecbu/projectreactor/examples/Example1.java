package de.gecbu.projectreactor.examples;

import reactor.core.publisher.Flux;

public class Example1 extends AbstractExample {

    @Override
    protected void run() {
        /**
         * Create a Flux stream from the integer values of an array.
         */
        Flux<Integer> integerFlux = Flux.fromArray(new Integer[]{1, 2, 3, 4, 5});

        /**
         * Subscribe to a stream of elements. In this case a stream of Integers.
         *
         * Although it seems all integers are displayed together at once, they are
         * actually streamed one after another.
         */
        System.out.println("Subscribing to immediate Integer-Flux...");
        integerFlux.subscribe(element -> System.out.println(element));

        /**
         * Subscribe to the same integer stream again.
         *
         * Be aware that all elements are streamed again on subscription and therefore
         * printed again.
         */
        System.out.println("Subscribing again to immediate Integer-Flux...");
        integerFlux.subscribe(element -> System.out.println(element));

    }

}
