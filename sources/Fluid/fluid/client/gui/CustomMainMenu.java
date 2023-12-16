// 
// Decompiled by Procyon v0.6.0
// 

package fluid.client.gui;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.Gui;
import fluid.client.config.ConfigManager;
import fluid.client.Client;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiScreen;

public class CustomMainMenu extends GuiScreen
{
    public ResourceLocation rs;
    public ScaledResolution sr;
    public Minecraft mc;
    
    public CustomMainMenu() {
        this.rs = new ResourceLocation("fluidclient/menu-bg.png");
    }
    
    @Override
    public void initGui() {
        this.mc = Minecraft.getMinecraft();
        this.sr = new ScaledResolution(this.mc);
        final int i = 24;
        final int j = this.height / 4 + 89;
        this.addSingleplayerMultiplayerButtons(j, 24);
        this.buttonList.add(new GuiButton(0, this.width - this.width / 4 - 100 + 20 + 2, j + 72 + 12 - 35, 156, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, this.width - this.width / 4 - 100 + 175 + 5, j + 72 + 12 - 35, 20, 20, "X"));
        this.buttonList.add(new GuiButtonLanguage(5, this.width - this.width / 4 - 100, j + 72 + 12 - 35));
        Client.INSTANCE.configManager = new ConfigManager();
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.mc.getTextureManager().bindTexture(this.rs);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.sr.getScaledWidth(), this.sr.getScaledHeight(), (float)this.sr.getScaledWidth(), (float)this.sr.getScaledHeight());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, this.width - this.width / 4 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width - this.width / 4 - 100, p_73969_1_ + p_73969_2_, I18n.format("menu.multiplayer", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
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
            this.mc.shutdown();
        }
    }
}
