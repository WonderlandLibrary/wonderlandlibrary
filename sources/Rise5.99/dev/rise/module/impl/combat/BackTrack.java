/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.combat;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.render.RenderUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@ModuleInfo(name = "BackTrack", description = "Lets you attack people in their previous locations", category = Category.COMBAT)
public final class BackTrack extends Module {

    public static EntityLivingBase target;
    public static List<Vec3> pastPositions = new ArrayList<>();
    public static List<Vec3> forwardPositions = new ArrayList<>();
    public static List<Vec3> positions = new ArrayList<>();
    private final Deque<Packet<?>> packets = new ArrayDeque<>();

    private final NumberSetting amount = new NumberSetting("Amount", this, 20, 1, 100, 1);
    private final NumberSetting forward = new NumberSetting("Forward", this, 20, 1, 100, 1);

    private int ticks;

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.thePlayer.ticksExisted < 5) {
            onDisable();
            return;
        }

        if (target == null) return;

        pastPositions.add(new Vec3(target.posX, target.posY, target.posZ));

        final double deltaX = (target.posX - target.lastTickPosX) * 2;
        final double deltaZ = (target.posZ - target.lastTickPosZ) * 2;

        forwardPositions.clear();
        int i = 0;
        while (forward.getValue() > forwardPositions.size()) {
            i++;
            forwardPositions.add(new Vec3(target.posX + deltaX * i, target.posY, target.posZ + deltaZ * i));
        }

        while (pastPositions.size() > (int) amount.getValue()) {
            pastPositions.remove(0);
        }

        positions.clear();
        positions.addAll(forwardPositions);
        positions.addAll(pastPositions);

        ticks++;
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        if (target != null && !positions.isEmpty()) RenderUtil.renderBreadCrumbs(positions);
    }

    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTarget() instanceof EntityPlayer) target = (EntityLivingBase) event.getTarget();
        ticks = 0;
    }

    @Override
    protected void onDisable() {
        target = null;
        positions.clear();
        pastPositions.clear();
        forwardPositions.clear();
        packets.clear();
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {

        /*
        if (target == null) return;

        Packet p = event.getPacket();


        packets.add(p);
        event.setCancelled(true);
        Rise.addChatMessage("test");


        if ((int) amount.getValue() <= pastPositions.size()) {

            for (final Packet thisPacket : packets) sendPacketNoEvent(thisPacket);

            pastPositions.clear();
            packets.clear();
            Rise.addChatMessage("Reset");
        }*/
    }

}
