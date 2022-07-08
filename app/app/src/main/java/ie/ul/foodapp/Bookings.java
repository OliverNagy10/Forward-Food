package ie.ul.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/** unused class but could be implemented with slight changes to present a bookings screen
 *  that displays all of the bookings that the customer books. Would follow a similar format
 *  to the Home.java; however, only pull offers that the current user booked */
public class Bookings extends AppCompatActivity {
    Adapter adapter;
    private final ArrayList<String> bookedItems = new ArrayList<>();
    private final ArrayList<String> orders = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        //ArrayList<String> orders = new ArrayList<>();
        //ArrayList<String> bookedItems = getBookedItems();
        //ArrayList<String> offers = new ArrayList<>();
        //orders = getDesc();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /** old version for pulling information from firebase; needs to be updated
         *  to fit the new format. Based on the old format of how information was stored
         *  in firebase, the following grabs orders booked by the user*/
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()){
                                int i = 0;
                                List<String> group = (List<String>) document.get("orders");
                                while(group.size() != i){
                                    String name = (String)document.get("email");
                                    String desc = group.get(i);
                                    bookedItems.add(count, name);
                                    orders.add(count, desc);
                                    count++;
                                    i++;
                                }
                                //String name = (String)document.get("email");
                                //bookedItems.add(count, name);
                                //business.add(count, name);
                                //count++;
                                Log.d("Firebase", "Name of Business:" + ((String) document.get("email")));
                                Log.d("Firebase", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        }else{
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });


        adapter = new Adapter(this, bookedItems, orders);
        recyclerView.setAdapter(adapter);
    }

    /* orginally used for placeholders but implemented firebase */
    private ArrayList<String> getBookedItems(){
        ArrayList<String> items = new ArrayList<>();
        items.add("First Booking");
        items.add("Second Booking");
        items.add("Third Booking");
        items.add("Fourth Booking");
        return items;
    }

    /* orginally used for placeholders but implemented firebase */
    private ArrayList<String> getDesc(){
        ArrayList<String> desc = new ArrayList<>();
        desc.add("First Booking Desc");
        desc.add("Second Booking Desc");
        desc.add("Third Booking Desc");
        desc.add("Fourth Booking Desc");
        return desc;
    }
}