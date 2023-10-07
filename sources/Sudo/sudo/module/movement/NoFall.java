package sudo.module.movement;


import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Formatting;
import sudo.module.Mod;
import sudo.module.settings.ModeSetting;

public class NoFall extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "OnGround", "OnGround", "BreakFall");
	
    public NoFall() {
        super("NoFall", "Prevent you from taking fall damage", Category.MOVEMENT, 0);
        addSetting(mode);
    }
    
    private static final Formatting Gray = Formatting.GRAY;
    
	@Override
	public void onTick() {
		this.setDisplayName("NoFall" + Gray + " ["+mode.getMode()+"] ");
        if (mc.player == null || mc.getNetworkHandler() == null) {
            return;
        }
        if (mode.getMode().equalsIgnoreCase("BreakFall")) {
        	if (mc.player.fallDistance > 2.5) {
        		mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        		mc.player.setVelocity(0, 0.1, 0);
        		mc.player.fallDistance = 0;
        	}
        } else if (mode.getMode().equalsIgnoreCase("OnGround")) {
        	if (mc.player.fallDistance > 2.5) mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        }
        super.onTick();
    }
}