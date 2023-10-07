// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.impl.msgo;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import exhibition.event.RegisterEvent;
import java.util.Iterator;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import exhibition.event.impl.EventPacket;
import net.minecraft.world.World;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import exhibition.util.TeamUtils;
import exhibition.management.friend.FriendManager;
import exhibition.event.impl.EventMotion;
import exhibition.event.Event;
import exhibition.util.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import exhibition.module.data.Setting;
import java.util.HashMap;
import exhibition.module.data.ModuleData;
import net.minecraft.util.Vec3;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.module.Module;

public class Aimbot extends Module
{
    public int ticks;
    public int lookDelay;
    private EntityPlayer target;
    public int buffer;
    private Map<EntityPlayer, List<Vec3>> playerPositions;
    public static boolean isFiring;
    private String ANTISPREAD;
    private String ANTIRECOIL;
    private String YAWRECOIL;
    private String PITCHRECOIL;
    private String SILENT;
    private String DELAY;
    private String AUTOFIRE;
    private String AIMSTEP;
    private String FOV;
    
    public Aimbot(final ModuleData data) {
        super(data);
        this.buffer = 10;
        this.ANTISPREAD = "ANTISPREAD";
        this.ANTIRECOIL = "ANTIRECOIL";
        this.YAWRECOIL = "YAWRECOIL";
        this.PITCHRECOIL = "PITCHRECOIL";
        this.SILENT = "SILENT";
        this.DELAY = "DELAY";
        this.AUTOFIRE = "AUTOFIRE";
        this.AIMSTEP = "AIMSTEP";
        this.FOV = "FOV";
        this.playerPositions = new HashMap<EntityPlayer, List<Vec3>>();
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.ANTISPREAD, new Setting<Boolean>(this.ANTISPREAD, true, "Reduces weapon spread."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.ANTIRECOIL, new Setting<Boolean>(this.ANTIRECOIL, true, "Reduces weapon recoil."));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.SILENT, new Setting<Boolean>(this.SILENT, true, "Aims silently."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.DELAY, new Setting<Integer>(this.DELAY, 3, "Tick delay before firing again. Good for high recoil weapons.", 1.0, 0.0, 10.0));
        ((HashMap<String, Setting<Boolean>>)this.settings).put(this.AUTOFIRE, new Setting<Boolean>(this.AUTOFIRE, true, "Automatically fires for you."));
        ((HashMap<String, Setting<Integer>>)this.settings).put(this.FOV, new Setting<Integer>(this.FOV, 90, "FOV check for ragebot.", 15.0, 0.0, 360.0));
        ((HashMap<String, Setting<Double>>)this.settings).put(this.YAWRECOIL, new Setting<Double>(this.YAWRECOIL, 1.5, "Yaw recoil scale.", 0.1, 0.0, 3.0));
        ((HashMap<String, Setting<Double>>)this.settings).put(this.PITCHRECOIL, new Setting<Double>(this.PITCHRECOIL, 1.5, "Yaw recoil scale.", 0.1, 0.0, 3.0));
    }
    
