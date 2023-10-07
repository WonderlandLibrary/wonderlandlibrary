package sudo.module.movement;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;
import sudo.module.settings.ModeSetting;
import sudo.utils.render.QuadColor;
import sudo.utils.render.RenderUtils;

public class ClickTP extends Mod {
	
	public static MinecraftClient mc = MinecraftClient.getInstance();

    @SuppressWarnings("unused")
	private static BlockPos targeted = new BlockPos(0, 0, 0);
    public static ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Split");
    
    public ClickTP() {
        super("ClickTP", "Tries to TP you to the targeted location", Category.MOVEMENT, GLFW.GLFW_KEY_R);
        addSettings(mode);
    }
    
    private static int lengthTo(BlockPos p) {
        Vec3d v = new Vec3d(p.getX(), p.getY(), p.getZ());
        return (int) roundToN(v.distanceTo(mc.player.getPos()), 0);
    }

    public static double roundToN(double x, int n) {
        if (n == 0) {
            return Math.floor(x);
        }
        double factor = Math.pow(10, n);
        return Math.round(x * factor) / factor;
    }
	
	public static void clickTPaction() {
		BlockHitResult ray = (BlockHitResult) mc.player.raycast(200, mc.getTickDelta(), true);
        targeted = new BlockPos(ray.getBlockPos());
        if (mc.currentScreen != null) {
        	return;
        }
		BlockHitResult ray1 = (BlockHitResult) mc.player.raycast(200, mc.getTickDelta(), true);
        int rd = lengthTo(ray1.getBlockPos());
        int raycastdistance = rd / 7;
        BlockHitResult blockHitResult = (BlockHitResult) mc.player.raycast(200, mc.getTickDelta(), true);
        BlockPos d = new BlockPos(blockHitResult.getBlockPos());
        BlockPos dest = new BlockPos(d.getX() + 0.5, d.getY(), d.getZ() + 0.5);
        dest = dest.offset(Direction.UP, 1);
        
        if (mode.getMode().equalsIgnoreCase("Normal") && mc.player != null) mc.player.updatePosition(dest.getX()+0.5, dest.getY(), dest.getZ()+0.5);
                
        if (mode.getMode().equalsIgnoreCase("Split") && mc.player != null) {
        	mc.player.jump();
        	ClientPlayerEntity player = mc.player;
        	Vec3d playerpos = player.getPos();
        	double xn = dest.getX() - playerpos.x;
        	double yn = dest.getY() - playerpos.y;
        	double zn = dest.getZ() - playerpos.z;
        	double x = xn / raycastdistance;
        	double y = yn / raycastdistance;
        	double z = zn / raycastdistance;
        	for (int i = 0; i < raycastdistance; i++) {
        		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(player.getX() + x,
        																						player.getY() + y,
        																						player.getZ() + z,
        																						true
        				));
        	}
        player.updatePosition(dest.getX()+0.5, dest.getY(), dest.getZ()+0.5);
        }
	}
	
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
        BlockHitResult blockHitResult = (BlockHitResult) mc.player.raycast(200, mc.getTickDelta(), true);
        BlockPos d = new BlockPos(blockHitResult.getBlockPos());
        BlockPos dest = new BlockPos(d.getX() + 0.5, d.getY(), d.getZ() + 0.5);
		RenderUtils.drawBoxFill(new Box(Vec3d.of(dest), Vec3d.of(dest).add(1, 0, 1)).stretch(0, 1, 0), QuadColor.single(0, 100f, 99f, 0.5f));
		super.onWorldRender(matrices);
	}
}
