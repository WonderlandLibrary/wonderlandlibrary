package sudo.module.combat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import sudo.Client;
import sudo.core.event.EventTarget;
import sudo.events.EventParticle;
import sudo.events.EventSendPacket;
import sudo.mixins.accessors.PlayerMoveC2SPacketAccessor;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;
import sudo.utils.player.DamageUtils;
import sudo.utils.player.RotationUtils;
import sudo.utils.world.EntityUtils;
import sudo.utils.world.InventoryUtils;

public class CrystalAura extends Mod {

	private int breakCooldown = 0;
	private int placeCooldown = 0;
	private Map<BlockPos, Integer> blacklist = new HashMap<>();
	private List<LivingEntity> targets;
	private Vec3d lookVec;

    
	public BooleanSetting explode = new BooleanSetting("Explode", true);
	public BooleanSetting antiWeak = new BooleanSetting("Anti Weakness", true);
	public BooleanSetting antiSuicide = new BooleanSetting("Anti Suicide", true);
	public NumberSetting aps = new NumberSetting("APS", 1, 30, 10, 1);
	public NumberSetting attackDelay = new NumberSetting("Attack Delay", 0, 30, 1, 0.1);
	public NumberSetting  minHp = new NumberSetting("Min Health", 0, 36, 2, 1);

	public BooleanSetting AKsound = new BooleanSetting("AK Sound", false);
	public BooleanSetting place = new BooleanSetting("Place", true);
	public NumberSetting cps = new NumberSetting("Crystals/s", 0, 30, 0, 1);
	public BooleanSetting autoSwitch = new BooleanSetting("Switch", true);
	public BooleanSetting switchBack = new BooleanSetting("Switch Back", true);
	public BooleanSetting oneDotTwelve = new BooleanSetting("1.12 Place", false);
	public BooleanSetting blacklistSet = new BooleanSetting("Blacklist", true);
	public BooleanSetting raycast = new BooleanSetting("Raycast", false);
	public NumberSetting minDamage = new NumberSetting("Min Damage", 1, 20, 2, 1);
	public NumberSetting minRatio = new NumberSetting("Min Ratio", 0.5, 6, 2, 0.5);
	public NumberSetting placeDelay = new NumberSetting("Place Delay", 0, 30, 2, 0.1);
//	public ColorSetting color = new ColorSetting("Place Color", new Color(255, 0, 0));
	
	public BooleanSetting sameTick = new BooleanSetting("Same Tick", true);
	
	public BooleanSetting rotate = new BooleanSetting("Rotate", true);
	
	public NumberSetting range = new NumberSetting("Range", 0, 6, 4.5, 0.1);
	
	public CrystalAura() {
		super("CrystalAura", "Automatically places and breakes crystals near players to kill them", Category.COMBAT, 0);
		addSettings(AKsound, explode, antiWeak, antiSuicide, aps, attackDelay, minHp, place, cps, autoSwitch, switchBack, oneDotTwelve, blacklistSet, raycast, minDamage, minRatio, placeDelay, sameTick, rotate, range);
	}

