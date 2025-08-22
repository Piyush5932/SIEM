/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Agents;

import Aggregator.PushAggregator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApplicationLogReader {
    private static final Set<String> fetchedLogs = Collections.synchronizedSet(new HashSet<>());
    private static final int FETCH_INTERVAL_SECONDS = 5;

    public static void main() {
        System.out.println("Starting to monitor Windows Application Event Logs...");

        try {
            PushAggregator.initializeFirebase();
        }catch(Exception er) {
            
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable fetchLogsTask = () -> {
            try {
                System.out.println("\nFetching Application logs...");
                fetchLogs();
            } catch (Exception e) {
                System.err.println("Error during log fetch: " + e.getMessage());
            }
        };

        scheduler.scheduleAtFixedRate(fetchLogsTask, 0, FETCH_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private static void fetchLogs() {
        try {
            // PowerShell command to fetch the latest 20 events
            String command = "powershell -Command \"Get-WinEvent -LogName 'Application' -MaxEvents 20 | " +
                    "Select-Object TimeCreated, Id, LevelDisplayName, Message\"";

            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the log is new
                if (!fetchedLogs.contains(line.trim()) && !line.trim().isEmpty()) {
                    fetchedLogs.add(line.trim());
                    InetAddress IP=InetAddress.getLocalHost();
                    PushAggregator.sendLogToFirebase("IP: "+IP.getHostAddress() + " Application Log: "+ line.trim());
                    System.out.println(line.trim());
                }
            }

            reader.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error reading system logs: " + e.getMessage());
        }
    }
}

