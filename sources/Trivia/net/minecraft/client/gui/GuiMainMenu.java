package net.minecraft.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.lwjgl.opengl.GL11;

import me.aidanmees.trivia.client.account.AccountScreen;
import me.aidanmees.trivia.client.alts.New.GuiAltManager;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.DarkRenderer;
import me.aidanmees.trivia.client.tools.MarisaTimer;
import me.aidanmees.trivia.client.tools.RenderHelper;
import me.aidanmees.trivia.client.tools.RenderUtils;
import me.aidanmees.trivia.client.tools.Utils;
import me.aidanmees.trivia.gui.GuitriviaAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private int k;
    public static int xx;
    public static int animation;
    private static boolean login;
    private static boolean session;
    
    
    
    private static boolean skins;
    private static boolean website;
    private static boolean checking;
    private static MarisaTimer timer;
    private static MarisaTimer timer2;
    private int anim1 = 0;
    private int anim2 = 0;
    private int anim3 = 0;
    FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 55));
    FontRenderer fontRenderer2 = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 95));
    
    FontRenderer fontRenderer3 = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 18));
    FontRenderer fontRenderer4 = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 30));
    private int anim4 = 0;
    private int anim5 = 0;
    static String HypixelBypass = "";
    static String GwenBypass = "";
    static String NCPBypass = "";
    static String GuardianBypass = "";
    static String AACBypass = "";
    
    static boolean HypixelBypassb = false;
    static boolean GwenBypassb = false;
    static boolean NCPBypassb = false;
    static boolean GuardianBypassb = false;
    static boolean AACBypassb = false;
    
    
    static boolean hypixel;

    
    
    static {
        animation = 0;
        timer = new MarisaTimer();
        timer2 = new MarisaTimer();
    }

    @Override
    public void initGui() {
        super.initGui();
    }
    

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    
    	
    	
    	
        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(trivia.triviaImage2);
        this.drawTexturedModalRect(0, 0, 0, 0, width, height);
        ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        GuiAltManager.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, width, height, width, height, width, height);
      
        
        
        int var3 = this.height / 4 + 48;
        RenderUtils.aaa(width,height / 6, -width, var3 - 999 + 24 * 6 - 4 - 0.1f, 1.5F, -1728053248, Integer.MIN_VALUE);
		
        this.drawSession(mouseX, mouseY);
       
       
        SimpleDateFormat df2 = new SimpleDateFormat("dd MM yyyy");
        Date today = Calendar.getInstance().getTime();
        String hex = String.format("#%02x%02x%02x", Utils.ColorRed(), Utils.ColorGreen(), Utils.ColorBlue());
        String renderDate = df2.format(today);
        SimpleDateFormat dff = new SimpleDateFormat("HH:mm:ss");
        Date todayy = Calendar.getInstance().getTime();
        String renderTime = dff.format(todayy);
        
        RenderUtils.aaa(width / 2 + 177, height , width / 2 - 165, height - 60, 7F, Color.black.getRGB(), 0);
		
        
        
        RenderUtils.aaa(width / 2 - 100, height / 2 - 50 - 10, width / 2 + 100, height / 2 + 50 + 0, 1.5F, -1728053248, Integer.MIN_VALUE);
		
       
        fontRenderer4.drawString("Bypass Status:", 10, height/17, -1);
        
        fontRenderer3.drawString("Remix client, Made by Aidan and Mees",this.width / 2 - 76,
				this.height / 2 + 35, -1);
        
        fontRenderer.drawString(renderTime, width /2 - fontRenderer.getStringWidth(renderTime) / 2, height / 2  + 6 - 10, -1);
       
        fontRenderer2.drawString("Remix", width /2 - 30- fontRenderer.getStringWidth("Remix") / 2, height / 2 - 45 - 10, -1);
        
        RenderHelper.drawHLine(width / 2 - 60, width / 2 + 60, height / 2 - 5.5f, -1);
        
        RenderHelper.drawHLine(width, -width, height / 6, Color.BLACK.getRGB());
        
        RenderHelper.drawHLine(width / 2 + 176, width / 2 - 167,height - 61.8f, Color.BLACK.getRGB());
       
       
       
        if ((animation += 11) > 720) {
            animation = 0;
        }
        this.animateAnimations(mouseX, mouseY);

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
        RenderHelper.drawIcon(width / 2 - 140, height - 47 - this.anim1, 40, 40, trivia.singleleplayer);
        RenderHelper.drawIcon(width / 2 - 80, height - 47 - this.anim2, 40, 40, trivia.multiplayer);
        RenderHelper.drawIcon(width / 2 - 20, height - 47 - this.anim3, 40, 40, trivia.altmanager);
        RenderHelper.drawIcon(width / 2 + 40, height - 47 - this.anim4, 40, 40,trivia.settings);
        RenderHelper.drawIcon(width / 2 + 105, height - 47 - this.anim5, 40, 40,trivia.exit);
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public int rgbToHex(int r, int g, int b) {
        return ((1 << 24) + (r << 16) + (g << 8) + b);
    }

    private void drawSession(int mouseX, int mouseY) {
      
        fontRenderer3.drawString("Watchdog:", 120, height / 14, -1);
        fontRenderer3.drawString("Gwen:", 195, height / 14, -1);
        fontRenderer3.drawString("NCP:", 255,height / 14, -1);
        fontRenderer3.drawString("Guardian:", 315, height / 14, -1);
        fontRenderer3.drawString("AAC:", 395, height / 14, -1);
       
        
           
        RenderHelper.drawFullCircle(170,  height / 14 + 5.5, 2.0, (- animation) / 2, 99.0f, 360, -13710223);
        RenderHelper.drawFullCircle(230,   height / 14 + 5.5, 2.0, (- animation) / 3, 10.0f, 360, -13710223);
        RenderHelper.drawFullCircle(287,   height / 14 + 5.5, 2.0, animation / 2, 10.0f, 360, -13710223);
        RenderHelper.drawFullCircle(365,   height / 14 + 5.5, 2.0, animation / 3, 10.0f, 360, -13710223);
        RenderHelper.drawFullCircle(428,   height / 14 + 5.5, 2.0, animation / 3, 10.0f, 360, -13710223);
        
    }

    public boolean checkSite(String url) {
        HttpURLConnection connection = null;
        try {
            URL u2 = new URL(url);
            connection = (HttpURLConnection)u2.openConnection();
            connection.setRequestMethod("POST");
            return true;
        }
        catch (MalformedURLException e2) {
            e2.printStackTrace();
            return false;
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void animateAnimations(int mouseX, int mouseY) {
        boolean isOverExit;
        boolean isOverSingleplayer = mouseX > width / 2 - 140 && mouseX < width / 2 - 100 && mouseY > height - 47 && mouseY < height - 7;
        boolean isOverMultiplayer = mouseX > width / 2 - 80 && mouseX < width / 2 - 40 && mouseY > height - 47 && mouseY < height - 7;
        boolean isOverAltManager = mouseX > width / 2 - 20 && mouseX < width / 2 + 20 && mouseY > height - 47 && mouseY < height - 7;
        boolean isOverSettings = mouseX > width / 2 + 40 && mouseX < width / 2 + 80 && mouseY > height - 47 && mouseY < height - 7;
        boolean bl2 = isOverExit = mouseX > width / 2 + 105 && mouseX < width / 2 + 145 && mouseY > height - 47 && mouseY < height - 7;
        if (isOverSingleplayer) {
            if (this.anim1 < 3) {
                ++this.anim1;
            }
        } else if (this.anim1 > 0) {
            --this.anim1;
        }
        if (isOverMultiplayer) {
            if (this.anim2 < 3) {
                ++this.anim2;
            }
        } else if (this.anim2 > 0) {
            --this.anim2;
        }
        if (isOverAltManager) {
            if (this.anim3 < 3) {
                ++this.anim3;
            }
        } else if (this.anim3 > 0) {
            --this.anim3;
        }
        if (isOverSettings) {
            if (this.anim4 < 3) {
                ++this.anim4;
            }
        } else if (this.anim4 > 0) {
            --this.anim4;
        }
        if (isOverExit) {
            if (this.anim5 < 3) {
                ++this.anim5;
            }
        } else if (this.anim5 > 0) {
            --this.anim5;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        boolean isOverExit;
        boolean isOverSingleplayer = mouseX > width / 2 - 140 && mouseX < width / 2 - 100 && mouseY > height - 47 && mouseY < height - 7;
        boolean isOverMultiplayer = mouseX > width / 2 - 80 && mouseX < width / 2 - 40 && mouseY > height - 47 && mouseY < height - 7;
        boolean isOverAltManager = mouseX > width / 2 - 20 && mouseX < width / 2 + 20 && mouseY > height - 47 && mouseY < height - 7;
        boolean isOverSettings = mouseX > width / 2 + 40 && mouseX < width / 2 + 80 && mouseY > height - 47 && mouseY < height - 7;
        boolean bl2 = isOverExit = mouseX > width / 2 + 105 && mouseX < width / 2 + 145 && mouseY > height - 47 && mouseY < height - 7;
        if (mouseButton == 0 && isOverSingleplayer) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (mouseButton == 0 && isOverMultiplayer) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (mouseButton == 0 && isOverAltManager) {
        	 this.mc.displayGuiScreen(new GuiMultiplayer(this));
            this.mc.displayGuiScreen(new GuiAltManager(this));
        }
        if (mouseButton == 0 && isOverSettings) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (mouseButton == 0 && isOverExit) {
            this.mc.shutdown();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}

