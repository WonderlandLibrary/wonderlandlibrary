package vestige.module.impl.combat;

import lombok.Getter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import vestige.Vestige;
import vestige.module.impl.player.Antivoid;
import vestige.module.impl.world.AutoBridge;
import vestige.event.Listener;
import vestige.event.impl.*;
import vestige.module.Category;
import vestige.module.Module;
import vestige.module.impl.movement.Speed;
import vestige.module.impl.world.Breaker;
import vestige.module.impl.world.Scaffold;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.DoubleSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.misc.LogUtil;
import vestige.util.misc.TimerUtil;
import vestige.util.network.PacketUtil;
import vestige.util.player.FixedRotations;
import vestige.util.player.MovementUtil;
import vestige.util.player.RotationsUtil;

import java.util.ArrayList;
import java.util.Comparator;

public class Killaura extends Module {

    @Getter
    private EntityLivingBase target;

    public final ModeSetting mode = new ModeSetting("Mode", "Single", "Single", "Switch", "Fast Switch");
    private final ModeSetting filter = new ModeSetting("Filter", "Range", "Range", "Health");
    private final ModeSetting rotations = new ModeSetting("Rotations", "Normal", "Normal", "Randomised", "Smooth", "None");
    private final DoubleSetting randomAmount = new DoubleSetting("Random amount", () -> mode.is("Randomised"), 4, 0.25, 10, 0.25);

    public final DoubleSetting startingRange = new DoubleSetting("Starting range", 4, 3, 6, 0.05);
    public final DoubleSetting range = new DoubleSetting("Range", 4, 3, 6, 0.05);
    public final DoubleSetting rotationRange = new DoubleSetting("Rotation range", 4, 3, 6, 0.05);

    private final ModeSetting raycast = new ModeSetting("Raycast", "Disabled", "Disabled", "Normal", "Legit");

    private final ModeSetting attackDelayMode = new ModeSetting("Attack delay mode", "APS", "APS", "Delay in ticks");

    private final IntegerSetting minAPS = new IntegerSetting("Min APS", () -> attackDelayMode.is("APS"), 10, 1, 20, 1);
    private final IntegerSetting maxAPS = new IntegerSetting("Max APS", () -> attackDelayMode.is("APS"), 10, 1, 20, 1);

    private final IntegerSetting attackDelay = new IntegerSetting("Attack delay", () -> attackDelayMode.is("Delay in ticks"), 2, 1, 20, 1);

    private final IntegerSetting failRate = new IntegerSetting("Fail rate", 0, 0, 30, 1);

    private final IntegerSetting hurtTime = new IntegerSetting("Hurt time", 10, 0, 10, 1);

    public final ModeSetting autoblock = new ModeSetting("Autoblock", "Fake", "Vanilla", "NCP", "AAC5", "Spoof", "Spoof2", "Blink", "Not moving", "Fake", "None");

    private final BooleanSetting noHitOnFirstTick = new BooleanSetting("No hit on first tick", () -> autoblock.is("Vanilla"), false);

    private final ModeSetting blockTiming = new ModeSetting("Block timing", () -> autoblock.is("Spoof") || autoblock.is("Spoof2"), "Post", "Pre", "Post");

    private final IntegerSetting blockHurtTime = new IntegerSetting("Block hurt time", () -> autoblock.is("Spoof") || autoblock.is("Spoof2") || autoblock.is("Blink"), 5, 0, 10, 1);
    private final BooleanSetting whileTargetNotLooking = new BooleanSetting("While target not looking", () -> autoblock.is("Blink"), true);
    private final ModeSetting slowdown = new ModeSetting("Slowdown", () -> autoblock.is("Blink"), "Enabled", "Enabled", "Onground", "Offground", "Disabled");
    private final IntegerSetting blinkTicks = new IntegerSetting("Blink ticks", () -> autoblock.is("Blink"), 5, 3, 10, 1);

    private final BooleanSetting whileHitting = new BooleanSetting("While hitting", () -> autoblock.is("Not moving"), false);

    private final BooleanSetting whileSpeedEnabled = new BooleanSetting("While speed enabled", () -> !autoblock.is("None") && !autoblock.is("Fake"), true);

