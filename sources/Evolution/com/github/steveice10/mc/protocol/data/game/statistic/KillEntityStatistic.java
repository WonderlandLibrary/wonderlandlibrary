/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.statistic;

import com.github.steveice10.mc.protocol.data.game.statistic.Statistic;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.Objects;

public class KillEntityStatistic
implements Statistic {
    private String id;

    public KillEntityStatistic(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KillEntityStatistic)) {
            return false;
        }
        KillEntityStatistic that = (KillEntityStatistic)o;
        return Objects.equals(this.id, that.id);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.id);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

