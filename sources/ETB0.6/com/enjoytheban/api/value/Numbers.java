package com.enjoytheban.api.value;

/**
 * @author Purity
 * @param <V>
 */

public class Numbers<T extends Number> extends Value<T> {
    //Basically creating variables
    private String name;
    public T min;
    public T max;
    public T inc;
    private boolean integer;

    /***
     * issa constructor
     * @param displayName
     * @param name
     * @param value
     * @param min
     * @param max
     * @param inc
     */
    public Numbers(final String displayName, final String name, final T value, final T min, final T max, final T inc) {
        super(displayName, name);
        this.setValue(value);
        this.min = min;
        this.max = max;
        this.inc = inc;
        integer = false;
    }

    //returns the min value
    public T getMinimum() {
        return this.min;
    }

    //returns the max value
    public T getMaximum() {
        return this.max;
    }

    //sets what the value can be incremented
    public void setIncrement(final T inc) {
        this.inc = inc;
    }

    //gets the increment
    public T getIncrement() {
        return this.inc;
    }

    //Gets the ID/name
    public String getId() {
        return this.name;
    }

}