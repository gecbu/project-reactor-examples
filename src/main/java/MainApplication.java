import de.gecbu.projectreactor.examples.ExampleRunner;
import de.gecbu.projectreactor.examples.FunctionalInterfaceCreationShortForm;

public class MainApplication {

    public static void main(String[] args) {
        /**
         * Runs the example given by the identifier/name. The examples are directly registered in the
         * ExampleRunner. Change the number to execute any other example.
         */
        try {
            ExampleRunner.run(FunctionalInterfaceCreationShortForm.FUNCT_INTERFACE_CREATION_SHORT_FORM);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
