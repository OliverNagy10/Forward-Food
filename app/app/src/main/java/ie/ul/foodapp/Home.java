package ie.ul.foodapp;


import androidx.annotation.NonNull;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;



public class Home extends AppCompatActivity {
    Adapter adapter;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final ArrayList<String> business = new ArrayList<>();
    private final ArrayList<String> offers = new ArrayList<>();
    private final ArrayList<String> IDs = new ArrayList<>();
    private Integer busID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ArrayList<String> business = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //ArrayList<String> items2 =   GetOffers();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //adapter = new Adapter(this, items2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* pulls all offers from firestore */
        db.collection("Offers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            String id = "0";
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //for each offer, gets the business id will be used to get business name
                                if(document.get("Business ID") == null){
                                    break;
                                }else{
                                    id = document.get("Business ID").toString();
                                    Log.d("Firebase", " Business ID:" + id);
                                }

                                String finalId = id;
                                int finalCount = count;

                                //pulls all of the current businesses; will get name based on above id
                                db.collection("Business")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String busName;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String bid = document.get("ID").toString();
                                                        //if the ids match, grab and store the business name
                                                        if (bid.equals(finalId)) {
                                                            Log.d("Firebase", "Business ID: " + bid);
                                                            busName = (String) document.get("name");
                                                            Log.d("Firebase", "Name ID: " + busName);
                                                            business.add(finalCount, busName);
                                                        }
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }else {
                                                        Log.d("Firebase", "Error getting documents: ", task.getException());
                                                        business.add(finalCount, "Error Business");
                                                }
                                            }
                                        });

                                //grabs the name of the offer
                                String name = (String) document.get("Name");

                                offers.add(count, name);
                                count++;

                                Log.d("Firebase", "Name of Business:" + ((String) document.get("name")));
                                Log.d("Firebase", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });


        adapter = new Adapter(this, business, offers);
        recyclerView.setAdapter(adapter);

    }


    /* method is not used but initially used to provide placeholder values */
    private ArrayList<String> getArrayList(){
        //List<Offer> offer = get name of all offers from each business

        ArrayList<String> items = new ArrayList<>();
        items.add("First Restaurant");
        items.add("Second Restaurant");
        items.add("Third Restaurant");
        items.add("Fourth Restaurant");
        items.add("Fifth Restaurant");
        return items;
    }

    /* method not used but initally used to provide placeholder offer names */
    public ArrayList<String> GetOffers(){
        ArrayList<String>   items = new ArrayList<>();
        items.add("Pizza");
        items.add("Chicken soup");
        items.add("Chinese noodles");
        items.add("Turkey");
        items.add("Sandwiches");

        return items;
    }




}