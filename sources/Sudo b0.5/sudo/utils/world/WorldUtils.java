package sudo.utils.world;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//import dev.hypnotic.config.friends.FriendManager;
//import dev.hypnotic.module.ModuleManager;
//import dev.hypnotic.module.player.Scaffold;
//import dev.hypnotic.utils.RotationUtils;
//import dev.hypnotic.utils.mixin.IVec3d;
//import dev.hypnotic.utils.player.inventory.FindItemResult;
//import dev.hypnotic.utils.player.inventory.InventoryUtils;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import sudo.module.ModuleManager;
import sudo.module.world.Scaffold;
import sudo.utils.player.RotationUtils;

public class WorldUtils {
	public static MinecraftClient mc = MinecraftClient.getInstance();
	@SuppressWarnings("unused")
	private static final Vec3d hitPos = new Vec3d(0, 0, 0);
    
	
	
    public static boolean placeBlockMainHand(BlockPos pos, Boolean rotate, boolean swing) {
        return placeBlockMainHand(pos, rotate, true, swing);
    }
    public static boolean placeBlockMainHand(BlockPos pos, Boolean rotate, Boolean airPlace, boolean swing) {
        return placeBlockMainHand(pos, rotate, airPlace, false, swing);
    }
    public static boolean placeBlockMainHand(BlockPos pos, Boolean rotate, Boolean airPlace, Boolean ignoreEntity, boolean swing) {
        return placeBlockMainHand(pos, rotate, airPlace, ignoreEntity, null, swing);
    }
    public static boolean placeBlockMainHand(BlockPos pos, Boolean rotate, Boolean airPlace, Boolean ignoreEntity, Direction overrideSide, boolean swing) {
        return placeBlock(Hand.MAIN_HAND, pos, rotate, airPlace, ignoreEntity, overrideSide, swing);
    }
    public static boolean placeBlockNoRotate(Hand hand, BlockPos pos, boolean swing) {
        return placeBlock(hand, pos, false, true, false, swing);
    }

    public static boolean placeBlock(Hand hand, BlockPos pos, boolean swing) {
        placeBlock(hand, pos, true, false);
        return true;
    }
    public static boolean placeBlock(Hand hand, BlockPos pos, Boolean rotate, boolean swing) {
        placeBlock(hand, pos, rotate, false, swing);
        return true;
    }
    public static boolean placeBlock(Hand hand, BlockPos pos, Boolean rotate, Boolean airPlace, boolean swing) {
        placeBlock(hand, pos, rotate, airPlace, false, swing);
        return true;
    }
    public static boolean placeBlock(Hand hand, BlockPos pos, Boolean rotate, Boolean airPlace, Boolean ignoreEntity, boolean swing) {
        placeBlock(hand, pos, rotate, airPlace, ignoreEntity, null, swing);
        return true;
    }

