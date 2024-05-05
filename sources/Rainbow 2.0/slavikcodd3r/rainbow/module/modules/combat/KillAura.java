package slavikcodd3r.rainbow.module.modules.combat;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import slavikcodd3r.rainbow.event.EventManager;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.NetUtil;
import slavikcodd3r.rainbow.utils.RayCastUtil;
import slavikcodd3r.rainbow.utils.TimeHelper;

@Module.Mod(displayName = "KillAura")
public class KillAura extends Module
{
    @Option.Op(name = "Range", min = 1.0, max = 1000.0, increment = 0.1)
    public double krange;
    @Option.Op(name = "Delay", min = 2.0, max = 1000.0, increment = 50.0)
    public double kdelay;
    @Option.Op(name = "Fail Rate", min = 0.0, max = 10.0, increment = 1.0)
    public double kfr;
    @Option.Op(name = "ParticlesAmount", min = 1.0, max = 10.0, increment = 1.0)
    public double particlesamount;
    @Option.Op(name = "AutoBlock")
    private static boolean abk;
    @Option.Op(name = "InteractAutoBlock")
    private static boolean iabk;
    @Option.Op(name = "UnBlock")
    private static boolean unblock;
    @Option.Op(name = "AntiBot")
    private static boolean ab;
    @Option.Op(name = "Teams")
    private static boolean teams;
    @Option.Op(name = "Players")
    private static boolean players;
    @Option.Op(name = "Invisibles")
    private static boolean invisibles;
    @Option.Op(name = "Animals")
    private static boolean animals;
    @Option.Op(name = "Mobs")
    private static boolean mobs;
    @Option.Op(name = "OpenInv")
    private static boolean openinv;
    @Option.Op(name = "ThroughWalls")
    private static boolean tw;
    @Option.Op(name = "NoSwing")
    private static boolean ns;
    @Option.Op(name = "RayCast")
    private static boolean rc;
    @Option.Op(name = "Particles")
    private static boolean particles;
    @Option.Op(name = "AAC")
    private static boolean aac;
    @Option.Op(name = "Reflex")
    private static boolean reflex;
    @Option.Op(name = "AutoDisable")
    private static boolean autodisable;
    @Option.Op(name = "Infinite")
    private static boolean infinite;
    @Option.Op(name = "MoreHit")
    private static boolean morehit;
    @Option.Op(name = "NoSprint")
    private static boolean nosprint;
    @Option.Op(name = "Ignite")
    private static boolean ignite;
    public static Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<String> nobots;
    public TimeHelper time;
    private float[] lastRotations;
    private int rotationSpeed;
    private boolean rotated;
    public static Entity attacked = null;
    public int fr;
    int delay;
    
    public KillAura() {
        this.nobots = new ArrayList<String>();
        this.time = new TimeHelper();
        this.lastRotations = new float[2];
        this.rotationSpeed = 1;
        this.rotated = false;
        this.fr = 0;
        this.delay = 0;
        this.krange = 4.0;
        this.kdelay = 83.0;
        this.particlesamount = 1.0;
        KillAura.players = true;
        KillAura.abk = true;
        KillAura.ab = true;
        KillAura.rc = true;
        KillAura.particles = true;
    }
    
    public void enable() {
    	EventManager.register(this);    	
    	time.reset();
    	super.enable();
    }
    
    public void disable() {
        EventManager.unregister(this);
        this.fr = 0;
        super.disable();
    }
    
