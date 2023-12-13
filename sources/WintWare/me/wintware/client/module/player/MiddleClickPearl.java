/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.wintware.client.module.player;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Mouse;

public class MiddleClickPearl
extends Module {
    public MiddleClickPearl() {
        super("MiddleClickPearl", Category.Player);
        Main.instance.setmgr.rSetting(new Setting("Current Item", this, false));
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        if (Mouse.isButtonDown(2)) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = Minecraft.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() != Items.ENDER_PEARL) continue;
                Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(i));
                Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
            }
        }
    }

    @Override
    public void onDisable() {
        Minecraft.player.connection.sendPacket(new CPacketHeldItemChange(Minecraft.player.inventory.currentItem));
        super.onDisable();
    }
}

