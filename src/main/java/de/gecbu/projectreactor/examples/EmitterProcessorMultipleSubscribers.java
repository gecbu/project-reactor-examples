package de.gecbu.projectreactor.examples;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * This example shows how an EmitterProcessor emits items to multiple subscribers
 */
public class EmitterProcessorMultipleSubscribers extends AbstractExample {

    public static final String NAME = "EMITTER_PROCESSOR_MULTIPLE_SUBSCRIBERS";

    @Override
    protected void run() {
        EmitterProcessor<String> processor = EmitterProcessor.create();

        Scheduler schedulerPublish1 = Schedulers.newSingle("Thread Publish 1");
        Scheduler schedulerPublish2 = Schedulers.newSingle("Thread Publish 2");
        Scheduler schedulerPublish3 = Schedulers.newSingle("Thread Publish 3");

        processor
                .publishOn(schedulerPublish1)
                .subscribe(createSubscriber("Subscriber 1"));
        processor
                .publishOn(schedulerPublish2)
                .subscribe(createSubscriber("Subscriber 2"));
        processor
                .publishOn(schedulerPublish3)
                .subscribe(createSubscriber("Subscriber 3"));

        Flux<Integer> publisher1 = Flux.range(1, 5);

        publisher1
                .map(String::valueOf)
                .subscribe(processor);

        schedulerPublish1.dispose();
        schedulerPublish2.dispose();
        schedulerPublish3.dispose();
    }


    private Subscriber<String> createSubscriber(String name) {
        return new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println(name + " subscribed on Thread " + Thread.currentThread().getName());
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println(name + " on next: " + s + " Thread: " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(name + " has error!" + throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println(name + " completed!");
            }
        };
    }

}
