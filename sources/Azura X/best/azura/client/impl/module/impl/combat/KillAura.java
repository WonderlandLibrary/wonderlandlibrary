package best.azura.client.impl.module.impl.combat;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.*;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.core.EventPriority;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.friend.Friend;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.module.impl.movement.Sprint;
import best.azura.client.impl.module.impl.other.AutoHypixel;
import best.azura.client.impl.module.impl.other.ClientModule;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.math.MathUtil;
import best.azura.client.util.other.DelayUtil;
import best.azura.client.util.other.ServerUtil;
import best.azura.client.util.math.CustomVec3;
import best.azura.client.util.pathing.Path;
import best.azura.client.util.pathing.PathUtil;
import best.azura.client.util.player.RaytraceUtil;
import best.azura.client.util.player.RotationUtil;
import best.azura.client.util.other.ItemUtil;
import best.azura.client.util.render.Line3D;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.*;
import best.azura.client.impl.value.dependency.BooleanDependency;
import best.azura.client.impl.value.dependency.ModeDependency;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.util.*;
import viamcp.ViaMCP;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings({"unused", "CommentedOutCode"})
@ModuleInfo(name = "Kill Aura", description = "Attacks entities around you automatically.", category = Category.COMBAT)
public class KillAura extends Module {

    private final ModeValue auraMode = new ModeValue("Aura Mode", "Switch aura modes", (val) -> {
        if (val != this.auraMode) return;
        if (val.getObject().equals("TP")) {
            this.legit.setObject(false);
            this.range.setMax(100.0);
            this.wallRange.setMax(100.0);
            this.blockRangeValue.setMax(100.0);
        } else {
            this.range.setMax(6.0);
            this.wallRange.setMax(6.0);
            this.blockRangeValue.setMax(10.0);
        }
        this.range.setObject(this.range.getObject());
        this.wallRange.setObject(this.wallRange.getObject());
        this.blockRangeValue.setObject(this.blockRangeValue.getObject());
    }, "Normal", "Normal", "TP");

    private final NumberValue<Double> tpOffset = new NumberValue<>("TP Offset", null, () -> auraMode.getObject().equals("TP"), 1.0, 0.1, 0.1, 10.0);
    private final ModeValue tpAuraMode = new ModeValue("TP Aura Mode", "Switch tp aura bypass", () -> auraMode.getObject().equals("TP"), "Normal", "Normal", "Rede Dark", "Round Y");
    private final BooleanValue tpGround = new BooleanValue("TP Ground", null, () -> auraMode.getObject().equals("TP"), false);
    private final BooleanValue legit = new BooleanValue("Click mouse", "Uses simulated mouse clicks to seem as legit as possible.", false);
    private final BooleanValue rayCast = new BooleanValue("Ray Cast", "Cast a ray from the player to get the closest entity", false);
    private final BooleanValue switchTarget = new BooleanValue("Switch target", "Make the aura switch between targets" + ".", true);
    private final NumberValue<Double> maxTargets = new NumberValue<>("Max targets", "Maximum amount of targets.", new BooleanDependency(switchTarget, false), 1.0, 1.0, 1.0, 10.0);
    private final ModeValue priorityMode = new ModeValue("Priority Mode", "Select the target selection priority", "Distance", "Distance", "Health", "HurtTime", "VampHealth");
    private final NumberValue<Double> range = new NumberValue<>("Range", "Select the range for attacking.", 3.0, 0.1, 1.0, 6.0);
    private final NumberValue<Double> wallRange = new NumberValue<>("Wall Range", "Select the range for attacking through walls.", 2.0, 0.1, 1.0, 6.0);
    private final NumberValue<Double> blockRangeValue = new NumberValue<>("Block Range", "Select the range for " + "blocking.", () -> !this.blockMode.getObject().equals("None"), 3.0, 0.1, 1.0, 10.0);
    private final ModeValue delayMode = new ModeValue("Delay Mode", "Delay type", "CPS", "CPS", "Hurt Time", "Tick");
    public final NumberValue<Double> min = new NumberValue<>("Min CPS", "Select the minimum amount of clicks per " + "second.", () -> {
        this.min.setObject(Math.min(this.max.getObject(), this.min.getObject()));
        return delayMode.getObject().equals("CPS");
    }, 12.0, 0.1, 1.0, 20.0);
    public final NumberValue<Double> max = new NumberValue<>("Max CPS", "Select the maximum amount of clicks per " + "second.", (val) -> {
        if (val != this.max) return;
        min.setObject(Math.min(min.getObject(), (Double) val.getObject()));
    }, () -> delayMode.getObject().equals("CPS"), 12.0, 0.1, 1.0, 20.0);
    private final NumberValue<Integer> ticks = new NumberValue<>("Ticks", "Delay ticks", () -> delayMode.getObject().equals("Tick") || delayMode.getObject().equals("Hurt Time"), 10, 1, 1, 10);
    private final ModeValue blockMode = new ModeValue("Auto Block", "Select the type of autoblock", "Watchdog HvH", "None", "Watchdog", "Watchdog HvH", "Watchdog New", "Spam Click", "Fake", "NCP", "AAC3", "Vanilla");
    private final ModeValue attackMode = new ModeValue("Attack Event", "Select the event in which you attack", "Pre", "Pre", "Post", "Random", "Mixed");

