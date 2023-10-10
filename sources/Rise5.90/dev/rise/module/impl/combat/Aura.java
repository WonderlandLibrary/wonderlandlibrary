package dev.rise.module.impl.combat;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PostMotionEvent;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.event.impl.other.WorldChangedEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.movement.LongJump;
import dev.rise.module.impl.movement.Speed;
import dev.rise.module.impl.player.Scaffold;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.ui.clickgui.impl.ClickGUI;
import dev.rise.util.math.MathUtil;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import dev.rise.util.player.RotationUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@ModuleInfo(name = "Aura", description = "Attacks entities for you", category = Category.COMBAT)
public final class Aura extends Module {

    private final TimeUtil timer = new TimeUtil();

    public static EntityLivingBase target;

    public static float yaw, pitch, lastYaw, lastPitch, serverYaw, serverPitch;
    private float randomYaw, randomPitch, derpYaw;
    private float sinWaveTicks;
    private double targetPosX, targetPosY, targetPosZ;
    private Vec3 positionOnPlayer, lastPositionOnPlayer;

    private final double ticks = 0;
    private final long lastFrame = 0;
    private int hitTicks, cps, targetIndex;
    private boolean blocking;
    private final List<C03PacketPlayer.C04PacketPlayerPosition> packetList = new ArrayList<>();
    private boolean targetstrafe;

    // Mode options.
    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Single", "Single", "Switch", "Multi");
    private final ModeSetting rotationMode = new ModeSetting("Rotation Mode", this, "Custom", "Custom", "Custom Simple", "Custom Advanced", "Smooth", "Sin Wave", "Down", "Derp", "None");
    private final ModeSetting blockMode = new ModeSetting("Block Mode", this, "None", "None", "Fake", "Vanilla", "Bypass", "NCP", "AAC", "Interact", "Hypixel");
    private final ModeSetting sortingMode = new ModeSetting("Sorting Mode", this, "Distance", "Distance", "Health", "Hurttime");

