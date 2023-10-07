/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package net.ccbluex.liquidbounce.utils.entity;

import java.util.HashSet;
import java.util.Set;
import net.ccbluex.liquidbounce.utils.entity.ICheck;
import net.minecraft.entity.Entity;

public final class EntityValidator {
    private final Set<ICheck> checks = new HashSet<ICheck>();

    public final boolean validate(Entity entity) {
        for (ICheck check : this.checks) {
            if (check.validate(entity)) continue;
            return false;
        }
        return true;
    }

    public void add(ICheck check) {
        this.checks.add(check);
    }
}

