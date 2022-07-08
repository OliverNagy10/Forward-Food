package ie.ul.foodapp.model;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

public class Customer {

    String Email;
    protected List<Offer> Orders;
    protected int ID;
    protected int max = 1000000000;
    protected int min = 1;

    public Customer() {
        Email = null;
        Orders= new LinkedList<>();
        ID = (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    public Customer(Customer c) {
        this.Email = c.Email;
        this.Orders = new LinkedList<>();
        ID = c.ID;
    }
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Customer(this);
    }

    public String getEmail() { return Email; }

    public int getID() {return  ID;}
    //used for database
    public void setID(int id){this.ID = id;};

    //used for database
    public void setEmail(String email) {
        this.Email = email;
    }
    //used for database
    public void setOrders(List<Offer> orders){
        this.Orders = orders;
    }

    public void addOrder(Offer order) { Orders.add(order); }

    public List<Offer> getOrders() { return Orders; }



}
