package com.thanhdc.GUI.View;

import Main.EndTask;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;
import com.thanhdc.GUI.Main.YourTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

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
        tablePane.setBackground(Color.lightGray);

        JPanel panelEstablish = createPanelEstablish();
        panelEstablish.setBackground(Color.lightGray);

        JPanel panelResult = createPanelResult();
        panelResult.setBackground(Color.lightGray);

        JPanel panelMonitor = createPanelMonitor();
        panelMonitor.setBackground(Color.lightGray);

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
                Calendar calStart = null;
                Calendar calEnd = null;
                LocalDate DateStart = asLocalDate(jdStartDate);
                // disengage element of TimeStart
                timePickerStart = toSqlTime(jdTimeStart);
                int timePickerStartHour = timePickerStart.getHours();
                int timePickerStartMin = timePickerStart.getMinutes();
                int timePickerStartSecond = timePickerStart.getSeconds();
                // format DateStart to Calendar type
                Date DateNoZoneStart = convertToDateViaSqlDate(DateStart);
                calStart = Calendar.getInstance();
                calStart.setTime(DateNoZoneStart);

                calStart.set(Calendar.HOUR_OF_DAY, timePickerStartHour);// import
                // time
                calStart.set(Calendar.MINUTE, timePickerStartMin);
                calStart.set(Calendar.SECOND, timePickerStartSecond);
                // converse text of intervalPeriod to interger
                int intPeriod = Integer.parseInt(textintervalPeriod.getText());

                // timer.schedule(Task, Time to start, Interval Period);
                Timer timerToStart = new Timer();
                timerToStart.schedule(new YourTask(), calStart.getTime(),
                        TimeUnit.MILLISECONDS
                                .convert(intPeriod, TimeUnit.SECONDS)); // period:
                // 1
                // hour

                LocalDate DateEnd = asLocalDate(jdEndDate);
                // disengage element of TimeEnd
                timePickerEnd = toSqlTime(jdTimeEnd);
                int timePickerEndHour = timePickerStart.getHours();
                int timePickerEndtMin = timePickerStart.getMinutes();
                int timePickerEndSecond = timePickerStart.getSeconds();
                // format DateEnd to Calendar type
//				Date DateNoZoneEnd = convertToDateViaSqlDate(DateEnd);
//				calEnd = Calendar.getInstance();
//				calEnd.setTime(DateNoZoneStart);
//
//				calEnd.set(Calendar.HOUR_OF_DAY, timePickerEndHour);// import
//																	// time
//				calEnd.set(Calendar.MINUTE, timePickerEndtMin);
//				calEnd.set(Calendar.SECOND, timePickerEndSecond);
//
//				Timer timerToEnd = new Timer();
//				timerToEnd.schedule(new EndTask(), calEnd.getTime(),
//						TimeUnit.MILLISECONDS.convert(0, TimeUnit.HOURS));
//
//				String mapSaveFolder = saveFolder.getText();
//
//				System.out.println(timePickerStartHour);
//				System.out.println(timePickerStartMin);
//				System.out.println(timePickerStartSecond);
//				System.out.println(intPeriod);
//				System.out.println(mapSaveFolder);

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
        Calendar calStop = null;
        LocalDate dateStop = LocalDate.now();
        Date DateNoZoneStop = convertToDateViaSqlDate(dateStop);
        calStop = Calendar.getInstance();
        calStop.setTime(DateNoZoneStop);
        TimePicker timePickerStop = new TimePicker();
        java.sql.Time timeStop = toSqlTime(timePickerStop);

        int timePickerEndHour = timePickerStart.getHours();
        int timePickerEndtMin = timePickerStart.getMinutes();
        int timePickerEndSecond = timePickerStart.getSeconds();
        // format DateEnd to Calendar type

        calStop.set(Calendar.HOUR_OF_DAY, timePickerEndHour);// import
        // time
        calStop.set(Calendar.MINUTE, timePickerEndtMin);
        calStop.set(Calendar.SECOND, timePickerEndSecond);

        Timer timerToEnd = new Timer();
        timerToEnd.schedule(new EndTask(), calStop.getTime(),
                TimeUnit.MILLISECONDS.convert(0, TimeUnit.HOURS));

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
        Preferences pref = Preferences.userRoot();

        // Retrieve the selected path or use
        // an empty string if no path has
        // previously been selected
        String path = pref.get("DEFAULT_PATH", "");

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // Set the path that was saved in preferences
        chooser.setCurrentDirectory(new File(path));

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            chooser.setCurrentDirectory(f);

            // Save the selected path
            pref.put("DEFAULT_PATH", f.getAbsolutePath());
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

    // public static java.sql.Time toSqlTimeFromLCTime(LocalTime LocalTime) {
    // LocalTime LcTimeStart = timePicker.getTime();
    // return java.sql.Time.valueOf(LcTimeStart);
    // }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
}