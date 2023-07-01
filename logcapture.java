public class MainActivity extends AppCompatActivity {
    
    private static final boolean DEBUG_MODE = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Perform some core functionality
        
        if (DEBUG_MODE) {
            // Enable additional debugging functionality
            
            // Log sensitive data (leftover from testing)
            logSensitiveData();
        }
        
        // Rest of the code...
    }
    
    private void logSensitiveData() {
        // Log sensitive data (for testing purposes only)
        String sensitiveData = "Sensitive data";
        Log.d("SensitiveData", sensitiveData);
    }
}

// In this example, the DEBUG_MODE variable is set to false to disable additional debugging functionality
//  in a production environment. However, the logSensitiveData() method is still present, and if the developer
//   forgets to remove it or the log captures during testing, it can lead to sensitive information being logged 
//   in the production app

// Mitigation 

public class MainActivity extends AppCompatActivity {
    
    private static final boolean DEBUG_MODE = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Perform some core functionality
        
        if (DEBUG_MODE) {
            // Enable additional debugging functionality (only for testing purposes)
            
            // Log sensitive data (for testing purposes only)
            logSensitiveData();
        }
        
        // Rest of the code...
    }
    
    private void logSensitiveData() {
        // Log sensitive data (for testing purposes only)
        if (BuildConfig.DEBUG) {
            String sensitiveData = "Sensitive data";
            Log.d("SensitiveData", sensitiveData);
        }
    }
}

// In the updated code:

//     The DEBUG_MODE is set to false to ensure that additional debugging functionality is disabled in a production
//      environment.

//     The logSensitiveData() method is enclosed within the if (DEBUG_MODE) condition. This ensures that the code
//      inside the block is only executed when DEBUG_MODE is true.

//     To further safeguard against forgetting to remove the log captures, an additional check is added using
//      BuildConfig.DEBUG. This ensures that sensitive data logging only occurs in debug builds and not in production
//       builds