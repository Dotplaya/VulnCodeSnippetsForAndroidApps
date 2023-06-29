import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating login with a weak password
        login("password123");
    }

    private void login(String password) {
        if (password.equals("password123")) {
            // Authentication successful
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            // Authentication failed
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }
}

// In this vulnerable code snippet, the app performs authentication by comparing the user-provided 
// password with a hardcoded weak password ("password123"). This weak password policy allows for easy 
// guessing and increases the likelihood of successful brute force attacks.

// Mitigation 

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simulating login with a stronger password
        login("StrongPassword123!");
    }

    private void login(String password) {
        if (isStrongPassword(password)) {
            // Authentication successful
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        } else {
            // Authentication failed
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isStrongPassword(String password) {
        // Check minimum length
        if (password.length() < 8) {
            return false;
        }

        // Check complexity requirements (e.g., uppercase, lowercase, number, special character)
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }

        // Avoid common passwords or patterns
        if (password.matches(".*(password|123456|qwerty|123456789|admin).*")) {
            return false;
        }

        // Additional checks can be added based on your specific requirements

        return true;
    }
}

// In this updated code snippet, the isStrongPassword method checks if the provided
//  password meets the defined strong password policy. It ensures that the password has
//   a minimum length of 8 characters, contains at least one uppercase letter, one lowercase letter
//   , one number, and one special character. It also checks for the avoidance of common passwords or 
//   patterns. You can customize the password policy and add additional checks based on your specific requirements.