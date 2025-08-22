/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Parser;

import Aggregator.*;
import Parser.SIEMLogParser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PopAggregate {

    private static final Set<String> fetchedLogs = new HashSet<>();

    public static void main(String[] args) {
        // Initialize Firebase
        PushAggregator.initializeFirebase();

        // Continuously fetch logs every 5 seconds
        try {
            while (true) {               
                readLogsFromFirebase();
                Thread.sleep(250000);  // Wait for 250 seconds before fetching again
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to read logs from Firebase
    public static void readLogsFromFirebase() {
        try {
            // Get the Firebase database reference
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("logs");

            // Attach a listener to read data from Firebase
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Iterate over the logs
                    String longContent = "";
                    for (DataSnapshot logEntry : dataSnapshot.getChildren()) {
                        String logMessage = logEntry.child("message").getValue(String.class);
                        Long timestamp = logEntry.child("timestamp").getValue(Long.class);

                        // Display logs if they haven't been fetched already
                        String uniqueLogKey = logEntry.getKey(); // Unique key for each log
                        if (uniqueLogKey != null && !fetchedLogs.contains(uniqueLogKey)) {
                            fetchedLogs.add(uniqueLogKey);    
                            longContent += "Log: " + logMessage + " | Timestamp: " + timestamp + "\n";
                        }
                    }
                    System.out.println(longContent);
                    //SIEMLogParser.getLog(longContent);
                    EnhancedSIEMLogViewer.main(longContent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Error reading logs from Firebase: " + databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in readLogsFromFirebase: " + e.getMessage());
        }
    }
}
