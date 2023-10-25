package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.gen.FlatGeneratorInfo;

public class GuiCreateFlatWorld
extends GuiScreen {
    private final GuiCreateWorld createWorldGui;
    private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
    private String flatWorldTitle;
    private String field_146394_i;
    private String field_146391_r;
    private Details createFlatWorldListSlotGui;
    private GuiButton field_146389_t;
    private GuiButton field_146388_u;
    private GuiButton field_146386_v;

    public GuiCreateFlatWorld(GuiCreateWorld createWorldGuiIn, String p_i1029_2_) {
        this.createWorldGui = createWorldGuiIn;
        this.func_146383_a(p_i1029_2_);
    }

    public String func_146384_e() {
        return this.theFlatGeneratorInfo.toString();
    }

    public void func_146383_a(String p_146383_1_) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString((String)p_146383_1_);
    }

    public void initGui() {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format((String)"createWorld.customize.flat.title", (Object[])new Object[0]);
        this.field_146394_i = I18n.format((String)"createWorld.customize.flat.tile", (Object[])new Object[0]);
        this.field_146391_r = I18n.format((String)"createWorld.customize.flat.height", (Object[])new Object[0]);
        this.createFlatWorldListSlotGui = new Details(this);
        this.field_146389_t = new GuiButton(2, width / 2 - 154, height - 52, 100, 20, I18n.format((String)"createWorld.customize.flat.addLayer", (Object[])new Object[0]) + " (NYI)");
        this.buttonList.add((Object)this.field_146389_t);
        this.field_146388_u = new GuiButton(3, width / 2 - 50, height - 52, 100, 20, I18n.format((String)"createWorld.customize.flat.editLayer", (Object[])new Object[0]) + " (NYI)");
        this.buttonList.add((Object)this.field_146388_u);
        this.field_146386_v = new GuiButton(4, width / 2 - 155, height - 52, 150, 20, I18n.format((String)"createWorld.customize.flat.removeLayer", (Object[])new Object[0]));
        this.buttonList.add((Object)this.field_146386_v);
        this.buttonList.add((Object)new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format((String)"gui.done", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(5, width / 2 + 5, height - 52, 150, 20, I18n.format((String)"createWorld.customize.presets", (Object[])new Object[0])));
        this.buttonList.add((Object)new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format((String)"gui.cancel", (Object[])new Object[0])));
        this.field_146388_u.visible = false;
        this.field_146389_t.visible = false;
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;
        if (button.id == 1) {
            this.mc.displayGuiScreen((GuiScreen)this.createWorldGui);
        } else if (button.id == 0) {
            this.createWorldGui.chunkProviderSettingsJson = this.func_146384_e();
            this.mc.displayGuiScreen((GuiScreen)this.createWorldGui);
        } else if (button.id == 5) {
            this.mc.displayGuiScreen((GuiScreen)new GuiFlatPresets(this));
        } else if (button.id == 4 && this.func_146382_i()) {
            this.theFlatGeneratorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.field_148228_k = Math.min((int)this.createFlatWorldListSlotGui.field_148228_k, (int)(this.theFlatGeneratorInfo.getFlatLayers().size() - 1));
        }
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }

    public void func_146375_g() {
        boolean flag;
        this.field_146386_v.enabled = flag = this.func_146382_i();
        this.field_146388_u.enabled = flag;
        this.field_146388_u.enabled = false;
        this.field_146389_t.enabled = false;
    }

    private boolean func_146382_i() {
        return this.createFlatWorldListSlotGui.field_148228_k > -1 && this.createFlatWorldListSlotGui.field_148228_k < this.theFlatGeneratorInfo.getFlatLayers().size();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, width / 2, 8, 0xFFFFFF);
        int i = width / 2 - 92 - 16;
        this.drawString(this.fontRendererObj, this.field_146394_i, i, 32, 0xFFFFFF);
        this.drawString(this.fontRendererObj, this.field_146391_r, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    static /* synthetic */ FlatGeneratorInfo access$000(GuiCreateFlatWorld x0) {
        return x0.theFlatGeneratorInfo;
    }
}
