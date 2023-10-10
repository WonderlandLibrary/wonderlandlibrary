// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.combat;

import java.util.Iterator;
import net.minecraft.client.renderer.WorldRenderer;
import java.util.Arrays;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.augustus.events.EventRender3D;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntitySlime;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.world.World;
import net.minecraft.item.ItemTool;
import net.augustus.modules.misc.MidClick;
import org.lwjgl.input.Mouse;
import net.minecraft.util.MovingObjectPosition;
import net.augustus.utils.RayTraceUtil;
import net.augustus.events.EventSilentMove;
import net.augustus.events.EventJump;
import net.augustus.events.EventMove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.lenni0451.eventapi.events.IEvent;
import net.augustus.utils.EventHandler;
import net.augustus.events.EventClickKillAura;
import net.augustus.events.EventAttackEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.augustus.events.EventPreMotion;
import net.augustus.events.EventPostMotion;
import net.augustus.events.EventClick;
import java.security.SecureRandom;
import net.minecraft.util.AxisAlignedBB;
import net.augustus.utils.RandomUtil;
import net.minecraft.entity.Entity;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.function.Predicate;
import net.augustus.events.EventEarlyTick;
import net.augustus.events.EventAttackSlowdown;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventClickGui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import net.minecraft.item.ItemSword;
import java.util.Locale;
import net.augustus.Augustus;
import net.augustus.savings.FileManager;
import net.augustus.settings.Setting;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.minecraft.util.Vec3;
import net.augustus.settings.BooleansSetting;
import net.augustus.settings.StringValue;
import net.augustus.settings.BooleanValue;
import net.augustus.settings.DoubleValue;
import net.augustus.utils.RotationUtil;
import net.minecraft.entity.EntityLivingBase;
import java.util.ArrayList;
import net.augustus.utils.TimeHelper;
import net.augustus.modules.Module;

public class KillAura extends Module
{
    private final transient TimeHelper targetDelayTimer;
    private final TimeHelper hitTimeHelper;
    private final TimeHelper hittedTimeHelper;
    private final ArrayList<double[]> positions;
    public EntityLivingBase target;
    public float[] rots;
    public float[] lastRots;
    public RotationUtil rotationUtil;
    public double range;
    public final DoubleValue preRange;
    public final DoubleValue rangeSetting;
    public final DoubleValue targetDelay;
    public final DoubleValue yawSpeedMin;
    public final DoubleValue yawSpeedMax;
    public final DoubleValue pitchSpeedMin;
    public final DoubleValue pitchSpeedMax;
    public final DoubleValue minDelay;
    public final DoubleValue maxDelay;
    public final BooleanValue gommeFix;
    public final DoubleValue hitChance;
    public final DoubleValue randomStrength;
    public final BooleanValue player;
    public final BooleanValue mob;
    public final BooleanValue animal;
    public final BooleanValue villager;
    public final BooleanValue armorStand;
    public final BooleanValue invisible;
    public final BooleanValue interpolation;
    public final BooleanValue smoothBackRotate;
    public final BooleanValue stopOnTarget;
    public final BooleanValue perfectHit;
    public final BooleanValue perfectHitGomme;
    public final BooleanValue moveFix;
    public final BooleanValue silentMoveFix;
    public final BooleanValue bestHitVec;
    public final BooleanValue smartAim;
    public final BooleanValue throughWalls;
    public final BooleanValue slowDown;
    public final BooleanValue coolDown;
    public final BooleanValue preHit;
    public final BooleanValue inInv;
    public final BooleanValue randomize;
    public final BooleanValue block;
    public final BooleanValue multi;
    public final BooleanValue heuristics;
    public final BooleanValue hazeRange;
    public final DoubleValue hazeAdd;
    public final DoubleValue hazeMax;
    public final BooleanValue intave;
    public final BooleanValue slotWhitelist;
    public final BooleanValue advancedRots;
    public final StringValue mode;
    public final BooleansSetting targets;
    public final StringValue sortMode;
    public final StringValue targetRandom;
    public final StringValue rangeMode;
    public final StringValue attackMode;
    public final StringValue blockMode;
    public final DoubleValue startBlock;
    public final DoubleValue endBlock;
    public final DoubleValue endBlockHitOnly;
    public final BooleanValue unblockHit;
    public final BooleanValue unblockHitOnly;
    public final BooleanValue slot1;
    public final BooleanValue slot2;
    public final BooleanValue slot3;
    public final BooleanValue slot4;
    public final BooleanValue slot5;
    public final BooleanValue slot6;
    public final BooleanValue slot7;
    public final BooleanValue slot8;
    public final BooleanValue slot9;
    public boolean backRotate;
    private int multiClickCounter;
    private long time;
    private EntityLivingBase lastTarget;
    private long randomDelay;
    private Vec3 best;
    private boolean ssBlocking;
    private Object[] recordedClicks;
    private int counter;
    private long changeDelayDelay;
    private long delaySpike;
    private long delayDrop;
    private double cpsCounter;
    private long randomCPS;
    private long lastTime;
    
