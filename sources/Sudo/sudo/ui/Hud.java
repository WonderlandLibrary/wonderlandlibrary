package sudo.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.systems.RenderSystem;

import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import sudo.Client;
import sudo.module.Mod;
import sudo.module.Mod.Category;
import sudo.module.ModuleManager;
import sudo.module.client.ArrylistModule;
import sudo.module.client.ClickGuiMod;
import sudo.module.combat.Killaura;
import sudo.module.combat.TargetHud;
import sudo.module.render.Notifications;
import sudo.module.render.PlayerEntityModule;
import sudo.module.render.TabGui;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.KeybindSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.module.settings.Setting;
import sudo.utils.TimerUtil;
import sudo.utils.misc.Notification;
import sudo.utils.misc.NotificationUtil;
import sudo.utils.render.ColorUtils;
import sudo.utils.render.RenderUtils;
import sudo.utils.surge.animation.BoundedAnimation;
import sudo.utils.surge.animation.Easing;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;
import sudo.utils.text.KeyUtils;

//=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

public class Hud {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	public static GlyphPageFontRenderer textRend = IFont.CONSOLAS;

	static int xOffset, yOffset;
	
	public static final ManagedCoreShader BLUR_SHADER = ShaderEffectManager.getInstance().manageCoreShader(new Identifier("blur"));
	private static ArrylistModule arrayModule = ModuleManager.INSTANCE.getModule(ArrylistModule.class);
	private static ClickGuiMod ClickGuiMod = ModuleManager.INSTANCE.getModule(ClickGuiMod.class);
	
	public static void render(MatrixStack matrices, float tickDelta) {
		textRend.drawString(matrices, "Sudo client", 5, 5, -1, 1);
		
		if (arrayModule.show.isEnabled()) renderArrayList(matrices);
		if (ModuleManager.INSTANCE.getModule(TargetHud.class).isEnabled()) renderTargetHud(matrices);
		if (ModuleManager.INSTANCE.getModule(Notifications.class).enabled.isEnabled()) Notifs(matrices);
		if (ModuleManager.INSTANCE.getModule(PlayerEntityModule.class).isEnabled()) RenderUtils.drawEntity(30, 75, 30, mc.player.getPitch(), mc.player.getYaw(), mc.player);
		if (ModuleManager.INSTANCE.getModule(TabGui.class).isEnabled()) renderTabGui(matrices);
	}
	public static int currentCategoryIndex, animCategoryIndex, animModuleIndex, moduleExpandAnim = 0, currentSettingIndex, animSettingIndex, settingExpandAnim = 0;
	public static boolean moduleExpanded, settingExpanded;
	
