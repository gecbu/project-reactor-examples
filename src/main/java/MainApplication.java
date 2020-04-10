import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class MainApplication {

    public static void main(String args[]) {

        /**
         * Create a Flux stream from the integer values of an array.
         */
        Flux<Integer> integerFlux = Flux.fromArray(new Integer[]{1, 2, 3, 4, 5});

        /**
         * Subscribe to a stream of elements. In this case a stream of Integers.
         *
         * Although it seems all integers are displayed together at once, they are
         * actually streamed one after another.
         */
        System.out.println("Subscribing to immediate Integer-Flux...");
        integerFlux.subscribe(element -> System.out.println(element));


        /**
         * Subscribe to the same integer stream again.
         *
         * Be aware that all elements are streamed again on subscription and therefore
         * printed again.
         */
        System.out.println("Subscribing again to immediate Integer-Flux...");
        integerFlux.subscribe(element -> System.out.println(element));

        /**
         * Create a new stream with a generator and subscribe to the emitted items.
         */
        Flux<Integer> customIntFlux = Flux.generate(
                () -> 0, // The first element to be emitted
                (state, sink) -> {
                    sink.next(state + 1);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        System.out.println("Subscribing to the custom generated stream...");
        customIntFlux.subscribe(number -> System.out.println("Generated number: " + number));

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
                        error -> {},
                        () -> integerThread.dispose());

        Flux delayedStringFlux = Flux.just("String 1", "String 2", "String 3", "String 4", "String 5")
                .delayElements(Duration.ofSeconds(1));
        Scheduler stringThread = Schedulers.newSingle("StringThread");
        System.out.println("Subscribing to a delayed String Flux");
        delayedStringFlux
                .subscribeOn(stringThread)
                .subscribe(
                        element -> System.out.println("String Flux: " + element),
                        error -> {},
                        () -> stringThread.dispose());
    }

}
