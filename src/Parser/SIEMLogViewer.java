/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Parser;

import Parser.SIEMLogParser.LogEntry;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.*;

/**
 *
 * @author DELL
 */
public class SIEMLogViewer {

    /**
     * Creates and shows the GUI for displaying and filtering logs.
     *
     * @param logs List of logs to display
     */
    public static void Old(List<LogEntry> logs) {
        JFrame frame = new JFrame("SIEM Log Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Table to display logs
        String[] columnNames = {"IP Address", "Log Type", "Time Created", "Event ID", "Level", "Message"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Populate the table with log data
        for (LogEntry log : logs) {
            tableModel.addRow(new Object[]{log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message});
        }

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filter by Level:"));
        JTextField levelField = new JTextField(10);
        filterPanel.add(levelField);

        filterPanel.add(new JLabel("Event ID:"));
        JTextField eventIdField = new JTextField(10);
        filterPanel.add(eventIdField);

        JButton filterButton = new JButton("Apply Filter");
        filterPanel.add(filterButton);

        // Add action listener to the filter button
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String levelFilter = levelField.getText().trim();
                String eventIdFilter = eventIdField.getText().trim();

                // Clear the table
                tableModel.setRowCount(0);

                // Add filtered logs back to the table
                for (LogEntry log : logs) {
                    boolean matches = true;
                    if (!levelFilter.isEmpty() && !log.level.equalsIgnoreCase(levelFilter)) {
                        matches = false;
                    }
                    if (!eventIdFilter.isEmpty() && !log.eventId.equals(eventIdFilter)) {
                        matches = false;
                    }
                    if (matches) {
                        tableModel.addRow(new Object[]{log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message});
                    }
                }
            }
        });

        // Add components to the frame
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(filterPanel, BorderLayout.NORTH);

        // Show the frame
        frame.setVisible(true);
    }

    public static void createAndShowGUI(List<LogEntry> logs) {
        JFrame frame = new JFrame("SIEM Log Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Table to display logs
        String[] columnNames = {"IP Address", "Log Type", "Time Created", "Event ID", "Level", "Message"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // Populate the table with log data
        for (LogEntry log : logs) {
            tableModel.addRow(new Object[]{log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message});
        }

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Filter by Column:"));

        // Dropdown for selecting the column
        JComboBox<String> columnDropdown = new JComboBox<>(columnNames);
        filterPanel.add(columnDropdown);

        // Input box for entering filter value
        filterPanel.add(new JLabel("Filter Value:"));
        JTextField filterValueField = new JTextField(20);
        filterPanel.add(filterValueField);

        JButton filterButton = new JButton("Apply Filter");
        filterPanel.add(filterButton);

        // Add action listener to the filter button
        filterButton.addActionListener(e -> {
            String selectedColumn = (String) columnDropdown.getSelectedItem();
            String filterValue = filterValueField.getText().trim();

            // Determine the column index based on the selected column name
            int columnIndex = Arrays.asList(columnNames).indexOf(selectedColumn);

            // Clear the table
            tableModel.setRowCount(0);

            // Add filtered logs back to the table
            for (LogEntry log : logs) {
                Object[] rowData = {
                    log.ipAddress, log.logType, log.timeCreated, log.eventId, log.level, log.message
                };
                // Check if the filter matches
                if (filterValue.isEmpty() || rowData[columnIndex].toString().contains(filterValue)) {
                    tableModel.addRow(rowData);
                }
            }
        });

        // Add components to the frame
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(filterPanel, BorderLayout.NORTH);

        // Show the frame
        frame.setVisible(true);
    }

}
