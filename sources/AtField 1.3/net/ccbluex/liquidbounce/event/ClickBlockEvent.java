/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

public final class ClickBlockEvent
extends Event {
    private final IEnumFacing WEnumFacing;
    private final WBlockPos clickedBlock;

    public ClickBlockEvent(@Nullable WBlockPos wBlockPos, @Nullable IEnumFacing iEnumFacing) {
        this.clickedBlock = wBlockPos;
        this.WEnumFacing = iEnumFacing;
    }

    public final IEnumFacing getWEnumFacing() {
        return this.WEnumFacing;
    }

    public final WBlockPos getClickedBlock() {
        return this.clickedBlock;
    }
}

