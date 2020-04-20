package de.gecbu.projectreactor.examples;

import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.function.Consumer;

/**
 * This example shows how to create a Mono programmatically with concise lambda syntax.
 */
public class FunctionalInterfaceCreationShortForm extends AbstractExample {

    public static final String FUNCT_INTERFACE_CREATION_SHORT_FORM = "FUNCT_INTERFACE_CREATION_SHORT_FORM";

    @Override
    protected void run() {
        // Short form of consumer creation. As the Consumer is a Functional Interface a lambda expression can be used.
        Consumer<MonoSink<Integer>> consumer = sink -> sink.success(1024);
        Mono.create(consumer)
                .subscribe(element -> System.out.println("Mono created " + element));
    }

}
