package vestige.impl.module.render;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import vestige.api.event.Listener;
import vestige.api.event.impl.MotionEvent;
import vestige.api.event.impl.MoveEvent;
import vestige.api.event.impl.RenderEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.ui.click.simple.SimpleClickGUI;
import vestige.ui.click.vestige.VestigeClickGUI;
import vestige.util.misc.TimerUtil;

@ModuleInfo(name = "ClickGUI", category = Category.RENDER, key = Keyboard.KEY_RSHIFT)
public class ClickGuiModule extends Module {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Vestige", "Vestige", "Simple");
	
	public final NumberSetting red1 = new NumberSetting("Red 1", this, 0, 0, 255, 5, true) { public boolean isShown() { return mode.is("Vestige") && !rainbow.isEnabled();}};
	public final NumberSetting green1 = new NumberSetting("Green 1", this, 200, 0, 255, 5, true) { public boolean isShown() { return mode.is("Vestige") && !rainbow.isEnabled();}};
	public final NumberSetting blue1 = new NumberSetting("Blue 1", this, 235, 0, 255, 5, true) { public boolean isShown() { return mode.is("Vestige") && !rainbow.isEnabled();}};

	public final NumberSetting red2 = new NumberSetting("Red 2", this, 20, 0, 255, 5, true) { public boolean isShown() { return mode.is("Vestige") && !rainbow.isEnabled();}};
	public final NumberSetting green2 = new NumberSetting("Green 2", this, 75, 0, 255, 5, true) { public boolean isShown() { return mode.is("Vestige") && !rainbow.isEnabled();}};
	public final NumberSetting blue2 = new NumberSetting("Blue 2", this, 230, 0, 255, 5, true) { public boolean isShown() { return mode.is("Vestige") && !rainbow.isEnabled();}};
	
	public final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false) { public boolean isShown() {return mode.is("Vestige");}};
	
	private VestigeClickGUI vestigeClickGUI;
	private SimpleClickGUI simpleClickGUI;
	
	private boolean initialisedClickGuis;
	
	private final TimerUtil timer = new TimerUtil();
	
	public ClickGuiModule() {
		this.registerSettings(mode, red1, green1, blue1, red2, green2, blue2, rainbow);
	}
	
	public void onEnable() {
		if(!initialisedClickGuis) {
			vestigeClickGUI = new VestigeClickGUI();
			simpleClickGUI = new SimpleClickGUI();
			initialisedClickGuis = true;
		}
		
		switch (mode.getMode()) {
		case "Vestige":
			mc.displayGuiScreen(vestigeClickGUI);
			break;
		case "Simple":
			mc.displayGuiScreen(simpleClickGUI);
			break;
		}
	}
	
	@Listener
	public void onRender(RenderEvent event) {
		if(mc.thePlayer.ticksExisted < 5) {
			this.setEnabled(false);
			return;
		}
		
		this.allowMove();
		
		final double speed = 0.15F;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) mc.thePlayer.rotationYaw += speed * timer.getTimeElapsed();
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) mc.thePlayer.rotationYaw -= speed * timer.getTimeElapsed();
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) mc.thePlayer.rotationPitch -= speed * timer.getTimeElapsed();
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) mc.thePlayer.rotationPitch += speed * timer.getTimeElapsed();
		
		if(mc.thePlayer.rotationPitch > 90) {
			mc.thePlayer.rotationPitch = 90;
		} else if(mc.thePlayer.rotationPitch < -90) {
			mc.thePlayer.rotationPitch = -90;
		}
		
		timer.reset();
	}
	
	@Listener
	public void onUpdate(UpdateEvent event) {
		if(mc.thePlayer.ticksExisted < 5) {
			this.setEnabled(false);
			return;
		}
		
		this.allowMove();
	}
 	
	@Listener
	public void onMove(MoveEvent event) {
		this.allowMove();
	}
	
	@Listener
	public void onMotion(MotionEvent event) {
		this.allowMove();
	}
	
	public void allowMove() {
		GameSettings gs = mc.gameSettings;
		KeyBinding keys[] = {gs.keyBindForward, gs.keyBindBack, gs.keyBindLeft, gs.keyBindRight, gs.keyBindJump};
		
		for(KeyBinding key : keys) {
			key.pressed = Keyboard.isKeyDown(key.getKeyCode());
		}
	}

}