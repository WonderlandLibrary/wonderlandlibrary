package xyz.cucumber.base.module.feat.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import xyz.cucumber.base.Client;
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
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

@ModuleInfo(category = Category.OTHER, description = "Displays enabled modules", name = "Array List", priority = ArrayPriority.LOW)
public class ArrayListModule extends Mod implements Dragable{

	
	public ArrayList<ArrayModule> mods = new ArrayList();
	
	private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 1000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 1000, 1);
	
	private BooleanSettings splitNames = new BooleanSettings("Split Names", true);
	
	private ModeSettings textStyle = new ModeSettings("Text Style", new String[] {
			"Normal", "Uppercase",
			"Lowercase"
	});
	
	private NumberSettings scale = new NumberSettings("Scale", 1, 0.3, 2, 0.02);
	
	private ModeSettings fonts = new ModeSettings("Fonts", SettingsUtils.getFonts());
	
	private ModeSettings priority = new ModeSettings("Priority", new String[] {
			"Low","Medium", "High"
	});
	
	private ModeSettings direction = new ModeSettings("Direction", new String[] {
			"Left Up", "Right Up",
			"Left Down", "Right Down"	
	});
	
	private NumberSettings spacing = new NumberSettings("Spacing",10, 8, 20, 0.2);
	private NumberSettings correction = new NumberSettings("Center corection",0, -5, 5, 0.02);
	
	private BooleanSettings rounded = new BooleanSettings("Rounded", true);
	
	private ColorSettings modColor = new ColorSettings("Text Color", "Static", -1, -1, 100);
	
	private BooleanSettings shadow = new BooleanSettings("Shadow", true);
	
	private ColorSettings shadowColor = new ColorSettings("Shadow color", "Static", -1, -1, 100);
	
	private BooleanSettings suffix = new BooleanSettings("Suffix", true);
	private ModeSettings suffixMode = new ModeSettings("Suffix Mode",new String[] {
			"None", "[]", "<>", "-"
	});
	private ColorSettings suffixColor = new ColorSettings("Suffix Color", "Static", 0xff777777, -1, 100);
	
	
	private BooleanSettings blur = new BooleanSettings("Blur", true);
	
	private BooleanSettings bloom = new BooleanSettings("Bloom", true);
	private BooleanSettings bloomMode = new BooleanSettings("Bloom same as font", true);
	private ColorSettings bloomColor = new ColorSettings("Bloom Color", "Static", 0xff000000, -1, 100);
	
	private BooleanSettings backgroundMode = new BooleanSettings("Background same as font", true);
	private ColorSettings backgroundColor = new ColorSettings("Background Color", "Static", 0xff000000, -1, 100);
	
	
	
	
	private BooleanSettings outline = new BooleanSettings("Outline", true);
	private NumberSettings size = new NumberSettings("Outline size", 100, 0, 100,1);
	private BooleanSettings outlineMode = new BooleanSettings("Outline same as font", true);
	private ColorSettings outlineColor = new ColorSettings("Outline Color", "Static", 0xff000000, -1, 100);
	private BooleanSettings outlineL = new BooleanSettings("Outline Left", true);
	private BooleanSettings outlineR = new BooleanSettings("Outline Right", true);
	private BooleanSettings outlineTop = new BooleanSettings("Outline Top", true);
	private BooleanSettings outlineBot = new BooleanSettings("Outline Bottom", true);
	private BooleanSettings outlineBetweenBot = new BooleanSettings("Outline Between Bottom", true);

	
	
	public ArrayListModule() {
		this.addSettings(
				positionX,
				positionY,
				
				splitNames,
				textStyle,
				direction,
				spacing,
				correction,
				fonts,
				priority,
				scale,
				
				rounded,
				
				modColor,
				shadow,
				shadowColor,
				
				suffix,
				suffixMode,
				suffixColor,
				
				blur,
				
				bloom,
				bloomMode,
				bloomColor,
				
				backgroundMode,
				backgroundColor,
				
				outline,
				size,
				outlineMode,
				outlineColor,
				outlineL,
				outlineR,
				outlineTop,
				outlineBot,
				outlineBetweenBot
				);
	}
	
	public void onEnable() {
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		mods.clear();
		for(Mod m : Client.INSTANCE.getModuleManager().getModules()) {
			mods.add(new ArrayModule(m, new PositionUtils(pos[0], pos[1],0,0,1)));
		}
	}
	
	@EventListener
	public void onBloom(EventBloom e) {
		
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		
		if(!bloom.isEnabled()) return;
		
		if(e.getType() == EventType.PRE) {
			e.setCancelled(true);
			return;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(pos[0] - pos[0]*scale.getValue(), pos[1]- pos[1]*scale.getValue(), 1);
		GL11.glScaled(scale.getValue(), scale.getValue(), 1);
		int i = 0;
		double spacing = this.spacing.getValue();
		for(ArrayModule mod : mods) {
			switch(priority.getMode().toLowerCase()) {
			case "medium":
				if(mod.module.getPriority() == ArrayPriority.LOW) {
					continue;
				}
				break;
			case "high":
				if(mod.module.getPriority() != ArrayPriority.HIGH) {
					continue;
				}
				break;
			}
			if(mod.module.isEnabled()) {
				
			}else {
				if(mod.animation < 1) continue;
			}
			int color = ColorUtils.getColor(bloomColor, System.nanoTime()/1000000, i*10, 5);
			if(bloomMode.isEnabled()) {
				ColorSettings fixedColor = new ColorSettings(modColor.getMode(), modColor.getMode(), modColor.getMainColor(), modColor.getSecondaryColor(), bloomColor.getAlpha());
				color =  ColorUtils.getColor(fixedColor, System.nanoTime()/1000000, i*10, 5);
			}
			renderBackGround(mod, color);
			i++;
		}
		GL11.glPopMatrix();
	}
	@EventListener
	public void onBlur(EventBlur e) {
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		if(!blur.isEnabled()) return;
		
		if(e.getType() == EventType.PRE) {
			e.setCancelled(true);
			return;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslated(pos[0] - pos[0]*scale.getValue(), pos[1]- pos[1]*scale.getValue(), 1);
		GL11.glScaled(scale.getValue(), scale.getValue(), 1);
		for(ArrayModule mod : mods) {
			switch(priority.getMode().toLowerCase()) {
			case "medium":
				if(mod.module.getPriority() == ArrayPriority.LOW) {
					continue;
				}
				break;
			case "high":
				if(mod.module.getPriority() != ArrayPriority.HIGH) {
					continue;
				}
				break;
			}
			if(mod.module.isEnabled()) {
			}else {
				if(mod.animation < 1) continue;
			}
			renderBackGround(mod, -1);
			
		}
		GL11.glPopMatrix();
	}
	
	@EventListener
	public void onRenderGui(EventRenderGui e) {
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		
		this.info = direction.getMode();
		Collections.sort(mods, new sortBySize());
		GL11.glPushMatrix();
		GL11.glTranslated(pos[0] - pos[0]*scale.getValue(), pos[1]- pos[1]*scale.getValue(), 1);
		GL11.glScaled(scale.getValue(), scale.getValue(), 1);
		
		int i = 0;
		for(ArrayModule mod : mods) {
			switch(priority.getMode().toLowerCase()) {
			case "medium":
				if(mod.module.getPriority() == ArrayPriority.LOW) {
					continue;
				}
				break;
			case "high":
				if(mod.module.getPriority() != ArrayPriority.HIGH) {
					continue;
				}
				break;
			}
			String hor = direction.getMode().toLowerCase().split(" ")[0];
			String ver = direction.getMode().toLowerCase().split(" ")[1];
			String font = fonts.getMode().toLowerCase();
			
			String suffix = getSuffix(mod);
			String fullString = mod.module.getName(splitNames.isEnabled()) + suffix;
			
			if(textStyle.getMode().toLowerCase().equals("lowercase")) {
				fullString = fullString.toLowerCase();
			}else if(textStyle.getMode().toLowerCase().equals("uppercase")) fullString = fullString.toUpperCase();
			double spacing = this.spacing.getValue();
			
			double width = Fonts.getFont(font).getWidth(fullString)+6;
			
			mod.position.setWidth(width);
			mod.position.setHeight(spacing);
			
			
			if(mod.module.isEnabled()) {
				mod.animation = ((mod.animation * 10 + (width))/11);
			}else {
				mod.animation = ((mod.animation * 10)/11);
				if(mod.animation < 1) continue;
			}
			if(ver.equals("up")) {
				mod.position.setY((mod.position.getY()* 10+(pos[1]+ spacing*i))/11);
			}else {
				mod.position.setY((mod.position.getY()* 10+(pos[1]-spacing*i))/11);
			}
			
			if(hor.equals("right")) {
				mod.position.setX(pos[0]-mod.animation);
			}else {
				mod.position.setX(pos[0]+mod.animation-width);
			}

			int color = ColorUtils.getColor(backgroundColor, System.nanoTime()/1000000, i*10, 5);
			if(backgroundMode.isEnabled()) {
				ColorSettings fixedColor = new ColorSettings(modColor.getMode(), modColor.getMode(), modColor.getMainColor(), modColor.getSecondaryColor(), backgroundColor.getAlpha());
				color =  ColorUtils.getColor(fixedColor, System.nanoTime()/1000000, i*10, 5);
			}
			
			renderBackGround(mod, color);
			renderOutlineForMod(mod);
			String modname = mod.module.getName(splitNames.isEnabled());
			String suffixname = suffix;
			
			if(textStyle.getMode().toLowerCase().equals("lowercase")) {
				 suffixname =  suffixname.toLowerCase();
				 modname =  modname.toLowerCase();
			}else if(textStyle.getMode().toLowerCase().equals("uppercase")) {
				suffixname = suffixname.toUpperCase();
				modname =  modname.toUpperCase();
			}
			if(!mod.module.info.equals("") && mod.module.info != null) {
				
				if(hor.equals("right")) {
					if(shadow.isEnabled()) {
						Fonts.getFont(font).drawString(modname, mod.position.getX()+3+.5+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue()+0.5, ColorUtils.getColor(shadowColor, System.nanoTime()/1000000, i*10, 5));
						if(!mod.module.info.equals("") || mod.module.info != null) {
							Fonts.getFont(font).drawString(suffixname, mod.position.getX2()-Fonts.getFont(font).getWidth(suffixname)-3+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue()+0.5, ColorUtils.getColor(shadowColor, System.nanoTime()/1000000, i*10, 5));
						}
					}
					Fonts.getFont(font).drawString(modname, mod.position.getX()+3+.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue(), ColorUtils.getColor(modColor, System.nanoTime()/1000000, i*10, 5));
					if(!mod.module.info.equals("") || mod.module.info != null) {
						Fonts.getFont(font).drawString(suffixname, mod.position.getX2()-Fonts.getFont(font).getWidth(suffixname)-3, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue(), ColorUtils.getColor(suffixColor, System.nanoTime()/1000000, i*10, 5));
					}
				}else {
					if(shadow.isEnabled()) {
						Fonts.getFont(font).drawString(modname, mod.position.getX2()-Fonts.getFont(font).getWidth(modname)-3.5+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue()+0.5,ColorUtils.getColor(shadowColor, System.nanoTime()/1000000, i*10, 5));
						if(!mod.module.info.equals("") || mod.module.info != null) {
							Fonts.getFont(font).drawString(suffixname, mod.position.getX()+3+0.5+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue()+0.5, ColorUtils.getColor(shadowColor, System.nanoTime()/1000000, i*10, 5));
						}
					}
					Fonts.getFont(font).drawString(modname, mod.position.getX2()-Fonts.getFont(font).getWidth(modname)-3.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue(), ColorUtils.getColor(modColor, System.nanoTime()/1000000, i*10, 5));
					if(!mod.module.info.equals("") || mod.module.info != null) {
						Fonts.getFont(font).drawString(suffixname, mod.position.getX()+3+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue(), ColorUtils.getColor(suffixColor, System.nanoTime()/1000000, i*10, 5));
					}
				}
			}else {
				if(shadow.isEnabled()) {
					Fonts.getFont(font).drawString(modname, mod.position.getX()+mod.position.getWidth()/2-Fonts.getFont(font).getWidth(modname)/2+0.5+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue()+0.5, ColorUtils.getColor(shadowColor, System.nanoTime()/1000000, i*10, 5));
				}
				Fonts.getFont(font).drawString(modname, mod.position.getX()+mod.position.getWidth()/2-Fonts.getFont(font).getWidth(modname)/2+0.5, mod.position.getY()+ mod.position.getHeight()/2-Fonts.getFont(font).getHeight(fullString)/2+correction.getValue(), ColorUtils.getColor(modColor, System.nanoTime()/1000000, i*10, 5));
			}
			
			
			i++;
		}
		GL11.glPopMatrix();
	}
	public void renderOutlineForMod(ArrayModule mod) {
		if(!outline.isEnabled()) return;
		String hor = direction.getMode().toLowerCase().split(" ")[0];
		String ver = direction.getMode().toLowerCase().split(" ")[1];
		int index = getAvalibeModules().indexOf(mod);
		
		int color = ColorUtils.getColor(outlineColor, System.nanoTime()/1000000, index*10, 5);
		if(outlineMode.isEnabled()) {
			ColorSettings fixedColor = new ColorSettings(modColor.getMode(), modColor.getMode(), modColor.getMainColor(), modColor.getSecondaryColor(), outlineColor.getAlpha());
			color =  ColorUtils.getColor(fixedColor, System.nanoTime()/1000000, index*10, 5);
		}
		
		if(outlineL.isEnabled()) {
			double w = mod.position.getHeight()*size.getValue()/100F;
			double var1 = (mod.position.getHeight() - w)/2;
			RenderUtils.drawLine(mod.position.getX(), mod.position.getY()+var1, mod.position.getX(), mod.position.getY2()-var1, color, (float) (2f*scale.getValue()));
		}
			
		if(outlineR.isEnabled()) {
			double w = mod.position.getHeight()*size.getValue()/100F;
			double var1 = (mod.position.getHeight() - w)/2;
			RenderUtils.drawLine(mod.position.getX2(), mod.position.getY()+var1, mod.position.getX2(), mod.position.getY2()-var1, color, (float) (2f*scale.getValue()));
		}
		
		
		if(ver.equals("up")) {
			double w = mod.position.getWidth()*size.getValue()/100F;
			double var1 = (mod.position.getWidth() - w)/2;
			if(index == 0) {
				if(outlineTop.isEnabled())
					RenderUtils.drawLine(mod.position.getX()+var1, mod.position.getY(), mod.position.getX2()-var1, mod.position.getY(), color, (float) (2f*scale.getValue()));
			}
			if(getAvalibeModules().size()-1 == index) {
				if(outlineBot.isEnabled())
					RenderUtils.drawLine(mod.position.getX()+var1, mod.position.getY2(), mod.position.getX2()-var1, mod.position.getY2(), color, (float) (2f*scale.getValue()));
			}
		}else {
			double w = mod.position.getWidth()*size.getValue()/100F;
			double var1 = (mod.position.getWidth() - w)/2;
			if(index == 0) {
				if(outlineBot.isEnabled())
					RenderUtils.drawLine(mod.position.getX()+var1, mod.position.getY2(), mod.position.getX2()-var1, mod.position.getY2(), color, (float) (2f*scale.getValue()));
			}
			if(getAvalibeModules().size()-1 == index) {
				if(outlineTop.isEnabled())
					RenderUtils.drawLine(mod.position.getX()+var1, mod.position.getY(), mod.position.getX2()-var1, mod.position.getY(), color, (float) (2f*scale.getValue()));
			}
		}
		
		if(index != 0 && outlineBetweenBot.isEnabled()) {
			if(hor.equals("right")) {
				double size = getAvalibeModules().get(index-1).position.getWidth()-mod.position.getWidth();
				if(ver.equals("down")) {
					RenderUtils.drawLine(mod.position.getX()-size, mod.position.getY2(), mod.position.getX(), mod.position.getY2(), color, (float) (2f*scale.getValue()));
				}else {
					RenderUtils.drawLine(mod.position.getX()-size, mod.position.getY(), mod.position.getX(), mod.position.getY(), color, (float) (2f*scale.getValue()));
				}
				
			}else {
				double size = getAvalibeModules().get(index-1).position.getWidth()-mod.position.getWidth();
				if(ver.equals("down")) {
					RenderUtils.drawLine(mod.position.getX2(), mod.position.getY2(), mod.position.getX2()+size, mod.position.getY2(), color, (float) (2f*scale.getValue()));
				}else {
					RenderUtils.drawLine(mod.position.getX2(), mod.position.getY(), mod.position.getX2()+size, mod.position.getY(), color, (float) (2f*scale.getValue()));
				}
			}
		}
		
	}
	
	public void renderBackGround(ArrayModule mod, int color) {
		int index = getAvalibeModules().indexOf(mod);
		
		String hor = direction.getMode().toLowerCase().split(" ")[0];
		String ver = direction.getMode().toLowerCase().split(" ")[1];
		
		if(!rounded.isEnabled())
			RenderUtils.drawRect(mod.position.getX(),mod.position.getY(), mod.position.getX2(), mod.position.getY2(), color);
		else {
			if(index == 0) {
				double size = 2;
				boolean leftT = false;
				boolean rightT = false;
				boolean leftB = false;
				boolean rightB = false;
				if(getAvalibeModules().size() == 1) {
					leftT =  true;
					rightT =  true;
					leftB =  true;
					rightB = true;
				} else {
					int futureindex = getAvalibeModules().indexOf(mod)+1;
					if(ver.equals("up")) {
						leftT =  true;
						rightT =  true;
						if(hor.equals("right")) {
							leftB =  true;
						}else {
							rightB = true;
						}
					}else {
						leftB =  true;
						rightB = true;
						if(hor.equals("right")) {
							leftT =  true;
						}else {
							rightT = true;
						}
					}
					
					if(size > Math.abs(getAvalibeModules().get(futureindex).position.getWidth() - mod.position.getWidth())) {
						size = Math.abs(getAvalibeModules().get(futureindex).position.getWidth() - mod.position.getWidth());
					}
				}
				RenderUtils.drawRoundedRectWithCorners(mod.position.getX(),mod.position.getY(), mod.position.getX2(), mod.position.getY2(), color,
						size, leftT, rightT, leftB, rightB);
			}else if (index == getAvalibeModules().indexOf(getAvalibeModules().get(getAvalibeModules().size()-1))) {
				boolean leftT = false;
				boolean rightT = false;
				boolean leftB = false;
				boolean rightB = false;
				
				double size = 2;
				
				if(ver.equals("up")) {
					leftB =  true;
					rightB =  true;
				}else {
					leftT =  true;
					rightT = true;
				}
				
				RenderUtils.drawRoundedRectWithCorners(mod.position.getX(),mod.position.getY(), mod.position.getX2(), mod.position.getY2(), color,
						size, leftT, rightT, leftB, rightB);
			}else {
				
				double size = 2;
				boolean leftT = false;
				boolean rightT = false;
				boolean leftB = false;
				boolean rightB = false;
				
				if(hor.equals("right")) {
					
					if(ver.equals("up")) {
						leftB =  true;
					}else {
						leftT =  true;
					}
				}else {
					if(ver.equals("up")) {
						rightB = true;
					}else {
						rightT =  true;
					}
				}
				int futureindex = getAvalibeModules().indexOf(mod)+1;
				if(size > Math.abs(getAvalibeModules().get(futureindex).position.getWidth() - mod.position.getWidth())) {
					size = Math.abs(getAvalibeModules().get(futureindex).position.getWidth() - mod.position.getWidth());
				}
				
				RenderUtils.drawRoundedRectWithCorners(mod.position.getX(),mod.position.getY(), mod.position.getX2(), mod.position.getY2(), color,
						size, leftT, rightT, leftB, rightB);
			}
		}
	}
	
	public ArrayList<ArrayModule> getAvalibeModules() {
		
		ArrayList<ArrayModule> md = new ArrayList();
		for(ArrayModule mod : mods) {
			switch(priority.getMode().toLowerCase()) {
			case "medium":
				if(mod.module.getPriority() == ArrayPriority.LOW) {
					continue;
				}
				break;
			case "high":
				if(mod.module.getPriority() != ArrayPriority.HIGH) {
					continue;
				}
				break;
			}
			if(!mod.module.isEnabled()) {
				if(mod.animation < 1) continue;
			}
			md.add(mod);
		}
		return md;
	}
	public String getSuffix(ArrayModule mod) {
		String suffix = "";
		String hor = direction.getMode().toLowerCase().split(" ")[0];
		if(this.suffix.isEnabled() && mod.module.info != null && !mod.module.info.equals("")) {
			switch(suffixMode.getMode().toLowerCase()) {
			case "none":
				suffix = (hor.equals("right")? " " : "")+mod.module.info+(hor.equals("left")? " " : "");
				break;
			case "[]":
				suffix = (hor.equals("right")? " " : "")+"[" + mod.module.info+"]"+(hor.equals("left")? " " : "");
				break;
			case "<>":
				suffix = (hor.equals("right")? " " : "")+"<" + mod.module.info+">"+(hor.equals("left")? " " : "");
				break;
			case "-":
				suffix = (hor.equals("right")? " - " : "") + mod.module.info +(hor.equals("left")? " - " : "");
				break;
			}
		}
		return suffix;
	}
	@Override
	public PositionUtils getPosition() {
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		
		String hor = direction.getMode().toLowerCase().split(" ")[0];
		String ver = direction.getMode().toLowerCase().split(" ")[1];
		double vertical = 0;
		if(!ver.equals("up")) {
			vertical = getAvalibeModules().size() == 0 ?spacing.getValue()*scale.getValue() : getAvalibeModules().size()*spacing.getValue()*scale.getValue();
		}

		double horizontal = 0;
		if(hor.equals("right")) {
			horizontal = getAvalibeModules().size() == 0 ? 50 : getAvalibeModules().get(0).position.getWidth();
		}
		return new PositionUtils(pos[0]-horizontal, pos[1]+vertical, getAvalibeModules().size() == 0 ? 50 : getAvalibeModules().get(0).position.getWidth(), getAvalibeModules().size() == 0 ?spacing.getValue()*scale.getValue() :getAvalibeModules().size()*spacing.getValue()*scale.getValue(),1);
	}
	
	public class sortBySize implements Comparator<ArrayModule>{

		@Override
		public int compare(ArrayModule o1, ArrayModule o2) {
			String font = fonts.getMode().toLowerCase();
			String n1 = o1.module.getName(splitNames.isEnabled())+getSuffix(o1);
			String n2 = o2.module.getName(splitNames.isEnabled())+getSuffix(o2);
			

			if(textStyle.getMode().toLowerCase().equals("lowercase")) {
				n1 =  n1.toLowerCase();
				n2 =  n2.toLowerCase();
			}else if(textStyle.getMode().toLowerCase().equals("uppercase")) {
				n1 = n1.toUpperCase();
				n2 =  n2.toUpperCase();
			}
			
			if(Fonts.getFont(font).getWidth(n1) > Fonts.getFont(font).getWidth(n2))
				return -1;
			else return 1;
		}
	}
	
	public class ArrayModule {
		public Mod module;
		public PositionUtils position;
		
		public double animation;
		public ArrayModule(Mod module, PositionUtils position) {
			this.module = module;
			this.position = position;
			
		}
	}

	@Override
	public void setXYPosition(double x, double y) {
		String hor = direction.getMode().toLowerCase().split(" ")[0];
		String ver = direction.getMode().toLowerCase().split(" ")[1];
		double vertical = 0;
		if(!ver.equals("up")) {
			vertical = getAvalibeModules().size() == 0 ?spacing.getValue()*scale.getValue() : getAvalibeModules().size()*spacing.getValue()*scale.getValue();
		}

		double horizontal = 0;
		if(hor.equals("right")) {
			horizontal = getAvalibeModules().size() == 0 ? 50 : getAvalibeModules().get(0).position.getWidth();
		}
		this.positionX.setValue(x+horizontal);
		this.positionY.setValue(y-vertical);
	}
}