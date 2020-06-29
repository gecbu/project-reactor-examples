package de.gecbu.projectreactor.examples;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

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
                .subscribe(element -> System.out.println("(Step 1) Consume element: " + element));
        for (int i = 1; i <= 100; i++) {
            sink.next(String.valueOf(i));
        }

        /*
         * Step 2:
         * Publish items before a subscriber is connected
         * When called without parameters, create() establishes a new EmitterProcessor with Queues.SMALL_BUFFER_SIZE ( = 256).
         * As stated in the documentation calls to onNext() block until the processor is drained. Therefore the publishing
         * needs to happen in a different thread.
         */

        EmitterProcessor<String> processor1 = EmitterProcessor.create();
        FluxSink<String> sink1 = processor1.sink();
        Runnable runnable = () -> {
            for (int i = 1; i <= 260; i++) {
                sink1.next(String.valueOf(i));
                System.out.println("Published " + i + " on Thread " + Thread.currentThread().getName());
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        try {
            /* Sleep long enough to show that the publishing thread (which is executing the runnable) stops after the buffer is
            filled. The items 257 to 260 will not be published (no console output), because the publishing thread is blocked.
            We are only blocking the main thread here and the publisher could still be publishing the last items 257 to 260
            thread as it runs on a different thread but due to the filled buffer of the emitter processor this does not happen.
             */
            Thread.sleep(2000);
            System.out.println("Awake!!!");
            processor1
                    .subscribe(element -> {
                        /* As soon as an element is consumed by the subscriber the buffer can be filled
                        directly with the next element by the publisher. To demonstrate this behavior we interrupt the
                        processing what will lead to an alternating output of the subscriber and the publisher. Do not be
                        irritated by the order of the console output. The subscribing and the publishing will be done almost
                        at the exact same point of time. The important point is that the publishing of elements 257 to 260
                        will only happen if another element in the buffer queue is consumed by the subscriber.
                         */
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("(Step 2) Consuming element: " + element);

                    });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
