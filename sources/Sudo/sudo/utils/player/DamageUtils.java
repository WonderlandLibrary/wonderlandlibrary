package sudo.utils.player;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.explosion.Explosion;
import sudo.core.event.EventTarget;
import sudo.core.managers.EventManager;
import sudo.events.EventJoinGame;
import sudo.utils.mixins.IExplosion;
import sudo.utils.mixins.IRaycastContext;
import sudo.utils.mixins.IVec3d;
//import skiddedclient.event.EventManager;
//import skiddedclient.event.EventTarget;
//import skiddedclient.event.events.EventJoinGame;
//import skiddedclient.utils.mixin.IVec3d;
//import skiddedclient.utils.world.WorldUtils;
//import skiddedclient.utils.mixin.IExplosion;
//import skiddedclient.utils.mixin.IRaycastContext;
import sudo.utils.world.FakePlayerEntity;
import sudo.utils.world.WorldUtils;

public class DamageUtils {
	public static MinecraftClient mc = MinecraftClient.getInstance();

	
	private static final Vec3d vec3d = new Vec3d(0, 0, 0);
    private static Explosion explosion;
    private static RaycastContext raycastContext;
    private static DamageUtils instance = new DamageUtils();
    
    public DamageUtils() {
    	EventManager.INSTANCE.register(this);
    }
    
    public static DamageUtils getInstance() {
    	return instance;
    }
    
	@EventTarget
    private static void onGameJoined(EventJoinGame event) {
        explosion = new Explosion(mc.world, null, 0, 0, 0, 6, false, Explosion.DestructionType.DESTROY);
        raycastContext = new RaycastContext(null, null, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, mc.player);
    }
	
	public double crystalDamage(PlayerEntity player, Vec3d crystal, boolean predictMovement, BlockPos obsidianPos, boolean ignoreTerrain) {
        if (player == null) return 0;
        if (WorldUtils.getGameMode(player) == GameMode.CREATIVE && !(player instanceof FakePlayerEntity)) return 0;

        ((IVec3d) vec3d).set(player.getPos().x, player.getPos().y, player.getPos().z);
        if (predictMovement) ((IVec3d) vec3d).set(vec3d.x + player.getVelocity().x, vec3d.y + player.getVelocity().y, vec3d.z + player.getVelocity().z);

        double modDistance = Math.sqrt(vec3d.squaredDistanceTo(crystal));
        if (modDistance > 12) return 0;

        double exposure = getExposure(crystal, player, predictMovement, raycastContext, obsidianPos, ignoreTerrain);
        double impact = (1 - (modDistance / 12)) * exposure;
        double damage = ((impact * impact + impact) / 2 * 7 * (6 * 2) + 1);

        damage = getDamageForDifficulty(damage);
        damage = DamageUtil.getDamageLeft((float) damage, (float) player.getArmor(), (float) player.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());
        damage = resistanceReduction(player, damage);

        ((IExplosion) explosion).set(crystal, 6, false);
        damage = blastProtReduction(player, damage, explosion);

        return damage < 0 ? 0 : damage;
    }
	
	private static double getExposure(Vec3d source, Entity entity, boolean predictMovement, RaycastContext raycastContext, BlockPos obsidianPos, boolean ignoreTerrain) {
        Box box = entity.getBoundingBox();
        if (predictMovement) {
            Vec3d v = entity.getVelocity();
            box.offset(v.x, v.y, v.z);
        }

        double d = 1 / ((box.maxX - box.minX) * 2 + 1);
        double e = 1 / ((box.maxY - box.minY) * 2 + 1);
        double f = 1 / ((box.maxZ - box.minZ) * 2 + 1);
        double g = (1 - Math.floor(1 / d) * d) / 2;
        double h = (1 - Math.floor(1 / f) * f) / 2;

        if (!(d < 0) && !(e < 0) && !(f < 0)) {
            int i = 0;
            int j = 0;

            for (double k = 0; k <= 1; k += d) {
                for (double l = 0; l <= 1; l += e) {
                    for (double m = 0; m <= 1; m += f) {
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        double o = MathHelper.lerp(l, box.minY, box.maxY);
                        double p = MathHelper.lerp(m, box.minZ, box.maxZ);

                        ((IVec3d) vec3d).set(n + g, o, p + h);
                        ((IRaycastContext) raycastContext).set(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity);

                        if (raycast(raycastContext, obsidianPos, ignoreTerrain).getType() == HitResult.Type.MISS) i++;

                        j++;
                    }
                }
            }

            return (double) i / j;
        }

        return 0;
    }
	
