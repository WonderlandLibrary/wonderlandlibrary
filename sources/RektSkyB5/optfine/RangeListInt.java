/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import optfine.Config;
import optfine.RangeInt;

public class RangeListInt {
    private RangeInt[] ranges = new RangeInt[0];

    public void addRange(RangeInt p_addRange_1_) {
        this.ranges = (RangeInt[])Config.addObjectToArray(this.ranges, p_addRange_1_);
    }

    public boolean isInRange(int p_isInRange_1_) {
        for (int i2 = 0; i2 < this.ranges.length; ++i2) {
            RangeInt rangeint = this.ranges[i2];
            if (!rangeint.isInRange(p_isInRange_1_)) continue;
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("[");
        for (int i2 = 0; i2 < this.ranges.length; ++i2) {
            RangeInt rangeint = this.ranges[i2];
            if (i2 > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append(rangeint.toString());
        }
        stringbuffer.append("]");
        return stringbuffer.toString();
    }
}

