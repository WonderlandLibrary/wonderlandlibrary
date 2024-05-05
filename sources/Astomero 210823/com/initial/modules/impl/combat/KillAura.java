package com.initial.modules.impl.combat;

import com.initial.settings.impl.*;
import com.initial.settings.*;
import com.initial.utils.player.*;
import com.initial.modules.*;
import net.minecraft.client.settings.*;
import net.minecraft.util.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import java.util.concurrent.*;
import com.initial.utils.movement.*;
import net.minecraft.item.*;
import java.util.*;
import com.initial.events.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.entity.*;
import com.initial.utils.render.*;
import net.minecraft.client.network.*;
import com.initial.events.impl.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.play.client.*;
import com.initial.modules.impl.*;

public class KillAura extends Module
{
    public static Entity closestTarget;
    public ModeSet killAuraMode;
    public BooleanSet moveFix;
    public DoubleSet apsMaxSet;
    public DoubleSet apsMinSet;
    public DoubleSet hitChance;
    public DoubleSet attackRange;
    public DoubleSet blockRange;
    public DoubleSet switchDelay;
    public ModeSet attackState;
    public ModeSet autoBlockSet;
    public ModuleCategory rotationsCategory;
    public ModeSet rotationsMode;
    public BooleanSet realRotate;
    public DoubleSet smoothnessSet;
    public ModeSet renderRotationsMode;
    public ModuleCategory targetsCategory;
    public BooleanSet attackPlayers;
    public BooleanSet attackMobs;
    public BooleanSet attackAnimals;
    public BooleanSet attackVillagers;
    public BooleanSet attackInvis;
    public BooleanSet attackTargets;
    public BooleanSet attackFriends;
    public ModuleCategory checksCategory;
    public BooleanSet teamsEnabled;
    public BooleanSet botCheck;
    public DoubleSet ticksExistedSet;
    public DoubleSet hurtTimeCheckSet;
    public BooleanSet inventoryCheckSet;
    public BooleanSet onlyCritsSet;
    public ModuleCategory otherCategory;
    public ModeSet swingModeSet;
    public BooleanSet keepSprintSet;
    public BooleanSet swingOnBlockRangeSet;
    public BooleanSet slowDownSet;
    public DoubleSet slowDownFactor;
    public BooleanSet packetUnSprintSet;
    public BooleanSet noDelaySet;
    public ModuleCategory hudCategory;
    public BooleanSet hudEnabled;
    public ModeSet hudMode;
    public DoubleSet targetHudOppacity;
    public ModeSet particleModeSet;
    public DoubleSet particleSet;
    public float pYaw;
    public float pPitch;
    public Setting[] settings;
    List<Entity> entitiesToDraw;
    public static Entity target;
    TimerHelper timer;
    TimerHelper swingTimer;
    TimerHelper switchTimer;
    float[] serversideRotations;
    double doggoUpAndDown;
    int doggoStatus;
    double progressVal;
    boolean isProgressing;
    double leftForHealth;
    double left;
    double right;
    double realRight;
    double lastRight;
    boolean drawDone;
    int switchState;
    boolean attacking;
    boolean isBlocking;
    boolean verusBlocking;
    float oldYaw;
    float oldPitch;
    public float checkYaw;
    public float checkPitch;
    
