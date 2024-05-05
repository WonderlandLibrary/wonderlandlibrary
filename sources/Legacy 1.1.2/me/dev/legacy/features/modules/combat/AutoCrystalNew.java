package me.dev.legacy.features.modules.combat;

import me.dev.legacy.Legacy;
import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.event.events.Render3DEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.modules.client.ClickGui;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.mixin.mixins.accessors.AccessorCPacketUseEntity;
import me.dev.legacy.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutoCrystalNew extends Module
{
    public enum Settings
    {
        Place,
        Break,
        Render,
        Misc;
    }

    public enum InfoMode
    {
        Target,
        Damage,
        Both;
    }

    public enum Rotate
    {
        OFF,
        Place,
        Break,
        All;
    }

    public enum Raytrace
    {
        None,
        Place,
        Break,
        Both;
    }


    private final Setting<Settings> setting  = this.register(new Setting("Settings", Settings.Place));
    private final Setting<Integer> placeDelay = this.register(new Setting("Delay", 0, 0, 200, v -> this.setting.getValue() == Settings.Place));
    private final Setting<Float> placeRange = this.register(new Setting("Range", 6.0f, 0.0f, 6.0f, v -> this.setting.getValue() == Settings.Place));
    private final Setting<Integer> breakDelay = this.register(new Setting("BDelay", 0, 0, 200, v -> this.setting.getValue() == Settings.Break));
    public Setting<Float> breakRange = this.register(new Setting("BRange", 6.0f, 0.0f, 6.0f, v -> this.setting.getValue() == Settings.Break));
    private final Setting<Boolean> cancelcrystal = this.register(new Setting("SetDead", false, v -> this.setting.getValue() == Settings.Break));
    private final Setting<InfoMode> infomode = this.register(new Setting("Info", InfoMode.Target, v -> this.setting.getValue() == Settings.Render));
    private final Setting<Boolean> offhandS = this.register(new Setting("OffhandSwing", true, v -> this.setting.getValue() == Settings.Render));
    public Setting<Boolean> render = this.register(new Setting("Render", true, v -> this.setting.getValue() == Settings.Render));
    private final Setting<Integer> red = this.register(new Setting("Red", 80, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    private final Setting<Integer> green = this.register(new Setting("Green", 120, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    private final Setting<Integer> blue = this.register(new Setting("Blue", 255, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    private final Setting<Integer> alpha = this.register(new Setting("Alpha", 120, 0,255, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    public Setting<Boolean> colorSync = this.register(new Setting("ColorSync", false, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    public Setting<Boolean> box = this.register(new Setting("Box", true, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    private final Setting<Integer> boxAlpha = this.register(new Setting("BoxAlpha", 30, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.box.getValue()));
    public Setting<Boolean> outline = this.register(new Setting("Outline", true, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    private final Setting<Float> lineWidth = this.register(new Setting("LineWidth", 0.1f, 0.1f, 5.0f, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.outline.getValue()));
    public Setting<Boolean> text = this.register(new Setting("DamageText", true, v -> this.setting.getValue() == Settings.Render && this.render.getValue()));
    public Setting<Boolean> customOutline = this.register(new Setting("CustomLine", false, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.outline.getValue()));
    private final Setting<Integer> cRed = this.register(new Setting("OL-Red", 255, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> cGreen = this.register(new Setting("OL-Green", 255, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> cBlue = this.register(new Setting("OL-Blue", 255, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Integer> cAlpha = this.register(new Setting("OL-Alpha", 255, 0, 255, v -> this.setting.getValue() == Settings.Render && this.render.getValue() && this.customOutline.getValue() && this.outline.getValue()));
    private final Setting<Float> range = this.register(new Setting("TargetRange", 9.5f, 0.0f, 16.0f, v -> this.setting.getValue() == Settings.Misc));
    public Setting<Rotate> rotate = this.register(new Setting("Rotate", Rotate.OFF, v -> this.setting.getValue() == Settings.Misc));
    public Setting<Integer> rotations = this.register(new Setting("Spoofs", 1, 1, 20, v -> this.setting.getValue() == Settings.Misc && this.rotate.getValue() != Rotate.OFF));
    public Setting<Boolean> rotateFirst = this.register(new Setting("FirstRotation", false, v -> this.setting.getValue() == Settings.Misc && this.rotate.getValue() != Rotate.OFF));
    public Setting<Raytrace> raytrace = this.register(new Setting("Raytrace", Raytrace.None, v -> this.setting.getValue() == Settings.Misc));
    public Setting<Float> placetrace = this.register(new Setting("Placetrace", 5.5f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.Misc && this.raytrace.getValue() != Raytrace.None && this.raytrace.getValue() != Raytrace.Break));
    public Setting<Float> breaktrace = this.register(new Setting("Breaktrace", 5.5f, 0.0f, 10.0f, v -> this.setting.getValue() == Settings.Misc && this.raytrace.getValue() != Raytrace.None && this.raytrace.getValue() != Raytrace.Place));
    private final Setting<Float> breakWallRange = this.register(new Setting("WallRange", 4.5f, 0.0f, 6.0f, v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> minDamage = this.register(new Setting("MinDamage", 0.7f, 0.0f, 30.0f, v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> maxSelf = this.register(new Setting("MaxSelf", 18.5f, 0.0f, 36.0f, v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> lethalMult = this.register(new Setting("LethalMult", 0.0f, 0.0f, 6.0f, v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> armorScale= this.register(new Setting("ArmorBreak",100.0f, 0.0f, 100.0f, v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Boolean> second = this.register(new Setting("Second", false, v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Boolean> autoSwitch = this.register(new Setting("AutoSwitch", false, v -> this.setting.getValue() == Settings.Misc));
    private final List<BlockPos> placedList;
    private final Timer breakTimer;
    private final Timer placeTimer;
    private final Timer renderTimer;
    private int rotationPacketsSpoofed;
    public static EntityPlayer currentTarget;
    private BlockPos renderPos;
    private double renderDamage;
    private BlockPos placePos;
    private boolean offHand;
    public boolean rotating;
    private float pitch;
    private float yaw;
    private boolean offhand;

    public AutoCrystalNew() {
        super("AutoCrystalNew", "Based crystal aura.", Category.COMBAT, true, false, false);
        this.placedList = new ArrayList<BlockPos>();
        this.breakTimer = new Timer();
        this.placeTimer = new Timer();
        this.renderTimer = new Timer();
        this.rotationPacketsSpoofed = 0;
        this.renderPos = null;
        this.renderDamage = 0.0;
        this.placePos = null;
        this.offHand = false;
        this.rotating = false;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    @Override
    public void onToggle() {
        this.placedList.clear();
        this.breakTimer.reset();
        this.placeTimer.reset();
        this.renderTimer.reset();
        AutoCrystalNew.currentTarget = null;
        this.renderPos = null;
        this.offhand = false;
        this.rotating = false;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.renderTimer.passedMs(500L)) {
            this.placedList.clear();
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.offhand = ((AutoCrystalNew.mc.player.inventory.offHandInventory.get(0)).getItem() == Items.END_CRYSTAL);
        AutoCrystalNew.currentTarget = EntityUtil.getClosestPlayer(this.range.getValue());
        if (AutoCrystalNew.currentTarget == null) {
            return;
        }
        this.doPlace();
        if (event.phase == TickEvent.Phase.START) {
            this.doBreak();
        }
    }

    private void doBreak() {
        Entity crystal = null;
        double maxDamage = 0.5;
        for (int size = AutoCrystalNew.mc.world.loadedEntityList.size(), i = 0; i < size; ++i) {
            final Entity entity = AutoCrystalNew.mc.world.loadedEntityList.get(i);
            if (entity instanceof EntityEnderCrystal && AutoCrystalNew.mc.player.getDistance(entity) < (AutoCrystalNew.mc.player.canEntityBeSeen(entity) ? this.breakRange.getValue() : this.breakWallRange.getValue())) {
                final float targetDamage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase) AutoCrystalNew.currentTarget);
                if (targetDamage > this.minDamage.getValue() || targetDamage * this.lethalMult.getValue() > AutoCrystalNew.currentTarget.getHealth() + AutoCrystalNew.currentTarget.getAbsorptionAmount() || ItemUtil.isArmorUnderPercent(AutoCrystalNew.currentTarget, this.armorScale.getValue())) {
                    final float selfDamage = EntityUtil.calculate(entity.posX, entity.posY, entity.posZ, (EntityLivingBase) AutoCrystalNew.mc.player);
                    if (selfDamage <= this.maxSelf.getValue() && selfDamage + 2.0f <= AutoCrystalNew.mc.player.getHealth() + AutoCrystalNew.mc.player.getAbsorptionAmount() && selfDamage < targetDamage) {
                        if (maxDamage <= targetDamage) {
                            maxDamage = targetDamage;
                            crystal = entity;
                        }
                    }
                }
            }
        }
        if (crystal != null && this.breakTimer.passedMs(this.breakDelay.getValue())) {
            AutoCrystalNew.mc.getConnection().sendPacket((Packet)new CPacketUseEntity(crystal));
            AutoCrystalNew.mc.player.swingArm(((boolean)this.offhandS.getValue()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            this.breakTimer.reset();
        }
    }

    private void doPlace() {
        BlockPos placePos = null;
        double maxDamage = 0.5;
        final List<BlockPos> sphere = BlockUtil.getSphereRealth(this.placeRange.getValue(), true);
        for (int size = sphere.size(), i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (BlockUtil.canPlaceCrystal(pos, this.second.getValue())) {
                final float targetDamage = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase) AutoCrystalNew.currentTarget);
                if (targetDamage > this.minDamage.getValue() || targetDamage * this.lethalMult.getValue() > AutoCrystalNew.currentTarget.getHealth() + AutoCrystalNew.currentTarget.getAbsorptionAmount() || ItemUtil.isArmorUnderPercent(AutoCrystalNew.currentTarget, this.armorScale.getValue())) {
                    final float selfDamage = EntityUtil.calculate(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, (EntityLivingBase) AutoCrystalNew.mc.player);
                    if (selfDamage <= this.maxSelf.getValue() && selfDamage + 2.0f <= AutoCrystalNew.mc.player.getHealth() + AutoCrystalNew.mc.player.getAbsorptionAmount() && selfDamage < targetDamage) {
                        if (maxDamage <= targetDamage) {
                            maxDamage = targetDamage;
                            placePos = pos;
                            this.renderPos = pos;
                            this.renderDamage = targetDamage;
                        }
                    }
                }
            }
        }
        boolean flag = false;
        if (!this.offhand && AutoCrystalNew.mc.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
            flag = true;
            if (!this.autoSwitch.getValue() || (AutoCrystalNew.mc.player.inventory.getCurrentItem().getItem() == Items.GOLDEN_APPLE && AutoCrystalNew.mc.player.isHandActive())) {
                return;
            }
        }
        if (placePos != null) {
            if (this.placeTimer.passedMs(this.placeDelay.getValue())) {
                if (flag) {
                    final int slot = ItemUtil.getItemFromHotbar(Items.END_CRYSTAL);
                    if (slot == -1) {
                        return;
                    }
                    AutoCrystalNew.mc.player.inventory.currentItem = slot;
                }
                this.placedList.add(placePos);
                AutoCrystalNew.mc.getConnection().sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                this.placeTimer.reset();
            }
            this.renderPos = placePos;
        }
        for (final BlockPos pos2 : BlockUtil.possiblePlacePositions(this.placeRange.getValue())) {
            if (!BlockUtil.rayTracePlaceCheck(pos2, (this.raytrace.getValue() == Raytrace.Place || this.raytrace.getValue() == Raytrace.Both) && AutoCrystal.mc.player.getDistanceSq(pos2) > MathUtil.square(this.placetrace.getValue()), 1.0f)) {
                continue;
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = event.getPacket();
            if (packet.getType() == 51 && this.placedList.contains(new BlockPos(packet.getX(), packet.getY() - 1.0, packet.getZ()))) {
                final AccessorCPacketUseEntity use = (AccessorCPacketUseEntity)new CPacketUseEntity();
                use.setEntityId(packet.getEntityID());
                use.setAction(CPacketUseEntity.Action.ATTACK);
                AutoCrystalNew.mc.getConnection().sendPacket((Packet)use);
                AutoCrystalNew.mc.player.swingArm(((boolean)this.offhandS.getValue()) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                this.breakTimer.reset();
                return;
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet2 = event.getPacket();
            if (packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                final SPacketSoundEffect sPacketSoundEffect = new SPacketSoundEffect();
                new ArrayList(AutoCrystalNew.mc.world.loadedEntityList).forEach(e -> {
                    if (e instanceof EntityEnderCrystal && ((EntityEnderCrystal) e).getDistanceSq(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) < 36.0) {
                        ((EntityEnderCrystal) e).setDead();
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.rotate.getValue() != Rotate.OFF && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet2 = event.getPacket();
            packet2.yaw = this.yaw;
            packet2.pitch = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
        BlockPos pos = null;
        CPacketUseEntity packet3 = null;
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            pos = packet3.getEntityFromWorld((World)AutoCrystal.mc.world).getPosition();
        }
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            final EntityEnderCrystal crystal = (EntityEnderCrystal)packet3.getEntityFromWorld((World)AutoCrystal.mc.world);
            if (EntityUtil.isCrystalAtFeet(crystal, this.range.getValue()) && pos != null) {
                this.rotateToPos(pos);
                BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, false);
            }
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet3 = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet3.getEntityFromWorld((World)AutoCrystal.mc.world) instanceof EntityEnderCrystal && this.cancelcrystal.getValue()) {
            Objects.requireNonNull(packet3.getEntityFromWorld((World)AutoCrystal.mc.world)).setDead();
            AutoCrystal.mc.world.removeEntityFromWorld(packet3.entityId);
        }
    }

    private void rotateToPos(final BlockPos pos) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case Place:
            case All: {
                final float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(AutoCrystalNew.mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
                if (this.rotate.getValue() != Rotate.OFF) {
                    Legacy.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
                break;
            }
        }
    }

    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.renderPos != null && this.render.getValue() && (this.box.getValue() || this.text.getValue() || this.outline.getValue())) {
            RenderUtil.drawBoxESP(this.renderPos, ((boolean)this.colorSync.getValue()) ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), ((boolean)this.colorSync.getValue()) ? this.getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            if (this.text.getValue()) {
                RenderUtil.drawText(this.renderPos, ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "");
            }
        }
    }

    public Color getCurrentColor() {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    @Override
    public String getDisplayInfo() {
        if (AutoCrystalNew.currentTarget != null) {
            if (this.infomode.getValue() == InfoMode.Target) {
                return AutoCrystalNew.currentTarget.getName();
            }
            if (this.infomode.getValue() == InfoMode.Damage) {
                return ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "";
            }
            if (this.infomode.getValue() == InfoMode.Both) {
                return AutoCrystalNew.currentTarget.getName() + ", " + ((Math.floor(this.renderDamage) == this.renderDamage) ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "";
            }
        }
        return null;
    }

    private boolean isValid(final Entity entity) {
        return entity != null && AutoCrystal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breakRange.getValue()) && (this.raytrace.getValue() == Raytrace.None || this.raytrace.getValue() == Raytrace.Place || AutoCrystal.mc.player.canEntityBeSeen(entity) || (!AutoCrystal.mc.player.canEntityBeSeen(entity) && AutoCrystal.mc.player.getDistanceSq(entity) <= MathUtil.square(this.breaktrace.getValue())));
    }
}