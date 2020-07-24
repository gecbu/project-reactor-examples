package de.gecbu.projectreactor.examples;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.logging.Level;

/**
 * This example shows how an EmitterProcessor emits items to multiple subscribers
 */
public class EmitterProcessorHub extends AbstractExample {

    public static final String NAME = "EMITTER_PROCESSOR_HUB";

    @Override
    protected void run() {
        EmitterProcessor<String> processor = EmitterProcessor.create();
        FluxSink<String> sink = processor.sink();
        processor
                .subscribeOn(Schedulers.newSingle("Thread SubscribeOn 1"))
                .publishOn(Schedulers.newSingle("Thread PublishOn 1"))
                .log()
                .subscribe(createSubscriber("Subscriber 1"));
//        processor
//                .subscribeOn(Schedulers.newSingle("Thread SubscribeOn 2"))
//                .publishOn(Schedulers.newSingle("Thread PublishOn 2"))
//                .subscribe(createSubscriber("Subscriber 2"));
//        processor
//                .subscribeOn(Schedulers.newSingle("Thread SubscribeOn 3"))
//                .publishOn(Schedulers.newSingle("Thread PublishOn 3"))
//                .subscribe(createSubscriber("Subscriber 3"));

        // Prüfen, warum onSubscribe nicht auf dem newSingle Thread ausgeführt wird, sondern im main Thread

        // log() zeigt in der Konsole an, dass onSubscribe im erwarteten Thread ausgeführt wird. Die Ausgabe in onSubscribe
        // des createSubscribers gibt die Ausgabe auf die Konsole aber im main thread.
        // evtl. bezieht sich das subscribeOn erst auf den Moment, bei dem die request() Methode aufgerufen wird?

        for (int i = 1; i <= 100; i++) {
            sink.next("String " + i);
        }
    }


    private Subscriber<String> createSubscriber(String name) {
        return new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
//                System.out.println(name + " subscribed on Thread " + Thread.currentThread().getName());
//                System.out.println("REQUESTING unbounded on " + Thread.currentThread().getName());
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
//                System.out.println(name + " on next: " + s + " Thread: " + Thread.currentThread().getName());

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(name + " has error!");
            }

            @Override
            public void onComplete() {
                System.out.println(name + " completed!");
            }
        };
    }

    private Subscriber<String> createSubscriber(String name, int requestSize) {
        return new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
//                System.out.println(name + " subscribed on Thread " + Thread.currentThread().getName());
                System.out.println("REQUESTING for " + name + ", " + requestSize + " on " + Thread.currentThread().getName());
                subscription.request(requestSize);
            }

            @Override
            public void onNext(String s) {
                System.out.println(name + " on next: " + s + " Thread: " + Thread.currentThread().getName());

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(name + " has error!");
            }

            @Override
            public void onComplete() {
                System.out.println(name + " completed!");
            }
        };
    }


}
