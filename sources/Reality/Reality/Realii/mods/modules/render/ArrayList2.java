package Reality.Realii.mods.modules.render;



import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.opengl.GL11;

import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import Reality.Realii.Client;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.font.CFontRenderer;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.AnimationUtils;
import Reality.Realii.utils.render.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;

import java.awt.*;

public class ArrayList2	
	extends Module {
	 
	public static Mode fontvchanger = new Mode("ArrayListFont", "ArrayListFont", new String[]{"Vannila","New", "Sigma","Arial","Swanses","Thick","Product","Cold"}, "Vannila");
	public static Mode outlinemode = new Mode("OutlineMode", "OutlineMode", new String[]{"Normal", "Normal2","Up","Flux"}, "Normal");
	public static Mode bkcolor = new Mode("BkColor", "BkColor", new String[]{"Normal","Rainbow", "Client","Nigger"}, "Client");
	public static Mode SufixMode = new Mode("SufixMode", "SufixMode", new String[]{"Normal", "{}","()"}, "Client");
	public static Mode SufixColor = new Mode("SufixColor", "SufixColor", new String[]{"White", "Gray","Black"}, "White");
	public static Numbers<Number>  capacity = new Numbers<>("BkCapacity", 45f, 5f, 255f, 100f);
	public static Numbers<Number>  RainbowSpeed = new Numbers<>("RainbowSpeed", 45f, 5f, 255f, 100f);
	public static Option<Boolean> Outline = new Option<Boolean>("Outline", "Outline", true);
    public static Option<Boolean> backround = new Option<Boolean>("Backround", "Backround", true);
    public static Option<Boolean> NoRender = new Option<Boolean>("NoRender", "NoRender", true);
    public static Mode colorMode = new Mode("ArrayListColor", "ArrayListColor", new String[]{"ColoredRainbow", "Color", "Rainbow"}, "ColoredRainbow");
    public static Mode Sufix = new Mode("Sufix", "Sufix", new String[]{"On", "Off"}, "On");
   
	
	public ArrayList2() {
        super("ArrayList", ModuleType.Render);
        this.addValues(bkcolor,fontvchanger,outlinemode, capacity, Outline,NoRender,  backround, colorMode, SufixMode, SufixColor, RainbowSpeed, Sufix);
    }
	
	
//ArrayList	
	private AnimationUtils animationUtils = new AnimationUtils();
	 private static ArrayList2 arrayList = new ArrayList2();
    private int rainbowTick;
    private int rainbowTick2;
    public  CFontRenderer font;
    public  FontRenderer fontVn;
    private TimerUtil timer = new TimerUtil();
   
    @EventHandler
    public void renderHud(EventRender2D event) {
    	//if (!mc.gameSettings.showDebugInfo) {
            arrayList.drawObject();
        //}
    }
    
   
    
    public void drawObject() {
    	 
    	   if (fontvchanger.getValue().equals("Vannila")) {
    		   fontVn = Minecraft.getMinecraft().fontRendererObj;
    	   }
    	   
    	   if (fontvchanger.getValue().equals("Sigma")) {
    		  font = FontLoaders.roboto16;
    		   
    	   }
    	   if (fontvchanger.getValue().equals("Arial")) {
    		   font = FontLoaders.arial18;
    		   
    	   }
    	   
    	   if (fontvchanger.getValue().equals("Product")) {
    		   font = FontLoaders.product18;
    	   }
    	   if (fontvchanger.getValue().equals("New")) {
    		   font = FontLoaders.New17;
    		   
    	   }
    	   if (fontvchanger.getValue().equals("Cold")) {
    		   font = FontLoaders.Codec_Cold18;
    	   }
    	   if (fontvchanger.getValue().equals("Thick")) {
    		   font = FontLoaders.arial16B;
    	   }
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float x1 = sr.getScaledWidth(), y1 = 0;
 
       
        
        
     
        		


				
            if (fontvchanger.getValue().equals("Vannila")) {
        	
            	
                    ModuleManager.modules.sort(((o1, o2) -> fontVn.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - fontVn.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix()))));
                     
            } else {
        
        	
            	
                     ModuleManager.modules.sort(((o1, o2) -> font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s %s", o2.getName(), o2.getSuffix())) - font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s %s", o1.getName(), o1.getSuffix()))));
            }
            
             
             
            
             
             
             
        

        rainbowTick = 0;
        rainbowTick2 = 0;

        java.util.ArrayList<Module> mods = new java.util.ArrayList<>();
        for (Module m : ModuleManager.modules) {
        	
            if (m.isEnabled()) {
            	   if(NoRender.getValue()) {
            	  if(m.getType() == ModuleType.Render)
                 continue;
            	   }
            	  
                  mods.add(m);
               
            } else {
                m.animX = x1;
            }
        }

        float ys = y1;
        if (timer.delay(10)) {
            for (Module mod : mods) {
                mod.animY = mod.animationUtils.animate(ys, mod.animY, 0.1f);
                if (fontvchanger.getValue().equals("Vannila")) {
                	
                		
                	
                      	 mod.animX = mod.animationUtils2.animate(x1 - (fontVn.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - 4), mod.animX, 0.1f);
                    	 
                } else {
                
                
                      	 mod.animX = mod.animationUtils2.animate(x1 - (font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - 4), mod.animX, 0.15f);
                    	 
                }
                
                
             
                ys += 12;
            }
            timer.reset();
        }
        
        float arrayListY = y1;
        int i = 0;
        for (Module mod : mods) {
        
            if (!mod.isEnabled())
                return;
            if (++rainbowTick2 > 50) {
                rainbowTick2 = 0;
            }
            Color arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
            if (colorMode.getValue().equals("Rainbow")) {
                arrayRainbow2 = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1, 1f));
            } else if (colorMode.getValue().equals("ColoredRainbow")) {
                Color temp = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 40.0 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 10.0 * 1.6)) % 1.0f, 0.5f, 1));
                arrayRainbow2 = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), temp.getRed());
            } else if (colorMode.getValue().equals("Color")) {
         	   Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
         	  Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
        	  arrayRainbow2 = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
             
            }
            
          
            
            
            
            if(backround.getValue()) {
            	if (bkcolor.getValue().equals("Client")) {
            		 int i1 = 0;
		      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(),capacity.getValue().intValue()); 
		      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue(),capacity.getValue().intValue());
		        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
		        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i1 * 20), colors.getFirst(), colors.getSecond(), false);
          	
            		// arrayRainbow2 = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                    // RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, arrayRainbow2.getRGB());
            	
            	
                RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, c.getRGB());
                
            	}
            	if (bkcolor.getValue().equals("Rainbow")) {
            		
            		 arrayRainbow2 = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                     RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, arrayRainbow2.getRGB());
            	
                
            	} 
            	 
            	else if (bkcolor.getValue().equals("Normal")) {
            		Color c2 = new Color(50,50,50,capacity.getValue().intValue());
            		RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, c2.getRGB());
            		
            	} else if (bkcolor.getValue().equals("Nigger")) {
            		Color c3 = new Color(10,10,10,capacity.getValue().intValue());
            	RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), (x1), (int) mod.animY + 12, c3.getRGB());
            	//	 BlurUtil.blur((int) mod.animX - 10, ((float) mod.animY - 120), (x1), (int) mod.animY + 12);
            		
            	}
            	
                }
            
            if(Outline.getValue() && !outlinemode.getValue().equals("Up") && !outlinemode.getValue().equals("Flux")) {
            	
                if (i + 1 <= mods.size() - 1) {
                    Module m2 = mods.get(i + 1);
                    if (fontvchanger.getValue().equals("Vannila")) {
                    	
                    	
                        	RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, (int) mod.animX - 9 + fontVn.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - fontVn.getStringWidth(m2.getName() + (m2.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + m2.getSuffix()), (int) mod.animY + 12, arrayRainbow2.getRGB());
                        
                    } else {
                    	RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, (int) mod.animX - 9 + font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - font.getStringWidth(m2.getName() + (m2.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + m2.getSuffix()), (int) mod.animY + 12, arrayRainbow2.getRGB());
                    }
                
                   
                    	
                   
                
                        
                    
                    
                 
                    
                    if (outlinemode.getValue().equals("Normal2")) {
                    
                    
                    if (fontvchanger.getValue().equals("Vannila")) {
                    	
                    	
                    	RenderUtil.drawRect((int) mod.animX - 13, ((float) mod.animY) + 11, (int) mod.animX - 9 + fontVn.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - fontVn.getStringWidth(m2.getName() + (m2.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + m2.getSuffix()), (int) mod.animY + 14, arrayRainbow2.getRGB());
                    
                }  else {
                
               
                	RenderUtil.drawRect((int) mod.animX - 13, ((float) mod.animY) + 11, (int) mod.animX - 9 + font.getStringWidth(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix()) - font.getStringWidth(m2.getName() + (m2.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + m2.getSuffix()), (int) mod.animY + 14, arrayRainbow2.getRGB());
               
            
                    
                }
                }
                
                    
                
                    
                } else if (i == mods.size() - 1) {

                	 
                     if (outlinemode.getValue().equals("Normal")) {
                    	 RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY) + 11, x1, (int) mod.animY + 12, arrayRainbow2.getRGB());
                     	
                     	
                     }
                     
                     if (outlinemode.getValue().equals("Normal2")) {
                    	 RenderUtil.drawRect((int) mod.animX - 13, ((float) mod.animY) + 15, x1, (int) mod.animY + 12, arrayRainbow2.getRGB());
                     	
                     	
                     }
                     
                     
                }
                
           	
             
           	
                if (outlinemode.getValue().equals("Normal")) {
                	RenderUtil.drawRect(mod.animX - 10, 1, 960, 0, arrayRainbow2.getRGB());
                	//flux
                	RenderUtil.drawRect((int) 960, ((float) mod.animY), ((int) 959), (int) mod.animY + 12, arrayRainbow2.getRGB());
                	RenderUtil.drawRect((int) mod.animX - 10, ((float) mod.animY), ((int) mod.animX - 9), (int) mod.animY + 11, arrayRainbow2.getRGB());
                	
                }
                
                
                if (outlinemode.getValue().equals("Normal2")) {
                	RenderUtil.drawRect((int) mod.animX - 13, ((float) mod.animY), ((int) mod.animX - 10), (int) mod.animY + 12, arrayRainbow2.getRGB());
                	
                }
                
               
            }
            if (outlinemode.getValue().equals("Up")) {
            	//up
            	
            RenderUtil.drawRect(mod.animX - 10, 1, 960, 0, arrayRainbow2.getRGB());
            	
            	
            }
            
            if (outlinemode.getValue().equals("Flux")) {
            	//flux
            	
            	RenderUtil.drawRect((int) 960, ((float) mod.animY), ((int) 958), (int) mod.animY + 12, arrayRainbow2.getRGB());
            	
            }
            
            
            
            
            if (fontvchanger.getValue().equals("Vannila")) {
            	if (SufixMode.getValue().equals("Normal")) {
                	
            		if (SufixColor.getValue().equals("White")) {
            			fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
            		}
            		if (SufixColor.getValue().equals("Black")) {
            			fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.BLACK + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
                		}
            		if (SufixColor.getValue().equals("Gray")) {
            			fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.GRAY + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
                		}
                    }
                    
                    if (SufixMode.getValue().equals("{}")) {
                    	
                        
                    	if (SufixColor.getValue().equals("Gray")) {
                    		fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " {" ) +   ChatFormatting.GRAY +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : "}")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("Black")) {
                    		fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " {" ) +   ChatFormatting.BLACK +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : "}")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("White")) {
                    		fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " {" ) +   ChatFormatting.WHITE +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : "}")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                    	}
                        }
                    if (SufixMode.getValue().equals("()")) {
                    	if (SufixColor.getValue().equals("Gray")) {
                    		fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " (" ) +   ChatFormatting.GRAY +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : ")")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("Black")) {
                    		fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " (" ) +   ChatFormatting.BLACK +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : ")")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("White")) {
                        fontVn.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " (" ) +   ChatFormatting.WHITE +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : ")")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                    	}
                        
                    	 
                        
                        }
            	
            
            
            arrayListY += 12f;
            i++;
            } else {
         
            	if (SufixMode.getValue().equals("Normal")) {
                	
            		if (SufixColor.getValue().equals("White")) {
                    font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.WHITE + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
            		}
            		if (SufixColor.getValue().equals("Black")) {
                        font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.BLACK + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
                		}
            		if (SufixColor.getValue().equals("Gray")) {
                        font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " ") + ChatFormatting.GRAY + mod.getSuffix(), mod.animX - 8, mod.animY + 3, arrayRainbow2.getRGB());
                		}
                    }
                    
                    if (SufixMode.getValue().equals("{}")) {
                    	
                        
                    	if (SufixColor.getValue().equals("Gray")) {
                            font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " {" ) +   ChatFormatting.GRAY +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : "}")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("Black")) {
                            font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " {" ) +   ChatFormatting.BLACK +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : "}")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("White")) {
                        font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " {" ) +   ChatFormatting.WHITE +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : "}")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                    	}
                        }
                    if (SufixMode.getValue().equals("()")) {
                    	if (SufixColor.getValue().equals("Gray")) {
                            font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " (" ) +   ChatFormatting.GRAY +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : ")")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("Black")) {
                            font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " (" ) +   ChatFormatting.BLACK +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : ")")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                        	}
                    	if (SufixColor.getValue().equals("White")) {
                        font.drawStringWithShadow(mod.getName() + (mod.getSuffix().isEmpty() ? "" : " (" ) +   ChatFormatting.WHITE +  mod.getSuffix() + (mod.getSuffix().isEmpty() ? "" : ")")   ,  mod.animX - 8, mod.animY + 3 , arrayRainbow2.getRGB());
                    	}
                        
                    	 
                        
                        }
            
            arrayListY += 12f;
            i++;
            }
            
         
            
        }
        if (rainbowTick++ > 50) {
            rainbowTick = 0;
        }
    }
 }