    public static boolean placeBlock(Hand hand, BlockPos pos, Boolean rotate, Boolean airPlace, Boolean ignoreEntity, Direction overrideSide, boolean swing) {
        // make sure place is empty if ignoreEntity is not true
        if(ignoreEntity) {
            if (!mc.world.getBlockState(pos).getMaterial().isReplaceable())
                return false;
        } else if(!mc.world.getBlockState(pos).getMaterial().isReplaceable() || !mc.world.canPlace(Blocks.OBSIDIAN.getDefaultState(), pos, ShapeContext.absent()))
            return false;

        Vec3d eyesPos = new Vec3d(mc.player.getX(),
                mc.player.getY() + mc.player.getEyeHeight(mc.player.getPose()),
                mc.player.getZ());

        Vec3d hitVec = null;
        BlockPos neighbor = null;
        Direction side2 = null;

        if(overrideSide != null) {
            neighbor = pos.offset(overrideSide.getOpposite());
            side2 = overrideSide;
        }

        for(Direction side: Direction.values()) {
            if(overrideSide == null) {
                neighbor = pos.offset(side);
                side2 = side.getOpposite();

                // check if neighbor can be right clicked aka it isnt air
                if(mc.world.getBlockState(neighbor).isAir() || mc.world.getBlockState(neighbor).getBlock() instanceof FluidBlock) {
                    neighbor = null;
                    side2 = null;
                    continue;
                }
            }

            hitVec = new Vec3d(neighbor.getX(), neighbor.getY(), neighbor.getZ()).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getUnitVector()).multiply(0.5));
            break;
        }

        // Air place if no neighbour was found
        if(airPlace) {
            if (hitVec == null) hitVec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
            if (neighbor == null) neighbor = pos;
            if (side2 == null) side2 = Direction.UP;
        } else if(hitVec == null || neighbor == null || side2 == null) {
            return false;
        }

        // place block
        double diffX = hitVec.x - eyesPos.x;
        double diffY = hitVec.y - eyesPos.y;
        double diffZ = hitVec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        
        float[] rotations = {
                mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - mc.player.getYaw()),
                mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - mc.player.getPitch())};

        if(rotate) {
        	if (ModuleManager.INSTANCE.getModule(Scaffold.class).extend.getValue() > 1) {
	        	RotationUtils.setSilentYaw(rotations[0]);
	        	RotationUtils.setSilentPitch(rotations[1]);
        	}
        	mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], mc.player.isOnGround()));
        } else {
        	RotationUtils.resetYaw();
        	RotationUtils.resetPitch();
        }

//        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        mc.interactionManager.interactBlock(mc.player, hand, new BlockHitResult(hitVec, side2, neighbor, false));
        if (swing) mc.player.swingHand(hand);
        else mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
