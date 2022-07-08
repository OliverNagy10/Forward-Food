package ie.ul.foodapp.activities.business;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ie.ul.foodapp.BusinessPageUI;
import ie.ul.foodapp.R;
import ie.ul.foodapp.model.Offer;

public class CreateOffer extends AppCompatActivity {
EditText name;
EditText desc;
EditText price;
EditText pickup;
ImageView image;
Object BusinessID;
String curr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer);
        name = findViewById(R.id.editText_offerName);
        desc = findViewById(R.id.editText_description);
        price = findViewById(R.id.editText_price);
        pickup = findViewById(R.id.Name);
        image  = findViewById(R.id.imageView_offerImage);
        Button btn1 = (Button) findViewById(R.id.CancelButton);

        btn1.setOnClickListener(v -> startActivity(new Intent(CreateOffer.this, BusinessPageUI.class)));

    }

    public void SetID(Object id){
        this.BusinessID = id;
    }
    public Object getBusinessID(){
        return BusinessID;
    }


    public void getID(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        curr = currentUser.getEmail();
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference docRef = dataBase.collection("Business").document(Objects.requireNonNull(curr));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        SetID(document.get("ID"));
                    } else {
                        Log.d(TAG, "Cant get Business Data ");
                    }
                } else {
                    Log.d(TAG, "Getting Business Data Failed:", task.getException());
                }
            }
        });
    }




    public void Create(View view){
       getID();
       myOnClickCreateOffer();

    }

    public void myOnClickCreateOffer(){


        // save offer
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        CollectionReference Offer = dataBase.collection("Offers");
        Offer thisOffer = new Offer();
         int thisID = thisOffer.getID();
        Map<String, Object > offer = new HashMap<>();
        offer.put("Business ID" , getBusinessID());
        offer.put("Name", name.getText().toString());
        offer.put("description", desc.getText().toString());
        offer.put("Price", price.getText().toString());
        offer.put("pickup time", pickup.getText().toString());
        Offer.document(String.valueOf(thisID)).set(offer);
        System.out.println(curr);
        Toast.makeText(CreateOffer.this,"You Have Successfully Created an Offer", Toast.LENGTH_SHORT).show();
    }


}