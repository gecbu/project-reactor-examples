import de.gecbu.projectreactor.examples.EmitterProcessorComplexExample;
import de.gecbu.projectreactor.examples.EmitterProcessorSinkExample;
import de.gecbu.projectreactor.examples.EmitterProcessorWithPublisher;
import de.gecbu.projectreactor.examples.ExampleRunner;

public class MainApplication {

    public static void main(String[] args) {
        /**
         * Runs the example given by the identifier/name. The examples are directly registered in the
         * ExampleRunner. Change the number to execute any other example.
         */
        try {
            ExampleRunner.run(EmitterProcessorWithPublisher.NAME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
