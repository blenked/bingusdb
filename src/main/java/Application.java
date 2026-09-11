import ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorado) {
            }
            new MainFrame().setVisible(true);
        });
    }
}