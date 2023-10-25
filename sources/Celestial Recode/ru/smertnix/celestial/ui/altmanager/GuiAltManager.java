package ru.smertnix.celestial.ui.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.player.NameProtect;
import ru.smertnix.celestial.ui.altmanager.alt.Alt;
import ru.smertnix.celestial.ui.altmanager.alt.AltLoginThread;
import ru.smertnix.celestial.ui.altmanager.alt.AltManager;
import ru.smertnix.celestial.ui.altmanager.alt.Server;
import ru.smertnix.celestial.ui.altmanager.alt.ServerManager;
import ru.smertnix.celestial.ui.altmanager.api.AltService;
import ru.smertnix.celestial.ui.button.GuiMainMenuButton;
import ru.smertnix.celestial.ui.mainmenu.MainMenu;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import javafx.animation.Interpolator;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GuiAltManager extends GuiScreen {
    public static final AltService altService = new AltService();
    public Alt selectedAlt = null;
    public String status;
    private GuiMainMenuButton login;
    private GuiMainMenuButton remove;
    private AltLoginThread loginThread;
    private float offset;
    public static float anim;
    public static float o;
    private ResourceLocation resourceLocation;

    public GuiAltManager() {
        status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
    }

    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }

    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                break;
            case 1:
                (loginThread = new AltLoginThread(selectedAlt)).start();
                selectedAlt = null;
                break;
            case 2:
                if (loginThread != null) {
                    loginThread = null;
                }

                AltManager.registry.remove(selectedAlt);
                status = TextFormatting.GREEN + "Removed.";

                selectedAlt = null;
                break;
            case 3:
                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 5:
                String randomName = RandomStringUtils.randomAlphabetic(5).toLowerCase() + RandomStringUtils.randomNumeric(2);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                (loginThread = new AltLoginThread(new Alt(randomName, "", dateFormat.format(date)))).start();
                AltManager.registry.add(new Alt(randomName, "", true, dateFormat.format(date)));
                break;
            case 6:
            	for (Alt a : getAlts()) {
            		if (a.random) {
            			AltManager.registry.remove(a);
            		}
            	}
                break;
            case 7:
            	 mc.displayGuiScreen(new GuiAddServer(this));
                break;
            case 8:
                /*try {
                    AltManager.registry.clear();
                    Main.instance.fileManager.getFile(Alts.class).loadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                status = TextFormatting.RED + "Refreshed!";
                break;
            case 8931:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 4545:
                mc.displayGuiScreen(new GuiConnecting(this, mc, new ServerData(I18n.format("selectServer.defaultName"), "play.hypixel.net", false)));
                break;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        ScaledResolution sr = new ScaledResolution(this.mc);
    	RenderUtils.drawImage(new ResourceLocation("celestial/pesun3.jpg"), 0,0,sr.getScaledWidth() + 5, sr.getScaledHeight(), new Color(155,155,155));
    	RenderUtils.drawRect(0,0,sr.getScaledWidth(), 40, MainMenu.s ? new Color(0,0,0,30).getRGB() : new Color(0,0,0,100).getRGB());
    	
    	anim = (float) Interpolator.LINEAR.interpolate(anim, o, 0.5f);
    	this.offset = anim;
    	
        super.drawScreen(par1, par2, par3);
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.o += 26.0;
                if (this.o < 0.0) {
                    this.o = 0.0F;
                }
            } else if (wheel > 0) {
                this.o -= 26.0;
                if (this.o < 0.0) {
                    this.o = 0.0F;
                }
            }
        }
        String altName = this.mc.session.getUsername();
        
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        RenderUtils.scissorRect(0.0f, 33.0f, this.width, this.height);
        GL11.glEnable(3089);
        int y = 48;
        
        int number = 0;
        
        Iterator<Alt> e = this.getAlts().iterator();
        int y2 = 0;
        for (Server s : getServers()) {
           	String name = s.getName();
           	if (par1 > 296 - mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 > 128 + y2 && par1 < 296 + mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 < 131 + y2 + 10 ) {
           		name = TextFormatting.GOLD + name;
           	}
           	y2 += 12;
           }
        final int y3 = y2;
        
        if (MainMenu.s) {
        	 RenderUtils.drawShadow(10,1, () -> {
           		 RoundedUtil.drawRound(230, 50, 135, 142 + y3 - 60, 3, new Color(-1));
               });
               RenderUtils.drawBlur(10, () -> {
               	RoundedUtil.drawRound(230, 50, 135, 142 + y3 - 60, 3, new Color(-1));
               });
        } else {
        	RoundedUtil.drawRound(230, 50, 135, 142 + y3 - 60, 0, new Color(0,0,0,100));
        }
       int y4 = 0;
       for (Server s : getServers()) {
          	String name = s.getName();
          	if (par1 > 296 - mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 > 128 + y4 && par1 < 296 + mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 < 131 + y4 + 10 ) {
          		name = TextFormatting.GOLD + name;
          	}
          	mc.mntsb_17.drawCenteredStringWithShadow(name, 296, 131 + y4, -1);
          	y4 += 12;
          }
       
       int count = 0;
       for (Alt a : getAlts()) {
    	   count++;
       }
       
       
       mc.mntsb_18.drawCenteredStringWithShadow("Alts count: " + count, 263 + 33, 90, -1);
       
       
       mc.mntsb_30.drawStringWithShadow("Alts Info", 263, 60, -1);
       
       mc.mntsb_25.drawStringWithShadow("Quick Join", 260, 113, -1);
       mc.mntsb_18.drawCenteredStringWithShadow("Current: " + altName, 263 + 33, 80, -1);
       
        while (true) {
        	
        	
            if (!e.hasNext()) {
                GL11.glDisable(3089);
                GL11.glPopMatrix();
                if (this.selectedAlt == null) {
                    this.login.enabled = false;
                    this.remove.enabled = false;
                } else {
                    this.login.enabled = true;
                    this.remove.enabled = true;
                }
                if (Keyboard.isKeyDown(200)) {
                    this.offset -= 26.0;
                } else if (Keyboard.isKeyDown(208)) {
                    this.offset += 26.0;
                }
                if (this.offset < 0.0) {
                    this.offset = 0.0F;
                }
                return;
            }
            final int altY = y;
            Alt alt = e.next();
            if (!this.isAltInArea(y)) continue;
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            
            
            String a;
            if (alt.getDate() != null) {
            	String d1 = "";
            	String d2 = "";
                   	d1 = alt.getDate().substring(0, alt.getDate().length() - 15);
                    d2 = alt.getDate().substring(1).substring(1).substring(1).substring(1).substring(1).substring(1).substring(1).substring(1);
                String date = alt.getDate().replaceAll(":", "").replaceAll("/", "");
                String date2 = date.substring(0, date.length() - 8);
                String d = "";
                    d = date2.substring(1).substring(1).substring(1).substring(1);
                int da = (int) Double.parseDouble(d) / 10;
                String month = "Dec";
                switch (da) {
                	case 1 :
                		month = "Jan";
                		break;
                	case 2 :
                		month = "Feb";
                		break;
                	case 3 :
                		month = "Mar";
                		break;
                	case 4 :
                		month = "Apr";
                		break;
                	case 5 :
                		month = "May";
                		break;
                	case 6 :
                		month = "Jun";
                		break;
                	case 7 :
                		month = "Jul";
                		break;
                	case 8 :
                		month = "Aug";
                		break;
                	case 9 :
                		month = "Sep";
                		break;
                	case 10 :
                		month = "Oct";
                		break;
                	case 11 :
                		month = "Nov";
                		break;
                	case 12:
                		month = "Dec";
                		break;
                }
                
                
                	 a = "Added " + d1 + " " + month + " " + d2;
            } else {
            	a = "Added null";
            }
            if (MainMenu.s) {
            RenderUtils.drawShadow(10,1, () -> {
           	 RoundedUtil.drawRound(20, altY - this.offset - 4, 195, 30, 3, new Color(-1));
           });
           RenderUtils.drawBlur(10, () -> {
           	 RoundedUtil.drawRound(20, altY - this.offset - 4, 195, 30, 3, new Color(-1));
           });
            } else {
            	 RoundedUtil.drawRound(0, 0, 0, 0, 0, new Color(0,0,0,100));
            	 RoundedUtil.drawRound(20, y - this.offset - 4, 195, 30, 0, new Color(0,0,0,100));
            }
            if ((name.equalsIgnoreCase(this.mc.session.getUsername()))) {
            	mc.mntsb_18.drawStringWithShadow(TextFormatting.GREEN + "Logged as: " + TextFormatting.RESET + alt.getUsername(), sr.getScaledWidth() - mc.mntsb_18.getStringWidth("Logged as: " + alt.getUsername()) - 2, sr.getScaledHeight() - 10, -1);
            }
            	if (alt == selectedAlt || this.isMouseOverAlt(par1, par2, (double)y - this.offset)) {
                    name = TextFormatting.GOLD + name;
                    a = TextFormatting.GOLD + a;
                }
            
            mc.mntsb_25.drawString(name, 50, (float)((double)y - this.offset + 5.0) - 5, -1);
            mc.mntsb_14.drawString(a, 50, (float)((double)y - this.offset + 19.0) - 5, -1);
            
            RenderUtils.drawImage(new ResourceLocation("celestial/images/atomwhite.png"), 20, altY - this.offset - 3, 28, 28, new Color(-1));
            
            y += 45;
            
            
        }
    }


    public void initGui() {
        buttonList.add(login = new GuiMainMenuButton(1, 110, 10, 80, 4, "Login", true));
        
        buttonList.add(remove = new GuiMainMenuButton(2, width - 90, 10, 80, 4, "Delete", true));
        
        buttonList.add(new GuiMainMenuButton(3, 20, 10, 80, 4, "Add alt", true));
        
        buttonList.add(new GuiMainMenuButton(5, 200, 10, 80, 4, "Random name", true));
        
        buttonList.add(new GuiMainMenuButton(6, 290, 10, 80, 4, "Clear random", true));
        
        buttonList.add(new GuiMainMenuButton(7, 380, 10, 115, 4, "Add server to QuickJoin", true));
        

        login.enabled = false;
        remove.enabled = false;
    }

    protected void keyTyped(char par1, int par2) {
        try {
            super.keyTyped(par1, par2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAltInArea(int y) {
        return y - offset <= height;
    }

    private boolean isMouseOverAlt(double x, double y, double y1) {
        return x >= 20 && y >= y1 - 4 && x <= 200 && y <= y1 + 30 && x >= 0 && y >= 33 && x <= width && y <= height;
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        if (offset < 0) {
            offset = 0;
        }
        int y4 = 0;
        for (Server s : getServers()) {
           	String name = s.getName();
           	if (par1 > 296 - mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 > 128 + y4 && par1 < 296 + mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 < 131 + y4 + 10 ) {
           		mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData(s.getName(), s.getIp(), false)));
           	}
           	y4 += 12;
           }
        
        int y2 = 0;
        for (Server s : getServers()) {
         	if (par1 > 296 - mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 > 128 + y4 && par1 < 296 + mc.mntsb_17.getStringWidth(s.getName()) / 2 && par2 < 131 + y4 + 10 ) {
        		mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData(s.getName(), s.getIp(), false)));
        	}
        	y2 += 12;
        }
        double y = 45 - offset;

        for (Iterator<Alt> e = getAlts().iterator(); e.hasNext(); y += 45) {
            Alt alt = e.next();
            if (isMouseOverAlt(par1, par2, y)) {
                if (alt == selectedAlt) {
                    actionPerformed(login);
                    selectedAlt = null;
                    return;
                }
                selectedAlt = alt;
            }
        }

        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Alt> getAlts() {
        List<Alt> altList = new ArrayList<>();
        Iterator iterator = AltManager.registry.iterator();

        while (true) {
            Alt alt;
            do {
                if (!iterator.hasNext()) {
                    return altList;
                }

                alt = (Alt) iterator.next();
            }
            while (false);

            altList.add(alt);
        }
    }
    
    private List<Server> getServers() {
        List<Server> altList = new ArrayList<>();
        Iterator iterator = ServerManager.getRegistry().iterator();


        while (true) {
            Server alt;
            do {
                if (!iterator.hasNext()) {
                    return altList;
                }

                alt = (Server) iterator.next();
            }
            while (false);

            altList.add(alt);
            
            altList.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().mntsb_18.getStringWidth(((Server)m).getName())).reversed());
        }
    }
}
