package xyz.cucumber.base.module.feat.visuals;

import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.events.ext.EventBlur;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.addons.Dragable;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.cfgs.SettingsUtils;
import xyz.cucumber.base.utils.math.PositionHandler;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.BloomUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

@ModuleInfo(category = Category.VISUALS, description = "Displays your active effects", name = "Effect Status", priority = ArrayPriority.LOW)
public class EffectStatusModule extends Mod implements Dragable{
	
	private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 1000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 1000, 1);
	
	private PositionUtils position = new PositionUtils(0,0, 80, 15,1);
	
	private ModeSettings textStyle = new ModeSettings("Text Style", new String[] {
			"Normal", "Uppercase",
			"Lowercase"
	});
	
	private ModeSettings fonts = new ModeSettings("Fonts", SettingsUtils.getFonts());
	
	private BooleanSettings rounded = new BooleanSettings("Rounded", true);
	
	private ColorSettings modColor = new ColorSettings("Text Color", "Static", -1, -1, 100);
	
	private BooleanSettings blur = new BooleanSettings("Blur", true);
	
	private BooleanSettings b = new BooleanSettings("Bloom", true);
	private BooleanSettings bloomMode = new BooleanSettings("Bloom same as font", true);
	private ColorSettings bloomColor = new ColorSettings("Bloom Color", "Static", 0xff000000, -1, 100);
	
	private ModeSettings backgroundMode = new ModeSettings("Background Mode", new String[] {
			"Default", "Text",
			"Custom"
	});
	private ColorSettings backgroundColor = new ColorSettings("Background Color", "Static", 0xff000000, -1, 100);
	
	private BloomUtils bloom = new BloomUtils();
	
	private double animation;
	
	public EffectStatusModule() {
		this.addSettings(
				positionX,
				positionY,
				textStyle,
				fonts,
				rounded,
				modColor,
				blur,
				b,
				bloomMode,
				bloomColor,
				backgroundMode,
				backgroundColor
		);
	}
	
	@EventListener
	public void onBloom(EventBloom e) {
		if(b.isEnabled()) {
			if(e.getType() == EventType.PRE) {
				e.setCancelled(true);
				return;
			}
			RenderUtils.drawCustomRect(position.getX(), position.getY(), position.getX2(), position.getY2()-1, ColorUtils.getColor(bloomMode.isEnabled() ? modColor : bloomColor, System.nanoTime()/1000000, 0, 5), 1, rounded.isEnabled());
		}
			

	}
	
	@EventListener
	public void onBlur(EventBlur e) {
		if(blur.isEnabled()) {
			if(e.getType() == EventType.PRE) {
				e.setCancelled(true);
				return;
			}
			RenderUtils.drawCustomRect(position.getX(), position.getY(), position.getX2(), position.getY2(), 0xff000000, 1, rounded.isEnabled());
		}
	}
	
	@Override
	public PositionUtils getPosition() {
		return position;
	}

	@Override
	public void setXYPosition(double x, double y) {
		positionX.setValue(x);
		positionY.setValue(y);
	}

	@EventListener
	public void onRenderGui(EventRenderGui e) {
		
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		position.setX(pos[0]);
		position.setY(pos[1]);
		
		GL11.glPushMatrix();
		String font = fonts.getMode().toLowerCase();
		
		animation = (animation * 9 + (15+mc.thePlayer.activePotionsMap.size()*10))/10;
		
		StencilUtils.initStencil();
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        StencilUtils.bindWriteStencilBuffer();
        RenderUtils.drawCustomRect(position.getX(), position.getY(), position.getX2(), position.getY2(), 0xff252725, 1, rounded.isEnabled());
		
		StencilUtils.bindReadStencilBuffer(1);
		
		int color = ColorUtils.getColor(backgroundColor, System.nanoTime()/1000000, 0, 5);
		if(backgroundMode.getMode().equalsIgnoreCase("Text")) {
			ColorSettings fixedColor = new ColorSettings(modColor.getMode(), modColor.getMode(), modColor.getMainColor(), modColor.getSecondaryColor(), backgroundColor.getAlpha());
			color =  ColorUtils.getColor(fixedColor, System.nanoTime()/1000000, 0, 5);
		}
		if(backgroundMode.getMode().equalsIgnoreCase("Default")) {
			color =  0xee212121;
		}
		RenderUtils.drawCustomRect(position.getX(), position.getY(), position.getX2(), position.getY2(), color, 1, rounded.isEnabled());
		if(mc.thePlayer.activePotionsMap.entrySet().size() != 0) {
			
			GL11.glPushMatrix();
			
			RenderUtils.start2D();
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glLineWidth(2);
			
			GL11.glBegin(GL11.GL_LINE_LOOP);
			RenderUtils.color(0x00aaaaaa);
			GL11.glVertex2d(position.getX(), position.getY()+12);
			RenderUtils.color(0xffaaaaaa);
			GL11.glVertex2d(position.getX()+position.getWidth()/2, position.getY()+12);
			RenderUtils.color(0x00aaaaaa);
			GL11.glVertex2d(position.getX2(), position.getY()+12);
			
			GL11.glEnd();
			
			RenderUtils.stop2D();
			
			GL11.glPopMatrix();
		}
		
		
		position.setHeight(animation);
		Fonts.getFont(font).drawString("Effects", position.getX()+position.getWidth()/2-Fonts.getFont(font).getWidth("Effects")/2, position.getY()+6, ColorUtils.getColor(modColor, System.nanoTime()/1000000, 0, 5));
		
		//potions.getDuration()/20
		int i = 0;
		for(Entry<Integer, PotionEffect> potions : mc.thePlayer.activePotionsMap.entrySet()) {
			
			Potion potion = Potion.potionTypes[potions.getValue().getPotionID()];
			
			String potionName = potions.getValue().getEffectName();
			potionName = I18n.format(potionName, new Object[0]);
			if(textStyle.getMode().toLowerCase().equals("lowercase")) {
				potionName = potionName.toLowerCase();
			}else if(textStyle.getMode().toLowerCase().equals("uppercase")) potionName = potionName.toUpperCase();
			
			GL11.glPushMatrix();
			Fonts.getFont(font).drawString(potionName+" "+ (potions.getValue().getAmplifier()+1 == 1 ? "" : (potions.getValue().getAmplifier()+1) + ""), position.getX()+2, position.getY()+16+10*i, 0xffcccccc);
			Fonts.getFont(font).drawString(Potion.getDurationString(potions.getValue()),position.getX2()-Fonts.getFont(font).getWidth(Potion.getDurationString(potions.getValue())), position.getY()+16+10*i, -1);
			GL11.glPopMatrix();
			i++;
		}
		
		
        StencilUtils.uninitStencilBuffer();
        GL11.glPopMatrix();
		
	}
}