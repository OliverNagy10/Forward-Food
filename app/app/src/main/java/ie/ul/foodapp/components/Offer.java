package ie.ul.foodapp.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ie.ul.foodapp.R;
import ie.ul.foodapp.utils.StringUtils;

public class Offer extends LinearLayout implements ViewTreeObserver.OnGlobalFocusChangeListener {

    /* UI parts */

    ImageView offerImage;
    TextView businessName;
    TextView offerName;
    TextView offerPrice;
    TextView pickupTime;
    TextView description;


    /* attributes */

    ie.ul.foodapp.model.Offer offer;

    /* Constructors */

    public Offer(Context context) {
        super(context);
        initialise(context);
    }

    public Offer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialise(context);
    }

    public Offer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialise(context);
    }

    public Offer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialise(context);
    }

    protected void initialise(Context context) {
        ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.offer, this);

        offerImage = findViewById(R.id.imageView_offerImage);
        businessName = findViewById(R.id.textView_businessName);
        offerName = findViewById(R.id.textView_offerName);
        offerPrice = findViewById(R.id.textView_offerPrice);
        pickupTime = findViewById(R.id.textView_pickupTime);
        description = findViewById(R.id.textView_description);

        this.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        this.setVisibility(GONE);
    }

    /* layout update */

    public void setOffer(ie.ul.foodapp.model.Offer offer) {
        this.offer = offer;

        if (this.offer == null) {
            this.setVisibility(GONE);
        } else {
            if (this.offer.getBusiness() != null) {
                businessName.setText(this.offer.getBusiness().getName());
            }
            offerName.setText(this.offer.getName());
            offerPrice.setText(StringUtils.priceToString(this.offer.getPrice()));
            pickupTime.setText(StringUtils.pickupTime(this.offer.getPickup()));
            description.setText(this.offer.getDescription());

            updateLayout();

            this.setVisibility(VISIBLE);
        }
    }

    protected void updateLayout() {
        if (isFocused()) {
            offerImage.setImageBitmap(offer.getOfferImage());
            description.setVisibility(VISIBLE);
        } else {
            offerImage.setImageBitmap(offer.getCroppedImage());
            description.setVisibility(GONE);
        }
    }

    /* Listeners */

    @Override
    public boolean isFocused() {
        return super.isFocused() || (this.getFocusedChild() != null);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        updateLayout();
    }

}
