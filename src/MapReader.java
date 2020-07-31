import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class MapReader {

    private JFrame frame;
    private final ButtonGroup TopLayer = new ButtonGroup();
    private boolean fileUploaded = false;
    private File fileImported;
    private BufferedReader reader;
    private static int lineCount = 1;
    private JTable table;
    private static ArrayList<Pixel> points = new ArrayList<>();
    private static int screenHeight;
    private static int screenWidth;
    private Robot r = new Robot();
    private boolean abort = false;

    public static void update(JTable tab) {
        TableModel t = new DefaultTableModel(new String[]{"#", "Name", "R", "G", "B", "T", "X", "Y"},
                points.size()) {
            private static final long serialVersionUID = 1L;
            boolean[] columnEditables = new boolean[]{false, false, false, false, false, false, false, false};

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        tab.setModel(t);
        tab.getColumnModel().getColumn(0).setPreferredWidth(10);
        tab.getColumnModel().getColumn(1).setPreferredWidth(50);
        tab.getColumnModel().getColumn(2).setPreferredWidth(10);
        tab.getColumnModel().getColumn(3).setPreferredWidth(10);
        tab.getColumnModel().getColumn(4).setPreferredWidth(10);
        tab.getColumnModel().getColumn(5).setPreferredWidth(10);
        tab.getColumnModel().getColumn(6).setPreferredWidth(10);
        tab.getColumnModel().getColumn(7).setPreferredWidth(10);
        for (int i = 0; i < points.size(); i++) {
            points.get(i).setPixelNumber(i + 1);
            t.setValueAt(points.get(i).getPixelNumber(), i, 0);
            t.setValueAt(points.get(i).getNickName(), i, 1);
            t.setValueAt(points.get(i).getR(), i, 2);
            t.setValueAt(points.get(i).getG(), i, 3);
            t.setValueAt(points.get(i).getB(), i, 4);
            t.setValueAt(points.get(i).getT(), i, 5);
            t.setValueAt(points.get(i).getX(), i, 6);
            t.setValueAt(points.get(i).getY(), i, 7);
        }
        Pixel.setPixelCounter(points.size());
    }

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
    public MapReader() throws AWTException {
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

        JScrollPane panelTable = new JScrollPane();
        springLayout.putConstraint(SpringLayout.SOUTH, panelTable, 0, SpringLayout.SOUTH, frame.getContentPane());
        panelTable.setBackground(Color.GRAY);
        springLayout.putConstraint(SpringLayout.NORTH, panelTable, 0, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, panelTable, 125, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panelTable, 0, SpringLayout.EAST, frame.getContentPane());
        frame.getContentPane().add(panelTable);

        JPanel panelRadio = new JPanel();
        panelRadio.setBackground(new Color(192, 192, 192));
        springLayout.putConstraint(SpringLayout.NORTH, panelRadio, 0, SpringLayout.NORTH, panelTable);
        springLayout.putConstraint(SpringLayout.WEST, panelRadio, 0, SpringLayout.WEST, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, panelRadio, 100, SpringLayout.NORTH, frame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, panelRadio, 0, SpringLayout.WEST, panelTable);
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
        springLayout.putConstraint(SpringLayout.EAST, panelButtons, 0, SpringLayout.WEST, panelTable);

        table = new JTable();
        panelTable.setViewportView(table);
        frame.getContentPane().add(panelButtons);
        SpringLayout sl_panelButtons = new SpringLayout();
        panelButtons.setLayout(sl_panelButtons);

        SwingWorker<Integer, Integer> scan = new SwingWorker<Integer, Integer>() {
            @Override
            protected Integer doInBackground() throws Exception {
                boolean is = false;
                boolean was = false;
                boolean printed = false;
                int mode = 0;
                if (TopLayer.isSelected((rdbtnOR.getModel()))) {
                    mode = 1;
                } else if (TopLayer.isSelected((rdbtnAND.getModel()))) {
                    mode = 2;
                }
                if (mode == 1) {
                    while (!abort) {
                        is = false;
                        for (Pixel p : points) {
                            if (Math.abs(r.getPixelColor(p.getX(), p.getY()).getRed() - p.getR()) <= p.getT() && Math.abs(r.getPixelColor(p.getX(), p.getY()).getGreen() - p.getG()) <= p.getT() && Math.abs(r.getPixelColor(p.getX(), p.getY()).getBlue() - p.getB()) <= p.getT()) {
                                is = true;
                                break;
                            }
                        }
                        if (is && !was) {
                            System.out.println("yes");
                            was = true;
                        } else if (!is && was) {
                            System.out.println("no");
                            was = false;
                        }
                    }
                } else if (mode == 2) {
                    while (!abort) {
                        is = true;
                        for (Pixel p : points) {
                            if (!(Math.abs(r.getPixelColor(p.getX(), p.getY()).getRed() - p.getR()) <= p.getT()) || !(Math.abs(r.getPixelColor(p.getX(), p.getY()).getGreen() - p.getG()) <= p.getT()) || !(Math.abs(r.getPixelColor(p.getX(), p.getY()).getBlue() - p.getB()) <= p.getT())) {
                                is = false;
                                break;
                            }

                        }
                        if (is && !was) {
                            System.out.println("yes");
                            was = true;
                        } else if (!is && was) {
                            System.out.println("no");
                            was = false;
                        }
                    }

                }
                abort = false;
                return null;
            }
        };

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (btnStart.getText().equals("Start")) {
                    scan.execute();
                    btnStart.setText("Stop");
                } else {
                    abort = true;
                    btnStart.setText("Start");
                }
            }
        });
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
                    if (points.size() > 0)
                        update(table);
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

    public static void readImport(BufferedReader bufferedText) {
        try {
            String version = bufferedText.readLine();

            if (version.contentEquals("v1.0")) {
                readFile1p0(bufferedText);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void readFile1p0(BufferedReader mapperPoints) {
        try {
            String line = "";
            String name;
            int r;
            int g;
            int b;
            int t;
            int x;
            int y;
            for (int i = 0; line != null; i++) {
                if (i > 0) {
                    mapperPoints.readLine();
                    name = mapperPoints.readLine();
                    r = Integer.parseInt(mapperPoints.readLine());
                    g = Integer.parseInt(mapperPoints.readLine());
                    b = Integer.parseInt(mapperPoints.readLine());
                    t = Integer.parseInt(mapperPoints.readLine());
                    x = Integer.parseInt(mapperPoints.readLine());
                    y = Integer.parseInt(mapperPoints.readLine());
                    points.add(new Pixel(name, 0, r, g, b, t, x, y));
                } else {
                    screenWidth = Integer.parseInt(mapperPoints.readLine());
                    screenHeight = Integer.parseInt(mapperPoints.readLine());
                }
                line = mapperPoints.readLine();
                lineCount++;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
