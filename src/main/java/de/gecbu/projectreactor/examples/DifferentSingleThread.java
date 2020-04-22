package de.gecbu.projectreactor.examples;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Consumer;

/**
 * This example is used to show the difference between {@link Schedulers#single()} and {@link Schedulers#newSingle(String)}
 * <p>
 * Running this example will print all 10 range elements emitted by the second {@link Flux#range(int, int)}.
 * In comparison,
 * the first flux subscription will not print out all 60 numbers. The program completely terminates before all numbers
 * are emitted.
 * <p>
 * When using {@link Schedulers#single()} a new daemon thread is started. Calling {@link Schedulers#newSingle(String)}
 * instead starts a non-daemon (user) thread. While existing user threads prevent the application from terminating,
 * daemon threads do not. So as long as there are running/open user threads the application will be running, whereas
 * daemon threads are immediately terminated when the application stops running, independent of their state.
 * <p>
 * The following code demonstrates this behavior by subscribing to two {@link Flux}. The first one uses a
 * {@link Schedulers#single()},
 * and is intended to print a number every second and therefore running one minute in total (but actually does not!). In
 * contrast, the second one
 * is intended to print out one number every half second from 1 to 10 and therefore running 5 seconds. When the emitting of
 * numbers is completed the newSingle Scheduler is disposed. </br>
 * The program terminates after the second {@link Flux} has emitted all items although the emitting of the first {@link Flux}
 * is not
 * completed. This is due to the fact that only the second {@link Flux} uses a user thread (non-daemon) and the daemon-thread is
 * directly terminated
 * when the application stops running what is the case after the "newSingle" scheduler is disposed.
 * <p>
 * The commented code demonstrates how the example could be achieved with a {@link Schedulers#single()}. As
 * {@link Flux#blockLast()}
 * blocks further processing of the program until the emitting of items is completed all 60 numbers would be printed
 * to the console. But because {@link Flux#blockLast()} returns just the last item, {@link Flux#doOnNext(Consumer)}
 * is used for printing.
 */
public class DifferentSingleThread extends AbstractExample {

    public static final String NAME = "DIFFERENT_SINGLE_THREAD";

    @Override
    protected void run() {

        System.out.println("Running example 5...");

        System.out.println("Subscribe on first Flux range...");
        Flux.range(1, 60)
                .delayElements(Duration.ofMillis(1000))
                .publishOn(Schedulers.single())
                .subscribe(count -> System.out.println("No explicit blocking: " + Thread.currentThread().getName() + " count: " + count));

        System.out.println("Start emitting the second Flux range...");
        Scheduler newSingleScheduler = Schedulers.newSingle("newSingle-Thread");
        Flux.range(1, 10)
                .delayElements(Duration.ofMillis(500))
                .publishOn(newSingleScheduler)
                .subscribe(
                        count -> System.out.println("With explicit blocking: " + Thread.currentThread().getName() + " count: " + count),
                        error -> System.out.println("Error: " + error),
                        () -> newSingleScheduler.dispose());

//
//        System.out.println("Start emitting the second Flux range...");
//        Flux.range(1, 60)
//                .delayElements(Duration.ofMillis(1000))
//                .publishOn(Schedulers.single())
//                .doOnNext(count -> System.out.println("With explicit blocking: " + Thread.currentThread().getName() + "
//                count: " + count))
//                .blockLast();

    }
}