    public KillAura() {
        super("KillAura", 0, Category.COMBAT);
        this.killAuraMode = new ModeSet("Mode", "Single", new String[] { "Single", "Switch", "NCP Multi", "Verus Multi", "Vanilla Multi" });
        this.moveFix = new BooleanSet("Strafe Fix", false);
        this.apsMaxSet = new DoubleSet("Max Speed", 10.0, 1.0, 20.0, 0.05, "APS");
        this.apsMinSet = new DoubleSet("Min Speed", 5.0, 1.0, 20.0, 0.05, "APS");
        this.hitChance = new DoubleSet("Hit Chance", 100.0, 0.0, 100.0, 1.0, "%");
        this.attackRange = new DoubleSet("Range", 3.4, 3.0, 7.0, 0.1) {
            @Override
            public void onChange() {
                if (KillAura.this.attackRange.getValue() > KillAura.this.blockRange.getValue()) {
                    KillAura.this.blockRange.setValue(KillAura.this.attackRange.getValue() + 0.1);
                }
                KillAura.this.blockRange.setMin(KillAura.this.attackRange.getValue());
            }
        };
        this.blockRange = new DoubleSet("Block Range", 3.5, 3.0, 9.0, 0.1) {
            @Override
            public void onChange() {
                if (KillAura.this.attackRange.getValue() > KillAura.this.blockRange.getValue()) {
                    this.setValue(KillAura.this.attackRange.getValue() + 0.1);
                }
                this.setMin(KillAura.this.attackRange.getValue());
            }
        };
        this.switchDelay = new DoubleSet("Switch Delay", 500.0, 60.0, 1000.0, 10.0, "ms");
        this.attackState = new ModeSet("Attack State", "Pre", new String[] { "Pre", "Post", "Double" });
        this.autoBlockSet = new ModeSet("AutoBlock", "Watchdog", new String[] { "Watchdog", "UnBlock", "Fake", "Vanilla", "Post", "None", "Reverse", "Tick", "Interact", "Verus" });
        this.rotationsCategory = new ModuleCategory("Rotations...");
        this.rotationsMode = new ModeSet("Rots Mode", "Verus", new String[] { "Verus", "Random", "Disabled", "EyeHeight", "Real Y", "LastTick", "UniversoCraft", "Dev2" });
        this.realRotate = new BooleanSet("Ray Trace", true);
        this.smoothnessSet = new DoubleSet("Smoothness", 35.0, 0.0, 100.0, 1.0);
        this.renderRotationsMode = new ModeSet("Render Rots", "Eye", new String[] { "Eye", "Disabled" });
        this.targetsCategory = new ModuleCategory("Targets...");
        this.attackPlayers = new BooleanSet("Players", true);
        this.attackMobs = new BooleanSet("Mobs", true);
        this.attackAnimals = new BooleanSet("Animals", false);
        this.attackVillagers = new BooleanSet("Villagers", false);
        this.attackInvis = new BooleanSet("Invisibles", false);
        this.attackTargets = new BooleanSet("Targets", true);
        this.attackFriends = new BooleanSet("Friends", false);
        this.checksCategory = new ModuleCategory("Checks...");
        this.teamsEnabled = new BooleanSet("Teams", true);
        this.botCheck = new BooleanSet("NPC Check", true);
        this.ticksExistedSet = new DoubleSet("Ticks Existed", 10.0, 0.0, 100.0, 1.0);
        this.hurtTimeCheckSet = new DoubleSet("Hurt Time", 10.0, 0.0, 10.0, 1.0);
        this.inventoryCheckSet = new BooleanSet("Inventory", false);
        this.onlyCritsSet = new BooleanSet("Only Falling", false);
        this.otherCategory = new ModuleCategory("Other...");
        this.swingModeSet = new ModeSet("Swing", "Client", new String[] { "Client", "Silent", "Disabled", "Spam" });
        this.keepSprintSet = new BooleanSet("KeepSprint", false);
        this.swingOnBlockRangeSet = new BooleanSet("Block-Range Swing", true);
        this.slowDownSet = new BooleanSet("Slowdown", false);
        this.slowDownFactor = new DoubleSet("Slowdown Factor", 0.95, 0.5, 0.99, 0.005);
        this.packetUnSprintSet = new BooleanSet("Packet UnSprint", false);
        this.noDelaySet = new BooleanSet("NoDelay", false);
        this.hudCategory = new ModuleCategory("Visual...");
        this.hudEnabled = new BooleanSet("Enabled", true);
        this.hudMode = new ModeSet("Mode", "Astolfo Old", new String[] { "Astolfo Old", "Astolfo" });
        this.targetHudOppacity = new DoubleSet("HUD Oppacity", 121.0, 0.0, 255.0, 1.0);
        this.particleModeSet = new ModeSet("Particles", "Sharp", new String[] { "Sharp", "Crit", "Both", "None" });
        this.particleSet = new DoubleSet("ParticleMult", 1.0, 0.0, 5.0, 1.0);
        this.pYaw = 0.0f;
        this.pPitch = 0.0f;
        this.settings = new Setting[] { this.killAuraMode, this.moveFix, this.apsMinSet, this.apsMaxSet, this.hitChance, this.switchDelay, this.rotationsCategory, this.targetsCategory, this.checksCategory, this.hudCategory, this.otherCategory, this.attackRange, this.blockRange, this.attackState, this.autoBlockSet };
        this.entitiesToDraw = new ArrayList<Entity>();
        KillAura.target = null;
        this.timer = new TimerHelper();
        this.swingTimer = new TimerHelper();
        this.switchTimer = new TimerHelper();
        this.doggoUpAndDown = 0.0;
        this.doggoStatus = 0;
        this.progressVal = 0.0;
        this.isProgressing = false;
        this.leftForHealth = 0.0;
        this.left = 0.0;
        this.right = 0.0;
        this.realRight = 0.0;
        this.lastRight = 0.0;
        this.drawDone = false;
        this.switchState = 0;
        this.attacking = false;
        this.isBlocking = false;
        this.verusBlocking = false;
        this.oldYaw = 0.0f;
        this.oldPitch = 0.0f;
        this.addSettings(this.settings);
        this.targetsCategory.addCatSettings(this.attackPlayers, this.attackMobs, this.attackVillagers, this.attackAnimals, this.attackInvis, this.attackTargets, this.attackFriends);
        this.rotationsCategory.addCatSettings(this.rotationsMode, this.realRotate, this.renderRotationsMode, this.smoothnessSet);
        this.hudCategory.addCatSettings(this.hudEnabled, this.hudMode, this.targetHudOppacity, this.particleModeSet, this.particleSet);
        this.checksCategory.addCatSettings(this.teamsEnabled, this.botCheck, this.ticksExistedSet, this.hurtTimeCheckSet, this.inventoryCheckSet, this.onlyCritsSet);
        this.otherCategory.addCatSettings(this.swingModeSet, this.swingOnBlockRangeSet, this.slowDownSet, this.slowDownFactor, this.packetUnSprintSet, this.keepSprintSet, this.noDelaySet);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.drawDone = false;
        this.progressVal = this.left;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.progressVal = this.leftForHealth;
        this.mc.gameSettings.keyBindUseItem.pressed = GameSettings.isKeyDown(this.mc.gameSettings.keyBindUseItem);
        if (this.autoBlockSet.is("Tick") && this.isBlocking) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (this.autoBlockSet.is("Verus") && this.verusBlocking) {
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        if (this.swingOnBlockRangeSet.isEnabled() && !this.getTargets(this.blockRange.getValue()).isEmpty() && this.getTargets(this.attackRange.getValue()).isEmpty() && this.swingTimer.timeElapsed(this.getAPSSpeed())) {
            this.swingArm();
            this.swingTimer.reset();
        }
        this.setDisplayName("Kill Aura §7" + this.killAuraMode.getMode());
        if (this.inventoryCheckSet.isEnabled() && this.mc.currentScreen != null) {
            return;
        }
        final List<Entity> targetList = this.getTargets(this.blockRange.getValue());
        this.entitiesToDraw = targetList;
        if (targetList.isEmpty()) {
            KillAura.target = null;
            KillAura.closestTarget = null;
        }
        else {
            long delay = (long)this.switchDelay.getValue();
            if (this.killAuraMode.getMode().equalsIgnoreCase("Verus Multi")) {
                delay = 20L;
            }
            if (this.killAuraMode.getMode().equalsIgnoreCase("NCP Multi")) {
                delay = 120L;
            }
            if (this.switchTimer.timeElapsed(delay)) {
                if (this.switchState > targetList.size()) {
                    this.switchState = 0;
                }
                else {
                    ++this.switchState;
                }
                this.switchTimer.reset();
            }
            final String mode = this.killAuraMode.getMode();
            switch (mode) {
                case "NCP Multi":
                case "Verus Multi":
                case "Switch": {
                    if (this.switchState >= targetList.size()) {
                        this.switchState = 0;
                    }
                    KillAura.closestTarget = targetList.get(this.switchState);
                    KillAura.target = targetList.get(this.switchState);
                    break;
                }
                default: {
                    KillAura.closestTarget = targetList.get(0);
                    KillAura.target = targetList.get(0);
                    break;
                }
            }
            final String mode2 = this.rotationsMode.getMode();
            switch (mode2) {
                case "Dev2": {
                    final double doX = KillAura.target.lastTickPosX + KillAura.target.motionX;
                    final double doZ = KillAura.target.lastTickPosZ + KillAura.target.motionZ;
                    final double minX = doX - 0.2;
                    final double maxX = doX + 0.2;
                    final double minZ = doZ - 0.2;
                    final double maxZ = doZ + 0.2;
                    final double rdY = KillAura.target.lastTickPosY + KillAura.target.motionY + 0.6;
                    final double rX = ThreadLocalRandom.current().nextDouble(minX, maxX);
                    final double rZ = ThreadLocalRandom.current().nextDouble(minZ, maxZ);
                    this.serversideRotations = RotationUtils.getRotationsWithDir(rX, rdY, rZ, 15.2f, 24.2f, this.oldYaw, this.oldPitch);
                    e.setYaw(this.serversideRotations[0]);
                    e.setPitch(this.serversideRotations[1]);
                    this.oldYaw = this.serversideRotations[0];
                    this.oldPitch = this.serversideRotations[1];
                    break;
                }
                case "Verus": {
                    if (this.doggoUpAndDown == 0.7 || this.doggoUpAndDown > 0.7) {
                        this.doggoStatus = 1;
                    }
                    if (this.doggoUpAndDown == -0.7 || this.doggoUpAndDown < -0.7) {
                        this.doggoStatus = 0;
                    }
                    if (this.doggoStatus == 0) {
                        this.doggoUpAndDown += 0.12;
                    }
                    if (this.doggoStatus == 1) {
                        this.doggoUpAndDown -= 0.12;
                    }
                    this.serversideRotations = RotationUtils.getRotsByPos(KillAura.target.posX + ThreadLocalRandom.current().nextDouble(0.1, 0.6), KillAura.target.posY + 0.92 + this.doggoUpAndDown, KillAura.target.posZ - ThreadLocalRandom.current().nextDouble(0.1, 0.6));
                    e.setYaw(this.serversideRotations[0]);
                    e.setPitch(this.serversideRotations[1]);
                    break;
                }
                case "Random": {
                    final double doX2 = KillAura.target.lastTickPosX + KillAura.target.motionX;
                    final double doZ2 = KillAura.target.lastTickPosZ + KillAura.target.motionZ;
                    final double minX2 = doX2 - 0.2;
                    final double maxX2 = doX2 + 0.2;
                    final double minZ2 = doZ2 - 0.2;
                    final double maxZ2 = doZ2 + 0.2;
                    final double rdY2 = KillAura.target.lastTickPosY + KillAura.target.motionY + 0.6;
                    final double rX2 = ThreadLocalRandom.current().nextDouble(minX2, maxX2);
                    final double rZ2 = ThreadLocalRandom.current().nextDouble(minZ2, maxZ2);
                    this.serversideRotations = RotationUtils.getRotationsWithDir(rX2, rdY2, rZ2, (float)ThreadLocalRandom.current().nextInt(0, 45), 24.2f, this.oldYaw, this.oldPitch);
                    e.setYaw(this.serversideRotations[0]);
                    e.setPitch(this.serversideRotations[1]);
                    this.oldYaw = this.serversideRotations[0];
                    this.oldPitch = this.serversideRotations[1];
                    break;
                }
                case "EyeHeight": {
                    this.serversideRotations = RotationUtils.getRotsByPos(KillAura.target.posX, KillAura.target.posY + 1.695, KillAura.target.posZ);
                    e.setYaw(this.serversideRotations[0]);
                    e.setPitch(this.serversideRotations[1]);
                    break;
                }
                case "Real Y": {
                    this.serversideRotations = RotationUtils.getRotsByPos(KillAura.target.posX, KillAura.target.posY, KillAura.target.posZ);
                    e.setYaw(this.serversideRotations[0]);
                    e.setPitch(this.serversideRotations[1]);
                    break;
                }
                case "LastTick": {
                    this.serversideRotations = RotationUtils.getRotsByPos(KillAura.target.lastTickPosX, KillAura.target.lastTickPosY, KillAura.target.lastTickPosZ);
                    e.setYaw(this.serversideRotations[0]);
                    e.setPitch(this.serversideRotations[1]);
                    break;
                }
                case "UniversoCraft": {
                    if (this.doggoUpAndDown == 0.7 || this.doggoUpAndDown > 0.7) {
                        this.doggoStatus = 1;
                    }
                    if (this.doggoUpAndDown == -0.7 || this.doggoUpAndDown < -0.7) {
                        this.doggoStatus = 0;
                    }
                    if (this.doggoStatus == 0) {
                        this.doggoUpAndDown += 0.12;
                    }
                    if (this.doggoStatus == 1) {
                        this.doggoUpAndDown -= 0.12;
                    }
                    this.serversideRotations = RotationUtils.getRotsByPos(KillAura.target.lastTickPosX, KillAura.target.posY + 0.92 + this.doggoUpAndDown, KillAura.target.lastTickPosZ);
                    e.setYaw((float)(this.serversideRotations[0] + ThreadLocalRandom.current().nextDouble(-10.0, 10.0)));
                    e.setPitch(Math.max(Math.min(this.serversideRotations[1], 90.0f), -90.0f));
                    break;
                }
            }
            this.mc.thePlayer.renderYawOffset = e.getYaw();
            this.mc.thePlayer.rotationYawHead = e.getYaw();
            this.mc.thePlayer.prevRenderYawOffset = e.getYaw();
        }
        if (!this.getTargets(this.blockRange.getValue()).isEmpty() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            final String mode3 = this.autoBlockSet.getMode();
            switch (mode3) {
                case "Vanilla": {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), this.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
                    break;
                }
                case "Interact":
                case "UnBlock":
                case "Watchdog": {
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
                }
                case "Reverse": {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), this.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
                    PacketUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(new BlockPos(new Random().nextInt(), new Random().nextInt(), new Random().nextInt()), 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    break;
                }
            }
        }
        final boolean verusAllowAttack = !this.verusBlocking || !this.autoBlockSet.is("Verus");
        if ((this.attackState.is("Pre") || this.attackState.is("Double")) && !targetList.isEmpty() && verusAllowAttack) {
            final Entity attackingTarget = KillAura.target;
            if ((this.noDelaySet.isEnabled() || this.timer.timeElapsed(this.getAPSSpeed())) && this.mc.thePlayer.getDistanceToEntity(attackingTarget) < this.attackRange.getValue()) {
                if (this.killAuraMode.is("Vanilla Multi")) {
                    for (final Entity e2 : targetList) {
                        this.attackEntity(e2);
                    }
                }
                else {
                    this.attackEntity(attackingTarget);
                }
                this.timer.reset();
            }
        }
        this.pYaw = e.getYaw();
        this.pPitch = e.getPitch();
    }
    