    public KillAura() {
        super("KillAura", new Color(102, 38, 28, 255), Categorys.COMBAT);
        this.targetDelayTimer = new TimeHelper();
        this.hitTimeHelper = new TimeHelper();
        this.hittedTimeHelper = new TimeHelper();
        this.positions = new ArrayList<double[]>();
        this.target = null;
        this.rots = new float[2];
        this.lastRots = new float[2];
        this.range = 3.0;
        this.preRange = new DoubleValue(1, "PreAimRange", this, 4.0, 0.0, 15.0, 1);
        this.rangeSetting = new DoubleValue(2, "Range", this, 3.0, 3.0, 6.0, 2);
        this.targetDelay = new DoubleValue(3, "TargetDelay", this, 500.0, 0.0, 1000.0, 0);
        this.yawSpeedMin = new DoubleValue(64, "YawSpeedMin", this, 40.0, 1.0, 180.0, 0);
        this.yawSpeedMax = new DoubleValue(4, "YawSpeedMax", this, 40.0, 1.0, 180.0, 0);
        this.pitchSpeedMin = new DoubleValue(65, "PitchSpeedMin", this, 40.0, 1.0, 180.0, 0);
        this.pitchSpeedMax = new DoubleValue(5, "PitchSpeedMax", this, 40.0, 1.0, 180.0, 0);
        this.minDelay = new DoubleValue(6, "MinDelay", this, 40.0, 0.0, 400.0, 0);
        this.maxDelay = new DoubleValue(46445, "MaxDelay", this, 40.0, 0.0, 400.0, 0);
        this.gommeFix = new BooleanValue(3284, "GommeFix", this, true);
        this.hitChance = new DoubleValue(7, "HitChance", this, 100.0, 0.0, 100.0, 0);
        this.randomStrength = new DoubleValue(8, "RStrength", this, 0.25, 0.01, 5.0, 2);
        this.player = new BooleanValue(9, "Player", this, true);
        this.mob = new BooleanValue(10, "Mob", this, true);
        this.animal = new BooleanValue(11, "Animal", this, true);
        this.villager = new BooleanValue(12, "Villager", this, true);
        this.armorStand = new BooleanValue(13, "ArmorStand", this, true);
        this.invisible = new BooleanValue(888, "Invisible", this, true);
        this.interpolation = new BooleanValue(14, "Interpolation", this, false);
        this.smoothBackRotate = new BooleanValue(15, "SBRotate", this, false);
        this.stopOnTarget = new BooleanValue(17, "StopOnTarget", this, false);
        this.perfectHit = new BooleanValue(90, "PerfectHit", this, true);
        this.perfectHitGomme = new BooleanValue(90, "PHIntave", this, true);
        this.moveFix = new BooleanValue(18, "MoveFix", this, true);
        this.silentMoveFix = new BooleanValue(19, "SilentMoveFix", this, true);
        this.bestHitVec = new BooleanValue(78, "BestHitVec", this, true);
        this.smartAim = new BooleanValue(20, "SmartAim", this, true);
        this.throughWalls = new BooleanValue(21, "ThroughWalls", this, false);
        this.slowDown = new BooleanValue(23, "SlowDown", this, true);
        this.coolDown = new BooleanValue(1338, "CoolDown", this, true);
        this.preHit = new BooleanValue(24, "PreHit", this, false);
        this.inInv = new BooleanValue(43, "InInv", this, false);
        this.randomize = new BooleanValue(44, "Randomize", this, false);
        this.block = new BooleanValue(45, "Block", this, false);
        this.multi = new BooleanValue(1337, "Multi", this, false);
        this.heuristics = new BooleanValue(46, "Heuristics", this, false);
        this.hazeRange = new BooleanValue(4343905, "HazeRange", this, false);
        this.hazeAdd = new DoubleValue(4343906, "HazeAdd", this, 0.5, 0.0, 1.0, 2);
        this.hazeMax = new DoubleValue(4343907, "HazeMax", this, 4.5, 3.0, 6.0, 2);
        this.intave = new BooleanValue(77, "Intave", this, false);
        this.slotWhitelist = new BooleanValue(47, "SlotWhiteList", this, false);
        this.advancedRots = new BooleanValue(66, "AdvancedRots", this, true);
        this.mode = new StringValue(42, "Mode", this, "Advanced", new String[] { "Basic", "Advanced" });
        this.targets = new BooleansSetting(31, "Targets", this, new Setting[] { this.player, this.mob, this.animal, this.villager, this.armorStand, this.invisible });
        this.sortMode = new StringValue(26, "TargetSort", this, "Distance", new String[] { "FOV", "Health", "Distance", "Best", "UltimateSwitch" });
        this.targetRandom = new StringValue(27, "Randomize", this, "Basic", new String[] { "None", "Basic", "Doubled", "OnlyRotation" });
        this.rangeMode = new StringValue(28, "RangeMode", this, "Legit", new String[] { "Legit", "Normal" });
        this.attackMode = new StringValue(30, "AttackMode", this, "Legit", new String[] { "Legit", "Pre", "Post" });
        this.blockMode = new StringValue(29, "BlockMode", this, "Basic", new String[] { "None", "Basic", "Intave", "Gomme1vs1", "Verus", "Custom" });
        this.startBlock = new DoubleValue(38, "StartBlock", this, 9.0, 0.0, 10.0, 0);
        this.endBlock = new DoubleValue(39, "EndBlock", this, 2.0, 0.0, 10.0, 0);
        this.endBlockHitOnly = new DoubleValue(39, "Unblock", this, 8.0, 0.0, 10.0, 0);
        this.unblockHit = new BooleanValue(40, "UnblockHit", this, true);
        this.unblockHitOnly = new BooleanValue(41, "UnblockHitOnly", this, false);
        this.slot1 = new BooleanValue(101, "Slot1", this, false);
        this.slot2 = new BooleanValue(102, "Slot2", this, false);
        this.slot3 = new BooleanValue(103, "Slot3", this, false);
        this.slot4 = new BooleanValue(104, "Slot4", this, false);
        this.slot5 = new BooleanValue(105, "Slot5", this, false);
        this.slot6 = new BooleanValue(106, "Slot6", this, false);
        this.slot7 = new BooleanValue(107, "Slot7", this, false);
        this.slot8 = new BooleanValue(108, "Slot8", this, false);
        this.slot9 = new BooleanValue(109, "Slot9", this, false);
        this.backRotate = false;
        this.multiClickCounter = 0;
        this.time = 50L;
        this.lastTarget = null;
        this.randomDelay = 100L;
        this.counter = 0;
        this.changeDelayDelay = 100L;
        this.delaySpike = 2000L;
        this.delayDrop = 2000L;
        this.cpsCounter = 1.0;
        this.randomCPS = 10L;
        this.player.setVisible(false);
        this.mob.setVisible(false);
        this.animal.setVisible(false);
        this.villager.setVisible(false);
        this.armorStand.setVisible(false);
        this.invisible.setVisible(false);
        this.slot1.setVisible(false);
        this.slot2.setVisible(false);
        this.slot3.setVisible(false);
        this.slot4.setVisible(false);
        this.slot5.setVisible(false);
        this.slot6.setVisible(false);
        this.slot7.setVisible(false);
        this.slot8.setVisible(false);
        this.slot9.setVisible(false);
    }
    
