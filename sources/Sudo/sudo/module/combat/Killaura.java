package sudo.module.combat;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Vec3d;
import sudo.events.EventSendPacket;
import sudo.mixins.accessors.PlayerMoveC2SPacketAccessor;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.module.world.Scaffold;
import sudo.utils.player.RotationUtils;
import sudo.utils.render.ColorUtils;
import sudo.utils.render.RenderUtils;
import sudo.utils.world.Timer;


public class Killaura extends Mod {
    public static ModeSetting rotationmode = new ModeSetting("Rotation", "Silent", "Silent", "Legit");
    public static ModeSetting sorting = new ModeSetting("Sort", "Distance", "Distance", "Health");
    public static NumberSetting range = new NumberSetting("Range", 3, 6, 4, 0.1);
    
	public NumberSetting minAps = new NumberSetting("Minimum APS", 1, 20, 10, 1);
	public NumberSetting maxAps = new NumberSetting("Maximum APS", 1, 20, 15, 1);
	public BooleanSetting random = new BooleanSetting("Random Delay", false);
	
    public static BooleanSetting cooldown = new BooleanSetting("1.9 Cooldown", true);
	public static BooleanSetting crits = new BooleanSetting("Criticals", false);
    public static BooleanSetting swing = new BooleanSetting("Swing", true);

	public static BooleanSetting trigger = new BooleanSetting("Trigger", false);
	
	public static BooleanSetting players = new BooleanSetting("Players", true);
	public static BooleanSetting animals = new BooleanSetting("Animals", false);
	public static BooleanSetting monsters = new BooleanSetting("Monsters", false);
	public static BooleanSetting passives = new BooleanSetting("Passives", false);
	public static BooleanSetting invisibles = new BooleanSetting("Invisibles", false);

	public BooleanSetting esp = new BooleanSetting("Esp", true);
	public ColorSetting espColor = new ColorSetting("Esp", new Color(200,0,0));
	
	public static LivingEntity target;
	private static Timer attackTimer = new Timer();
	private float smoothYaw, smoothPitch;

    public Killaura() {
        super("Killaura", "Automatically attacks entities for you", Category.COMBAT, GLFW.GLFW_KEY_K);
        addSettings(rotationmode, sorting, range, minAps,maxAps,random, cooldown, crits, swing, trigger, players,animals,monsters,passives,invisibles, esp,espColor);
    }
    
    public void onTick() {
		if (maxAps.getValue() <= minAps.getValue()) maxAps.setValue(minAps.getValue() + 1);
        if (this.isEnabled()) {
            if (mc.world != null && mc.world.getEntities() != null) {
				List<LivingEntity> targets = Lists.<LivingEntity>newArrayList();
                for (Entity e : mc.world.getEntities()) {
                    if (e instanceof LivingEntity && e != mc.player && mc.player.distanceTo(e) <= range.getValue())
                        targets.add((LivingEntity) e);
                    else {
                        if (targets.contains(e)) targets.remove(e);
                    }
                }
                
                //remove target if not good target
                if (target != null && mc.player.distanceTo(target) > range.getValue() && !isKillauraEntity(target)) {
					targets.remove(target);
					target = null;
					if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
						RotationUtils.resetPitch();
						RotationUtils.resetYaw();
					}
				}
                
                //sort target
                switch(sorting.getSelected()) {
					case "Distance": 
						targets.sort(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity)));
						break;
					case "Health": 
						targets.sort(Comparator.comparingDouble(entity -> ((LivingEntity)entity).getHealth()));
						break;
				}
                
