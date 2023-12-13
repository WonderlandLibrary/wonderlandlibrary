/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package me.arithmo.module.impl.msgo;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.arithmo.event.Event;
import me.arithmo.event.RegisterEvent;
import me.arithmo.event.impl.EventMotion;
import me.arithmo.event.impl.EventPacket;
import me.arithmo.management.friend.FriendManager;
import me.arithmo.module.Module;
import me.arithmo.module.data.ModuleData;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.module.impl.msgo.AntiAim;
import me.arithmo.util.RotationUtils;
import me.arithmo.util.TeamUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Aimbot
extends Module {
    public int ticks;
    public int lookDelay;
    private EntityPlayer target;
    public int buffer = 10;
    private Map<EntityPlayer, List<Vec3>> playerPositions = new HashMap<EntityPlayer, List<Vec3>>();
    public static boolean isFiring;
    private String ANTISPREAD = "ANTISPREAD";
    private String ANTIRECOIL = "ANTIRECOIL";
    private String YAWRECOIL = "YAWRECOIL";
    private String PITCHRECOIL = "PITCHRECOIL";
    private String SILENT = "SILENT";
    private String DELAY = "DELAY";
    private String AUTOFIRE = "AUTOFIRE";
    private String AIMSTEP = "AIMSTEP";
    private String FOV = "FOV";

    public Aimbot(ModuleData data) {
        super(data);
        this.settings.put(this.ANTISPREAD, new Setting<Boolean>(this.ANTISPREAD, true, "Reduces weapon spread."));
        this.settings.put(this.ANTIRECOIL, new Setting<Boolean>(this.ANTIRECOIL, true, "Reduces weapon recoil."));
        this.settings.put(this.SILENT, new Setting<Boolean>(this.SILENT, true, "Aims silently."));
        this.settings.put(this.DELAY, new Setting<Integer>(this.DELAY, 3, "Tick delay before firing again. Good for high recoil weapons.", 1.0, 0.0, 10.0));
        this.settings.put(this.AUTOFIRE, new Setting<Boolean>(this.AUTOFIRE, true, "Automatically fires for you."));
        this.settings.put(this.FOV, new Setting<Integer>(this.FOV, 90, "FOV check for ragebot.", 15.0, 0.0, 360.0));
        this.settings.put(this.YAWRECOIL, new Setting<Double>(this.YAWRECOIL, 1.5, "Yaw recoil scale.", 0.1, 0.0, 3.0));
        this.settings.put(this.PITCHRECOIL, new Setting<Double>(this.PITCHRECOIL, 1.5, "Yaw recoil scale.", 0.1, 0.0, 3.0));
    }

    public boolean isVisibleFOV(EntityLivingBase e, EntityLivingBase e2, float fov) {
        return (Math.abs(RotationUtils.getRotations(e)[0] - e2.rotationYaw) % 360.0f > 180.0f ? 360.0f - Math.abs(RotationUtils.getRotations(e)[0] - e2.rotationYaw) % 360.0f : Math.abs(RotationUtils.getRotations(e)[0] - e2.rotationYaw) % 360.0f) <= fov;
    }

    @RegisterEvent(events={EventMotion.class, EventPacket.class})
    public void onEvent(Event event) {
        EventPacket ep;
        if (isFiring) {
            isFiring = false;
        }
        if (event instanceof EventMotion && Aimbot.mc.thePlayer.isEntityAlive()) {
            EventMotion em = (EventMotion)event;
            if (em.isPre()) {
                double targetWeight = Double.NEGATIVE_INFINITY;
                this.target = null;
                for (Object o : Aimbot.mc.theWorld.getLoadedEntityList()) {
                    EntityPlayer p;
                    if (!(o instanceof EntityPlayer) || (p = (EntityPlayer)o) == Aimbot.mc.thePlayer || FriendManager.isFriend(p.getName()) || p.ticksExisted <= 20 || p.isInvisible() || TeamUtils.isTeam(Aimbot.mc.thePlayer, p) || !Aimbot.mc.thePlayer.canEntityBeSeen(p) || !this.isVisibleFOV(Aimbot.mc.thePlayer, p, ((Number)((Setting)this.settings.get(this.FOV)).getValue()).floatValue())) continue;
                    if (this.target == null) {
                        this.target = p;
                        targetWeight = this.getTargetWeight(p);
                        continue;
                    }
                    if (this.getTargetWeight(p) <= targetWeight) continue;
                    this.target = p;
                    targetWeight = this.getTargetWeight(p);
                }
                for (Object o : this.playerPositions.keySet().toArray()) {
                    EntityPlayer player = (EntityPlayer)o;
                    if (Aimbot.mc.theWorld.playerEntities.contains(player)) continue;
                    this.playerPositions.remove(player);
                }
                for (Object o : Aimbot.mc.theWorld.playerEntities) {
                    EntityPlayer player = (EntityPlayer)o;
                    this.playerPositions.putIfAbsent(player, new ArrayList());
                    List<Vec3> previousPositions = this.playerPositions.get(player);
                    previousPositions.add(new Vec3(player.posX, player.posY, player.posZ));
                    if (previousPositions.size() <= this.buffer) continue;
                    int i = 0;
                    for (Vec3 position : new ArrayList<Vec3>(previousPositions)) {
                        if (i < previousPositions.size() - this.buffer) {
                            previousPositions.remove(previousPositions.get(i));
                        }
                        ++i;
                    }
                }
                if (this.target != null) {
                    boolean recoil = (Boolean)((Setting)this.settings.get(this.ANTIRECOIL)).getValue();
                    if (recoil && this.ticks >= 30) {
                        this.ticks = 0;
                    }
                    EntityLivingBase simulated = (EntityLivingBase)this.predictPlayerMovement(this.target);
                    float[] rotations = RotationUtils.getRotations(simulated);
                    if (recoil) {
                        float yaw = rotations[0] + ((Number)((Setting)this.settings.get(this.YAWRECOIL)).getValue()).floatValue() * (float)this.ticks;
                        float pitch = rotations[1] + ((Number)((Setting)this.settings.get(this.PITCHRECOIL)).getValue()).floatValue() * (float)this.ticks;
                        em.setYaw(yaw);
                        em.setPitch(pitch);
                    } else {
                        em.setYaw(rotations[0]);
                        em.setPitch(rotations[1]);
                    }
                    ++this.lookDelay;
                    if ((float)this.lookDelay >= ((Number)((Setting)this.settings.get(this.DELAY)).getValue()).floatValue()) {
                        isFiring = true;
                        boolean nospread = (Boolean)((Setting)this.settings.get(this.ANTISPREAD)).getValue();
                        if (nospread) {
                            Aimbot.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Aimbot.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        if (((Boolean)((Setting)this.settings.get(this.AUTOFIRE)).getValue()).booleanValue() && Aimbot.mc.thePlayer.inventory.getCurrentItem() != null) {
                            Aimbot.mc.playerController.sendUseItem(Aimbot.mc.thePlayer, Aimbot.mc.theWorld, Aimbot.mc.thePlayer.inventory.getCurrentItem());
                        }
                        if (nospread) {
                            Aimbot.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Aimbot.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                        this.updateAngles(em.getYaw(), em.getPitch());
                        this.lookDelay = 0;
                    }
                } else {
                    --this.ticks;
                    if (this.ticks <= 0) {
                        this.ticks = 0;
                    }
                }
            }
        } else if (event instanceof EventPacket && (ep = (EventPacket)event).isOutgoing() && ep.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            ++this.ticks;
        }
    }

    public void updateAngles(float yaw, float pitch) {
        if (Aimbot.mc.gameSettings.thirdPersonView != 0) {
            AntiAim.rotationPitch = pitch;
            Aimbot.mc.thePlayer.rotationYawHead = yaw;
            Aimbot.mc.thePlayer.renderYawOffset = yaw;
            return;
        }
    }

    public double getTargetWeight(EntityPlayer p) {
        double weight = - Aimbot.mc.thePlayer.getDistanceToEntity(p);
        if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
            weight += 200.0;
        }
        return weight -= (double)(p.getDistanceToEntity(Aimbot.mc.thePlayer) / 5.0f);
    }

    private Entity predictPlayerMovement(EntityPlayer target) {
        int pingTicks = (int)Math.ceil((double)mc.getNetHandler().func_175102_a(Aimbot.mc.thePlayer.getUniqueID()).getResponseTime() / 50.0);
        return this.predictPlayerLocation(target, pingTicks);
    }

    public Entity predictPlayerLocation(EntityPlayer player, int ticks) {
        List<Vec3> previousPositions;
        if (this.playerPositions.containsKey(player) && (previousPositions = this.playerPositions.get(player)).size() > 1) {
            Vec3 origin = previousPositions.get(0);
            ArrayList<Vec3> deltas = new ArrayList<Vec3>();
            Vec3 previous = origin;
            for (Vec3 position : previousPositions) {
                deltas.add(new Vec3(position.xCoord - previous.xCoord, position.yCoord - previous.yCoord, position.zCoord - previous.zCoord));
                previous = position;
            }
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            for (Vec3 delta : deltas) {
                x += delta.xCoord * 1.5;
                y += delta.yCoord;
                z += delta.zCoord * 1.5;
            }
            x /= (double)deltas.size();
            y /= (double)deltas.size();
            z /= (double)deltas.size();
            EntityOtherPlayerMP simulated = new EntityOtherPlayerMP(Aimbot.mc.theWorld, player.getGameProfile());
            simulated.noClip = false;
            simulated.setPosition(player.posX, player.posY + 0.5, player.posZ);
            for (int i = 0; i < ticks; ++i) {
                simulated.moveEntity(x, y, z);
            }
            return simulated;
        }
        return player;
    }
}

