import de.gecbu.projectreactor.examples.ExampleRunner;
import de.gecbu.projectreactor.examples.SimpleFluxGeneratingExample;

public class MainApplication {

    public static void main(String args[]) {
        /**
         * Runs the example given by the identifier/name. The examples are directly registered in the
         * ExampleRunner. Change the number to execute any other example.
         */
        try {
            ExampleRunner.run(SimpleFluxGeneratingExample.GENERATE_SIMPLE_FLUX);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
