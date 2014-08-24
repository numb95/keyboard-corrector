package keyboardcorrector;

/**
 *
 * @author mjafar
 */
public class KeyboardCorrector {

    public static KeyboardCorrectorEngine processor;
    public static Form form;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        form = new Form();
        processor = new KeyboardCorrectorEngine();

        /* Create and display the form */
        form.setVisible(true);
    }
}