    // General boolean options.
    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final NumberSetting range = new NumberSetting("Range", this, 3, 0, 6, 0.1);
    private final NumberSetting rotationRange = new NumberSetting("Rotation Range", this, 6, 0, 12, 0.1);
    private final NumberSetting extendedRangeNumb = new NumberSetting("Extended Range", this, 7, 6, 12, 0.1);
    private final BooleanSetting extendedRangeBool = new BooleanSetting("Extended Range", this, false);
    private final NumberSetting minCps = new NumberSetting("Min CPS", this, 8.0, 1, 20.0, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", this, 8.0, 1, 20.0, 1);
    private final NumberSetting maxTargets = new NumberSetting("Max Targets", this, 25, 2, 50, 1);

    // Bypass options.
    private final NoteSetting bypassSettings = new NoteSetting("Bypass Settings", this);
    private final NumberSetting predict = new NumberSetting("Predict", this, 0, 0, 4, 0.1);
    private final NumberSetting random = new NumberSetting("Random", this, 0, 0, 18, 0.1);
    private final NumberSetting maxRotation = new NumberSetting("Max Rot", this, 180, 1, 180, 0.1);
    private final NumberSetting minRotation = new NumberSetting("Min Rot", this, 180, 1, 180, 0.1);
    private final NumberSetting minYawRotation = new NumberSetting("Min Yaw Rot", this, 180, 1, 180, 0.1);
    private final NumberSetting maxYawRotation = new NumberSetting("Max Yaw Rot", this, 180, 1, 180, 0.1);
    private final NumberSetting minPitchRotation = new NumberSetting("Min Pitch Rot", this, 180, 1, 180, 0.1);
    private final NumberSetting maxPitchRotation = new NumberSetting("Max Pitch Rot", this, 180, 1, 180, 0.1);
    private final NumberSetting sinWaveSpeed = new NumberSetting("Sin Speed", this, 180, 1, 180, 0.1);
    private final NumberSetting derpSpeed = new NumberSetting("Derp Speed", this, 30, 1, 180, 1);
    private final BooleanSetting predictedPosition = new BooleanSetting("Predicted Position", this, false);
    private final BooleanSetting rayTrace = new BooleanSetting("Raytrace", this, false);
    private final BooleanSetting alwaysSwing = new BooleanSetting("Realistic Swings", this, false);
    private final BooleanSetting deadZone = new BooleanSetting("DeadZone", this, false);
    private final BooleanSetting throughWalls = new BooleanSetting("Through Walls", this, true);
    private final BooleanSetting silentRotations = new BooleanSetting("Silent Rotations", this, true);
    private final BooleanSetting keepSprint = new BooleanSetting("Keep Sprint", this, false);
    private final BooleanSetting onlyInAir = new BooleanSetting("Only In Air", this, false);
    private final BooleanSetting strafe = new BooleanSetting("Movement Fix", this, false);
    private final BooleanSetting newCombat = new BooleanSetting("1.9 Delay", this, false);
    private final BooleanSetting newSwing = new BooleanSetting("1.9 Swing", this, false);

    // Attacked entity types.
    private final NoteSetting targets = new NoteSetting("Targets", this);
    private final BooleanSetting players = new BooleanSetting("Players", this, true);
    private final BooleanSetting nonPlayers = new BooleanSetting("Non Players", this, true);
    private final BooleanSetting teams = new BooleanSetting("Ignore Teammates", this, true);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", this, false);
    private final BooleanSetting dead = new BooleanSetting("Attack Dead", this, false);

    // Render options.
    private final NoteSetting renderSettings = new NoteSetting("Render Settings", this);
    private final BooleanSetting targetESP = new BooleanSetting("Target ESP", this, true);
    private final BooleanSetting displayRange = new BooleanSetting("Display Range", this, false);
    private final BooleanSetting targetOnPlayer = new BooleanSetting("Target On Player", this, true);

    // Other options.
    private final NoteSetting otherSettings = new NoteSetting("Other Settings", this);
    private final BooleanSetting disableOnWorldChange = new BooleanSetting("Disable On World Change", this, true);
    private final BooleanSetting attackWithScaffold = new BooleanSetting("Attack with Scaffold", this, false);
    private final BooleanSetting attackInInterfaces = new BooleanSetting("Attack in Interfaces", this, true);
    private final BooleanSetting onClick = new BooleanSetting("On Click", this, false);


    @Override
    public void onWorldChanged(final WorldChangedEvent event) {
        if (this.disableOnWorldChange.isEnabled()) {
            this.registerNotification("Disabled " + this.getModuleInfo().name() + " due to world change.");
            this.toggleModule();
        }
    }

    @Override
    public void onUpdateAlwaysInGui() {
        /*
         * Make sure our minimum cps is always smaller than our maximum and our maximum
         * being bigger than minimum as we do not want errors with our random utilities.
         */
        if (this.maxCps.getValue() < this.minCps.getValue()) {
            maxCps.setValue(minCps.getValue());
        }

        if (this.rotationRange.getValue() < this.range.getValue()) {
            this.rotationRange.setValue(this.range.getValue());
        }

        maxTargets.hidden = !this.mode.is("Multi");

        minRotation.hidden = this.maxRotation.hidden = !this.rotationMode.is("Custom") && !this.rotationMode.is("Custom Simple");

        minYawRotation.hidden = maxYawRotation.hidden = minPitchRotation.hidden = maxPitchRotation.hidden = !this.rotationMode.is("Custom Advanced");

        sinWaveSpeed.hidden = !rotationMode.is("Sin Wave");

        derpSpeed.hidden = !rotationMode.is("Derp");

        extendedRangeNumb.hidden = !extendedRangeBool.isEnabled();

        predictedPosition.hidden = rotationMode.is("Derp") || rotationMode.is("None");

        onlyInAir.hidden = !keepSprint.isEnabled();
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ++this.hitTicks;
        if (target != null && blockMode.is("Hypixel")) {
            double playerDistance = mc.thePlayer.getDistanceToEntity(target);
            if (playerDistance <= range.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof net.minecraft.item.ItemSword)
                mc.thePlayer.setItemInUse(mc.thePlayer.getCurrentEquippedItem(), mc.thePlayer.getCurrentEquippedItem().getMaxItemUseDuration());
        }
        /* Used to determine if targetstrafe is enabled so aura doesn't override movementyaw */
        targetstrafe = this.

                getModule(TargetStrafe.class).

                isEnabled();

        handle:

        {
            /*
             * If we do not have a target that means we cannot run the aura, so we
             * can just break our label here as there is no point in going further.
             */
            if (target == null) {
                /*
                 * We want to make sure whilst we do not have a target to attack we
                 * do not break the players strafing, so we reset it every tick in here.
                 */
                if (!targetstrafe) EntityPlayer.movementYaw = null;

                /*
                 * Unblocking sword serverside if sword is blocked,
                 * this is to stop movement from flagging on some AntiCheats
                 * because the sword is still blocking and the player still runs at full speed.
                 */
                unblock();

                break handle;
            } else {
                switch (blockMode.getMode()) {
                    case "NCP":
                    case "Interact":
                        unblock();
                        break;
                }
            }

            /*
             * Whilst we have silent rotations enabled we only want the rotations to be seen server sided.
             * And whilst we have non-silent rotations we can just update our rotations manually.
             */
            if (this.silentRotations.isEnabled() && !rotationMode.is("None")) {
                if (!(PlayerUtil.isOnServer("Hypixel") && (this.getModule(Speed.class).isEnabled() || this.getModule(LongJump.class).isEnabled()))) {
                    event.setYaw(serverYaw);
                    event.setPitch(serverPitch);
                }

                mc.thePlayer.renderYawOffset = serverYaw;
                mc.thePlayer.rotationYawHead = serverYaw;
            } else {
                mc.thePlayer.rotationYaw = serverYaw;
                mc.thePlayer.rotationPitch = serverPitch;
            }

            /*
             * Gets position on player to be used for render options
             */

            final Vec3 rayCast = Objects.requireNonNull(PlayerUtil.getMouseOver(serverYaw, serverPitch, (float) range.getValue())).hitVec;
            if (rayCast == null) return;
            lastPositionOnPlayer = positionOnPlayer;
            positionOnPlayer = rayCast;
        }

    }

    @Override
    public void onPostMotion(final PostMotionEvent event) {
        if (target != null && PlayerUtil.isHoldingSword()) {
            switch (blockMode.getMode()) {
                case "NCP":
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    break;

                case "Hypixel":
                    block();
                    break;

                case "Interact":
                    mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    break;
            }
        }
    }

    @Override
    public void onUpdate(final UpdateEvent event) {
        if (!(!onClick.isEnabled() || Mouse.isButtonDown(0))) {
            target = null;
            return;
        }

        if (target == null) {
            /*
             * We want to make sure whilst we do not have a target to attack we
             * do not break the players strafing, so we reset it every tick in here.
             */
            if (!targetstrafe) EntityPlayer.movementYaw = null;

            /*
             * This unblocks the aura when its not in a situation to block, because otherwise you flag movement
             */
            unblock();

            sinWaveTicks = 0;

            return;
        }

        double ping = 250;
        ping /= 50;
        if (predictedPosition.isEnabled()) {
            final double deltaX = (target.posX - target.lastTickPosX) * 2;
            final double deltaY = (target.posY - target.lastTickPosY) * 2;
            final double deltaZ = (target.posZ - target.lastTickPosZ) * 2;
            targetPosX = target.posX + deltaX * ping;
            targetPosY = target.posY + deltaY * ping;
            targetPosZ = target.posZ + deltaZ * ping;
        } else {
            targetPosX = target.posX;
            targetPosY = target.posY;
            targetPosZ = target.posZ;
        }

        if (AutoGap.gap != -37) {
            this.unblock();
            return;
        }

        if (!rotationMode.is("Sin Wave"))
            sinWaveTicks = 0;

        if ((this.getModule(Scaffold.class).isEnabled() && !attackWithScaffold.isEnabled())
                || ((mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) && !attackInInterfaces.isEnabled())) {
            unblock();
            target = null;
            return;
        }

        /*
         * For our movement to be correctly fixed we are going to have
         * to use rotations the server actually sees instead of the
         * current ones as our rotations update per frame and this
         * will make it so our movement yaw and server yaw will
         * be different which will cause issues.
         */
        serverYaw = yaw;
        serverPitch = pitch;

        /*
         * If we want to correct our movement whilst rotating silently we can update
         * the movementYaw variable which will correct our movement to the given yaw for us.
         */
        if (this.strafe.isEnabled() && this.silentRotations.isEnabled()) EntityPlayer.movementYaw = serverYaw;
        else if (!targetstrafe) EntityPlayer.movementYaw = null;

        double delayValue = -1;

        /*
         * In the modern versions of Minecraft there is a hit delay which occurs when you hit somebody.
         * On specific items for a certain amount of ticks causes low damage until the time required has passed.
         * We have the delays set in here as an option for people who play on 1.9 and above servers.
         */
        if (this.newCombat.isEnabled()) {
            delayValue = 4;

            if (mc.thePlayer.getHeldItem() != null) {
                final Item item = mc.thePlayer.getHeldItem().getItem();

                if (item instanceof ItemSpade || item == Items.golden_axe || item == Items.diamond_axe || item == Items.wooden_hoe || item == Items.golden_hoe)
                    delayValue = 20;

                if (item == Items.wooden_axe || item == Items.stone_axe)
                    delayValue = 25;

                if (item instanceof ItemSword)
                    delayValue = 12;

                if (item instanceof ItemPickaxe)
                    delayValue = 17;

                if (item == Items.iron_axe)
                    delayValue = 22;

                if (item == Items.stone_hoe)
                    delayValue = 10;

                if (item == Items.iron_hoe)
                    delayValue = 7;
            }

            delayValue *= Math.max(1, mc.timer.timerSpeed);
        }

        boolean attack = false;

        /*
         * This is the part we actually calculate the click delay we need in order land another hit.
         * The attack boolean will be true when the time required for another attack passes.
         */
        if (this.timer.hasReached(this.cps)) {
            final int maxValue = (int) ((this.minCps.getMaximum() - this.maxCps.getValue()) * 20);
            final int minValue = (int) ((this.minCps.getMaximum() - this.minCps.getValue()) * 20);

            this.cps = (int) (randomBetween(minValue, maxValue) - MathUtil.RANDOM.nextInt(10) + MathUtil.RANDOM.nextInt(10));

            this.timer.reset();

            attack = true;
        } else if (blockMode.is("Bypass"))
            this.unblock();

        /*
         * Updates the Derp Rotation Modes yaw so that it rotates.
         */
        derpYaw += derpSpeed.getValue() - (((Math.random() - 0.5) * random.getValue()) / 2);

        if ((!newSwing.isEnabled() && attack) || (newSwing.isEnabled() && this.hitTicks > delayValue)) {
            final boolean rayCast = PlayerUtil.isMouseOver(serverYaw, serverPitch, target, (float) range.getValue()) || predictedPosition.isEnabled();
            double x = mc.thePlayer.posX;
            double z = mc.thePlayer.posZ;
            final double y = mc.thePlayer.posY;
            final double endPositionX = targetPosX;
            final double endPositionZ = targetPosZ;
            double distanceX = x - endPositionX;
            double distanceZ = z - endPositionZ;
            double distanceY = y - targetPosY;
            double distance = MathHelper.sqrt_double(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;
            if (extendedRangeBool.isEnabled()) {
                int packets = 0;

                while (distance > (range.getValue() - 0.5657) * 6.5 && packets < 100) {
                    final C03PacketPlayer.C04PacketPlayerPosition c04 = new C03PacketPlayer.C04PacketPlayerPosition(x, mc.thePlayer.posY, z, true);

                    PacketUtil.sendPacket(c04);

                    packetList.add(c04);

                    distanceX = x - endPositionX;
                    distanceZ = z - endPositionZ;
                    distanceY = y - targetPosY;
                    distance = MathHelper.sqrt_double(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ) * 6.5;

                    final double v = (x * distance + endPositionX) / (distance + 1) - x;
                    final double v1 = (z * distance + endPositionZ) / (distance + 1) - z;
                    Rise.addChatMessage(MathHelper.sqrt_double(v * v + v1 * v1));

                    x = (x * distance + endPositionX) / (distance + 1);
                    z = (z * distance + endPositionZ) / (distance + 1);

                    packets++;
                }
            }

            /*
             * Whilst the time required to attack an entity again has passed and our raytrace failed
             * when this setting enabled the client will swing even tho it cannot see it causing more
             * realistic swinging during pvp which could help making the aura less detectable.
             */
            if ((mc.thePlayer.getDistance(targetPosX, targetPosY, targetPosZ) - 0.5657 > ((this.extendedRangeBool.isEnabled()) ? this.extendedRangeNumb.getValue() : this.range.getValue()) && !rayCast)
                    || (this.rayTrace.isEnabled() && !rayCast)) {
                if (this.alwaysSwing.isEnabled()) {
                    PacketUtil.sendPacket(new C0APacketAnimation());
                    return;
                }
            }

            /*
             * We want to make sure the target is actually on our attack distance and not only our rotation distance.
             * Plus if raytrace is enabled we shall make sure there is an intersection.
             */
            if (mc.thePlayer.getDistance(targetPosX, targetPosY, targetPosZ) - 0.5657 > ((this.extendedRangeBool.isEnabled()) ? this.extendedRangeNumb.getValue() : this.range.getValue())
                    || (this.rayTrace.isEnabled() && !rayCast)) return;

            /*
             * If we are not allowed to hit through walls we should not
             * attack the entity by checking if we can see them or not.
             */
            if (!this.throughWalls.isEnabled() && !mc.thePlayer.canEntityBeSeen(target)) return;

            /*
             * On the legacy versions of Minecraft the player before sending an interaction
             * packet sends a swing packet. Which is not the case on newer versions.
             */
            if (!this.newSwing.isEnabled()) mc.thePlayer.swingItem();

            /*
             * When keep sprint is disabled to keep everything vanilla about
             * movement we can use the games attack method to keep everything vanilla.
             */

            switch (this.blockMode.getMode()) {
                case "AAC":
                case "Interact": {
                    this.unblock();
                    break;
                }
            }


            switch (mode.getMode()) {
                case "Single": {
                    /*
                     * Calls attack event so other modules can use information from the entity
                     * When the C02 packet is sent the attack event does not
                     * get called, so we have to manually call it ourselves in here.
                     */
                    final AttackEvent attackEvent = new AttackEvent(target);
                    attackEvent.call();

                    if (attackEvent.isCancelled())
                        return;

                    if (this.keepSprint.isEnabled() && (!mc.thePlayer.onGround || !onlyInAir.isEnabled())) {
                        PacketUtil.sendPacket(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                    } else {
                        mc.playerController.attackEntity(mc.thePlayer, target);
                    }

                    if (mc.thePlayer.fallDistance > 0) mc.thePlayer.onCriticalHit(target);
                    break;
                }

                case "Switch": {
                    final List<EntityLivingBase> entities = getTargets();

                    if (entities.size() >= targetIndex)
                        targetIndex = 0;

                    if (entities.isEmpty()) {
                        targetIndex = 0;
                        return;
                    }

                    final EntityLivingBase entity = entities.get(targetIndex);

                    /*
                     * Calls attack event so other modules can use information from the entity
                     * When the C02 packet is sent the attack event does not
                     * get called, so we have to manually call it ourselves in here.
                     */
                    final AttackEvent attackEvent = new AttackEvent(entity);
                    attackEvent.call();

                    if (attackEvent.isCancelled())
                        return;

                    if (this.keepSprint.isEnabled() && (!mc.thePlayer.onGround || !onlyInAir.isEnabled())) {
                        PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                    } else {
                        mc.playerController.attackEntity(mc.thePlayer, entity);
                    }

                    if (mc.thePlayer.fallDistance > 0) mc.thePlayer.onCriticalHit(target);

                    targetIndex++;
                    break;
                }

                case "Multi": {
                    for (final EntityLivingBase entity : getTargets()) {
                        /*
                         * Calls attack event so other modules can use information from the entity
                         * When the C02 packet is sent the attack event does not
                         * get called, so we have to manually call it ourselves in here.
                         */
                        final AttackEvent attackEvent = new AttackEvent(target);
                        attackEvent.call();

                        if (attackEvent.isCancelled())
                            return;

                        if (this.keepSprint.isEnabled() && (!mc.thePlayer.onGround || !onlyInAir.isEnabled())) {
                            PacketUtil.sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                        } else {
                            mc.playerController.attackEntity(mc.thePlayer, entity);
                        }

                        if (mc.thePlayer.fallDistance > 0) mc.thePlayer.onCriticalHit(entity);
                    }
                    break;
                }
            }

            /*
             *
             */
            if (extendedRangeBool.isEnabled()) {
                Collections.reverse(packetList);
                packetList.forEach(PacketUtil::sendPacket);
                packetList.clear();
            }

            /*
             * On the modern versions of Minecraft unlike legacy the player
             * sends an arm swing after they interact with the object.
             */
            if (this.newSwing.isEnabled()) mc.thePlayer.swingItem();

            // Resetting the hit ticks
            this.hitTicks = 0;
        }

        if (PlayerUtil.isHoldingSword() && !Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Scaffold")).isEnabled()) {
            switch (this.blockMode.getMode()) {
                case "AAC": {
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }
                    break;
                }

                case "Bypass":
                case "Vanilla": {
                    PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    break;
                }
            }
        }
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {
        this.update();

        if (this.targetESP.isEnabled()) {
            switch (mode.getMode()) {
                case "Single":
                case "Switch": {
                    if (target != null) {
                        drawCircle(target, 0.67, Rise.CLIENT_THEME_COLOR, true);
                    }
                    break;
                }

                case "Multi": {
                    for (final EntityLivingBase entity : getTargets()) {
                        drawCircle(entity, 0.67, Rise.CLIENT_THEME_COLOR, true);
                    }
                    break;
                }
            }
        }

        if (positionOnPlayer != null && lastPositionOnPlayer != null && target != null && targetOnPlayer.isEnabled()) {
            Vec3 interpolatedPosition = new Vec3(
                    (positionOnPlayer.xCoord - lastPositionOnPlayer.xCoord) * mc.timer.renderPartialTicks + lastPositionOnPlayer.xCoord,
                    (positionOnPlayer.yCoord - lastPositionOnPlayer.yCoord) * mc.timer.renderPartialTicks + lastPositionOnPlayer.yCoord,
                    (positionOnPlayer.zCoord - lastPositionOnPlayer.zCoord) * mc.timer.renderPartialTicks + lastPositionOnPlayer.zCoord);
            RenderUtil.renderBreadCrumb(interpolatedPosition);
        }

        if (this.displayRange.isEnabled()) {
            this.drawCircle(mc.thePlayer, this.range.getValue() - 1);
        }
    }

    @Override
    protected void onEnable() {
        /*
         * For the first rotation to be properly rounded we can set our last
         * rotations to our current rotations in order to round everything properly.
         */
        lastYaw = mc.thePlayer.rotationYaw;
        lastPitch = mc.thePlayer.rotationPitch;
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;

        sinWaveTicks = 0;

        /*
         * Sets blocking variable, so we can use this to not send extra blocking packets
         */
        blocking = mc.gameSettings.keyBindUseItem.isKeyDown();
    }

    @Override
    protected void onDisable() {
        // We do not want to strafe whilst not using aura.
        if (!targetstrafe) EntityPlayer.movementYaw = null;

        // Resetting the target index for Switch.
        targetIndex = 0;

        // Reset our timer for our attacks.
        timer.reset();

        // Set our target null as we do not want other stuff thinking we are attacking something.
        target = null;

        // This will only unblock if you're already blocking
        unblock();
    }

    /**
     * The update method is used to grab the target we want to attack
     * based on given settings and update the rotations and the last rotations.
     */
    private void update() {
        if ((this.getModule(Scaffold.class).isEnabled() && !attackWithScaffold.isEnabled())
                || ((mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) && !attackInInterfaces.isEnabled())) {
            unblock();
            target = null;
            return;
        }

        // Update our target for the aura as we want the entity we want to attack right now.
        this.updateTarget();

        /*
         * If the aura could not find a target on the specified settings
         * we cannot grab rotations or attack anything so we can return.
         */
        if (target == null) {
            lastYaw = mc.thePlayer.rotationYaw;
            lastPitch = mc.thePlayer.rotationPitch;
        } else {
            /*
             * Because we have found a target successfully we can grab the
             * required rotations to look and actually attack this target.
             */
            this.updateRotations();
        }
    }

    private void block() {
        sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
        mc.gameSettings.keyBindUseItem.pressed = true;
        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
        blocking = true;
    }

    private void unblock() {
        if (blocking) {
            mc.gameSettings.keyBindUseItem.pressed = false;
            PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            blocking = false;
        }
    }

    public void sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
        if (!(mc.playerController.currentGameType == WorldSettings.GameType.SPECTATOR)) {
            mc.playerController.syncCurrentPlayItem();
            int i = itemStackIn.stackSize;
            ItemStack itemstack = itemStackIn.useItemRightClick(worldIn,
                    playerIn);

            if (itemstack != itemStackIn || itemstack.stackSize != i) {
                playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = itemstack;

                if (itemstack.stackSize == 0) {
                    playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
                }

            }
        }
    }

    private void updateRotations() {
        /*
         * Update our last rotations as the current ones as we are updating
         * the current ones soon. We require the last rotations to smooth
         * out the current rotations properly based on the last rotations.
         */
        lastYaw = yaw;
        lastPitch = pitch;

        /*
         * Finally grab the required rotations to actually aim at the target.
         * We do not need to pass in any parameters as the method already grabs the settings for us.
         */
        final float[] rotations = this.getRotations();

        /*
         * We can now update the rotation fields for the aura so the client
         * can send the server the rotations we actually want to apply.
         */
        yaw = rotations[0];
        pitch = rotations[1];

        if (deadZone.isEnabled()) {
            if (rayTrace(lastYaw, lastPitch, rotationRange.getValue(), target)) {
                yaw = lastYaw;
                pitch = lastPitch;
            }
        }
    }

    private float[] getRotations() {
        final double predictValue = predict.getValue();

        final double x = (targetPosX - (target.lastTickPosX - targetPosX) * predictValue) + 0.01 - mc.thePlayer.posX;
        final double z = (targetPosZ - (target.lastTickPosZ - targetPosZ) * predictValue) - mc.thePlayer.posZ;

        double minus = (mc.thePlayer.posY - targetPosY);

        if (minus < -1.4) minus = -1.4;
        if (minus > 0.1) minus = 0.1;

        final double y = (targetPosY - (target.lastTickPosY - targetPosY) * predictValue) + 0.4 + target.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight()) + minus;

        final double xzSqrt = MathHelper.sqrt_double(x * x + z * z);

        float yaw = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(z, x)) - 90.0F);
        float pitch = MathHelper.wrapAngleTo180_float((float) Math.toDegrees(-Math.atan2(y, xzSqrt)));

