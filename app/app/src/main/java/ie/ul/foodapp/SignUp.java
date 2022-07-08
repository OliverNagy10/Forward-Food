package ie.ul.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ie.ul.foodapp.model.Business;
import ie.ul.foodapp.model.Customer;

public class SignUp extends AppCompatActivity {
    Button btn2_signup;
    EditText user_name, pass_word;
    FirebaseAuth mAuth;
    Button btn_sign;
    Spinner userType;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        user_name=findViewById(R.id.username);
        pass_word=findViewById(R.id.password1);
        btn2_signup=findViewById(R.id.sign);
        mAuth=FirebaseAuth.getInstance();
        btn_sign = findViewById(R.id.LoginBtn);
        userType = findViewById(R.id.spinner2);
        btn_sign.setOnClickListener(v -> startActivity(new Intent(SignUp.this, LogIn.class )));
        btn2_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_name.getText().toString().trim();
                String password= pass_word.getText().toString().trim();
                if(email.isEmpty())
                {
                    user_name.setError("Email is empty");
                    user_name.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    user_name.setError("Enter the valid email address");
                    user_name.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    pass_word.setError("Enter the password");
                    pass_word.requestFocus();
                    return;
                }
                if(password.length()<6)
                {
                    pass_word.setError("Length of the password should be more than 6");
                    pass_word.requestFocus();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String userName = user_name.getText().toString();
                            String typeOfUser = userType.getSelectedItem().toString();
                            FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
                            CollectionReference UserType = dataBase.collection(typeOfUser);

                            if( typeOfUser.equals("User")){
                                Customer thisCustomer = new Customer();
                                Map<String, Object> user = new HashMap<>();
                                user.put("ID", thisCustomer.getID());
                                user.put("email", userName);
                                user.put("orders", thisCustomer.getOrders());
                                user.put("type", typeOfUser);
                                UserType.document(userName).set(user);
                            }
                            else {


                                Business thisBusiness = new Business();
                                Map<String, Object > business = new HashMap<>();
                                business.put("email", userName);
                                business.put("ID", thisBusiness.getID());
                                business.put("type", typeOfUser);
                                business.put("name", thisBusiness.getName());
                                business.put("opening hours monday", thisBusiness.getOpeningHours(1));
                                business.put("opening hours tuesday", thisBusiness.getOpeningHours(2));
                                business.put("opening hours wednesday", thisBusiness.getOpeningHours(3));
                                business.put("opening hours thursday", thisBusiness.getOpeningHours(4));
                                business.put("opening hours friday", thisBusiness.getOpeningHours(5));
                                business.put("opening hours saturday", thisBusiness.getOpeningHours(6));
                                business.put("opening hours sunday", thisBusiness.getOpeningHours(7));
                                business.put("offers", thisBusiness.getOffers());

                                UserType.document(userName).set(business);

                            }
                            Toast.makeText(SignUp.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();

                            if(typeOfUser.equals("Business")) {
                                startActivity(new Intent(SignUp.this, BusinessPageUI.class));
                            }
                            else{
                                startActivity(new Intent(SignUp.this, Home.class));
                            }
                        }
                        else
                        {
                            Toast.makeText(SignUp.this,"You are not Registered! Try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}