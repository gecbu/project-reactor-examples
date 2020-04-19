package de.gecbu.projectreactor.examples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.function.Consumer;

/**
 * This example shows a basic generation of a Flux stream via the generate method. The CounterConsumer provided as a parameter is
 * nothing more than a Consumer which needs a SynchronousSink for its accept method. The sink is the API to trigger the events via
 * onNext, onError or onComplete. The program prints out the numbers from 1 to 10 with the additional information where the
 * printing actually takes place. The order of the console output shows the synchronous behavior and the Consumer and the
 * Subscriber are called alternately.
 * <p>
 * As we do not use any specific threads the program runs in the main thread and therefore is terminated after the last number is
 * emitted.
 */
public class SimpleFluxGeneratingExample extends AbstractExample {

    public static final String GENERATE_SIMPLE_FLUX = "GENERATE_SIMPLE_FLUX";

    @Override
    protected void run() {
        class CounterConsumer implements Consumer<SynchronousSink<String>> {
            int i = 1;

            @Override
            public void accept(SynchronousSink sink) {
                if (i <= 10) {
                    System.out.println("Consumer: " + i);
                    sink.next(String.valueOf(i));
                    i++;
                } else {
                    sink.complete();
                }
            }
        }

        Flux.generate(new CounterConsumer())
                .subscribe(element -> System.out.println("Subscriber " + element));
    }

}