    private final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", true);

    private final ModeSetting moveFix = new ModeSetting("Move fix", "Disabled", "Disabled", "Normal", "Silent");

    private final BooleanSetting delayTransactions = new BooleanSetting("Delay transactions", false);

    private final BooleanSetting whileInventoryOpened = new BooleanSetting("While inventory opened", false);
    private final BooleanSetting whileScaffoldEnabled = new BooleanSetting("While scaffold opened", false);
    private final BooleanSetting whileUsingBreaker = new BooleanSetting("While using breaker", false);

    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting animals = new BooleanSetting("Animals", false);
    private final BooleanSetting monsters = new BooleanSetting("Monsters", false);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    private final BooleanSetting attackDead = new BooleanSetting("Attack dead", false);

    private boolean hadTarget;

    private FixedRotations fixedRotations;

    private double random;

    private boolean attackNextTick;

    private double rotSpeed;
    private boolean done;

    private boolean blocking;
    private int autoblockTicks;

    private int attackCounter;

    private Antibot antibotModule;
    private Teams teamsModule;
    private Speed speedModule;
    private Scaffold scaffoldModule;
    private AutoBridge autoBridgeModule;
    private Breaker breakerModule;
    private Antivoid antivoidModule;
    private Velocity velocityModule;

    private boolean couldBlock;
    private boolean blinking;

    private int lastSlot;

    private final TimerUtil attackTimer = new TimerUtil();

    public Killaura() {
        super("Killaura", Category.COMBAT);
        this.addSettings(mode, filter, rotations, randomAmount, startingRange, range, rotationRange, raycast, attackDelayMode, minAPS, maxAPS, attackDelay, failRate, hurtTime, autoblock, noHitOnFirstTick, blockTiming, blinkTicks, blockHurtTime, whileTargetNotLooking, slowdown, whileHitting, whileSpeedEnabled, keepSprint, moveFix, delayTransactions, whileInventoryOpened, whileScaffoldEnabled, whileUsingBreaker, players, animals, monsters, invisibles, attackDead);
    }

    @Override
    public void onEnable() {
        fixedRotations = new FixedRotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);

        rotSpeed = 15;
        done = false;

        random = 0.5;

        attackNextTick = false;

