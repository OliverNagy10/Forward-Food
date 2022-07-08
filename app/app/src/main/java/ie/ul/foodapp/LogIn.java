package ie.ul.foodapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LogIn extends AppCompatActivity {
    private EditText user_name, pass_word;
    FirebaseAuth mAuth;
    String curr;
    String Type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        user_name = findViewById(R.id.email);
        pass_word = findViewById(R.id.password);
        Button btn_login = findViewById(R.id.btn_login);
        Button btn_sign = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(v -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();
            if (email.isEmpty()) {
                user_name.setError("Email is empty");
                user_name.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                user_name.setError("Enter the valid email");
                user_name.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                pass_word.setError("Password is empty");
                pass_word.requestFocus();
                return;
            }
            if (password.length() < 6) {
                pass_word.setError("Length of password is more than 6");
                pass_word.requestFocus();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    getType();
                    if( returnType() != null && returnType().equals("Business")) {
                        startActivity(new Intent(LogIn.this, BusinessPageUI.class));
                    }
                    else if(returnType() != null && returnType().equals("User")) {
                        startActivity(new Intent(LogIn.this, Home.class));
                    }

                } else {
                    Toast.makeText(LogIn.this,
                            "Please Check Your login Credentials",
                            Toast.LENGTH_SHORT).show();
                }

            });
        });
        btn_sign.setOnClickListener(v -> startActivity(new Intent(LogIn.this, SignUp.class)));
    }


    public void getType() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        curr = currentUser.getEmail();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference docRefUser = dataBase.collection("User").document(Objects.requireNonNull(curr));
        DocumentReference docRefBusiness = dataBase.collection("Business").document(Objects.requireNonNull(curr));
        docRefBusiness.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        setType((String) document.get("type"));
                    } else {
                        Log.d(TAG, "docrefbis:Cant get Business Data ");
                    }
                } else {
                    Log.d(TAG, "Getting Business Data Failed:", task.getException());
                }
            }
        });
        docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        setType(document.getString("type"));
                    } else {
                        Log.d(TAG, "docrefuser:Cant get Business Data ");
                    }
                } else {
                    Log.d(TAG, "Getting Business Data Failed:", task.getException());
                }
            }
        });
    }


    public void setType( String t) {
        this.Type = t;
    }
    public String returnType(){
        return Type;
    }
}