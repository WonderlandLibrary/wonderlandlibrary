/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 */
package net.ccbluex.liquidbounce.utils.entity.impl;

import net.ccbluex.liquidbounce.utils.entity.ICheck;
import net.minecraft.entity.Entity;

public final class WallCheck
implements ICheck {
    @Override
    public boolean validate(Entity entity) {
        return WallCheck.mc.field_71439_g.func_70685_l(entity);
    }
}