    @EventTarget
    public void onPacket(final PacketSendEvent event) {
        final Packet packet = event.getPacket();
        if (packet instanceof C03PacketPlayer) {
            for (final Object j : mc.theWorld.loadedEntityList) {
                final Entity e = (Entity)j;
                if (this.isValid(e)) {
                    if (e == null) {
                        this.lastRotations[0] = mc.thePlayer.rotationYaw;
                        this.lastRotations[1] = mc.thePlayer.rotationPitch;
                        return;
                    }
                    this.rotated = false;
                    final float[] facing = this.getRotations(getCenter(e.getEntityBoundingBox()));
                    this.lastRotations[0] = facing[0];
                    this.lastRotations[1] = facing[1];
                    if (((EntityLivingBase)e).deathTime != 0) {
                        continue;
                    }
                    if (!this.reflex && !this.aac) {
                    C03PacketPlayer.yaw = facing[0];
                    C03PacketPlayer.pitch = facing[1];
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    if (ModuleManager.getModule("TargetStrafe").getInstance().isEnabled()) {
                    mc.thePlayer.rotationYaw = facing[0];	
                    }
                }
            }
            }
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
    	if (!this.infinite) {
        this.setSuffix(new StringBuilder(String.valueOf("Multi")).toString());
    	} else {
    	this.setSuffix(new StringBuilder(String.valueOf("Infinite")).toString());
    	}
        if (this.reflex) {
            for (final Object j : mc.theWorld.loadedEntityList) {
                final Entity e = (Entity)j;
                if (this.isValid(e)) {
                    if (e == null) {
                        this.lastRotations[0] = mc.thePlayer.rotationYaw;
                        this.lastRotations[1] = mc.thePlayer.rotationPitch;
                        return;
                    }
                    this.rotated = false;
                    final float[] facing = this.getRotations(getCenter(e.getEntityBoundingBox()));
                    this.lastRotations[0] = facing[0];
                    this.lastRotations[1] = facing[1];
                    if (((EntityLivingBase)e).deathTime != 0) {
                        continue;
                    }
                    mc.thePlayer.rotationYaw = facing[0];
                    mc.thePlayer.rotationPitch = facing[1];
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                		final float randomYaw = (float) (Math.random() * 1);
                		final float randomPitch = (float) (Math.random() * 1);
                        mc.thePlayer.rotationYaw += randomYaw;
                        mc.thePlayer.rotationPitch += randomPitch;
                        mc.thePlayer.rotationYawHead += randomYaw;
                        mc.thePlayer.renderYawOffset += randomYaw;
                    } else {
                		final float randomYaw = (float) (Math.random() * 1);
                		final float randomPitch = (float) (Math.random() * 1);
                        mc.thePlayer.rotationYaw -= randomYaw;
                        mc.thePlayer.rotationPitch -= randomPitch;
                        mc.thePlayer.rotationYawHead -= randomYaw;
                        mc.thePlayer.renderYawOffset -= randomYaw;
                    }
                }
            }
        }
        if (this.aac) {
            for (final Object j : mc.theWorld.loadedEntityList) {
                final Entity e = (Entity)j;
                if (this.isValid(e)) {
                    if (e == null) {
                        this.lastRotations[0] = mc.thePlayer.rotationYaw;
                        this.lastRotations[1] = mc.thePlayer.rotationPitch;
                        return;
                    }
                    this.rotated = false;
                    final float[] facing = this.getRotations(getCenter(e.getEntityBoundingBox()));
                    this.lastRotations[0] = facing[0];
                    this.lastRotations[1] = facing[1];
                    if (((EntityLivingBase)e).deathTime != 0) {
                        continue;
                    }
                    event.setYaw(facing[0]);
                    event.setPitch(facing[1]);
                    mc.thePlayer.rotationYawHead = facing[0];
                    mc.thePlayer.renderYawOffset = facing[0];
                    if (ModuleManager.getModule("TargetStrafe").getInstance().isEnabled()) {
                    mc.thePlayer.rotationYaw = facing[0];	
                    }
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                		final float randomYaw = (float) (facing[0] + Math.random() * 1);
                		final float randomPitch = (float) (facing[1] + Math.random() * 1);
                		event.setYaw(randomYaw);
                		event.setPitch(randomPitch);
                        mc.thePlayer.rotationYawHead = randomYaw;
                        mc.thePlayer.renderYawOffset = randomYaw;
                    } else {
                		final float randomYaw = (float) (facing[0] - Math.random() * 1);
                		final float randomPitch = (float) (facing[1] - Math.random() * 1);
                		event.setYaw(randomYaw);
                		event.setPitch(randomPitch);
                        mc.thePlayer.rotationYawHead = randomYaw;
                        mc.thePlayer.renderYawOffset = randomYaw;
                    }
                }
            }
        }
        if (this.autodisable && mc.thePlayer.ticksExisted <= 1 || this.autodisable && mc.currentScreen instanceof GuiGameOver) {
        	ClientUtils.sendMessage("KillAura disabled due to death");
        	this.toggle();
        }
        final int failrate = (int)this.kfr;
        for (final Object j : mc.theWorld.loadedEntityList) {
            Entity e = (Entity)j;
            if (e instanceof EntityLivingBase && e.onGround && !this.nobots.contains(new StringBuilder().append(e.getEntityId()).toString())) {
                this.nobots.add(new StringBuilder().append(e.getEntityId()).toString());
            }
            if (this.isValid(e)) {
            	if (this.infinite) {
            		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(e.posX, e.posY, e.posZ, false));
            	}
                if (this.abk) {
                    final int range = (int)this.krange;
                    if (mc.thePlayer.getDistanceToEntity(e) <= range) {
                        try {
                        	if (!this.iabk) {
                        		if (ClientUtils.player().getCurrentEquippedItem() != null && ClientUtils.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                                    ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getCurrentEquippedItem());
                        		}
                        	} else {
                            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                                mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), (int) (Math.random() * 100));                                
                            }
                        }
                        }
                        catch (Exception ex) {}
                    }
                }               
                if (this.unblock) {
                    final int range = (int)this.krange;
                    if (mc.thePlayer.getDistanceToEntity(e) <= range) {
                    	if (mc.thePlayer.isBlocking()) {
                    		mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    	}
                    }
                }
                final Random rdm = new Random();
                this.delay = rdm.nextInt((int)this.kdelay);
                if (this.delay < this.kdelay * 0.5) {
                    this.delay = (int)this.kdelay;
                }
                if (!this.time.hasReached(this.delay)) {
                    continue;
                }
                if (((EntityLivingBase)e).deathTime == 0) {
                    try {
                        if (this.rc) {
                            final MovingObjectPosition objectMouseOver = RayCastUtil.getMouseOver(this.lastRotations[0], this.lastRotations[1], (float)this.krange);
                            if (objectMouseOver.entityHit != null && e != objectMouseOver.entityHit && objectMouseOver.entityHit instanceof EntityLivingBase) {
                                e = objectMouseOver.entityHit;
                            }
                        }
                    }
                    catch (Exception ex2) {}
                    final int particlesamount = (int)this.particlesamount;
                    if (failrate != 0 && failrate != 1) {
                        if (this.fr < failrate) {
                        	if (this.ns) {
                        		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        	}
                        	else {
                        		mc.thePlayer.swingItem();
                        	}
                            if (KillAura.nosprint) {
                            	if (this.morehit) {
                        	        if (mc.thePlayer.ticksExisted % 2 == 0) {                      	        
                                    mc.playerController.attackEntity(mc.thePlayer, e);
                                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                    mc.thePlayer.swingItem();
                            	    if (mc.thePlayer.ticksExisted % 2 == 0) {
                                    mc.playerController.attackEntity(mc.thePlayer, e);	
                                    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                    mc.thePlayer.swingItem();
                            	    }
                            	}
                            	} else {                            		
                            	mc.playerController.attackEntity(mc.thePlayer, e);
                            	}
                            } else {
                            	if (this.morehit) {
                        	        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                            		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                    mc.thePlayer.swingItem();
                        	        if (mc.thePlayer.ticksExisted % 2 == 0) {
                        	        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                        	        	mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());   
                                        mc.thePlayer.swingItem();
                        	        }
                        	        }
                            	} else {
                            	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                            	}
                            }
                            if (this.fr < failrate) {
                                ++this.fr;
                            }
                            if (this.particles) {
                                for (int i = 0; i < particlesamount; ++i) {
                                    mc.effectRenderer.func_178926_a(e, EnumParticleTypes.CRIT);
                                }
                            }
                            attacked = e;
                        }
                        else {
                            this.fr = 0;
                        }
                    }
                    else {
                    	if (this.ns) {
                    		mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    	}
                    	else {
                    		mc.thePlayer.swingItem();
                    	}
                    	if (KillAura.nosprint) {
                        	if (this.morehit) {
                    	        if (mc.thePlayer.ticksExisted % 2 == 0) {
                                mc.playerController.attackEntity(mc.thePlayer, e);
                        	    if (mc.thePlayer.ticksExisted % 2 == 0) {
                                mc.playerController.attackEntity(mc.thePlayer, e);	
                        	    }
                        	}
                        	} else {                            		
                        	mc.playerController.attackEntity(mc.thePlayer, e);
                        	}
                        } else {
                        	if (this.morehit) {
                    	        if (mc.thePlayer.ticksExisted % 1 == 0) {
                        		mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                    	        if (mc.thePlayer.ticksExisted % 1 == 0) {
                    	        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                    	        }
                    	        }
                        	} else {
                        	mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                        	}
                        }
                        if (this.particles) {
                            for (int i = 0; i < particlesamount; ++i) {
                                mc.effectRenderer.func_178926_a(e, EnumParticleTypes.CRIT);
                            }
                        }
                        attacked = e;
                    }
                }
                this.time.reset();
            }
        }
    }
    
    public boolean isBot(final Entity e) {
        return !this.nobots.contains(new StringBuilder().append(e.getEntityId()).toString());
    }
    
    public boolean isValid(final Entity e) {
        return e instanceof EntityLivingBase && !FriendManager.isFriend(e.getName()) && (this.invisibles || !(e.isInvisible())) && (this.players || !(e instanceof EntityPlayer)) && (this.animals || !(e instanceof EntityAnimal)) && (this.mobs || !(e instanceof EntityMob)) && e != mc.thePlayer && !(e instanceof EntityVillager) && mc.thePlayer.getDistanceToEntity(e) <= this.krange && !e.getName().contains("#")  && (!this.teams || !e.getDisplayName().getFormattedText().startsWith("§" + mc.thePlayer.getDisplayName().getFormattedText().charAt(1))) && (!this.ab || !this.isBot(e)) && !e.getName().toLowerCase().contains("shop") && (this.tw || mc.thePlayer.canEntityBeSeen(e)) && (this.openinv || !(mc.currentScreen instanceof Gui));
    }
    
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }
    
    public float[] getRotations(final Vec3 vec) {
        final Vec3 eyesPos = this.getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch) };
    }
    
    public Vec3 getEyesPos() {
        return new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    }
    
    public static double myRound(final double wert, final int stellen) {
        return Math.round(wert * Math.pow(10.0, stellen)) / Math.pow(10.0, stellen);
    }
}
