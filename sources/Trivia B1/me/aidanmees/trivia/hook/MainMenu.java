package me.aidanmees.trivia.hook;


import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.darkstorm.minecraft.gui.font.UnicodeFontRenderer;
import org.lwjgl.opengl.GLContext;

import com.google.common.collect.Lists;

import me.aidanmees.trivia.client.alts.GuiAltChecker;
import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.minimeme.CookieClicker;
import me.aidanmees.trivia.cracker.gui.GuitriviaAccHacker;
import me.aidanmees.trivia.gui.GuitriviaAltLogin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.CustomButton.GuiButtonDark;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

public class MainMenu
extends GuiScreen
implements GuiYesNoCallback {
    private static final AtomicInteger field_175373_f = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final Random field_175374_h = new Random();
    
    private Random random;
    private float updateCounter;
    FontRenderer fontRenderer = new UnicodeFontRenderer(new Font("Arial", Font.PLAIN, 60));
    public boolean doCheckVersion = false;

    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    
    private boolean field_175375_v;
    private final Object field_104025_t;
    private String field_92025_p;
    private String field_146972_A;
    private String field_104024_v;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
    public static final String field_96138_a = "Please click " + (Object)((Object)EnumChatFormatting.UNDERLINE) + "here" + (Object)((Object)EnumChatFormatting.RESET) + " for more information.";
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton field_175372_K;
    private static final String __OBFID = "CL_00001154";

    public MainMenu() {
        block18 : {
            BufferedReader var1;
            this.random = new Random();
            this.field_175375_v = true;
            this.field_104025_t = new Object();
            this.field_146972_A = field_96138_a;
            this.splashText = "missingno";
            var1 = null;
            try {
                try {
                    String var3;
                    ArrayList<String> var2 = Lists.newArrayList();
                    var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
                    while ((var3 = var1.readLine()) != null) {
                        if ((var3 = var3.trim()).isEmpty()) continue;
                        var2.add(var3);
                    }
                    if (!var2.isEmpty()) {
                        do {
                            this.splashText = (String)var2.get(field_175374_h.nextInt(var2.size()));
                        } while (this.splashText.hashCode() == 125780783);
                    }
                }
                catch (IOException var2) {
                    if (var1 != null) {
                        try {
                            var1.close();
                        }
                        catch (IOException iOException) {}
                    }
                    break block18;
                }
            }
            catch (Throwable throwable) {
                if (var1 != null) {
                    try {
                        var1.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                
            }
            if (var1 != null) {
                try {
                    var1.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }
        this.updateCounter = field_175374_h.nextFloat();
        this.field_92025_p = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.field_92025_p = I18n.format("title.oldgl1", new Object[0]);
            this.field_146972_A = I18n.format("title.oldgl2", new Object[0]);
            this.field_104024_v = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }

    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void initGui() {
    	
    	
        
        Calendar var1 = Calendar.getInstance();
        var1.setTime(new Date());
        if (var1.get(2) + 1 == 11 && var1.get(5) == 9) {
            this.splashText = "Happy birthday, ez!";
        } else if (var1.get(2) + 1 == 6 && var1.get(5) == 1) {
            this.splashText = "Happy birthday, Notch!";
        } else if (var1.get(2) + 1 == 12 && var1.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        } else if (var1.get(2) + 1 == 1 && var1.get(5) == 1) {
            this.splashText = "Happy new year!";
        } else if (var1.get(2) + 1 == 10 && var1.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        boolean var2 = true;
        int var3 = height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(var3, 24);
        } else {
            this.addSingleplayerMultiplayerButtons(var3, 24);
        }
        Object object = this.field_104025_t;
        synchronized (object) {
            this.field_92023_s = this.fontRendererObj.getStringWidth(this.field_92025_p);
            this.field_92024_r = this.fontRendererObj.getStringWidth(this.field_146972_A);
            int var5 = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (width - var5) / 2;
            this.field_92021_u = ((GuiButton)this.buttonList.get((int)0)).yPosition - 24;
            this.field_92020_v = this.field_92022_t + var5;
            this.field_92019_w = this.field_92021_u + 24;
        }
        ScaledResolution sr2 = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.displayWidth, Minecraft.displayHeight);
       
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
        this.buttonList.add(new GuiButtonDark(187, width / 2 - 77, height / 2 - 115 + 40, I18n.format("Devs", new Object[0])));
        this.buttonList.add(new GuiButtonDark(1337, width / 2 - 77, height / 2 - 115 + 40 + p_73969_2_ * 1 - 4, I18n.format("Portscan", new Object[0])));
        this.field_175372_K = new GuiButtonDark(10000, width / 2 - 77, height / 2 - 115 + 40 + p_73969_2_ * 2 - 4 - 4, I18n.format("Updates", new Object[0]));
        
        this.buttonList.add(this.field_175372_K);
        this.buttonList.add(new GuiButtonDark(555, width / 2 - 77, height / 2 - 115 + 40 + p_73969_2_ * 3 + 8, I18n.format("AltChecker", new Object[0])));
        this.buttonList.add(new GuiButtonDark(666, width / 2 - 77, height / 2 - 115 + 40 + p_73969_2_ * 3  - 4 - 4 - 4, I18n.format("Cookieclicker", new Object[0])));
        this.buttonList.add(new GuiButtonDark(4, width / 2 - 77, height / 2 - 115 + 40 + p_73969_2_ * 6 - 4 - 4 - 4 - 4, I18n.format("Back", new Object[0])));
        
    
    
    
    }

    private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonResetDemo = new GuiButton(12, width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0]));
        this.buttonList.add(this.buttonResetDemo);
        ISaveFormat var3 = this.mc.getSaveLoader();
        WorldInfo var4 = var3.getWorldInfo("Demo_World");
        if (var4 == null) {
            this.buttonResetDemo.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        WorldInfo var3;
        ISaveFormat var2;
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 10000) {
            //this.mc.displayGuiScreen(new GuiChangelog());
        }
        if (button.id == 1337) {
        	this.mc.displayGuiScreen(new GuitriviaAccHacker(this));
        }
        if (button.id == 187) {
           /// Wrapper.openWebpage("https://www.youtube.com/c/NexHax");
        }
        if (button.id == 555) {
        	this.mc.displayGuiScreen(new GuiAltChecker(this));
         }
        if (button.id == 666) {
        	this.mc.displayGuiScreen(new CookieClicker(this));
         }
        if (button.id == 1000) {
            this.mc.displayGuiScreen(new GuitriviaAltLogin(this));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 4) {
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12 && (var3 = (var2 = this.mc.getSaveLoader()).getWorldInfo("Demo_World")) != null) {
            GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
            this.mc.displayGuiScreen(var4);
        }
    }

    @Override
    public void confirmClicked(boolean result, int id2) {
        if (result && id2 == 12) {
            ISaveFormat var6 = this.mc.getSaveLoader();
            var6.flushCache();
            var6.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        } else if (id2 == 13) {
            if (result) {
                try {
                    Class var3 = Class.forName("java.awt.Desktop");
                    Object var4 = var3.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    var3.getMethod("browse", URI.class).invoke(var4, new URI(this.field_104024_v));
                }
                catch (Throwable var5) {
                    logger.error("Couldn't open link", var5);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    public MainMenu(boolean doUpdateCheck) {
		this();
		
		Random RANDOM = new Random();
		int img = RANDOM.nextInt(5);
		doCheckVersion = doUpdateCheck;
	}


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	Gui.drawRect(0.0, 0.0, 0.0, 0.0, -1);
        ScaledResolution scaledRes = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        ScaledResolution scaledRes1 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        
            this.mc.getTextureManager().bindTexture(trivia.triviaImage2);
        
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, scaledRes1.getScaledWidth(), scaledRes1.getScaledHeight(), scaledRes1.getScaledWidth(), scaledRes1.getScaledHeight(), scaledRes1.getScaledWidth(), scaledRes1.getScaledHeight());
        
        MainMenu.drawRect(0, ScaledResolution.getScaledHeight() - 24, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight() - 23, new Color(0, 200, 255, 255).getRGB());
        MainMenu.drawRect(0, ScaledResolution.getScaledHeight() - 23, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), Integer.MIN_VALUE);
        SimpleDateFormat df2 = new SimpleDateFormat("dd MM yyyy");
        Date today = Calendar.getInstance().getTime();
  
        String renderDate = df2.format(today);
        SimpleDateFormat dff = new SimpleDateFormat("HH:mm:ss");
        Date todayy = Calendar.getInstance().getTime();
        String renderTime = dff.format(todayy);
        mc.fontRendererObj.drawString(renderTime, width / 2 - 19, ScaledResolution.getScaledHeight() - 15, 16777215);
        fontRenderer.drawString("Info", width / 2 - 25, height / 2 - 130 + 40 - 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        Object var4 = this.field_104025_t;
        Object object = this.field_104025_t;
        synchronized (object) {
            if (this.field_92025_p.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this, this.field_104024_v, 13, true);
                var5.disableSecurityWarning();
                this.mc.displayGuiScreen(var5);
            }
        }
    }
}

