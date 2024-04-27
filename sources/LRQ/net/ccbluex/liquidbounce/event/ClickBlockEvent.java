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
    private final WBlockPos clickedBlock;
    private final IEnumFacing WEnumFacing;

    public final WBlockPos getClickedBlock() {
        return this.clickedBlock;
    }

    public final IEnumFacing getWEnumFacing() {
        return this.WEnumFacing;
    }

    public ClickBlockEvent(@Nullable WBlockPos clickedBlock, @Nullable IEnumFacing WEnumFacing) {
        this.clickedBlock = clickedBlock;
        this.WEnumFacing = WEnumFacing;
    }
}

