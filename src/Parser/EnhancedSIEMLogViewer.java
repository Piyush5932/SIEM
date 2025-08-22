/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Parser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List; // Explicitly import java.util.List
import java.util.ArrayList; // Explicitly import java.util.ArrayList
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnhancedSIEMLogViewer {

    

    // Class to represent a parsed log entry
    static class LogEntry {
        String ipAddress;
        String logType;
        String timeCreated;
        String eventId;
        String level;
        String message;
        long timestamp;

        @Override
        public String toString() {
            return String.format(
                    "IP: %s | Type: %s | Time: %s | Event ID: %s | Level: %s | Message: %s | Timestamp: %d",
                    ipAddress, logType, timeCreated, eventId, level, message, timestamp
            );
        }
    }

    // Unified regex pattern for both formats
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "Log: IP: (\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(System Log|Application Log):\\s+" +
                    "(\\d{2}-\\d{2}-\\d{4}\\s+\\d{2}:\\d{2}:\\d{2}\\s+[AP]M|\\d{2}-\\d{2}-\\d{4}\\s+\\d{2}:\\d{2}:\\d{2})\\s+" +
                    "(\\d+)\\s+(\\S+)\\s+(.+?)\\s+\\|\\s+Timestamp:\\s+(\\d+)"
    );

    public static void main(String logContent) {

        // Parse the logs
        List<LogEntry> logs = parseLogsFromString(logContent);

        // Create and display the GUI
        SwingUtilities.invokeLater(() -> createAndShowGUI(logs));
    }

    /**
     * Parses logs from a given string input.
     *
     * @param logContent The log content as a single string
     * @return List of parsed LogEntry objects
     */
    public static List<LogEntry> parseLogsFromString(String logContent) {
        List<LogEntry> logs = new ArrayList<>();
        String[] lines = logContent.split("\\n");

        for (String line : lines) {
            processLogEntry(line.trim(), logs);
        }

        return logs;
    }

    /**
     * Processes a single log entry and adds it to the log list if valid.
     *
     * @param logLine The raw log line
     * @param logs    The list to add the parsed log entry
     */
    private static void processLogEntry(String logLine, List<LogEntry> logs) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);

        if (matcher.matches()) {
            logs.add(createLogEntry(matcher));
        } else {
            System.err.println("Malformed or unrecognized log: " + logLine);
        }
    }

    /**
     * Creates a LogEntry from a regex matcher.
     *
     * @param matcher The matcher containing parsed log data
     * @return A new LogEntry object
     */
    private static LogEntry createLogEntry(Matcher matcher) {
        LogEntry entry = new LogEntry();
        entry.ipAddress = matcher.group(1);
        entry.logType = matcher.group(2);
        entry.timeCreated = matcher.group(3);
        entry.eventId = matcher.group(4);
        entry.level = matcher.group(5);
        entry.message = matcher.group(6);
        entry.timestamp = Long.parseLong(matcher.group(7));
        return entry;
    }

    /**
     * Creates and displays the GUI for viewing logs.
     *
     * @param logs The list of parsed logs
     */
    public static void createAndShowGUI(List<LogEntry> logs) {
        JFrame frame = new JFrame("Enhanced SIEM Log Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        // Table to display logs
        String[] columnNames = {"IP Address", "Log Type", "Time Created", "Event ID", "Level", "Message", "Timestamp"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Populate the table with log data
        for (LogEntry log : logs) {
            tableModel.addRow(new Object[]{
                    log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message, log.timestamp
            });
        }

        // Add a dropdown and filter field
        JPanel filterPanel = new JPanel(new FlowLayout());
        JComboBox<String> columnFilter = new JComboBox<>(columnNames);
        JTextField filterField = new JTextField(20);
        JButton applyFilter = new JButton("Filter");

        filterPanel.add(new JLabel("Filter by:"));
        filterPanel.add(columnFilter);
        filterPanel.add(filterField);
        filterPanel.add(applyFilter);

        
        
        
        // "Apply Rule" Button
        JButton applyRuleButton1 = new JButton("Rule1");
        applyFilter.addActionListener(e -> {
            String column = (String) columnFilter.getSelectedItem();
            String filter = filterField.getText().trim();
            int columnIndex = columnFilter.getSelectedIndex();
            tableModel.setRowCount(0); // Clear table

            // Apply filter
            logs.stream()
                    .filter(log -> log.toString().contains(filter))
                    .forEach(log -> tableModel.addRow(new Object[]{
                            log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message, log.timestamp
                    }));
        });



        // Apply the threat detection rules
        applyRuleButton1.addActionListener(e -> {
            // Apply custom security rules
            Map<String, Integer> loginAttempts = new HashMap<>();  // Track failed login attempts
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String ipAddress = (String) tableModel.getValueAt(i, 0);
                String level = (String) tableModel.getValueAt(i, 4);
                String message = (String) tableModel.getValueAt(i, 5);
                String eventId = (String) tableModel.getValueAt(i, 3);

                // Rule 1: Failed login attempts (EventID=7001 or level=Error or Warning)
                if (level.equals("Error") || eventId.equals("7001")) {
                    System.out.println("Eror detected");
                    loginAttempts.put(ipAddress, loginAttempts.getOrDefault(ipAddress, 0) + 1);
                    table.setRowSelectionInterval(i, i);
                    table.setSelectionBackground(Color.YELLOW);  // Highlight device connections
                    table.setSelectionForeground(Color.BLACK);
                }

            }
        });

        
        
        // "Apply Rule" Button
        JButton applyRuleButton2 = new JButton("Rule2");
        applyFilter.addActionListener(e -> {
            String column = (String) columnFilter.getSelectedItem();
            String filter = filterField.getText().trim();
            int columnIndex = columnFilter.getSelectedIndex();
            tableModel.setRowCount(0); // Clear table

            // Apply filter
            logs.stream()
                    .filter(log -> log.toString().contains(filter))
                    .forEach(log -> tableModel.addRow(new Object[]{
                            log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message, log.timestamp
                    }));
        });



        // Apply the threat detection rules
        applyRuleButton2.addActionListener(e -> {
            // Apply custom security rules
            Map<String, Integer> loginAttempts = new HashMap<>();  // Track failed login attempts
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String ipAddress = (String) tableModel.getValueAt(i, 0);
                String level = (String) tableModel.getValueAt(i, 4);
                String message = (String) tableModel.getValueAt(i, 5);
                String eventId = (String) tableModel.getValueAt(i, 3);

                // Rule 1: Failed login attempts (EventID=7001 or level=Error or Warning)
                if (level.equals("Error") || eventId.equals("7001")) {
                    System.out.println("Eror detected");
                    loginAttempts.put(ipAddress, loginAttempts.getOrDefault(ipAddress, 0) + 1);
                    table.setRowSelectionInterval(i, i);
                    table.setSelectionBackground(Color.YELLOW);  // Highlight device connections
                    table.setSelectionForeground(Color.BLACK);
                }

                // Rule 2: Device connection (simple check for 'device' keyword in message)
                if (message != null && message.toLowerCase().contains("device connected")) {
                    table.setRowSelectionInterval(i, i);
                    table.setSelectionBackground(Color.YELLOW);  // Highlight device connections
                    table.setSelectionForeground(Color.BLACK);
                }

                // Rule 3: Check for specific patterns like "UMDF" and "Failed"
                if (message != null && (message.contains("UMDF") || message.contains("Failed"))) {
                    System.out.println("UMDF detected");
                    table.setRowSelectionInterval(i, i);
                    table.setSelectionBackground(Color.RED);  // Highlight with red for failed or UMDF related events
                    table.setSelectionForeground(Color.WHITE);
                }
            }
        });

        JTextArea jta = new JTextArea();
        JScrollPane jsp = new JScrollPane(jta);
        
        // Add components to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyRuleButton1);
        buttonPanel.add(applyRuleButton2);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(filterPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        

        // Show the frame
        frame.setVisible(true);
    }
}
