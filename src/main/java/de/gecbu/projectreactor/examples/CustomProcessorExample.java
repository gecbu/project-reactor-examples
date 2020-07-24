package de.gecbu.projectreactor.examples;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This example shows a very basic implementation of a custom flux processor.
 * The processor subscribes on integer values and publishes them as strings.
 */
public class CustomProcessorExample extends AbstractExample {

    public static final String NAME = "CustomProcessorExample";

    @Override
    protected void run() {

        FluxProcessor processor = Flux.range(1, 10).subscribeWith(createCustomProcessor());
        // The first subscriber will receive the numbers from 1..10
        processor.subscribe(System.out::println);
        processor.sink().next(11);
        // As the custom processor never dequeues elements, the second subscriber will receive all 10 previous added
        // integers as well as the number 11
        processor.subscribe(System.out::println);
    }

    /**
     * Creates a custom FluxProcessor. This processor has a queue which never will be emptied.
     * @return A custom FluxProcessor returning the String representation of integers
     */
    private FluxProcessor<Integer, String> createCustomProcessor() {
        return new FluxProcessor<>() {

            private Queue<Integer> queue = new LinkedBlockingQueue<>();

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext Processor");
                queue.add(integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }

            @Override
            public void subscribe(CoreSubscriber coreSubscriber) {
                System.out.println("subscribe");
                System.out.println("Queue: " + this.queue.size());
                this.queue.stream().forEach(number -> {
                            coreSubscriber.onNext(String.valueOf(number));
                        }
                );
                coreSubscriber.onComplete();
            }
        };
    }
}