//        mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));

        return true;
    }
	
    public static BlockPos getForwardBlock(double length) {
		MinecraftClient mc = MinecraftClient.getInstance();
        final double yaw = Math.toRadians(mc.player.getYaw());
        BlockPos fPos = new BlockPos(mc.player.getX() + (-Math.sin(yaw) * length), mc.player.getY(), mc.player.getZ() + (Math.cos(yaw) * length));
        return fPos;
	}
    public static BlockPos getSideBlock(double length, boolean direction) {
		MinecraftClient mc = MinecraftClient.getInstance();
        final double yaw = Math.toRadians(mc.player.getYaw() + (direction ? 45 : 90));
        BlockPos fPos = new BlockPos(mc.player.getX() + (-Math.sin(yaw) * length), mc.player.getY(), mc.player.getZ() + (Math.cos(yaw) * length));
        return fPos;
	}



    public static final List<Block> NONSOLID_BLOCKS = Arrays.asList(
            Blocks.AIR, Blocks.LAVA, Blocks.WATER, Blocks.GRASS,
            Blocks.VINE, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS,
            Blocks.SNOW, Blocks.TALL_GRASS, Blocks.FIRE, Blocks.VOID_AIR);

    public static boolean canReplace(BlockPos pos) {
        return NONSOLID_BLOCKS.contains(mc.world.getBlockState(pos).getBlock()) && mc.world.getOtherEntities(null, new Box(pos)).stream().noneMatch(Entity::collides);
    }

    public static void moveEntityWithSpeed(Entity entity, double speed, boolean shouldMoveY) {
        float yaw = (float) Math.toRadians(mc.player.getYaw());

        double motionX = 0;
        double motionY = 0;
        double motionZ = 0;

        if(mc.player.input.pressingForward) {
            motionX = -(MathHelper.sin(yaw) * speed);
            motionZ = MathHelper.cos(yaw) * speed;
        } else if(mc.player.input.pressingBack) {
            motionX = MathHelper.sin(yaw) * speed;
            motionZ = -(MathHelper.cos(yaw) * speed);
        }

        if(mc.player.input.pressingLeft) {
            motionZ = MathHelper.sin(yaw) * speed;
            motionX = MathHelper.cos(yaw) * speed;
        } else if(mc.player.input.pressingRight) {
            motionZ = -(MathHelper.sin(yaw) * speed);
            motionX = -(MathHelper.cos(yaw) * speed);
        }

        if(shouldMoveY) {
            if(mc.player.input.jumping) {
                motionY = speed;
            } else if(mc.player.input.sneaking) {
                motionY = -speed;
            }
        }

        //strafe
        if(mc.player.input.pressingForward && mc.player.input.pressingLeft) {
            motionX = (MathHelper.cos(yaw) * speed) - (MathHelper.sin(yaw) * speed);
            motionZ = (MathHelper.cos(yaw) * speed) + (MathHelper.sin(yaw) * speed);
        } else if(mc.player.input.pressingLeft && mc.player.input.pressingBack) {
            motionX = (MathHelper.cos(yaw) * speed) + (MathHelper.sin(yaw) * speed);
            motionZ = -(MathHelper.cos(yaw) * speed) + (MathHelper.sin(yaw) * speed);
        } else if(mc.player.input.pressingBack && mc.player.input.pressingRight) {
            motionX = -(MathHelper.cos(yaw) * speed) + (MathHelper.sin(yaw) * speed);
            motionZ = -(MathHelper.cos(yaw) * speed) - (MathHelper.sin(yaw) * speed);
        } else if(mc.player.input.pressingRight && mc.player.input.pressingForward) {
            motionX = -(MathHelper.cos(yaw) * speed) - (MathHelper.sin(yaw) * speed);
            motionZ = (MathHelper.cos(yaw) * speed) - (MathHelper.sin(yaw) * speed);
        }

        entity.setVelocity(motionX, motionY, motionZ);
    }

    public static List<BlockPos> getAllInBox(final double d, final int y1, final double e, final double f, final int y2, final double g) {
        List<BlockPos> list = new ArrayList<>();
        // wanted to see how inline I could make this XD, good luck any future readers
        for(double x = Math.min(d, f); x <= Math.max(d, f); x++) for(int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) for(double z = Math.min(e, g); z <= Math.max(e, g); z++) list.add(new BlockPos(x, y, z));
        return list;
    }

    public static List<BlockPos> getBlocksInReachDistance() {
        List<BlockPos> cube = new ArrayList<>();
        for(int x = -4; x <= 4; x++)
            for(int y = -4; y <= 4; y++)
                for(int z = -4; z <= 4; z++)
                    cube.add(mc.player.getBlockPos().add(x, y, z));

        return cube.stream().filter(pos -> mc.player.squaredDistanceTo(new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ())) <= 18.0625).collect(Collectors.toList());
    }

    public static Iterable<BlockEntity> blockEntities() {
        return BlockEntityIterator::new;
    }
    
    //Credit to KAMI for code below
    public static double[] calculateLookAt(double px, double py, double pz, PlayerEntity me) {
        double dirx = me.getX() - px;
        double diry = me.getY() + me.getEyeHeight(me.getPose()) - py;
        double dirz = me.getZ() - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0d / Math.PI;
        yaw = yaw * 180.0d / Math.PI;

        yaw += 90f;

        return new double[]{yaw, pitch};
    }
    //End credit to Kami

    public static void rotate(float yaw, float pitch) {
        mc.player.setYaw(yaw);
        mc.player.setPitch(pitch);
    }

    public static void rotate(double[] rotations) {
        mc.player.setYaw((float) rotations[0]);
        mc.player.setPitch((float) rotations[1]);
    }

    public static void lookAtBlock(BlockPos blockToLookAt) {
        rotate(calculateLookAt(blockToLookAt.getX(), blockToLookAt.getY(), blockToLookAt.getZ(), mc.player));
    }

    public static String vectorToString(Vec3d vector, boolean... includeY) {
        boolean reallyIncludeY = includeY.length <= 0 || includeY[0];
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append((int) Math.floor(vector.x));
        builder.append(", ");
        if(reallyIncludeY) {
            builder.append((int) Math.floor(vector.y));
            builder.append(", ");
        }
        builder.append((int) Math.floor(vector.z));
        builder.append(")");
        return builder.toString();
    }

    public static List<Entity> getTargets(boolean players, boolean friends, boolean teammates, boolean passive, boolean hostile, boolean nametagged, boolean bots) {
        return StreamSupport.stream(mc.world.getEntities().spliterator(), false).filter(entity -> isTarget(entity, players, friends, teammates, passive, hostile, nametagged, bots)).collect(Collectors.toList());
    }

    public static boolean isTarget(Entity entity, boolean players, boolean friends, boolean teammates, boolean passive, boolean hostile, boolean nametagged, boolean bots) {
        if(!(entity instanceof LivingEntity) || entity == mc.player) return false;

        if(players && entity instanceof PlayerEntity) return true;
        if(friends && entity instanceof PlayerEntity) return true;
        if(teammates && entity.getScoreboardTeam() == mc.player.getScoreboardTeam() && mc.player.getScoreboardTeam() != null) return true;
        if(passive && isPassive(entity)) return true;
        if(hostile && isHostile(entity)) return true;
        if(nametagged && entity.hasCustomName()) return true;
        if(bots && isBot(entity)) return true;

        return false;
    }

    public static boolean isValidTarget(Entity entity) {
        return isValidTarget(entity, -1, false);
    }
    public static boolean isValidTarget(Entity entity, double distance) {
        return isValidTarget(entity, distance, true);
    }
    public static boolean isValidTarget(Entity entity, double distance, boolean doDistance) {
        return (entity instanceof PlayerEntity || entity instanceof OtherClientPlayerEntity)
                && !entity.isRemoved()
                && !hasZeroHealth(entity)
                && !shouldDistance(entity, distance, doDistance)
                && entity != mc.player;
    }

    private static boolean shouldDistance(Entity entity, double distance, boolean doDistance) {
        if(doDistance) return mc.player.distanceTo(entity) > distance;
        else return false;
    }

    public static boolean hasZeroHealth(PlayerEntity playerEntity) {
        return hasZeroHealth((Entity) playerEntity);
    }
    public static boolean hasZeroHealth(Entity entity) {
        if(entity instanceof PlayerEntity) {
            return (((PlayerEntity) entity).getHealth() <= 0);
        } else return false;
    }

    public static boolean isPassive(Entity entity) {
        if(entity instanceof IronGolemEntity && ((IronGolemEntity) entity).getAngryAt() == null) return true;
        else if(entity instanceof WolfEntity && (!((WolfEntity) entity).isAttacking() || ((WolfEntity) entity).getOwner() == mc.player)) return true;
        else return entity instanceof AmbientEntity || entity instanceof PassiveEntity || entity instanceof SquidEntity;
    }

    public static boolean isHostile(Entity entity) {
        if(entity instanceof IronGolemEntity) return ((IronGolemEntity) entity).getAngryAt() == mc.player.getUuid() && ((IronGolemEntity) entity).getAngryAt() != null;
        else if(entity instanceof WolfEntity) return ((WolfEntity) entity).isAttacking() && ((WolfEntity) entity).getOwner() != mc.player;
        else if(entity instanceof PiglinEntity) return ((PiglinEntity) entity).isAngryAt(mc.player);
        else if(entity instanceof EndermanEntity) return ((EndermanEntity) entity).isAngry();
        return entity.getType().getSpawnGroup() == SpawnGroup.MONSTER;
    }

    public static boolean isBot(Entity entity) {
        return entity instanceof PlayerEntity && entity.isInvisibleTo(mc.player) && !entity.isOnGround() && !entity.collides();
    }

    public static void fakeJump() {
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.40, mc.player.getZ(), true));
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.75, mc.player.getZ(), true));
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.01, mc.player.getZ(), true));
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.15, mc.player.getZ(), true));
    }

    public static BlockPos roundBlockPos(Vec3d vec) {
        return new BlockPos(vec.x, (int) Math.round(vec.y), vec.z);
    }

    public static void snapPlayer() {
        BlockPos lastPos = mc.player.isOnGround() ? WorldUtils.roundBlockPos(mc.player.getPos()) : mc.player.getBlockPos();
        snapPlayer(lastPos);
    }
    public static void snapPlayer(BlockPos lastPos) {
        double xPos = mc.player.getPos().x;
        double zPos = mc.player.getPos().z;

        if(Math.abs((lastPos.getX() + 0.5) - mc.player.getPos().x) >= 0.2) {
            int xDir = (lastPos.getX() + 0.5) - mc.player.getPos().x > 0 ? 1 : -1;
            xPos += 0.3 * xDir;
        }

        if(Math.abs((lastPos.getZ() + 0.5) - mc.player.getPos().z) >= 0.2) {
            int zDir = (lastPos.getZ() + 0.5) - mc.player.getPos().z > 0 ? 1 : -1;
            zPos += 0.3 * zDir;
        }

        mc.player.setVelocity(0, 0, 0);
        mc.player.updatePosition(xPos, mc.player.getY(), zPos);
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround()));
    }
  
    public static boolean canPlace(BlockPos blockPos, boolean checkEntities) {
        if (blockPos == null) return false;

        // Check y level
        if (!World.isValid(blockPos)) return false;

        // Check if current block is replaceable
        if (!mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) return false;

        // Check if intersects entities
        return !checkEntities || mc.world.canPlace(Blocks.STONE.getDefaultState(), blockPos, ShapeContext.absent());
    }

    public static boolean canPlace(BlockPos blockPos) {
        return canPlace(blockPos, true);
    }
    
    public static boolean isClickable(Block block) {
        return block instanceof CraftingTableBlock
            || block instanceof AnvilBlock
            || block instanceof AbstractButtonBlock
            || block instanceof AbstractPressurePlateBlock
            || block instanceof BlockWithEntity
            || block instanceof BedBlock
            || block instanceof FenceGateBlock
            || block instanceof DoorBlock
            || block instanceof NoteBlock
            || block instanceof TrapdoorBlock;
    }
    
    public static GameMode getGameMode(PlayerEntity player) {
    	PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
    	if (playerListEntry != null) return playerListEntry.getGameMode();
    	return GameMode.DEFAULT;
    }
    
    public static Block getBlock(BlockPos pos) {
        if (mc.world == null)
            return null;
        return mc.world.getBlockState(pos).getBlock();
    }
   
    public static List<WorldChunk> getLoadedChunks() {
		List<WorldChunk> chunks = new ArrayList<>();

		int viewDist = mc.options.getViewDistance().getValue();

		for (int x = -viewDist; x <= viewDist; x++) {
			for (int z = -viewDist; z <= viewDist; z++) {
				WorldChunk chunk = mc.world.getChunkManager().getWorldChunk((int) mc.player.getX() / 16 + x, (int) mc.player.getZ() / 16 + z);

				if (chunk != null) {
					chunks.add(chunk);
				}
			}
		}

		return chunks;
	}
    
    public static boolean breakBlock(BlockPos blockPos, boolean swing) {
        if (!canBreak(blockPos, mc.world.getBlockState(blockPos))) return false;

        BlockPos pos = blockPos instanceof BlockPos.Mutable ? new BlockPos(blockPos) : blockPos;

        if (mc.interactionManager.isBreakingBlock()) mc.interactionManager.updateBlockBreakingProgress(pos, Direction.UP);
        else mc.interactionManager.attackBlock(pos, Direction.UP);

        if (swing) mc.player.swingHand(Hand.MAIN_HAND);
        else mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));

        return true;
    }
    
    public static boolean canBreak(BlockPos blockPos, BlockState state) {
        if (!mc.player.isCreative() && state.getHardness(mc.world, blockPos) < 0) return false;
        return state.getOutlineShape(mc.world, blockPos) != VoxelShapes.empty();
    }
    public static boolean canBreak(BlockPos blockPos) {
        return canBreak(blockPos, mc.world.getBlockState(blockPos));
    }

    public static boolean canInstaBreak(BlockPos blockPos, BlockState state) {
        return mc.player.isCreative() || state.calcBlockBreakingDelta(mc.player, mc.world, blockPos) >= 1;
    }
    public static boolean canInstaBreak(BlockPos blockPos) {
        return canInstaBreak(blockPos, mc.world.getBlockState(blockPos));
    }
}
