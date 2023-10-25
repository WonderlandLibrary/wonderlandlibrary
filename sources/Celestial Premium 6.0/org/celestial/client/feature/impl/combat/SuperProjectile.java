/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class SuperProjectile
extends Feature {
    private BooleanSetting bow;
    private BooleanSetting eggs;
    private BooleanSetting pearls;
    private BooleanSetting snowballs;
    private NumberSetting shotPower = new NumberSetting("Shot Power", 70.0f, 1.0f, 100.0f, 5.0f, () -> true);

    public SuperProjectile() {
        super("SuperProjectile", "\u0423\u0431\u0438\u0432\u0430\u0435\u0442 \u043f\u0440\u043e\u0442\u0438\u0432\u043d\u0438\u043a\u043e\u0432 \u0432\u044b\u0441\u0442\u0440\u0435\u043b\u043e\u043c \u0438\u0437 \u043b\u0443\u043a\u0430, \u044d\u043d\u0434\u0435\u0440 \u043f\u0435\u0440\u043b\u0430, \u0441\u043d\u0435\u0436\u043a\u0430 \u0438 \u0442.\u0434 \u0441 1 \u043f\u043e\u043f\u0430\u0434\u0430\u043d\u0438\u044f", Type.Combat);
        this.bow = new BooleanSetting("Bow", true, () -> true);
        this.eggs = new BooleanSetting("Eggs", false, () -> true);
        this.pearls = new BooleanSetting("Pearls", false, () -> true);
        this.snowballs = new BooleanSetting("Snowballs", false, () -> true);
        this.addSettings(this.shotPower, this.bow, this.eggs, this.pearls, this.snowballs);
    }

    private void doShot() {
        SuperProjectile.mc.player.connection.sendPacket(new CPacketEntityAction(SuperProjectile.mc.player, CPacketEntityAction.Action.START_SPRINTING));
        int index = 0;
        while ((float)index < this.shotPower.getCurrentValue()) {
            SuperProjectile.mc.player.connection.sendPacket(new CPacketPlayer.Position(SuperProjectile.mc.player.posX, SuperProjectile.mc.player.posY + 1.0E-10, SuperProjectile.mc.player.posZ, false));
            SuperProjectile.mc.player.connection.sendPacket(new CPacketPlayer.Position(SuperProjectile.mc.player.posX, SuperProjectile.mc.player.posY - 1.0E-10, SuperProjectile.mc.player.posZ, true));
            ++index;
        }
    }

    @EventTarget
    public void onSendPacket(EventSendPacket event) {
        ItemStack handStack;
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            ItemStack handStack2;
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = SuperProjectile.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty() && handStack2.getItem() instanceof ItemBow && this.bow.getCurrentValue()) {
                this.doShot();
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.MAIN_HAND && !(handStack = SuperProjectile.mc.player.getHeldItem(EnumHand.MAIN_HAND)).isEmpty()) {
            if (handStack.getItem() instanceof ItemEgg && this.eggs.getCurrentValue()) {
                this.doShot();
            } else if (handStack.getItem() instanceof ItemEnderPearl && this.pearls.getCurrentValue()) {
                this.doShot();
            } else if (handStack.getItem() instanceof ItemSnowball && this.snowballs.getCurrentValue()) {
                this.doShot();
            }
        }
    }
}