	public static void renderTabGui(MatrixStack matrices) {
		int offsetx = 0, offsety = 0;
		Category category = Mod.Category.values()[currentCategoryIndex];
		List<Mod> modules = ModuleManager.INSTANCE.getModulesInCategory(category);
		int index = 0, settingIndex=0;
		
		if (animCategoryIndex<currentCategoryIndex*15)animCategoryIndex++;
		if (animCategoryIndex>currentCategoryIndex*15)animCategoryIndex--;

		RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), 5+offsetx, 20+offsety, 62+offsetx, 35+((Mod.Category.values().length-1)*15)+offsety, 1, 20);
		RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), 6+offsetx, 21+animCategoryIndex+offsety, 61+offsetx, 34+animCategoryIndex+offsety, 1, 20);
		for (Category c : Mod.Category.values()) {
			textRend.drawString(matrices, c.name, 7+offsetx, 20+index+offsety, -1, 1);
			index+=15;
		}
		
		index=0;
	
		if (animModuleIndex<category.moduleIndex*15)animModuleIndex++;
		if (animModuleIndex>category.moduleIndex*15)animModuleIndex--;

		//e
		if (animSettingIndex<currentSettingIndex*15)animSettingIndex++;
		if (animSettingIndex>currentSettingIndex*15)animSettingIndex--;
		
		if (moduleExpanded) if (moduleExpandAnim<86) moduleExpandAnim+=5;
		if (moduleExpandAnim<0) moduleExpandAnim=0;
		if (moduleExpandAnim>86) moduleExpandAnim=86;
		
		if (settingExpanded) if (settingExpandAnim<100) settingExpandAnim+=5;
		if (settingExpandAnim<0) settingExpandAnim=0;
		if (settingExpandAnim>100) settingExpandAnim=100;
		
		RenderUtils.startScissor(60, 0, moduleExpandAnim+settingExpandAnim+offsetx, 800+offsety);
		RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), 63+offsetx, 20+offsety, 146+offsetx, 35+(modules.size()-1)*15+offsety, 1, 20);
		RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), 64+offsetx, 21+animModuleIndex+offsety, 145+offsetx, 34+animModuleIndex+offsety, 1, 20);
		for (Mod mod : modules) {
			textRend.drawString(matrices, mod.getName(), 3+62+offsetx, 20+index+offsety, mod.isEnabled() ? ColorUtils.mixColorsAnimated(index*10, -1, ClickGuiMod.primaryColor.getColor(), ClickGuiMod.secondaryColor.getColor()).getRGB() : -1, 1);

			if (index/15==category.moduleIndex) {
				RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), 63+84+offsetx, 20+offsety, 146+84+12+offsetx, 35+(mod.getSettings().size()-1)*15+offsety, 1, 20);
				RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), 64+84+offsetx, 21+animSettingIndex+offsety, 145+84+12+offsetx, 34+animSettingIndex+offsety, 1, 20);
				RenderUtils.renderRoundedQuad(matrices, mod.getSetting().get(currentSettingIndex).focused ? ColorUtils.mixColorsAnimated(settingIndex, -1, ClickGuiMod.primaryColor.getColor(), ClickGuiMod.secondaryColor.getColor()) : new Color(0, 0, 0, 0), 64+84+offsetx-1, 21+animSettingIndex+offsety, 145+84-79, 34+animSettingIndex+offsety, 1, 20);
				for (Setting setting : mod.getSetting()) {
					if (setting instanceof BooleanSetting) {
						RenderUtils.renderRoundedQuad(matrices, ((BooleanSetting) setting).isEnabled() ? ColorUtils.mixColorsAnimated(settingIndex, -1, ClickGuiMod.primaryColor.getColor(), ClickGuiMod.secondaryColor.getColor()) : new Color(0, 0, 0, 90), 145+76+11+offsetx, 24+(settingIndex*15)+offsety, 145+83+11+offsetx, 31+(settingIndex*15)+offsety, 1, 20);
					} else if (setting instanceof KeybindSetting) {
						textRend.drawString(matrices, KeyUtils.NumToKey(((KeybindSetting) setting).getKey()), 3+62+81+80-textRend.getStringWidth(KeyUtils.NumToKey(((KeybindSetting) setting).getKey()))+12+offsetx, 20+(settingIndex*15)+offsety, -1, 1);
					} else if (setting instanceof ModeSetting) {
						textRend.drawString(matrices, ((ModeSetting) setting).getMode(), 3+62+81+80-textRend.getStringWidth(((ModeSetting) setting).getMode())+12+offsetx, 20+(settingIndex*15)+offsety, -1, 1);
					} else if (setting instanceof NumberSetting) {
						RenderUtils.renderRoundedQuad(matrices, ColorUtils.mixColorsAnimated(settingIndex, -1, ClickGuiMod.primaryColor.getColor(), ClickGuiMod.secondaryColor.getColor()), 63+85+offsetx, 30+(settingIndex*15)+offsety, 148+ ((81+11)*(((NumberSetting) setting).getValue() - ((NumberSetting) setting).getMin()) / (((NumberSetting) setting).getMax() - ((NumberSetting) setting).getMin()))+offsetx, 33+(settingIndex*15)+offsety, 1, 20);
						textRend.drawString(matrices, ((NumberSetting) setting).getValue() + "", 3+62+81+80-textRend.getStringWidth(((NumberSetting) setting).getValue() + "")+13+offsetx, 20+(settingIndex*15)+offsety, -1, 1);
					} else if (setting instanceof ColorSetting) {
						RenderUtils.renderRoundedQuad(matrices, ((ColorSetting) setting).getColor(), 145+76+5+offsetx, 24+(settingIndex*15)+offsety, 145+83+11+offsetx, 31+(settingIndex*15)+offsety, 1, 20);
						textRend.drawString(matrices, "#"+((ColorSetting) setting).getHex(), 3+62+81+80-6-textRend.getStringWidth("#"+((ColorSetting) setting).getHex())+12+offsetx, 20+(settingIndex*15)+2+offsety, ((ColorSetting) setting).getColor().getRGB(), 0.8f);
					}
					textRend.drawString(matrices, setting.getName(), 3+62+84+offsetx, 20+(settingIndex*15)+offsety, -1, 1);
					settingIndex++;
				}
			}
			index+=15;
		}
		RenderUtils.endScissor();
		if (!moduleExpanded) if (moduleExpandAnim>0) moduleExpandAnim-=5;
		if (!settingExpanded) if (settingExpandAnim>0) settingExpandAnim-=5;
	}
	
	public static void onKeyPress(int key, int action) {
		Category category = Mod.Category.values()[currentCategoryIndex];
		List<Mod> modules = ModuleManager.INSTANCE.getModulesInCategory(category);
		
		if (action == GLFW.GLFW_PRESS && mc.currentScreen==null) {
			
			if (moduleExpanded && settingExpanded) {
				Mod module = modules.get(category.moduleIndex);
				if (module.getSetting().get(currentSettingIndex).focused && module.getSetting().get(currentSettingIndex) instanceof KeybindSetting) {
					if (key!=GLFW.GLFW_KEY_ENTER && key!=GLFW.GLFW_KEY_UP && key!=GLFW.GLFW_KEY_DOWN && key!=GLFW.GLFW_KEY_LEFT && key!=GLFW.GLFW_KEY_RIGHT && key!=GLFW.GLFW_KEY_ESCAPE) {
						KeybindSetting keybind = ((KeybindSetting) modules.get(category.moduleIndex).getSetting().get(currentSettingIndex));
						
						if (key!=GLFW.GLFW_KEY_DELETE) {
							keybind.toggle();
							keybind.setKey(key);
							modules.get(category.moduleIndex).setKey(key);
							keybind.toggle();
							keybind.focused = false;
						} else if (key==GLFW.GLFW_KEY_DELETE) {
							keybind.toggle();
							keybind.setKey(0);
							modules.get(category.moduleIndex).setKey(0);
							keybind.toggle();
							keybind.focused = false;
						}
						Client.logger.info(key + modules.get(category.moduleIndex).getName());
						return;
					}
				}
			}
			
			if (key==GLFW.GLFW_KEY_UP) {
				if (moduleExpanded) {
					if (settingExpanded) {
						if (modules.get(category.moduleIndex).getSetting().get(currentSettingIndex).focused) {
							
						} else {
							if (currentSettingIndex<=0) {
								currentSettingIndex = modules.get(category.moduleIndex).getSettings().size()-1;
								animSettingIndex=(modules.get(category.moduleIndex).getSettings().size()-1)*15;
							}
							else currentSettingIndex--;
						}
					}
					else {
						if (category.moduleIndex<=0) {
							category.moduleIndex = modules.size()-1;
							animModuleIndex=(modules.size()-1)*15;
						}
						else category.moduleIndex--;
					}
				} else {
					if (currentCategoryIndex<=0) {
						currentCategoryIndex=Mod.Category.values().length-1;
						animCategoryIndex=(Mod.Category.values().length-1)*15;
					}
					else currentCategoryIndex--;
				}
			}
			
			if (key==GLFW.GLFW_KEY_DOWN) {
				if (moduleExpanded) {
					if (settingExpanded) {
						if (modules.get(category.moduleIndex).getSetting().get(currentSettingIndex).focused) {
							
						} else {
							if (currentSettingIndex>=modules.get(category.moduleIndex).getSettings().size()-1) {
								currentSettingIndex = 0;
								animSettingIndex=0;
							}
							else currentSettingIndex++;
						}
					}
					else {
						if (category.moduleIndex>=modules.size()-1)  {
							category.moduleIndex = 0;
							animModuleIndex=0;
						}
						else category.moduleIndex++;
					}
				} else {
					if (currentCategoryIndex>=Mod.Category.values().length-1) {
						currentCategoryIndex=0;
						animCategoryIndex=0;
					}
					else currentCategoryIndex++;
				}
			}
			
			if (key==GLFW.GLFW_KEY_RIGHT) {
				if (moduleExpanded) {
					if (settingExpanded) {
						if (modules.get(category.moduleIndex).getSetting().get(currentSettingIndex).focused) {
							Setting setting = modules.get(category.moduleIndex).getSetting().get(currentSettingIndex);
							if (setting instanceof BooleanSetting) {
								((BooleanSetting) setting).setEnabled(!((BooleanSetting) setting).isEnabled());
							} else if (setting instanceof ModeSetting) {
								((ModeSetting) setting).cycle();
							} else if (setting instanceof NumberSetting) {
								((NumberSetting) setting).increment(true);
							} else if (setting instanceof KeybindSetting) {
							} else if (setting instanceof ColorSetting) {
							}
						}
					} else if (!settingExpanded) {
						settingExpanded=true;
					}
				} else {
					moduleExpandAnim+=5;
					moduleExpanded=true;
				}
			}
			if (key==GLFW.GLFW_KEY_LEFT) {
				if (settingExpanded) {
					if (modules.get(category.moduleIndex).getSetting().get(currentSettingIndex).focused) {
						Setting setting = modules.get(category.moduleIndex).getSetting().get(currentSettingIndex);
						if (setting instanceof BooleanSetting) {
							((BooleanSetting) setting).setEnabled(!((BooleanSetting) setting).isEnabled());
						} else if (setting instanceof ModeSetting) {
							((ModeSetting) setting).cycleBack();
						} else if (setting instanceof NumberSetting) {
							((NumberSetting) setting).increment(false);
						} else if (setting instanceof KeybindSetting) {
						} else if (setting instanceof ColorSetting) {
						}
					} else {
						settingExpanded=false;
						currentSettingIndex=0;
						animSettingIndex=0;
					}
				} else {
					moduleExpandAnim-=5;
					moduleExpanded=false;
				}
			}
			
			if (key==GLFW.GLFW_KEY_ENTER) {
				if (moduleExpanded) {
					if (settingExpanded) {
						modules.get(category.moduleIndex).getSetting().get(currentSettingIndex).focused = !modules.get(category.moduleIndex).getSetting().get(currentSettingIndex).focused;
					} else modules.get(category.moduleIndex).toggle();
				}
			}
		}
	}
	
	public static void renderArrayList(MatrixStack matrices) {
		xOffset = arrayModule.offsetX1.getValueInt();
		yOffset = arrayModule.offsetY1.getValueInt();
		
		int index = 0;
		int sWidth = mc.getWindow().getScaledWidth();
//		int sHeight = mc.getWindow().getScaledHeight();
		
		List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();
		if (arrayModule.SortY.is("Normal")) enabled.sort(Comparator.comparingInt(m -> (int)textRend.getStringWidth(((Mod)m).getDisplayName())).reversed());
		else if (arrayModule.SortY.is("Reversed")) enabled.sort(Comparator.comparingInt(m -> (int)textRend.getStringWidth(((Mod)m).getDisplayName())));
		else enabled.sort(Comparator.comparingInt(m -> (int)textRend.getStringWidth(((Mod)m).getDisplayName())).reversed());

        RenderSystem.setShader(BLUR_SHADER::getProgram);
        RenderSystem.beginInitialization();
		for (Mod mod : enabled) {
			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
			int fHeight = (int) textRend.getFontHeight();

			int fromX = xOffset+(sWidth-4) - fWidth-2   +1;
			int fromY = yOffset+(fHeight*index)-1;
			int toX = xOffset+(sWidth-2)+1;
			int toY = yOffset+(fHeight*index)+fHeight-1;
			
			int fromX2=fromX + (arrayModule.SortX.is("Left") ? (fWidth-100) : 0);
			int fromY2=fromY+1;
			int toX2=toX - (arrayModule.SortX.is("Left") ? (100-fWidth) : 0);
			int toY2=toY;
			
			
			if (mod.isEnabled()) {
				if (arrayModule.glow.isEnabled()) {
					DrawableHelper.fill(matrices, fromX2, fromY2-1, toX2, toY2, 0xffff2222);
//					RenderUtils.renderRoundedShadow(matrices, new Color(arrayModule.glowcolor.getColor().getRed(), arrayModule.glowcolor.getColor().getGreen(), arrayModule.glowcolor.getColor().getBlue(), 95), 
//							fromX2, fromY2, toX2, toY2, 1, 500, 4);
//					RenderUtils.renderRoundedQuad(matrices, new Color(arrayModule.glowcolor.getColor().getRed(), arrayModule.glowcolor.getColor().getGreen(), arrayModule.glowcolor.getColor().getBlue(), 95), 
//							fromX2, fromY2, toX2, toY2, 1, 500);
				}
				index++;
			}
		}
		index=0;
		
		for (Mod mod : enabled) {
			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
			int fHeight = (int) textRend.getFontHeight();

			int fromX = xOffset+(sWidth-4) - fWidth-2   +1;
			int fromY = yOffset+(fHeight*index)-1;
			int toX = xOffset+(sWidth-2);
			int toY = yOffset+(fHeight*index)+fHeight-1;
			
			int lastWidth;
			
			if (index-1 >= 0) lastWidth = textRend.getStringWidth(enabled.get(index-1).getDisplayName());
			else lastWidth = sWidth;
			
			int fromX2 = fromX + (arrayModule.SortX.is("Left") ? (fWidth-100) : 0);
			float fromY2 = fromY-0.8f;
			int toX2=toX - (arrayModule.SortX.is("Left") ? (100-fWidth) : 0);
			int toY2=toY;
			
			Color arrayColor = new Color(255,0,0);
			Color outlineColor = new Color(255,255,255);
			if (mod.isEnabled()) {
				if (arrayModule.background.isEnabled())
					DrawableHelper.fill(matrices, fromX2, fromY, toX2, toY2, 0x90000000);
				
				switch (arrayModule.mode.getSelected()) {
					case "Pulse": 
						arrayColor = ColorUtils.mixColorsAnimated(index, -1, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor(), ModuleManager.INSTANCE.getModule(ClickGuiMod.class).secondaryColor.getColor());
						outlineColor = ColorUtils.mixColorsAnimated(index, -1, ModuleManager.INSTANCE.getModule(ClickGuiMod.class).primaryColor.getColor(), ModuleManager.INSTANCE.getModule(ClickGuiMod.class).secondaryColor.getColor());
						break;
					case "Cute": 
						arrayColor = ColorUtils.getCuteColor(index);
						outlineColor = ColorUtils.getCuteColor(index);
						break;
					case "Simple":
						arrayColor = ClickGuiMod.primaryColor.getColor();
						outlineColor = ClickGuiMod.primaryColor.getColor();
						break;
					default: 
						arrayColor = new Color(255,255,255);
						outlineColor = new Color(255,255,255);
						break;
				}
				
				textRend.drawStringWithShadow(matrices, mod.getDisplayName(), fromX2, fromY2, arrayColor.getRGB(), 1);
				
				if (arrayModule.outline.isEnabled()) {
					DrawableHelper.fill(matrices, fromX2, (int)fromY2, fromX2-1, toY2,  outlineColor.getRGB());
					DrawableHelper.fill(matrices, toX2+1, (int)fromY2+1, toX2, toY2,  outlineColor.getRGB());
					if (index == enabled.size()-1) {
						DrawableHelper.fill(matrices, fromX2-1, toY2, toX2+1, toY2+1,  outlineColor.getRGB()); //lines from the roots
					} if (index == enabled.size()-enabled.size()) {
						DrawableHelper.fill(matrices, fromX2-1, (int)fromY2, toX2+1, (int)fromY2+1,  outlineColor.getRGB()); //lines from the skies
					} else {
						if (arrayModule.SortX.is("Left"))
							DrawableHelper.fill(matrices, fromX2+lastWidth+ (arrayModule.SortY.is("Normal")?4:3), (int)fromY2+1, fWidth+fromX2+4, (int)fromY2+2, outlineColor.getRGB()); //lines from the lands between
						else
							DrawableHelper.fill(matrices, fromX2, (int)fromY2, toX2-lastWidth-4, (int)fromY2+1, outlineColor.getRGB()); //lines from the lands between
					}
				}
				index++;
			}
		}
	}

	
	
	static LivingEntity target = null;
	static int renderWith, healthAnimation=0;
	static boolean shouldRender=false;
	@SuppressWarnings("static-access")
	public static void renderTargetHud(MatrixStack matrices) {
		HitResult hit = mc.crosshairTarget;
		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		if (mc.player != null) {
			if (Killaura.target == null || ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled()==false) {
				if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
				    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
				        target = player;
				    }
				} else if (target == null) return;
			} else if (Killaura.target != null) {
				target = Killaura.target;
			}
			
			int maxDistance = 24;
			if (!(target == null)) {
				if (target.isDead() || mc.player.squaredDistanceTo(target) > maxDistance) {
					target = null;
				}
			}
			
			int offsetX = 30;
			int offsetY = 15;
			int fromX = 5+sWidth/2;
			int fromY = 5+sHeight/2;
			int toX = 20+sWidth/2+95;
			int toY = 15+sHeight/2+40;
			
			if (target!=null) {
				shouldRender=true;
			} else if (target==null) {
				shouldRender=false;
				healthAnimation=84;
			}
			
			if (shouldRender) {
				if (target!=null) renderWith=(int) (((target.getHealth()-0)/(target.getMaxHealth()-1)*100)/1.26);			
				else if (target==null) renderWith=84;
				
				if (healthAnimation<renderWith) healthAnimation++;
				if (healthAnimation>renderWith) healthAnimation--;
				
				RenderUtils.renderRoundedShadow(matrices, new Color(0, 0, 0, 90), fromX+offsetX, fromY+3+offsetY, toX+1+offsetX, toY+offsetY, ModuleManager.INSTANCE.getModule(TargetHud.class).round.getValue(), 50, ModuleManager.INSTANCE.getModule(TargetHud.class).shadow.getValue());
				RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 90), fromX+offsetX, fromY+3+offsetY, toX+1+offsetX, toY+offsetY, ModuleManager.INSTANCE.getModule(TargetHud.class).round.getValue(), 50);
				RenderUtils.startScissor(fromX+offsetX, fromY+3+offsetY, 25, 47);
				RenderUtils.drawEntity(fromX+13+offsetX, toY-4+offsetY, 20, 0, 180, target);
				RenderUtils.endScissor();
				DrawableHelper.fill(matrices, toX-2+offsetX, fromY+45+offsetY, fromX+25+offsetX, toY-3+offsetY, new Color(0,0,0,90).getRGB());
				DrawableHelper.fill(matrices, fromX+25+offsetX, fromY+45+offsetY, (int) (fromX+25+offsetX) + healthAnimation, toY-3+offsetY, ColorUtils.mixColorsAnimated(1, 1, ClickGuiMod.primaryColor.getColor(), ClickGuiMod.secondaryColor.getColor()).getRGB());
				textRend.drawString(matrices, (target instanceof PlayerEntity ? target.getName().getString().replaceAll(ColorUtils.colorChar, "&") : target.getDisplayName().getString()), (int) fromX+25+offsetX, (int) 10+sHeight/2-2+offsetY, ClickGuiMod.primaryColor.getColor().getRGB(), 1);
				textRend.drawString(matrices, String.format("%.1f", target.getHealth()) + " | " + (target.isOnGround() ? "OnGround" : "InAir") , fromX+25+offsetX, fromY+15+offsetY, -1, 1);
				if (target instanceof PlayerEntity) textRend.drawString(matrices, "Ping " + getPing((PlayerEntity) target) + "ms", fromX+25+offsetX, fromY+27+offsetY, -1, 1);
			}
		}
	}
	
    public static int getPing(PlayerEntity player) {
        if (mc.getNetworkHandler() == null) return 0;

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }

    static BoundedAnimation SlidIn = new BoundedAnimation(0f, 120f, 100f, false, Easing.LINEAR);
	public static void Notifs(MatrixStack matrices) {

		boolean anim=false;
		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		ArrayList<Notification> notifications;
		NotificationUtil.update();
		TimerUtil afterTimer = new TimerUtil();
		notifications = NotificationUtil.get_notifications();
		
		int renderY = sHeight-55;    
		for (Notification n : notifications) {
			if (System.currentTimeMillis()-n.getTimeCreated()>1 && System.currentTimeMillis()-n.getTimeCreated()<2) anim=false; 
			else if (System.currentTimeMillis()-n.getTimeCreated()<=1800) anim=true; 
			

			SlidIn.setState(anim);
			
			RenderUtils.renderRoundedQuad(matrices, new Color(0,0,0,180), sWidth-SlidIn.getAnimationValue(), renderY+22, sWidth+50, renderY+38, 3, 50);
			textRend.drawString(matrices, n.getMessage(), sWidth+2-SlidIn.getAnimationValue(), renderY + 22, -1, 0.85f);
			RenderUtils.startScissor((int) (sWidth-SlidIn.getAnimationValue()), renderY+22, sWidth+50, renderY+38);
			DrawableHelper.fill(matrices, (int) (sWidth-(afterTimer.lastMS - n.getTimeCreated()) / animation(0, 125, 0.121,0)), renderY + 34, sWidth, renderY+36, ColorUtils.mixColorsAnimated(1, 1, ClickGuiMod.primaryColor.getColor(), ClickGuiMod.secondaryColor.getColor()).getRGB());
			RenderUtils.endScissor();
			renderY -= 18;
		}
	}
	
	private static double animation(double current, double end, double factor, double start) {
		double progress = (end - current) * factor;
		if (progress > 0) {
			progress = Math.max(start, progress);
			progress = Math.min(end - current, progress);
		} else if (progress < 0) {
			progress = Math.min(-start, progress);
			progress = Math.max(end - current, progress);
		}
		return current + progress;
	}
}
