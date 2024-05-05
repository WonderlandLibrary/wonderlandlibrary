package com.initial.modules.impl.movement;

import com.initial.settings.impl.*;
import com.initial.settings.*;
import com.initial.utils.player.*;
import com.initial.modules.*;
import com.initial.events.*;
import com.initial.utils.network.*;
import net.minecraft.network.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraft.stats.*;
import net.minecraft.potion.*;
import net.minecraft.client.entity.*;
import com.initial.events.impl.*;
import net.minecraft.block.*;
import net.minecraft.network.play.client.*;
import com.initial.utils.movement.*;
import java.util.concurrent.*;
import net.minecraft.item.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import com.initial.*;

public class Scaffold extends Module
{
    private ModeSet modeSet;
    private DoubleSet timerSpeed;
    private ModeSet placeModeSet;
    private ModeSet vecModeSet;
    private ModeSet rotationsSet;
    public BooleanSet sneakSet;
    public BooleanSet sprintSet;
    private DoubleSet expandValSet;
    private ModeSet swingModeSet;
    public ModuleCategory spoofBlockCategory;
    public ModeSet spoofBlockMode;
    public BooleanSet spoofClientSwapBack;
    public BooleanSet spoofSetverSwitch;
    public ModuleCategory keepYCategory;
    public BooleanSet keepYEnabled;
    public BooleanSet keepYOnBhop;
    public ModuleCategory towerCategory;
    public ModeSet towerMode;
    public DoubleSet towerTimer;
    public DoubleSet towerPacketTicks;
    public DoubleSet towerConstantMotion;
    public ModuleCategory watchdogCategory;
    public BooleanSet watchdogRandomVec;
    public DoubleSet watchdogVecY;
    public DoubleSet watchdogYLevel;
    public DoubleSet watchdogSideVal;
    public BooleanSet watchdogKeepRots;
    public final Setting[] settingArray;
    private BlockPos cPos;
    private EnumFacing cFacing;
    float[] renderRotations;
    private boolean rotated;
    private boolean shouldRotate;
    boolean watchdogJumped;
    int watchdogState;
    float[] onplacerotupdate;
    boolean changed;
    int slotWithBlock;
    int itemBeforeSwap;
    boolean firstBlockPlaced;
    TimerHelper timer;
    
    public Scaffold() {
        super("Scaffold", 0, Category.MOVEMENT);
        this.modeSet = new ModeSet("Mode", "Watchdog", new String[] { "Normal", "Packet Sneak", "Slow", "Watchdog", "Watchdog2", "AAC", "Ray" });
        this.timerSpeed = new DoubleSet("Timer Speed", 1.0, 0.5, 5.0, 0.05);
        this.placeModeSet = new ModeSet("Place Mode", "Verus", new String[] { "Verus", "Post", "Pre", "Mixed", "Double" });
        this.vecModeSet = new ModeSet("VecHit Mode", "Verus", new String[] { "Verus", "Watchdog", "Morgan", "Legit" });
        this.rotationsSet = new ModeSet("Rotations", "Dynamic", new String[] { "Watchdog", "Dynamic", "VecHit", "NullPitch", "Morgan", "Disabled", "Redesky", "Verus", "AAC" });
        this.sneakSet = new BooleanSet("Sneak", false);
        this.sprintSet = new BooleanSet("Sprint", true);
        this.expandValSet = new DoubleSet("Expand", 0.0, 0.0, 4.0, 0.1);
        this.swingModeSet = new ModeSet("Swing", "Client", new String[] { "Client", "Server", "None", "Spam" });
        this.spoofBlockCategory = new ModuleCategory("Spoof...");
        this.spoofBlockMode = new ModeSet("Mode", "Server", new String[] { "Client", "Server", "Silent" });
        this.spoofClientSwapBack = new BooleanSet("Client Swap-Back", true);
        this.spoofSetverSwitch = new BooleanSet("Server Switch (Beta)", false);
        this.keepYCategory = new ModuleCategory("KeepY...");
        this.keepYEnabled = new BooleanSet("Enabled", false);
        this.keepYOnBhop = new BooleanSet("Only On-Speed", false);
        this.towerCategory = new ModuleCategory("Tower...");
        this.towerMode = new ModeSet("Mode", "Disabled", new String[] { "Packet", "Watchdog", "Morgan", "Constant", "Disabled", "Verus", "Universocraft" });
        this.towerTimer = new DoubleSet("Timer Speed", 1.0, 0.3, 5.0, 0.05);
        this.towerPacketTicks = new DoubleSet("Packet Ticks", 2.0, 1.0, 5.0, 1.0);
        this.towerConstantMotion = new DoubleSet("Constant Motion", 0.42, 0.42, 0.82, 0.01);
        this.watchdogCategory = new ModuleCategory("Watchdog...");
        this.watchdogRandomVec = new BooleanSet("Random Vec", true);
        this.watchdogVecY = new DoubleSet("Vec Y", 0.98, 0.0, 1.0, 0.01);
        this.watchdogYLevel = new DoubleSet("Y-Level", 12.0, 0.0, 20.0, 0.01);
        this.watchdogSideVal = new DoubleSet("Side Value", 0.3, 0.0, 0.5, 0.01);
        this.watchdogKeepRots = new BooleanSet("Keep", false);
        this.settingArray = new Setting[] { this.modeSet, this.timerSpeed, this.placeModeSet, this.vecModeSet, this.rotationsSet, this.keepYCategory, this.towerCategory, this.spoofBlockCategory, this.watchdogCategory, this.sneakSet, this.sprintSet, this.expandValSet, this.swingModeSet };
        this.rotated = false;
        this.shouldRotate = false;
        this.watchdogState = 0;
        this.changed = false;
        this.slotWithBlock = 0;
        this.itemBeforeSwap = 0;
        this.firstBlockPlaced = false;
        this.timer = new TimerHelper();
        this.addSettings(this.settingArray);
        this.watchdogCategory.addCatSettings(this.watchdogYLevel, this.watchdogSideVal, this.watchdogKeepRots, this.watchdogRandomVec, this.watchdogVecY);
        this.towerCategory.addCatSettings(this.towerMode, this.towerTimer, this.towerPacketTicks, this.towerConstantMotion);
        this.keepYCategory.addCatSettings(this.keepYEnabled, this.keepYOnBhop);
        this.spoofBlockCategory.addCatSettings(this.spoofBlockMode, this.spoofClientSwapBack, this.spoofSetverSwitch);
    }
    
