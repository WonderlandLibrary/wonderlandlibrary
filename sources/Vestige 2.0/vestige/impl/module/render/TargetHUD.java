package vestige.impl.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.RenderEvent;
import vestige.api.module.Category;
import vestige.api.module.DraggableRenderModule;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.ModeSetting;
import vestige.impl.module.combat.Killaura;
import vestige.util.misc.TimerUtil;
import vestige.util.render.BlurUtil;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;
import vestige.util.render.RenderUtils;

@ModuleInfo(name = "TargetHUD", category = Category.RENDER)
public class TargetHUD extends DraggableRenderModule {
	
	private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "New");
	
	public TargetHUD() {
		this.registerSettings(mode);
	}
	
	@Override
	public void initialisePositions() {
		x = 400;
		y = 300;
		width = 130;
		height = 50;
	}
	
	private final TimerUtil animationTimer = new TimerUtil();
	
	private TimerUtil timer = new TimerUtil();
	private float lastHealth;
	
	private EntityPlayer player;
	private boolean hadTarget;
	
	@Listener
	public void onRender(RenderEvent event) {
		boolean rendered = false;
		Killaura killaura = (Killaura) Vestige.getInstance().getModuleManager().getModule(Killaura.class);
		if(killaura.isEnabled() && killaura.getTarget() != null && this.isEnabled()) {
			if(killaura.getTarget() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) killaura.getTarget();

				this.player = player;

				float mult = 0.006F;

				if(mult * animationTimer.getTimeElapsed() < 2.1) {
					mult -= (float) (animationTimer.getTimeElapsed() * 0.0000008);
				}

				mult *= animationTimer.getTimeElapsed();
				mult = Math.min(mult, 1);

				renderTargetHUD(player, mult);
				rendered = true;
				hadTarget = true;
			}
		}

		if(!rendered) {
			if(hadTarget) {
				animationTimer.reset();
				hadTarget = false;
			}

			if(player != null) {
				float mult = 0.006F;

				if(mult * animationTimer.getTimeElapsed() < 2.1) {
					mult -= (float) (animationTimer.getTimeElapsed() * 0.0000008);
				}

				mult *= animationTimer.getTimeElapsed();
				//mult = Math.min(mult, 1);

				float finalMult = 1 - mult;

				if(finalMult > 0) {
					renderTargetHUD(player, 1 - mult);
					rendered = true;
				} else {
					player = null;
					rendered = false;
					lastHealth = 20;
				}
			}
		}

		if(!rendered && !(mc.currentScreen instanceof GuiChat)) {
			animationTimer.reset();
		}
	}
	
	public void onChatRender(int mouseX, int mouseY, float partialTicks) {
		renderTargetHUD(mc.thePlayer, 1);
	}
	
	public void renderTargetHUD(EntityPlayer target, float mult) {
    	FontRenderer fr = mc.fontRendererObj;
    	float endX = x + width;
    	float endY = y + height;
		
		float xAmount = endX - (width / 2);
		float yAmount = endY - (height / 2);
		
		float finalXAnim = xAmount - mult * xAmount;
		float finalYAnim = yAmount - mult * yAmount;
		
		GL11.glTranslatef(finalXAnim, finalYAnim, 0);
		GL11.glScalef(mult, mult, 0);
    	
		if(mode.is("Normal")) {
			DrawUtil.drawRoundedRect(x, y, endX, endY, 6, 0x99000000);
		} else if(mode.is("New")) {
			if(mult > 0.8) {
				BlurUtil.blur(x, y, endX, endY);
			}
			DrawUtil.drawRoundedRect(x, y, endX, endY, 6, 0x75000000);
		}
		
		HUD hud = (HUD) Vestige.getInstance().getModuleManager().getModule(HUD.class);
		
		long timeElapsed = timer.getTimeElapsed();
		timer.reset();
		
		timeElapsed = Math.max(timeElapsed, 1);
		
		float health = lastHealth - ((lastHealth - target.getHealth()) * 0.013F * timeElapsed);
		
		health = Math.max(health, target.getHealth());
		health = Math.min(20, health);
		
		for(float i = x + 5; i < x + health * 4; i++) {
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
			Gui.drawRect(i, y + 35, i + 1, endY - 5, color);
		}

		RenderUtils.drawHead(((AbstractClientPlayer) target).getLocationSkin(), (int) x + 85, (int) y + 5, 40, 40);
		fr.drawStringWithShadow(target.getGameProfile().getName(), x + 5, y + 9, -1);
		fr.drawStringWithShadow((int) target.getHealth() + " HP", x + 5, y + 21, -1);
		
		lastHealth = health;
    	
    	GL11.glScalef(1 / mult, 1 / mult, 0);
    	GL11.glTranslatef(-finalXAnim, -finalYAnim, 0);
    }
	
}