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

The coding examples 1, 2, 3 and 5 use one of these basic approaches. 

## Using a generator

Another way of creating a Flux stream is by implementing a generator function and thereby creating a sequence programmatically. The generator is handed to the Flux.generate method as a parameter.

The simplest form of a generator is a Consumer which expects a SynchronousSink<T> as a paremeter for its accept method. The accept methods gets invoked every time the subscriber requests the next element of the sequence. It is important to know that the default subscribe methods request an unbounded demand. So you have to make sure, that you control the completion in your accept method by using sink.complete() or do this with a custom subscriber which handles the amount of elements to react to.

Exmaple "GENERATE_SIMPLE_FLUX" shows this approach for generating a stream.

A more advanced way is to use a BiFunction.
tbd.

# Upcoming
* Description of different ways to create and consume streams
* How to work with Threads
* Difference between Cold and Hot sequences