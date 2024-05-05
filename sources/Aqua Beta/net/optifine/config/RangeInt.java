package net.optifine.config;

public class RangeInt {
    private int min;
    private int max;

    public RangeInt(int min, int max) {
        this.min = Math.min((int)min, (int)max);
        this.max = Math.max((int)min, (int)max);
    }

    public boolean isInRange(int val) {
        return val < this.min ? false : val <= this.max;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public String toString() {
        return "min: " + this.min + ", max: " + this.max;
    }
}