        final double randomAmount = random.getValue();

        if (randomAmount != 0) {
            randomYaw += ((Math.random() - 0.5) * randomAmount) / 2;
            randomYaw += ((Math.random() - 0.5) * randomAmount) / 2;
            randomPitch += ((Math.random() - 0.5) * randomAmount) / 2;

            if (mc.thePlayer.ticksExisted % 5 == 0) {
                randomYaw = (float) (((Math.random() - 0.5) * randomAmount) / 2);
                randomPitch = (float) (((Math.random() - 0.5) * randomAmount) / 2);
            }

            yaw += randomYaw;
            pitch += randomPitch;
        }

        final int fps = (int) (Minecraft.getDebugFPS() / 20.0F);

        switch (this.rotationMode.getMode()) {
            case "Custom": {
                if (this.maxRotation.getValue() != 180.0F && this.minRotation.getValue() != 180.0F) {
                    final float distance = (float) randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());

                    final float deltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
                    final float deltaPitch = pitch - lastPitch;

                    final float distanceYaw = MathHelper.clamp_float(deltaYaw, -distance, distance) / fps * 4;
                    final float distancePitch = MathHelper.clamp_float(deltaPitch, -distance, distance) / fps * 4;

                    yaw = MathHelper.wrapAngleTo180_float(lastYaw) + distanceYaw;
                    pitch = MathHelper.wrapAngleTo180_float(lastPitch) + distancePitch;
                }
                break;
            }

