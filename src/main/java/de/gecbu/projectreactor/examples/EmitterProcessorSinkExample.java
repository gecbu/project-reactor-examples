package de.gecbu.projectreactor.examples;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

/**
 * This class demonstrates how to basically publish elements to an EmitterProcessor via a sink
 */
public class EmitterProcessorSinkExample extends AbstractExample {

    public static final String NAME = "EMITTER_PROCESSOR_SINK_EXAMPLE";

    @Override
    protected void run() {
        /*
         * Step 1:
         * Publish items via a sink. It is important to note that the subscription happens before items
         * are published via the sink in the for-loop.
         * All items a printed to the console
         */

        EmitterProcessor<String> processor = EmitterProcessor.create();
        FluxSink<String> sink = processor.sink();
        processor.publishOn(Schedulers.newSingle("publishOn-Thread"))
                .subscribe(element -> System.out.println("(Step 1) Element: " + element));
        for (int i=1; i <= 100; i++) {
            sink.next(String.valueOf(i));
        }
    }

}
