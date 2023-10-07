package sudo.module.movement;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import sudo.module.Mod;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

public class Flight extends Mod {

	public static MinecraftClient mc = MinecraftClient.getInstance();
	
    public static GameMode getGameMode(PlayerEntity player) {
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid()); 
        if (playerListEntry != null) return playerListEntry.getGameMode(); 
        return GameMode.DEFAULT;
    }
	
	public NumberSetting speed = new NumberSetting("Speed", 0, 10, 2, 1);
	public ModeSetting mode = new ModeSetting("Mode", "Static", "Static", "Vector", "Vanilla");
	
	int bypassTimer = 0;
	
	public Flight() {
		super("Flight", "Allows you to fly", Category.MOVEMENT, GLFW.GLFW_KEY_G);
		addSettings(speed, mode);
	}
//	TODO remove -0.03125D from the Y velocity every 75 tick
	
	private static final Formatting Gray = Formatting.GRAY;
	@Override
	public void onTick() {
		this.setDisplayName("Flight" + Gray + " ["+mode.getMode()+"] ");
		if (mc.player == null || mc.getNetworkHandler() == null) {
            return;
        }
		
        if (mode.getMode().equalsIgnoreCase("Vanilla")) {
        	mc.player.getAbilities().allowFlying = true;
        	mc.player.getAbilities().setFlySpeed(((float) speed.getValueFloat()) / 100);
        } else if (mode.getMode().equalsIgnoreCase("Static")) {
        	GameOptions go = mc.options;
        	float y = mc.player.getYaw();
        	int mx = 0, my = 0, mz = 0;
 
        	if (go.jumpKey.isPressed()) 
        		my++;
        	if (go.backKey.isPressed()) 
        		mz++;
        	if (go.leftKey.isPressed()) 
        		mx--;
        	if (go.rightKey.isPressed()) 
        		mx++;
        	if (go.sneakKey.isPressed())
        		my--;
        	if (go.forwardKey.isPressed())
        		mz--;
        	double ts = speed.getValueFloat() / 2;
            double s = Math.sin(Math.toRadians(y));
            double c = Math.cos(Math.toRadians(y));
            double nx = ts * mz * s;
            double nz = ts * mz * -c;
            double ny = ts * my;
            nx += ts * mx * -c;
            nz += ts * mx * -s;
            Vec3d nv3 = new Vec3d(nx, ny, nz);
            mc.player.setVelocity(nv3);
        } else if (mode.is("Vector")) {
			float yaw = (float) Math.toRadians(mc.player.getYaw());
			if (mc.options.forwardKey.isPressed()) {
				mc.player.setVelocity(
						(-MathHelper.sin(yaw) * speed.getValue() / 30)*speed.getValue(), 
						(((mc.player.getRotationVector().multiply(speed.getValue()).y /2) * speed.getValue()) / 60)*(speed.getValue()/2),
						(MathHelper.cos(yaw) * speed.getValue() / 30)*speed.getValue());
			}
        }
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		if ((getGameMode(mc.player) == GameMode.CREATIVE)) {
			mc.player.getAbilities().allowFlying = true;
        	mc.player.getAbilities().setFlySpeed(0.05f);
			mc.player.getAbilities().flying = false;
		} else {
			mc.player.getAbilities().allowFlying = false;
			mc.player.getAbilities().flying = false;
		}
		super.onDisable();
	}
	@Override
	public void onEnable() {
		if ((getGameMode(mc.player) == GameMode.CREATIVE)) {
			mc.player.getAbilities().allowFlying = true;
			mc.player.getAbilities().flying = true;
		} else {
			mc.player.getAbilities().allowFlying = true;
			mc.player.getAbilities().flying = true;
		}
		super.onEnable();
	}
}
