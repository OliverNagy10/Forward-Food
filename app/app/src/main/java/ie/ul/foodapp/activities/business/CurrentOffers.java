package ie.ul.foodapp.activities.business;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import ie.ul.foodapp.R;
import ie.ul.foodapp.model.Business;
import ie.ul.foodapp.model.Offer;

public class CurrentOffers extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_offers);
        backgroundLoad_0();
    }

    protected void backgroundLoad_0() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;

        FirebaseFirestore.getInstance()
                .collection("Business")
                .document(Objects.requireNonNull(currentUser.getEmail()))
                .get()
                .addOnCompleteListener( (task) -> {
                    long businessId = 0;
                    Business business = null;

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            try {
                                businessId = document.getLong("ID");
                                business = new Business();
                                business.setName(document.getString("name"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    backgroundLoad_1(businessId, business);
                });
    }

    protected void backgroundLoad_1(long businessId, Business business) {
        FirebaseFirestore.getInstance()
                .collection("Offers")
                .get()
                .addOnCompleteListener((task) -> {
                    List<Offer> offers = new LinkedList<>();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getLong("Business ID") == businessId) {
                                Offer o = new Offer();

                                o.setBusiness(business);
                                o.setName(document.get("Name").toString());
                                o.setDescription(document.get("description").toString());
                                {
                                    LocalTime pickUpTimeAsLT;
                                    String pickupTimeAsString = document.get("pickup time").toString();

                                    if (pickupTimeAsString.contains("pm")) {
                                        String[] time = pickupTimeAsString.split("pm")[0].split(":");
                                        pickUpTimeAsLT = LocalTime.of(12 + Integer.parseInt(time[0]), time.length > 1 ? Integer.parseInt(time[1]) : 0);
                                    } else { // pm
                                        String[] time = pickupTimeAsString.split("am")[0].split(":");
                                        pickUpTimeAsLT = LocalTime.of(Integer.parseInt(time[0]), time.length > 1 ? Integer.parseInt(time[1]) : 0);
                                    }

                                    o.setPickup(pickUpTimeAsLT);
                                }
                                o.setPrice(Double.parseDouble(document.get("Price").toString()));
                                o.setOfferImage(BitmapFactory.decodeResource(getResources(), R.drawable.food_banner));

                                offers.add(o);
                            }
                        }
                    }

                    backgroundLoad_2(offers);
                });
    }

    protected void backgroundLoad_2(List<Offer> offers) {
        for (Offer o : offers) {
            ie.ul.foodapp.components.Offer offerView = new ie.ul.foodapp.components.Offer(this);
            offerView.setOffer(o);
            ((LinearLayout)findViewById(R.id.linearLayout_content)).addView(offerView);
        }
    }

}
