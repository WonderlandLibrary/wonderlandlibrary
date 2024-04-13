package com.canon.majik.impl.modules.impl.combat;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.eventBus.Priority;
import com.canon.majik.api.event.events.PacketEvent;
import com.canon.majik.api.event.events.PlayerUpdateEvent;
import com.canon.majik.api.event.events.Render3DEvent;
import com.canon.majik.api.mixin.impl.IEntityLivingBase;
import com.canon.majik.api.thread.ThreadedHandler;
import com.canon.majik.api.utils.autocrystal.CrystalExplosion;
import com.canon.majik.api.utils.autocrystal.CrystalUtil;
import com.canon.majik.api.utils.autocrystal.PredictionUtil;
import com.canon.majik.api.utils.autocrystal.RaytraceUtil;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.api.utils.client.Timer;
import com.canon.majik.api.utils.autocrystal.AutoCrystalThread;
import com.canon.majik.api.utils.autocrystal.CrystalPosition;
import com.canon.majik.api.utils.player.BlockUtil;
import com.canon.majik.api.utils.player.InventoryUtil;
import com.canon.majik.api.utils.player.rotation.Result;
import com.canon.majik.api.utils.player.rotation.Rotation;
import com.canon.majik.api.utils.player.rotation.RotationUtil;
import com.canon.majik.api.utils.render.RenderUtils;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.TreeMap;

public class AutoCrystal extends Module {

    private final NumberSetting scanRadius = setting("Scan radius", 5.0f, 0.0f, 15.0f);
    private final NumberSetting enemyRadius = setting("Enemy radius", 10.0f, 0.0f, 15.0f);
    private final NumberSetting placeRadius = setting("Place radius", 5.0f, 0.0f, 15.0f);
    private final NumberSetting placeWallRadius = setting("Place wall radius", 3.5f, 0.0f, 15.0f);
    private final NumberSetting breakRadius = setting("Break radius", 5.0f, 0.0f, 15.0f);
    private final NumberSetting breakWallRadius = setting("Break wall radius", 5.0f, 0.0f, 15.0f);

    private final NumberSetting placeTimeout = setting("Place timeout", 50, 0, 1000);
    private final NumberSetting breakTimeout = setting("Break timeout", 50, 0, 1000);
    private final BooleanSetting inhibit = setting("Inhibit", true);
    private final BooleanSetting damageTick = setting("Damage Tick", false);
    private final BooleanSetting subTick = setting("Sub tick", false);

    private final BooleanSetting netPositive = setting("Net positive", true);
    private final BooleanSetting onePointThirteen = setting("1.13+", true);
    private final NumberSetting minimumDamage = setting("Minimum damage", 20.0f, 0.0f, 40.0f);
    private final NumberSetting maximumSelfDamage = setting("Maximum self damage", 25.0f, 0.0f, 40.0f);
    private final BooleanSetting antiSuicide = setting("Anti suicide", true);
    private final NumberSetting antiSuicideSafety = setting("Anti suicide safety", 2.0f, 0.0f, 15.0f);
    private final NumberSetting predict = setting("Extrapolate", 2.0f, 0.0f, 6.0f);

    private final BooleanSetting rotations = setting("Rotations", true);
    private final NumberSetting maxDelta = setting("Max delta", 45.0f, 1.0f, 180.0f);
    private final BooleanSetting mine = setting("Mine", true);
    private final BooleanSetting swing = setting("Swing", true);
    private final BooleanSetting cSwitch = setting("Switch", true);
    private final ColorSetting color = setting("Color", new TColor(255,0,0,100));

    private Rotation spoofed = new Rotation(0, 0);
    private ArrayList<BlockPos> placeable = new ArrayList<>();
    private ArrayList<EntityEnderCrystal> attackedCrystals = new ArrayList<>();
    private final Timer placeTimer = new Timer(), breakTimer = new Timer();
    private BlockPos renderPos;

    public AutoCrystal(String name, Category category) {
        super(name,category);
        attackedCrystals.clear();
    }

