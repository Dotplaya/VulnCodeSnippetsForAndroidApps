import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating login with a vulnerable username and password
        login("admin", "password");
    }

    private void login(String username, String password) {
        // Perform SQL query without proper input validation
        String query = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");

            // Execute SQL query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Check if any results are returned
            if (resultSet.next()) {
                // Authentication successful
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                // Authentication failed
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }

            // Close database connection and resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
            Toast.makeText(this, "An error occurred during login", Toast.LENGTH_SHORT).show();
        }
    }
}


// In this vulnerable code snippet, the login method constructs a SQL query by concatenating the user-supplied username
//  and password directly into the query string without proper input validation or parameterization. This leaves the 
//  code susceptible to SQL injection attacks

// Mitigation 

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating login with a stronger username and password
        login("admin", "password");
    }

    private void login(String username, String password) {
        // Perform SQL query with parameterized query
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");

            // Prepare the parameterized query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if any results are returned
            if (resultSet.next()) {
                // Authentication successful
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                // Authentication failed
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }

            // Close database connection and resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
            Toast.makeText(this, "An error occurred during login", Toast.LENGTH_SHORT).show();
        }
    }
}

// In this updated code snippet, the login method uses parameterized queries with placeholders (?) for the username and 
// password values. The values are then set using the setString method, which ensures proper handling of user input and 
// prevents SQL injection attacks.

// By using parameterized queries or prepared statements, you eliminate the risk of SQL injection by separating the SQL
//  logic from the user input, making the code more secure.

