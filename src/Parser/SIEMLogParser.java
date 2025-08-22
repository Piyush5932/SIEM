/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Parser;
import java.util.*;
import java.util.regex.*;

public class SIEMLogParser {

    // Class to represent a parsed log entry
    static class LogEntry {
        String ipAddress;
        String logType;
        String timeCreated;
        String eventId;
        String level;
        String message;

        @Override
        public String toString() {
            return String.format(
                "IP: %s | Type: %s | Time: %s | Event ID: %s | Level: %s | Message: %s",
                ipAddress, logType, timeCreated, eventId, level, message
            );
        }
    }

    // Regular expression pattern for parsing logs
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "Log: IP: (\\S+) (\\S+ Log): (\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2} [AP]M)\\s+(\\d+)\\s+(\\S+)\\s+(.+) \\| Timestamp: \\d+"
    );

    public static void getLog(String logContent) {
        // Parse the logs and store them in a collection
        List<LogEntry> parsedLogs = parseLogs(logContent);

        // Display the parsed logs
        /*System.out.println("Parsed Logs:");
        for (LogEntry entry : parsedLogs) {
            System.out.println(entry);
        }*/
        if(parsedLogs.size() > 0)
            SIEMLogViewer.createAndShowGUI(parsedLogs);
        // Example: Filter logs for a specific level (e.g., "Warning")
        /*System.out.println("\nFiltered Logs for Level 'Warning':");
        parsedLogs.stream()
                  .filter(log -> "System Log".equals(log.logType))
                  .forEach(System.out::println);*/
    }

    /**
     * Parses logs from the given log content string.
     * 
     * @param logContent The log content as a single string
     * @return List of parsed LogEntry objects
     */
    public static List<LogEntry> parseLogs(String logContent) {
        List<LogEntry> logs = new ArrayList<>();
        String[] lines = logContent.split("\\n"); // Split log content into lines

        for (String line : lines) {
            Matcher matcher = LOG_PATTERN.matcher(line);
            if (matcher.find()) {
                LogEntry entry = new LogEntry();
                entry.ipAddress = matcher.group(1);
                entry.logType = matcher.group(2);
                entry.timeCreated = matcher.group(3);
                entry.eventId = matcher.group(4);
                entry.level = matcher.group(5);
                entry.message = matcher.group(6);

                logs.add(entry);
            }
        }

        return logs;
    }
}
