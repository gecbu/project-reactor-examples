import de.gecbu.projectreactor.examples.*;

public class MainApplication {

    public static void main(String[] args) {
        /**
         * Runs the example given by the identifier/name. The examples are directly registered in the
         * ExampleRunner. Change the name to execute any other example.
         */
        try {
            ExampleRunner.run(CustomProcessorExample.NAME);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
