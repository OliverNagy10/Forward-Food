package ie.ul.foodapp.model;

import android.graphics.Bitmap;
import android.location.Address;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ie.ul.foodapp.utils.TimeSpan;

public class Business {

    protected String name;
    protected Bitmap banner;
    private int max = 1000000000;
    private int min = 1 ;
    protected TimeSpan[] openingHours;
    protected Object localisation;
    protected int ID;
    protected android.location.Address address;

    protected List<Offer> previousOffers;
    protected List<Offer> currentOffers;

    public Business() {
        name = "My business";
        banner = null;
        ID = (int)Math.floor(Math.random()*(max-min+1)+min);
        openingHours = new TimeSpan[7];
        Arrays.fill(openingHours, null);
        localisation = null;
        address = null;
        previousOffers = new LinkedList<>();
        currentOffers = new LinkedList<>();
    }
    public Business(Business b) {
        this.name = b.name;
        this.banner = b.banner;
        this.ID = b.ID ;
        this.openingHours = new TimeSpan[7];
        this.address = b.address;
        System.arraycopy(b.openingHours, 0, this.openingHours, 0, this.openingHours.length);
        this.localisation = b.localisation;
        this.previousOffers = new LinkedList<>();
        this.currentOffers = new LinkedList<>();
    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Business(this);
    }

    public String getName() {
        return name;
    }
    //used for database
    public void setID(int id){ this.ID = id; }
    //used for database
    public int getID() {return ID;}
    //used for database
    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }
    public void SetAddress(android.location.Address a) {
        this.address = a;
    }

    public Bitmap getBanner() {
        return banner;
    }

    public void setBanner(Bitmap banner) {
        this.banner = banner;
    }

    public TimeSpan getOpeningHours(int day) {
        if (day < 0 || day >= 7) {
            return null;
        } else {
            return openingHours[day];
        }
    }



    public void setOpeningHours(TimeSpan openingHours, int day) {
        if (!(day < 0 || day >= 7)) {
            this.openingHours[day] = openingHours;
        }
    }

    public Object getLocalisation() {
        return localisation;
    }

    public void setLocalisation(Object localisation) {
        this.localisation = localisation;
    }

    //used for database
    public void setOffers(List<Offer> currentOffers) {
        this.currentOffers = currentOffers;
    }

    public void addOffer(Offer offer) {
        currentOffers.add(offer);
    }

    public List<Offer> getOffers() {
        return currentOffers;
    }

    public void archiveOffer(Offer offer) {
        if (currentOffers.remove(offer)) {
            previousOffers.add(offer);
        }
    }


}
