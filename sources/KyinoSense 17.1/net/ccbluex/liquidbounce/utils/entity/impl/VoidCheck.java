/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 */
package net.ccbluex.liquidbounce.utils.entity.impl;

import net.ccbluex.liquidbounce.utils.entity.ICheck;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

public final class VoidCheck
implements ICheck {
    @Override
    public boolean validate(Entity entity) {
        return this.isBlockUnder(entity);
    }

    private boolean isBlockUnder(Entity entity) {
        int offset = 0;
        while ((double)offset < entity.field_70163_u + (double)entity.func_70047_e()) {
            AxisAlignedBB boundingBox = entity.func_174813_aQ().func_72317_d(0.0, (double)(-offset), 0.0);
            if (!VoidCheck.mc.field_71441_e.func_72945_a(entity, boundingBox).isEmpty()) {
                return true;
            }
            offset += 2;
        }
        return false;
    }
}

