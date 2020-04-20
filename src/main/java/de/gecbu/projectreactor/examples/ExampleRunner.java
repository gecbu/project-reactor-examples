package de.gecbu.projectreactor.examples;

public class ExampleRunner {

    private ExampleRunner() {
        throw new AssertionError();
    }

    public static void run(String exampleNumber) {
        AbstractExample example = null;
        switch (exampleNumber) {
            case "1":
                example = new Example1();
                break;
            case "2":
                example = new Example2();
                break;
            case "3":
                example = new Example3();
                break;
            case "4":
                example = new Example4();
                break;
            case "5":
                example = new Example5();
                break;
            case SimpleFluxGeneratingExample.GENERATE_SIMPLE_FLUX:
                example = new SimpleFluxGeneratingExample();
                break;
            case FunctionalInterfaceCreationShortForm.FUNCT_INTERFACE_CREATION_SHORT_FORM:
                example = new FunctionalInterfaceCreationShortForm();
                break;
            default:
                throw new UnknownExampleException();
        }
        example.run();
    }

}
