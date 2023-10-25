/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.game.statistic.Statistic;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;

public class CustomStatistic
implements Statistic {
    private String name;

    public CustomStatistic(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomStatistic)) {
            return false;
        }
        CustomStatistic that = (CustomStatistic)o;
        return Objects.equals(this.name, that.name);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.name);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

