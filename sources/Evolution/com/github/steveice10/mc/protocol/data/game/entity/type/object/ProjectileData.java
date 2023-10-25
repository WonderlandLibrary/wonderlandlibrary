/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.entity.type.object;

import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectData;
import com.github.steveice10.mc.protocol.util.ObjectUtil;

public class ProjectileData
implements ObjectData {
    private int ownerId;

    public ProjectileData(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectileData)) {
            return false;
        }
        ProjectileData that = (ProjectileData)o;
        return this.ownerId == that.ownerId;
    }

    public int hashCode() {
        return ObjectUtil.hashCode(this.ownerId);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

