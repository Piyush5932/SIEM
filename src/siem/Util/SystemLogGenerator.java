/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package siem.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemLogGenerator {

    private static final String LOG_NAME = "System";
    private static final String SOURCE_NAME = "CustomSystemSource";

    public static void main(String[] args) {
        try {
            // Step 1: Register the source if it does not exist
            //registerEventLogSource();

            // Step 2: Generate logs in a loop
            System.out.println("Generating test logs in the Windows System log...");
            for (int i = 0; i < 3; i++) {
                try {
                    // Generate different types of logs
                    createLog("Information", 3001, "Test System Information Log Entry " + i);
                    createLog("Warning", 3002, "Test System Warning Log Entry " + i);
                    createLog("Error", 3003, "Test System Error Log Entry " + i);

                    System.out.println("Logs generated successfully for iteration: " + (i + 1));

                    // Wait for 5 seconds before generating the next set of logs
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.err.println("Sleep interrupted: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during log source registration or generation: " + e.getMessage());
        }
    }

private static void registerEventLogSource() throws IOException {
    String checkCommand = String.format(
        "powershell -Command \"Get-EventLog -LogName '%s' -Source '%s' -ErrorAction SilentlyContinue\"",
        LOG_NAME, SOURCE_NAME
    );

    String registerCommand = String.format(
        "powershell -Command \"New-EventLog -LogName '%s' -Source '%s'\"",
        LOG_NAME, SOURCE_NAME
    );

    // Check if the source is already registered
    if (!executeCommandAndCheckOutput(checkCommand, SOURCE_NAME)) {
        System.out.println("Registering new source for System log: " + SOURCE_NAME);
        try {
            executeCommand(registerCommand);
            System.out.println("Source registered successfully.");
        } catch (Exception e) {
            System.err.println("Error: You need to run this program as an administrator to register the source.");
            System.exit(1); // Exit the program if source registration fails
        }
    } else {
        System.out.println("Source '" + SOURCE_NAME + "' is already registered.");
    }
}


    /**
     * Creates a log entry in the Windows System log.
     *
     * @param logType Type of log (e.g., Information, Warning, Error)
     * @param eventId Event ID for the log entry
     * @param message Message for the log entry
     */
    private static void createLog(String logType, int eventId, String message) {
        String command = String.format(
            "powershell -Command \"Write-EventLog -LogName '%s' -Source '%s' -EntryType %s -EventId %d -Message '%s'\"",
            LOG_NAME, SOURCE_NAME, logType, eventId, message
        );

        executeCommand(command);
    }

    /**
     * Executes a given command in the system shell and checks if specific output is present.
     *
     * @param command Command to execute
     * @param expectedOutput Substring to check in the output
     * @return true if the expected output is found, false otherwise
     * @throws IOException if an I/O error occurs
     */
    private static boolean executeCommandAndCheckOutput(String command, String expectedOutput) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.contains(expectedOutput)) {
                found = true;
            }
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Command interrupted: " + e.getMessage());
        }

        return found;
    }

    /**
     * Executes a given command in the system shell.
     *
     * @param command Command to execute
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

