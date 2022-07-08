package ie.ul.foodapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

/* The details class corresponds to the Details screen. When a user clicks on the business name and
   offer, they will be redirected to the details screen which displays additional information about
   the offer.
 */
public class Details extends AppCompatActivity {
    String ID;
    String curr;
    TextView restName,
            restDescription,
            offerName,
            offerDesc,
            price,
            pickup;
    Button bookingPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /* the following will be utilized to alter the placeholder text on the activity_details.xml */
        restName = findViewById(R.id.details_title);
        offerName = findViewById(R.id.offerName);
        offerDesc = findViewById(R.id.offerDesc);
        price = findViewById(R.id.priceInput);
        pickup = findViewById(R.id.inputTime);
        bookingPage = findViewById(R.id.ReserveOffer);

        /*grabs information about the business name and offer from the Home card */
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("description");

        /* pulls the specific offer based on the name, then grabs the additional information about
           the offer to replace the various textViews on the activity_details.xml */
        db.collection("Offers")
                .whereEqualTo("Name", desc)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String description = (String) document.get("description");
                                offerDesc.setText(description);
                                ID = document.getId();
                                String cost = (String) document.get("Price");
                                price.setText(cost);

                                String time = (String) document.get("pickup time");
                                pickup.setText(time);

                                Log.d("Firebase", "Name of Business:" + ((String) document.get("name")));
                                Log.d("Firebase", document.getId() + " => " + document.getData());
                            }
                        }else{
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });


        restName.setText(title);
        offerName.setText(desc);

    }

    public void OnClickBook(View view){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        curr = currentUser.getEmail();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference docRef = dataBase.collection("User").document(Objects.requireNonNull(curr));
         docRef.update("orders", FieldValue.arrayUnion(ID));

        Toast.makeText(Details.this,"You Have Successfully Booked The Offer", Toast.LENGTH_SHORT).show();

    }
    public void OnClickCancel(View view){
        startActivity(new Intent(Details.this, Home.class));

    }
}