    private final ModeValue gcd = new ModeValue("GCD", "Fix sensitivity (some anticheats detect if you dont have this on)", "None", "None", "Fake", "Real");
    private final ModeValue customMode = new ModeValue("Location", "Choose where to aim.", "Center", "Center", "Closest", "Random", "Reduce", "NCP", "None");
    private final NumberValue<Double> height = new NumberValue<>("Height", "Select the height to aim at.", new ModeDependency(customMode, "Center"), 0.75, 0.05, 0.1, 0.9);
    private final NumberValue<Double> angleStep = new NumberValue<>("Angle Step", "Select the Angle Step.", 45., 1., 1., 180.);
    private final BooleanValue silentAim = new BooleanValue("Silent Rotations", "Silently set your rotations", true);
    private final BooleanValue predict = new BooleanValue("Predict", "Predict the targets movements", false);
    private final NumberValue<Double> modifier = new NumberValue<>("Modifier", "Select the modifier of the predict.", new BooleanDependency(predict, true), 0.1, 0.01, 0.05, 0.9);
    private final BooleanValue jitter = new BooleanValue("Jitter", "Jitter when target an entity.", false);
    private final NumberValue<Double> jitterValue = new NumberValue<>("Jitter strength", "Select the amount of jitter.", new BooleanDependency(jitter, true), 1.0, 0.1, 0.5, 10.0);
    private final BooleanValue legitJitter = new BooleanValue("Legit jitter", "Make the jitter look a lot more legit.", new BooleanDependency(jitter, true), false);

    private final BooleanValue movementFix = new BooleanValue("Move fix", "Move to your rotation.", false);
    private final BooleanValue silent = new BooleanValue("Silent move fix", "Move to your rotation silently.", new BooleanDependency(movementFix, true), false);
    private final BooleanValue sprintFix = new BooleanValue("Sprint fix", "Prevents getting flagged for keep sprint.", false);

    private final BooleanValue targetEsp = new BooleanValue("Target ESP", "Marks the target using a fancy animation.", true);
    private final BooleanValue useBox = new BooleanValue("Use box", "Marks the target using a box.", false);

    private final ColorValue colorValue = new ColorValue("Color", "Change the color of the target esp.", () -> !useBox.getObject() && targetEsp.getObject(), new HSBColor(1f, 1f, 0f));
    private final NumberValue<Double> lengthValue = new NumberValue<>("Length", "Change the length of the target esp.", () -> !useBox.getObject() && targetEsp.getObject(), 0.06, 0.01, 0.01, 0.3);
    private final BooleanValue renderPaths = new BooleanValue("Render Paths", "Renders the path(s) of the tp aura mode.", false);

    private final BooleanValue checkPitSpawn = new BooleanValue("Check Pit Spawn", "Checks if an entity is at spawn when you play hypixel pit.", () -> ServerUtil.isHypixel() && AutoHypixel.getCurrentGameMode().equals("Pit"), false);
    private final ComboValue targetsCombo = new ComboValue("Targets", "Define targets for the killaura.",
            new ComboSelection("Players", true),
            new ComboSelection("Monsters", false), new ComboSelection("Animals", false),
            new ComboSelection("Teams", false), new ComboSelection("Invisible", false), new ComboSelection("Client-User", false));
    private final BooleanValue checkPitTeams = new BooleanValue("Check Pit Teams", "Checks for your team mates when you play hypixel pit", () -> ServerUtil.isHypixel() && AutoHypixel.getCurrentGameMode().equals("Pit") && targetsCombo.isSelected("Teams"), false);
    private final ComboValue antiBotCombo = new ComboValue("Anti Bot", "Check for bots and not attack them", new ComboSelection("Matrix", false),
            new ComboSelection("Watchdog Staff", false));
    private final BooleanValue autoWeapon = new BooleanValue("Auto Weapon", "Automatically switch to strongest weapon", false);
    private final NumberValue<Integer> ticksValue = new NumberValue<>("Ticks existed", "Minimum amount of entities to have existed before being attacked.", 0, 0, 500);
    private double prevAnimation;
    private double animation;
    private final ArrayList<Path> paths = new ArrayList<>();
    private float yaw, pitch, prevYaw, lastYaw, lastPitch;
    private double nextDelay = 0, legitJitterDelay = 0;
    private boolean up = false, sprint;
    public boolean block, disableNoSlow, disable;
    private final DelayUtil delayUtil = new DelayUtil(), legitJitterDelayUtil = new DelayUtil();
    public final ArrayList<Entity> targets = new ArrayList<>(), bots = new ArrayList<>();
    private final ArrayList<Packet<?>> packets = new ArrayList<>();
    private int blockState;

    @EventHandler
    public final Listener<EventSentPacket> eventSentPacketListener = e -> {
        if (block && blockMode.getObject().equals("Watchdog New") &&
                e.getPacket() instanceof C03PacketPlayer && !e.isCancelled() && !packets.contains(e.getPacket())) {
            packets.add(e.getPacket());
            e.setCancelled(true);
        }
    };

