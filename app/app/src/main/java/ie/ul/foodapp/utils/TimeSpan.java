package ie.ul.foodapp.utils;

import androidx.annotation.NonNull;

import java.time.LocalTime;

public class TimeSpan {

    protected LocalTime from;
    protected LocalTime to;

    public TimeSpan() {
        this(LocalTime.of(8, 0), LocalTime.of(20, 0));
    }
    public TimeSpan(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }
    public TimeSpan(TimeSpan ts) {
        this(ts.getFrom(), ts.getTo());
    }

    @NonNull
    @Override
    protected Object clone() {
        return new TimeSpan(this);
    }

    public LocalTime getFrom() {
        return from;
    }

    public LocalTime getTo() {
        return to;
    }

}