/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package siem.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WindowsApplicationLogGenerator {

    public static void main(String[] args) {
        System.out.println("Generating test logs in Windows Event Viewer...");

        // Generate logs in a loop for testing purposes
        for (int i = 0; i < 3; i++) {
            try {
                // Generate different types of logs
                createLog("Information", 1001, "Test Information Log Entry " + i);
                createLog("Warning", 1002, "Test Warning Log Entry " + i);
                createLog("Error", 1003, "Test Error Log Entry " + i);

                System.out.println("Logs generated successfully for iteration: " + (i + 1));

                // Wait for 5 seconds before generating the next set of logs
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Sleep interrupted: " + e.getMessage());
            }
        }
    }

    /**
     * Create a log entry in the Windows Event Viewer using PowerShell commands.
     *
     * @param logType Type of log (e.g., Information, Warning, Error)
     * @param eventId Event ID for the log entry
     * @param message Message for the log entry
     */
    private static void createLog(String logType, int eventId, String message) {
        String command = String.format(
            "powershell -Command \"Write-EventLog -LogName 'Application' -Source 'Application' -EntryType %s -EventId %d -Message '%s'\"",
            logType, eventId, message
        );

        executeCommand(command);
    }

    /**
     * Executes a given command in the system shell.
     *
     * @param command Command to be executed
     */
    private static void executeCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing command: " + e.getMessage());
        }
    }
}

