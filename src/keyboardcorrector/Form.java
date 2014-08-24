package keyboardcorrector;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author mjafar
 */
public class Form extends javax.swing.JDialog {

    public static final Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    public final Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
    private static final Image TRAY_ICON = readImage("icon.png");
    private static final Image APP_ICON = readImage("AppIcon.png");
    private TrayIcon trayIcon;
    private Point dragOrigin;
    private boolean alwaysOnTop = false;
    private boolean lock = false;
    //private static final int SOURCE_PER = 0;
    //private static final int SOURCE_ENG = 1;

    /**
     * Creates new form Form
     */
    public Form() {
        initComponents();

        GraphicsDevice gd = getGraphicsConfiguration().getDevice();
        if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
            setOpacity(0.9F);
        }

        txtPer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        txtEng.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                if (lock) {
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lock = true;
                            if (txtEng.getText().length() == txtPer.getText().length()) {
                                return;
                            }
                            String before = txtPer.getText().substring(0, e.getOffset());
                            String newText = txtEng.getText().substring(e.getOffset(), e.getOffset() + e.getLength());
                            String after;
                            if (txtPer.getText().length() >= e.getOffset()) {
                                after = txtPer.getText().substring(e.getOffset());
                            } else {
                                after = "";
                            }
                            txtPer.setText(before + KeyboardCorrector.processor.convert(newText) + after);
                        } finally {
                            lock = false;
                        }
                    }
                });
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                if (lock) {
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lock = true;
                            if (txtEng.getText().length() == txtPer.getText().length()) {
                                return;
                            }
                            if (txtEng.getText().length() == 0) {
                                txtPer.setText("");
                            }
                            String before = txtPer.getText().substring(0, e.getOffset());
                            String after;
                            if (txtPer.getText().length() >= e.getOffset() + e.getLength()) {
                                after = txtPer.getText().substring(e.getOffset() + e.getLength());
                            } else {
                                after = "";
                            }
                            txtPer.setText(before + after);
                        } finally {
                            lock = false;
                        }
                    }
                });
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (lock) {
                    return;
                }
            }
        });

        txtPer.getDocument()
                .addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                if (lock) {
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lock = true;
                            if (txtEng.getText().length() == txtPer.getText().length()) {
                                return;
                            }
                            String before = txtEng.getText().substring(0, e.getOffset());
                            String newText = txtPer.getText().substring(e.getOffset(), e.getOffset() + e.getLength());
                            String after;
                            if (txtEng.getText().length() >= e.getOffset()) {
                                after = txtEng.getText().substring(e.getOffset());
                            } else {
                                after = "";
                            }
                            txtEng.setText(before + KeyboardCorrector.processor.convert(newText) + after);
                        } finally {
                            lock = false;
                        }
                    }
                });
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                if (lock) {
                    return;
                }
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lock = true;
                            if (txtEng.getText().length() == txtPer.getText().length()) {
                                return;
                            }
                            if (txtPer.getText().length() == 0) {
                                txtEng.setText("");
                            }
                            String before = txtEng.getText().substring(0, e.getOffset());
                            String after;
                            if (txtEng.getText().length() >= e.getOffset() + e.getLength()) {
                                after = txtEng.getText().substring(e.getOffset() + e.getLength());
                            } else {
                                after = "";
                            }
                            txtEng.setText(before + after);
                        } finally {
                            lock = false;
                        }
                    }
                });
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (lock) {
                    return;
                }
            }
        });

        this.getContentPane()
                .setBackground(Color.WHITE);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int x = (int) scrSize.width - this.getWidth();
        int y = (int) scrSize.height - this.getHeight() - toolHeight.bottom;

        this.setLocation(x, y);

        makeTrayIcon();
    }

    private static Image readImage(String fileName) {
        URL resource = Form.class
                .getResource(fileName);


        try {
            return ImageIO.read(resource);
        } catch (IOException e) {
            return null;
        }
    }

    public void makeTrayIcon() {

        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            trayIcon = new TrayIcon(Form.TRAY_ICON, "Keyboard corrector", rightClickMenu);
            trayIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Form.this.mnuShowHideActionPerformed(null);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rightClickMenu = new java.awt.PopupMenu();
        mnuStandard = new java.awt.CheckboxMenuItem();
        mnuAlwaysOnTop = new java.awt.CheckboxMenuItem();
        mnuShowHide = new java.awt.MenuItem();
        mnuExit = new java.awt.MenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtEng = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPer = new javax.swing.JTextArea();

        rightClickMenu.setLabel("Keyboard Corrector");
        rightClickMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightClickMenuActionPerformed(evt);
            }
        });

        mnuStandard.setEnabled(false);
        mnuStandard.setLabel("Standard Layout");
        mnuStandard.setState(true);
        mnuStandard.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnuStandardItemStateChanged(evt);
            }
        });
        rightClickMenu.add(mnuStandard);
        rightClickMenu.addSeparator();
        mnuAlwaysOnTop.setLabel("Always on top");
        mnuAlwaysOnTop.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mnuAlwaysOnTopItemStateChanged(evt);
            }
        });
        rightClickMenu.add(mnuAlwaysOnTop);

        mnuShowHide.setLabel("Show / Hide");
        mnuShowHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuShowHideActionPerformed(evt);
            }
        });
        rightClickMenu.add(mnuShowHide);
        rightClickMenu.addSeparator();
        mnuExit.setLabel("Exit");
        mnuExit.setName("");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        rightClickMenu.add(mnuExit);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(Form.APP_ICON);
        setName("frmMain"); // NOI18N
        setUndecorated(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txtEng.setBackground(new java.awt.Color(249, 249, 249));
        txtEng.setColumns(20);
        txtEng.setLineWrap(true);
        txtEng.setRows(5);
        txtEng.setName("txtEng"); // NOI18N
        txtEng.setNextFocusableComponent(txtPer);
        jScrollPane1.setViewportView(txtEng);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        txtPer.setBackground(new java.awt.Color(249, 249, 249));
        txtPer.setColumns(20);
        txtPer.setLineWrap(true);
        txtPer.setRows(5);
        txtPer.setName("txtPer"); // NOI18N
        txtPer.setNextFocusableComponent(txtEng);
        jScrollPane2.setViewportView(txtPer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rightClickMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightClickMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rightClickMenuActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        Point p = evt.getLocationOnScreen();
        p.x -= dragOrigin.x;
        p.y -= dragOrigin.y;
        this.setLocation(p);
    }//GEN-LAST:event_formMouseDragged

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        dragOrigin = evt.getPoint();
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON2) {
            rightClickMenu.show(this, evt.getPoint().x, evt.getPoint().y);
        }
    }//GEN-LAST:event_formMousePressed

    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        dispose();
        SystemTray.getSystemTray().remove(trayIcon);
        System.exit(0);
    }//GEN-LAST:event_mnuExitActionPerformed

    private void mnuShowHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuShowHideActionPerformed
        if (isVisible()) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }//GEN-LAST:event_mnuShowHideActionPerformed

    private void mnuAlwaysOnTopItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnuAlwaysOnTopItemStateChanged
        alwaysOnTop = !alwaysOnTop;
        setAlwaysOnTop(alwaysOnTop);
        mnuAlwaysOnTop.setState(alwaysOnTop);
    }//GEN-LAST:event_mnuAlwaysOnTopItemStateChanged

    private void mnuStandardItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mnuStandardItemStateChanged
        //KeyboardCorrector.processor.setStandardLayout(mnuStandard.getState());
        //refreshText();
    }//GEN-LAST:event_mnuStandardItemStateChanged
    private final Color clr = new java.awt.Color(96, 211, 63);

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(clr);
        g2d.drawRect(0, 0, getWidth() - (1 << 1) + 1, getHeight() - (1 << 1) + 1);
        g2d.drawRect(1, 1, getWidth() - (2 << 1) + 1, getHeight() - (2 << 1) + 1);
        g2d.drawRect(2, 2, getWidth() - (3 << 1) + 1, getHeight() - (3 << 1) + 1);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.CheckboxMenuItem mnuAlwaysOnTop;
    private java.awt.MenuItem mnuExit;
    private java.awt.MenuItem mnuShowHide;
    private java.awt.CheckboxMenuItem mnuStandard;
    private java.awt.PopupMenu rightClickMenu;
    private javax.swing.JTextArea txtEng;
    private javax.swing.JTextArea txtPer;
    // End of variables declaration//GEN-END:variables

    private void refreshText(final int source) {
        /*
         SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
         if (txtPer.getText().length() != txtEng.getText().length()) {
         if (source == SOURCE_ENG) {
         txtPer.setText(KeyboardCorrector.processor.convert(txtEng.getText()));
         } else {
         txtEng.setText(KeyboardCorrector.processor.convert(txtPer.getText()));
         }
         }
         }
         });
         * */
    }
}