    @EventListener(getPriority = Priority.HIGHEST)
    public void onMotionUpdate(PlayerUpdateEvent event) {
        ThreadedHandler.queue(() -> placeable = AutoCrystalThread.start(scanRadius.getValue().floatValue(), onePointThirteen.getValue()));

        EntityPlayer enemy = CrystalUtil.getEntityPlayer(enemyRadius.getValue().floatValue());
        if (enemy == null) {
            return;
        }

        int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }

        boolean offhand = false;
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        } else if (crystalSlot == -1) {
            return;
        }

        if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
            if (cSwitch.getValue()) {
                InventoryUtil.switchToSlot(crystalSlot);
            }
            return;
        }

        AxisAlignedBB old = enemy.getEntityBoundingBox();
        setPredict(enemy);

        boolean sent = false;
        CrystalPosition crystalPosition = getBestPosition(getFilteredPositions(getPositions(enemy)));
        if (placeTimer.over(placeTimeout.getValue().floatValue())) {
            if (crystalPosition != null) {
                Result result = placeCrystal(event, crystalPosition);
                if (result.equals(Result.SUCCESS)) {
                    renderPos = crystalPosition.getPos();
                    placeTimer.sync();
                    sent = true;
                }
            }
        }

        CrystalExplosion crystalExplosion = getBestCrystal(getFilteredCrystals(getCrystals(enemy)));
        if (breakTimer.over(breakTimeout.getValue().floatValue()) && (!sent || subTick.getValue())) {
            if (crystalExplosion != null) {
                if(inhibit.getValue()){
                    attackedCrystals.add(crystalExplosion.getEntity());
                }
                Result result = breakCrystal(event, crystalExplosion, inhibit.getValue());
                if (result.equals(Result.SUCCESS)) {
                    renderPos = crystalExplosion.getEntity().getPosition().down();
                    breakTimer.sync();
                    sent = true;
                }
            }
        }

        if (rotations.getValue()) {
            rotateOnly(event, crystalPosition == null ? crystalExplosion == null ? null : crystalExplosion.getRaytrace() : crystalPosition.getRaytrace(), crystalPosition == null ? crystalExplosion == null ? null : crystalExplosion.getEntity().getPosition().add(0, -0.5f, 0) : crystalPosition.getPos().offset(EnumFacing.UP));
        }

        enemy.setEntityBoundingBox(old);
    }

    private void setPredict(EntityPlayer entityPlayer) {
        AxisAlignedBB bb = PredictionUtil.predictedTarget(entityPlayer, Math.round(predict.getValue().floatValue()));
        entityPlayer.setEntityBoundingBox(bb);
    }

    private void rotateOnly(PlayerUpdateEvent event, Vec3d raytrace, BlockPos safe) {
        spoofed = RotationUtil.facePos(raytrace == null ? new Vec3d(safe.getX(), safe.getY(), safe.getZ()) : raytrace, event, spoofed, maxDelta.getValue().floatValue());
    }

    private Result breakCrystal(PlayerUpdateEvent event, CrystalExplosion crystalExplosion, boolean inhibit) {
        Vec3d raytrace = crystalExplosion.getRaytrace();

        if (rotations.getValue()) {
            spoofed = RotationUtil.facePos(raytrace == null ? new Vec3d(crystalExplosion.getEntity().posX, crystalExplosion.getEntity().posY, crystalExplosion.getEntity().posZ) : raytrace, event, spoofed, maxDelta.getValue().floatValue());
            if (spoofed.getResult().equals(Result.AWAIT_ROTATIONS)) {
                return Result.AWAIT_ROTATIONS;
            }
        }

        EnumHand enumHand = InventoryUtil.isHeld(EnumHand.MAIN_HAND, Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : InventoryUtil.isHeld(EnumHand.OFF_HAND, Items.END_CRYSTAL) ? EnumHand.OFF_HAND : null;
        if (enumHand == null) {
            return Result.FAILURE;
        }
        if(inhibit) {
            if(attackedCrystals.contains(crystalExplosion.getEntity())) {
                sendBreak(crystalExplosion.getEntity(), enumHand);
            }
        }else{
            sendBreak(crystalExplosion.getEntity(), enumHand);
        }
        if(swing.getValue()){
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }

        return Result.SUCCESS;
    }


    private CrystalExplosion getBestCrystal(ArrayList<CrystalExplosion> posses) {
        TreeMap<Float, CrystalExplosion> posTreeMap = new TreeMap<>();
        for (CrystalExplosion crystalPosition : posses) {
            posTreeMap.put(crystalPosition.getDamage(), crystalPosition);
        }
        return posTreeMap.isEmpty() ? null : posTreeMap.lastEntry().getValue();
    }


    private ArrayList<CrystalExplosion> getFilteredCrystals(ArrayList<CrystalExplosion> posses) {
        ArrayList<CrystalExplosion> crystalPositions = new ArrayList<>();
        for (CrystalExplosion crystalPosition : posses) {
            Vec3d raytrace = RaytraceUtil.getRaytraceSides(crystalPosition.getEntity().getPosition());
            if (raytrace == null && crystalPosition.getDistance() > breakWallRadius.getValue().floatValue()) {
                continue;
            }
            crystalPosition.setRaytrace(raytrace);
            crystalPositions.add(crystalPosition);
        }
        return crystalPositions;
    }

    private ArrayList<CrystalExplosion> getCrystals(EntityPlayer entityPlayer) {
        ArrayList<CrystalExplosion> crystalExplosions = new ArrayList<>();
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            float distance = mc.player.getDistance(entity);
            if (distance > breakRadius.getValue().floatValue()) {
                continue;
            }
            float selfDamage = BlockUtil.calculateEntityDamage((EntityEnderCrystal) entity, mc.player);
            float damage = BlockUtil.calculateEntityDamage((EntityEnderCrystal) entity, entityPlayer);

            if (netPositive.getValue() && damage - selfDamage < 0) {
                continue;
            }
            if (antiSuicide.getValue() && selfDamage > CrystalUtil.getHealth(mc.player) - antiSuicideSafety.getValue().floatValue()) {
                continue;
            }
            if (selfDamage > CrystalUtil.getHealth(mc.player) * (maximumSelfDamage.getValue().floatValue() / 100.0f)) {
                continue;
            }
            if (damage < CrystalUtil.getHealth(entityPlayer) * (minimumDamage.getValue().floatValue() / 100.0f)) {
                continue;
            }

            crystalExplosions.add(new CrystalExplosion((EntityEnderCrystal) entity, damage, distance, null));
        }
        return crystalExplosions;
    }

    private Result placeCrystal(PlayerUpdateEvent event, CrystalPosition crystalPosition) {
        Vec3d raytrace = crystalPosition.getRaytrace();

        BlockPos pos = crystalPosition.getPos().offset(EnumFacing.UP);
        if (rotations.getValue() && event != null) {
            spoofed = RotationUtil.facePos(raytrace == null ? new Vec3d(pos.getX(), pos.getY(), pos.getZ()) : raytrace, event, spoofed, maxDelta.getValue().floatValue());
            if (spoofed.getResult().equals(Result.AWAIT_ROTATIONS)) {
                return Result.AWAIT_ROTATIONS;
            }
        }

        EnumHand enumHand = InventoryUtil.isHeld(EnumHand.MAIN_HAND, Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : InventoryUtil.isHeld(EnumHand.OFF_HAND, Items.END_CRYSTAL) ? EnumHand.OFF_HAND : null;
        if (enumHand == null) {
            return Result.FAILURE;
        }

        sendPlace(crystalPosition.getPos(), enumHand, raytrace);


        return Result.SUCCESS;
    }

    private CrystalPosition getBestPosition(ArrayList<CrystalPosition> posses) {
        TreeMap<Float, CrystalPosition> posTreeMap = new TreeMap<>();
        for (CrystalPosition crystalPosition : posses) {
            posTreeMap.put(crystalPosition.getDamage(), crystalPosition);
        }
        return posTreeMap.isEmpty() ? null : posTreeMap.lastEntry().getValue();
    }

    private ArrayList<CrystalPosition> getFilteredPositions(ArrayList<CrystalPosition> posses) {
        ArrayList<CrystalPosition> crystalPositions = new ArrayList<>();
        for (CrystalPosition crystalPosition : posses) {
            Vec3d raytrace = RaytraceUtil.getRaytraceSides(crystalPosition.getPos());
            if (raytrace == null && crystalPosition.getDistance() > placeWallRadius.getValue().floatValue()) {
                continue;
            }
            crystalPosition.setRaytrace(raytrace);
            crystalPositions.add(crystalPosition);
        }
        return crystalPositions;
    }


    private ArrayList<CrystalPosition> getPositions(EntityPlayer entityPlayer) {
        ArrayList<CrystalPosition> posses = new ArrayList<>();
        boolean resistant = damageTick.getValue() && entityPlayer.hurtResistantTime > (float) entityPlayer.maxHurtResistantTime / 2.0f;

        for (BlockPos pos : placeable) {
            float distance = (float) Math.sqrt(mc.player.getDistanceSq(BlockUtil.center()));
            if (distance > placeRadius.getValue().floatValue()) {
                continue;
            }

            float selfDamage = BlockUtil.calculatePosDamage(pos, mc.player);
            float damage = BlockUtil.calculatePosDamage(pos, entityPlayer);

            if (resistant && damage <= ((IEntityLivingBase) entityPlayer).getLastDamage()) {
                continue;
            }

            if (netPositive.getValue() && damage - selfDamage < 0) {
                continue;
            }
            if (antiSuicide.getValue() && selfDamage > CrystalUtil.getHealth(mc.player) - antiSuicideSafety.getValue().floatValue()) {
                continue;
            }
            if (selfDamage > CrystalUtil.getHealth(mc.player) * (maximumSelfDamage.getValue().floatValue() / 100.0f)) {
                continue;
            }
            if (damage < CrystalUtil.getHealth(entityPlayer) * (minimumDamage.getValue().floatValue() / 100.0f)) {
                continue;
            }

            posses.add(new CrystalPosition(pos, damage, distance, null));
        }
        return posses;
    }

    @EventListener
    public void onRender3D(Render3DEvent event) {
        if (renderPos != null) {
            RenderUtils.renderBox(new AxisAlignedBB(renderPos), true, true, color.getValue());
        }
    }

    @EventListener
    public void onPacketSend(PacketEvent.Send event) {
        if (mine.getValue()) {
            if (event.getPacket() instanceof CPacketPlayerDigging) {
                CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
                if (packet.getAction().equals(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)) {
                    placeCrystal(null, new CrystalPosition(packet.getPosition(), 10.0f, 0.0f, RaytraceUtil.getRaytraceSides(packet.getPosition())));
                }
            }
        }
    }

    public static void send(Packet<?> packet) {
        if (mc.getConnection() != null) {
            mc.getConnection().getNetworkManager().channel().writeAndFlush(packet);
        }
    }

    public static void sendPlace(BlockPos pos, EnumHand enumHand, Vec3d raytrace) {
        AxisAlignedBB bb = new AxisAlignedBB(pos);
        send(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, enumHand, (float) (raytrace == null ? 0.5f : (raytrace.x - bb.minX)), (float) (raytrace == null ? 0.5f : (raytrace.y - bb.minY)), (float) (raytrace == null ? 0.5f : (raytrace.z - bb.minZ))));
        send(new CPacketAnimation(enumHand));
    }

    public static void sendBreak(Entity entity, EnumHand enumHand) {
        send(new CPacketUseEntity(entity));
        send(new CPacketAnimation(enumHand));
    }
}
