package vestige.impl.module.render;

import java.awt.Color;

import net.minecraft.client.gui.Gui;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.RenderEvent;
import vestige.api.module.Category;
import vestige.api.module.DraggableRenderModule;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.ModeSetting;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.util.network.ServerUtils;
import vestige.util.render.BlurUtil;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;

@ModuleInfo(name = "SessionInfo", category = Category.RENDER)
public class SessionInfo extends DraggableRenderModule {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "New");
	
	public SessionInfo() {
		this.registerSettings(mode);
	}
	
	@Override
	public void initialisePositions() {
		x = 10;
		y = 50;
		width = 150;
		//width = 140;
		height = 40;
	}
	
	@Listener
	public void onRender(RenderEvent e) {
		if (mc.gameSettings.showDebugInfo) return;

		if(mc.currentScreen == null) {
			renderSessionInfo(false);
		}
	}
	
	public void onChatRender(int mouseX, int mouseY, float partialTicks) {
		renderSessionInfo(true);
	}
	
	private void renderSessionInfo(boolean inChat) {
		MinecraftFontRenderer fr = FontUtil.product_sans;
		HUD hud = (HUD) Vestige.getInstance().getModuleManager().getModule(HUD.class);
		
		String username = Vestige.getInstance().getModuleManager().getModule(NameProtect.class).isEnabled() && inChat ? "You" : mc.getSession().getUsername();
		
		switch (mode.getMode()) {
			case "Normal":
				DrawUtil.drawRoundedRect(x, y, x + width, y + height, 4, 0x85000000);
				break;
			case "New":
				BlurUtil.blur(x, y, x + width, y + height);
				
				DrawUtil.drawRoundedRect(x, y, x + 2, y + height - 1, 2, 0x25000000);
				DrawUtil.drawRoundedRect(x + width - 2, y, x + width, y + height - 1, 2, 0x25000000);
				DrawUtil.drawRoundedRect(x + 1, y + height - 2, x + width - 1, y + height, 2, 0x25000000);
				break;
		}
		
		for(float i = x; i < x + width; i++) {
			int color = -1;
			if(hud.color.is("Normal")) {
				color = ColorUtil.getVestigeColors(3F, (long) (25 - (i * 10))).getRGB();
			} else if(hud.color.is("Custom")) {
				Color c1 = new Color((int) hud.red1.getCurrentValue(), (int) hud.green1.getCurrentValue(), (int) hud.blue1.getCurrentValue());
				Color c2 = new Color((int) hud.red2.getCurrentValue(), (int) hud.green2.getCurrentValue(), (int) hud.blue2.getCurrentValue());
				color = ColorUtil.customColors(c1, c2, false, 3F, (long) (25 - (i * 10))).getRGB();
			} else if(hud.color.is("Rainbow")) {
				color = ColorUtil.getRainbow(3F, 0.8F, 1F, (long) (25 + (-i * 8))).getRGB();
			}
			
			Gui.drawRect(i, y, i + 1, y + 2, color);
		}
		
		fr.drawStringWithShadow("Server : " + ServerUtils.getCurrentServer(), x + 4, y + 5, -1);
		fr.drawStringWithShadow("Username : " + username, x + 4, y + 16, -1);
		//fr.drawStringWithShadow("Ping : " + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() + " ms", x + 4, y + 27, -1);
		
		String timeSpent = Vestige.getInstance().getSessionInfoProcessor().getHoursElapsed() + "h" + " "
						 + Vestige.getInstance().getSessionInfoProcessor().getMinutesElapsed() + "min" + " "
						 + Vestige.getInstance().getSessionInfoProcessor().getSecondsElapsed() + "s";
		
		//fr.drawStringWithShadow("Time Spent : " + timeSpent, x + 4, y + 16, -1);
		
		fr.drawStringWithShadow("Time Spent : " + timeSpent, x + 4, y + 27, -1);
	}
	
}