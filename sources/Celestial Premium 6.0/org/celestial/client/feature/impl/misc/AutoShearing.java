/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemShears;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AutoShearing
extends Feature {
    private final NumberSetting radius = new NumberSetting("Radius", 4.0f, 1.0f, 6.0f, 0.1f, () -> true);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true, () -> true);

    public AutoShearing() {
        super("AutoShearing", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0441\u0442\u0440\u0435\u0436\u0435\u0442 \u043e\u0432\u0435\u0446", Type.Misc);
        this.addSettings(this.radius, this.rotate);
    }

    private int getSlotWithShears() {
        for (int i = 0; i < 9; ++i) {
            if (!(Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem() instanceof ItemShears)) continue;
            return i;
        }
        return 0;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        int oldSlot = AutoShearing.mc.player.inventory.currentItem;
        for (Entity entity : AutoShearing.mc.world.loadedEntityList) {
            EntitySheep sheep;
            if (entity == null || !(entity instanceof EntitySheep) || (sheep = (EntitySheep)entity).getHealth() < 1.0f || sheep.getSheared() || sheep.isChild()) continue;
            if (AutoShearing.mc.player.getDistanceToEntity(entity) <= this.radius.getCurrentValue()) {
                AutoShearing.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.getSlotWithShears()));
                if (this.rotate.getCurrentValue()) {
                    float[] rots = RotationHelper.getRotations(sheep, true);
                    event.setYaw(rots[0]);
                    event.setPitch(rots[1]);
                    AutoShearing.mc.player.renderYawOffset = rots[0];
                    AutoShearing.mc.player.rotationYawHead = rots[0];
                    AutoShearing.mc.player.rotationPitchHead = rots[1];
                }
                AutoShearing.mc.playerController.interactWithEntity(AutoShearing.mc.player, entity, EnumHand.MAIN_HAND);
                continue;
            }
            AutoShearing.mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }
    }
}

