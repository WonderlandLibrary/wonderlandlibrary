package net.ccbluex.LiquidBase.module.modules.combat;

import net.ccbluex.LiquidBase.event.EventTarget;
import net.ccbluex.LiquidBase.event.events.MotionUpdateEvent;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleCategory;
import net.ccbluex.LiquidBase.module.ModuleInfo;
import net.ccbluex.LiquidBase.valuesystem.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@ModuleInfo(moduleName = "Aura", moduleDescription = "You need to know this.", moduleCateogry = ModuleCategory.COMBAT, defaultKey = Keyboard.KEY_R)
public class KillAura extends Module {

    private EntityLivingBase target;

    private Value<Float> rangeValue = new Value<>("Range", 4.2F);

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if(!getState())
            return;

        switch(event.getState()) {
            case PRE:
                Object[] objects = mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase && entity != mc.thePlayer && ((EntityLivingBase) entity).getHealth() > 0F && entity.getDistanceToEntity(mc.thePlayer) <= rangeValue.getObject()).sorted(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer))).toArray();

                if(objects.length > 0)
                    target = (EntityLivingBase) objects[0];

                // Your facing etc here then
                break;
            case POST:
                if(target == null)
                    return;

                mc.thePlayer.swingItem();
                mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                target = null;
                break;
        }
    }
}
