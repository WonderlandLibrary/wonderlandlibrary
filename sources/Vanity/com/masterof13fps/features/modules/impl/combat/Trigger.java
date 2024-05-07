package com.masterof13fps.features.modules.impl.combat;

import com.masterof13fps.features.commands.impl.Friend;
import com.masterof13fps.features.modules.Category;
import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.manager.eventmanager.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@ModuleInfo(name = "Trigger", category = Category.COMBAT, description = "You automatically hit when a target is focused")
public class Trigger extends Module {
    public static int cps = 12;

    public static boolean hurttime = false;
    private boolean isCritting;

    @Override
    public void onToggle() {

    }

    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        boolean sprint = getPlayer().isSprinting();
        try {
            if (mc.objectMouseOver.typeOfHit == null) {
                return;
            }
            if ((mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) && (timeHelper.isDelayComplete(1000 / cps))) {
                Entity ent = mc.objectMouseOver.entityHit;
                if ((ent instanceof EntityLivingBase)) {
                    if (getPlayer().getDistanceToEntity(ent) > 3.9D) {
                        return;
                    }
                    if ((hurttime) && (((EntityLivingBase) ent).hurtTime > 0.3D)) {
                        return;
                    }
                    if (!(ent instanceof EntityPlayer)) {
                        return;
                    }
                    if (Friend.friends.contains(ent.getName())) {
                        return;
                    }
                    if (ent.isInvisible()) {
                        return;
                    }
                    getPlayer().setSprinting(false);
                    if ((ent.isEntityAlive()) && (!getPlayer().isEating()) && (!getPlayer().isDead)) {
                        if (getPlayer().isBlocking()) {
                            mc.getNetHandler().addToSendQueue(
                                    new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                            new BlockPos(0, 0, 0), EnumFacing.UP));
                        }
                        getPlayer().swingItem();
                        mc.getNetHandler().getNetworkManager()
                                .sendPacket(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
                        for (int i = 0; i < 3; i++) {
                            mc.getNetHandler().addToSendQueue(
                                    new C02PacketUseEntity(ent, new Vec3(ent.posX, ent.posY, ent.posZ)));
                        }
                        getPlayer().setSprinting(true);
                    }
                }
            }
        } catch (Exception localException) {
        }
    }
}
