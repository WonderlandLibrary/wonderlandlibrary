package best.azura.client.impl.module.impl.combat.copsncrims;

import best.azura.client.api.friend.Friend;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventRender3DPost;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.player.RaytraceUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.render.RenderUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
@ModuleInfo(name = "CopsNCrims", description = "Module for CopsNCrims (hypixel)", category = Category.COMBAT)
public class CopsNCrims extends Module {

    private float[] currentRotations;
    private Entity target;
    private final ArrayList<Entity> lastAttacked = new ArrayList<>();
    private final NumberValue<Double> rangeValue = new NumberValue<>("Range", null, 100D, 10D, 1000D);
    private final BooleanValue teamsValue = new BooleanValue("Teams", null, true);
    private final BooleanValue silentAimValue = new BooleanValue("Silent Aim", null, true);
    public final NumberValue<Double> minPredictValue = new NumberValue<Double>("Min Predict", null, () -> {
        this.minPredictValue.setObject(Math.min(this.maxPredictValue.getObject(), this.minPredictValue.getObject()));
        return true;
    }, 0D, 0.1D, 0D, 10D);
    public final NumberValue<Double> maxPredictValue = new NumberValue<Double>("Max Predict", null, (val) -> {
        if (val != this.maxPredictValue) return;
        minPredictValue.setObject(Math.min(minPredictValue.getObject(), (Double) val.getObject()));
    }, 0D, 0.1D, 0D, 10D);
    private final DelayUtil switchDelay = new DelayUtil();
    private final HashMap<Entity, ArrayList<Vec3>> positions = new HashMap<>();

    @EventHandler
    public final Listener<Event> eventListener = this::handle;

    public void handle(final Event event) {
        if (event instanceof EventMotion) {
            EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                findTarget();
                if (target != null) {
                    mc.rightClickMouse();
                    getRotation();
                    e.yaw = currentRotations[0];
                    e.pitch = currentRotations[1];
                    if (silentAimValue.getObject()) return;
                    mc.thePlayer.rotationYaw = e.yaw;
                    mc.thePlayer.rotationPitch = e.pitch;
                }
            }
        }
        if (event instanceof EventRender3DPost && target != null) {
            Entity en = target;
            double x = en.lastTickPosX + (en.posX - en.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX, y = en.lastTickPosY + (en.posY - en.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY, z = en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            AxisAlignedBB bb = en.getEntityBoundingBox();
            Color c = new Color(150, 0, 0, 50);
            double width = (bb.maxX - bb.minX + bb.maxZ - bb.minZ) / 4;
            double height = (bb.maxY - bb.minY);
            RenderUtil.INSTANCE.renderBox(x, y, z, width, height, c, false);
            GlStateManager.resetColor();
            if (currentRotations == null) return;
            MovingObjectPosition position = RaytraceUtil.rayTrace(mc.thePlayer.getDistanceToEntity(target), currentRotations[0], currentRotations[1]);
            if (position == null) return;
            if (position.getBlockPos() == null) return;
            BlockPos pos = position.getBlockPos();
            x = pos.getX() - RenderManager.renderPosX;
            y = pos.getY() - RenderManager.renderPosY;
            z = pos.getZ() - RenderManager.renderPosZ;
            c = new Color(150, 0, 255, 50);
            RenderUtil.INSTANCE.renderBox(x + 0.5, y, z + 0.5, width, height, c, false);
            GlStateManager.resetColor();
            glPushMatrix();
            glColor4f(1, 1, 1, 1);
            glEnable(GL_BLEND);
            glEnable(GL_LINE_SMOOTH);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_TEXTURE_2D);
            glBegin(GL_LINE_LOOP);
            glVertex3d(0, mc.thePlayer.getEyeHeight(), 0);
            glVertex3d(x + 0.5, y, z + 0.5);
            glEnd();
            glDisable(GL_BLEND);
            glDisable(GL_LINE_SMOOTH);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_TEXTURE_2D);
            glPopMatrix();
            GlStateManager.resetColor();
        }
    }

    private void getRotation() {
        if (currentRotations == null)
            currentRotations = new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
        final double travelX = target.posX - target.lastTickPosX, travelY = target.posY - target.lastTickPosY, travelZ = target.posZ - target.lastTickPosZ;
        final AxisAlignedBB baseBB = target.getEntityBoundingBox();
        AxisAlignedBB bb = baseBB.offset(travelX * MathUtil.getRandom_double(minPredictValue.getObject(), maxPredictValue.getObject()), travelY * MathUtil.getRandom_double(minPredictValue.getObject(), maxPredictValue.getObject()), travelZ * MathUtil.getRandom_double(minPredictValue.getObject(), maxPredictValue.getObject()));
        final Vec3 vec = new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * mc.thePlayer.getDistanceToEntity(target) * 0.01, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
        currentRotations = RotationUtil.getNeededRotations(vec);
        if (mc.thePlayer.getHeldItem() != null) {
            ItemStack stack = mc.thePlayer.getHeldItem();
            CopsNCrimsWeapons weapon = null;
            for (CopsNCrimsWeapons c : CopsNCrimsWeapons.values())
                if (EnumChatFormatting.getTextWithoutFormattingCodes(stack.getDisplayName()).contains(c.getWeapon()))
                    weapon = c;
            if (weapon == null) return;
            currentRotations[1] += weapon.getRecoil();
        }
    }

    public void findTarget() {
        if (!isValid(target)) target = null;
        if (!switchDelay.hasReached(150)) return;
        ArrayList<Entity> list = getTargets();
        if (lastAttacked.size() >= list.size()) lastAttacked.clear();
        for (Entity en : list) {
            if (lastAttacked.contains(en)) continue;
            target = en;
        }
        if (target == null) return;
        lastAttacked.add(target);
        switchDelay.reset();
    }

    public ArrayList<Entity> getTargets() {
        ArrayList<Entity> targets = new ArrayList<>();
        for (Entity en : mc.theWorld.loadedEntityList) {
            if (!isValid(en)) continue;
            if (!positions.containsKey(en)) positions.put(en, new ArrayList<>());
            targets.add(en);
        }
        return targets;
    }

    public boolean isValid(Entity ent) {
        for (Friend friend : Client.INSTANCE.getFriendManager().getFriendList()) {
            if (friend.getName().contains(ent.getName())) return false;
        }
        if (ent == null) return false;
        if (ent == mc.thePlayer) return false;
        if (ent.isInvisibleToPlayer(mc.thePlayer)) return false;
        if (mc.thePlayer.getDistanceToEntity(ent) > rangeValue.getObject()) return false;
        if (!mc.thePlayer.canEntityBeSeen(ent)) return false;
        if (!mc.theWorld.loadedEntityList.contains(ent)) return false;
        if (ent.getName().contains("[NPC]")) return false;
        if (ent.getName().equalsIgnoreCase(mc.thePlayer.getName())) return false;
        if (ent instanceof EntityArmorStand) return false;
        if (teamsValue.getObject() && ent.getDisplayName().getFormattedText().startsWith("§" + mc.thePlayer.getDisplayName().getFormattedText().charAt(1)))
            return false;
        if (ent instanceof EntityLivingBase && ((EntityLivingBase) ent).deathTime != 0) return false;
        return ent instanceof EntityPlayer;
    }

}