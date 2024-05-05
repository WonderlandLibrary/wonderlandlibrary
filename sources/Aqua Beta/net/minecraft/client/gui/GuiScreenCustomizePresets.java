package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeWorldScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenCustomizePresets;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkProviderSettings;
import org.lwjgl.input.Keyboard;

public class GuiScreenCustomizePresets
extends GuiScreen {
    private static final List<Info> field_175310_f = Lists.newArrayList();
    private ListPreset field_175311_g;
    private GuiButton field_175316_h;
    private GuiTextField field_175317_i;
    private GuiCustomizeWorldScreen field_175314_r;
    protected String field_175315_a = "Customize World Presets";
    private String field_175313_s;
    private String field_175312_t;

    public GuiScreenCustomizePresets(GuiCustomizeWorldScreen p_i45524_1_) {
        this.field_175314_r = p_i45524_1_;
    }

    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.field_175315_a = I18n.format((String)"createWorld.customize.custom.presets.title", (Object[])new Object[0]);
        this.field_175313_s = I18n.format((String)"createWorld.customize.presets.share", (Object[])new Object[0]);
        this.field_175312_t = I18n.format((String)"createWorld.customize.presets.list", (Object[])new Object[0]);
        this.field_175317_i = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
        this.field_175311_g = new ListPreset(this);
        this.field_175317_i.setMaxStringLength(2000);
        this.field_175317_i.setText(this.field_175314_r.func_175323_a());
        this.field_175316_h = new GuiButton(0, width / 2 - 102, height - 27, 100, 20, I18n.format((String)"createWorld.customize.presets.select", (Object[])new Object[0]));
        this.buttonList.add((Object)this.field_175316_h);
        this.buttonList.add((Object)new GuiButton(1, width / 2 + 3, height - 27, 100, 20, I18n.format((String)"gui.cancel", (Object[])new Object[0])));
        this.func_175304_a();
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_175311_g.handleMouseInput();
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.field_175317_i.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!this.field_175317_i.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.field_175314_r.func_175324_a(this.field_175317_i.getText());
                this.mc.displayGuiScreen((GuiScreen)this.field_175314_r);
                break;
            }
            case 1: {
                this.mc.displayGuiScreen((GuiScreen)this.field_175314_r);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.field_175311_g.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_175315_a, width / 2, 8, 0xFFFFFF);
        this.drawString(this.fontRendererObj, this.field_175313_s, 50, 30, 0xA0A0A0);
        this.drawString(this.fontRendererObj, this.field_175312_t, 50, 70, 0xA0A0A0);
        this.field_175317_i.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        this.field_175317_i.updateCursorCounter();
        super.updateScreen();
    }

    public void func_175304_a() {
        this.field_175316_h.enabled = this.func_175305_g();
    }

    private boolean func_175305_g() {
        return this.field_175311_g.field_178053_u > -1 && this.field_175311_g.field_178053_u < field_175310_f.size() || this.field_175317_i.getText().length() > 1;
    }

    static /* synthetic */ List access$000() {
        return field_175310_f;
    }

    static /* synthetic */ ListPreset access$100(GuiScreenCustomizePresets x0) {
        return x0.field_175311_g;
    }

    static /* synthetic */ GuiTextField access$200(GuiScreenCustomizePresets x0) {
        return x0.field_175317_i;
    }

    static {
        ChunkProviderSettings.Factory chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{ \"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":8.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":0.5, \"biomeScaleWeight\":2.0, \"biomeScaleOffset\":0.375, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":255 }");
        ResourceLocation resourcelocation = new ResourceLocation("textures/gui/presets/water.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.waterWorld", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
        chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{\"coordinateScale\":3000.0, \"heightScale\":6000.0, \"upperLimitScale\":250.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
        resourcelocation = new ResourceLocation("textures/gui/presets/isles.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.isleLand", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
        chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":5.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":1.0, \"biomeScaleWeight\":4.0, \"biomeScaleOffset\":1.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
        resourcelocation = new ResourceLocation("textures/gui/presets/delight.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.caveDelight", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
        chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{\"coordinateScale\":738.41864, \"heightScale\":157.69133, \"upperLimitScale\":801.4267, \"lowerLimitScale\":1254.1643, \"depthNoiseScaleX\":374.93652, \"depthNoiseScaleZ\":288.65228, \"depthNoiseScaleExponent\":1.2092624, \"mainNoiseScaleX\":1355.9908, \"mainNoiseScaleY\":745.5343, \"mainNoiseScaleZ\":1183.464, \"baseSize\":1.8758626, \"stretchY\":1.7137525, \"biomeDepthWeight\":1.7553768, \"biomeDepthOffset\":3.4701107, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":2.535211, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
        resourcelocation = new ResourceLocation("textures/gui/presets/madness.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.mountains", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
        chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":1000.0, \"mainNoiseScaleY\":3000.0, \"mainNoiseScaleZ\":1000.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":20 }");
        resourcelocation = new ResourceLocation("textures/gui/presets/drought.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.drought", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
        chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":2.0, \"lowerLimitScale\":64.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":6 }");
        resourcelocation = new ResourceLocation("textures/gui/presets/chaos.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.caveChaos", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
        chunkprovidersettings$factory = ChunkProviderSettings.Factory.jsonToFactory((String)"{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":true, \"seaLevel\":40 }");
        resourcelocation = new ResourceLocation("textures/gui/presets/luck.png");
        field_175310_f.add((Object)new Info(I18n.format((String)"createWorld.customize.custom.preset.goodLuck", (Object[])new Object[0]), resourcelocation, chunkprovidersettings$factory));
    }
}
