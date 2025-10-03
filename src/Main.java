
import ui.ApplicationController;

public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public void start() {
        ApplicationController controller = new ApplicationController();
        controller.demarrerApplication();
    }
}