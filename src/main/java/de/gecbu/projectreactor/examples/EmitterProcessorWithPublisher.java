package de.gecbu.projectreactor.examples;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * This example demonstrates how to connect (subscribe) an {@link EmitterProcessor} to a given Publisher.
 */
public class EmitterProcessorWithPublisher extends AbstractExample {

    public static final String NAME = "EMITTER_PROCESSOR_PUBLISHER_EXAMPLE";

    @Override
    protected void run() {
        EmitterProcessor<Integer> customProcessor = EmitterProcessor.create();
        customProcessor.doOnNext(element -> System.out.println("doOnNext " + element));
        Flux<Integer> origin = Flux.range(1, 20);
        origin.publishOn(Schedulers.newSingle("origin"))
                .subscribeWith(customProcessor)
                .handle((element, synchronousSink) -> synchronousSink.next(element * 10))
                .subscribe(element -> System.out.println("UARGH " + element));
    }
}
