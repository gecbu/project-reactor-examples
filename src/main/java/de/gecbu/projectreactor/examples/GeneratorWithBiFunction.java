package de.gecbu.projectreactor.examples;

import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.BiFunction;

/**
 * Subscribe to a stream which generates sums by applying Gaussian sum formula.
 * <p>
 * This examples demonstrates a possible usage of the BiFunction in the generator method. As there is no calculation limit, the
 * subscriber has to define how many sums should be calculated (i.e. by take(n)) A new thread is used for the subscription and
 * disposed after emitting of elements is completed.
 * <p>
 * By using Schedulers.newElastic() a worker pool is used. This enables setting the time, after which the worker pools
 * automatically get disposed. After the last worker pool is disposed, the main thread will terminate too.
 */
public class GeneratorWithBiFunction extends AbstractExample {

    public static final String GENERATOR_WITH_BIFUNCTION = "GENERATOR_WITH_BI_FUNCTION";

    @Override
    protected void run() {
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
                .delayElements(Duration.ofMillis(500));
        Scheduler publishThread = Schedulers.newElastic("PublishThread", 5);
        additionFlux
                .take(10)
                .publishOn(publishThread)
                .subscribe(
                        element -> System.out.println(Thread.currentThread().getName() + " Current sum: " + element),
                        error -> {},
                        () -> {});
    }

}