	private static BlockHitResult raycast(RaycastContext context, BlockPos obsidianPos, boolean ignoreTerrain) {
        return BlockView.raycast(context.getStart(), context.getEnd(), context, (raycastContext, blockPos) -> {
            BlockState blockState;
            if (blockPos.equals(obsidianPos)) blockState = Blocks.OBSIDIAN.getDefaultState();
            else {
                blockState = mc.world.getBlockState(blockPos);
                if (blockState.getBlock().getBlastResistance() < 600 && ignoreTerrain) blockState = Blocks.AIR.getDefaultState();
            }

            Vec3d vec3d = raycastContext.getStart();
            Vec3d vec3d2 = raycastContext.getEnd();

            VoxelShape voxelShape = raycastContext.getBlockShape(blockState, mc.world, blockPos);
            BlockHitResult blockHitResult = mc.world.raycastBlock(vec3d, vec3d2, blockPos, voxelShape, blockState);
            VoxelShape voxelShape2 = VoxelShapes.empty();
            BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3d, vec3d2, blockPos);

            double d = blockHitResult == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult.getPos());
            double e = blockHitResult2 == null ? Double.MAX_VALUE : raycastContext.getStart().squaredDistanceTo(blockHitResult2.getPos());

            return d <= e ? blockHitResult : blockHitResult2;
        }, (raycastContext) -> {
            Vec3d vec3d = raycastContext.getStart().subtract(raycastContext.getEnd());
            return BlockHitResult.createMissed(raycastContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), new BlockPos(raycastContext.getEnd()));
        });
    }
	
	private static double getDamageForDifficulty(double damage) {
        return switch (mc.world.getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY     -> Math.min(damage / 2 + 1, damage);
            case HARD     -> damage * 3 / 2;
            default       -> damage;
        };
    }
	
	private static double resistanceReduction(LivingEntity player, double damage) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            int lvl = (player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1);
            damage *= (1 - (lvl * 0.2));
        }

        return damage < 0 ? 0 : damage;
    }
	
	private static double blastProtReduction(Entity player, double damage, Explosion explosion) {
        int protLevel = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), DamageSource.explosion(explosion));
        if (protLevel > 20) protLevel = 20;

        damage *= (1 - (protLevel / 25.0));
        return damage < 0 ? 0 : damage;
    }
	
	public static float getExplosionDamage(Vec3d explosionPos, float power, LivingEntity target) {
		if (mc.world.getDifficulty() == Difficulty.PEACEFUL)
			return 0f;

		double maxDist = power * 2;
		if (!mc.world.getOtherEntities(null, new Box(
				MathHelper.floor(explosionPos.x - maxDist - 1.0),
				MathHelper.floor(explosionPos.y - maxDist - 1.0),
				MathHelper.floor(explosionPos.z - maxDist - 1.0),
				MathHelper.floor(explosionPos.x + maxDist + 1.0),
				MathHelper.floor(explosionPos.y + maxDist + 1.0),
				MathHelper.floor(explosionPos.z + maxDist + 1.0))).contains(target)) {
			return 0f;
		}

		if (!target.isImmuneToExplosion() && !target.isInvulnerable()) {
			double distExposure = Math.sqrt(target.squaredDistanceTo(explosionPos)) / maxDist;
			if (distExposure <= 1.0) {
				double xDiff = target.getX() - explosionPos.x;
				double yDiff = target.getEyeY() - explosionPos.y;
				double zDiff = target.getZ() - explosionPos.z;
				double diff = Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
				if (diff != 0.0) {
					double exposure = Explosion.getExposure(explosionPos, target);
					double finalExposure = (1.0 - distExposure) * exposure;

					float toDamage = (float) Math.floor((finalExposure * finalExposure + finalExposure) / 2.0 * 7.0 * maxDist + 1.0);

					if (target instanceof PlayerEntity) {
						if (mc.world.getDifficulty() == Difficulty.EASY) {
							toDamage = Math.min(toDamage / 2f + 1f, toDamage);
						} else if (mc.world.getDifficulty() == Difficulty.HARD) {
							toDamage = toDamage * 3f / 2f;
						}
					}

					// Armor
					toDamage = DamageUtil.getDamageLeft(toDamage, target.getArmor(),
							(float) target.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS).getValue());

					// Enchantments
					if (target.hasStatusEffect(StatusEffects.RESISTANCE)) {
						int resistance = 25 - (target.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
						float resistance_1 = toDamage * resistance;
						toDamage = Math.max(resistance_1 / 25f, 0f);
					}

					if (toDamage <= 0f) {
						toDamage = 0f;
					} else {
						int protAmount = EnchantmentHelper.getProtectionAmount(target.getArmorItems(), DamageSource.explosion((LivingEntity) null));
						if (protAmount > 0) {
							toDamage = DamageUtil.getInflictedDamage(toDamage, protAmount);
						}
					}

					return toDamage;
				}
			}
		}

		return 0f;
	}
	
	public static float getItemAttackDamage(ItemStack stack) {
		float damage = 1f
				+ stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
				.stream()
				.map(e -> (float) e.getValue())
				.findFirst().orElse(0f);

		return damage + EnchantmentHelper.getAttackDamage(stack, EntityGroup.DEFAULT);
	}
	
	public static boolean willKill(LivingEntity target, float damage) {
		if (target.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING || target.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
			return false;
		}

		return damage >= target.getHealth() + target.getAbsorptionAmount();
	}
	
	public static boolean willPop(LivingEntity target, float damage) {
		if (target.getMainHandStack().getItem() != Items.TOTEM_OF_UNDYING && target.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
			return false;
		}

		return damage >= target.getHealth() + target.getAbsorptionAmount();
	}

	public static boolean willPopOrKill(LivingEntity target, float damage) {
		return damage >= target.getHealth() + target.getAbsorptionAmount();
	}
	
	public static boolean willGoBelowHealth(LivingEntity target, float damage, float minHealth) {
		return target.getHealth() + target.getAbsorptionAmount() - damage < minHealth;
	}
}