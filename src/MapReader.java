import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.ButtonGroup;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MapReader {

    private JFrame frame;
    private final ButtonGroup TopLayer = new ButtonGroup();
    private boolean fileUploaded = false;
    private File fileImported;
    private BufferedReader reader;
    private static int lineCount = 1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MapReader window = new MapReader();
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
    public MapReader() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout springLayout = new SpringLayout();
        frame.getContentPane().setLayout(springLayout);
        frame.setMinimumSize(new Dimension(400, 253));

        JPanel panelTabel = new JPanel();
        springLayout.putConstraint(SpringLayout.SOUTH, panelTabel, 0, SpringLayout.SOUTH, frame.getContentPane());
        panelTabel.setBackground(Color.GRAY);
        springLayout.putConstraint(SpringLayout.NORTH, panelTabel, 0, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, panelTabel, 125, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panelTabel, 0, SpringLayout.EAST, frame.getContentPane());
        frame.getContentPane().add(panelTabel);


        JPanel panelRadio = new JPanel();
        panelRadio.setBackground(new Color(192, 192, 192));
        springLayout.putConstraint(SpringLayout.NORTH, panelRadio, 0, SpringLayout.NORTH, panelTabel);
        springLayout.putConstraint(SpringLayout.WEST, panelRadio, 0, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panelRadio, 100, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panelRadio, 0, SpringLayout.WEST, panelTabel);
        frame.getContentPane().add(panelRadio);
        SpringLayout sl_panelRadio = new SpringLayout();
        panelRadio.setLayout(sl_panelRadio);

        JRadioButton rdbtnAND = new JRadioButton("AND");
        sl_panelRadio.putConstraint(SpringLayout.WEST, rdbtnAND, 20, SpringLayout.WEST, panelRadio);
        sl_panelRadio.putConstraint(SpringLayout.EAST, rdbtnAND, -20, SpringLayout.EAST, panelRadio);
        TopLayer.add(rdbtnAND);
        rdbtnAND.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        sl_panelRadio.putConstraint(SpringLayout.NORTH, rdbtnAND, 5, SpringLayout.NORTH, panelRadio);
        panelRadio.add(rdbtnAND);

        JRadioButton rdbtnOR = new JRadioButton("OR");
        rdbtnOR.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
        sl_panelRadio.putConstraint(SpringLayout.NORTH, rdbtnOR, 0, SpringLayout.SOUTH, rdbtnAND);
        sl_panelRadio.putConstraint(SpringLayout.WEST, rdbtnOR, 0, SpringLayout.WEST, rdbtnAND);
        sl_panelRadio.putConstraint(SpringLayout.EAST, rdbtnOR, 0, SpringLayout.EAST, rdbtnAND);
        TopLayer.add(rdbtnOR);
        panelRadio.add(rdbtnOR);

        JRadioButton rdbtnCustom = new JRadioButton("Custom");
        sl_panelRadio.putConstraint(SpringLayout.NORTH, rdbtnCustom, 0, SpringLayout.SOUTH, rdbtnOR);
        sl_panelRadio.putConstraint(SpringLayout.WEST, rdbtnCustom, 0, SpringLayout.WEST, rdbtnOR);
        sl_panelRadio.putConstraint(SpringLayout.EAST, rdbtnCustom, 0, SpringLayout.EAST, rdbtnOR);
        panelRadio.add(rdbtnCustom);
        rdbtnCustom.setVisible(false);

        JSpinner CustomNum = new JSpinner();
        sl_panelRadio.putConstraint(SpringLayout.NORTH, CustomNum, 0, SpringLayout.SOUTH, rdbtnCustom);
        sl_panelRadio.putConstraint(SpringLayout.WEST, CustomNum, 20, SpringLayout.WEST, panelRadio);
        sl_panelRadio.putConstraint(SpringLayout.SOUTH, CustomNum, 0, SpringLayout.SOUTH, panelRadio);
        sl_panelRadio.putConstraint(SpringLayout.EAST, CustomNum, -20, SpringLayout.EAST, panelRadio);
        panelRadio.add(CustomNum);
        CustomNum.setVisible(false);


        JPanel panelButtons = new JPanel();
        springLayout.putConstraint(SpringLayout.NORTH, panelButtons, 0, SpringLayout.SOUTH, panelRadio);
        springLayout.putConstraint(SpringLayout.WEST, panelButtons, 0, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panelButtons, 0, SpringLayout.SOUTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panelButtons, 0, SpringLayout.WEST, panelTabel);
        frame.getContentPane().add(panelButtons);
        SpringLayout sl_panelButtons = new SpringLayout();
        panelButtons.setLayout(sl_panelButtons);

        JButton btnStart = new JButton("Start");
        sl_panelButtons.putConstraint(SpringLayout.NORTH, btnStart, -50, SpringLayout.SOUTH, panelButtons);
        sl_panelButtons.putConstraint(SpringLayout.WEST, btnStart, 5, SpringLayout.WEST, panelButtons);
        sl_panelButtons.putConstraint(SpringLayout.SOUTH, btnStart, -5, SpringLayout.SOUTH, panelButtons);
        sl_panelButtons.putConstraint(SpringLayout.EAST, btnStart, -5, SpringLayout.EAST, panelButtons);
        panelButtons.add(btnStart);

        JButton btnImport = new JButton("Import");
        btnImport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
                jfc.setFileFilter(filter);
                jfc.setDialogTitle("Choose a Mapper preset");
                int returnValue = jfc.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    fileImported = jfc.getSelectedFile();
                    fileUploaded = true;
                    try {
                        reader = new BufferedReader(new FileReader(fileImported));
                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    readImport(reader);
                }
            }
        });

        sl_panelButtons.putConstraint(SpringLayout.NORTH, btnImport, 7, SpringLayout.NORTH, panelButtons);
        sl_panelButtons.putConstraint(SpringLayout.WEST, btnImport, 3, SpringLayout.WEST, panelButtons);
        sl_panelButtons.putConstraint(SpringLayout.SOUTH, btnImport, 31, SpringLayout.NORTH, panelButtons);
        sl_panelButtons.putConstraint(SpringLayout.EAST, btnImport, -3, SpringLayout.EAST, panelButtons);
        panelButtons.add(btnImport);

        JButton btnEdit = new JButton("Edit");
        sl_panelButtons.putConstraint(SpringLayout.WEST, btnEdit, 0, SpringLayout.WEST, btnImport);
        sl_panelButtons.putConstraint(SpringLayout.EAST, btnEdit, 0, SpringLayout.EAST, btnImport);
        panelButtons.add(btnEdit);
        btnEdit.setVisible(false);

        JButton btnSettings = new JButton("Settings");
        sl_panelButtons.putConstraint(SpringLayout.NORTH, btnEdit, 1, SpringLayout.SOUTH, btnSettings);
        sl_panelButtons.putConstraint(SpringLayout.SOUTH, btnEdit, 25, SpringLayout.SOUTH, btnSettings);
        sl_panelButtons.putConstraint(SpringLayout.NORTH, btnSettings, 1, SpringLayout.SOUTH, btnImport);
        sl_panelButtons.putConstraint(SpringLayout.SOUTH, btnSettings, 25, SpringLayout.SOUTH, btnImport);
        sl_panelButtons.putConstraint(SpringLayout.WEST, btnSettings, 0, SpringLayout.WEST, btnEdit);
        sl_panelButtons.putConstraint(SpringLayout.EAST, btnSettings, 0, SpringLayout.EAST, btnEdit);
        panelButtons.add(btnSettings);
    }

    public static void readImport(BufferedReader bufferedText){
        try {
            String version = bufferedText.readLine();
            System.out.println(version);

            if (version.contentEquals("v1.0")) {
                readFile1p0(bufferedText);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void readFile1p0(BufferedReader mapperPoints){
        System.out.println("Now reading a v1.0 file");
        try {

            String line = mapperPoints.readLine();
            while(line != null) {
                System.out.println("Line number " + lineCount + " says: ");
                System.out.println(line);
                System.out.println();
                line = mapperPoints.readLine();
                lineCount++;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
