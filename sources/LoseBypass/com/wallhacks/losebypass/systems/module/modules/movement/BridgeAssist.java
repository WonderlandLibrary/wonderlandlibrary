/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.wallhacks.losebypass.systems.module.modules.movement;

import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.systems.setting.settings.BooleanSetting;
import com.wallhacks.losebypass.systems.setting.settings.IntSetting;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

@Module.Registration(name="BridgeAssist", description="Helps you bridge", category=Module.Category.MOVEMENT)
public class BridgeAssist
extends Module {
    private final IntSetting down = this.intSetting("Down", 4, 1, 10).description("How many blocks below you should be air for bridge assist to work");
    private final BooleanSetting smart = this.booleanSetting("Smart", true).description("Tries to predict when you actually need bridge assist to work");

    @SubscribeEvent
    public void onTick(TickEvent event) {
        boolean flag;
        if (BridgeAssist.mc.thePlayer.onGround && !BridgeAssist.mc.gameSettings.keyBindJump.isPressed() && BridgeAssist.mc.thePlayer.getHeldItem() != null && BridgeAssist.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            if (BridgeAssist.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || BridgeAssist.mc.gameSettings.keyBindForward.isKeyDown()) {
                if ((Boolean)this.smart.getValue() != false) return;
            }
            flag = false;
        } else {
            BridgeAssist.mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown((int)BridgeAssist.mc.gameSettings.keyBindSneak.getKeyCode());
            return;
        }
        for (int i = 1; i <= (Integer)this.down.getValue(); ++i) {
            BlockPos pos = new BlockPos(BridgeAssist.mc.thePlayer.posX, BridgeAssist.mc.thePlayer.posY - (double)i, BridgeAssist.mc.thePlayer.posZ);
            if (BridgeAssist.mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) continue;
            flag = true;
            break;
        }
        BridgeAssist.mc.gameSettings.keyBindSneak.pressed = !flag || Keyboard.isKeyDown((int)BridgeAssist.mc.gameSettings.keyBindSneak.getKeyCode());
    }
}

