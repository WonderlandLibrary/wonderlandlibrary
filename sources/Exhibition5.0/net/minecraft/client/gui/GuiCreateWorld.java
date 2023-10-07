// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import net.minecraft.world.storage.WorldInfo;
import java.io.IOException;
import net.minecraft.world.WorldSettings;
import java.util.Random;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.WorldType;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.resources.I18n;

public class GuiCreateWorld extends GuiScreen
{
    private GuiScreen field_146332_f;
    private GuiTextField field_146333_g;
    private GuiTextField field_146335_h;
    private String field_146336_i;
    private String field_146342_r;
    private String field_175300_s;
    private boolean field_146341_s;
    private boolean field_146340_t;
    private boolean field_146339_u;
    private boolean field_146338_v;
    private boolean field_146337_w;
    private boolean field_146345_x;
    private boolean field_146344_y;
    private GuiButton field_146343_z;
    private GuiButton field_146324_A;
    private GuiButton field_146325_B;
    private GuiButton field_146326_C;
    private GuiButton field_146320_D;
    private GuiButton field_146321_E;
    private GuiButton field_146322_F;
    private String field_146323_G;
    private String field_146328_H;
    private String field_146329_I;
    private String field_146330_J;
    private int field_146331_K;
    public String field_146334_a;
    private static final String[] field_146327_L;
    private static final String __OBFID = "CL_00000689";
    
    public GuiCreateWorld(final GuiScreen p_i46320_1_) {
        this.field_146342_r = "survival";
        this.field_146341_s = true;
        this.field_146334_a = "";
        this.field_146332_f = p_i46320_1_;
        this.field_146329_I = "";
        this.field_146330_J = I18n.format("selectWorld.newWorld", new Object[0]);
    }
    
    @Override
    public void updateScreen() {
        this.field_146333_g.updateCursorCounter();
        this.field_146335_h.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(this.field_146343_z = new GuiButton(2, this.width / 2 - 75, 115, 150, 20, I18n.format("selectWorld.gameMode", new Object[0])));
        this.buttonList.add(this.field_146324_A = new GuiButton(3, this.width / 2 - 75, 187, 150, 20, I18n.format("selectWorld.moreWorldOptions", new Object[0])));
        this.buttonList.add(this.field_146325_B = new GuiButton(4, this.width / 2 - 155, 100, 150, 20, I18n.format("selectWorld.mapFeatures", new Object[0])));
        this.field_146325_B.visible = false;
        this.buttonList.add(this.field_146326_C = new GuiButton(7, this.width / 2 + 5, 151, 150, 20, I18n.format("selectWorld.bonusItems", new Object[0])));
        this.field_146326_C.visible = false;
        this.buttonList.add(this.field_146320_D = new GuiButton(5, this.width / 2 + 5, 100, 150, 20, I18n.format("selectWorld.mapType", new Object[0])));
        this.field_146320_D.visible = false;
        this.buttonList.add(this.field_146321_E = new GuiButton(6, this.width / 2 - 155, 151, 150, 20, I18n.format("selectWorld.allowCommands", new Object[0])));
        this.field_146321_E.visible = false;
        this.buttonList.add(this.field_146322_F = new GuiButton(8, this.width / 2 + 5, 120, 150, 20, I18n.format("selectWorld.customizeType", new Object[0])));
        this.field_146322_F.visible = false;
        (this.field_146333_g = new GuiTextField(9, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20)).setFocused(true);
        this.field_146333_g.setText(this.field_146330_J);
        (this.field_146335_h = new GuiTextField(10, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20)).setText(this.field_146329_I);
        this.func_146316_a(this.field_146344_y);
        this.func_146314_g();
        this.func_146319_h();
    }
    