    @EventTarget
    public void onPost(final EventPostMotion e) {
        if (this.inventoryCheckSet.isEnabled() && this.mc.currentScreen != null) {
            return;
        }
        final boolean verusAllowAttack = !this.verusBlocking || !this.autoBlockSet.is("Verus");
        if ((this.attackState.is("Post") || this.attackState.is("Double")) && KillAura.target != null && verusAllowAttack) {
            final Entity attackingTarget = KillAura.target;
            if ((this.noDelaySet.isEnabled() || this.timer.timeElapsed(this.getAPSSpeed())) && this.mc.thePlayer.getDistanceToEntity(attackingTarget) < this.attackRange.getValue()) {
                if (this.killAuraMode.is("Vanilla Multi")) {
                    for (final Entity e2 : this.getTargets(this.blockRange.getValue())) {
                        this.attackEntity(e2);
                    }
                }
                else {
                    this.attackEntity(attackingTarget);
                }
                this.timer.reset();
            }
        }
        if (!this.getTargets(this.blockRange.getValue()).isEmpty() && this.mc.thePlayer.getCurrentEquippedItem() != null && this.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            final String mode = this.autoBlockSet.getMode();
            switch (mode) {
                case "Verus": {
                    if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                        this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), this.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
                        this.verusBlocking = true;
                        break;
                    }
                    this.verusBlocking = false;
                    break;
                }
                case "Interact": {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), this.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
                    if (KillAura.closestTarget != null) {
                        PacketUtil.sendPacket(new C02PacketUseEntity(KillAura.closestTarget, RotationUtils.getVectorToEntity(KillAura.closestTarget)));
                        PacketUtil.sendPacket(new C02PacketUseEntity(KillAura.closestTarget, C02PacketUseEntity.Action.INTERACT));
                    }
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-0.5534147541, -0.5534147541, -0.5534147541), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    break;
                }
                case "Watchdog": {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), this.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-0.5534147541, -0.5534147541, -0.5534147541), 255, Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
                    break;
                }
                case "UnBlock":
                case "Post": {
                    this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getCurrentEquippedItem());
                    break;
                }
                case "Reverse": {
                    PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
                }
                case "Tick": {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getCurrentEquippedItem(), this.mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
                    if (this.mc.thePlayer.ticksExisted % 2 == 0) {
                        if (!this.isBlocking) {
                            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.getCurrentEquippedItem()));
                        }
                        this.isBlocking = true;
                        break;
                    }
                    if (this.isBlocking) {
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    this.isBlocking = false;
                    break;
                }
            }
        }
        else {
            this.verusBlocking = false;
            this.isBlocking = false;
        }
    }
    
    @EventTarget
    public void onDraw(final EventRender2D e) {
        this.left = e.getWidth() / 2.0f - 70.0f;
        final double otherLeft = this.left;
        if (KillAura.target == null) {
            this.progressVal = otherLeft;
            this.isProgressing = false;
            return;
        }
        final EntityLivingBase target2 = (EntityLivingBase)KillAura.target;
        Color healthCol = new Color(-1);
        if (target2.getHealth() > 18.0f) {
            healthCol = new Color(6487914);
        }
        else if (target2.getHealth() > 16.0f) {
            healthCol = new Color(8716130);
        }
        else if (target2.getHealth() > 14.0f) {
            healthCol = new Color(11992930);
        }
        else if (target2.getHealth() > 12.0f) {
            healthCol = new Color(14876514);
        }
        else if (target2.getHealth() > 10.0f) {
            healthCol = new Color(16771682);
        }
        else if (target2.getHealth() > 8.0f) {
            healthCol = new Color(16757602);
        }
        else if (target2.getHealth() > 6.0f) {
            healthCol = new Color(16751189);
        }
        else if (target2.getHealth() > 4.0f) {
            healthCol = new Color(16738645);
        }
        else if (target2.getHealth() > 2.0f) {
            healthCol = new Color(16730698);
        }
        else {
            healthCol = new Color(16724273);
        }
        if (!this.isProgressing) {
            this.progressVal = otherLeft;
            this.isProgressing = true;
        }
        else {
            this.progressVal += 3.0;
        }
        this.lastRight = this.left + target2.getHealth() * 7.0f;
        if (!this.drawDone) {
            this.right = this.left + target2.getHealth() * 7.0f;
            this.drawDone = true;
        }
        if (this.right < this.lastRight) {
            this.right = this.lastRight;
        }
        else {
            this.right -= 0.5;
        }
        this.realRight = Math.min(this.progressVal, Math.max(this.lastRight, this.right));
        final double difference = 4.0;
        final double staticRight = this.left + 140.0;
        if (!this.hudEnabled.isEnabled()) {
            return;
        }
        final String mode = this.hudMode.getMode();
        switch (mode) {
            case "Astolfo": {
                final float y = e.getHeight() / 2.0f + 35.0f;
                final float x = e.getWidth() / 2.0f - 77.5f;
                final int color = new Color(16734296).getRGB();
                this.drawRectB(x - 1.0f, y + 2.0f, 155.0f, 57.0f, new Color(-1459157241, true));
                this.mc.fontRendererObj.drawStringWithShadow(KillAura.target.getName(), x + 31.0f, y + 6.0f, -1);
                GL11.glPushMatrix();
                GlStateManager.translate(x, y, 1.0f);
                GL11.glScalef(2.0f, 2.0f, 2.0f);
                GlStateManager.translate(-x, -y, 1.0f);
                this.mc.fontRendererObj.drawStringWithShadow(Math.round(target2.getHealth() / 2.0f * 10.0) / 10.0 + " \u2764", x + 16.0f, y + 13.0f, new Color(color).darker().getRGB());
                GL11.glPopMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GuiInventory.drawEntityOnScreen((int)x + 16, (int)y + 55, 25, KillAura.target.rotationYaw, -KillAura.target.rotationPitch, target2);
                final int xHealthbar = 30;
                final int yHealthbar = 46;
                final float add = 120.0f;
                this.drawRectB(x + xHealthbar, y + yHealthbar, add, 8.0f, new Color(color).darker().darker().darker());
                this.drawRectB(x + xHealthbar, y + yHealthbar, target2.getHealth() / target2.getMaxHealth() * add, 8.0f, new Color(color));
                final double addX = x + xHealthbar + target2.getHealth() / target2.getMaxHealth() * add;
                this.drawRectB((float)(addX - 3.0), y + yHealthbar, 3.0f, 8.0f, new Color(-1979711488, true));
                for (int index = 1; index < 5; ++index) {
                    if (target2.getEquipmentInSlot(index) == null) {}
                }
                break;
            }
            case "Astolfo Old": {
                if (KillAura.target instanceof EntityPlayer) {
                    final ScaledResolution s1r = new ScaledResolution(this.mc);
                    double hpPercentage = target2.getHealth() / target2.getMaxHealth();
                    final float scaledWidth = (float)s1r.getScaledWidth();
                    final float scaledHeight = (float)s1r.getScaledHeight();
                    final EntityPlayer player = (EntityPlayer)KillAura.target;
                    if (hpPercentage > 1.0) {
                        hpPercentage = 1.0;
                    }
                    else if (hpPercentage < 0.0) {
                        hpPercentage = 0.0;
                    }
                    final NetworkPlayerInfo networkPlayerInfo = this.mc.getNetHandler().getPlayerInfo(KillAura.target.getUniqueID());
                    Render2DUtils.drawRect2(scaledWidth / 2.0f - 200.0f, scaledHeight / 2.0f - 42.0f, scaledWidth / 2.0f - 200.0f + 40.0f + ((this.mc.fontRendererObj.getStringWidth(player.getName()) > 105) ? (this.mc.fontRendererObj.getStringWidth(player.getName()) - 10) : 105), scaledHeight / 2.0f - 2.0f, new Color(0, 0, 0, 150).getRGB());
                    Render2DUtils.drawFace((int)scaledWidth / 2 - 196, (int)(scaledHeight / 2.0f - 38.0f), 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f, (AbstractClientPlayer)player);
                    this.mc.fontRendererObj.drawStringWithShadow(player.getName(), scaledWidth / 2.0f - 196.0f + 40.0f, scaledHeight / 2.0f - 36.0f, -1);
                    Render2DUtils.drawRect2(scaledWidth / 2.0f - 196.0f + 40.0f, scaledHeight / 2.0f - 26.0f, (float)(scaledWidth / 2.0f - 196.0f + 40.0f + 87.5), scaledHeight / 2.0f - 14.0f, new Color(0, 0, 0).getRGB());
                    Render2DUtils.drawRect2(scaledWidth / 2.0f - 196.0f + 40.0f, scaledHeight / 2.0f - 26.0f, (float)(scaledWidth / 2.0f - 196.0f + 40.0f + hpPercentage * 1.25 * 70.0), scaledHeight / 2.0f - 14.0f, ColorUtil.getHealthColor(player).getRGB());
                    this.mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", player.getHealth()), scaledWidth / 2.0f - 196.0f + 40.0f + 36.0f, scaledHeight / 2.0f - 23.0f, ColorUtil.getHealthColor(player).getRGB());
                    this.mc.fontRendererObj.drawStringWithShadow("Ping: §7" + ((networkPlayerInfo == null) ? "0ms" : (networkPlayerInfo.responseTime + "ms")), scaledWidth / 2.0f - 196.0f + 40.0f, scaledHeight / 2.0f - 12.0f, -1);
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onRender(final EventRender3D e) {
        this.checkYaw = this.pYaw;
        this.checkPitch = this.pPitch;
        if (KillAura.target == null) {
            return;
        }
        if (this.inventoryCheckSet.isEnabled() && this.mc.currentScreen != null) {
            return;
        }
        if (this.renderRotationsMode.is("Eye")) {
            final double posX = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * this.mc.timer.renderPartialTicks;
            final double posY = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * this.mc.timer.renderPartialTicks;
            final double posZ = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * this.mc.timer.renderPartialTicks;
            final float[] serverRots = RotationUtils.getRotsByPos(posX, posY + KillAura.target.getEyeHeight(), posZ);
            this.mc.thePlayer.renderYawOffset = this.pYaw;
            this.mc.thePlayer.rotationYawHead = this.pYaw;
            this.mc.thePlayer.prevRenderYawOffset = this.pYaw;
        }
    }
    
    public void drawRectB(final float x, final float y, final float w, final float h, final Color color) {
        Gui.drawRect(x, y, x + w, y + h, color.getRGB());
    }
    
    public void attackEntity(final Entity e) {
        if (this.realRotate.isEnabled() && this.mc.objectMouseOver.entityHit != e) {
            return;
        }
        if (e.ticksExisted < this.ticksExistedSet.getValue()) {
            return;
        }
        if (e instanceof EntityLivingBase) {
            final EntityLivingBase castedEnt = (EntityLivingBase)e;
            if (castedEnt.hurtTime != 0 && castedEnt.hurtTime >= this.hurtTimeCheckSet.getValue()) {
                return;
            }
        }
        if (this.onlyCritsSet.isEnabled() && this.mc.thePlayer.fallDistance <= 0.0f) {
            return;
        }
        final String mode = this.swingModeSet.getMode();
        switch (mode) {
            case "Client": {
                this.mc.thePlayer.swingItem();
                break;
            }
            case "Silent": {
                PacketUtil.sendPacket(new C0APacketAnimation());
                break;
            }
            case "Spam": {
                this.mc.thePlayer.swingItem();
                PacketUtil.sendPacketSilent(new C0APacketAnimation());
                break;
            }
        }
        if (this.getRNG((int)this.hitChance.getValue())) {
            if (!this.keepSprintSet.isEnabled()) {
                this.mc.playerController.attackEntity(this.mc.thePlayer, e);
            }
            else {
                PacketUtil.sendPacket(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
            }
            for (int a = 0; a < this.particleSet.getValue(); ++a) {
                final String mode2 = this.particleModeSet.getMode();
                switch (mode2) {
                    case "Sharp": {
                        this.mc.thePlayer.onEnchantmentCritical(e);
                        break;
                    }
                    case "Crit": {
                        this.mc.thePlayer.onCriticalHit(e);
                        break;
                    }
                    case "Both": {
                        this.mc.thePlayer.onCriticalHit(e);
                        this.mc.thePlayer.onEnchantmentCritical(e);
                        break;
                    }
                }
            }
        }
    }
    
    public void swingArm() {
        this.mc.thePlayer.swingItem();
    }
    
    public boolean getRNG(final int chance) {
        final int random = ThreadLocalRandom.current().nextInt(0, 100);
        return random < chance;
    }
    
    public List<Entity> getTargets(final double range) {
        return Targets.getKillAuraTargets(range, this.attackPlayers.isEnabled(), this.attackVillagers.isEnabled(), this.attackMobs.isEnabled(), this.attackInvis.isEnabled(), this.attackAnimals.isEnabled(), this.attackFriends.isEnabled(), this.attackTargets.isEnabled(), this.teamsEnabled.isEnabled());
    }
    
    public long getAPSSpeed() {
        double maxSpeed = this.apsMaxSet.getValue();
        final double minSpeed = this.apsMinSet.getValue();
        if (minSpeed >= maxSpeed) {
            maxSpeed = Math.max(maxSpeed, minSpeed) + 0.15;
        }
        return (long)(1000.0 / Math.max(0.0, ThreadLocalRandom.current().nextDouble(minSpeed, maxSpeed)));
    }
}
