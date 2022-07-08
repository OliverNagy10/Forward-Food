package ie.ul.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ie.ul.foodapp.activities.business.CreateOffer;
import ie.ul.foodapp.activities.business.CurrentOffers;

public class BusinessPageUI extends AppCompatActivity {
    String curr;
    EditText name;
    TabLayout tab;
    EditText from;
    EditText to;
    String mondayFrom;
    String mondayTo;
    String tuesdayFrom;
    String tuesdayTo;
    String wednesdayFrom;
    String wednesdayTo;
    String thursdayFrom;
    String thursdayTo;
    String fridayFrom;
    String fridayTo;
    String SaturdayFrom;
    String SaturdayTo;
    String SundayFrom;
    String SundayTo;
    TextView banner;
    FloatingActionButton createOffer;
    FloatingActionButton showOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_settings);

        name = findViewById(R.id.Name);
        tab = findViewById(R.id.tabLayout_daysOfTheWeek);
        from = findViewById(R.id.editTextTime_fromTime);
        to = findViewById(R.id.editTextTime_toTime);
        banner = findViewById(R.id.buss_name);
        createOffer = findViewById(R.id.createOffer);
        showOffers = findViewById(R.id.showOffers);



    }
    public void OnClickCreateOffer(View view){
     startActivity(new Intent(BusinessPageUI.this, CreateOffer.class));
    }
    public void OnClickShowOffers(View view){
     startActivity(new Intent(BusinessPageUI.this, CurrentOffers.class));
    }

    public void updateBussiness() {

        switch (tab.getSelectedTabPosition()) {
            case 0: {
                mondayFrom = from.getText().toString();
                mondayTo = to.getText().toString();
                break;
            }
            case 1: {
                tuesdayFrom = from.getText().toString();
                tuesdayTo = to.getText().toString();
                break;
            }
            case 2: {
                wednesdayFrom = from.getText().toString();
                wednesdayTo = to.getText().toString();
                break;
            }
            case 3: {
                thursdayFrom = from.getText().toString();
                thursdayTo = to.getText().toString();
                break;
            }
            case 4: {
                fridayFrom = from.getText().toString();
                fridayTo = to.getText().toString();
                break;
            }
            case 5: {
                SaturdayFrom = from.getText().toString();
                SaturdayTo = to.getText().toString();
                break;
            }
            case 6: {
                SundayFrom = from.getText().toString();
                SundayTo = to.getText().toString();
                break;
            }




        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        curr = currentUser.getEmail();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference docRef = dataBase.collection("Business").document(Objects.requireNonNull(curr));
        docRef.update("name", name.getText().toString());
        docRef.update("opening hours monday", mondayFrom + "-" + mondayTo);
        docRef.update("opening hours tuesday", tuesdayFrom + "-" + tuesdayTo);
        docRef.update("opening hours wednesday", wednesdayFrom + "-" + wednesdayTo);
        docRef.update("opening hours thursday", thursdayFrom + "-" + thursdayTo);
        docRef.update("opening hours friday", fridayFrom + "-" + fridayTo);
        docRef.update("opening hours saturday", SaturdayFrom + "-" + SaturdayTo);
        docRef.update("opening hours sunday", SundayFrom + "-" + SundayTo);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                       banner.setText(document.getString("name"));
                    }
                }
            }
        });

    }


    public void UpdateBiss(View view){
        updateBussiness();

    }
}




