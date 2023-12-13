package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event3D;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.event.impl.EventWorldChange;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.impl.combat.sigmertpaura.TPAuraPathFinder;
import me.nyan.flush.module.impl.combat.sigmertpaura.TPAuraVec3;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import me.nyan.flush.notifications.Notification;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.player.PlayerUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SigmaTPAura extends Module {
    public SigmaTPAura() {
        super("SigmaTPAura", Category.COMBAT);
    }

    private final Timer cpsTimer = new Timer();
    public List<EntityLivingBase> targets = new CopyOnWriteArrayList<>();

    private final NumberSetting cps = new NumberSetting("CPS", this, 3, 0.2, 20, 0.1),
            reach = new NumberSetting("Reach", this, 50, 6, 200),
            dashDistance = new NumberSetting("Dash Distance", this, 5, 1, 20),
            maxTargets = new NumberSetting("Max Targets", this, 2, 1, 10);
    public final BooleanSetting targetEsp = new BooleanSetting("Target ESP", this, true);
    private final BooleanSetting players = new BooleanSetting("Players", this, true),
            creatures = new BooleanSetting("Creatures", this, false),
            villagers = new BooleanSetting("Villagers", this, false),
            invisibles = new BooleanSetting("Invisibles", this, false),
            ignoreTeam = new BooleanSetting("Ignore Team", this, false),
            autoDisable = new BooleanSetting("Auto Disable",this,true);

    @Override
    public void onEnable() {
        targets.clear();
        super.onEnable();
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (e.getState() == EventMotion.State.PRE) {
            targets = getTargets();

            if (targets.size() > 0 && cpsTimer.hasTimeElapsed((long) (1000 / (cps.getValueFloat())), true)) {
                for (int i = 0; i < (Math.min(targets.size(), maxTargets.getValueInt())); i++) {
                    EntityLivingBase target = targets.get(i);
                    TPAuraVec3 topFrom = new TPAuraVec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    TPAuraVec3 to = new TPAuraVec3(target.posX, target.posY, target.posZ);
                    ArrayList<TPAuraVec3> path = computePath(topFrom, to);
                    for (TPAuraVec3 pathElm : path)
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));

                    mc.thePlayer.swingItem();

                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                    Collections.reverse(path);

                    for (TPAuraVec3 pathElm : path)
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                }
                cpsTimer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onRender3D(Event3D e) {
        if (targetEsp.getValue()) {
            for (EntityLivingBase target : targets) {
                double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * e.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * e.getPartialTicks() - mc.getRenderManager().renderPosY;
                double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * e.getPartialTicks() - mc.getRenderManager().renderPosZ;

                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);

                drawESPBox(target, x, y, z, target.hurtTime > 0 && target.hurtTime < 14 ? 0x5000FF00 : 0x50FF0000);
                drawESPBoxOutline(target, x, y, z, target.hurtTime > 0 && target.hurtTime < 14 ? 0xFF00FF00 : 0xFFFF0000);

                GlStateManager.enableTexture2D();
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.color(1, 1, 1, 1);
            }
        }
    }

    private void drawESPBoxOutline(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        GL11.glLineWidth(1F);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        RenderUtils.drawBoundingBoxOutline(axisAlignedBB, color);
    }

    private void drawESPBox(Entity entity, double x, double y, double z, int color) {
        AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBoundingBox.minX - entity.posX + x,
                entityBoundingBox.minY - entity.posY + y + 0.05D,
                entityBoundingBox.minZ - entity.posZ + z,
                entityBoundingBox.maxX - entity.posX + x,
                entityBoundingBox.maxY - entity.posY + y + 0.15D,
                entityBoundingBox.maxZ - entity.posZ + z
        );

        RenderUtils.fillBoundingBox(axisAlignedBB, color);
    }

    @SubscribeEvent
    public void onWorldChange(EventWorldChange e) {
        if (autoDisable.getValue()) {
            disable();
            flush.getNotificationManager().show(Notification.Type.INFO, name, name + " was " + EnumChatFormatting.RED + "disabled " + EnumChatFormatting.RESET + "because you teleported.");
        }
    }

    private ArrayList<TPAuraVec3> computePath(TPAuraVec3 topFrom, TPAuraVec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc())))
            topFrom = topFrom.addVector(0, 1, 0);

        TPAuraPathFinder pathfinder = new TPAuraPathFinder(topFrom, to);
        pathfinder.compute();

        int i = 0;

        TPAuraVec3 lastLoc = null;
        TPAuraVec3 lastDashLoc = null;
        ArrayList<TPAuraVec3> path = new ArrayList<>();
        ArrayList<TPAuraVec3> pathFinderPath = pathfinder.getPath();

        for (TPAuraVec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null)
                    path.add(lastLoc.addVector(0.5, 0, 0.5));

                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                double dashDistance = this.dashDistance.getValueFloat();

                if (pathElm.squareDistanceTo(lastDashLoc) > dashDistance * dashDistance)
                    canContinue = false;
                else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());

                    cordsLoop:
                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!TPAuraPathFinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }

                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }

            lastLoc = pathElm;
            i++;
        }

        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = new BlockPos(pos.getX(), pos.getY(), pos.getZ()).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine ||
                block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign ||
                block == Blocks.standing_sign;
    }

    private boolean isValid(EntityLivingBase entity) {
        return PlayerUtils.isValid(entity, players.getValue(), creatures.getValue(), villagers.getValue(), invisibles.getValue(),
                                   ignoreTeam.getValue()) && entity.getDistanceToEntity(mc.thePlayer) <= reach.getValue();
    }

    private List<EntityLivingBase> getTargets() {
        List<EntityLivingBase> targets = new ArrayList<>();

        for (Entity e : mc.theWorld.getLoadedEntityList()) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) e;

                if (entity.posY < mc.thePlayer.posY - 6 || entity.posY > mc.thePlayer.posY + 7)
                    continue;

                if (isValid(entity))
                    targets.add(entity);
            }
        }

        targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) * 1000 - o2.getDistanceToEntity(mc.thePlayer) * 1000));
        return targets;
    }
}