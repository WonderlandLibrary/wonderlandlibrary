package club.dortware.client.module.impl.combat;

import club.dortware.client.Client;
import club.dortware.client.manager.impl.PropertyManager;
import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.property.impl.BooleanProperty;
import club.dortware.client.property.impl.DoubleProperty;
import club.dortware.client.property.impl.StringProperty;
import club.dortware.client.event.impl.PacketEvent;
import club.dortware.client.event.impl.UpdateEvent;
import club.dortware.client.module.Module;
import club.dortware.client.util.impl.networking.PacketUtil;
import club.dortware.client.util.impl.combat.AimUtil;
import club.dortware.client.util.impl.combat.FightUtil;
import club.dortware.client.util.impl.combat.extras.Rotation;
import club.dortware.client.util.impl.time.Stopwatch;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import java.util.List;

@ModuleData(name = "Aura", defaultKeyBind = Keyboard.KEY_R, description = "Attacks nearby entities", category = ModuleCategory.COMBAT)
public class KillAura extends Module {

    private Stopwatch attackTimer = new Stopwatch();
    private boolean canBlock;
    private boolean blockedBefore;

    @Override
    public void setup() {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        propertyManager.add(new StringProperty<>("Mode", this, "Single", "Switch", "Multi", "Multi2"));
        propertyManager.add(new DoubleProperty<>("Attack Speed", this, 10, 1, 20, true));
        propertyManager.add(new DoubleProperty<>("Range", this, 4.2, 1, 6));
        propertyManager.add(new BooleanProperty<>("F5 Rotations", this, true));
        propertyManager.add(new BooleanProperty<>("No Rotations", this, false));
        propertyManager.add(new BooleanProperty<>("Auto Block", this, true));
        propertyManager.add(new BooleanProperty<>("Invisibles", this, false));
        propertyManager.add(new BooleanProperty<>("Lock View", this, false));
        propertyManager.add(new BooleanProperty<>("Players", this, true));
        propertyManager.add(new BooleanProperty<>("Animals", this, false));
        propertyManager.add(new BooleanProperty<>("Walls", this, true));
        propertyManager.add(new BooleanProperty<>("Mobs", this, false));
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        PropertyManager propertyManager = Client.INSTANCE.getPropertyManager();
        String mode = (String) propertyManager.getProperty(this, "Mode").getValue();
        Double attackSpeed = (Double) propertyManager.getProperty(this, "Attack Speed").getValue();
        double range = (Double) propertyManager.getProperty(this, "Range").getValue();
        boolean doRotations = (Boolean) propertyManager.getProperty(this, "F5 Rotations").getValue();
        boolean noRotations = (Boolean) propertyManager.getProperty(this, "No Rotations").getValue();
        boolean autoBlock = (Boolean) propertyManager.getProperty(this, "Auto Block").getValue();
        boolean invisibles = (Boolean) propertyManager.getProperty(this, "Invisibles").getValue();
        boolean lockView = (Boolean) propertyManager.getProperty(this, "Lock View").getValue();
        boolean players = (Boolean) propertyManager.getProperty(this, "Players").getValue();
        boolean animals = (Boolean) propertyManager.getProperty(this, "Animals").getValue();
        boolean walls = (Boolean) propertyManager.getProperty(this, "Walls").getValue();
        boolean mobs = (Boolean) propertyManager.getProperty(this, "Mobs").getValue();
        switch (mode) {
            case "Single": {
                EntityLivingBase singleEntity = FightUtil.getSingleTarget(range, players, animals, walls, mobs, invisibles);
                ItemStack heldItem = mc.thePlayer.getHeldItem();
                canBlock = FightUtil.isNearEntity(range, players, animals, walls, mobs, invisibles)
                        && autoBlock
                        && heldItem != null
                        && heldItem.getItem() instanceof ItemSword;
                mc.thePlayer.setBlocking(canBlock);
                if (canBlock) {
                    if (!event.isPre()) {
//                        mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 144);
//                        PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(new BlockPos(RandomUtils.nextFloat(Float.MIN_VALUE, Float.MAX_VALUE), RandomUtils.nextFloat(Float.MIN_VALUE, Float.MAX_VALUE), RandomUtils.nextFloat(Float.MIN_VALUE, Float.MAX_VALUE)), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    } else {
//                        PacketUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1.25F, -1, -1), EnumFacing.DOWN));
                    }
                }
                if (singleEntity == null)
                    return;
                Rotation rotation = AimUtil.getRotationsRandom(singleEntity);
                if (!noRotations) {
                    if (lockView) {
                        mc.thePlayer.rotationYaw = rotation.getRotationYaw();
                        mc.thePlayer.rotationPitch = rotation.getRotationPitch();
                    }
                    event.setRotationYaw(rotation.getRotationYaw());
                    event.setRotationPitch(rotation.getRotationPitch());
                }
                if (doRotations) {
                    mc.thePlayer.renderPitchHead = rotation.getRotationPitch();
                    mc.thePlayer.renderYawOffset = rotation.getRotationYaw();
                    mc.thePlayer.renderYawHead = rotation.getRotationYaw();
                }
                //NEVER ATTACK ON POST OR YOU'LL GET AUTOBANNED ON ALMOST ANY AC.
                if (attackTimer.timeElapsed(1000L / attackSpeed.longValue()) && event.isPre()) {
                    mc.thePlayer.swingItem();
                    PacketUtil.sendPacketNoEvent(new C02PacketUseEntity(singleEntity, C02PacketUseEntity.Action.ATTACK));
                    attackTimer.resetTime();
                }
                blockedBefore = true;
                break;
            }
            case "Switch":
                break;
            case "Multi": {
                List<EntityLivingBase> multiEntities = FightUtil.getMultipleTargets(12, range, players, animals, walls, mobs);
                ItemStack heldItem = mc.thePlayer.getHeldItem();
                canBlock = FightUtil.isNearEntity(range, players, animals, walls, mobs, invisibles)
                        && autoBlock
                        && heldItem != null
                        && heldItem.getItem() instanceof ItemSword;
                mc.thePlayer.setBlocking(canBlock);
                if (canBlock) {
                    if (!event.isPre()) {
                        mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), 144);
                        PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(new BlockPos(RandomUtils.nextFloat(Float.MIN_VALUE, Float.MAX_VALUE), RandomUtils.nextFloat(Float.MIN_VALUE, Float.MAX_VALUE), RandomUtils.nextFloat(Float.MIN_VALUE, Float.MAX_VALUE)), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    } else {
                        PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1.25F, -1, -1), EnumFacing.DOWN));
                    }
                }
                if (multiEntities.isEmpty())
                    return;
                Rotation rotation = AimUtil.getRotationsRandom(multiEntities.get(0));
                if (!noRotations) {
                    if (lockView) {
                        mc.thePlayer.rotationYaw = rotation.getRotationYaw();
                        mc.thePlayer.rotationPitch = rotation.getRotationPitch();
                    }
                    event.setRotationYaw(rotation.getRotationYaw());
                    event.setRotationPitch(rotation.getRotationPitch());
                }
                if (doRotations) {
                    mc.thePlayer.renderPitchHead = rotation.getRotationPitch();
                    mc.thePlayer.renderYawOffset = rotation.getRotationYaw();
                    mc.thePlayer.renderYawHead = rotation.getRotationYaw();
                }
                if (attackTimer.timeElapsed(1000L / attackSpeed.longValue()) && event.isPre()) {
                    mc.thePlayer.swingItem();
                    for (EntityLivingBase entityLivingBase : multiEntities) {
                        PacketUtil.sendPacketNoEvent(new C02PacketUseEntity(entityLivingBase, C02PacketUseEntity.Action.ATTACK));
                    }
                    attackTimer.resetTime();
                }
                break;
            }
            case "Multi2":
                break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent packetEvent) {
        Packet packet = packetEvent.getPacket();

        if (packet instanceof C07PacketPlayerDigging && !canBlock && blockedBefore) {
            packetEvent.setPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1.3F, -1, -1), EnumFacing.DOWN));
            blockedBefore = false;
        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
        PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1.3F, -1, -1), EnumFacing.DOWN));
    }
}
