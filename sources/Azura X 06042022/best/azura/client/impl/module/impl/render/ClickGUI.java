package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.value.ColorValue;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.util.color.HSBColor;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "ClickGUI", description = "Screen to configure modules in style.", keyBind = Keyboard.KEY_RSHIFT, category = Category.RENDER)
public class ClickGUI extends Module {

	public static final ModeValue theme = new ModeValue("Theme", "Change the look of the ClickGUI.", "Azura X", "Azura X", "NEW");
	public static final ColorValue color = new ColorValue("Color", "Change the color of the ClickGui.", new HSBColor(1f, 1f, 1f));

	@Override
	public void onEnable() {
		super.onEnable();
		switch (theme.getObject().toLowerCase()) {
			case "azura x":
				Minecraft.getMinecraft().displayGuiScreen(Client.INSTANCE.getClickGUI());
				break;
			case "new":
				Minecraft.getMinecraft().displayGuiScreen(Client.INSTANCE.getNewGUI());
		}

	}

	@EventHandler
	public final Listener<EventRender2D> render2DListener = event -> {
		if (Client.INSTANCE.getClickGUI().close) this.setEnabled(false);
		if (mc.currentScreen != null && !(mc.currentScreen instanceof best.azura.client.impl.clickgui.azura.ClickGUI)) setEnabled(false);
	};

}
