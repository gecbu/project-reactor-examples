import examples.ExampleRunner;

public class MainApplication {

    public static void main(String args[]) {

        /**
         * Runs the example given by the identifier/name. The examples are directly registered in the
         * ExampleRunner. Change the number to execute any other example.
         */
        ExampleRunner.run("4");

        // TODO: Inspect Scheduler.single()
        // Single maybe executed but shutdown immediately. Has to be tested with a long running operation in
        // the background
        // (Was: Find out, why using Schedulers.single() does not keep an open thread until calculation is done
        // for the Gaussian Sum Formula)

    }

}