    public boolean isVisibleFOV(final EntityLivingBase e, final EntityLivingBase e2, final float fov) {
        return ((Math.abs(RotationUtils.getRotations(e)[0] - e2.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(RotationUtils.getRotations(e)[0] - e2.rotationYaw) % 360.0f) : (Math.abs(RotationUtils.getRotations(e)[0] - e2.rotationYaw) % 360.0f)) <= fov;
    }
    
    @RegisterEvent(events = { EventMotion.class, EventPacket.class })
    @Override
    public void onEvent(final Event event) {
        if (Aimbot.isFiring) {
            Aimbot.isFiring = false;
        }
        if (event instanceof EventMotion && Aimbot.mc.thePlayer.isEntityAlive()) {
            final EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                double targetWeight = Double.NEGATIVE_INFINITY;
                this.target = null;
                for (final Object o : Aimbot.mc.theWorld.getLoadedEntityList()) {
                    if (o instanceof EntityPlayer) {
                        final EntityPlayer p = (EntityPlayer)o;
                        if (p == Aimbot.mc.thePlayer || FriendManager.isFriend(p.getName()) || p.ticksExisted <= 20 || p.isInvisible() || TeamUtils.isTeam(Aimbot.mc.thePlayer, p) || !Aimbot.mc.thePlayer.canEntityBeSeen(p) || !this.isVisibleFOV(Aimbot.mc.thePlayer, p, ((HashMap<K, Setting<Number>>)this.settings).get(this.FOV).getValue().floatValue())) {
                            continue;
                        }
                        if (this.target == null) {
                            this.target = p;
                            targetWeight = this.getTargetWeight(p);
                        }
                        else {
                            if (this.getTargetWeight(p) <= targetWeight) {
                                continue;
                            }
                            this.target = p;
                            targetWeight = this.getTargetWeight(p);
                        }
                    }
                }
                for (final Object o2 : this.playerPositions.keySet().toArray()) {
                    final EntityPlayer player = (EntityPlayer)o2;
                    if (!Aimbot.mc.theWorld.playerEntities.contains(player)) {
                        this.playerPositions.remove(player);
                    }
                }
                for (final Object o : Aimbot.mc.theWorld.playerEntities) {
                    final EntityPlayer player2 = (EntityPlayer)o;
                    this.playerPositions.putIfAbsent(player2, new ArrayList<Vec3>());
                    final List<Vec3> previousPositions = this.playerPositions.get(player2);
                    previousPositions.add(new Vec3(player2.posX, player2.posY, player2.posZ));
                    if (previousPositions.size() > this.buffer) {
                        int i = 0;
                        for (final Vec3 position : new ArrayList<Vec3>(previousPositions)) {
                            if (i < previousPositions.size() - this.buffer) {
                                previousPositions.remove(previousPositions.get(i));
                            }
                            ++i;
                        }
                    }
                }
                if (this.target != null) {
                    final boolean recoil = ((HashMap<K, Setting<Boolean>>)this.settings).get(this.ANTIRECOIL).getValue();
                    if (recoil && this.ticks >= 30) {
                        this.ticks = 0;
                    }
                    final EntityLivingBase simulated = (EntityLivingBase)this.predictPlayerMovement(this.target);
                    final float[] rotations = RotationUtils.getRotations(simulated);
                    if (recoil) {
                        final float yaw = rotations[0] + ((HashMap<K, Setting<Number>>)this.settings).get(this.YAWRECOIL).getValue().floatValue() * this.ticks;
                        final float pitch = rotations[1] + ((HashMap<K, Setting<Number>>)this.settings).get(this.PITCHRECOIL).getValue().floatValue() * this.ticks;
                        em.setYaw(yaw);
                        em.setPitch(pitch);
                    }
                    else {
                        em.setYaw(rotations[0]);
                        em.setPitch(rotations[1]);
                    }
                    ++this.lookDelay;
                    if (this.lookDelay >= ((HashMap<K, Setting<Number>>)this.settings).get(this.DELAY).getValue().floatValue()) {
                        Aimbot.isFiring = true;
                        final boolean nospread = ((HashMap<K, Setting<Boolean>>)this.settings).get(this.ANTISPREAD).getValue();
                        if (nospread) {
                            Aimbot.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Aimbot.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        if (((HashMap<K, Setting<Boolean>>)this.settings).get(this.AUTOFIRE).getValue() && Aimbot.mc.thePlayer.inventory.getCurrentItem() != null) {
                            Aimbot.mc.playerController.sendUseItem(Aimbot.mc.thePlayer, Aimbot.mc.theWorld, Aimbot.mc.thePlayer.inventory.getCurrentItem());
                        }
                        if (nospread) {
                            Aimbot.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Aimbot.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                        this.updateAngles(em.getYaw(), em.getPitch());
                        this.lookDelay = 0;
                    }
                }
                else {
                    --this.ticks;
                    if (this.ticks <= 0) {
                        this.ticks = 0;
                    }
                }
            }
        }
        else if (event instanceof EventPacket) {
            final EventPacket ep = (EventPacket)event;
            if (ep.isOutgoing() && ep.getPacket() instanceof C08PacketPlayerBlockPlacement) {
                ++this.ticks;
            }
        }
    }
    
    public void updateAngles(final float yaw, final float pitch) {
        if (Aimbot.mc.gameSettings.thirdPersonView != 0) {
            AntiAim.rotationPitch = pitch;
            Aimbot.mc.thePlayer.rotationYawHead = yaw;
            Aimbot.mc.thePlayer.renderYawOffset = yaw;
        }
    }
    
    public double getTargetWeight(final EntityPlayer p) {
        double weight = -Aimbot.mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        weight -= p.getDistanceToEntity(Aimbot.mc.thePlayer) / 5.0f;
        return weight;
    }
    
    private Entity predictPlayerMovement(final EntityPlayer target) {
        final int pingTicks = (int)Math.ceil(Aimbot.mc.getNetHandler().func_175102_a(Aimbot.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0);
        return this.predictPlayerLocation(target, pingTicks);
    }
    
    public Entity predictPlayerLocation(final EntityPlayer player, final int ticks) {
        if (this.playerPositions.containsKey(player)) {
            final List<Vec3> previousPositions = this.playerPositions.get(player);
            if (previousPositions.size() > 1) {
                final Vec3 origin = previousPositions.get(0);
                final List<Vec3> deltas = new ArrayList<Vec3>();
                Vec3 previous = origin;
                for (final Vec3 position : previousPositions) {
                    deltas.add(new Vec3(position.xCoord - previous.xCoord, position.yCoord - previous.yCoord, position.zCoord - previous.zCoord));
                    previous = position;
                }
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                for (final Vec3 delta : deltas) {
                    x += delta.xCoord * 1.5;
                    y += delta.yCoord;
                    z += delta.zCoord * 1.5;
                }
                x /= deltas.size();
                y /= deltas.size();
                z /= deltas.size();
                final EntityPlayer simulated = new EntityOtherPlayerMP(Aimbot.mc.theWorld, player.getGameProfile());
                simulated.noClip = false;
                simulated.setPosition(player.posX, player.posY + 0.5, player.posZ);
                for (int i = 0; i < ticks; ++i) {
                    simulated.moveEntity(x, y, z);
                }
                return simulated;
            }
        }
        return player;
    }
}
