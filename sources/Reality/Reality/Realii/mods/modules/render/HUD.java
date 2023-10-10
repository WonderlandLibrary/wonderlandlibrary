
package Reality.Realii.mods.modules.render;


import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.shader.Framebuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.world.EventPostUpdate;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.font.CFontRenderer;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;


import Reality.Realii.utils.math.AnimationUtils;
import Reality.Realii.utils.render.RenderUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import Reality.Realii.Client;
import Reality.Realii.guis.font.CFontRenderer;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.render.HUD;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.render.RenderUtil;
import net.minecraft.client.gui.FontRenderer;

import java.awt.*;

import org.lwjgl.opengl.GL11;


public class HUD
        extends Module {
    public TabUI tabui;
    public static Option<Boolean> noRender = new Option<Boolean>("NoRender", "NoRender", true);
    public static Mode USerinfp = new Mode("ShowUserInfo", "ShowUserInfo", new String[]{"True", "False"}, "True");
    public static Option<Boolean> notification = new Option<Boolean>("Notification", "Notification", true);
    public static Mode logoMode = new Mode("LogoMode", "LogoMode", new String[]{"Logo", "Text","Rise","Fulllogo","ImageLogo","ImageLogoGlow","New"}, "Logo");
    public static Mode Color23 = new Mode("ColorMode", "ColorMode", new String[]{"Client","Rainbow"}, "Client");
    public static Mode fontvchanger12 = new Mode("WaterMarkFont", "WaterMarkFont", new String[]{"Vannila", "Sigma","Arial","Thick","Jello","Product"}, "Vannila");
    public Mode mod = new Mode("Mode", "Mode", new String[]{"Reality"}, "Reality");

    private  TimerUtil timeHelper;
    private AnimationUtils animationUtils = new AnimationUtils();

    public HUD() {
        super("HUD", ModuleType.Render);
        this.setEnabled(true);
        this.addValues(this.notification, this.mod, noRender, logoMode, fontvchanger12, USerinfp, Color23);
    }

    int rainbowTick = 0;
   
   
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

    float x;
    
    
  
    public String getServerIP(EntityPlayerMP player) {
        String address = player.getPlayerIP();
       
        return address + ":";
    }

    
    @EventHandler
    public void renderHud(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(mc);
        
        if (((boolean) notification.getValue())) {
            NotificationsManager.renderNotifications();
            NotificationsManager.update();
        }
        float x1 = sr.getScaledWidth(), y1 = 0;
        float arrayListY = y1;
       
      //  BloomUtil.renderBlur(null, rainbowTick, rainbowTick, rainbowTick, enabledOnStartup, null);
    // List<Runnable> NORMAL_BLUR_RUNNABLES = new ArrayList<>();
       // GaussianBlur.renderBlur(8, NORMAL_BLUR_RUNNABLES);
     //   NORMAL_BLUR_RUNNABLES.clear();
     //  NORMAL_BLUR_RUNNABLES.add(() -> RenderUtil.drawRect69(300, 5, 60, 13, ColorUtility.rgba(21, 21, 21, 255)));
      
    
        if (USerinfp.getModeAsString().equals("True")) {
        	 if (Color23.getModeAsString().equals("Client")) {
        		   int i = 0;
        		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
        		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
          	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
          	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
       	  FontLoaders.arial18.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " Client Nyghtfull (0069) {Beta DevBuilt} ", 3, 530, c.getRGB());
        	 }
        	 
        	 if (Color23.getModeAsString().equals("Rainbow")) {
        	
              	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
            	  FontLoaders.arial18.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " Client Nyghtfull (0069) {Beta DevBuilt} ", 3, 530, c.getRGB());
             	 }
        }
        
        if (USerinfp.getModeAsString().equals("False")) {
         
        }
        
        if (logoMode.getModeAsString().equals("New")) {
         
        	Color c2 = new Color(0,0,0,100);
        	 Color c3 = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
        	   int i = 0;
      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
        		 double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                 double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                 double moveSpeed = Math.sqrt(xDist * xDist + zDist * zDist) * 20;
                 String serverIp;
            	 serverIp = mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "Singleplayer";
        	  Color color3 = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
        	//RenderUtil.drawRect(4, 4, FontLoaders.product18.getStringWidth("Reality Client" + " | "  + mc.thePlayer.getName() +  " | " + serverIp  +  " | " + Math.round(moveSpeed)) + 10, 20, c2.getRGB());
            RenderUtil.drawBorderedRect(4, 4, FontLoaders.product18.getStringWidth("Reality Client" + " | "  + mc.thePlayer.getName() +  " | " + serverIp  +  " | " + Math.round(moveSpeed)) + 10, 20,3, color3.getRGB(), c2.getRGB());
        	
        	 if (Color23.getModeAsString().equals("Client")) {
        		 
        	//RenderUtil.drawRect(4, 18, FontLoaders.product18.getStringWidth("Reality Client" + " | "  + mc.thePlayer.getName() +  " | " + serverIp  +  " | " + Math.round(moveSpeed)) + 10, 20, color3.getRGB());
        	 }
        	 
        	 if (Color23.getModeAsString().equals("Rainbow")) {
             	RenderUtil.drawRect(4, 4, 278, 6, c3.getRGB());
             	 }
        	
        	if (fontvchanger12.getValue().equals("Product")) {
       		
       			 
           	Color c = new Color(255,255,255);
               FontLoaders.product18.drawStringWithShadow("Reality Client" + " | "  + mc.thePlayer.getName() +  " | " + serverIp  +  " | " + Math.round(moveSpeed) , 6, 8, c.getRGB());
       		 
       		 
               TabUI.height = 21;
           	}
        }
        
        if (logoMode.getModeAsString().equals("Rise")) {
            if (Color23.getModeAsString().equals("Client")) {
        	Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
            FontLoaders.Codec_Cold35.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  + Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + "", 2, 5, c.getRGB());
           // FontLoaders.Codec_Cold19.drawStringWithShadow("Totally Rise Client", 66, 15, c.getRGB());
            FontLoaders.Codec_Cold19.drawStringWithShadow("Full Verus Disabler edition!!", 66, 15, c.getRGB());
            // FontLoaders.Codec_Cold19.drawStringWithShadow("Pika Arilines" + getServerIP((EntityPlayerMP)player), 66, 15, c.getRGB());
            FontLoaders.Codec_Cold19.drawStringWithShadow("Beta", 66, 6, c.getRGB());
            
        		 }
            
            
            if (Color23.getModeAsString().equals("Rainbow")) {
            	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
            	 FontLoaders.Codec_Cold35.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  + Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + "" , 2, 5, c.getRGB());
            	 FontLoaders.Codec_Cold19.drawStringWithShadow("Full Verus Disabler edition!!", 66, 15, c.getRGB());
                 FontLoaders.Codec_Cold19.drawStringWithShadow("Beta", 66, 6, c.getRGB());
            }
            TabUI.height = 21;
        	}
        
      
        
        if (logoMode.getModeAsString().equals("Logo")) {
            Color rainbow = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0)) % 1.0f, 0.5f, 1.0f));

            if (!mc.gameSettings.showDebugInfo) {
                RenderUtil.drawCustomImage(3, 2, 200, 41, new ResourceLocation("client/onletterlogo.png"), new Color(255, 255, 255).getRGB());
                TabUI.height = 42;
            }
            
           
        } else if (logoMode.getModeAsString().equals("Text")) {
            if (!mc.gameSettings.showDebugInfo) {
            	
            	if (fontvchanger12.getValue().equals("Arial")) {
                if (Color23.getModeAsString().equals("Client")) {
            	Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
                FontLoaders.waterarial18.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  + Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " " + ChatFormatting.GRAY, 5, 5, c.getRGB());
            		 }
                
                if (Color23.getModeAsString().equals("Rainbow")) {
                	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                	 FontLoaders.waterarial18.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  + Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " " + ChatFormatting.GRAY, 5, 5, c.getRGB());
                }
                TabUI.height = 21;
            	}
            	
            	if (fontvchanger12.getValue().equals("Product")) {
            		 if (Color23.getModeAsString().equals("Client")) {
            			 
            			   int i = 0;
      		      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
      		      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
      		        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
      		        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
      		        	// FontLoaders.product30.drawStringWithShadow("Mystique", 5, 5, c.getRGB());
                       //  FontLoaders.product18.drawStringWithShadow("Beta", 70, 4, c.getRGB());
      		        	  
                    FontLoaders.product23.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  + Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " " + ChatFormatting.GRAY, 5, 5, c.getRGB());
               //     FontLoaders.product23.drawStringWithShadow("Dev 1.3", 55, 4, c.getRGB());
                        Color gray = new Color(150,150,150);
                         FontLoaders.product18.drawStringWithShadow("h", 44, 7, gray.getRGB());
                         FontLoaders.product18.drawStringWithShadow("a", 49, 7, gray.getRGB());
                         FontLoaders.product18.drawStringWithShadow("c", 54, 7, gray.getRGB());
                         FontLoaders.product18.drawStringWithShadow("k", 59, 7, gray.getRGB());
                         FontLoaders.product18.drawStringWithShadow("e", 64, 7, gray.getRGB());
                         FontLoaders.product18.drawStringWithShadow("d", 69, 7, gray.getRGB());
                         FontLoaders.product18.drawStringWithShadow("", 69, 7, gray.getRGB());



                      //   FontLoaders.product18.drawStringWithShadow("c", 50, 7, c.getRGB());
                     //    FontLoaders.product18.drawStringWithShadow("k", 52, 7, c.getRGB());
                     //    FontLoaders.product18.drawStringWithShadow("e", 54, 7, c.getRGB());
                   //      FontLoaders.product18.drawStringWithShadow("d", 56, 7, c.getRGB());

                    double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    double moveSpeed = Math.sqrt(xDist * xDist + zDist * zDist) * 20;
                    
              	Color c23 = new Color(255,255,255);
                  FontLoaders.Bpsarial18.drawStringWithShadow("BPS\247r " + Math.round(moveSpeed) , 11, 185, c23.getRGB());
            		 }
            		 if (Color23.getModeAsString().equals("Rainbow")) {
            			 Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                         FontLoaders.product30.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  + Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " " + ChatFormatting.GRAY, 5, 5, c.getRGB());
                 		 }
                    TabUI.height = 21;
                	}
            	
            	else if (this.fontvchanger12.getValue().equals("Sigma")) {
            		 if (Color23.getModeAsString().equals("Client")) {
                 	Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
                 	  FontLoaders.roboto16.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " ", 5, 5, c.getRGB());
            		 }
            		 
            		 if (Color23.getModeAsString().equals("Rainbow")) {
            			 Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                      	  FontLoaders.roboto16.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " ", 5, 5, c.getRGB());
                 		 }
                     TabUI.height = 21;
                     
             		  }
            	
            	else if (this.fontvchanger12.getValue().equals("Jello")) {
            		 if (Color23.getModeAsString().equals("Client")) {
                 	Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue(), 200);
                 	

                 	 FontLoaders.JelloFont49.drawString(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " ", 5, 5, c.getRGB());
                 	 FontLoaders.JelloFont30.drawString("Jello", 5, 28, c.getRGB());
            		 }
            		 
            		 if (Color23.getModeAsString().equals("Rainbow")) {
            			 Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                      	

                      	 FontLoaders.JelloFont49.drawString(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " ", 5, 5, c.getRGB());
                      	 FontLoaders.JelloFont30.drawString("Jello", 5, 28, c.getRGB());
                 		 }
                     TabUI.height = 21;
                     
             		  }
            	
        		else if (this.fontvchanger12.getValue().equals("Vannila")) {
        			FontRenderer fontv2 = Minecraft.getMinecraft().fontRendererObj;
        			 if (Color23.getModeAsString().equals("Client")) {
        				   int i = 0;
        		      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
        		      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
        		        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
        		        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i * 20), colors.getFirst(), colors.getSecond(), false);
                  	
        		        	  	
        		        		fontv2.drawStringWithShadow("R" + ChatFormatting.WHITE + "eality", 5, 5, c.getRGB());
        		        		//fontv2.drawStringWithShadow("  eality", 5, 5, new Color(255,255,255).getRGB());
        		        		
                  	//fontv2.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " (ducadamul is black)", 5, 5, c.getRGB());
                      TabUI.height = 21;
        			 }
        			 
        			 if (Color23.getModeAsString().equals("Rainbow")) {
        				 Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                       	
                       	

                       	fontv2.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " ", 5, 5, c.getRGB());
                           TabUI.height = 21;
             			 }
        		 }
            	
            	
            	else if (this.fontvchanger12.getValue().equals("Thick")) {

                 	Color c = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue());
                 	

                 	  FontLoaders.arial16B.drawStringWithShadow(Client.CLIENT_NAME.substring(0, 1)  +  Client.CLIENT_NAME.substring(1, Client.CLIENT_NAME.length()) + " ", 5, 5, c.getRGB());

                     TabUI.height = 21;
             		  }
            	
            	
            }
            
            } else if (logoMode.getModeAsString().equals("Fulllogo")) {
                Color rainbow = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0)) % 1.0f, 0.5f, 1.0f));

                if (!mc.gameSettings.showDebugInfo) {
                    RenderUtil.drawCustomImageAlpha(1, 3, 80, 20, new ResourceLocation("client/hudlogo.png"), new Color(255, 255, 255).getRGB(), rainbow.getBlue());
                    TabUI.height = 26;
                }
                      
                
    } else if (logoMode.getModeAsString().equals("ImageLogo")) {
        Color rainbow = new Color(Color.HSBtoRGB((float) ((double) this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double) rainbowTick / 50.0)) % 1.0f, 0.5f, 1.0f));

        if (!mc.gameSettings.showDebugInfo) {
            RenderUtil.drawCustomImageAlpha(1, 3, 80, 80, new ResourceLocation("client/Coollogo.png"), new Color(255, 255, 255).getRGB(), rainbow.getBlue());
            TabUI.height = 26;
        }
            
 else if (logoMode.getModeAsString().equals("ImageLogoGlow")) {
   

    if (!mc.gameSettings.showDebugInfo) {
        RenderUtil.drawCustomImageAlpha(1, 3, 80, 80, new ResourceLocation("client/ColllogoGlow.png"), new Color(255, 255, 255).getRGB(), rainbow.getBlue());
        TabUI.height = 26;
    }
          
    }
            
        
        if (mod.getValue().equals("Reality")) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            double moveSpeed = Math.sqrt(xDist * xDist + zDist * zDist) * 20;
            String text = (EnumChatFormatting.GRAY) + "FPS" + (Object) ((Object) EnumChatFormatting.WHITE) + ": " + Minecraft.debugFPS + "  " + Math.round(moveSpeed) + " \2477bps\247r";
            
            Client.fontLoaders.msFont18.drawStringWithShadow(text, 4.0F, new ScaledResolution(mc).getScaledHeight() - 10, new Color(11, 12, 17).getRGB());
            drawPotionStatus(sr);
        }
    }
    }

    @EventHandler
    public void onUpdate(EventPostUpdate e) {
        if (rainbowTick++ > 50) {
            rainbowTick = 0;
        }


    }

    private void drawPotionStatus(ScaledResolution sr) {
        CFontRenderer font = FontLoaders.arial16;
        int y = -5;
        for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
            int ychat;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String PType = I18n.format(potion.getName(), new Object[0]);
            switch (effect.getAmplifier()) {
                case 1: {
                    PType = String.valueOf(PType) + " II";
                    break;
                }
                case 2: {
                    PType = String.valueOf(PType) + " III";
                    break;
                }
                case 3: {
                    PType = String.valueOf(PType) + " IV";
                    break;
                }
            }
            if (effect.getDuration() < 600 && effect.getDuration() > 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = String.valueOf(PType) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = String.valueOf(PType) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
            }
            font.drawStringWithShadow(PType, sr.getScaledWidth() - font.getStringWidth(PType) - 2, sr.getScaledHeight() - font.getHeight() + y, potion.getLiquidColor());
            y -= 12;
        }
    }

}

