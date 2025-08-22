package Aggregator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.DatabaseError;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PushAggregator {

    // Method to initialize Firebase
    public static void initializeFirebase() {
        try {
            // Load the service account key JSON file
            FileInputStream serviceAccount = new FileInputStream("siem-2abd1-firebase-adminsdk-bddza-1071518e12.json");

            // Initialize Firebase options with Google credentials
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .setDatabaseUrl("https://siem-2abd1-default-rtdb.firebaseio.com/") // Use your database URL
                    .build();

            // Initialize FirebaseApp
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing Firebase: " + e.getMessage());
        }
    }

    // Method to send logs to Firebase Realtime Database
    public static void sendLogToFirebase(String logMessage) {
        try {
            // Get the Firebase database reference
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("logs");

            // Prepare log data
            Map<String, Object> log = new HashMap<>();
            log.put("message", logMessage);
            log.put("timestamp", System.currentTimeMillis());

            // Push log to Firebase Realtime Database
            //ref.push().setValue(log);
            ref.push().setValue(log, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Error sending log to Firebase: " + databaseError.getMessage());
                    } else {
                        System.out.println("Log successfully sent to Firebase");
                    }
                }
            });

            System.out.println("Log sent to Firebase: " + log);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending log to Firebase: " + e.getMessage());
        }
    }

}
