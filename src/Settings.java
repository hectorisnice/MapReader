import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Settings {

    private JFrame frame;
    private JTextField textFieldSerialPort;
    private JButton btnSave;
    private static Settings window;

    /**
     * Launch the application.
     */
    public static void launch() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    window = new Settings();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Settings() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Settings");
        frame.setBounds(MapReader.getWindow().getFrame().getX() + MapReader.getWindow().getFrame().getWidth(), MapReader.getWindow().getFrame().getY(), 300, 200);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        frame.getContentPane().setLayout(springLayout);

        btnSave = new JButton("Save");
        springLayout.putConstraint(SpringLayout.WEST, btnSave, 10, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, btnSave, -10, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, btnSave, 90, SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(btnSave);


        JButton btnCancel = new JButton("Cancel");
        springLayout.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, btnSave);
        springLayout.putConstraint(SpringLayout.WEST, btnCancel, 6, SpringLayout.EAST, btnSave);
        springLayout.putConstraint(SpringLayout.EAST, btnCancel, 90, SpringLayout.EAST, btnSave);
        frame.getContentPane().add(btnCancel);

        JLabel lblSerialPort = new JLabel("SerialPort:");
        springLayout.putConstraint(SpringLayout.NORTH, lblSerialPort, 10, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, lblSerialPort, 10, SpringLayout.WEST, frame.getContentPane());
        frame.getContentPane().add(lblSerialPort);

        textFieldSerialPort = new JTextField();
        textFieldSerialPort.setText(MapReader.getSerialPort());
        springLayout.putConstraint(SpringLayout.NORTH, textFieldSerialPort, 1, SpringLayout.SOUTH, lblSerialPort);
        springLayout.putConstraint(SpringLayout.WEST, textFieldSerialPort, 0, SpringLayout.WEST, btnSave);
        frame.getContentPane().add(textFieldSerialPort);
        textFieldSerialPort.setColumns(10);

        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MapReader.setSerialPort(textFieldSerialPort.getText());
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textFieldSerialPort.setText(MapReader.getSerialPort());
            }
        });
    }
}