    private void func_146314_g() {
        this.field_146336_i = this.field_146333_g.getText().trim();
        for (final char var4 : ChatAllowedCharacters.allowedCharactersArray) {
            this.field_146336_i = this.field_146336_i.replace(var4, '_');
        }
        if (StringUtils.isEmpty((CharSequence)this.field_146336_i)) {
            this.field_146336_i = "World";
        }
        this.field_146336_i = func_146317_a(this.mc.getSaveLoader(), this.field_146336_i);
    }
    
    private void func_146319_h() {
        this.field_146343_z.displayString = I18n.format("selectWorld.gameMode", new Object[0]) + ": " + I18n.format("selectWorld.gameMode." + this.field_146342_r, new Object[0]);
        this.field_146323_G = I18n.format("selectWorld.gameMode." + this.field_146342_r + ".line1", new Object[0]);
        this.field_146328_H = I18n.format("selectWorld.gameMode." + this.field_146342_r + ".line2", new Object[0]);
        this.field_146325_B.displayString = I18n.format("selectWorld.mapFeatures", new Object[0]) + " ";
        if (this.field_146341_s) {
            this.field_146325_B.displayString += I18n.format("options.on", new Object[0]);
        }
        else {
            this.field_146325_B.displayString += I18n.format("options.off", new Object[0]);
        }
        this.field_146326_C.displayString = I18n.format("selectWorld.bonusItems", new Object[0]) + " ";
        if (this.field_146338_v && !this.field_146337_w) {
            this.field_146326_C.displayString += I18n.format("options.on", new Object[0]);
        }
        else {
            this.field_146326_C.displayString += I18n.format("options.off", new Object[0]);
        }
        this.field_146320_D.displayString = I18n.format("selectWorld.mapType", new Object[0]) + " " + I18n.format(WorldType.worldTypes[this.field_146331_K].getTranslateName(), new Object[0]);
        this.field_146321_E.displayString = I18n.format("selectWorld.allowCommands", new Object[0]) + " ";
        if (this.field_146340_t && !this.field_146337_w) {
            this.field_146321_E.displayString += I18n.format("options.on", new Object[0]);
        }
        else {
            this.field_146321_E.displayString += I18n.format("options.off", new Object[0]);
        }
    }
    