        couldBlock = false;
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer != null) {
            if(hadTarget && rotations.is("Smooth")) {
                mc.thePlayer.rotationYaw = fixedRotations.getYaw();
            }

            stopTargeting();
        }

        Vestige.instance.getSlotSpoofHandler().stopSpoofing();
    }

    private void stopTargeting() {
        target = null;

        this.releaseBlocking();

        hadTarget = false;
        attackCounter = attackDelay.getValue();

        attackNextTick = false;

        if(delayTransactions.isEnabled()) {
            Vestige.instance.getPacketDelayHandler().stopAll();
        }
    }

    @Listener
    public void onRender(RenderEvent event) {
        if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 10) {
            this.setEnabled(false);
            return;
        }

        if(target != null && attackDelayMode.is("APS")) {
            long delay1 = (long) (1000.0 / minAPS.getValue());
            long delay2 = (long) (1000.0 / maxAPS.getValue());

            delay1 = Math.max(delay1, delay2);

            long delay = (long) (delay2 + (delay1 - delay2) * random);

            if(attackTimer.getTimeElapsed() >= delay) {
                attackNextTick = true;
                attackTimer.reset();
            }
        }
    }

    @Override
    public void onClientStarted() {
        antibotModule = Vestige.instance.getModuleManager().getModule(Antibot.class);
        speedModule = Vestige.instance.getModuleManager().getModule(Speed.class);
        teamsModule = Vestige.instance.getModuleManager().getModule(Teams.class);
        scaffoldModule = Vestige.instance.getModuleManager().getModule(Scaffold.class);
        autoBridgeModule = Vestige.instance.getModuleManager().getModule(AutoBridge.class);
        breakerModule = Vestige.instance.getModuleManager().getModule(Breaker.class);
        antivoidModule = Vestige.instance.getModuleManager().getModule(Antivoid.class);
        velocityModule = Vestige.instance.getModuleManager().getModule(Velocity.class);
    }

    @Listener
    public void onTick(TickEvent event) {
        if(mc.thePlayer.ticksExisted < 10) {
            this.setEnabled(false);
            return;
        }

        random = Math.random();

        switch (mode.getMode()) {
            case "Single":
                if(target == null || !canAttack(target)) {
                    target = findTarget(true);
                }
                break;
            case "Switch":
                target = findTarget(true);
                break;
            case "Fast Switch":
                target = findTarget(false);
                break;
        }

        getRotations();

        boolean inventoryOpened = mc.currentScreen instanceof GuiContainer && !whileInventoryOpened.isEnabled();
        boolean scaffoldEnabled = (scaffoldModule.isEnabled() || autoBridgeModule.isEnabled()) && !whileScaffoldEnabled.isEnabled();
        boolean usingBreaker = breakerModule.isEnabled() && breakerModule.isBreakingBed() && !whileUsingBreaker.isEnabled();

        if(target == null || inventoryOpened || scaffoldEnabled || usingBreaker) {
            stopTargeting();
            couldBlock = false;
            return;
        }

        boolean attackTick = false;

        if(getDistanceToEntity(target) <= (hadTarget ? range.getValue() : startingRange.getValue())) {
            if(target.hurtTime <= hurtTime.getValue()) {
                switch (attackDelayMode.getMode()) {
                    case "APS":
                        if(!hadTarget) {
                            attackTick = true;
                            attackTimer.reset();
                        } else if(attackNextTick) {
                            attackTick = true;
                            attackNextTick = false;
                        }
                        break;
                    case "Delay in ticks":
                        if(++attackCounter >= attackDelay.getValue()) {
                            attackTick = true;
                        }
                        break;
                }
            }

            if(delayTransactions.isEnabled()) {
                Vestige.instance.getPacketDelayHandler().startDelayingPing(2000);
            }

            hadTarget = true;
        } else {
            hadTarget = false;
        }

        boolean shouldBlock = canBlock();
        couldBlock = shouldBlock;

        if(shouldBlock) {
            if(!autoblockAllowAttack()) {
                attackTick = false;
            }

            beforeAttackAutoblock(attackTick);
        } else {
            if(blocking) {
                attackTick = false;
            }

            releaseBlocking();
        }

        if(attackTick) {
            boolean canAttack = true;

            if(!raycast.is("Disabled")) {
                canAttack = raycast.is("Legit") ?
                        RotationsUtil.raycastEntity(target, fixedRotations.getYaw(), fixedRotations.getPitch(), fixedRotations.getLastYaw(), fixedRotations.getLastPitch(), range.getValue() + 0.3) :
                        RotationsUtil.raycastEntity(target, fixedRotations.getYaw(), fixedRotations.getPitch(), fixedRotations.getYaw(), fixedRotations.getPitch(), range.getValue() + 0.3);
            }

            double aaa = failRate.getValue() / 100.0;

            if(Math.random() > 1 - aaa) {
                canAttack = false;
            }

            mc.thePlayer.swingItem();

            if(canAttack) {
                if(keepSprint.isEnabled()) {
                    mc.playerController.attackEntityNoSlowdown(mc.thePlayer, target);
                } else {
                    mc.playerController.attackEntity(mc.thePlayer, target);
                }
            }

            attackCounter = 0;
        }

        if(shouldBlock) {
            afterAttackAutoblock(attackTick);
        }

        mc.gameSettings.keyBindAttack.pressed = false;

        if(!autoblock.is("None") && !autoblock.is("Blink")) {
            mc.gameSettings.keyBindUseItem.pressed = false;
        }

        if(!rotations.is("None") && isRotating() && moveFix.is("Silent")) {
            float diff = MathHelper.wrapAngleTo180_float(MathHelper.wrapAngleTo180_float(fixedRotations.getYaw()) - MathHelper.wrapAngleTo180_float(MovementUtil.getPlayerDirection())) + 22.5F;

            if (diff < 0) {
                diff = 360 + diff;
            }

            int a = (int) (diff / 45.0);

            float value = mc.thePlayer.moveForward != 0 ? Math.abs(mc.thePlayer.moveForward) : Math.abs(mc.thePlayer.moveStrafing);

            float forward = value;
            float strafe = 0;

            for (int i = 0; i < 8 - a; i++) {
                float dirs[] = MovementUtil.incrementMoveDirection(forward, strafe);

                forward = dirs[0];
                strafe = dirs[1];
            }

            if(forward < 0.8F) {
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
            }
        }
    }

    @Listener
    public void onSlowdown(SlowdownEvent event) {
        if(canBlock()) {
            switch (autoblock.getMode()) {
                case "Blink":
                    switch (slowdown.getMode()) {
                        case "Onground":
                            if(!mc.thePlayer.onGround) {
                                event.setAllowedSprinting(true);
                                event.setForward(1F);
                                event.setStrafe(1F);
                            }
                            break;
                        case "Offground":
                            if(mc.thePlayer.onGround) {
                                event.setAllowedSprinting(true);
                                event.setForward(1F);
                                event.setStrafe(1F);
                            }
                            break;
                        case "Disabled":
                            event.setAllowedSprinting(true);
                            event.setForward(1F);
                            event.setStrafe(1F);
                            break;
                    }
                    break;
            }
        }
    }

    @Listener
    public void onJump(JumpEvent event) {
        if(target != null && !rotations.is("None") && !moveFix.is("Disabled")) {
            event.setYaw(fixedRotations.getYaw());
        }
    }

    @Listener
    public void onStrafe(StrafeEvent event) {
        if(!rotations.is("None") && isRotating()) {
            switch (moveFix.getMode()) {
                case "Normal":
                    event.setYaw(fixedRotations.getYaw());
                    break;
                case "Silent":
                    event.setYaw(fixedRotations.getYaw());

                    float diff = MathHelper.wrapAngleTo180_float(MathHelper.wrapAngleTo180_float(fixedRotations.getYaw()) - MathHelper.wrapAngleTo180_float(MovementUtil.getPlayerDirection())) + 22.5F;

                    if (diff < 0) {
                        diff = 360 + diff;
                    }

                    int a = (int) (diff / 45.0);

                    float value = event.getForward() != 0 ? Math.abs(event.getForward()) : Math.abs(event.getStrafe());

                    float forward = value;
                    float strafe = 0;

                    for (int i = 0; i < 8 - a; i++) {
                        float dirs[] = MovementUtil.incrementMoveDirection(forward, strafe);

                        forward = dirs[0];
                        strafe = dirs[1];
                    }

                    event.setForward(forward);
                    event.setStrafe(strafe);
                    break;
            }
        }
    }

    private boolean canRenderBlocking() {
        return canBlock() || autoblock.is("Fake");
    }

    private boolean canBlock() {
        ItemStack stack = mc.thePlayer.getHeldItem();

        if(autoblock.is("Blink")) {
            if(antivoidModule.isBlinking()) {
                return false;
            }

            if(mc.thePlayer.hurtTime > blockHurtTime.getValue()) {
                return false;
            }

            if(target != null && !whileTargetNotLooking.isEnabled()) {
                float targetYaw = MathHelper.wrapAngleTo180_float(target.rotationYaw);
                float diff = Math.abs(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) - targetYaw);

                boolean targetLooking = (diff > 90 && diff < 270) || mc.thePlayer.getDistanceToEntity(target) < 1.3;

                if (!targetLooking) {
                    return false;
                }
            }
        }

        if(autoblock.is("Spoof") || autoblock.is("Spoof2")) {
            if(mc.thePlayer.hurtTime > blockHurtTime.getValue()) {
                return false;
            }

            if(autoblock.is("Spoof2") && target != null) {
                return true;
            }
        }

        return target != null && stack != null && stack.getItem() instanceof ItemSword && (whileSpeedEnabled.isEnabled() || !Vestige.instance.getModuleManager().getModule(Speed.class).isEnabled());
    }

    private void beforeAttackAutoblock(boolean attackTick) {
        int slot = mc.thePlayer.inventory.currentItem;

        switch (autoblock.getMode()) {
            case "Vanilla":
                if(!blocking) {
                    PacketUtil.sendBlocking(true, false);
                    blocking = true;
                }

                autoblockTicks++;
                break;
            case "NCP":
                if(blocking) {
                    PacketUtil.releaseUseItem(true);
                    blocking = false;
                }
                break;
            case "Spoof":
                PacketUtil.sendPacket(new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));

                if(blockTiming.is("Pre")) {
                    PacketUtil.sendBlocking(true, false);
                    blocking = true;
                }
                break;
            case "Spoof2":
                if(autoblockTicks >= 2) {
                    mc.thePlayer.inventory.currentItem = lastSlot;
                    mc.playerController.syncCurrentPlayItem();
                    Vestige.instance.getSlotSpoofHandler().stopSpoofing();

                    if(blinking) {
                        Vestige.instance.getPacketBlinkHandler().releaseAll();
                    }
                    autoblockTicks = 0;
                }

                if(autoblockTicks == 0) {
                    if(blockTiming.is("Pre")) {
                        PacketUtil.sendBlocking(true, false);
                        blocking = true;
                    }
                } else if(autoblockTicks == 1) {
                    if(!velocityModule.isEnabled() || !velocityModule.mode.is("Hypixel") || !speedModule.isEnabled()) {
                        Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                        blinking = true;
                    }

                    lastSlot = slot;

                    Vestige.instance.getSlotSpoofHandler().startSpoofing(slot);
                    mc.thePlayer.inventory.currentItem = slot < 8 ? slot + 1 : 0;
                }
                break;
            case "Blink":
                if (autoblockTicks > 0 && autoblockTicks < blinkTicks.getValue()) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                }

                if (autoblockTicks == blinkTicks.getValue() || autoblockTicks == 0) {
                    mc.gameSettings.keyBindUseItem.pressed = true;

                    autoblockTicks = 0;
                }

                blocking = true;
                break;
            case "Not moving":
                if(MovementUtil.isMoving() || (target.hurtTime < hurtTime.getValue() + 1 && !whileHitting.isEnabled())) {
                    mc.gameSettings.keyBindUseItem.pressed = false;
                    blocking = false;
                }
                break;
        }
    }

    private void afterAttackAutoblock(boolean attackTick) {
        switch (autoblock.getMode()) {
            case "AAC5":
                PacketUtil.sendBlocking(true, false);
                blocking = true;
                break;
        }
    }

    private void postAutoblock() {
        switch (autoblock.getMode()) {
            case "NCP":
                if(!blocking) {
                    PacketUtil.sendBlocking(true, false);
                    blocking = true;
                }
                break;
            case "Spoof":
                if(blockTiming.is("Post")) {
                    PacketUtil.sendBlocking(true, false);
                    blocking = true;
                }
                break;
            case "Spoof2":
                if(blockTiming.is("Post")) {
                    PacketUtil.sendBlocking(true, false);
                    blocking = true;
                }

                autoblockTicks++;
                break;
            case "Not moving":
                if(!MovementUtil.isMoving() && (target.hurtTime >= hurtTime.getValue() + 1 || whileHitting.isEnabled())) {
                    mc.gameSettings.keyBindUseItem.pressed = true;
                    blocking = true;
                }
                break;
            case "Blink":
                if(target == null) {
                    LogUtil.addChatMessage("Autoblock test 2");
                }

                if(autoblockTicks == 0) {
                    Vestige.instance.getPacketBlinkHandler().releaseAll();
                    Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                }

                autoblockTicks++;
                blinking = true;
                break;
        }
    }

    private boolean autoblockAllowAttack() {
        switch (autoblock.getMode()) {
            case "Vanilla":
                return noHitOnFirstTick.isEnabled() ? autoblockTicks > 1 : true;
            case "Spoof2":
                return autoblockTicks == 2;
            case "Blink":
                return autoblockTicks >= 2 && autoblockTicks < blinkTicks.getValue();
        }

        return true;
    }

    private void releaseBlocking() {
        ItemStack stack = mc.thePlayer.getHeldItem();

        if(hadTarget && autoblock.is("Blink") && !blocking && target == null) {
            LogUtil.addChatMessage("Autoblock test : " + Vestige.instance.getPacketBlinkHandler().isBlinking());
        }

        int slot = mc.thePlayer.inventory.currentItem;

        if(blocking) {
            switch (autoblock.getMode()) {
                case "Vanilla":
                case "NCP":
                case "AAC5":
                    if(stack != null && stack.getItem() instanceof ItemSword) {
                        PacketUtil.releaseUseItem(true);
                    }
                    break;
                case "Spoof":
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(slot < 8 ? slot + 1 : 0));
                    PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));
                    break;
                case "Spoof2":
                    if(autoblockTicks == 1) {
                        mc.thePlayer.inventory.currentItem = lastSlot < 8 ? lastSlot + 1 : 0;

                        new Thread(() -> {
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mc.thePlayer.inventory.currentItem = lastSlot;
                            mc.playerController.syncCurrentPlayItem();
                        }).start();
                    } else {
                        mc.thePlayer.inventory.currentItem = lastSlot;
                    }

                    mc.playerController.syncCurrentPlayItem();

                    Vestige.instance.getSlotSpoofHandler().stopSpoofing();

                    if(blinking) {
                        Vestige.instance.getPacketBlinkHandler().stopAll();
                        blinking = false;
                    }
                    break;
                case "Not moving":
                    mc.gameSettings.keyBindUseItem.pressed = false;
                    break;
            }

            blocking = false;
        }

        if(autoblock.is("Blink") && (blinking || blocking)) {
            Vestige.instance.getPacketBlinkHandler().stopAll();
            blinking = false;
            blocking = false;
            mc.gameSettings.keyBindUseItem.pressed = false;
        }

        autoblockTicks = 0;
    }

    @Listener
    public void onItemRender(ItemRenderEvent event) {
        if(canRenderBlocking() && (blocking || !autoblock.is("Not moving")) && !autoblock.is("None")) {
            event.setRenderBlocking(true);
        }
    }

    private void getRotations() {
        float yaw = fixedRotations.getYaw();
        float pitch = fixedRotations.getPitch();

        if(target != null) {
            float rots[] = RotationsUtil.getRotationsToEntity(target, false);

            if(speedModule.isEnabled() && speedModule.mode.is("Pathfind")) {
                rots = RotationsUtil.getRotationsToEntity(speedModule.getActualX(), speedModule.getActualY(), speedModule.getActualZ(), target, false);
            }

            float yaw1;
            float diff;

            switch (rotations.getMode()) {
                case "Normal":
                    yaw = rots[0];
                    pitch = rots[1];
                    break;
                case "Randomised":
                    double amount = randomAmount.getValue();

                    yaw = (float) (rots[0] + Math.random() * amount - amount / 2);
                    pitch = (float) (rots[1] + Math.random() * amount - amount / 2);
                    break;
                case "Smooth":
                    yaw1 = rots[0];
                    float currentYaw = MathHelper.wrapAngleTo180_float(yaw);

                    diff = Math.abs(currentYaw - yaw1);

                    if(diff >= 8) {
                        if(diff > 35) {
                            rotSpeed += 4 - Math.random();

                            rotSpeed = Math.max(rotSpeed, (float) (31 - Math.random()));
                        } else {
                            rotSpeed -= 6.5 - Math.random();

                            rotSpeed = Math.max(rotSpeed, (float) (14 - Math.random()));
                        }

                        if(diff <= 180) {
                            if(currentYaw > yaw1) {
                                yaw -= rotSpeed;
                            } else {
                                yaw += rotSpeed;
                            }
                        } else {
                            if(currentYaw > yaw1) {
                                yaw += rotSpeed;
                            } else {
                                yaw -= rotSpeed;
                            }
                        }
                    } else {
                        if(currentYaw > yaw1) {
                            yaw -= diff * 0.8;
                        } else {
                            yaw += diff * 0.8;
                        }
                    }

                    yaw += Math.random() * 0.7 - 0.35;
                    pitch = (float) (mc.thePlayer.rotationPitch + (rots[1] - mc.thePlayer.rotationPitch) * 0.6);
                    pitch += Math.random() * 0.5 - 0.25;

                    done = false;
                    break;
            }
        } else {
            switch (rotations.getMode()) {
                case "Smooth":
                    rotSpeed = 15;

                    if(!hadTarget) {
                        done = true;
                    }
                    break;
            }
        }

        fixedRotations.updateRotations(yaw, pitch);
    }

    private boolean isRotating() {
        switch (rotations.getMode()) {
            case "Normal":
            case "Randomised":
                return target != null;
            case "Smooth":
                return target != null || !done;
            case "None":
                return false;
        }

        return false;
    }

    @Listener
    public void onMotion(MotionEvent event) {
        if(isRotating()) {
            event.setYaw(fixedRotations.getYaw());
            event.setPitch(fixedRotations.getPitch());
        }
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        if(couldBlock) {
            postAutoblock();
        }
    }

    public EntityLivingBase findTarget(boolean allowSame) {
        return findTarget(allowSame, rotationRange.getValue());
    }

    public EntityLivingBase findTarget(boolean allowSame, double range) {
        ArrayList<EntityLivingBase> entities = new ArrayList<>();
        for(Entity entity : mc.theWorld.loadedEntityList) {
            if(entity instanceof EntityLivingBase && entity != mc.thePlayer) {
                if(canAttack((EntityLivingBase) entity, range)) {
                    entities.add((EntityLivingBase) entity);
                }
            }
        }

        if(entities != null && entities.size() > 0) {
            switch (filter.getMode()) {
                case "Range":
                    entities.sort(Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer)));
                    break;
                case "Health":
                    entities.sort(Comparator.comparingDouble(entity -> entity.getHealth()));
                    break;
            }

            if(!allowSame && entities.size() > 1 && entities.get(0) == target) {
                return entities.get(1);
            } else {
                return entities.get(0);
            }
        }

        return null;
    }

    public boolean canAttack(EntityLivingBase entity) {
        return canAttack(entity, rotationRange.getValue());
    }

    public boolean canAttack(EntityLivingBase entity, double range) {
        if(getDistanceToEntity(entity) > range) {
            return false;
        }

        if((entity.isInvisible() || entity.isInvisibleToPlayer(mc.thePlayer)) && !invisibles.isEnabled()) {
            return false;
        }

        if(entity instanceof EntityPlayer) {
            if(!players.isEnabled() || !teamsModule.canAttack((EntityPlayer) entity)) {
                return false;
            }
        }

        if(entity instanceof EntityAnimal && !animals.isEnabled()) {
            return false;
        }

        if(entity instanceof EntityMob && !monsters.isEnabled()) {
            return false;
        }

        if(!(entity instanceof EntityPlayer || entity instanceof EntityAnimal || entity instanceof EntityMob)) {
            return false;
        }

        if(entity.isDead && !attackDead.isEnabled()) {
            return false;
        }

        if(!antibotModule.canAttack(entity, this)) {
            return false;
        }

        return true;
    }

    public double getDistanceToEntity(EntityLivingBase entity) {
        Vec3 playerVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if(speedModule.isEnabled() && speedModule.mode.is("Pathfind")) {
            playerVec = new Vec3(speedModule.getActualX(), speedModule.getActualY() + mc.thePlayer.getEyeHeight(), speedModule.getActualZ());
        }

        double yDiff = mc.thePlayer.posY - entity.posY;

        double targetY = yDiff > 0 ? entity.posY + entity.getEyeHeight() : -yDiff < mc.thePlayer.getEyeHeight() ? mc.thePlayer.posY + mc.thePlayer.getEyeHeight() : entity.posY;

        Vec3 targetVec = new Vec3(entity.posX, targetY, entity.posZ);

        return playerVec.distanceTo(targetVec) - 0.3F;
    }

    public double getDistanceCustomPosition(double x, double y, double z, double eyeHeight) {
        Vec3 playerVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        double yDiff = mc.thePlayer.posY - y;

        double targetY = yDiff > 0 ? y + eyeHeight : -yDiff < mc.thePlayer.getEyeHeight() ? mc.thePlayer.posY + mc.thePlayer.getEyeHeight() : y;

        Vec3 targetVec = new Vec3(x, targetY, z);

        return playerVec.distanceTo(targetVec) - 0.3F;
    }

    @Override
    public String getSuffix() {
        return mode.getMode();
    }

}