            case "Custom Simple": {
                final float yawDistance = (float) randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());
                final float pitchDistance = (float) randomBetween(this.minRotation.getValue(), this.maxRotation.getValue());


                final float deltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
                final float deltaPitch = pitch - lastPitch;

                final float distanceYaw = MathHelper.clamp_float(deltaYaw, -yawDistance, yawDistance) / fps * 4;
                final float distancePitch = MathHelper.clamp_float(deltaPitch, -pitchDistance, pitchDistance) / fps * 4;

                yaw = lastYaw + distanceYaw;
                pitch = lastPitch + distancePitch;
                break;
            }

            case "Custom Advanced": {
                final float advancedYawDistance = (float) randomBetween(this.minYawRotation.getValue(), this.maxYawRotation.getValue());
                final float advancedPitchDistance = (float) randomBetween(this.minPitchRotation.getValue(), this.maxPitchRotation.getValue());

                final float advancedDeltaYaw = (((yaw - lastYaw) + 540) % 360) - 180;
                final float advancedDeltaPitch = pitch - lastPitch;

                final float advancedDistanceYaw = MathHelper.clamp_float(advancedDeltaYaw, -advancedYawDistance, advancedYawDistance) / fps * 4;
                final float advancedDistancePitch = MathHelper.clamp_float(advancedDeltaPitch, -advancedPitchDistance, advancedPitchDistance) / fps * 4;

                yaw = lastYaw + advancedDistanceYaw;
                pitch = lastPitch + advancedDistancePitch;
                break;
            }

            case "Smooth": {
                final float yawDelta = (float) (((((yaw - lastYaw) + 540) % 360) - 180) / (fps / 3 * (1 + Math.random())));
                final float pitchDelta = (float) ((pitch - lastPitch) / (fps / 3 * (1 + Math.random())));

                yaw = lastYaw + yawDelta;
                pitch = lastPitch + pitchDelta;

                break;
            }

            case "Down": {
                pitch = RandomUtils.nextFloat(89, 90);
                break;
            }

            case "Derp": {
                pitch = RandomUtils.nextFloat(89, 90);
                yaw = derpYaw;
                break;
            }

            case "Sin Wave": {
                final float halal = (float) (Math.abs(Math.sin((sinWaveTicks + Math.random() * 0.001) / 10)) * sinWaveSpeed.getValue());

                final float sinWaveYaw = MathHelper.clamp_float((((yaw - lastYaw) + 540) % 360) - 180, -halal, halal) / fps;
                final float sinWavePitch = MathHelper.clamp_float(pitch - lastPitch, -halal, halal) / fps / fps;

                yaw = lastYaw + sinWaveYaw;
                pitch = lastPitch + sinWavePitch;

                sinWaveTicks++;
                break;
            }
        }

        final float[] rotations = new float[]{yaw, pitch};
        final float[] lastRotations = new float[]{Aura.yaw, Aura.pitch};

        final float[] fixedRotations = RotationUtil.getFixedRotation(rotations, lastRotations);

        yaw = fixedRotations[0];
        pitch = fixedRotations[1];

        if (this.rotationMode.is("None")) {
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.rotationPitch;
        }

        pitch = MathHelper.clamp_float(pitch, -90.0F, 90.0F);

        return new float[]{yaw, pitch};
    }

    private List<EntityLivingBase> getTargets() {
        final List<EntityLivingBase> entities = mc.theWorld.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (entity instanceof EntityPlayer && !players.isEnabled()) return false;

                    if (!(entity instanceof EntityPlayer) && !nonPlayers.isEnabled()) return false;

                    if (entity.isInvisible() && !invisibles.isEnabled()) return false;

                    if (PlayerUtil.isOnSameTeam(entity) && teams.isEnabled()) return false;

                    if (entity.isDead && !dead.isEnabled()) return false;

                    if (entity.deathTime != 0 && !dead.isEnabled()) return false;

                    if (entity.ticksExisted < 2) return false;

                    if (AntiBot.bots.contains(entity)) return false;

                    if (entity instanceof EntityPlayer) {
                        final EntityPlayer player = ((EntityPlayer) entity);

                        for (final String name : instance.getFriends()) {
                            if (name.equalsIgnoreCase(player.getGameProfile().getName()))
                                return false;
                        }
                    }

                    return mc.thePlayer != entity;
                })

                // Do a proper distance calculation to get entities we can reach.
                .filter(entity -> {
                    // DO NOT TOUCH THIS VALUE ITS CALCULATED WITH MATH
                    final double girth = 0.5657;

                    // See if the other entity is in our range.
                    return mc.thePlayer.getDistanceToEntity(entity) - girth < rotationRange.getValue();
                })

                // Sort out potential targets with the algorithm provided as a setting.
                .sorted(Comparator.comparingDouble(entity -> {
                    switch (sortingMode.getMode()) {
                        case "Distance":
                            return mc.thePlayer.getDistanceSqToEntity(entity);
                        case "Health":
                            return entity.getHealth();
                        case "Hurttime":
                            return entity.hurtTime;

                        default:
                            return -1;
                    }
                }))

                // Sort out all the specified targets.
                .sorted(Comparator.comparing(entity -> entity instanceof EntityPlayer && !Rise.INSTANCE.getTargets().contains(((EntityPlayer) entity).getName())))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());

        // Removes entities when there are too many targets
        final int maxTargets = (int) Math.round(this.maxTargets.getValue());

        if (mode.is("Multi") && entities.size() > maxTargets) {
            entities.subList(maxTargets, entities.size()).clear();
        }

        // Returns the list of entities
        return entities;
    }

    private void updateTarget() {
        final List<EntityLivingBase> entities = getTargets();

        // Grab our best option from the list.
        target = entities.size() > 0 ? entities.get(0) : null;
    }

    public double randomBetween(final double min, final double max) {
        return min + (MathUtil.RANDOM.nextDouble() * (max - min));
    }

    private boolean rayTrace(final float yaw, final float pitch, final double reach, final Entity target) {
        final Vec3 vec3 = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
        final Vec3 vec31 = mc.thePlayer.getVectorForRotation(MathHelper.clamp_float(pitch, -90.F, 90.F), yaw % 360);
        final Vec3 vec32 = vec3.addVector(vec31.xCoord * reach, vec31.yCoord * reach, vec31.zCoord * reach);

        final MovingObjectPosition objectPosition = target.getEntityBoundingBox().calculateIntercept(vec3, vec32);

        return (objectPosition != null && objectPosition.hitVec != null);
    }

    private void drawCircle(final Entity entity, final double rad, final int color, final boolean shade) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3153, 4354);
        GL11.glDepthMask(false);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        if (shade) GL11.glShadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableCull();
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosX;
        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosY) + Math.sin(System.currentTimeMillis() / 2E+2) + 1;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - (mc.getRenderManager()).renderPosZ;

        Color c;

        for (float i = 0; i < Math.PI * 2; i += Math.PI * 2 / 64.F) {
            final double vecX = x + rad * Math.cos(i);
            final double vecZ = z + rad * Math.sin(i);

            c = ThemeUtil.getThemeColor(i, ThemeType.GENERAL);

            if (shade) {
                GL11.glColor4f(c.getRed() / 255.F,
                        c.getGreen() / 255.F,
                        c.getBlue() / 255.F,
                        0
                );
                GL11.glVertex3d(vecX, y - Math.cos(System.currentTimeMillis() / 2E+2) / 2.0F, vecZ);
                GL11.glColor4f(c.getRed() / 255.F,
                        c.getGreen() / 255.F,
                        c.getBlue() / 255.F,
                        0.85F
                );
            }
            GL11.glVertex3d(vecX, y, vecZ);
        }

        GL11.glEnd();
        if (shade) GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableCull();
        GL11.glDisable(2848);
        GL11.glDisable(2848);
        GL11.glEnable(2832);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glColor3f(255, 255, 255);
    }

    private void drawCircle(final Entity entity, final double rad) {
        GL11.glDisable(3553);
        GL11.glLineWidth(1f);
        GL11.glBegin(3);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
        final double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY) + mc.thePlayer.getEyeHeight() - 0.7;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;

        for (int i = 0; i <= 90; ++i) {
            RenderUtil.color(new Color(Rise.CLIENT_THEME_COLOR));

            GL11.glVertex3d(x + rad * Math.cos(i * 6.283185307179586 / 45.0), y, z + rad * Math.sin(i * 6.283185307179586 / 45.0));
        }

        GL11.glEnd();
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        RenderUtil.color(Color.WHITE);
    }
}