    public static String func_146317_a(final ISaveFormat p_146317_0_, String p_146317_1_) {
        p_146317_1_ = p_146317_1_.replaceAll("[\\./\"]", "_");
        for (final String var5 : GuiCreateWorld.field_146327_L) {
            if (p_146317_1_.equalsIgnoreCase(var5)) {
                p_146317_1_ = "_" + p_146317_1_ + "_";
            }
        }
        while (p_146317_0_.getWorldInfo(p_146317_1_) != null) {
            p_146317_1_ += "-";
        }
        return p_146317_1_;
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.mc.displayGuiScreen(this.field_146332_f);
            }
            else if (button.id == 0) {
                this.mc.displayGuiScreen(null);
                if (this.field_146345_x) {
                    return;
                }
                this.field_146345_x = true;
                long var2 = new Random().nextLong();
                final String var3 = this.field_146335_h.getText();
                if (!StringUtils.isEmpty((CharSequence)var3)) {
                    try {
                        final long var4 = Long.parseLong(var3);
                        if (var4 != 0L) {
                            var2 = var4;
                        }
                    }
                    catch (NumberFormatException var7) {
                        var2 = var3.hashCode();
                    }
                }
                final WorldSettings.GameType var5 = WorldSettings.GameType.getByName(this.field_146342_r);
                final WorldSettings var6 = new WorldSettings(var2, var5, this.field_146341_s, this.field_146337_w, WorldType.worldTypes[this.field_146331_K]);
                var6.setWorldName(this.field_146334_a);
                if (this.field_146338_v && !this.field_146337_w) {
                    var6.enableBonusChest();
                }
                if (this.field_146340_t && !this.field_146337_w) {
                    var6.enableCommands();
                }
                this.mc.launchIntegratedServer(this.field_146336_i, this.field_146333_g.getText().trim(), var6);
            }
            else if (button.id == 3) {
                this.func_146315_i();
            }
            else if (button.id == 2) {
                if (this.field_146342_r.equals("survival")) {
                    if (!this.field_146339_u) {
                        this.field_146340_t = false;
                    }
                    this.field_146337_w = false;
                    this.field_146342_r = "hardcore";
                    this.field_146337_w = true;
                    this.field_146321_E.enabled = false;
                    this.field_146326_C.enabled = false;
                    this.func_146319_h();
                }
                else if (this.field_146342_r.equals("hardcore")) {
                    if (!this.field_146339_u) {
                        this.field_146340_t = true;
                    }
                    this.field_146337_w = false;
                    this.field_146342_r = "creative";
                    this.func_146319_h();
                    this.field_146337_w = false;
                    this.field_146321_E.enabled = true;
                    this.field_146326_C.enabled = true;
                }
                else {
                    if (!this.field_146339_u) {
                        this.field_146340_t = false;
                    }
                    this.field_146342_r = "survival";
                    this.func_146319_h();
                    this.field_146321_E.enabled = true;
                    this.field_146326_C.enabled = true;
                    this.field_146337_w = false;
                }
                this.func_146319_h();
            }
            else if (button.id == 4) {
                this.field_146341_s = !this.field_146341_s;
                this.func_146319_h();
            }
            else if (button.id == 7) {
                this.field_146338_v = !this.field_146338_v;
                this.func_146319_h();
            }
            else if (button.id == 5) {
                ++this.field_146331_K;
                if (this.field_146331_K >= WorldType.worldTypes.length) {
                    this.field_146331_K = 0;
                }
                while (!this.func_175299_g()) {
                    ++this.field_146331_K;
                    if (this.field_146331_K >= WorldType.worldTypes.length) {
                        this.field_146331_K = 0;
                    }
                }
                this.field_146334_a = "";
                this.func_146319_h();
                this.func_146316_a(this.field_146344_y);
            }
            else if (button.id == 6) {
                this.field_146339_u = true;
                this.field_146340_t = !this.field_146340_t;
                this.func_146319_h();
            }
            else if (button.id == 8) {
                if (WorldType.worldTypes[this.field_146331_K] == WorldType.FLAT) {
                    this.mc.displayGuiScreen(new GuiCreateFlatWorld(this, this.field_146334_a));
                }
                else {
                    this.mc.displayGuiScreen(new GuiCustomizeWorldScreen(this, this.field_146334_a));
                }
            }
        }
    }
    
    private boolean func_175299_g() {
        final WorldType var1 = WorldType.worldTypes[this.field_146331_K];
        return var1 != null && var1.getCanBeCreated() && (var1 != WorldType.DEBUG_WORLD || GuiScreen.isShiftKeyDown());
    }
    
    private void func_146315_i() {
        this.func_146316_a(!this.field_146344_y);
    }
    
    private void func_146316_a(final boolean p_146316_1_) {
        this.field_146344_y = p_146316_1_;
        if (WorldType.worldTypes[this.field_146331_K] == WorldType.DEBUG_WORLD) {
            this.field_146343_z.visible = !this.field_146344_y;
            this.field_146343_z.enabled = false;
            if (this.field_175300_s == null) {
                this.field_175300_s = this.field_146342_r;
            }
            this.field_146342_r = "spectator";
            this.field_146325_B.visible = false;
            this.field_146326_C.visible = false;
            this.field_146320_D.visible = this.field_146344_y;
            this.field_146321_E.visible = false;
            this.field_146322_F.visible = false;
        }
        else {
            this.field_146343_z.visible = !this.field_146344_y;
            this.field_146343_z.enabled = true;
            if (this.field_175300_s != null) {
                this.field_146342_r = this.field_175300_s;
                this.field_175300_s = null;
            }
            this.field_146325_B.visible = (this.field_146344_y && WorldType.worldTypes[this.field_146331_K] != WorldType.CUSTOMIZED);
            this.field_146326_C.visible = this.field_146344_y;
            this.field_146320_D.visible = this.field_146344_y;
            this.field_146321_E.visible = this.field_146344_y;
            this.field_146322_F.visible = (this.field_146344_y && (WorldType.worldTypes[this.field_146331_K] == WorldType.FLAT || WorldType.worldTypes[this.field_146331_K] == WorldType.CUSTOMIZED));
        }
        this.func_146319_h();
        if (this.field_146344_y) {
            this.field_146324_A.displayString = I18n.format("gui.done", new Object[0]);
        }
        else {
            this.field_146324_A.displayString = I18n.format("selectWorld.moreWorldOptions", new Object[0]);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.field_146333_g.isFocused() && !this.field_146344_y) {
            this.field_146333_g.textboxKeyTyped(typedChar, keyCode);
            this.field_146330_J = this.field_146333_g.getText();
        }
        else if (this.field_146335_h.isFocused() && this.field_146344_y) {
            this.field_146335_h.textboxKeyTyped(typedChar, keyCode);
            this.field_146329_I = this.field_146335_h.getText();
        }
        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.buttonList.get(0).enabled = (this.field_146333_g.getText().length() > 0);
        this.func_146314_g();
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.field_146344_y) {
            this.field_146335_h.mouseClicked(mouseX, mouseY, mouseButton);
        }
        else {
            this.field_146333_g.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("selectWorld.create", new Object[0]), this.width / 2, 20, -1);
        if (this.field_146344_y) {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterSeed", new Object[0]), this.width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.seedInfo", new Object[0]), this.width / 2 - 100, 85, -6250336);
            if (this.field_146325_B.visible) {
                this.drawString(this.fontRendererObj, I18n.format("selectWorld.mapFeatures.info", new Object[0]), this.width / 2 - 150, 122, -6250336);
            }
            if (this.field_146321_E.visible) {
                this.drawString(this.fontRendererObj, I18n.format("selectWorld.allowCommands.info", new Object[0]), this.width / 2 - 150, 172, -6250336);
            }
            this.field_146335_h.drawTextBox();
            if (WorldType.worldTypes[this.field_146331_K].showWorldInfoNotice()) {
                this.fontRendererObj.drawSplitString(I18n.format(WorldType.worldTypes[this.field_146331_K].func_151359_c(), new Object[0]), this.field_146320_D.xPosition + 2, this.field_146320_D.yPosition + 22, this.field_146320_D.getButtonWidth(), 10526880);
            }
        }
        else {
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.enterName", new Object[0]), this.width / 2 - 100, 47, -6250336);
            this.drawString(this.fontRendererObj, I18n.format("selectWorld.resultFolder", new Object[0]) + " " + this.field_146336_i, this.width / 2 - 100, 85, -6250336);
            this.field_146333_g.drawTextBox();
            this.drawString(this.fontRendererObj, this.field_146323_G, this.width / 2 - 100, 137, -6250336);
            this.drawString(this.fontRendererObj, this.field_146328_H, this.width / 2 - 100, 149, -6250336);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void func_146318_a(final WorldInfo p_146318_1_) {
        this.field_146330_J = I18n.format("selectWorld.newWorld.copyOf", p_146318_1_.getWorldName());
        this.field_146329_I = p_146318_1_.getSeed() + "";
        this.field_146331_K = p_146318_1_.getTerrainType().getWorldTypeID();
        this.field_146334_a = p_146318_1_.getGeneratorOptions();
        this.field_146341_s = p_146318_1_.isMapFeaturesEnabled();
        this.field_146340_t = p_146318_1_.areCommandsAllowed();
        if (p_146318_1_.isHardcoreModeEnabled()) {
            this.field_146342_r = "hardcore";
        }
        else if (p_146318_1_.getGameType().isSurvivalOrAdventure()) {
            this.field_146342_r = "survival";
        }
        else if (p_146318_1_.getGameType().isCreative()) {
            this.field_146342_r = "creative";
        }
    }
    
    static {
        field_146327_L = new String[] { "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9" };
    }
}
