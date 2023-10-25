/*
 * Decompiled with CFR 0.152.
 */
package ibxm;

public class Pattern {
    private int data_offset;
    private int note_index;
    public int num_rows = 1;
    private byte[] pattern_data;

    public void set_pattern_data(byte[] byArray) {
        if (byArray != null) {
            this.pattern_data = byArray;
        }
        this.data_offset = 0;
        this.note_index = 0;
    }

    public void get_note(int[] nArray, int n) {
        if (n < this.note_index) {
            this.note_index = 0;
            this.data_offset = 0;
        }
        while (this.note_index <= n) {
            this.data_offset = this.next_note(this.data_offset, nArray);
            ++this.note_index;
        }
    }

    public Pattern() {
        this.set_pattern_data(new byte[0]);
    }

    public int next_note(int n, int[] nArray) {
        if (n < 0) {
            n = this.pattern_data.length;
        }
        int n2 = 128;
        if (n < this.pattern_data.length) {
            n2 = this.pattern_data[n] & 0xFF;
        }
        if ((n2 & 0x80) == 128) {
            ++n;
        } else {
            n2 = 31;
        }
        int n3 = 0;
        while (n3 < 5) {
            nArray[n3] = 0;
            if ((n2 & 1) == 1 && n < this.pattern_data.length) {
                nArray[n3] = this.pattern_data[n] & 0xFF;
                ++n;
            }
            n2 >>= 1;
            ++n3;
        }
        return n;
    }
}

