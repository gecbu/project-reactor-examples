import de.gecbu.projectreactor.examples.ExampleRunner;

public class MainApplication {

    public static void main(String args[]) {
        /**
         * Runs the example given by the identifier/name. The examples are directly registered in the
         * ExampleRunner. Change the number to execute any other example.
         */
        try {
            ExampleRunner.run("5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
