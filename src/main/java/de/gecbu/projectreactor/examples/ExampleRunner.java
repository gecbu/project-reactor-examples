package de.gecbu.projectreactor.examples;

public class ExampleRunner {

    private ExampleRunner() {
        throw new AssertionError();
    }

    public static void run(String exampleNumber) {
        AbstractExample example = null;
        switch (exampleNumber) {
            case BasicFluxCreation.NAME:
                example = new BasicFluxCreation();
                break;
            case SimpleThreading.NAME:
                example = new SimpleThreading();
                break;
            case GeneratorWithSimpleBiFunctionLambda.NAME:
                example = new GeneratorWithSimpleBiFunctionLambda();
                break;
            case DifferentSingleThread.NAME:
                example = new DifferentSingleThread();
                break;
            case SimpleFluxGeneratingExample.NAME:
                example = new SimpleFluxGeneratingExample();
                break;
            case FunctionalInterfaceCreationShortForm.NAME:
                example = new FunctionalInterfaceCreationShortForm();
                break;
            case GeneratorWithBiFunction.NAME:
                example = new GeneratorWithBiFunction();
                break;
            default:
                throw new UnknownExampleException();
        }
        example.run();
    }

}
