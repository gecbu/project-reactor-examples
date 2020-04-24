# Purpose of This Repository
This repository documents basic usage of Reactor (https://projectreactor.io/) and is meant to be a personal "Reactor Knowledge Center" in the long term. It does not claim to be fully covering all possibilities with Reactor, nor to be built up didactically or methodically correct. Nevertheless it might help other programmers to get started with Reactor too. 

References used:
* [Official Website](https://projectreactor.io/)
* [Official API](https://projectreactor.io/docs/core/release/api)
* [Hopping Threads and Schedulers](https://spring.io/blog/2019/12/13/flight-of-the-flux-3-hopping-threads-and-schedulers)

# Difference between Mono and Flux

A Mono is a Publisher that emits one item at most. In contrast, a Flux is a Publisher that can emit 0 to N items. Both are working asynchronously and can optionally emit an error or completed signal.

More information can be found in the reference: 
[Core Features](https://projectreactor.io/docs/core/release/reference/#core-features)

# Ways to create a stream (Flux)

## Basic approach

The Flux-API provides a lot of different ways to create/generate a stream. The simplest ones are those available through one of the factory methods. Examples are:
* Flux.fromArray(array)
* Flux.just(items ...)
* Flux.range(int start, int count)

The following examples use this approach:
* BASIC_FLUX_CREATION
* DIFFERENT_SINGLE_THREAD
* SIMPLE_THREADING

## Using a generator

Another way of creating a Flux stream is by implementing a generator function and thereby creating a sequence programmatically. The generator is handed to the Flux.generate method as a parameter.

The simplest form of a generator is a Consumer which expects a SynchronousSink<T> as a paremeter for its accept method. The accept methods gets invoked every time the subscriber requests the next element of the sequence. It is important to know that the default subscribe methods request an unbounded demand. So you have to make sure that you control the completion in your accept method by using sink.complete() or do this with a custom subscriber which handles the amount of elements to react to.

Exmaple GENERATE_SIMPLE_FLUX shows this approach for generating a stream.

How to use a short form of consumer creation (lambda) is shown in example "FUNCT_INTERFACE_CREATION_SHORT_FORM". It uses a Mono and not a Flux but the handling would be the same.

A more advanced way to create a stream is via a BiFunction as a generator function:

`generate(Callable<S>, BiFunction<S, SynchronousSink<T>, S>) `

With this approach it is possible to use a state S in the generator function. Depending on this state, the processing and emitting of stream elements may vary and finally the state can be returned and used on the next request of the subscriber. As with the simple consumer it is the responsibility of the developer to finish the stream of elements by providing a `sink.complete()` or subscribing for just a specific amount of items.
The Example GENERATOR_WITH_BI_FUNCTION shows the usage of this approach.

# Threading

Basic information can be found at [Reference Guide - Threading and Schedulers](https://projectreactor.io/docs/core/release/reference/#schedulers). 

A `Scheduler` is a helper to abstract from the direct handling with Threads and has "scheduling responsibilities similar to an ExecutorService" (see Reference Guide).

When it comes to `Schedulers.single()` and `Schedulers.newSingle()` there is an important thing worth mentioning. One has to be aware that the `Schedulers.single()` will make the execution run in a daemon thread, while `Schedulers.newSinlge()` will make it run in a user (non-daemon) thread. Daemon threads are instantly closed when all other user threads are terminated. A more detailed explanation is given through the following example: DIFFERENT_SINGLE_THREAD


# Upcoming
* How to work with Threads
* Difference between Cold and Hot sequences