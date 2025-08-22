/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package siem.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimulateAttackEvents {

    private static final String LOG_NAME = "System";  // Target log
    private static final String SOURCE_NAME = "AttackSimulator"; // Custom source name for logs

    public static void main(String[] args) {
        try {
            // Step 1: Register Event Log Source (One-time setup)
            registerEventLogSource();

            // Step 2: Simulate Attack Events
            simulateFailedLoginEvents();
            simulateMalwareBehavior();
            simulateDataExfiltration();
            simulatePortScan();

            System.out.println("Attack events successfully simulated.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
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

        // Check if the source already exists
        if (!executeCommandAndCheckOutput(checkCommand, SOURCE_NAME)) {
            System.out.println("Registering new source for System log: " + SOURCE_NAME);
            try {
                executeCommand(registerCommand);
                System.out.println("Source registered successfully.");
            } catch (Exception e) {
                System.err.println("Error: Unable to register source. Run this program as an administrator.");
                System.exit(1); // Exit if source registration fails
            }
        } else {
            System.out.println("Source '" + SOURCE_NAME + "' is already registered.");
        }
    }

    private static void simulateFailedLoginEvents() {
        System.out.println("Simulating failed login events...");
        for (int i = 1; i <= 5; i++) {
            String message = "Simulated failed login attempt " + i;
            createLog("Warning", 1001, message);
        }
    }

    private static void simulateMalwareBehavior() {
        System.out.println("Simulating malware behavior...");
        String message = "Simulated malware created a suspicious registry key.";
        createLog("Error", 2001, message);
    }

    private static void simulateDataExfiltration() {
        System.out.println("Simulating data exfiltration...");
        String message = "Simulated data exfiltration attempt to external server.";
        createLog("Error", 3001, message);
    }

    private static void simulatePortScan() {
        System.out.println("Simulating port scanning...");
        String message = "Simulated port scan detected on multiple ports.";
        createLog("Information", 4001, message);
    }

    private static void createLog(String logType, int eventId, String message) {
        String command = String.format(
            "powershell -Command \"Write-EventLog -LogName '%s' -Source '%s' -EntryType %s -EventId %d -Message '%s'\"",
            LOG_NAME, SOURCE_NAME, logType, eventId, message
        );

        executeCommand(command);
    }

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