	public void onTick() {
		try {
			breakCooldown = Math.max(0, breakCooldown - 1);
			placeCooldown = Math.max(0, placeCooldown - 1);
	
			List<LivingEntity> targets = Streams.stream(mc.world.getEntities())
					.filter(e -> e instanceof PlayerEntity)
					.filter(e -> EntityUtils.isAttackable(e, true))
					.map(e -> (LivingEntity) e)
					.collect(Collectors.toList());
	
			if (targets.isEmpty()) {
				return;
			}
			
			this.targets = targets;
			
			for (Entry<BlockPos, Integer> e : new HashMap<>(blacklist).entrySet()) {
				if (e.getValue() > 0) {
					blacklist.replace(e.getKey(), e.getValue() - 1);
				} else {
					blacklist.remove(e.getKey());
				}
			}
	
			if (mc.player.isUsingItem() && mc.player.getMainHandStack().isFood()) {
				return;
			}
	
			// Explode
			List<EndCrystalEntity> nearestCrystals = Streams.stream(mc.world.getEntities())
					.filter(e -> e instanceof EndCrystalEntity)
					.map(e -> (EndCrystalEntity) e)
					.sorted(Comparator.comparing(mc.player::distanceTo))
					.collect(Collectors.toList());
	
			int breaks = 0;
			if (explode.isEnabled() && !nearestCrystals.isEmpty() && breakCooldown <= 0) {
				boolean end = false;
				for (EndCrystalEntity c : nearestCrystals) {
					if (mc.player.distanceTo(c) > range.getValue()
							|| mc.world.getOtherEntities(null, new Box(c.getPos(), c.getPos()).expand(7), targets::contains).isEmpty())
						continue;
	
					float damage = DamageUtils.getExplosionDamage(c.getPos(), 6f, mc.player);
					if (DamageUtils.willGoBelowHealth(mc.player, damage, (float)minHp.getValue()))
						continue;
	
					int oldSlot = mc.player.getInventory().selectedSlot;
					if (antiWeak.isEnabled() && mc.player.hasStatusEffect(StatusEffects.WEAKNESS)) {
						InventoryUtils.selectSlot(false, true, Comparator.comparing(i -> DamageUtils.getItemAttackDamage(mc.player.getInventory().getStack(i))));
					}
	
					if (rotate.isEnabled()) {
						Vec3d eyeVec = mc.player.getEyePos();
						Vec3d v = new Vec3d(c.getX(), c.getY() + 0.5, c.getZ());
						for (Direction d : Direction.values()) {
							Vec3d vd = RotationUtils.getLegitLookPos(c.getBoundingBox(), d, true, 5, -0.001);
							if (vd != null && eyeVec.distanceTo(vd) <= eyeVec.distanceTo(v)) {
								v = vd;
							}
						}
	
						double[] rots = new double[] {RotationUtils.getYaw(v), RotationUtils.getPitch(v)};
						RotationUtils.setSilentYaw((float)rots[0]);
						RotationUtils.setSilentPitch((float)rots[1]);
						lookVec = v;
					}
	
					mc.interactionManager.attackEntity(mc.player, c);
					mc.player.swingHand(Hand.MAIN_HAND);
					
					if (AKsound.isEnabled()) {
						float min = 0.8f, max = 1.5f;
						Random random = new Random();
						float randomPitch = min + random.nextFloat() * (max-min);
			            mc.getSoundManager().play(PositionedSoundInstance.master(Client.MY_SOUND_EVENT, randomPitch));
					}
					
					c.remove(null);
		            blacklist.remove(c.getBlockPos().down());

					InventoryUtils.selectSlot(oldSlot);
	
					end = true;
					breaks++;
					if (breaks >= aps.getValue()) {
						break;
					}
				}
	
				breakCooldown = (int)attackDelay.getValue() + 1;
	
				if (!sameTick.isEnabled() && end) {
					return;
				}
			}
	
			// Place
			if (place.isEnabled() && placeCooldown <= 0) {
				
				int crystalSlot = !autoSwitch.isEnabled()
						? (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL ? mc.player.getInventory().selectedSlot
								: mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL ? 40
										: -1)
								: InventoryUtils.getSlot(true, i -> mc.player.getInventory().getStack(i).getItem() == Items.END_CRYSTAL);
	
				if (crystalSlot == -1) {
					return;
				}
	
				Map<BlockPos, Float> placeBlocks = new LinkedHashMap<>();
	
				for (Vec3d v : getCrystalPoses()) {
					float playerDamg = DamageUtils.getExplosionDamage(v, 6f, mc.player);
	
					if (DamageUtils.willKill(mc.player, playerDamg))
						continue;
	
					for (LivingEntity e : targets) {
						
						float targetDamg = DamageUtils.getExplosionDamage(v, 6f, e);
						if (DamageUtils.willPop(mc.player, playerDamg) && !DamageUtils.willPopOrKill(e, targetDamg)) {
							continue;
						}
	
						if (targetDamg >= minDamage.getValue()) {
							float ratio = playerDamg == 0 ? targetDamg : targetDamg / playerDamg;
	
							if (ratio > minRatio.getValue()) {
								placeBlocks.put(new BlockPos(v).down(), ratio);
							}
						}
					}
				}
	
				placeBlocks = placeBlocks.entrySet().stream()
						.sorted((b1, b2) -> Float.compare(b2.getValue(), b1.getValue()))
						.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (x, y) -> y, LinkedHashMap::new));
	
				int oldSlot = mc.player.getInventory().selectedSlot;
				int places = 0;
				for (Entry<BlockPos, Float> e : placeBlocks.entrySet()) {
					BlockPos block = e.getKey();
	
					Vec3d eyeVec = mc.player.getEyePos();
	
					Vec3d vec = Vec3d.ofCenter(block, 1);
					Direction dir = null;
					for (Direction d : Direction.values()) {
						Vec3d vd = RotationUtils.getLegitLookPos(block, d, true, 5);
						if (vd != null && eyeVec.distanceTo(vd) <= eyeVec.distanceTo(vec)) {
							vec = vd;
							dir = d;
						}
					}
	
					if (dir == null) {
						if (raycast.isEnabled())
							continue;
	
						dir = Direction.UP;
					}
	
					if (blacklistSet.isEnabled())
						blacklist.put(block, 4);
	
					if (rotate.isEnabled()) {
						double[] rots = new double[] {RotationUtils.getYaw(vec), RotationUtils.getPitch(vec)};
						RotationUtils.setSilentYaw((float)rots[0]);
						RotationUtils.setSilentPitch((float)rots[1]);
						lookVec = vec;
					}
	
					Hand hand = InventoryUtils.selectSlot(crystalSlot);
	
					if (canPlace(block)) mc.interactionManager.interactBlock(mc.player, hand, new BlockHitResult(vec, dir, block, false));
	
					places++;
					if (places >= (int)cps.getValue()) {
						break;
					}
				}
	
				if (places > 0) {
					if (autoSwitch.isEnabled()
							&& switchBack.isEnabled()) {
						InventoryUtils.selectSlot(oldSlot);
					}
	
					placeCooldown = (int)placeDelay.getValue() + 1;
				}
			}
		} catch(Exception e) {
//			e.printStackTrace();
		}
	}