    @EventTarget
    public void onWalk(final EventSafewalk e) {
        e.setSafewalk(true);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.changed = false;
        this.shouldRotate = false;
        this.firstBlockPlaced = false;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        final String mode = this.spoofBlockMode.getMode();
        switch (mode) {
            case "Client": {
                if (this.spoofClientSwapBack.isEnabled()) {
                    this.mc.thePlayer.inventory.currentItem = this.itemBeforeSwap;
                    break;
                }
                break;
            }
            case "Server": {
                if (this.mc.thePlayer.inventory.currentItem != this.itemBeforeSwap || (Scaffold.localPlayer.getCurrentEquippedItem() != null && Scaffold.localPlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                    this.mc.thePlayer.inventory.currentItem = this.itemBeforeSwap;
                    break;
                }
                PacketUtil.sendPacket(new C09PacketHeldItemChange(this.itemBeforeSwap));
                break;
            }
        }
        this.resetSneaking();
        if (this.modeSet.is("Packet Sneak") && this.rotated) {
            PacketUtil.sendPacketSilent(new C0BPacketEntityAction(Scaffold.localPlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        this.firstBlockPlaced = false;
        this.mc.timer.setTimerSpeed(1.0f);
    }
    
    @EventTarget
    public void onTick(final EventTick e) {
        this.mc.timer.setTimerSpeed((float)this.timerSpeed.getValue());
        if (this.sprintSet.isEnabled() && MovementUtils.isMoving()) {
            Scaffold.localPlayer.setSprinting(true);
        }
        else {
            Scaffold.localPlayer.setSprinting(false);
        }
        final String mode = this.spoofBlockMode.getMode();
        switch (mode) {
            case "Server": {
                if (Scaffold.localPlayer.getCurrentEquippedItem() == null || !(Scaffold.localPlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                    for (int a = 0; a < 9; ++a) {
                        if (Scaffold.localPlayer.inventory.getStackInSlot(a) != null) {
                            final boolean isSafeToSpoof = Scaffold.localPlayer.inventory.getStackInSlot(a).getItem() instanceof ItemBlock && !this.changed;
                            if (isSafeToSpoof) {
                                this.itemBeforeSwap = this.mc.thePlayer.inventory.currentItem;
                                this.slotWithBlock = a;
                                PacketUtil.sendPacketSilent(new C09PacketHeldItemChange(this.slotWithBlock));
                                this.changed = true;
                                break;
                            }
                        }
                    }
                    break;
                }
                break;
            }
            case "Client": {
                if (Scaffold.localPlayer.getCurrentEquippedItem() == null || !(Scaffold.localPlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                    for (int a = 0; a < 9; ++a) {
                        if (Scaffold.localPlayer.inventory.getStackInSlot(a) != null) {
                            final boolean isSafeToSpoof = Scaffold.localPlayer.inventory.getStackInSlot(a).getItem() instanceof ItemBlock && !this.changed;
                            if (isSafeToSpoof) {
                                this.itemBeforeSwap = this.mc.thePlayer.inventory.currentItem;
                                this.slotWithBlock = a;
                                Scaffold.localPlayer.inventory.currentItem = this.slotWithBlock;
                                this.changed = true;
                                break;
                            }
                        }
                    }
                    break;
                }
                break;
            }
        }
        if (Scaffold.localPlayer.inventory.getStackInSlot(this.slotWithBlock) == null && this.spoofSetverSwitch.isEnabled()) {
            this.changed = false;
        }
    }
    
    @EventTarget
    public void onPre(final EventMotion e) {
        this.setDisplayName("Scaffold");
        if (this.modeSet.is("Packet Sneak") && this.rotated && this.cPos != null && this.cFacing != null) {
            PacketUtil.sendPacketSilent(new C0BPacketEntityAction(Scaffold.localPlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        if (this.sneakSet.isEnabled()) {
            this.setSneaking(this.rotated);
        }
        boolean shouldTower = GameSettings.isKeyDown(this.mc.gameSettings.keyBindJump) && !MovementUtils.isMoving();
        if (this.towerMode.is("Verus")) {
            shouldTower = GameSettings.isKeyDown(this.mc.gameSettings.keyBindJump);
        }
        if (shouldTower && !this.towerMode.is("Disabled")) {
            this.mc.gameSettings.keyBindJump.pressed = false;
            this.mc.timer.setTimerSpeed((float)this.towerTimer.getValue());
            final String mode = this.towerMode.getMode();
            switch (mode) {
                case "Packet": {
                    Scaffold.localPlayer.motionZ = 0.0;
                    Scaffold.localPlayer.motionZ = 0.0;
                    if (MovementUtils.getOnRealGround(Scaffold.localPlayer, 0.01) && Scaffold.localPlayer.ticksExisted % this.towerPacketTicks.getValue() == 0.0) {
                        Scaffold.localPlayer.motionY = 0.0;
                        Scaffold.localPlayer.isAirBorne = true;
                        this.mc.thePlayer.triggerAchievement(StatList.jumpStat);
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.localPlayer.posX, Scaffold.localPlayer.posY + 0.41999998688698, Scaffold.localPlayer.posZ, false));
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Scaffold.localPlayer.posX, Scaffold.localPlayer.posY + 0.7531999805212, Scaffold.localPlayer.posZ, false));
                        MovementUtils.setY(Scaffold.localPlayer.posY + 1.0);
                        break;
                    }
                    break;
                }
                case "Constant": {
                    Scaffold.localPlayer.motionZ = 0.0;
                    Scaffold.localPlayer.motionZ = 0.0;
                    Scaffold.localPlayer.motionY = this.towerConstantMotion.getValue();
                    break;
                }
                case "Watchdog": {
                    Scaffold.localPlayer.motionZ = 0.0;
                    Scaffold.localPlayer.motionZ = 0.0;
                    if (this.watchdogJumped) {
                        switch (this.watchdogState) {
                            case 0: {
                                Scaffold.localPlayer.isAirBorne = true;
                                this.mc.thePlayer.triggerAchievement(StatList.jumpStat);
                                Scaffold.localPlayer.motionY = 0.41999998688698;
                                ++this.watchdogState;
                                break;
                            }
                            case 1: {
                                Scaffold.localPlayer.motionY = 0.33319999363422;
                                ++this.watchdogState;
                                break;
                            }
                            case 2: {
                                Scaffold.localPlayer.motionY = 0.24813599859094704;
                                ++this.watchdogState;
                                break;
                            }
                        }
                    }
                    if ((!this.watchdogJumped || this.watchdogState > 2) && MovementUtils.getOnRealGround(Scaffold.localPlayer, 1.0)) {
                        this.watchdogState = 0;
                        this.watchdogJumped = true;
                        break;
                    }
                    break;
                }
                case "Verus": {
                    if (MovementUtils.getOnRealGround(Scaffold.localPlayer, 0.01) && Scaffold.localPlayer.onGround && Scaffold.localPlayer.isCollidedVertically) {
                        this.watchdogState = 0;
                        this.watchdogJumped = true;
                    }
                    if (this.watchdogJumped) {
                        MovementUtils.setSpeed1(MovementUtils.getSpeed());
                        switch (this.watchdogState) {
                            case 0: {
                                Scaffold.localPlayer.isAirBorne = true;
                                this.mc.thePlayer.triggerAchievement(StatList.jumpStat);
                                Scaffold.localPlayer.motionY = 0.41999998688697815;
                                ++this.watchdogState;
                                break;
                            }
                            case 1: {
                                ++this.watchdogState;
                                break;
                            }
                            case 2: {
                                ++this.watchdogState;
                                break;
                            }
                            case 3: {
                                e.setOnGround(true);
                                Scaffold.localPlayer.motionY = 0.0;
                                ++this.watchdogState;
                                break;
                            }
                            case 4: {
                                ++this.watchdogState;
                                break;
                            }
                        }
                        this.watchdogJumped = false;
                        break;
                    }
                    this.watchdogJumped = true;
                    break;
                }
            }
        }
        else {
            this.watchdogState = -1;
        }
        final String mode2 = this.modeSet.getMode();
        switch (mode2) {
            case "Watchdog": {
                if (this.rotated && !Scaffold.localPlayer.onGround && !this.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP localPlayer = Scaffold.localPlayer;
                    localPlayer.motionZ *= 0.43;
                    final EntityPlayerSP localPlayer2 = Scaffold.localPlayer;
                    localPlayer2.motionX *= 0.43;
                    break;
                }
                break;
            }
            case "Watchdog2": {
                if (!Scaffold.localPlayer.onGround) {
                    break;
                }
                if (Scaffold.localPlayer.isSprinting()) {
                    final EntityPlayerSP localPlayer3 = Scaffold.localPlayer;
                    localPlayer3.motionZ *= 0.91;
                    final EntityPlayerSP localPlayer4 = Scaffold.localPlayer;
                    localPlayer4.motionX *= 0.91;
                }
                if (Scaffold.localPlayer.isPotionActive(Potion.moveSpeed)) {
                    final EntityPlayerSP localPlayer5 = Scaffold.localPlayer;
                    localPlayer5.motionZ *= 0.51;
                    final EntityPlayerSP localPlayer6 = Scaffold.localPlayer;
                    localPlayer6.motionX *= 0.51;
                    break;
                }
                break;
            }
            case "Slow": {
                if (Scaffold.localPlayer.onGround) {
                    final EntityPlayerSP localPlayer7 = Scaffold.localPlayer;
                    localPlayer7.motionZ *= 0.86;
                    final EntityPlayerSP localPlayer8 = Scaffold.localPlayer;
                    localPlayer8.motionX *= 0.86;
                    break;
                }
                break;
            }
            case "AAC": {
                final EntityPlayerSP localPlayer9 = Scaffold.localPlayer;
                localPlayer9.motionZ *= 0.6;
                final EntityPlayerSP localPlayer10 = Scaffold.localPlayer;
                localPlayer10.motionX *= 0.6;
                break;
            }
        }
        this.rotated = false;
        if (this.cPos != null && this.cFacing != null) {
            final float[] rots = this.getHypixelRotations(this.cPos, this.cFacing);
            float rotationYaw = this.mc.thePlayer.rotationYaw;
            if (this.mc.thePlayer.moveForward < 0.0f) {
                rotationYaw += 180.0f;
            }
            float forward = 1.0f;
            if (this.mc.thePlayer.moveForward < 0.0f) {
                forward = -0.5f;
            }
            else if (this.mc.thePlayer.moveForward > 0.0f) {
                forward = 0.5f;
            }
            if (this.mc.thePlayer.moveStrafing > 0.0f) {
                rotationYaw -= 90.0f * forward;
            }
            if (this.mc.thePlayer.moveStrafing < 0.0f) {
                rotationYaw += 90.0f * forward;
            }
            final String mode3 = this.rotationsSet.getMode();
            switch (mode3) {
                case "AAC": {
                    if (this.firstBlockPlaced) {
                        final float[] r2 = this.getRotationsToVec(this.getVec(this.cPos, this.cFacing));
                        if (this.shouldRotate) {
                            this.onplacerotupdate = r2;
                        }
                        else if (this.watchdogKeepRots.isEnabled()) {
                            this.onplacerotupdate = r2;
                        }
                        e.setYaw(this.onplacerotupdate[0]);
                        e.setPitch((float)Math.min(this.onplacerotupdate[1] + this.watchdogYLevel.getValue(), 90.0));
                        break;
                    }
                    e.setYaw(rotationYaw + 180.0f);
                    e.setPitch(84.0f);
                    break;
                }
                case "Watchdog": {
                    if (this.firstBlockPlaced) {
                        final float[] r2 = this.getRotationsToVec(this.getVec(this.cPos, this.cFacing));
                        if (this.shouldRotate) {
                            this.onplacerotupdate = r2;
                        }
                        e.setYaw(this.onplacerotupdate[0]);
                        e.setPitch((float)Math.min(this.onplacerotupdate[1] + this.watchdogYLevel.getValue(), 90.0));
                        break;
                    }
                    e.setYaw(rotationYaw - 50.0f);
                    e.setPitch(90.0f);
                    break;
                }
                case "Dynamic": {
                    e.setYaw(rots[0]);
                    e.setPitch(Scaffold.localPlayer.onGround ? 80.31f : rots[1]);
                    break;
                }
                case "NullPitch": {
                    e.setYaw(rots[0]);
                    e.setPitch(Float.MAX_VALUE);
                    break;
                }
                case "Redesky": {
                    final float[] redeRots = this.getDirectionToBlock(this.cPos.getX(), this.cPos.getY(), this.cPos.getZ(), this.cFacing);
                    e.setYaw(redeRots[0]);
                    e.setPitch(Scaffold.localPlayer.onGround ? 87.4f : (redeRots[1] - 9.0f));
                    break;
                }
                case "VecHit": {
                    final float[] vecRots = this.getRotationsToVec(this.getVec(this.cPos, this.cFacing));
                    e.setYaw(vecRots[0]);
                    e.setPitch(Math.min(vecRots[1], 90.0f));
                    break;
                }
                case "Morgan": {
                    final float[] r3 = this.getRotationsToVec(this.getVec(this.cPos, this.cFacing));
                    if (this.shouldRotate) {
                        e.setYaw(r3[0]);
                        e.setPitch(Math.min(r3[1], 90.0f));
                        break;
                    }
                    break;
                }
                case "Verus": {
                    if (this.shouldRotate) {
                        e.setPitch(90.0f);
                        break;
                    }
                    break;
                }
            }
            this.renderRotations = new float[] { e.getYaw(), e.getPitch() };
        }
        this.shouldRotate = false;
        final String mode4 = this.placeModeSet.getMode();
        switch (mode4) {
            case "Verus":
            case "Pre":
            case "Double": {
                this.place();
                break;
            }
            case "Mixed": {
                if (Scaffold.localPlayer.ticksExisted % 2 == 0) {
                    this.place();
                    break;
                }
                break;
            }
        }
        this.mc.thePlayer.rotationPitchHead = this.renderRotations[1];
        this.mc.thePlayer.renderYawOffset = this.renderRotations[0];
        this.mc.thePlayer.rotationYawHead = this.renderRotations[0];
        this.mc.thePlayer.prevRenderYawOffset = this.renderRotations[0];
    }
    
    @EventTarget
    public void onPost(final EventPostMotion e) {
        final String mode = this.placeModeSet.getMode();
        switch (mode) {
            case "Post":
            case "Double": {
                this.place();
                break;
            }
            case "Mixed": {
                if (Scaffold.localPlayer.ticksExisted % 2 != 0) {
                    this.place();
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onDraw(final EventRender2D e) {
        this.mc.fontRendererObj.drawStringWithShadow(this.getBlockCount() + " blocks", e.getWidth() / 2.0f - this.mc.fontRendererObj.getStringWidth(this.getBlockCount() + " blocks") - 15.0f, e.getHeight() / 2.0f - 3.5, -1);
        this.mc.thePlayer.rotationPitchHead = this.renderRotations[1];
        this.mc.thePlayer.renderYawOffset = this.renderRotations[0];
        this.mc.thePlayer.rotationYawHead = this.renderRotations[0];
        this.mc.thePlayer.prevRenderYawOffset = this.renderRotations[0];
    }
    
    public void place() {
        final double dir = MovementUtils.getDirection();
        final double posy = this.mc.thePlayer.posY - 1.0;
        final double expandX = Scaffold.localPlayer.motionX * this.expandValSet.getValue() * 10.0;
        final double expandZ = Scaffold.localPlayer.motionZ * this.expandValSet.getValue() * 10.0;
        final BlockPos pos = new BlockPos(this.mc.thePlayer.posX + expandX, posy, this.mc.thePlayer.posZ + expandZ);
        if (this.mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
            this.rotated = true;
            this.setPosAndFace(pos);
            if (this.cPos != null && this.cFacing != null) {
                this.shouldRotate = true;
                if (this.hasBlockOnHotbar()) {
                    if (this.modeSet.is("Packet Sneak")) {
                        PacketUtil.sendPacketSilent(new C0BPacketEntityAction(Scaffold.localPlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                    }
                    this.firstBlockPlaced = true;
                    final String mode = this.swingModeSet.getMode();
                    switch (mode) {
                        case "Client": {
                            Scaffold.localPlayer.swingItem();
                            break;
                        }
                        case "Server": {
                            PacketUtil.sendPacketSilent(new C0APacketAnimation());
                            break;
                        }
                        case "Spam": {
                            for (int a = 0; a < 5; ++a) {
                                PacketUtil.sendPacketSilent(new C0APacketAnimation());
                            }
                            break;
                        }
                    }
                    if (this.spoofBlockMode.is("Silent")) {
                        for (int i = 0; i < 9; ++i) {
                            if (Scaffold.localPlayer.inventory.getStackInSlot(i) != null && Scaffold.localPlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                                PacketUtil.sendPacket(new C09PacketHeldItemChange(this.slotWithBlock));
                                break;
                            }
                        }
                    }
                    final Vec3 vec = this.getVec(this.cPos, this.cFacing);
                    if (this.modeSet.is("Ray")) {}
                    this.placeBlock(this.mc.thePlayer.inventory.getStackInSlot(this.slotWithBlock), this.cPos, this.cFacing, vec);
                    if (this.spoofBlockMode.is("Silent")) {
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(Scaffold.localPlayer.inventory.currentItem));
                    }
                }
            }
        }
    }
    
    public float[] getRotationsToVec(final Vec3 vec) {
        final double x = vec.xCoord;
        final double y = vec.yCoord;
        final double z = vec.zCoord;
        return RotationUtils.getRotsByPos(x, y, z);
    }
    
    public void resetSneaking() {
        this.mc.gameSettings.keyBindSneak.pressed = GameSettings.isKeyDown(this.mc.gameSettings.keyBindSneak);
    }
    
    public void setSneaking(final boolean sneaking) {
        this.mc.gameSettings.keyBindSneak.pressed = sneaking;
    }
    
    public float[] getHypixelRotations(final BlockPos pos, final EnumFacing facing) {
        final float yaw = this.getYaw(pos, facing);
        final float[] rots2 = this.getDirectionToBlock(pos.getX(), pos.getY(), pos.getZ(), facing);
        return new float[] { (float)(yaw + ThreadLocalRandom.current().nextDouble(-1.0, 1.0)), Math.min(90.0f, rots2[1]) };
    }
    
    public boolean hasBlockOnHotbar() {
        for (int a = 0; a < 9; ++a) {
            if (this.mc.thePlayer.inventory.getStackInSlot(a) != null && this.mc.thePlayer.inventory.getStackInSlot(a).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }
    
    public Vec3 getVec(final BlockPos pos, final EnumFacing facing) {
        final ThreadLocalRandom randomThread = ThreadLocalRandom.current();
        final Vec3 vecToModify = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        final String mode = this.vecModeSet.getMode();
        switch (mode) {
            case "Legit": {
                if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    vecToModify.zCoord = Scaffold.localPlayer.posZ;
                    final Vec3 vec3 = vecToModify;
                    vec3.yCoord += 0.7;
                }
                if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
                    vecToModify.xCoord = Scaffold.localPlayer.posX;
                    final Vec3 vec4 = vecToModify;
                    vec4.yCoord += 0.7;
                }
                if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
                    final Vec3 vec5 = vecToModify;
                    vec5.xCoord += 0.5;
                    final Vec3 vec6 = vecToModify;
                    vec6.zCoord += 0.5;
                }
                if (facing == EnumFacing.UP) {
                    final Vec3 vec7 = vecToModify;
                    ++vec7.yCoord;
                }
                switch (facing) {
                    case SOUTH: {
                        final Vec3 vec8 = vecToModify;
                        ++vec8.zCoord;
                        break;
                    }
                    case NORTH: {
                        final Vec3 vec9 = vecToModify;
                        vec9.zCoord += 0.0;
                        break;
                    }
                    case EAST: {
                        final Vec3 vec10 = vecToModify;
                        ++vec10.xCoord;
                        break;
                    }
                    case WEST: {
                        final Vec3 vec11 = vecToModify;
                        vec11.xCoord += 0.0;
                        break;
                    }
                }
                break;
            }
            case "Watchdog": {
                if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    vecToModify.zCoord = Scaffold.localPlayer.posZ;
                    final Vec3 vec12 = vecToModify;
                    vec12.yCoord += 0.98;
                }
                if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
                    vecToModify.xCoord = Scaffold.localPlayer.posX;
                    final Vec3 vec13 = vecToModify;
                    vec13.yCoord += 0.98;
                }
                if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
                    final Vec3 vec14 = vecToModify;
                    vec14.xCoord += 0.5;
                    final Vec3 vec15 = vecToModify;
                    vec15.zCoord += 0.5;
                }
                if (facing == EnumFacing.UP) {
                    final Vec3 vec16 = vecToModify;
                    ++vec16.yCoord;
                }
                switch (facing) {
                    case SOUTH: {
                        final Vec3 vec17 = vecToModify;
                        ++vec17.zCoord;
                        break;
                    }
                    case NORTH: {
                        final Vec3 vec18 = vecToModify;
                        vec18.zCoord += 0.0;
                        break;
                    }
                    case EAST: {
                        final Vec3 vec19 = vecToModify;
                        ++vec19.xCoord;
                        break;
                    }
                    case WEST: {
                        final Vec3 vec20 = vecToModify;
                        vec20.xCoord += 0.0;
                        break;
                    }
                }
                if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    final boolean limit = Scaffold.localPlayer.posZ > pos.getZ() + 0.5;
                    final Vec3 vec21 = vecToModify;
                    vec21.zCoord += (limit ? -0.312 : 0.312);
                }
                if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
                    final boolean limit = Scaffold.localPlayer.posX > pos.getX() + 0.5;
                    final Vec3 vec22 = vecToModify;
                    vec22.xCoord += (limit ? -0.312 : 0.312);
                    break;
                }
                break;
            }
            case "Morgan": {
                final Vec3 vec23 = vecToModify;
                vec23.xCoord -= 0.01009971204872145;
                final Vec3 vec24 = vecToModify;
                vec24.zCoord -= 0.010002380947912738;
                break;
            }
            case "Verus": {
                final double dynamicVerusVec = ThreadLocalRandom.current().nextDouble(0.826, 0.827);
                final double extraVerusRandomization = ThreadLocalRandom.current().nextDouble(1.0E-4, 4.0E-4);
                if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                    final Vec3 vec25 = vecToModify;
                    vec25.zCoord += ThreadLocalRandom.current().nextDouble(0.499, 0.502);
                    final Vec3 vec26 = vecToModify;
                    vec26.yCoord += dynamicVerusVec;
                }
                else if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH) {
                    final Vec3 vec27 = vecToModify;
                    vec27.xCoord += ThreadLocalRandom.current().nextDouble(0.499, 0.501);
                    final Vec3 vec28 = vecToModify;
                    vec28.yCoord += dynamicVerusVec;
                }
                else if (facing == EnumFacing.UP) {
                    final Vec3 vec29 = vecToModify;
                    vec29.xCoord += 0.5;
                    final Vec3 vec30 = vecToModify;
                    vec30.zCoord += 0.5;
                    final Vec3 vec31 = vecToModify;
                    ++vec31.yCoord;
                }
                else if (facing == EnumFacing.DOWN) {
                    final Vec3 vec32 = vecToModify;
                    vec32.xCoord += 0.5;
                    final Vec3 vec33 = vecToModify;
                    vec33.zCoord += 0.5;
                }
                final Vec3 vec34 = vecToModify;
                vec34.xCoord += extraVerusRandomization;
                final Vec3 vec35 = vecToModify;
                vec35.yCoord += extraVerusRandomization;
                final Vec3 vec36 = vecToModify;
                vec36.zCoord += extraVerusRandomization;
                break;
            }
        }
        return vecToModify;
    }
    
    public float getYaw(final BlockPos block, final EnumFacing face) {
        final Vec3 vecToModify = new Vec3(block.getX(), block.getY(), block.getZ());
        switch (face) {
            case EAST:
            case WEST: {
                final Vec3 vec3 = vecToModify;
                vec3.zCoord += 0.5;
                break;
            }
            case SOUTH:
            case NORTH: {
                final Vec3 vec4 = vecToModify;
                vec4.xCoord += 0.5;
                break;
            }
            case UP:
            case DOWN: {
                final Vec3 vec5 = vecToModify;
                vec5.xCoord += 0.5;
                final Vec3 vec6 = vecToModify;
                vec6.zCoord += 0.5;
                break;
            }
        }
        final double x = vecToModify.xCoord - this.mc.thePlayer.posX;
        final double z = vecToModify.zCoord - this.mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        if (yaw < 0.0f) {
            yaw -= 360.0f;
        }
        return yaw;
    }
    
    public void placeBlock(final ItemStack stack, final BlockPos pos, final EnumFacing facing, final Vec3 vecHit) {
        this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, stack, pos, facing, vecHit);
    }
    
    private void updateRenderRotations(final EventMotion e) {
        this.mc.thePlayer.rotationPitchHead = e.getPitch();
        this.mc.thePlayer.renderYawOffset = e.getYaw();
        this.mc.thePlayer.rotationYawHead = e.getYaw();
        this.mc.thePlayer.prevRenderYawOffset = e.getYaw();
    }
    
    public int getBlockCount() {
        int blockCount = 0;
        for (int a = 0; a < 45; ++a) {
            if (this.mc.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
                final ItemStack is = this.mc.thePlayer.inventoryContainer.getSlot(a).getStack();
                if (is.getItem() instanceof ItemBlock) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
    }
    
    public float[] getDirectionToBlock(final int var0, final int var1, final int var2, final EnumFacing var3) {
        final EntityEgg var4 = new EntityEgg(this.mc.theWorld);
        var4.posX = var0 + 0.5;
        var4.posY = var1 + 0.5;
        var4.posZ = var2 + 0.5;
        final EntityEgg entityEgg = var4;
        entityEgg.posX += var3.getDirectionVec().getX() * 0.25;
        final EntityEgg entityEgg2 = var4;
        entityEgg2.posY += var3.getDirectionVec().getY() * 0.25;
        final EntityEgg entityEgg3 = var4;
        entityEgg3.posZ += var3.getDirectionVec().getZ() * 0.25;
        return getDirectionToEntity(var4);
    }
    
    public static float[] getDirectionToEntity(final Entity var0) {
        return new float[] { getYaw(var0) + Scaffold.localPlayer.rotationYaw, getPitch(var0) + Scaffold.localPlayer.rotationPitch };
    }
    
    public static float getYaw(final Entity var0) {
        final double var = var0.posX - Scaffold.localPlayer.posX;
        final double var2 = var0.posZ - Scaffold.localPlayer.posZ;
        final double degrees = Math.toDegrees(Math.atan(var2 / var));
        double var3;
        if (var2 < 0.0 && var < 0.0) {
            var3 = 90.0 + degrees;
        }
        else if (var2 < 0.0 && var > 0.0) {
            var3 = -90.0 + degrees;
        }
        else {
            var3 = Math.toDegrees(-Math.atan(var / var2));
        }
        return MathHelper.wrapAngleTo180_float(-(Scaffold.localPlayer.rotationYaw - (float)var3));
    }
    
    public static float getPitch(final Entity var0) {
        final double var = var0.posX - Scaffold.localPlayer.posX;
        final double var2 = var0.posZ - Scaffold.localPlayer.posZ;
        final double var3 = var0.posY - 1.6 + var0.getEyeHeight() - Scaffold.localPlayer.posY;
        final double var4 = MathHelper.sqrt_double(var * var + var2 * var2);
        final double var5 = -Math.toDegrees(Math.atan(var3 / var4));
        return -MathHelper.wrapAngleTo180_float(Scaffold.localPlayer.rotationPitch - (float)var5);
    }
    
    private void setPosAndFace(final BlockPos var1) {
        if (this.mc.theWorld.getBlockState(var1.add(0, -1, 0)).getBlock() != Blocks.air) {
            if (this.keepYEnabled.isEnabled()) {
                final boolean towering = GameSettings.isKeyDown(this.mc.gameSettings.keyBindJump) && !MovementUtils.isMoving();
                final boolean speedToggled = Astomero.instance.moduleManager.getModuleByName("Speed").isToggled();
                if (!this.keepYOnBhop.isEnabled()) {
                    if (towering) {
                        this.cPos = var1.add(0, -1, 0);
                        this.cFacing = EnumFacing.UP;
                    }
                }
                else if (towering || !speedToggled) {
                    this.cPos = var1.add(0, -1, 0);
                    this.cFacing = EnumFacing.UP;
                }
            }
            else {
                this.cPos = var1.add(0, -1, 0);
                this.cFacing = EnumFacing.UP;
            }
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, 0, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, 0, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, 0, -1);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, 0, 1);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, 0, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, 0, -1);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, 0, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, 0, 1);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, 0, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, 0, -1);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, 0, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, 0, 1);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, 0, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, 0, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, 0, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, 0, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, 0, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, 0, -2);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, 0, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, 0, 2);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, 0, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, 0, -2);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, 0, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, 0, 2);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, 0, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, 0, -2);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, 0, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, 0, 2);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, -1, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, -1, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, -1, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, -1, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -1, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -1, -1);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -1, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -1, 1);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, -1, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, -1, -1);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, -1, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, -1, 1);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, -1, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, -1, -1);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, -1, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, -1, 1);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, -1, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, -1, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, -1, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, -1, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -1, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -1, -2);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -1, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -1, 2);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, -1, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, -1, -2);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, -1, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, -1, 2);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, -1, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, -1, -2);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, -1, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, -1, 2);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-3, -1, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-3, -1, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(3, -1, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(3, -1, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -1, -3)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -1, -3);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -1, 3)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -1, 3);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-3, -1, -3)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-3, -1, -3);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-3, -1, 3)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-3, -1, 3);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(3, -1, -3)).getBlock() != Blocks.air) {
            this.cPos = var1.add(3, -1, -3);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(3, -1, 3)).getBlock() != Blocks.air) {
            this.cPos = var1.add(3, -1, 3);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, -2, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, -2, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, -2, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, -2, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -2, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -2, -1);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -2, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -2, 1);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, -2, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, -2, -1);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-1, -2, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-1, -2, 1);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, -2, -1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, -2, -1);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(1, -2, 1)).getBlock() != Blocks.air) {
            this.cPos = var1.add(1, -2, 1);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, -2, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, -2, 0);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, -2, 0)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, -2, 0);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -2, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -2, -2);
            this.cFacing = EnumFacing.SOUTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(0, -2, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(0, -2, 2);
            this.cFacing = EnumFacing.NORTH;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, -2, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, -2, -2);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(-2, -2, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(-2, -2, 2);
            this.cFacing = EnumFacing.EAST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, -2, -2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, -2, -2);
            this.cFacing = EnumFacing.WEST;
        }
        else if (this.mc.theWorld.getBlockState(var1.add(2, -2, 2)).getBlock() != Blocks.air) {
            this.cPos = var1.add(2, -2, 2);
            this.cFacing = EnumFacing.WEST;
        }
        else {
            this.cPos = null;
            this.cFacing = null;
        }
    }
}
