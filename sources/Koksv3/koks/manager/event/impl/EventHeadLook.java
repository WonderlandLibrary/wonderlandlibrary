package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 22:55
 */
public class EventHeadLook extends Event {

    public float f1,f2;
    public double d0, d1, d2;

    public EventHeadLook(double d0, double d1, double d2, float f1, float f2) {
        this.f1 = f1;
        this.f2 = f2;
        this.d0 = d0;
        this.d1 = d1;
        this.d2 = d2;
    }

    public float getF1() {
        return f1;
    }

    public void setF1(float f1) {
        this.f1 = f1;
    }

    public float getF2() {
        return f2;
    }

    public void setF2(float f2) {
        this.f2 = f2;
    }

    public double getD0() {
        return d0;
    }

    public void setD0(double d0) {
        this.d0 = d0;
    }

    public double getD1() {
        return d1;
    }

    public void setD1(double d1) {
        this.d1 = d1;
    }

    public double getD2() {
        return d2;
    }

    public void setD2(double d2) {
        this.d2 = d2;
    }
}
