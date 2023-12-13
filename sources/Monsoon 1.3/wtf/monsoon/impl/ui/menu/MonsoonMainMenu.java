package wtf.monsoon.impl.ui.menu;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import wtf.monsoon.Monsoon;
import wtf.monsoon.api.auth.MonsoonLoginScreen;
import wtf.monsoon.api.util.render.ImageButton;
import wtf.monsoon.impl.ui.alt.GuiAltLogin;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;

public class MonsoonMainMenu extends GuiScreen {

    public float mouseXOffset = 0, mouseYOffset = 0;

    ArrayList<ImageButton> imageButtons = new ArrayList<>();

    public MonsoonMainMenu() {

    }

    @Override
    public void initGui() {
        if(!Monsoon.authorized) {
            //mc.shutdown();
            mc.displayGuiScreen(new MonsoonLoginScreen());
        }
        super.initGui();
        int var3 = this.height / 2;
        buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 - 32, 98, 20, "Singleplayer"));
        buttonList.add(new GuiButton(2, this.width / 2 + 2, var3 - 32, 98, 20, "Multiplayer"));

        buttonList.add(new GuiButton(4, this.width / 2 - 100, var3 - 10, 98, 20, "Alts"));
        buttonList.add(new GuiButton(3, this.width / 2 + 2, var3 - 10, 98, 20, "Settings"));

        buttonList.add(new GuiButton(5, this.width / 2 - 100, var3 + 12, 98, 20, "Language"));
        buttonList.add(new GuiButton(6, this.width / 2 + 2, var3 + 12, 98, 20, "Quit"));

        this.addButtons(this.height / 2 + (48 - 70), 24);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int move = 100;
        mouseXOffset = 4;
        mouseYOffset = 4;

        GlStateManager.pushMatrix();
        Gui.drawRect(0,0,width,height, new Color(23,23,23).getRGB());
        GlStateManager.popMatrix();

        //this.drawGradientRect(0, height - 100, width, height, 0x00000000, new Color(0,170,255, 220).getRGB());

        //Gui.drawRect(this.width / 2 - 120, this.height / 2 - 70,this.width / 2 + 120, this.height / 2 + 80, new Color(30, 30, 30).getRGB());
        //DrawUtil.drawHollowRect(this.width / 2 - 120, this.height / 2 - 70,240, 150, ColorUtil.colorLerpv2(new Color(0,140,255), new Color(142,65,173), 0.5f).getRGB());
        //DrawUtil.draw2DImage(new ResourceLocation("monsoon/monsoon.png"), this.width / 2 - 1, this.height / 2 - 65, 150, 150, Color.white);


        GlStateManager.pushMatrix();
        GlStateManager.scale(0.8, 0.8, 0.8);
        this.drawString(fontRendererObj, "Welcome to Monsoon, " + Monsoon.INSTANCE.monsoonUsername + "! Current build: " + Monsoon.INSTANCE.version, 4, 4, -1);
        GlStateManager.popMatrix();

        ArrayList<String> changeLogs = new ArrayList<String>();

        int line = 1;
        changeLogs.add("");
        changeLogs.add("+ Literally every module and a lot of the base rewritten");
        changeLogs.add("+ Mineplex bypasses (speed, longjump, antibot, killaura)");
        changeLogs.add("+ Verus bypasses");
        changeLogs.add("+ Step (Motion and Normal)");
        changeLogs.add("+ New Longjump");
        changeLogs.add("+ New Watermark Modes");
        changeLogs.add("+ CSGO/2D ESP (rewritten twice in development)");
        changeLogs.add("+ Item ESP");
        changeLogs.add("+ New Main Menu!");
        changeLogs.add("+ Crasher");
        changeLogs.add("+ Discord RPC");
        changeLogs.add("+ Disabler");
        changeLogs.add("+ MonsoonGen");
        changeLogs.add("+ New Swing Animation");
        changeLogs.add("+ Overlay (block and entity)");
        changeLogs.add("+ BreakReminder (reminds you to take a break every so often, toggleable and customizable)");
        changeLogs.add("+ Hypixel Bow Longjump");
        changeLogs.add("+ Breaker");
        changeLogs.add("+ Exploit category");
        changeLogs.add("+ ClickGUI Rewrite");
        changeLogs.add("+ Infinite Verus fly");
        changeLogs.add("+ HighJump");
        changeLogs.add("+ VoidHop (antivoid)");
        changeLogs.add("+ Full spacepotato.de bypass (nice cUsToM aNtIcHeAt)");
        changeLogs.add("+ Config System Rewrite (actually fucking works now)");

        changeLogs.add("");
        changeLogs.add("");

        changeLogs.add("* AntiBot");
        changeLogs.add("* Hypixel Fly");
        changeLogs.add("* Alt Login (now sponsored by Drilled alts :D)");
        changeLogs.add("* Scaffold");

        changeLogs.add("");
        changeLogs.add("");

        changeLogs.add("- Music Player");
        changeLogs.add("- Bedwars fly");
        changeLogs.add("- Old ClickGUI");


        changeLogs.add("");
        changeLogs.add("");
        changeLogs.add("Developed by quick.");


        for (String s : changeLogs) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.7, 0.7, 0.7);
            if (s.startsWith("+")) {
                //DrawUtil.drawBorderedRoundedRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, 5, new Color(0,0,0).getRGB(), new Color(0, 255, 0).getRGB());
                Gui.drawRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, new Color(0, 255, 0).getRGB());
                this.drawString(fontRendererObj, s, 11, this.fontRendererObj.FONT_HEIGHT * line + 5, -1);
            }
            if (s.startsWith("*")) {
                //DrawUtil.drawBorderedRoundedRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, 5, new Color(0,0,0).getRGB(), new Color(255, 255, 0).getRGB());
                Gui.drawRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, new Color(255, 255, 0).getRGB());
                this.drawString(fontRendererObj, s, 11, this.fontRendererObj.FONT_HEIGHT * line + 5, -1);
            }
            if (s.startsWith("-")) {
                //DrawUtil.drawBorderedRoundedRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, 5, new Color(0,0,0).getRGB(), new Color(255, 0, 0).getRGB());
                Gui.drawRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, new Color(255, 0, 0).getRGB());
                this.drawString(fontRendererObj, s, 11, this.fontRendererObj.FONT_HEIGHT * line + 5, -1);
            } if(s.isEmpty()) {
                this.drawString(fontRendererObj, s, 11, this.fontRendererObj.FONT_HEIGHT * line + 5, -1);
            }
            if (s.startsWith("Developed by Bruzz.")) {
                //DrawUtil.drawBorderedRoundedRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, 5, new Color(0,0,0).getRGB(), new Color(0, 170, 255).getRGB());
                Gui.drawRect(3, this.fontRendererObj.FONT_HEIGHT * line + 5.5f, 4 + 5, this.fontRendererObj.FONT_HEIGHT * line + 11.f, new Color(0, 170, 255).getRGB());
                this.drawString(fontRendererObj, s, 11, this.fontRendererObj.FONT_HEIGHT * line + 5, -1);
            }
            GlStateManager.popMatrix();
            line++;
        }

        //NotificationManager.render();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void addButtons(int p_73969_1_, int p_73969_2_) {
        //this.imageButtons.clear();
        //this.imageButtons.add(new ImageButton(new ResourceLocation("monsoon/monsoon.png"), this.width / 2 - (int) (34), this.height / 2 - (125), 70, 70, "Monsoon", 934, this));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch(button.id) {
            case 1:
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 3:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;
            case 4:
                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 5:
                mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
                break;
            case 6:
                mc.shutdown();
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void onGuiClosed() {

    }

}
