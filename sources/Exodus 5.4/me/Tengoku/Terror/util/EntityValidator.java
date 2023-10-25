/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.Tengoku.Terror.util.ICheck;
import net.minecraft.entity.Entity;

public final class EntityValidator {
    private final Set checks = new HashSet();

    public void add(ICheck iCheck) {
        this.checks.add(iCheck);
    }

    public final boolean validate(Entity entity) {
        ICheck iCheck;
        Iterator iterator = this.checks.iterator();
        do {
            if (iterator.hasNext()) continue;
            return true;
        } while ((iCheck = (ICheck)iterator.next()).validate(entity));
        return false;
    }
}