//	@EventTarget
//	public void onRenderWorld(EventRender3D event) {
//		if (this.render != null && canPlace(render)) {
//			float[] col = color.getRGBFloat();
//			RenderUtils.drawBoxBoth(render, QuadColor.single(col[0], col[1], col[2], 0.4f), 2.5f);
//		}
//	}

	public Set<Vec3d> getCrystalPoses() {
		Set<Vec3d> poses = new HashSet<>();

		int range = (int) Math.floor(this.range.getValue());
		for (int x = -range; x <= range; x++) {
			for (int y = -range; y <= range; y++) {
				for (int z = -range; z <= range; z++) {
					BlockPos basePos = new BlockPos(mc.player.getEyePos()).add(x, y, z);

					if (!canPlace(basePos) || (blacklist.containsKey(basePos) && blacklistSet.isEnabled()))
						continue;

					if (raycast.isEnabled()) {
						boolean allBad = true;
						for (Direction d : Direction.values()) {
							if (RotationUtils.getLegitLookPos(basePos, d, true, 5) != null) {
								allBad = false;
								break;
							}
						}

						if (allBad) {
							continue;
						}
					}

					if (mc.player.getPos().distanceTo(Vec3d.of(basePos).add(0.5, 1, 0.5)) <= this.range.getValue() + 0.25)
						poses.add(Vec3d.of(basePos).add(0.5, 1, 0.5));
				}
			}
		}

		return poses;
	}

	private boolean canPlace(BlockPos basePos) {
		BlockState baseState = mc.world.getBlockState(basePos);

		if (baseState.getBlock() != Blocks.BEDROCK && baseState.getBlock() != Blocks.OBSIDIAN)
			return false;

		boolean oldPlace = oneDotTwelve.isEnabled();
		BlockPos placePos = basePos.up();
		if (!mc.world.isAir(placePos) || (oldPlace && !mc.world.isAir(placePos.up())))
			return false;

		return mc.world.getOtherEntities(null, new Box(placePos, placePos.up(oldPlace ? 2 : 1))).isEmpty();
	}
	
	@Override
	public void onTickDisabled() {
		antiWeak.setVisible(explode.isEnabled());
		antiSuicide.setVisible(explode.isEnabled());
		aps.setVisible(explode.isEnabled());
		attackDelay.setVisible(explode.isEnabled());
		minHp.setVisible(explode.isEnabled());
		autoSwitch.setVisible(place.isEnabled());
		switchBack.setVisible(place.isEnabled() && autoSwitch.isEnabled());
		oneDotTwelve.setVisible(place.isEnabled());
		blacklistSet.setVisible(place.isEnabled());
		raycast.setVisible(place.isEnabled());
		minDamage.setVisible(place.isEnabled());
		minRatio.setVisible(place.isEnabled());
		cps.setVisible(place.isEnabled());
		placeDelay.setVisible(place.isEnabled());
//		color.setVisible(place.isEnabled());
		super.onTickDisabled();
	}
	
	@EventTarget
	public void sendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof PlayerMoveC2SPacket) {
			if (targets != null && !targets.isEmpty() && lookVec != null) {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw((float)RotationUtils.getYaw(lookVec));
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch((float)RotationUtils.getPitch(lookVec));
			} else {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(mc.player.getYaw());
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(mc.player.getPitch());
			}
		}
	}
	
	@EventTarget
	public void eventParticle(EventParticle.Normal event) {
		if (event.getParticle() instanceof ExplosionLargeParticle) event.setCancelled(true);
	}
}