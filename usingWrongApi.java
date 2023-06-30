public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get the current date
        Date currentDate = new Date();
        
        // Extract the year from the date
        int year = currentDate.getYear();
        
        // Display the year to the user
        Toast.makeText(this, "Current Year: " + year, Toast.LENGTH_SHORT).show();
        
        // Rest of the code...
    }
}

// In this example, the MainActivity class attempts to retrieve the current year from a Date object using the getYear()
//  method. However, the getYear() method returns the year relative to 1900, which is deprecated and can lead to
//   incorrect results.

// Mitigation 

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        
        // Extract the year from the date
        int year = calendar.get(Calendar.YEAR);
        
        // Display the year to the user
        Toast.makeText(this, "Current Year: " + year, Toast.LENGTH_SHORT).show();
        
        // Rest of the code...
    }
}

// In the updated code:

//     The Calendar class is used to get the current date and time using Calendar.getInstance().

//     The get(Calendar.YEAR) method is used to retrieve the current year from the Calendar object.

// By using the correct API (Calendar) to retrieve the current year, you can ensure accurate results and improve the
//  code quality of the Android app. Remember to use the appropriate APIs and methods based on the intended 
//  functionality to avoid any potential issues.

