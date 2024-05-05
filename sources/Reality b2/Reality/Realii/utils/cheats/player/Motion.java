package Reality.Realii.utils.cheats.player;
import Reality.Realii.mods.modules.world.Scaffold;
import Reality.Realii.utils.cheats.player.Helper;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.potion.Potion;

public class Motion {
	  public static final double WALK_SPEED = 0.221;
	    public static final double BUNNY_SLOPE = 0.66;
	    public static final double MOD_SPRINTING = 1.3F;
	    public static final double MOD_SNEAK = 0.3F;
	    public static final double MOD_ICE = 2.5F;
	    public static final double MOD_WEB = 0.105 / WALK_SPEED;
	    public static final double JUMP_HEIGHT = 0.42F;
	    public static final double BUNNY_FRICTION = 159.9F;
	    public static final double Y_ON_GROUND_MIN = 0.00001;
	    public static final double Y_ON_GROUND_MAX = 0.0626;

	    public static final double AIR_FRICTION = 0.9800000190734863D;
	    public static final double WATER_FRICTION = 0.800000011920929D;
	    public static final double LAVA_FRICTION = 0.5D;
	    public static final double MOD_SWIM = 0.115F / WALK_SPEED;
	    public static final double[] MOD_DEPTH_STRIDER = {
	            1.0F,
	            0.1645F / MOD_SWIM / WALK_SPEED,
	            0.1995F / MOD_SWIM / WALK_SPEED,
	            1.0F / MOD_SWIM,
	    };
	    
	    public static final double UNLOADED_CHUNK_MOTION = -0.09800000190735147;
	    public static final double HEAD_HITTER_MOTION = -0.0784000015258789;
	    public static double getAllowedHorizontalDistance() {
	    	  double horizontalDistance;
	          boolean useBaseModifiers = false;

	          if (Helper.mc.thePlayer.isInWeb) {
	              horizontalDistance = MOD_WEB * WALK_SPEED;
	          } else if (BlockHelper.isInLiquid()) {
	              horizontalDistance = MOD_SWIM * WALK_SPEED;

	            

	          } else if (Helper.mc.thePlayer.isSneaking()) {
	              horizontalDistance = MOD_SNEAK * WALK_SPEED;
	          } else {
	              horizontalDistance = WALK_SPEED;
	              useBaseModifiers = true;
	          }

	          if (useBaseModifiers) {
	              if (canSprint(false)) {
	                  horizontalDistance *= MOD_SPRINTING;
	              }

	          

	              if (Helper.mc.thePlayer.isPotionActive(Potion.moveSpeed) && Helper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).duration > 0) {
	                  horizontalDistance *= 1 + (0.2 * (Helper.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1));
	              }

	              if (Helper.mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
	                  horizontalDistance = 0.29;
	              }
	          }

	          return horizontalDistance;
	    }
	    
	    
	    public static boolean canSprint(final boolean legit) {
	        return (legit ? Helper.mc.thePlayer.moveForward >= 0.8F
	                && !Helper.mc.thePlayer.isCollidedHorizontally
	                && (Helper.mc.thePlayer.getFoodStats().getFoodLevel() > 6 || Helper.mc.thePlayer.capabilities.allowFlying)
	                && !Helper.mc.thePlayer.isPotionActive(Potion.blindness)
	                && !Helper.mc.thePlayer.isUsingItem()
	                && !Helper.mc.thePlayer.isSneaking()
	                : enoughMovementForSprinting());
	    }
	    
	    public static boolean enoughMovementForSprinting() {
	        return Math.abs(Helper.mc.thePlayer.moveForward) >= 0.8F || Math.abs(Helper.mc.thePlayer.moveStrafing) >= 0.8F;
	    }

	    
	  

}
