package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import com.google.common.base.Supplier;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.listener.event.Event;
import tech.atani.client.listener.event.minecraft.game.RunTickEvent;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.event.minecraft.render.Render3DEvent;
import tech.atani.client.listener.event.minecraft.world.WorldLoadEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.utility.math.VecUtil;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.utility.player.combat.FightUtil;
import tech.atani.client.utility.world.entities.EntitiesUtil;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

@Native
@ModuleData(name = "Backtrack", description = "Delay packets to get higher reach", category = Category.COMBAT)
public class Backtrack extends Module {

    private final StringBoxValue mode = new StringBoxValue("Mode", "How will the module operate?", this, new String[]{"Packet", "Packet (Old)", "AABB"});
    public StringBoxValue packetMode = new StringBoxValue("Packets", "Which packets to cancel?", this, new String[]{"Select", "All"}, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet (Old)")});
    private SliderValue<Long> delay = new SliderValue<>("Delay", "What will be the packet delay?", this, 450L, 0L, 5000L, 0, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet (Old)")});
    public SliderValue<Float> maximumRange = new SliderValue<>("Max Range", "What'll be the maximum range?", this, 6f, 1f, 6f, 1, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet (Old)")});
    public CheckBoxValue onlyWhenNeeded = new CheckBoxValue("Only When Out of Reach", "Backtrack target only if it is out of reach?", this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet (Old)")});
    private final SliderValue<Float> minRange = new SliderValue<>("Min Range",  "What will be the minimum range for operating?", this, 2.9f, 0f, 6f, 1, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final SliderValue<Float> maxStartRange = new SliderValue<>("Max Start Range", "What will be the maximum range to start?", this, 3.2f, 2f, 4f, 1, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final SliderValue<Float> maxActiveRange = new SliderValue<>("Max Active Range", "What will be the maximum range for being active?", this, 5f, 2f, 6f, 1, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final SliderValue<Integer> minDelay = new SliderValue<>("Min Delay", "What will the minimum packet delay?", this, 100, 0, 500, 0, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final SliderValue<Integer> maxDelay = new SliderValue<>("Max Delay", "What will the maximum packet delay?", this, 200, 0, 1000, 1, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final SliderValue<Integer> maxHurtTime = new SliderValue<>("Max Hurt Time", "What will the minimum hurt time?", this, 6, 0, 10, 0, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final CheckBoxValue syncHurtTime = new CheckBoxValue("Sync HT with Ping", "Sync hurt time with ping?",this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final SliderValue<Float> minReleaseRange = new SliderValue<>("Min Release Range", "What will be the minimum range for releasing?",this, 3.2F, 2f, 6f, 1, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final CheckBoxValue onlyKillAura = new CheckBoxValue("Only KillAura", "Operate only on killaura targets?",this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final CheckBoxValue resetOnVelocity = new CheckBoxValue("Release on Velocity", "Release after getting knockback?",this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    private final CheckBoxValue resetOnLagging = new CheckBoxValue("Release on Flag", "Release player after flagging?", this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    public CheckBoxValue players = new CheckBoxValue("Players", "Attack Players?", this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    public CheckBoxValue animals = new CheckBoxValue("Animals", "Attack Animals?", this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    public CheckBoxValue monsters = new CheckBoxValue("Monsters", "Attack Monsters?", this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});
    public CheckBoxValue invisible = new CheckBoxValue("Invisibles", "Attack Invisibles?", this, true, new Supplier[]{() -> mode.getValue().equalsIgnoreCase("Packet")});

    // Old
    private final ArrayList<Packet<INetHandler>> packets = new ArrayList<>();
    private EntityLivingBase entity = null;
    private INetHandler packetListener = null;
    private WorldClient lastWorld;
    private final TimeHelper timeHelper = new TimeHelper();

    // New
    private final ArrayList<Packet<INetHandler>> storedPackets = new ArrayList<>();
    private final ArrayList<Entity> targets = new ArrayList<>();

    private KillAura killAura;

    private TimeHelper freezeTimer = new TimeHelper();
    private Entity targetEntity = null;
    private boolean freezingNeeded = false;

    @Override
    public String getSuffix() {
        switch (this.mode.getValue()) {
            default:
                return this.maxDelay.getValue() + "ms";
            case "Packet (Old)":
                return this.delay.getValue() + "ms";
        }
    }

    @Listen
    public void onRunTick(RunTickEvent runTickEvent) {
        if(this.mode.getValue().equalsIgnoreCase("Packet (Old)")) {
            try {
                if (entity != null && getPlayer() != null && this.packetListener != null && getWorld() != null) {
                    double d0 = this.entity.realPosX / 32.0D;
                    double d1 = this.entity.realPosY / 32.0D;
                    double d2 = this.entity.realPosZ / 32.0D;
                    double d3 = (double) this.entity.serverPosX / 32.0D;
                    double d4 = (double) this.entity.serverPosY / 32.0D;
                    double d5 = (double) this.entity.serverPosZ / 32.0D;
                    AxisAlignedBB alignedBB = new AxisAlignedBB(d3 - (double) this.entity.width, d4, d5 - (double) this.entity.width, d3 + (double) this.entity.width, d4 + (double) this.entity.height, d5 + (double) this.entity.width);
                    Vec3 positionEyes = getPlayer().getPositionEyes(getTimer().renderPartialTicks);
                    double currentX = MathHelper.clamp_double(positionEyes.xCoord, alignedBB.minX, alignedBB.maxX);
                    double currentY = MathHelper.clamp_double(positionEyes.yCoord, alignedBB.minY, alignedBB.maxY);
                    double currentZ = MathHelper.clamp_double(positionEyes.zCoord, alignedBB.minZ, alignedBB.maxZ);
                    AxisAlignedBB alignedBB2 = new AxisAlignedBB(d0 - (double) this.entity.width, d1, d2 - (double) this.entity.width, d0 + (double) this.entity.width, d1 + (double) this.entity.height, d2 + (double) this.entity.width);
                    double realX = MathHelper.clamp_double(positionEyes.xCoord, alignedBB2.minX, alignedBB2.maxX);
                    double realY = MathHelper.clamp_double(positionEyes.yCoord, alignedBB2.minY, alignedBB2.maxY);
                    double realZ = MathHelper.clamp_double(positionEyes.zCoord, alignedBB2.minZ, alignedBB2.maxZ);
                    double distance = this.maximumRange.getValue();
                    if (!this.getPlayer().canEntityBeSeen(this.entity)) {
                        distance = distance > 3 ? 3 : distance;
                    }
                    double bestX = MathHelper.clamp_double(positionEyes.xCoord, this.entity.getEntityBoundingBox().minX, this.entity.getEntityBoundingBox().maxX);
                    double bestY = MathHelper.clamp_double(positionEyes.yCoord, this.entity.getEntityBoundingBox().minY, this.entity.getEntityBoundingBox().maxY);
                    double bestZ = MathHelper.clamp_double(positionEyes.zCoord, this.entity.getEntityBoundingBox().minZ, this.entity.getEntityBoundingBox().maxZ);
                    boolean b = positionEyes.distanceTo(new Vec3(bestX, bestY, bestZ)) > 2.9 || (getPlayer().hurtTime < 8 && getPlayer().hurtTime > 1);
                    if (!this.onlyWhenNeeded.getValue()) {
                        b = true;
                    }
                    if (!(b && positionEyes.distanceTo(new Vec3(realX, realY, realZ)) > positionEyes.distanceTo(new Vec3(currentX, currentY, currentZ)) + 0.05) || !(getPlayer().getDistance(d0, d1, d2) < distance) || this.timeHelper.hasReached((long) this.delay.getValue())) {
                        this.resetPackets(this.packetListener);
                        this.timeHelper.reset();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Listen
    public void onPacket(PacketEvent event) {
        if (killAura == null)
            killAura = ModuleStorage.getInstance().getByClass(KillAura.class);

        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if(this.mode.is("Packet")) {
            Packet<?> packet = event.getPacket();
            WorldClient theWorld = mc.theWorld;

            if (event.getType() == PacketEvent.Type.INCOMING) {
                if (packet instanceof S14PacketEntity) {
                    handleS14PacketEntity(packet, theWorld, event);
                } else {
                    handleNonS14Packet(packet, event);
                }
            } else if (packet instanceof C02PacketUseEntity) {
                handleC02PacketUseEntity(packet, theWorld);
            }
        } else if(this.mode.is("Packet (Old)")) {
            if (event.getiNetHandler() != null && event.getiNetHandler() instanceof OldServerPinger) return;
            if (mc.theWorld != null) {
                if (event.getType() == PacketEvent.Type.INCOMING) {
                    this.packetListener = event.getiNetHandler();
                    synchronized (Backtrack.class) {
                        final Packet<?> p = event.getPacket();
                        if (p instanceof S14PacketEntity) {
                            S14PacketEntity packetEntity = (S14PacketEntity) p;
                            final Entity entity = getWorld().getEntityByID(packetEntity.getEntityId());
                            if (entity instanceof EntityLivingBase) {
                                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                                entityLivingBase.realPosX += packetEntity.func_149062_c();
                                entityLivingBase.realPosY += packetEntity.func_149061_d();
                                entityLivingBase.realPosZ += packetEntity.func_149064_e();
                            }
                        }
                        if (p instanceof S18PacketEntityTeleport) {
                            S18PacketEntityTeleport teleportPacket = (S18PacketEntityTeleport) p;
                            final Entity entity = getWorld().getEntityByID(teleportPacket.getEntityId());
                            if (entity instanceof EntityLivingBase) {
                                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                                entityLivingBase.realPosX = teleportPacket.getX();
                                entityLivingBase.realPosY = teleportPacket.getY();
                                entityLivingBase.realPosZ = teleportPacket.getZ();
                            }
                        }

                        this.entity = null;
                        try {
                            if (ModuleStorage.getInstance()
                                    .getByClass(KillAura.class)
                                    .isEnabled()) {
                                this.entity = KillAura.curEntity;
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        if (this.entity == null) {
                            this.resetPackets(event.getiNetHandler());
                            return;
                        }
                        if (getWorld() != null && getPlayer() != null) {
                            if (this.lastWorld != getWorld()) {
                                resetPackets(event.getiNetHandler());
                                this.lastWorld = getWorld();
                                return;
                            }
                            this.addPackets(p, event);
                        }
                        this.lastWorld = getWorld();
                    }
                }
            }
        }
    }

    private void resetPackets(INetHandler netHandler) {
        if (this.packets.size() > 0) {
            synchronized (this.packets) {
                while (this.packets.size() != 0) {
                    try {
                        this.packets.get(0).processPacket(netHandler);
                    } catch (Exception ignored) {
                    }
                    this.packets.remove(this.packets.get(0));
                }

            }
        }
    }

    private void addPackets(Packet packet, Event eventReadPacket) {
        synchronized (this.packets) {
            if (this.blockPacket(packet)) {
                this.packets.add(packet);
                eventReadPacket.setCancelled(true);
            }
        }
    }

    private boolean blockPacket(Packet packet) {
        switch (this.packetMode.getValue()) {
            case "All":
                return true;
            default:
                if (packet instanceof S03PacketTimeUpdate) {
                    return true;
                } else if (packet instanceof S00PacketKeepAlive) {
                    return true;
                } else if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
                    return true;
                } else {
                    return packet instanceof S32PacketConfirmTransaction || packet instanceof S14PacketEntity || packet instanceof S19PacketEntityStatus || packet instanceof S19PacketEntityHeadLook || packet instanceof S18PacketEntityTeleport || packet instanceof S0FPacketSpawnMob;
                }
        }
    }


    private void handleS14PacketEntity(Packet<?> packet, WorldClient theWorld, PacketEvent event) {
        S14PacketEntity entityPacket = (S14PacketEntity) packet;
        Entity entity = entityPacket.getEntity(theWorld);

        if (entity == null || !(entity instanceof EntityLivingBase)) {
            return;
        }

        entity.serverPosX += entityPacket.func_149062_c();
        entity.serverPosY += entityPacket.func_149061_d();
        entity.serverPosZ += entityPacket.func_149064_e();

        double x = entity.serverPosX / 32.0;
        double y = entity.serverPosY / 32.0;
        double z = entity.serverPosZ / 32.0;

        boolean isValidTarget = (!onlyKillAura.getValue() || killAura.isEnabled() || freezingNeeded)
                && FightUtil.isValidWithPlayer(entity, 100, invisible.getValue(), players.getValue(), animals.getValue(), monsters.getValue());

        if (isValidTarget) {
            double afterRange = calculateAfterRange(x, y, z);
            double beforeRange = calculateBeforeRange(entity);

            if (beforeRange <= maxStartRange.getValue() && isInRange(afterRange, minRange.getValue(), maxActiveRange.getValue()) && afterRange > beforeRange + 0.02 && ((EntityLivingBase) entity).hurtTime <= calculateMaxHurtTime()) {
                handleValidTarget(entity, event);
                return;
            }
        }

        if (freezingNeeded) {
            handleFreezing(entity, event);
            return;
        }

        handleNonCancelled(entity, x, y, z, entityPacket, event);
    }

    private void handleNonS14Packet(Packet<?> packet, PacketEvent event) {
        if ((packet instanceof S12PacketEntityVelocity && resetOnVelocity.getValue()) || (packet instanceof S08PacketPlayerPosLook && resetOnLagging.getValue())) {
            storedPackets.add((Packet<INetHandler>) packet);
            event.setCancelled(true);
            releasePackets();
        } else if (freezingNeeded && !event.isCancelled()) {
            if (packet instanceof S19PacketEntityStatus) {
                if (((S19PacketEntityStatus) packet).logicOpcode == (byte) 2) {
                    return;
                }
            }
            storedPackets.add((Packet<INetHandler>) packet);
            event.setCancelled(true);
        }
    }

    private void handleC02PacketUseEntity(Packet<?> packet, WorldClient theWorld) {
        C02PacketUseEntity useEntityPacket = (C02PacketUseEntity) packet;
        if (useEntityPacket.getAction() == C02PacketUseEntity.Action.ATTACK && freezingNeeded) {
            targetEntity = useEntityPacket.getEntityFromWorld(theWorld);
        }
    }

    private double calculateAfterRange(double x, double y, double z) {
        AxisAlignedBB afterBB = new AxisAlignedBB(x - 0.4, y - 0.1, z - 0.4, x + 0.4, y + 1.9, z + 0.4);
        Vec3 eyes = mc.thePlayer.getPositionEyes(1F);
        return VecUtil.getNearestPointBB(eyes, afterBB).distanceTo(eyes);
    }

    private double calculateBeforeRange(Entity entity) {
        return EntitiesUtil.getDistanceToEntityBox(mc.thePlayer, entity);
    }

    private boolean isInRange(double value, double minValue, double maxValue) {
        return value >= minValue && value <= maxValue;
    }

    private void handleValidTarget(Entity entity, PacketEvent event) {
        if (!freezingNeeded) {
            freezeTimer.reset();
            freezingNeeded = true;
        }
        if (!targets.contains(entity)) {
            targets.add(entity);
        }
        event.setCancelled(true);
    }

    private void handleFreezing(Entity entity, PacketEvent event) {
        if (!targets.contains(entity)) {
            targets.add(entity);
        }
        event.setCancelled(true);
    }

    private void handleNonCancelled(Entity entity, double x, double y, double z, S14PacketEntity entityPacket, PacketEvent event) {
        float f = entityPacket.func_149060_h() ? (entityPacket.func_149066_f() * 360) / 256.0f : entity.rotationYaw;
        float f1 = entityPacket.func_149060_h() ? (entityPacket.func_149063_g() * 360) / 256.0f : entity.rotationPitch;

        entity.setPositionAndRotation2(x, y, z, f, f1, 3, false);
        entity.onGround = entityPacket.onGround;
        event.setCancelled(true);
    }

    @Listen
    public void onRender3D(Render3DEvent event) {
        if(this.mode.is("Packet")) {
            if (!freezingNeeded) {
                return;
            }
        }

        GL11.glPushMatrix();
        GlStateManager.disableAlpha();

        try {
            if(this.mode.is("Packet")) {
                for (Entity entity : targets) {
                    renderFrozenEntity(entity, event);
                }
            } else if(this.mode.is("Packet (Old)")) {
                renderFrozenEntity(this.entity, event);
            }
        } catch (ConcurrentModificationException e) {

        }

        GlStateManager.enableAlpha();
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }

    private void renderFrozenEntity(Entity entity, Render3DEvent event) {
        if (!(entity instanceof EntityOtherPlayerMP)) {
            return;
        }

        EntityOtherPlayerMP mp = new EntityOtherPlayerMP(mc.theWorld, ((EntityOtherPlayerMP) entity).getGameProfile());
        mp.posX = entity.serverPosX / 32.0;
        mp.posY = entity.serverPosY / 32.0;
        mp.posZ = entity.serverPosZ / 32.0;
        mp.prevPosX = mp.posX;
        mp.prevPosY = mp.posY;
        mp.prevPosZ = mp.posZ;
        mp.lastTickPosX = mp.posX;
        mp.lastTickPosY = mp.posY;
        mp.lastTickPosZ = mp.posZ;
        mp.rotationYaw = entity.rotationYaw;
        mp.rotationPitch = entity.rotationPitch;
        mp.rotationYawHead = ((EntityOtherPlayerMP) entity).rotationYawHead;
        mp.prevRotationYaw = mp.rotationYaw;
        mp.prevRotationPitch = mp.rotationPitch;
        mp.prevRotationYawHead = mp.rotationYawHead;
        mp.swingProgress = ((EntityOtherPlayerMP) entity).swingProgress;
        mp.swingProgressInt = ((EntityOtherPlayerMP) entity).swingProgressInt;
        mc.renderManager.renderEntitySimple(mp, event.getPartialTicks());
    }

    @Listen
    public void onMotion(UpdateMotionEvent event) {
        if (event.getType() != UpdateMotionEvent.Type.POST) {
            return;
        }

        if (freezingNeeded && this.mode.is("Packet")) {
            if (freezeTimer.hasReached(maxDelay.getValue())) {
                releasePackets();
                return;
            }

            if (!targets.isEmpty()) {
                boolean shouldRelease = false;

                for (Entity entity : targets) {
                    double x = entity.serverPosX / 32.0;
                    double y = entity.serverPosY / 32.0;
                    double z = entity.serverPosZ / 32.0;

                    AxisAlignedBB entityBB = new AxisAlignedBB(x - 0.4, y - 0.1, z - 0.4, x + 0.4, y + 1.9, z + 0.4);

                    double range = entityBB.getLookingTargetRange(mc.thePlayer);

                    if (range == Double.MAX_VALUE) {
                        Vec3 eyes = mc.thePlayer.getPositionEyes(1F);
                        range = VecUtil.getNearestPointBB(eyes, entityBB).distanceTo(eyes) + 0.075;
                    }

                    if (range <= minRange.getValue()) {
                        shouldRelease = true;
                        break;
                    }

                    Entity entity1 = targetEntity;
                    if (entity1 != entity) {
                        continue;
                    }

                    if (freezeTimer.hasReached(minDelay.getValue())) {
                        if (range >= minReleaseRange.getValue()) {
                            shouldRelease = true;
                            break;
                        }
                    }
                }

                if (shouldRelease) {
                    releasePackets();
                }
            }
        }
    }

    @Listen
    public void onWorld(WorldLoadEvent event) {
        targetEntity = null;
        targets.clear();

        if (event.getWorldClient() == null) {
            storedPackets.clear();
        }
    }

    public void releasePackets() {
        targetEntity = null;
        INetHandlerPlayClient netHandler = mc.getNetHandler();

        if (storedPackets.isEmpty()) {
            return;
        }

        while (!storedPackets.isEmpty()) {
            Packet<INetHandler> packet = storedPackets.remove(0);

            try {
                try {
                    packet.processPacket(netHandler);
                } catch (Exception e) {
                }
            } catch (ThreadQuickExitException ignored) {
                // Ignore the exception
            }
        }

        while (!targets.isEmpty()) {
            Entity entity = targets.remove(0);

            if (!entity.isDead) {
                double x = entity.serverPosX / 32.0;
                double y = entity.serverPosY / 32.0;
                double z = entity.serverPosZ / 32.0;

                entity.setPosition(x, y, z);
            }
        }

        freezingNeeded = false;
    }

    private int calculateMaxHurtTime() {
        int ping = EntitiesUtil.getPing(mc.thePlayer);

        return maxHurtTime.getValue() + (syncHurtTime.getValue() ? (int) Math.ceil(ping / 50.0) : 0);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