    @Override
    public void onEnable() {
        this.rotationUtil = new RotationUtil();
        if (KillAura.mc.thePlayer != null) {
            this.rots = new float[] { KillAura.mc.thePlayer.rotationYaw, KillAura.mc.thePlayer.rotationPitch };
            this.lastRots = new float[] { KillAura.mc.thePlayer.prevRotationYaw, KillAura.mc.thePlayer.prevRotationPitch };
        }
        this.range = this.rangeSetting.getValue();
        this.target = null;
        this.positions.clear();
        this.time = 0L;
        try {
            final FileManager<Integer> fileManager = new FileManager<Integer>();
            final ArrayList<Integer> list = fileManager.readFileAll(Augustus.getInstance().getName().toLowerCase(Locale.ROOT) + "/clickpattern", "ClickingPattern.json");
            this.recordedClicks = list.toArray();
        }
        catch (Exception ex) {}
    }
    
    @Override
    public void onDisable() {
        if (KillAura.mc.thePlayer.inventory.getCurrentItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), Keyboard.isKeyDown(1));
        }
        this.positions.clear();
        this.target = null;
    }
    
    @EventTarget
    public void onEventClickGui(final EventClickGui eventClickGui) {
        this.range = this.rangeSetting.getValue();
    }
    
    @EventTarget
    public void onEventAttackSlowDown(final EventAttackSlowdown attackSlowdown) {
        if (!this.slowDown.getBoolean()) {
            attackSlowdown.setSprint(true);
            attackSlowdown.setSlowDown(1.0);
        }
    }
    
    @EventTarget
    public void onEventEarlyTick(final EventEarlyTick eventTick) {
        if (KillAura.mc.theWorld != null) {
            this.setDisplayName(super.getName() + " §8" + 0);
        }
        if (KillAura.mc.currentScreen != null || !this.mode.getSelected().equals("Advanced")) {
            return;
        }
        Object[] listOfTargets = null;
        if (KillAura.mc.theWorld != null) {
            final String selected = this.sortMode.getSelected();
            switch (selected) {
                case "FOV": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::fov)).toArray();
                    break;
                }
                case "Health": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entity -> entity.getHealth())).toArray();
                    break;
                }
                case "Distance": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entity -> KillAura.mc.thePlayer.getDistanceToEntity(entity))).toArray();
                    break;
                }
                case "Best": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::isBestTarget)).toArray();
                    break;
                }
                case "UltimateSwitch": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::ultimateTarget)).toArray();
                    break;
                }
            }
            if (KillAura.mc.theWorld != null && listOfTargets != null) {
                this.setDisplayName(super.getName() + " §8" + Math.min(listOfTargets.length, 9));
            }
        }
        if (!this.canAttacked(this.target)) {
            this.target = null;
        }
        final Vec3 lastBest = this.best;
        final EntityLivingBase last = this.target;
        assert listOfTargets != null;
        if (listOfTargets.length > 0) {
            if (this.smartAim.getBoolean() && !this.throughWalls.getBoolean()) {
                boolean b = false;
                final EntityLivingBase entity2 = (EntityLivingBase)listOfTargets[0];
                double ex = entity2.posX;
                double ey = entity2.posY + entity2.getEyeHeight();
                double ez = entity2.posZ;
                if (this.bestHitVec.getBoolean()) {
                    final Vec3 entityVec = RotationUtil.getBestHitVec(entity2);
                    ex = entityVec.xCoord;
                    ey = entityVec.yCoord;
                    ez = entityVec.zCoord;
                }
                if (KillAura.mc.thePlayer.canPosBeSeen(new Vec3(ex, ey, ez))) {
                    this.best = new Vec3(ex, ey, ez);
                    this.target = (EntityLivingBase)listOfTargets[0];
                    b = true;
                }
                else {
                    for (int i = 0; i < listOfTargets.length; ++i) {
                        final AxisAlignedBB boundingBox = ((EntityLivingBase)listOfTargets[i]).getEntityBoundingBox();
                        this.best = null;
                        double nearest = 15.0;
                        for (double x = boundingBox.minX; x <= boundingBox.maxX; x += 0.07) {
                            for (double z = boundingBox.minZ; z <= boundingBox.maxZ; z += 0.07) {
                                for (double y = boundingBox.minY; y <= boundingBox.maxY; y += 0.07) {
                                    final Vec3 pos = new Vec3(x, y, z);
                                    if (KillAura.mc.thePlayer.canPosBeSeen(pos)) {
                                        final Vec3 eyes = KillAura.mc.thePlayer.getPositionEyes(1.0f);
                                        final double dist = Math.sqrt(Math.pow(x - eyes.xCoord, 2.0) + Math.pow(y - eyes.yCoord, 2.0) + Math.pow(z - eyes.zCoord, 2.0));
                                        if (dist <= nearest) {
                                            nearest = dist;
                                            this.best = pos;
                                        }
                                    }
                                }
                            }
                        }
                        if (this.best != null) {
                            this.target = (EntityLivingBase)listOfTargets[i];
                            b = true;
                            break;
                        }
                    }
                }
                if (!b) {
                    this.target = null;
                }
            }
            else {
                this.target = (EntityLivingBase)listOfTargets[0];
            }
        }
        if (this.slotWhitelist.getBoolean()) {
            final int id = KillAura.mc.thePlayer.inventory.currentItem + 1;
            if (id == 1 && !this.slot1.getBoolean()) {
                this.target = null;
            }
            else if (id == 2 && !this.slot2.getBoolean()) {
                this.target = null;
            }
            else if (id == 3 && !this.slot3.getBoolean()) {
                this.target = null;
            }
            else if (id == 4 && !this.slot4.getBoolean()) {
                this.target = null;
            }
            else if (id == 5 && !this.slot5.getBoolean()) {
                this.target = null;
            }
            else if (id == 6 && !this.slot6.getBoolean()) {
                this.target = null;
            }
            else if (id == 7 && !this.slot7.getBoolean()) {
                this.target = null;
            }
            else if (id == 8 && !this.slot8.getBoolean()) {
                this.target = null;
            }
            else if (id == 9 && !this.slot9.getBoolean()) {
                this.target = null;
            }
        }
        if (last != null && this.target != null && !this.target.getName().equals(last.getName()) && this.targetDelay.getValue() != 0.0) {
            if (!this.targetDelayTimer.reached((long)this.targetDelay.getValue() + RandomUtil.nextLong(0L, 60L))) {
                this.target = last;
                this.best = lastBest;
            }
            else {
                this.targetDelayTimer.reset();
            }
        }
        if (this.mode.getSelected().equals("Advanced")) {
            this.rotateNormal();
        }
    }
    
    private void rotateNormal() {
        final SecureRandom secureRandom = new SecureRandom();
        float deltaYaw = RandomUtil.nextFloat(this.yawSpeedMin.getValue() - 0.0010000000474974513, this.yawSpeedMax.getValue()) / 2.0f + secureRandom.nextFloat() + RandomUtil.nextFloat(this.yawSpeedMin.getValue() - 0.0010000000474974513, this.yawSpeedMax.getValue()) / 2.0f;
        float deltaPitch = RandomUtil.nextFloat(this.pitchSpeedMin.getValue() - 0.0010000000474974513, this.pitchSpeedMax.getValue()) / 2.0f + secureRandom.nextFloat() + RandomUtil.nextFloat(this.pitchSpeedMin.getValue() - 0.0010000000474974513, this.pitchSpeedMax.getValue()) / 2.0f;
        if (this.target != null) {
            this.backRotate = true;
            final double distance = KillAura.mc.thePlayer.getDistanceToEntity(this.target);
            if (distance < 0.4) {
                deltaPitch = RandomUtil.nextFloat(0.0f, 4.0f) / 2.0f + RandomUtil.nextFloat(0.0f, 4.0f) / 2.0f;
                deltaYaw = RandomUtil.nextFloat(0.0f, 4.0f) / 2.0f + RandomUtil.nextFloat(0.0f, 4.0f) / 2.0f;
            }
            final float[] floats = this.rotationUtil.faceEntityCustom(this.target, deltaYaw, deltaPitch, this.rots[0], this.rots[1], this.targetRandom.getSelected(), this.interpolation.getBoolean(), this.smartAim.getBoolean(), this.stopOnTarget.getBoolean(), (float)this.randomStrength.getValue(), this.best, this.throughWalls.getBoolean(), this.advancedRots.getBoolean(), this.heuristics.getBoolean(), this.intave.getBoolean(), this.bestHitVec.getBoolean());
            if (floats == null) {
                this.rots = this.lastRots;
                this.setRotation();
                this.lastRots = this.rots;
                this.target = null;
                return;
            }
            this.lastRots = this.rots;
            this.rots = floats;
            this.setRotation();
        }
        else if (this.smoothBackRotate.getBoolean()) {
            if (this.backRotate) {
                if (this.rots[0] % 360.0f <= Augustus.getInstance().getYawPitchHelper().realYaw % 360.0f + 20.0f && this.rots[0] % 360.0f > Augustus.getInstance().getYawPitchHelper().realYaw % 360.0f - 20.0f && this.rots[1] % 360.0f <= Augustus.getInstance().getYawPitchHelper().realPitch % 360.0f + 20.0f && this.rots[1] % 360.0f > Augustus.getInstance().getYawPitchHelper().realPitch % 360.0f - 20.0f) {
                    this.backRotate = false;
                    this.resetRotation();
                }
                else {
                    final float[] f = this.rotationUtil.backRotate(deltaYaw, deltaPitch, this.rots[0], this.rots[1], Augustus.getInstance().getYawPitchHelper().realYaw, Augustus.getInstance().getYawPitchHelper().realPitch);
                    this.lastRots = this.rots;
                    this.rots = f;
                    this.setRotation();
                }
            }
            else {
                this.resetRotation();
            }
        }
        else {
            this.resetRotation();
        }
    }
    
    private void basicMode() {
        if (!this.mode.getSelected().equals("Basic")) {
            return;
        }
        Object[] listOfTargets = null;
        if (KillAura.mc.theWorld != null) {
            final String selected = this.sortMode.getSelected();
            switch (selected) {
                case "FOV": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::fov)).toArray();
                    break;
                }
                case "Health": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entity -> entity.getHealth())).toArray();
                    break;
                }
                case "Distance": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble(entity -> KillAura.mc.thePlayer.getDistanceToEntity(entity))).toArray();
                    break;
                }
                case "Best": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::isBestTarget)).toArray();
                    break;
                }
                case "UltimateSwitch": {
                    listOfTargets = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::ultimateTarget)).toArray();
                    break;
                }
            }
            if (listOfTargets != null) {
                this.setDisplayName(super.getName() + " §8" + Math.min(listOfTargets.length, 9));
            }
        }
        if (!this.canAttacked(this.target)) {
            this.target = null;
        }
        assert listOfTargets != null;
        if (listOfTargets.length > 0) {
            this.target = (EntityLivingBase)listOfTargets[0];
        }
        if (this.target == null) {
            this.lastTarget = this.target;
            return;
        }
        this.rots = this.rotationUtil.basicRotation(this.target, this.rots[0], this.rots[1], this.randomize.getBoolean());
    }
    
    @EventTarget
    public void onEventClick(final EventClick eventClick) {
        if (this.target != null && !this.isScaffoldToggled() && KillAura.mc.currentScreen == null) {
            eventClick.setCanceled(true);
        }
        if (this.attackMode.getSelected().equalsIgnoreCase("Legit") && this.mode.getSelected().equals("Advanced")) {
            this.attack();
        }
        if (this.hazeRange.getBoolean()) {
            if (this.target != null && this.target.hurtTime == 10) {
                this.range = Math.min(this.range + this.hazeAdd.getValue(), this.hazeMax.getValue());
                this.lastTime = System.currentTimeMillis();
            }
            if (this.target != this.lastTarget) {
                this.range = this.rangeSetting.getValue();
            }
            if (Math.abs(System.currentTimeMillis() - this.lastTime) > 1000L) {
                this.range = this.rangeSetting.getValue();
            }
        }
        else {
            this.range = this.rangeSetting.getValue();
        }
    }
    
    @EventTarget
    public void onEventPostMotion(final EventPostMotion eventPostMotion) {
        if (this.attackMode.getSelected().equalsIgnoreCase("Post") && this.mode.getSelected().equals("Advanced")) {
            this.attack();
        }
        if (this.mode.getSelected().equals("Basic") && !this.isScaffoldToggled()) {
            if (!this.inInv.getBoolean() && KillAura.mc.currentScreen != null) {
                return;
            }
            if (this.target == null) {
                return;
            }
            if (this.block.getBoolean()) {
                this.block();
            }
        }
        if (this.mode.getSelected().equals("Advanced") && !this.isScaffoldToggled() && this.target != null && KillAura.mc.thePlayer.inventory.getCurrentItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && (this.blockMode.getSelected().equals("Basic") || this.blockMode.getSelected().equals("Gomme1vs1"))) {
            this.block();
        }
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotion eventPreMotion) {
        if (this.target != null && !this.isScaffoldToggled()) {
            final String selected = this.blockMode.getSelected();
            switch (selected) {
                case "Intave": {
                    if (KillAura.mc.thePlayer.getHeldItem() == null || (this.target == null && !this.ssBlocking)) {
                        break;
                    }
                    if (this.ssBlocking) {
                        KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, KillAura.mc.thePlayer.getPosition(), EnumFacing.DOWN));
                        this.ssBlocking = false;
                        break;
                    }
                    KillAura.mc.rightClickMouse();
                    this.ssBlocking = true;
                    break;
                }
            }
        }
        if (this.attackMode.getSelected().equalsIgnoreCase("Pre") && this.mode.getSelected().equals("Advanced")) {
            this.attack();
        }
        if (this.mode.getSelected().equals("Basic")) {
            if (!this.inInv.getBoolean() && KillAura.mc.currentScreen != null) {
                return;
            }
            this.basicMode();
            if (this.target == null) {
                this.resetRotation();
                return;
            }
            eventPreMotion.setYaw(this.rots[0]);
            eventPreMotion.setPitch(this.rots[1]);
            KillAura.mc.thePlayer.rotationYawHead = this.rots[0];
            KillAura.mc.thePlayer.rotationPitchHead = this.rots[1];
            KillAura.mc.thePlayer.renderYawOffset = this.rots[0];
            this.lastRots = this.rots;
            if (this.hitTimeHelper.reached(this.randomDelay)) {
                if (this.block.getBoolean()) {
                    this.unBlock();
                }
                KillAura.mc.thePlayer.swingItem();
                final EventAttackEntity eventAttackEntity = new EventAttackEntity();
                if (this.multi.getBoolean()) {
                    final Object[] array;
                    final Object[] listOfTargets = array = KillAura.mc.theWorld.loadedEntityList.stream().filter((Predicate<? super Object>)this::canAttacked).sorted(Comparator.comparingDouble((ToDoubleFunction<? super Object>)this::ultimateTarget)).toArray();
                    for (final Object object : array) {
                        if (object instanceof Entity) {
                            final Entity entity = (Entity)object;
                            if (KillAura.mc.thePlayer.getDistanceToEntity(entity) <= this.range) {
                                final EventClickKillAura eventClickKillAura = new EventClickKillAura();
                                EventHandler.call(eventClickKillAura);
                                KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                            }
                        }
                    }
                }
                else if (this.slowDown.getBoolean()) {
                    if (KillAura.mc.thePlayer.getDistanceToEntity(this.target) <= this.range) {
                        KillAura.mc.playerController.attackEntity(KillAura.mc.thePlayer, this.target);
                        EventHandler.call(eventAttackEntity);
                    }
                }
                else if (KillAura.mc.thePlayer.getDistanceToEntity(this.target) <= this.range) {
                    final EventClickKillAura eventClickKillAura2 = new EventClickKillAura();
                    EventHandler.call(eventClickKillAura2);
                    KillAura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.target, C02PacketUseEntity.Action.ATTACK));
                    EventHandler.call(eventAttackEntity);
                }
                this.setRandomDelay();
                this.hitTimeHelper.reset();
            }
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove eventMove) {
        if (KillAura.mc.currentScreen == null && this.target != null && this.mode.getSelected().equals("Advanced") && !this.isScaffoldToggled() && !KillAura.mm.targetStrafe.isToggled() && !this.moveFix.getBoolean()) {
            eventMove.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventJump(final EventJump eventJump) {
        if (KillAura.mc.currentScreen == null && this.target != null && this.mode.getSelected().equals("Advanced") && !this.isScaffoldToggled() && !KillAura.mm.targetStrafe.isToggled() && !this.moveFix.getBoolean()) {
            eventJump.setYaw(Augustus.getInstance().getYawPitchHelper().realYaw);
        }
    }
    
    @EventTarget
    public void onEventSilentMove(final EventSilentMove eventSilentMove) {
        if (KillAura.mc.currentScreen == null && (this.target != null || (this.smoothBackRotate.getBoolean() && this.backRotate)) && this.mode.getSelected().equals("Advanced") && !this.isScaffoldToggled() && !KillAura.mm.targetStrafe.isToggled() && this.moveFix.getBoolean() && this.silentMoveFix.getBoolean()) {
            eventSilentMove.setSilent(true);
        }
    }
    
    private boolean shouldHit() {
        if (this.perfectHit.getBoolean()) {
            if (this.perfectHitGomme.getBoolean()) {
                final MovingObjectPosition objectPosition = RayTraceUtil.rayCast(2.0f, this.rots);
                if (objectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && KillAura.mc.objectMouseOver.entityHit instanceof EntityLivingBase && KillAura.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                    return false;
                }
            }
            if (KillAura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && KillAura.mc.objectMouseOver.entityHit instanceof EntityLivingBase) {
                final EntityLivingBase entity = (EntityLivingBase)KillAura.mc.objectMouseOver.entityHit;
                if (entity.hurtTime == 0 || entity.hurtTime == 1) {
                    return true;
                }
            }
            if (this.gommeFix.getBoolean() && this.target.hurtTime == 4) {
                return false;
            }
        }
        return this.hitTimeHelper.reached(this.randomDelay) || this.hittedTimeHelper.reached(1000L);
    }
    
    private void attack() {
        if (KillAura.mc.currentScreen != null) {
            return;
        }
        if (this.target != null && !this.isScaffoldToggled()) {
            if (this.shouldHit()) {
                if (KillAura.mc.thePlayer.inventory.getCurrentItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && !this.isScaffoldToggled()) {
                    final String selected = this.blockMode.getSelected();
                    switch (selected) {
                        case "Basic": {
                            this.unBlock();
                            this.rawHit();
                            this.hitTimeHelper.reset();
                            break;
                        }
                        case "Verus": {
                            this.rawHit();
                            this.hitTimeHelper.reset();
                            if (KillAura.mc.thePlayer.isSwingInProgress && KillAura.mc.thePlayer.hurtTime != 0 && KillAura.mc.thePlayer.getHeldItem() != null && KillAura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                                KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                break;
                            }
                            break;
                        }
                        case "Custom": {
                            if (!this.unblockHit.getBoolean()) {
                                KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                this.rawHit();
                                break;
                            }
                            if (this.unblockHitOnly.getBoolean()) {
                                if (this.target.hurtTime <= this.endBlockHitOnly.getValue()) {
                                    this.unBlock();
                                    this.rawHit();
                                    KillAura.mc.rightClickMouse();
                                    KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                    break;
                                }
                                break;
                            }
                            else if (this.target.hurtTime <= this.endBlock.getValue()) {
                                if (KillAura.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
                                    KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                    break;
                                }
                                KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                                if (this.target.hurtTime < this.endBlock.getValue()) {
                                    this.rawHit();
                                    break;
                                }
                                break;
                            }
                            else {
                                if (this.target.hurtTime == this.startBlock.getValue()) {
                                    KillAura.mc.rightClickMouse();
                                    KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                        default: {
                            if (KillAura.mc.thePlayer.isUsingItem()) {
                                if (!KillAura.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                                    KillAura.mc.playerController.onStoppedUsingItem(KillAura.mc.thePlayer);
                                }
                                while (KillAura.mc.gameSettings.keyBindAttack.isPressed()) {}
                                while (KillAura.mc.gameSettings.keyBindUseItem.isPressed()) {}
                                while (KillAura.mc.gameSettings.keyBindPickBlock.isPressed()) {}
                            }
                            else {
                                while (KillAura.mc.gameSettings.keyBindAttack.isPressed()) {}
                                this.rawHit();
                                while (KillAura.mc.gameSettings.keyBindUseItem.isPressed()) {
                                    KillAura.mc.rightClickMouse();
                                }
                                while (KillAura.mc.gameSettings.keyBindPickBlock.isPressed()) {
                                    KillAura.mc.middleClickMouse();
                                }
                            }
                            if (KillAura.mc.gameSettings.keyBindUseItem.isKeyDown() && KillAura.mc.getRightClickDelayTimer() == 0 && !KillAura.mc.thePlayer.isUsingItem()) {
                                KillAura.mc.rightClickMouse();
                                break;
                            }
                            break;
                        }
                    }
                }
                else {
                    if (KillAura.mc.thePlayer.isUsingItem()) {
                        if (!KillAura.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                            KillAura.mc.playerController.onStoppedUsingItem(KillAura.mc.thePlayer);
                        }
                        while (KillAura.mc.gameSettings.keyBindAttack.isPressed()) {}
                        while (KillAura.mc.gameSettings.keyBindUseItem.isPressed()) {}
                        while (KillAura.mc.gameSettings.keyBindPickBlock.isPressed()) {}
                    }
                    else {
                        while (KillAura.mc.gameSettings.keyBindAttack.isPressed()) {}
                        this.rawHit();
                        while (KillAura.mc.gameSettings.keyBindUseItem.isPressed()) {
                            KillAura.mc.rightClickMouse();
                        }
                        while (KillAura.mc.gameSettings.keyBindPickBlock.isPressed()) {
                            KillAura.mc.middleClickMouse();
                        }
                    }
                    if (KillAura.mc.gameSettings.keyBindUseItem.isKeyDown() && KillAura.mc.getRightClickDelayTimer() == 0 && !KillAura.mc.thePlayer.isUsingItem()) {
                        KillAura.mc.rightClickMouse();
                    }
                }
                this.setRandomDelay();
                this.hitTimeHelper.reset();
            }
            else {
                this.time = 0L;
            }
        }
        else if (KillAura.mc.thePlayer.inventory.getCurrentItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword && !this.isScaffoldToggled()) {
            KeyBinding.setKeyBindState(KillAura.mc.gameSettings.keyBindUseItem.getKeyCode(), Mouse.isButtonDown(1));
        }
        KillAura.mc.sendClickBlockToController(false);
    }
    
    private void rawHit() {
        if (!this.hitChance((int)this.hitChance.getValue())) {
            return;
        }
        if (this.rangeMode.getSelected().equals("Normal") && KillAura.mc.thePlayer.getDistanceToEntity(this.target) >= this.range) {
            return;
        }
        if (!this.preHit.getBoolean() && (KillAura.mc.objectMouseOver == null || KillAura.mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY)) {
            this.lastTarget = this.target;
            return;
        }
        if (KillAura.mc.objectMouseOver != null && KillAura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && KillAura.mc.objectMouseOver.entityHit instanceof EntityPlayer && ((KillAura.mm.teams.isToggled() && KillAura.mm.teams.getTeammates().contains(KillAura.mc.objectMouseOver.entityHit)) || (KillAura.mm.midClick.isToggled() && MidClick.friends.contains(KillAura.mc.objectMouseOver.entityHit.getName()) && !KillAura.mm.midClick.noFiends))) {
            return;
        }
        if (this.coolDown.getBoolean()) {
            int tickCounter = 1;
            if (KillAura.mc.thePlayer.getCurrentEquippedItem() != null && (KillAura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword || KillAura.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemTool)) {
                tickCounter = KillAura.mc.thePlayer.getCurrentEquippedItem().getItem().coolDownTicks;
                if (KillAura.mc.thePlayer.ticksSinceLastSwing >= tickCounter) {
                    KillAura.mc.clickMouse();
                }
            }
            else {
                KillAura.mc.clickMouse();
            }
        }
        else {
            KillAura.mc.clickMouse();
        }
        this.hittedTimeHelper.reset();
        if (KillAura.mc.objectMouseOver != null && KillAura.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            this.lastTarget = this.target;
        }
    }
    
    private void block() {
        if (KillAura.mc.thePlayer != null && KillAura.mc.thePlayer.inventory != null && KillAura.mc.thePlayer.inventory.getCurrentItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() != null && KillAura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            KillAura.mc.playerController.sendUseItem(KillAura.mc.thePlayer, KillAura.mc.theWorld, KillAura.mc.thePlayer.inventory.getCurrentItem());
        }
    }
    
    private void unBlock() {
        KillAura.mc.playerController.onStoppedUsingItem(KillAura.mc.thePlayer);
    }
    
    private void setRandomDelay() {
        if (this.minDelay.getValue() == 0.0 && this.maxDelay.getValue() == 0.0) {
            this.randomDelay = 0L;
        }
        else if (Math.abs(this.minDelay.getValue() - this.maxDelay.getValue()) > 0.0) {
            this.randomDelay = (long)RandomUtil.nextSecureInt((int)this.minDelay.getValue(), (int)this.maxDelay.getValue());
        }
        else {
            this.randomDelay = (long)this.minDelay.getValue();
        }
    }
    
    private boolean hitChance(final int hitChance) {
        final int randomNumber = ThreadLocalRandom.current().nextInt(0, 99);
        return randomNumber <= hitChance;
    }
    
    private void setRotation() {
        if (!this.isScaffoldToggled()) {
            KillAura.mc.thePlayer.rotationYaw = this.rots[0];
            KillAura.mc.thePlayer.rotationPitch = this.rots[1];
            KillAura.mc.thePlayer.prevRotationYaw = this.lastRots[0];
            KillAura.mc.thePlayer.prevRotationPitch = this.lastRots[1];
        }
        else {
            this.resetRotation();
        }
    }
    
    private void resetRotation() {
        (this.rots = this.lastRots)[0] = Augustus.getInstance().getYawPitchHelper().realYaw;
        this.rots[1] = Augustus.getInstance().getYawPitchHelper().realPitch;
        this.lastRots[0] = Augustus.getInstance().getYawPitchHelper().realLastYaw;
        this.lastRots[1] = Augustus.getInstance().getYawPitchHelper().realLastPitch;
    }
    
    private boolean canAttacked(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isInvisible() && !this.invisible.getBoolean()) {
                return false;
            }
            if (entity instanceof EntitySlime) {
                return false;
            }
            if (((EntityLivingBase)entity).deathTime > 1) {
                return false;
            }
            if (entity instanceof EntityArmorStand && !this.armorStand.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityAnimal && !this.animal.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityMob && !this.mob.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityPlayer && !this.player.getBoolean()) {
                return false;
            }
            if (entity instanceof EntityVillager && !this.villager.getBoolean()) {
                return false;
            }
            if (entity.ticksExisted < 1) {
                return false;
            }
            if (entity instanceof EntityPlayer && KillAura.mm.teams.isToggled() && KillAura.mm.teams.getTeammates().contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && (entity.getName().equals("§aShop") || entity.getName().equals("SHOP") || entity.getName().equals("UPGRADES"))) {
                return false;
            }
            if (this.mode.getSelected().equals("Advanced")) {
                if (!KillAura.mc.thePlayer.canEntityBeSeen(entity) && !this.throughWalls.getBoolean() && !this.smartAim.getBoolean()) {
                    return false;
                }
            }
            else if (!KillAura.mc.thePlayer.canEntityBeSeen(entity) && !this.throughWalls.getBoolean()) {
                return false;
            }
            if (entity.isDead) {
                return false;
            }
            if (entity instanceof EntityPlayer && KillAura.mm.antiBot.isToggled() && AntiBot.bots.contains(entity)) {
                return false;
            }
            if (entity instanceof EntityPlayer && !KillAura.mm.midClick.noFiends && MidClick.friends.contains(entity.getName())) {
                return false;
            }
        }
        return entity instanceof EntityLivingBase && entity != KillAura.mc.thePlayer && KillAura.mc.thePlayer.getDistanceToEntity(entity) < this.preRange.getValue();
    }
    
    private double isBestTarget(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final double distance = KillAura.mc.thePlayer.getDistanceToEntity(entity);
            final double health = ((EntityLivingBase)entity).getHealth();
            double hurtTime = 10.0;
            if (entity instanceof EntityPlayer) {
                hurtTime = ((EntityPlayer)entity).hurtTime;
            }
            return distance * 2.0 + health + hurtTime * 4.0;
        }
        return 1000.0;
    }
    
    private double ultimateTarget(final Entity entity) {
        if (entity instanceof EntityLivingBase && ((RayTraceUtil.couldHit(entity, 1.0f, this.rots[0], this.rots[1], 180.0f, 180.0f) && this.range == 3.0) || this.range != 3.0)) {
            final double distance = KillAura.mc.thePlayer.getDistanceToEntity(entity);
            final double hurtTime = ((EntityLivingBase)entity).hurtTime * 6;
            return hurtTime + distance;
        }
        return 1000.0;
    }
    
    private double fov(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final float yaw = RotationUtil.getFovToTarget(entity.posX, entity.posY, entity.posZ, Augustus.getInstance().getYawPitchHelper().realYaw, Augustus.getInstance().getYawPitchHelper().realPitch)[0];
            return Math.abs(yaw);
        }
        return 1000.0;
    }
    
    @EventTarget
    public void onEvent3D(final EventRender3D eventRender3D) {
        if (KillAura.mm.line.isToggled() && KillAura.mm.line.killAura.getBoolean()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GlStateManager.disableCull();
            GL11.glDepthMask(false);
            final Vec3 vec31 = KillAura.mc.thePlayer.getCustomLook(1.0f, this.rots[0], this.rots[1]);
            final Vec3 vec32 = KillAura.mc.thePlayer.getPositionEyes(1.0f).addVector(vec31.xCoord * 3.0, vec31.yCoord * 3.0, vec31.zCoord * 3.0);
            final float x = (float)vec32.xCoord;
            final float y = (float)vec32.yCoord;
            final float z = (float)vec32.zCoord;
            this.positions.add(new double[] { x, y, z, (double)System.currentTimeMillis() });
            this.positions.removeIf(values -> this.shouldRenderPoint(values[3]));
            GL11.glColor4f(KillAura.mm.line.killAuraColor.getColor().getRed() / 255.0f, KillAura.mm.line.killAuraColor.getColor().getGreen() / 255.0f, KillAura.mm.line.killAuraColor.getColor().getBlue() / 255.0f, KillAura.mm.line.killAuraColor.getColor().getAlpha() / 255.0f);
            GL11.glLineWidth((float)KillAura.mm.line.killAuraLineWidth.getValue());
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            worldrenderer.begin(3, DefaultVertexFormats.POSITION);
            double[] lastPosition = { -2.0, -1.0, -1.0 };
            for (final double[] position : this.positions) {
                if (!Arrays.equals(lastPosition, new double[] { -2.0, -1.0, -1.0 }) && !Arrays.equals(lastPosition, position)) {
                    worldrenderer.pos((float)position[0] - KillAura.mc.getRenderManager().getRenderPosX(), (float)position[1] - KillAura.mc.getRenderManager().getRenderPosY(), (float)position[2] - KillAura.mc.getRenderManager().getRenderPosZ()).endVertex();
                }
                lastPosition = position;
            }
            tessellator.draw();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.enableCull();
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2848);
        }
    }
    
    private boolean shouldRenderPoint(final double time) {
        return Math.abs(time - System.currentTimeMillis()) > KillAura.mm.line.killAuraLineTime.getValue();
    }
    
    private boolean isScaffoldToggled() {
        return KillAura.mm.scaffoldWalk.isToggled() || KillAura.mm.newScaffold.isToggled() || KillAura.mm.blockFly.isToggled();
    }
}
