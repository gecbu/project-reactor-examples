package de.gecbu.projectreactor.examples;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * This example demonstrates how to publish elements with a {@link EmitterProcessor} via a {@link FluxSink}. The sink can be used
 * by different publisher at the same time.
 */
public class EmitterProcessorComplexExample extends AbstractExample {

    public static final String NAME = "EMITTER_PROCESSOR_COMPLEX_EXAMPLE";

    @Override
    protected void run() {
        EmitterProcessor<String> processor = EmitterProcessor.create();
        // Sobald diese Methode aufgerufen wird, bricht ein subscribeWith mit cancel ab
//        FluxSink<String> sink = processor.sink();

        // Verhalten prüfen für mehrere Subscriber
        // 1) Wie funktioniert dabei das Threading?
        // 2) Warum fängt subscriber2 erst so spät an, Ausgaben zu erzeugen?
        // Kann evtl. nur ein Single Thread gleichzeitig laufen?
        // 3) Wie kann demonstriert werden, dass der Buffer eines processors nur bis zu seiner Buffergröße gefüllt
        // wird, bis ein Subsciption erfolgt. Aktuell scheint es so als würden mehr Elemente in den Buffer geschrieben -> siehe
        // Code Ausführung

        // Unterscheiden zwischen Processor, der über ein Sink gefütter wird, oder der direkt als Subscriber mit einem Buffer eingesetzt wird


        processor.subscribe(element -> System.out.println("Element: " + element));

        EmitterProcessor<String> test = Flux.range(1, 10000).log()
                .map(String::valueOf)
                .subscribeWith(processor);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




//        CountDownLatch latch = new CountDownLatch(100);

//        processor.publishOn(Schedulers.newSingle("publishOn Thread (processor)"));
        // Fill buffer of emitter processor
        // Use different thread so the main thread is not blocked
//        Scheduler testPublish = Schedulers.newElastic("test");
//        Scheduler testSubscribe = Schedulers.newElastic("test");
//        Flux.range(1, 100)
//                .map(String::valueOf)
//                .publishOn(testPublish)
//                .subscribeOn(testSubscribe)
//                .subscribe(
//                        element -> {
//                            System.out.println("Thread = " + Thread.currentThread().getName() + ", element = " + element);
//                            processor.onNext(element);
//                            sink.next(element);
//                            System.out.println("Currently pending: " + processor.getPending());
//                        },
//                        (error) -> System.out.println(error),
//                        () -> {
//                            System.out.println("completed");
//                        });

        // Fill on main thread. Will block after buffer size is reached
//        for (int i = 1; i <= 100; i++) {
//            sink.next(String.valueOf(i));
//            System.out.println("Pending " + processor.getPending());
//            System.out.println("Fill up with element " + String.valueOf(i));
//        }

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Buffer with size " + processor.getBufferSize() + " is filled with " + processor.getPending() + " items");
//        processor.log().subscribe(element -> System.out.println("Getting " + element));

//
//        CustomSubscriber subscriber1 = new CustomSubscriber();
//        CustomSubscriber subscriber2 = new CustomSubscriber();
//        CustomSubscriber subscriber3 = new CustomSubscriber();
//


//        processor.subscribeOn(Schedulers.newElastic("subscribeOn Thread (subscriber1)"))
////                .delayElements(Duration.ofMillis(500))
//                .publishOn(Schedulers.newElastic("publishOn Thread (subscriber1)"))
////                .subscribe(element -> System.out.println(Thread.currentThread().getName() + " value = " + element));
//                .subscribe(subscriber1);

//        processor.subscribeOn(Schedulers.newElastic("subscribeOn Thread (subscriber2)"))
//                .publishOn(Schedulers.newElastic("publishOn Thread (subscriber2)"))
////                .subscribe(element -> System.out.println(Thread.currentThread().getName() + " value = " + element));
//                .subscribe(subscriber2);


//        Flux.range(1, 100)
//                .publishOn(Schedulers.newSingle("GENERATOR_PART_1-PUBLISH_ON"))
//                .map(element -> String.valueOf(element))
//                .delayElements(Duration.ofMillis(500))
//                .subscribeOn(Schedulers.newSingle("GENERATOR_PART1-SUBSCRIBE-ON"))
//                .subscribe(element -> {
//                    System.out.println("Subscribing to range part 1");
//                    sink.next(element);
//                });
//
//        Flux.range(101, 100)
//                .publishOn(Schedulers.newSingle("GENERATOR_PART_2-PUBLISH_ON"))
//                .map(element -> String.valueOf(element))
//                .delayElements(Duration.ofMillis(500))
//                .subscribeOn(Schedulers.newSingle("GENERATOR_PART2-SUBSCRIBE-ON"))
//                .subscribe(element -> {
//                    System.out.println("Subscribing to range part 2");
//                    sink.next(element);
//                });
//
//        sink.next("This is a test");
//        sink.next("With some strings");
//        sink.next("Produced by a sink");

    }

    private static class CustomSubscriber implements Subscriber<String> {
        @Override
        public void onSubscribe(Subscription subscription) {
            System.out.println("onSubscribe Thread = " + Thread.currentThread().getName());
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(String s) {
            System.out.println(String.format("onNext Thread = %s; next element = %s", Thread.currentThread().getName(), s));
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
}
