package com.thanhdc.GUI.View;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class BOTView extends JFrame {

    JTextArea textArea;
    JButton save;
    JButton Start;
    JButton Stop;
    Date datePickerStart = null;
    Date datePickerEnd = null;
    java.sql.Time timePickerStart = null;
    java.sql.Time timePickerEnd = null;
    int intervalPeriod = 0;

    public BOTView() {
        createGUI();
        setDisplay();
    }

    private void setDisplay() {
        setTitle("BOT - AML - BOT VIEW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
        setDefaultLookAndFeelDecorated(true);
    }

    private void createGUI() {
        getContentPane().add(createTabbedPane());
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tablePane = new JTabbedPane();
        JPanel panelEstablish = createPanelEstablish();
        JPanel panelResult = createPanelResult();
        JPanel panelMonitor = createPanelMonitor();

        tablePane
                .addTab("Monitor", null, panelMonitor, "click to show panel 1");
        tablePane.addTab("Báo cáo", null, panelResult, "click to show panel 1");
        tablePane.addTab("Thiết lập", null, panelEstablish,
                "click to show panel 1");
        tablePane.setSelectedIndex(tablePane.getTabCount() - 3);

        return tablePane;
    }

    private JPanel createPanelResult() {
        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        JScrollPane scrollPane = new JScrollPane(createTextArea(10, 40));
        JLabel fromDate = createLabel("Từ ngày: ");
        JLabel toDate = createLabel("Tới ngày: ");

        DatePicker jdStartDate = generateDatePicker();
        LocalDate DateStart = asLocalDate(jdStartDate);

        DatePicker jdEndDate = generateDatePicker();
        LocalDate DateEnd = asLocalDate(jdEndDate);

        // JTextArea textFromDate = createTextArea(1, 1);
        // JTextArea textToDate = createTextArea(1, 1);
        JButton btExport = createButton("Xuất ra file *.pdf");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3; // 2 columns wide
        c.gridx = 0;
        c.gridy = 0;
        panel.add(scrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(fromDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        panel.add(jdStartDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(toDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(jdEndDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(btExport, c);
        return panel;
    }

    public JPanel createPanelEstablish() {
        File file = new File("C:\\");
        Desktop desktop = Desktop.getDesktop();

        JPanel pl = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        pl.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        JLabel startDate = createLabel("Ngày bắt đầu: ");
        JLabel endDate = createLabel("Ngày kết thúc: ");
        JLabel startTime = createLabel("Giờ bắt đầu: ");
        JLabel endTime = createLabel("Giờ kết thúc: ");

        JLabel intervalPeriod = createLabel("Interval (Period): ");
        JLabel saveFolder = createLabel("Folder lưu trữ: ");

        DatePicker jdStartDate = generateDatePicker();


        DatePicker jdEndDate = generateDatePicker();


        TimePicker jdTimeStart = generateTimePicker();


        TimePicker jdTimeEnd = generateTimePicker();


        JTextArea textintervalPeriod = createTextArea(1, 1);

        JButton btsaveFolder = createButton("SAVE FOLDER");
        btsaveFolder.addActionListener(e -> saveToFile());

        JButton btStart = createButton("START");
        btStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDate DateStart = asLocalDate(jdStartDate);
                LocalDate DateEnd = asLocalDate(jdEndDate);
                timePickerStart = toSqlTime(jdTimeStart);
                timePickerEnd = toSqlTime(jdTimeEnd);
                int intervalPeriod = Integer.parseInt(textintervalPeriod.getText());
                System.out.println(DateStart);
                System.out.println(DateEnd);
                System.out.println(timePickerStart);
                System.out.println(timePickerEnd);
                System.out.println(intervalPeriod);

            }
        });

        JButton btStop = createButton("STOP");
        btStop.addActionListener(e -> StopBt());

        // Text start Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 50;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(startDate, c);

        // Picker start Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdStartDate, c);

        // Text start Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 0;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        pl.add(startTime, c);

        // Picker start Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 0;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdTimeStart, c);

        // Text End Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 50;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(endDate, c);

        // Picker End Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdEndDate, c);

        // Picker End Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdTimeEnd, c);

        // Text End Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        pl.add(endTime, c);

        // Text intervalPeriod
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(intervalPeriod, c);

        // Label intervalPeriod
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(textintervalPeriod, c);

        // Textarea intervalPeriod
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(saveFolder, c);

        // Button savefolder
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(btsaveFolder, c);

        // Button start
        c.fill = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(btStart, c);

        // Button stop
        c.fill = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 3;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(btStop, c);

        return pl;
    }

    protected void StopBt() {
        // TODO Auto-generated method stub

    }

    protected void StartBt() {

    }

    private JPanel createPanelMonitor() {
        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        JScrollPane scrollPane = new JScrollPane(createTextArea(10, 40));

        c.fill = GridBagConstraints.CENTER;
        c.ipady = 40;
        c.ipadx = 40; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3; // 2 columns wide
        c.gridx = 0;
        c.gridy = 0;
        panel.add(scrollPane, c);

        return panel;
    }

    private JTextArea createTextArea(int row, int col) {
        JTextArea ta = new JTextArea(row, col);
        return ta;
    }

    private JLabel createLabel(String s) {
        JLabel jl = new JLabel(s);
        return jl;
    }

    private JTextField createTextField() {
        JTextField jtf = new JTextField();
        return jtf;
    }

    private JButton createButton(String s) {
        JButton jb = new JButton(s);
        return jb;
    }

    private DatePicker generateDatePicker() {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd.MM.yyyy");
        final DatePickerSettings settings = new DatePickerSettings(Locale.US);
        settings.setFormatForDatesCommonEra(formatter);
        settings.setFormatForDatesBeforeCommonEra(DateTimeFormatter
                .ofPattern("d.M.yy.G"));
        ArrayList parsingList = settings.getFormatsForParsing();
        parsingList.add(DateTimeFormatter.ofPattern("d M yyyy"));
        parsingList.add(DateTimeFormatter.ofPattern("d-M-yyyy"));
        parsingList.add(DateTimeFormatter.ofPattern("ddMMyyyy"));

        DatePicker datePicker = new DatePicker(settings.copySettings());
        return datePicker;
    }

    private TimePicker generateTimePicker() {
        // Set up the form which holds the date picker components.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);

        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setColor(TimeArea.TimePickerTextValidTime, Color.black);
        timeSettings.initialTime = LocalTime.now();
        TimePicker timePicker = new TimePicker(timeSettings);

        return timePicker;
    }

    protected void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int retval = fileChooser.showSaveDialog(save);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file == null) {
                return;
            }
            if (!file.getName().toLowerCase().endsWith(".txt")) {
                file = new File(file.getParentFile(), file.getName() + ".txt");
            }
            try {
                textArea.write(new OutputStreamWriter(
                        new FileOutputStream(file), "utf-8"));
                Desktop.getDesktop().open(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Date asDate(DatePicker datePicker) {
        LocalDate selectedDate = datePicker.getDate();
        Date date = new Date(selectedDate.atStartOfDay(ZoneId.of("UTC+00:00"))
                .toEpochSecond() * 1000);
        return date;
    }

    public static LocalDate asLocalDate(DatePicker datePicker) {
        LocalDate selectedDate = datePicker.getDate();
        return selectedDate;
    }

    public static java.sql.Time toSqlTime(TimePicker timePicker) {
        LocalTime LcTimeStart = timePicker.getTime();
        return java.sql.Time.valueOf(LcTimeStart);
    }
}
