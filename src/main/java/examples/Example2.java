package examples;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class Example2 extends AbstractExample {

    @Override
    protected void run() {
        /**
         * Subscribe to a stream, which emits elements with a delay.
         *
         * It is important to use a different thread. Otherwise the emitting and consuming of elements
         * will be done in the main thread, which terminates directly after the method call due to the
         * non-blocking processing. The elements might not be printed in console because the main
         * thread is already terminated.
         *
         * In this example a separate thread is used to handle the consuming (subscribing) of (to) the
         * emitted elements. When the subscription completes, the thread gets disposed so the main
         * application can terminate.
         */
        Scheduler integerThread = Schedulers.newSingle("IntegerThread");
        Flux delayedIntegerFlux = Flux.just(6, 5, 4, 3, 2, 1)
                .delayElements(Duration.ofSeconds(1));
        System.out.println("Subscribing to delayed Integer Flux");
        delayedIntegerFlux
                .subscribeOn(integerThread)
                .subscribe(
                        element -> System.out.println("Integer Flux: " + element),
                        error -> {
                        },
                        () -> integerThread.dispose());

        Flux delayedStringFlux = Flux.just("String 1", "String 2", "String 3", "String 4", "String 5")
                .delayElements(Duration.ofSeconds(1));
        Scheduler stringThread = Schedulers.newSingle("StringThread");
        System.out.println("Subscribing to a delayed String Flux");
        delayedStringFlux
                .subscribeOn(stringThread)
                .subscribe(
                        element -> System.out.println("String Flux: " + element),
                        error -> {
                        },
                        () -> stringThread.dispose());
    }
}
