package sudo.module.movement;

import net.minecraft.util.Formatting;
import sudo.module.Mod;
import sudo.module.settings.NumberSetting;

public class Step extends Mod {

	NumberSetting height = new NumberSetting("Height", 1, 6, 2, 1.0);

    public Step() {
        super("Step", "Gives the player the ability to climb set ammount of block", Category.MOVEMENT, 0);
        addSetting(height);
    }
    private static final Formatting Gray = Formatting.GRAY;
    @Override
    public void onTick() {
        this.setDisplayName("Step" + Gray + " [H:"+height.getValue()+"] ");
        mc.player.stepHeight = height.getValueInt();
        super.onTick();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.6f;
        super.onDisable();
    }
	
}
