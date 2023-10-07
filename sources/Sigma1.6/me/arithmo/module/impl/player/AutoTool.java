/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.module.impl.player;

import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventTick;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.util.misc.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class AutoTool
extends Module {
    public AutoTool(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events={EventTick.class})
    public void onEvent(Event event) {
        if (!AutoTool.mc.gameSettings.keyBindAttack.getIsKeyPressed()) {
            return;
        }
        if (AutoTool.mc.objectMouseOver == null) {
            return;
        }
        BlockPos pos = AutoTool.mc.objectMouseOver.getBlockPos();
        if (pos == null) {
            return;
        }
        BlockUtil.updateTool(pos);
    }
}

