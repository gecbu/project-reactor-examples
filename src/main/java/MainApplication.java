import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.BiFunction;

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

        /**
         * Subscribe to a stream which generates sums by applying Gaussian sum formula.
         *
         * This examples demonstrates a possible usage of the BiFunction in the generator method. As there is
         * no calculation limit, the subscriber has to define how many sums should be calculated (i.e. by take(n))
         * A new Thread is used for the subscription and disposed after the emitting of elements is completed.
         */
        Flux additionFlux = Flux.generate(
                () -> 1,
                new BiFunction<Integer, SynchronousSink<Integer>, Integer>() {
                    Integer currentSum = 0;

                    @Override
                    public Integer apply(Integer value, SynchronousSink<Integer> synchronousSink) {
                        currentSum += value;
                        synchronousSink.next(currentSum);
                        return value + 1;
                    }
                })
                .delayElements(Duration.ofSeconds(1));
        Scheduler additionThread = Schedulers.newSingle("AdditionThread");
        System.out.println("Subscribing to Flux generating Gaußsche Summenformel...");
        additionFlux
                .take(20)
                .subscribeOn(additionThread)
                .subscribe(
                        element -> System.out.println("Current sum: " + element),
                        error -> {},
                        () -> additionThread.dispose());

        // TODO: Inspect Scheduler.single()
        // Find out, why using Schedulers.single() does not keep an open thread until calculation is done
        // for the Gaussian Sum Formula

    }

}
