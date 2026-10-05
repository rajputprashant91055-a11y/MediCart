import javax.swing.SwingUtilities;
import ui.HomeFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Create Home Screen
            HomeFrame homeFrame = new HomeFrame();

            // Display Home Screen
            homeFrame.setVisible(true);
        });
    }
}