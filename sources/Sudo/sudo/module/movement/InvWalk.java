package sudo.module.movement;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;

public class InvWalk extends Mod{
	
	public BooleanSetting items = new BooleanSetting("Items", true);
	BooleanSetting inventory = new BooleanSetting("Inventory", true);
	BooleanSetting shift = new BooleanSetting("Shift", true);
	
	public static InvWalk INSTANCE = new InvWalk();

	public InvWalk() {
		super("InvWalk", "Allows the player to move when the inventory is opened", Category.MOVEMENT, 0);
	}
	
	 @Override
	    public void onTick() {
	        if (inventory.isEnabled() 
	        		&& mc.currentScreen != null 
	        		&& !(mc.currentScreen instanceof ChatScreen) 
	        		&& !(mc.currentScreen instanceof SignEditScreen) 
	        		&& !(mc.currentScreen instanceof BookScreen)) {
	            for (KeyBinding k : new KeyBinding[]{mc.options.forwardKey, mc.options.backKey,
	                    mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey}) {
	                k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
	                        InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
	            }
	            if (shift.isEnabled()) mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
	                    InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
	        }
	    }

}