                if(!targets.isEmpty()) {
                	target = (LivingEntity)targets.get(0);
                    if (target != null) {
						
						this.setDisplayName("Killaura " + ColorUtils.gray + (target instanceof PlayerEntity ? target.getName().getString().replaceAll(ColorUtils.colorChar, "&") : target.getDisplayName().getString()) + "  ");
						if (canAttack(target) && !target.isInvulnerable()) {
							float yaw = RotationUtils.getRotations(target)[0];
							float pitch = RotationUtils.getRotations(target)[1];
							if (smoothYaw != yaw) smoothYaw += RenderUtils.slowDownTo(smoothYaw, yaw, 10);
							if (smoothPitch != pitch) smoothYaw += RenderUtils.slowDownTo(smoothPitch, pitch, 10);
							RotationUtils.setSilentPitch(smoothPitch);
							RotationUtils.setSilentYaw(smoothYaw);

							if (rotationmode.is("Legit")) {
							    mc.player.setYaw(yaw);
							    mc.player.setPitch(pitch); 
							}
							long aps = minAps.getValue() < 20 ? new Random().nextInt((int) (maxAps.getValue() - minAps.getValue())) + (int) minAps.getValue() : 20;
							if (cooldown.isEnabled() && random.isEnabled() && mc.player.getAttackCooldownProgress(0.5F) != 1) attackTimer.reset();
							if(cooldown.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 && (random.isEnabled() ? attackTimer.hasTimeElapsed(new Random().nextInt(300 - 100) + 100, true) : true) : attackTimer.hasTimeElapsed((long) (1000L / aps), true)){
								if (crits.isEnabled()) {
									double posX = mc.player.getX();
									double posY = mc.player.getY();
									double posZ = mc.player.getZ();
								    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY + 0.0633, posZ, false));
								    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY, posZ, false));
								}
								mc.interactionManager.attackEntity(mc.player, target);
								if (swing.isEnabled()) mc.player.swingHand(Hand.MAIN_HAND);
								else mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
								if (swing.isEnabled() && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) mc.player.swingHand(Hand.MAIN_HAND); 
								resetRotation();
							}
	                	}
                    }
                    if (mc.player.squaredDistanceTo(target)>range.getValue()) resetRotation();
                }
            }
        }
    
    }

	double anim = 0;
	double anim2 = 0;
	boolean dir = false;
	boolean filler = false;
	
    @Override
    public void onWorldRender(MatrixStack matrices) {
		if (target != null ) {

			if (anim > 200) {
				dir = false;
			}
			if (anim < 0) {
				dir = true;
			}
			if (dir) {
				anim+=3;
			} else {
				anim-=3;
			}
	    	if (esp.isEnabled() && target.isAlive() && canAttack(target)) RenderUtils.drawCircle(matrices, new Vec3d(target.getX(), target.getY() + anim / 100, target.getZ()), 0.6f, 1, espColor.getColor().getRGB());
		} else {
			anim = 0;
		}
    	super.onWorldRender(matrices);
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
    }
    
	@Override
	public void onDisable() {
		resetRotation();
		target = null;
		super.onDisable();
	}
	
    public void resetRotation() {
    	RotationUtils.resetPitch();
    	RotationUtils.resetYaw();
    }

    @Subscribe
    public void sendPacket(EventSendPacket event) {
    	if (event.getPacket() instanceof PlayerMoveC2SPacket) {
			if (target != null && rotationmode.is("Silent")) {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(RotationUtils.getRotations(target)[0]);
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(RotationUtils.getRotations(target)[1]);
			} else {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(mc.player.getYaw());
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(mc.player.getPitch());
			}
		}
    }
    
	private boolean canAttack(LivingEntity entity) {
		if (entity != mc.player && mc.player.distanceTo(entity) <= range.getValue() && entity.isAlive() && mc.player.isAlive()) {
			if (!trigger.isEnabled()) {
				return isKillauraEntity(entity);
			} else {
				HitResult hitResult = mc.crosshairTarget;
				if (hitResult != null && hitResult.getType() == Type.ENTITY) {
					Entity entity1 = ((EntityHitResult) hitResult).getEntity();
					if (entity1 != null && entity1 == entity) return isKillauraEntity(entity);
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean isKillauraEntity(LivingEntity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof ArmorStandEntity)) return true;
		if (passives.isEnabled() && entity.isInvisible()) return true;
		return false;
	}
}