    public KillAura() {
        super();
        Client.INSTANCE.getEventBus().register(new AntiBot());
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;
        targets.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Packet<?> packet : packets)
            mc.thePlayer.sendQueue.addToSendQueue(packet);
        packets.clear();
        blockState = 0;
        targets.clear();
        paths.clear();
        if (block) mc.gameSettings.keyBindUse.pressed = false;
        block = disableNoSlow = false;
        Sprint.disable = false;
    }

    @EventHandler(EventPriority.HIGHEST)
    public final Listener<EventClickMouse> eventClickMouseListener = e -> {
        if ((blockMode.getObject().equals("Watchdog HvH") || blockMode.getObject().equals("Watchdog New")) && shouldBlock()) {
            final double dist = (targets.isEmpty() ? 1 : mc.thePlayer.getDistanceToEntity(targets.get(0)));
            final Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f), vec31 = mc.thePlayer.getLook(1.0f),
                    vec32 = vec3.addVector(vec31.xCoord * dist, vec31.yCoord * dist, vec31.zCoord * dist);
            e.setObjectMouseOver(new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec32, null, new BlockPos(vec32)));
        }
    };


    @EventHandler(EventPriority.HIGHEST)
    public final Listener<EventRender3D> render3DListener = e -> {
        if (auraMode.getObject().equals("TP") && renderPaths.getObject()) {
            glEnable(GL_BLEND);
            glEnable(GL_LINE_SMOOTH);
            glDisable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            glLineWidth(1.0f);
            for (Path path : paths) {
                CustomVec3 last = null;
                for (CustomVec3 vec3 : path.getAdditionalVectors()) {
                    if (last != null) {
                        glColor4d(1, 1, 1, 1);
                        final double x = last.getX() - RenderManager.renderPosX,
                                y = last.getY() - RenderManager.renderPosY,
                                z = last.getZ() - RenderManager.renderPosZ,
                                x1 = vec3.getX() - RenderManager.renderPosX,
                                y1 = vec3.getY() - RenderManager.renderPosY,
                                z1 = vec3.getZ() - RenderManager.renderPosZ;
                        glBegin(GL_LINE_LOOP);
                        glVertex3d(x, y, z);
                        glVertex3d(x1, y1, z1);
                        glEnd();
                    }
                    last = vec3;
                }
            }
            glDisable(GL_BLEND);
            glDisable(GL_LINE_SMOOTH);
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);
            GlStateManager.resetColor();
        }

        if (targetEsp.getObject()) {
            if (useBox.getObject()) {
                for (Entity entity : targets) {
                    final double addedX = predict.getObject() ? (entity.posX - entity.lastTickPosX) * modifier.getObject() : 0,
                            addedY = predict.getObject() ? (entity.posY - entity.lastTickPosY) * modifier.getObject() : 0,
                            addedZ = predict.getObject() ? (entity.posZ - entity.lastTickPosZ) * modifier.getObject() : 0;
                    final double x = entity.lastTickPosX + addedX + (entity.posX - entity.lastTickPosX)
                            * mc.timer.renderPartialTicks / 2 - RenderManager.renderPosX,
                            y = entity.lastTickPosY + addedY + (entity.posY - entity.lastTickPosY) *
                                    mc.timer.renderPartialTicks / 2 - RenderManager.renderPosY,
                            z = entity.lastTickPosZ + addedZ + (entity.posZ - entity.lastTickPosZ) *
                                    mc.timer.renderPartialTicks / 2 - RenderManager.renderPosZ;
                    if (entity instanceof EntityLivingBase) {
                        EntityLivingBase b = (EntityLivingBase) entity;
                        float interpolated = b.prevRotationYawHead +
                                (b.rotationYawHead - b.prevRotationYawHead) *
                                        mc.timer.renderPartialTicks;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-interpolated, 0, 1, 0);
                        GlStateManager.translate(-x, -y, -z);
                    }
                    RenderUtil.INSTANCE.renderBox(x, y, z, entity.width / 2,
                            entity.height, new Color(208, 38, 38, 100), true);
                    if (entity instanceof EntityLivingBase) {
                        EntityLivingBase b = (EntityLivingBase) entity;
                        float interpolated = b.prevRotationYawHead +
                                (b.rotationYawHead - b.prevRotationYawHead) *
                                        mc.timer.renderPartialTicks;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(interpolated, 0, 1, 0);
                        GlStateManager.translate(-x, -y, -z);
                    }
                    glColor4d(1, 1, 1, 1);
                }
            } else {
                for (Entity target : targets) {
                    double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
                    double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
                    double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                    double interA = prevAnimation + (animation - prevAnimation) * mc.timer.renderPartialTicks;
                    double a = (Math.sin(interA * 1.570796 * 4) + 1) / 2;
                    Color color = colorValue.getObject().getColor();
                    glEnable(GL_BLEND);
                    glDisable(GL_DEPTH_TEST);
                    glDisable(GL_TEXTURE_2D);
                    glLineWidth(1.0f);

                    double a2 = (Math.sin((animation - lengthValue.getObject() * 0) * 1.570796 * 4) + 1) / 2;
                    double v2 = y + (target.height * (a2 + 2 * (1 / Math.max(0.1, target.height) * 0.1)));
                    double a3 = (Math.sin((animation - lengthValue.getObject() * 0.9) * 1.570796 * 4) + 1) / 2;
                    double v3 = y + (target.height * (a3 + 2 * (1 / Math.max(0.1, target.height) * 0.1)));

                    glShadeModel(GL_SMOOTH);
                    RenderUtil.INSTANCE.color(color.getRGB());
                    glBegin(GL_TRIANGLE_STRIP);
                    for (double i = -25; i <= 25; i++) {
                        final double angle = Math.PI * i / 25, cos = Math.cos(angle) * target.width, sin = Math.sin(angle) * target.width;
                        RenderUtil.INSTANCE.color(new Color(color.getRed(), color.getGreen(), color.getBlue(), 10).getRGB());
                        glVertex3d(x + sin, v3, z + cos);
                        RenderUtil.INSTANCE.color(color);
                        glVertex3d(x + sin, v2, z + cos);
                        RenderUtil.INSTANCE.color(new Color(color.getRed(), color.getGreen(), color.getBlue(), 10).getRGB());
                        glVertex3d(x + sin, v3, z + cos);
                    }
                    for (double i = -25; i <= 25; i++) {
                        final double angle = Math.PI * (i + 1) / 25, cos = Math.cos(angle) * target.width, sin = Math.sin(angle) * target.width;
                        RenderUtil.INSTANCE.color(color);
                        glVertex3d(x + sin, v2, z + cos);
                        RenderUtil.INSTANCE.color(new Color(color.getRed(), color.getGreen(), color.getBlue(), 10).getRGB());
                        glVertex3d(x + sin, v3, z + cos);
                        RenderUtil.INSTANCE.color(color);
                        glVertex3d(x + sin, v2, z + cos);
                    }
                    glEnd();
                    glShadeModel(GL_FLAT);

                    glDisable(GL_BLEND);
                    glEnable(GL_DEPTH_TEST);
                    glEnable(GL_TEXTURE_2D);
                }
            }
            GlStateManager.resetColor();
        }
    };

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventFastTick> tickListener = event -> {
        if (!targets.isEmpty()) {
            animation += 0.01;
            if (animation > 1) animation = 0;
        } else {
            animation = prevAnimation = 0;
        }
        findTargets();
    };

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventUpdate> updateListener = event -> {
        setSuffix(auraMode.getObject());

        if (targets.isEmpty()) {
            yaw = mc.thePlayer.rotationYaw;
            pitch = mc.thePlayer.rotationPitch;
            return;
        }

        if (!movementFix.getObject()) {
            float[] rots = getRotations();
            yaw = rots[0];
            pitch = rots[1];
            mc.thePlayer.rotationYawHead = yaw;
            mc.thePlayer.rotationPitchHead = pitch;

            if (!ClientModule.otherRotations.getObject()) mc.thePlayer.renderYawOffset = yaw;
        }

    };

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventMotion> eventMotionListener1 = e -> {

        if (blockMode.getObject().equals("Watchdog")) {
            boolean should = shouldBlock();
            if (should) mc.gameSettings.keyBindUse.pressed = false;
            if (e.isPre() && should && block) {
                mc.playerController.onStoppedUsingItem(mc.thePlayer);
                block = false;
            }
            if (e.isPost() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && !block) {
                if (!targets.isEmpty()) {
                    MovingObjectPosition object = new MovingObjectPosition(targets.get(0));
                    mc.playerController.isPlayerRightClickingOnEntity(mc.thePlayer, object.entityHit, object);
                    mc.playerController.interactWithEntitySendPacket(mc.thePlayer, object.entityHit);
                }
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                block = true;
            }
        }
        if (blockMode.getObject().equals("NCP")) {
            boolean should = shouldBlock();
            if (should) mc.gameSettings.keyBindUse.pressed = false;
            if (e.isUpdate() && should) {
                if (!targets.isEmpty()) {
                    MovingObjectPosition object = new MovingObjectPosition(targets.get(0));
                    mc.playerController.isPlayerRightClickingOnEntity(mc.thePlayer, object.entityHit, object);
                    mc.playerController.interactWithEntitySendPacket(mc.thePlayer, object.entityHit);
                }
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
            }
        }
        if (blockMode.getObject().equals("AAC3")) {
            boolean should = shouldBlock();
            if (should) mc.gameSettings.keyBindUse.pressed = false;
            if (e.isPost() && should)
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
        }
        if (blockMode.getObject().equals("Watchdog HvH")) {
            if (e.isPost() && block)
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, null);
        }

        final String[] states = { "Update", "Post" };
        boolean attackEventAvailable = (attackMode.getObject().equals("Pre") && e.isUpdate()) ||
                (attackMode.getObject().equals("Post") && e.isPost()) ||
                (attackMode.getObject().equals("Random") && e.getState().equals(states[MathUtil.getRandom_int(0, states.length)])) ||
                (attackMode.getObject().equals("Mixed") && ((mc.thePlayer.ticksExisted % 2 == 0 && e.isUpdate()) || (mc.thePlayer.ticksExisted % 2 != 0 && e.isPost())));
        if (attackEventAvailable) {
            if (targets.isEmpty()) {
                yaw = mc.thePlayer.rotationYaw;
                pitch = mc.thePlayer.rotationPitch;
                if (sprint) {
                    Sprint.disable = false;
                    sprint = false;
                }
                return;
            }
            sprint = true;
            if (shouldAttack()) {

                if (sprintFix.getObject()) {
                    mc.thePlayer.setSprinting(false);
                }
                boolean sprint = mc.thePlayer.isSprinting();

                if (legit.getObject()) {
                    mc.clickMouse();
                } else if (rayCast.getObject()) {
                    final Entity rayCast = Optional.ofNullable(RaytraceUtil.rayCast(range.getObject() + 1, yaw, pitch))
                            .orElse(RaytraceUtil.rayCast(range.getObject() + 1, lastYaw + (yaw - lastYaw) * mc.timer.renderPartialTicks,
                                    lastPitch + (pitch - lastPitch) * mc.timer.renderPartialTicks));
                    if (rayCast != null) {
                        if (!mc.thePlayer.canEntityBeSeen(rayCast) && mc.thePlayer.getDistanceToEntity(rayCast) >= wallRange.getObject()) return;
                        if (ViaMCP.getInstance().getVersion() == ViaMCP.PROTOCOL_VERSION) mc.thePlayer.swingItem();
                        attack(rayCast);
                        if (ViaMCP.getInstance().getVersion() != ViaMCP.PROTOCOL_VERSION) mc.thePlayer.swingItem();
                    }
                } else {
                    paths.clear();
                    for (Entity entity : targets) {
                        if (!mc.thePlayer.canEntityBeSeen(entity) && mc.thePlayer.getDistanceToEntity(entity) >= wallRange.getObject()) continue;
                        if (!(mc.thePlayer.getDistanceToEntity(entity) <= range.getObject())) {
                            continue;
                        }
                        if (ViaMCP.getInstance().getVersion() == ViaMCP.PROTOCOL_VERSION) mc.thePlayer.swingItem();
                        attack(entity);
                        if (ViaMCP.getInstance().getVersion() != ViaMCP.PROTOCOL_VERSION) mc.thePlayer.swingItem();
                    }
                }
                if (sprint && (blockMode.getObject().equals("Watchdog") || blockMode.getObject().equals("Watchdog HvH")) && mc.thePlayer.isMovingForward())
                    mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));

                if (min.getObject().equals(max.getObject())) {
                    nextDelay = 1000.0 / min.getObject();
                } else nextDelay = 1000.0 / ThreadLocalRandom.current().nextDouble(min.getObject(), max.getObject());

                delayUtil.reset();
            } else if (sprintFix.getObject()) {
                mc.thePlayer.setSprinting(true);
            }
        }
    };

    private boolean shouldAttack() {
        return ((delayMode.getObject().equals("CPS") && delayUtil.hasReached(nextDelay))
                || (((EntityLivingBase) targets.get(0)).hurtTime <= ticks.getObject() && delayMode.getObject().equals("Hurt Time"))
                || (mc.thePlayer.ticksExisted % ticks.getObject() == 0 && delayMode.getObject().equals("Tick"))) && !disable;
    }

    @EventHandler(EventPriority.HIGHEST)
    public final Listener<EventJump> eventJumpListener = e -> {
        if (movementFix.getObject() && !targets.isEmpty()) {
            float var1 = this.yaw;
            if (silent.getObject()) {
                final float[] movementValues = RotationUtil.getSilentMovementValues(yaw);
                final float moveForward = movementValues[0], moveStrafing = movementValues[1];
                float roundedStrafing = Math.max(-1, Math.min(1, Math.round(moveStrafing * 100))),
                        roundedForward = Math.max(-1, Math.min(1, Math.round(moveForward * 100)));
                if (roundedStrafing != 0)
                    yaw -= 90 * roundedStrafing * (roundedForward != 0 ? roundedForward * 0.5 : 1);
                if (moveForward < 0) yaw += 180;
                if (moveForward <= 0) e.setSpeed(0);
            }
            e.setYaw(var1);
        }
    };

    @EventHandler(EventPriority.HIGHEST)
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (block && blockMode.getObject().equals("Watchdog New") && e.getPacket() instanceof S30PacketWindowItems) {
            e.setCancelled(true);
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, null);
            /*mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem - 1 < 0 ? 8 : mc.thePlayer.inventory.currentItem - 1));
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down(), EnumFacing.UP.getIndex(), null, 0, (float) Math.random() / 5f, 0));
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down(), EnumFacing.UP.getIndex(), null, 0, (float) Math.random() / 5f, 0));
            mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);*/
        }
    };

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventStrafe> strafeListener = event -> {

        if (movementFix.getObject() && !targets.isEmpty()) {
            float[] rots = getRotations();
            yaw = rots[0];
            pitch = rots[1];
            event.yaw = yaw;
            if (silent.getObject()) RotationUtil.silentMoveFix(event, yaw);
            mc.thePlayer.rotationYawHead = yaw;
            mc.thePlayer.rotationPitchHead = pitch;
            if (!ClientModule.otherRotations.getObject()) mc.thePlayer.renderYawOffset = yaw;

        }

    };

    private boolean shouldBlock() {
        return !blockMode.getObject().equals("None") && (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) && anyTargetInRange(blockRangeValue.getObject());
    }

    private boolean anyTargetInRange(double range) {
        return mc.theWorld.loadedEntityList.stream().anyMatch(e -> isValid(e, Math.max(this.range.getObject(), range)));
    }

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventMotion> motionListener = event -> {
        if (blockMode.getObject().equals("Watchdog HvH")) {
            if (event.isUpdate()) {
                if (shouldBlock()) {
                    mc.gameSettings.keyBindUse.pressed = block = true;
                } else if (block) {
                    mc.gameSettings.keyBindUse.pressed = block = false;
                }
            }
            disableNoSlow = !block;
        }
        if (blockMode.getObject().equals("Watchdog New")) {
            if (event.isUpdate()) {
                if (shouldBlock()) {
                    mc.gameSettings.keyBindUse.pressed = block = true;
                    blockState++;
                    if (blockState >= 2) {
                        for (Packet<?> packet : packets)
                            mc.thePlayer.sendQueue.addToSendQueue(packet);
                        packets.clear();
                        blockState = 0;
                    }
                } else if (block) {
                    for (Packet<?> packet : packets)
                        mc.thePlayer.sendQueue.addToSendQueue(packet);
                    packets.clear();
                    mc.gameSettings.keyBindUse.pressed = block = false;
                }
            }
            disableNoSlow = !block;
        }
        if (!targets.isEmpty()) {
            event.yaw = yaw;
            event.pitch = pitch;
            if (!silentAim.getObject() && event.isPost()) {
                mc.thePlayer.rotationYaw = yaw;
                mc.thePlayer.rotationPitch = pitch;
                yaw = mc.thePlayer.rotationYaw;
                pitch = mc.thePlayer.rotationPitch;
            }
            mc.thePlayer.rotationYawHead = yaw;
            mc.thePlayer.rotationPitchHead = pitch;
            if (!ClientModule.otherRotations.getObject()) mc.thePlayer.renderYawOffset = yaw;
        }
        if (event.isPost() && shouldBlock() && blockMode.getObject().equals("Spam Click")) mc.rightClickMouse();

        if (blockMode.getObject().equals("Fake")) {
            if (event.isPost() && shouldBlock()) block = true;
            if (event.isPre() && !shouldBlock() && block) block = false;
        }

        if (blockMode.getObject().equals("Vanilla")) {
            if (event.isUpdate()) {
                if (shouldBlock()) {
                    mc.gameSettings.keyBindUse.pressed = block = true;
                } else if (block) {
                    mc.gameSettings.keyBindUse.pressed = block = false;
                }
            }
        }


    };

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventSlowDown> eventSlowDownListener = e -> {
        if ((blockMode.getObject().equals("Vanilla") || blockMode.getObject().equals("Watchdog New") || blockMode.getObject().equals("Watchdog HvH")) && block) {
            e.setForward(1);
            e.setStrafe(1);
            e.setStopSprint(false);
        }
    };

    @EventHandler(EventPriority.HIGHEST)
    public Listener<EventRenderItem> eventRenderItemListener = e -> {
        if (block && (blockMode.getObject().equals("Fake") || blockMode.getObject().equals("Watchdog New") || blockMode.getObject().equals("Watchdog HvH") || blockMode.getObject().equals("Vanilla")))
            e.isUsing = true;
    };

    public void attack(Entity entity) {
        if (autoWeapon.getObject()) {
            float lastDamage = 0;
            int lastSlot = -1;
            for (int i = 0; i < 9; i++) {
                final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack == null) continue;
                final float damage = ItemUtil.getCombatDamage(stack);
                if ((stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe || stack.getItem() instanceof ItemPickaxe) &&
                        damage > lastDamage) {
                    lastDamage = damage;
                    lastSlot = i;
                }
            }
            if (lastSlot != -1 && mc.thePlayer.inventory.currentItem != lastSlot)
                mc.thePlayer.inventory.currentItem = lastSlot;
        }
        Path path = null;
        final CustomVec3 start = new CustomVec3(mc.thePlayer), end = new CustomVec3(entity);
        if (tpAuraMode.getObject().equals("Rede Dark"))
            end.setY(start.getY());
        if (auraMode.getObject().equals("TP") && mc.thePlayer.getDistanceToEntity(entity) > 6)
            path = PathUtil.findPath(start, end, tpOffset.getObject());
        if (path != null) {
            Path oldPath = path;
            paths.add(oldPath);
            path = PathUtil.findPath(path.getEnd(), path.getStart(), tpOffset.getObject());
            for (CustomVec3 v : oldPath.getAdditionalVectors()) {
                if (tpAuraMode.getObject().equals("Round Y")) v.setY((int)Math.round(v.getY()));
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(v.getX(), v.getY(), v.getZ(), tpGround.getObject()));
                mc.thePlayer.setPosition(v.getX(), v.getY(), v.getZ());
            }
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (path != null) {
            for (CustomVec3 v : path.getAdditionalVectors()) {
                if (tpAuraMode.getObject().equals("Round Y")) v.setY((int)Math.round(v.getY()));
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(v.getX(), v.getY(), v.getZ(), tpGround.getObject()));
                mc.thePlayer.setPosition(v.getX(), v.getY(), v.getZ());
            }
        }
    }

    public float[] getRotations() {
        lastYaw = yaw;
        lastPitch = pitch;
        float[] rotations;
        float[] nextRotations = new float[]{};
        float[] centerRotations = RotationUtil.getNeededRotations(RotationUtil.getCenter(targets.get(0).getEntityBoundingBox()));
        switch (customMode.getObject()) {
            case "Center":
                nextRotations = RotationUtil.getNeededRotations(RotationUtil.getHead(targets.get(0).getEntityBoundingBox(), height.getObject()));
                float[] prevRots = {yaw, pitch};
                float[] rotOffset = {MathHelper.wrapAngleTo180_float(nextRotations[0] - prevRots[0]), MathHelper.wrapAngleTo180_float(nextRotations[1] - prevRots[1])};
                nextRotations[0] = (float) (yaw + rotOffset[0] * (angleStep.getObject() / angleStep.getMax()));
                break;
            case "Closest":
                nextRotations = RotationUtil.getNeededRotations(RotationUtil.getClosestPoint(mc.thePlayer.getPositionEyes(1.0f), targets.get(0).getEntityBoundingBox()));
                prevRots = new float[]{yaw, pitch};
                rotOffset = new float[]{MathHelper.wrapAngleTo180_float(nextRotations[0] - prevRots[0]), MathHelper.wrapAngleTo180_float(nextRotations[1] - prevRots[1])};
                nextRotations[0] = (float) (yaw + rotOffset[0] * (angleStep.getObject() / angleStep.getMax()));
                break;
            case "Random":
                nextRotations = RotationUtil.getNeededRotations(RotationUtil.getRandomCenter(targets.get(0).getEntityBoundingBox()));
                prevRots = new float[]{yaw, pitch};
                rotOffset = new float[]{MathHelper.wrapAngleTo180_float(nextRotations[0] - prevRots[0]), MathHelper.wrapAngleTo180_float(nextRotations[1] - prevRots[1])};
                nextRotations[0] = (float) (yaw + rotOffset[0] * (angleStep.getObject() / angleStep.getMax()));
                break;
            case "Reduce":
                nextRotations = RotationUtil.getNeededRotations(RotationUtil.getFeet(targets.get(0).getEntityBoundingBox()));
                prevRots = new float[]{yaw, pitch};
                nextRotations[0] -= (nextRotations[0] - prevRots[0]) % angleStep.getObject();
                final double xDiff = MathUtil.getDifference(mc.thePlayer.posX, targets.get(0).posX), zDiff = MathUtil.getDifference(mc.thePlayer.posZ, targets.get(0).posZ), distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                if (distanceXZ <= mc.thePlayer.width * 2.0) angleStep.setObject(85.0);
                if (distanceXZ < mc.thePlayer.width * 1.5) angleStep.setObject(145.0);
                if (distanceXZ < mc.thePlayer.width) {
                    angleStep.setObject(180.0);
                    nextRotations[1] = 90;
                }


                if (distanceXZ > mc.thePlayer.width * 3) {
                    nextRotations = RotationUtil.getNeededRotations(RotationUtil.getHead(targets.get(0).getEntityBoundingBox(), Double.MIN_VALUE));
                    nextRotations[0] -= (nextRotations[0] - prevRots[0]) % 60;

                }
                break;
            case "None":
                return new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
            case "NCP":
                return new float[] {RotationUtil.getNeededRotations(RotationUtil.getCenter(targets.get(0).getEntityBoundingBox()))[0],
                        Math.max(-90, Math.min(90, 45 + (mc.thePlayer.getDistanceToEntity(targets.get(0)) - 3.0f) * -80))};
            case "Spin":
                return new float[]{yaw + 45, 90};

        }

        rotations = nextRotations;

        if (predict.getObject()) {

            float diff = prevYaw - centerRotations[0];
            rotations[0] += diff * modifier.getObject();
            prevYaw = centerRotations[0];

        }

        if (jitter.getObject()) {
            if (legitJitter.getObject()) {

                if (up) {
                    rotations[1] += ThreadLocalRandom.current().nextDouble(jitterValue.getObject() * 0.5, jitterValue.getObject() * 2);
                } else {
                    rotations[1] -= ThreadLocalRandom.current().nextDouble(jitterValue.getObject() * 0.5, jitterValue.getObject() * 2);
                }

                if (legitJitterDelayUtil.hasReached(legitJitterDelay)) {
                    legitJitterDelayUtil.reset();
                    up = !up;
                    legitJitterDelay = ThreadLocalRandom.current().nextDouble(100, 600);
                }

            } else {
                rotations[0] += ThreadLocalRandom.current().nextDouble(-jitterValue.getObject(), jitterValue.getObject());
                rotations[1] += ThreadLocalRandom.current().nextDouble(-jitterValue.getObject(), jitterValue.getObject());
            }
        }


        switch (gcd.getObject()) {
            case "None":
                break;
            case "Fake":
                rotations = RotationUtil.mouseFix(rotations[0], rotations[1]);
                break;
            case "Real":
                rotations = RotationUtil.mouseFix(yaw, pitch, rotations[0], rotations[1]);
                break;
        }

        rotations[1] = MathHelper.clamp_float(rotations[1], -90, 90);
        return rotations;
    }

    public void findTargets() {

        ArrayList<EntityLivingBase> finalEntities = mc.theWorld.loadedEntityList.stream().filter(this::isValid).map(e -> (EntityLivingBase) e).collect(Collectors.toCollection(ArrayList::new));
        targets.clear();
        switch (priorityMode.getObject()) {
            case "Distance":
                finalEntities = finalEntities.stream().sorted(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toCollection(ArrayList::new));
                break;
            case "Health":
                finalEntities = finalEntities.stream().sorted(Comparator.comparingDouble(EntityLivingBase::getHealth)).collect(Collectors.toCollection(ArrayList::new));
                break;
            case "HurtTime":
                finalEntities = finalEntities.stream().sorted(Comparator.comparingInt(e -> e.hurtTime)).collect(Collectors.toCollection(ArrayList::new));
                break;
            case "VampHealth":
                finalEntities = finalEntities.stream().sorted(Comparator.comparingInt(e -> e.hurtTime)).sorted(Comparator.comparingDouble(EntityLivingBase::getHealth)).collect(Collectors.toCollection(ArrayList::new));
                break;
        }
        for (Entity entity : finalEntities) {
            if (targets.size() >= maxTargets.getObject()) break;
            targets.add(entity);
        }

    }

    private class AntiBot {
        @EventHandler(EventPriority.HIGHEST)
        public final Listener<Event> handler = ((KillAura) getInstance())::checkBots;
    }

    private void checkBots(final Event event) {
        if (event instanceof EventReceivedPacket && antiBotCombo.isSelected("Matrix")) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S38PacketPlayerListItem) {
                final S38PacketPlayerListItem s38 = e.getPacket();
                for (final S38PacketPlayerListItem.AddPlayerData entry : s38.getEntries()) {
                    if (s38.getAction() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                        final NetworkPlayerInfo networkplayerinfo = new NetworkPlayerInfo(entry);
                        if (mc.getNetHandler().getPlayerInfoMap().stream().anyMatch(i -> i.getGameProfile().getName().equals(networkplayerinfo.getGameProfile().getName())))
                            e.setCancelled(true);
                    }
                }
            }
        }
        if (event instanceof EventWorldChange && antiBotCombo.isSelected("Watchdog Staff"))
            bots.clear();
        if (event instanceof EventReceivedPacket && antiBotCombo.isSelected("Watchdog Staff")) {
            final EventReceivedPacket e = (EventReceivedPacket) event;
            /*if (e.getPacket() instanceof S0CPacketSpawnPlayer && ServerUtil.isHypixel()) {
                final S0CPacketSpawnPlayer s0C = e.getPacket();
                final Entity entity = mc.theWorld.getEntityByID(s0C.getEntityID());
                if (!(entity instanceof EntityPlayer)) return;
                EntityPlayer player = (EntityPlayer) entity;
                boolean hasArmor = false;
                for (int i = 0; i < 4; i++) {
                    if (player.getCurrentArmor(i) != null) hasArmor = true;
                }
                if ((((player.posX != 0 || player.posZ != 0) &&
                        mc.theWorld.isAirBlock(player.getPosition().add(0, -1, 0))
                        && mc.theWorld.isAirBlock(player.getPosition().add(0, -2, 0))) || hasArmor) && AutoHypixel.isCurrentlyInGame) {
                    Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Anti Bot", "STAFF BOT DETECTED (" + player.getName() + ")", 20000, Type.WARNING));
                    bots.add(entity);
                }
            }*/
            for (final Entity entity : mc.theWorld.loadedEntityList) {
                if (!AutoHypixel.alreadyDiedPlayers.contains(entity.getName())) continue;
                if (bots.contains(entity)) continue;
                bots.add(entity);
            }
        }
    }


    public boolean isValid(Entity entity) {
        return isValid(entity, range.getObject());
    }

    public boolean isValid(Entity entity, double range) {
        return isValidTarget(entity) && entity != mc.thePlayer && mc.thePlayer.getDistanceToEntity(entity) < range && entity instanceof EntityLivingBase; //TODO: More checks
    }

    public boolean isValidTarget(Entity entity) {
        for (Friend friend : Client.INSTANCE.getFriendManager().getFriendList()) {
            if (friend.getName().contains(entity.getName())) return false;
        }
        if (entity.getEntityId() < 0) return false;
        if (entity.ticksExisted < ticksValue.getObject()) return false;
        if (bots.contains(entity)) return false;
        if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).deathTime != 0) return false;
        if (checkPitSpawn.getObject() && checkPitSpawn.checkDependency() && entity.posY > 90
                && entity.posX < 25 && entity.posX > -25 && entity.posZ < 25 && entity.posZ > -25) return false;
        if (checkPitTeams.getObject() && checkPitTeams.checkDependency()) {
            if (targetsCombo.isSelected("Teams") && entity.getDisplayName().getFormattedText().startsWith(ChatFormatting.PREFIX_CODE +
                    mc.thePlayer.getDisplayName().getFormattedText().substring(0, 2)) && (AutoHypixel.getCurrentPitEvent().contains("TDM") ||
                    AutoHypixel.getCurrentPitEvent().contains("SQUAD")))
                return false;
        } else {
            if (targetsCombo.isSelected("Teams") && entity.getDisplayName().getFormattedText().startsWith(Character.toString(ChatFormatting.PREFIX_CODE) +
                    mc.thePlayer.getDisplayName().getFormattedText().charAt(mc.thePlayer.getDisplayName().getFormattedText().indexOf(ChatFormatting.PREFIX_CODE) + 1)))
                return false;
        }
        if (entity instanceof EntityPlayer && targetsCombo.isSelected("Client-User") && Client.INSTANCE.getIrcConnector().getIrcCache().getIrcUserHashMap().entrySet().stream().anyMatch(stringUserEntry -> stringUserEntry.getValue().getMinecraftName().equalsIgnoreCase(((EntityPlayer)entity).getGameProfile().getName())))
            return false;
        if (entity.getName().contains("[NPC]")) return false;
        if (entity.isInvisibleToPlayer(mc.thePlayer) && !targetsCombo.isSelected("Invisible")) return false;
        return (entity instanceof EntityMob && targetsCombo.isSelected("Monsters")) ||
                ((entity instanceof EntityAnimal || entity instanceof EntityVillager) && targetsCombo.isSelected("Animals")) ||
                (entity instanceof EntityPlayer && targetsCombo.isSelected("Players"));
    }

}