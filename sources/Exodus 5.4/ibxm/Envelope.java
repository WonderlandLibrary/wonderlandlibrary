/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

public class Envelope {
    private int[] ampls;
    public boolean looped;
    public boolean sustain;
    private int[] ticks;
    private int loop_start_tick;
    private int loop_end_tick;
    private int sustain_tick;

    public void set_num_points(int n) {
        if (n <= 0) {
            n = 1;
        }
        this.ticks = new int[n];
        this.ampls = new int[n];
        this.set_point(0, 0, 0);
    }

    public void set_sustain_point(int n) {
        if (n < 0) {
            n = 0;
        }
        if (n >= this.ticks.length) {
            n = this.ticks.length - 1;
        }
        this.sustain_tick = this.ticks[n];
    }

    public void set_point(int n, int n2, int n3) {
        if (n >= 0 && n < this.ticks.length) {
            if (n == 0) {
                n2 = 0;
            }
            if (n > 0) {
                if (n2 < this.ticks[n - 1]) {
                    n2 += 256;
                }
                if (n2 <= this.ticks[n - 1]) {
                    System.out.println("Envelope: Point not valid (" + n2 + " <= " + this.ticks[n - 1] + ")");
                    n2 = this.ticks[n - 1] + 1;
                }
            }
            this.ticks[n] = n2;
            this.ampls[n] = n3;
            ++n;
            while (n < this.ticks.length) {
                this.ticks[n] = this.ticks[n - 1] + 1;
                this.ampls[n] = 0;
                ++n;
            }
        }
    }

    public void set_loop_points(int n, int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n >= this.ticks.length) {
            n = this.ticks.length - 1;
        }
        if (n2 < n || n2 >= this.ticks.length) {
            n2 = n;
        }
        this.loop_start_tick = this.ticks[n];
        this.loop_end_tick = this.ticks[n2];
    }

    public int next_tick(int n, boolean bl) {
        if (this.looped && ++n >= this.loop_end_tick) {
            n = this.loop_start_tick;
        }
        if (this.sustain && bl && n >= this.sustain_tick) {
            n = this.sustain_tick;
        }
        return n;
    }

    public int calculate_ampl(int n) {
        int n2 = this.ampls[this.ticks.length - 1];
        if (n < this.ticks[this.ticks.length - 1]) {
            int n3 = 0;
            int n4 = 1;
            while (n4 < this.ticks.length) {
                if (this.ticks[n4] <= n) {
                    n3 = n4;
                }
                ++n4;
            }
            int n5 = this.ticks[n3 + 1] - this.ticks[n3];
            int n6 = this.ampls[n3 + 1] - this.ampls[n3];
            n2 = (n6 << 15) / n5;
            n2 = n2 * (n - this.ticks[n3]) >> 15;
            n2 += this.ampls[n3];
        }
        return n2;
    }

    public void dump() {
        int n = 0;
        while (n < this.ticks.length) {
            System.out.println(String.valueOf(this.ticks[n]) + ", " + this.ampls[n]);
            ++n;
        }
        int n2 = 0;
        while (n2 < 222) {
            System.out.print(String.valueOf(this.calculate_ampl(n2)) + ", ");
            ++n2;
        }
    }

    public Envelope() {
        this.set_num_points(1);
    }
}

