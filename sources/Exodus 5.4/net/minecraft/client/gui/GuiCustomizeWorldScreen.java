/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.primitives.Floats
 */
package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.primitives.Floats;
import java.io.IOException;
import java.util.Random;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiListButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenCustomizePresets;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

public class GuiCustomizeWorldScreen
extends GuiScreen
implements GuiSlider.FormatHelper,
GuiPageButtonList.GuiResponder {
    protected String[] field_175342_h = new String[4];
    private GuiCreateWorld field_175343_i;
    private GuiButton field_175346_u;
    private ChunkProviderSettings.Factory field_175334_E;
    private Predicate<String> field_175332_D = new Predicate<String>(){

        public boolean apply(String string) {
            Float f = Floats.tryParse((String)string);
            return string.length() == 0 || f != null && Floats.isFinite((float)f.floatValue()) && f.floatValue() >= 0.0f;
        }
    };
    private GuiButton field_175344_w;
    protected String field_175333_f = "Page 1 of 3";
    private Random random;
    private boolean field_175338_A = false;
    private GuiButton field_175350_z;
    private ChunkProviderSettings.Factory field_175336_F;
    private GuiButton field_175347_t;
    protected String field_175341_a = "Customize World Settings";
    private GuiButton field_175348_s;
    private GuiButton field_175351_y;
    private boolean field_175340_C = false;
    private int field_175339_B = 0;
    private GuiButton field_175345_v;
    protected String field_175335_g = "Basic Settings";
    private GuiButton field_175352_x;
    private GuiPageButtonList field_175349_r;

    private void func_175327_a(float f) {
        Gui gui = this.field_175349_r.func_178056_g();
        if (gui instanceof GuiTextField) {
            GuiTextField guiTextField;
            Float f2;
            float f3 = f;
            if (GuiScreen.isShiftKeyDown()) {
                f3 = f * 0.1f;
                if (GuiScreen.isCtrlKeyDown()) {
                    f3 *= 0.1f;
                }
            } else if (GuiScreen.isCtrlKeyDown()) {
                f3 = f * 10.0f;
                if (GuiScreen.isAltKeyDown()) {
                    f3 *= 10.0f;
                }
            }
            if ((f2 = Floats.tryParse((String)(guiTextField = (GuiTextField)gui).getText())) != null) {
                f2 = Float.valueOf(f2.floatValue() + f3);
                int n = guiTextField.getId();
                String string = this.func_175330_b(guiTextField.getId(), f2.floatValue());
                guiTextField.setText(string);
                this.func_175319_a(n, string);
            }
        }
    }

    @Override
    public void initGui() {
        int n = 0;
        int n2 = 0;
        if (this.field_175349_r != null) {
            n = this.field_175349_r.func_178059_e();
            n2 = this.field_175349_r.getAmountScrolled();
        }
        this.field_175341_a = I18n.format("options.customizeTitle", new Object[0]);
        this.buttonList.clear();
        this.field_175345_v = new GuiButton(302, 20, 5, 80, 20, I18n.format("createWorld.customize.custom.prev", new Object[0]));
        this.buttonList.add(this.field_175345_v);
        this.field_175344_w = new GuiButton(303, width - 100, 5, 80, 20, I18n.format("createWorld.customize.custom.next", new Object[0]));
        this.buttonList.add(this.field_175344_w);
        this.field_175346_u = new GuiButton(304, width / 2 - 187, height - 27, 90, 20, I18n.format("createWorld.customize.custom.defaults", new Object[0]));
        this.buttonList.add(this.field_175346_u);
        this.field_175347_t = new GuiButton(301, width / 2 - 92, height - 27, 90, 20, I18n.format("createWorld.customize.custom.randomize", new Object[0]));
        this.buttonList.add(this.field_175347_t);
        this.field_175350_z = new GuiButton(305, width / 2 + 3, height - 27, 90, 20, I18n.format("createWorld.customize.custom.presets", new Object[0]));
        this.buttonList.add(this.field_175350_z);
        this.field_175348_s = new GuiButton(300, width / 2 + 98, height - 27, 90, 20, I18n.format("gui.done", new Object[0]));
        this.buttonList.add(this.field_175348_s);
        this.field_175346_u.enabled = this.field_175338_A;
        this.field_175352_x = new GuiButton(306, width / 2 - 55, 160, 50, 20, I18n.format("gui.yes", new Object[0]));
        this.field_175352_x.visible = false;
        this.buttonList.add(this.field_175352_x);
        this.field_175351_y = new GuiButton(307, width / 2 + 5, 160, 50, 20, I18n.format("gui.no", new Object[0]));
        this.field_175351_y.visible = false;
        this.buttonList.add(this.field_175351_y);
        if (this.field_175339_B != 0) {
            this.field_175352_x.visible = true;
            this.field_175351_y.visible = true;
        }
        this.func_175325_f();
        if (n != 0) {
            this.field_175349_r.func_181156_c(n);
            this.field_175349_r.scrollBy(n2);
            this.func_175328_i();
        }
    }

    private void func_181031_a(boolean bl) {
        this.field_175338_A = bl;
        this.field_175346_u.enabled = bl;
    }

    @Override
    protected void keyTyped(char c, int n) throws IOException {
        super.keyTyped(c, n);
        if (this.field_175339_B == 0) {
            switch (n) {
                case 200: {
                    this.func_175327_a(1.0f);
                    break;
                }
                case 208: {
                    this.func_175327_a(-1.0f);
                    break;
                }
                default: {
                    this.field_175349_r.func_178062_a(c, n);
                }
            }
        }
    }

    private void func_175322_b(int n) {
        this.field_175339_B = n;
        this.func_175329_a(true);
    }

    @Override
    protected void mouseReleased(int n, int n2, int n3) {
        super.mouseReleased(n, n2, n3);
        if (this.field_175340_C) {
            this.field_175340_C = false;
        } else if (this.field_175339_B == 0) {
            this.field_175349_r.mouseReleased(n, n2, n3);
        }
    }

    private void func_175326_g() {
        this.field_175336_F.func_177863_a();
        this.func_175325_f();
        this.func_181031_a(false);
    }

    public void func_175324_a(String string) {
        this.field_175336_F = string != null && string.length() != 0 ? ChunkProviderSettings.Factory.jsonToFactory(string) : new ChunkProviderSettings.Factory();
    }

    private void func_175328_i() {
        this.field_175345_v.enabled = this.field_175349_r.func_178059_e() != 0;
        this.field_175344_w.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
        this.field_175333_f = I18n.format("book.pageIndicator", this.field_175349_r.func_178059_e() + 1, this.field_175349_r.func_178057_f());
        this.field_175335_g = this.field_175342_h[this.field_175349_r.func_178059_e()];
        this.field_175347_t.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
    }

    @Override
    protected void mouseClicked(int n, int n2, int n3) throws IOException {
        super.mouseClicked(n, n2, n3);
        if (this.field_175339_B == 0 && !this.field_175340_C) {
            this.field_175349_r.mouseClicked(n, n2, n3);
        }
    }

    @Override
    public void func_175319_a(int n, String string) {
        float f = 0.0f;
        try {
            f = Float.parseFloat(string);
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        float f2 = 0.0f;
        switch (n) {
            case 132: {
                f2 = this.field_175336_F.mainNoiseScaleX = MathHelper.clamp_float(f, 1.0f, 5000.0f);
                break;
            }
            case 133: {
                f2 = this.field_175336_F.mainNoiseScaleY = MathHelper.clamp_float(f, 1.0f, 5000.0f);
                break;
            }
            case 134: {
                f2 = this.field_175336_F.mainNoiseScaleZ = MathHelper.clamp_float(f, 1.0f, 5000.0f);
                break;
            }
            case 135: {
                f2 = this.field_175336_F.depthNoiseScaleX = MathHelper.clamp_float(f, 1.0f, 2000.0f);
                break;
            }
            case 136: {
                f2 = this.field_175336_F.depthNoiseScaleZ = MathHelper.clamp_float(f, 1.0f, 2000.0f);
                break;
            }
            case 137: {
                f2 = this.field_175336_F.depthNoiseScaleExponent = MathHelper.clamp_float(f, 0.01f, 20.0f);
                break;
            }
            case 138: {
                f2 = this.field_175336_F.baseSize = MathHelper.clamp_float(f, 1.0f, 25.0f);
                break;
            }
            case 139: {
                f2 = this.field_175336_F.coordinateScale = MathHelper.clamp_float(f, 1.0f, 6000.0f);
                break;
            }
            case 140: {
                f2 = this.field_175336_F.heightScale = MathHelper.clamp_float(f, 1.0f, 6000.0f);
                break;
            }
            case 141: {
                f2 = this.field_175336_F.stretchY = MathHelper.clamp_float(f, 0.01f, 50.0f);
                break;
            }
            case 142: {
                f2 = this.field_175336_F.upperLimitScale = MathHelper.clamp_float(f, 1.0f, 5000.0f);
                break;
            }
            case 143: {
                f2 = this.field_175336_F.lowerLimitScale = MathHelper.clamp_float(f, 1.0f, 5000.0f);
                break;
            }
            case 144: {
                f2 = this.field_175336_F.biomeDepthWeight = MathHelper.clamp_float(f, 1.0f, 20.0f);
                break;
            }
            case 145: {
                f2 = this.field_175336_F.biomeDepthOffset = MathHelper.clamp_float(f, 0.0f, 20.0f);
                break;
            }
            case 146: {
                f2 = this.field_175336_F.biomeScaleWeight = MathHelper.clamp_float(f, 1.0f, 20.0f);
                break;
            }
            case 147: {
                f2 = this.field_175336_F.biomeScaleOffset = MathHelper.clamp_float(f, 0.0f, 20.0f);
            }
        }
        if (f2 != f && f != 0.0f) {
            ((GuiTextField)this.field_175349_r.func_178061_c(n)).setText(this.func_175330_b(n, f2));
        }
        ((GuiSlider)this.field_175349_r.func_178061_c(n - 132 + 100)).func_175218_a(f2, false);
        if (!this.field_175336_F.equals(this.field_175334_E)) {
            this.func_181031_a(true);
        }
    }

    public GuiCustomizeWorldScreen(GuiScreen guiScreen, String string) {
        this.field_175334_E = new ChunkProviderSettings.Factory();
        this.random = new Random();
        this.field_175343_i = (GuiCreateWorld)guiScreen;
        this.func_175324_a(string);
    }

    private void func_175325_f() {
        GuiPageButtonList.GuiListEntry[] guiListEntryArray = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiSlideEntry(160, I18n.format("createWorld.customize.custom.seaLevel", new Object[0]), true, this, 1.0f, 255.0f, this.field_175336_F.seaLevel), new GuiPageButtonList.GuiButtonEntry(148, I18n.format("createWorld.customize.custom.useCaves", new Object[0]), true, this.field_175336_F.useCaves), new GuiPageButtonList.GuiButtonEntry(150, I18n.format("createWorld.customize.custom.useStrongholds", new Object[0]), true, this.field_175336_F.useStrongholds), new GuiPageButtonList.GuiButtonEntry(151, I18n.format("createWorld.customize.custom.useVillages", new Object[0]), true, this.field_175336_F.useVillages), new GuiPageButtonList.GuiButtonEntry(152, I18n.format("createWorld.customize.custom.useMineShafts", new Object[0]), true, this.field_175336_F.useMineShafts), new GuiPageButtonList.GuiButtonEntry(153, I18n.format("createWorld.customize.custom.useTemples", new Object[0]), true, this.field_175336_F.useTemples), new GuiPageButtonList.GuiButtonEntry(210, I18n.format("createWorld.customize.custom.useMonuments", new Object[0]), true, this.field_175336_F.useMonuments), new GuiPageButtonList.GuiButtonEntry(154, I18n.format("createWorld.customize.custom.useRavines", new Object[0]), true, this.field_175336_F.useRavines), new GuiPageButtonList.GuiButtonEntry(149, I18n.format("createWorld.customize.custom.useDungeons", new Object[0]), true, this.field_175336_F.useDungeons), new GuiPageButtonList.GuiSlideEntry(157, I18n.format("createWorld.customize.custom.dungeonChance", new Object[0]), true, this, 1.0f, 100.0f, this.field_175336_F.dungeonChance), new GuiPageButtonList.GuiButtonEntry(155, I18n.format("createWorld.customize.custom.useWaterLakes", new Object[0]), true, this.field_175336_F.useWaterLakes), new GuiPageButtonList.GuiSlideEntry(158, I18n.format("createWorld.customize.custom.waterLakeChance", new Object[0]), true, this, 1.0f, 100.0f, this.field_175336_F.waterLakeChance), new GuiPageButtonList.GuiButtonEntry(156, I18n.format("createWorld.customize.custom.useLavaLakes", new Object[0]), true, this.field_175336_F.useLavaLakes), new GuiPageButtonList.GuiSlideEntry(159, I18n.format("createWorld.customize.custom.lavaLakeChance", new Object[0]), true, this, 10.0f, 100.0f, this.field_175336_F.lavaLakeChance), new GuiPageButtonList.GuiButtonEntry(161, I18n.format("createWorld.customize.custom.useLavaOceans", new Object[0]), true, this.field_175336_F.useLavaOceans), new GuiPageButtonList.GuiSlideEntry(162, I18n.format("createWorld.customize.custom.fixedBiome", new Object[0]), true, this, -1.0f, 37.0f, this.field_175336_F.fixedBiome), new GuiPageButtonList.GuiSlideEntry(163, I18n.format("createWorld.customize.custom.biomeSize", new Object[0]), true, this, 1.0f, 8.0f, this.field_175336_F.biomeSize), new GuiPageButtonList.GuiSlideEntry(164, I18n.format("createWorld.customize.custom.riverSize", new Object[0]), true, this, 1.0f, 5.0f, this.field_175336_F.riverSize)};
        GuiPageButtonList.GuiListEntry[] guiListEntryArray2 = new GuiPageButtonList.GuiListEntry[66];
        guiListEntryArray2[0] = new GuiPageButtonList.GuiLabelEntry(416, I18n.format("tile.dirt.name", new Object[0]), false);
        guiListEntryArray2[2] = new GuiPageButtonList.GuiSlideEntry(165, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.dirtSize);
        guiListEntryArray2[3] = new GuiPageButtonList.GuiSlideEntry(166, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.dirtCount);
        guiListEntryArray2[4] = new GuiPageButtonList.GuiSlideEntry(167, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.dirtMinHeight);
        guiListEntryArray2[5] = new GuiPageButtonList.GuiSlideEntry(168, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.dirtMaxHeight);
        guiListEntryArray2[6] = new GuiPageButtonList.GuiLabelEntry(417, I18n.format("tile.gravel.name", new Object[0]), false);
        guiListEntryArray2[8] = new GuiPageButtonList.GuiSlideEntry(169, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.gravelSize);
        guiListEntryArray2[9] = new GuiPageButtonList.GuiSlideEntry(170, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.gravelCount);
        guiListEntryArray2[10] = new GuiPageButtonList.GuiSlideEntry(171, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.gravelMinHeight);
        guiListEntryArray2[11] = new GuiPageButtonList.GuiSlideEntry(172, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.gravelMaxHeight);
        guiListEntryArray2[12] = new GuiPageButtonList.GuiLabelEntry(418, I18n.format("tile.stone.granite.name", new Object[0]), false);
        guiListEntryArray2[14] = new GuiPageButtonList.GuiSlideEntry(173, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.graniteSize);
        guiListEntryArray2[15] = new GuiPageButtonList.GuiSlideEntry(174, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.graniteCount);
        guiListEntryArray2[16] = new GuiPageButtonList.GuiSlideEntry(175, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.graniteMinHeight);
        guiListEntryArray2[17] = new GuiPageButtonList.GuiSlideEntry(176, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.graniteMaxHeight);
        guiListEntryArray2[18] = new GuiPageButtonList.GuiLabelEntry(419, I18n.format("tile.stone.diorite.name", new Object[0]), false);
        guiListEntryArray2[20] = new GuiPageButtonList.GuiSlideEntry(177, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.dioriteSize);
        guiListEntryArray2[21] = new GuiPageButtonList.GuiSlideEntry(178, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.dioriteCount);
        guiListEntryArray2[22] = new GuiPageButtonList.GuiSlideEntry(179, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.dioriteMinHeight);
        guiListEntryArray2[23] = new GuiPageButtonList.GuiSlideEntry(180, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.dioriteMaxHeight);
        guiListEntryArray2[24] = new GuiPageButtonList.GuiLabelEntry(420, I18n.format("tile.stone.andesite.name", new Object[0]), false);
        guiListEntryArray2[26] = new GuiPageButtonList.GuiSlideEntry(181, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.andesiteSize);
        guiListEntryArray2[27] = new GuiPageButtonList.GuiSlideEntry(182, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.andesiteCount);
        guiListEntryArray2[28] = new GuiPageButtonList.GuiSlideEntry(183, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.andesiteMinHeight);
        guiListEntryArray2[29] = new GuiPageButtonList.GuiSlideEntry(184, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.andesiteMaxHeight);
        guiListEntryArray2[30] = new GuiPageButtonList.GuiLabelEntry(421, I18n.format("tile.oreCoal.name", new Object[0]), false);
        guiListEntryArray2[32] = new GuiPageButtonList.GuiSlideEntry(185, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.coalSize);
        guiListEntryArray2[33] = new GuiPageButtonList.GuiSlideEntry(186, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.coalCount);
        guiListEntryArray2[34] = new GuiPageButtonList.GuiSlideEntry(187, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.coalMinHeight);
        guiListEntryArray2[35] = new GuiPageButtonList.GuiSlideEntry(189, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.coalMaxHeight);
        guiListEntryArray2[36] = new GuiPageButtonList.GuiLabelEntry(422, I18n.format("tile.oreIron.name", new Object[0]), false);
        guiListEntryArray2[38] = new GuiPageButtonList.GuiSlideEntry(190, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.ironSize);
        guiListEntryArray2[39] = new GuiPageButtonList.GuiSlideEntry(191, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.ironCount);
        guiListEntryArray2[40] = new GuiPageButtonList.GuiSlideEntry(192, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.ironMinHeight);
        guiListEntryArray2[41] = new GuiPageButtonList.GuiSlideEntry(193, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.ironMaxHeight);
        guiListEntryArray2[42] = new GuiPageButtonList.GuiLabelEntry(423, I18n.format("tile.oreGold.name", new Object[0]), false);
        guiListEntryArray2[44] = new GuiPageButtonList.GuiSlideEntry(194, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.goldSize);
        guiListEntryArray2[45] = new GuiPageButtonList.GuiSlideEntry(195, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.goldCount);
        guiListEntryArray2[46] = new GuiPageButtonList.GuiSlideEntry(196, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.goldMinHeight);
        guiListEntryArray2[47] = new GuiPageButtonList.GuiSlideEntry(197, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.goldMaxHeight);
        guiListEntryArray2[48] = new GuiPageButtonList.GuiLabelEntry(424, I18n.format("tile.oreRedstone.name", new Object[0]), false);
        guiListEntryArray2[50] = new GuiPageButtonList.GuiSlideEntry(198, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.redstoneSize);
        guiListEntryArray2[51] = new GuiPageButtonList.GuiSlideEntry(199, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.redstoneCount);
        guiListEntryArray2[52] = new GuiPageButtonList.GuiSlideEntry(200, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.redstoneMinHeight);
        guiListEntryArray2[53] = new GuiPageButtonList.GuiSlideEntry(201, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.redstoneMaxHeight);
        guiListEntryArray2[54] = new GuiPageButtonList.GuiLabelEntry(425, I18n.format("tile.oreDiamond.name", new Object[0]), false);
        guiListEntryArray2[56] = new GuiPageButtonList.GuiSlideEntry(202, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.diamondSize);
        guiListEntryArray2[57] = new GuiPageButtonList.GuiSlideEntry(203, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.diamondCount);
        guiListEntryArray2[58] = new GuiPageButtonList.GuiSlideEntry(204, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.diamondMinHeight);
        guiListEntryArray2[59] = new GuiPageButtonList.GuiSlideEntry(205, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.diamondMaxHeight);
        guiListEntryArray2[60] = new GuiPageButtonList.GuiLabelEntry(426, I18n.format("tile.oreLapis.name", new Object[0]), false);
        guiListEntryArray2[62] = new GuiPageButtonList.GuiSlideEntry(206, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0f, 50.0f, this.field_175336_F.lapisSize);
        guiListEntryArray2[63] = new GuiPageButtonList.GuiSlideEntry(207, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0f, 40.0f, this.field_175336_F.lapisCount);
        guiListEntryArray2[64] = new GuiPageButtonList.GuiSlideEntry(208, I18n.format("createWorld.customize.custom.center", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.lapisCenterHeight);
        guiListEntryArray2[65] = new GuiPageButtonList.GuiSlideEntry(209, I18n.format("createWorld.customize.custom.spread", new Object[0]), false, this, 0.0f, 255.0f, this.field_175336_F.lapisSpread);
        GuiPageButtonList.GuiListEntry[] guiListEntryArray3 = guiListEntryArray2;
        GuiPageButtonList.GuiListEntry[] guiListEntryArray4 = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiSlideEntry(100, I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]), false, this, 1.0f, 5000.0f, this.field_175336_F.mainNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(101, I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]), false, this, 1.0f, 5000.0f, this.field_175336_F.mainNoiseScaleY), new GuiPageButtonList.GuiSlideEntry(102, I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]), false, this, 1.0f, 5000.0f, this.field_175336_F.mainNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(103, I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]), false, this, 1.0f, 2000.0f, this.field_175336_F.depthNoiseScaleX), new GuiPageButtonList.GuiSlideEntry(104, I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]), false, this, 1.0f, 2000.0f, this.field_175336_F.depthNoiseScaleZ), new GuiPageButtonList.GuiSlideEntry(105, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]), false, this, 0.01f, 20.0f, this.field_175336_F.depthNoiseScaleExponent), new GuiPageButtonList.GuiSlideEntry(106, I18n.format("createWorld.customize.custom.baseSize", new Object[0]), false, this, 1.0f, 25.0f, this.field_175336_F.baseSize), new GuiPageButtonList.GuiSlideEntry(107, I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]), false, this, 1.0f, 6000.0f, this.field_175336_F.coordinateScale), new GuiPageButtonList.GuiSlideEntry(108, I18n.format("createWorld.customize.custom.heightScale", new Object[0]), false, this, 1.0f, 6000.0f, this.field_175336_F.heightScale), new GuiPageButtonList.GuiSlideEntry(109, I18n.format("createWorld.customize.custom.stretchY", new Object[0]), false, this, 0.01f, 50.0f, this.field_175336_F.stretchY), new GuiPageButtonList.GuiSlideEntry(110, I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]), false, this, 1.0f, 5000.0f, this.field_175336_F.upperLimitScale), new GuiPageButtonList.GuiSlideEntry(111, I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]), false, this, 1.0f, 5000.0f, this.field_175336_F.lowerLimitScale), new GuiPageButtonList.GuiSlideEntry(112, I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]), false, this, 1.0f, 20.0f, this.field_175336_F.biomeDepthWeight), new GuiPageButtonList.GuiSlideEntry(113, I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]), false, this, 0.0f, 20.0f, this.field_175336_F.biomeDepthOffset), new GuiPageButtonList.GuiSlideEntry(114, I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]), false, this, 1.0f, 20.0f, this.field_175336_F.biomeScaleWeight), new GuiPageButtonList.GuiSlideEntry(115, I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]), false, this, 0.0f, 20.0f, this.field_175336_F.biomeScaleOffset)};
        GuiPageButtonList.GuiListEntry[] guiListEntryArray5 = new GuiPageButtonList.GuiListEntry[]{new GuiPageButtonList.GuiLabelEntry(400, String.valueOf(I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(132, String.format("%5.3f", Float.valueOf(this.field_175336_F.mainNoiseScaleX)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(401, String.valueOf(I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(133, String.format("%5.3f", Float.valueOf(this.field_175336_F.mainNoiseScaleY)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(402, String.valueOf(I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(134, String.format("%5.3f", Float.valueOf(this.field_175336_F.mainNoiseScaleZ)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(403, String.valueOf(I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(135, String.format("%5.3f", Float.valueOf(this.field_175336_F.depthNoiseScaleX)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(404, String.valueOf(I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(136, String.format("%5.3f", Float.valueOf(this.field_175336_F.depthNoiseScaleZ)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(405, String.valueOf(I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(137, String.format("%2.3f", Float.valueOf(this.field_175336_F.depthNoiseScaleExponent)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(406, String.valueOf(I18n.format("createWorld.customize.custom.baseSize", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(138, String.format("%2.3f", Float.valueOf(this.field_175336_F.baseSize)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(407, String.valueOf(I18n.format("createWorld.customize.custom.coordinateScale", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(139, String.format("%5.3f", Float.valueOf(this.field_175336_F.coordinateScale)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(408, String.valueOf(I18n.format("createWorld.customize.custom.heightScale", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(140, String.format("%5.3f", Float.valueOf(this.field_175336_F.heightScale)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(409, String.valueOf(I18n.format("createWorld.customize.custom.stretchY", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(141, String.format("%2.3f", Float.valueOf(this.field_175336_F.stretchY)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(410, String.valueOf(I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(142, String.format("%5.3f", Float.valueOf(this.field_175336_F.upperLimitScale)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(411, String.valueOf(I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(143, String.format("%5.3f", Float.valueOf(this.field_175336_F.lowerLimitScale)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(412, String.valueOf(I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(144, String.format("%2.3f", Float.valueOf(this.field_175336_F.biomeDepthWeight)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(413, String.valueOf(I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(145, String.format("%2.3f", Float.valueOf(this.field_175336_F.biomeDepthOffset)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(414, String.valueOf(I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(146, String.format("%2.3f", Float.valueOf(this.field_175336_F.biomeScaleWeight)), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(415, String.valueOf(I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0])) + ":", false), new GuiPageButtonList.EditBoxEntry(147, String.format("%2.3f", Float.valueOf(this.field_175336_F.biomeScaleOffset)), false, this.field_175332_D)};
        this.field_175349_r = new GuiPageButtonList(this.mc, width, height, 32, height - 32, 25, this, guiListEntryArray, guiListEntryArray3, guiListEntryArray4, guiListEntryArray5);
        int n = 0;
        while (n < 4) {
            this.field_175342_h[n] = I18n.format("createWorld.customize.custom.page" + n, new Object[0]);
            ++n;
        }
        this.func_175328_i();
    }

    private void func_175329_a(boolean bl) {
        this.field_175352_x.visible = bl;
        this.field_175351_y.visible = bl;
        this.field_175347_t.enabled = !bl;
        this.field_175348_s.enabled = !bl;
        this.field_175345_v.enabled = !bl;
        this.field_175344_w.enabled = !bl;
        this.field_175346_u.enabled = this.field_175338_A && !bl;
        this.field_175350_z.enabled = !bl;
        this.field_175349_r.func_181155_a(!bl);
    }

    @Override
    public void onTick(int n, float f) {
        Gui gui;
        switch (n) {
            case 100: {
                this.field_175336_F.mainNoiseScaleX = f;
                break;
            }
            case 101: {
                this.field_175336_F.mainNoiseScaleY = f;
                break;
            }
            case 102: {
                this.field_175336_F.mainNoiseScaleZ = f;
                break;
            }
            case 103: {
                this.field_175336_F.depthNoiseScaleX = f;
                break;
            }
            case 104: {
                this.field_175336_F.depthNoiseScaleZ = f;
                break;
            }
            case 105: {
                this.field_175336_F.depthNoiseScaleExponent = f;
                break;
            }
            case 106: {
                this.field_175336_F.baseSize = f;
                break;
            }
            case 107: {
                this.field_175336_F.coordinateScale = f;
                break;
            }
            case 108: {
                this.field_175336_F.heightScale = f;
                break;
            }
            case 109: {
                this.field_175336_F.stretchY = f;
                break;
            }
            case 110: {
                this.field_175336_F.upperLimitScale = f;
                break;
            }
            case 111: {
                this.field_175336_F.lowerLimitScale = f;
                break;
            }
            case 112: {
                this.field_175336_F.biomeDepthWeight = f;
                break;
            }
            case 113: {
                this.field_175336_F.biomeDepthOffset = f;
                break;
            }
            case 114: {
                this.field_175336_F.biomeScaleWeight = f;
                break;
            }
            case 115: {
                this.field_175336_F.biomeScaleOffset = f;
            }
            default: {
                break;
            }
            case 157: {
                this.field_175336_F.dungeonChance = (int)f;
                break;
            }
            case 158: {
                this.field_175336_F.waterLakeChance = (int)f;
                break;
            }
            case 159: {
                this.field_175336_F.lavaLakeChance = (int)f;
                break;
            }
            case 160: {
                this.field_175336_F.seaLevel = (int)f;
                break;
            }
            case 162: {
                this.field_175336_F.fixedBiome = (int)f;
                break;
            }
            case 163: {
                this.field_175336_F.biomeSize = (int)f;
                break;
            }
            case 164: {
                this.field_175336_F.riverSize = (int)f;
                break;
            }
            case 165: {
                this.field_175336_F.dirtSize = (int)f;
                break;
            }
            case 166: {
                this.field_175336_F.dirtCount = (int)f;
                break;
            }
            case 167: {
                this.field_175336_F.dirtMinHeight = (int)f;
                break;
            }
            case 168: {
                this.field_175336_F.dirtMaxHeight = (int)f;
                break;
            }
            case 169: {
                this.field_175336_F.gravelSize = (int)f;
                break;
            }
            case 170: {
                this.field_175336_F.gravelCount = (int)f;
                break;
            }
            case 171: {
                this.field_175336_F.gravelMinHeight = (int)f;
                break;
            }
            case 172: {
                this.field_175336_F.gravelMaxHeight = (int)f;
                break;
            }
            case 173: {
                this.field_175336_F.graniteSize = (int)f;
                break;
            }
            case 174: {
                this.field_175336_F.graniteCount = (int)f;
                break;
            }
            case 175: {
                this.field_175336_F.graniteMinHeight = (int)f;
                break;
            }
            case 176: {
                this.field_175336_F.graniteMaxHeight = (int)f;
                break;
            }
            case 177: {
                this.field_175336_F.dioriteSize = (int)f;
                break;
            }
            case 178: {
                this.field_175336_F.dioriteCount = (int)f;
                break;
            }
            case 179: {
                this.field_175336_F.dioriteMinHeight = (int)f;
                break;
            }
            case 180: {
                this.field_175336_F.dioriteMaxHeight = (int)f;
                break;
            }
            case 181: {
                this.field_175336_F.andesiteSize = (int)f;
                break;
            }
            case 182: {
                this.field_175336_F.andesiteCount = (int)f;
                break;
            }
            case 183: {
                this.field_175336_F.andesiteMinHeight = (int)f;
                break;
            }
            case 184: {
                this.field_175336_F.andesiteMaxHeight = (int)f;
                break;
            }
            case 185: {
                this.field_175336_F.coalSize = (int)f;
                break;
            }
            case 186: {
                this.field_175336_F.coalCount = (int)f;
                break;
            }
            case 187: {
                this.field_175336_F.coalMinHeight = (int)f;
                break;
            }
            case 189: {
                this.field_175336_F.coalMaxHeight = (int)f;
                break;
            }
            case 190: {
                this.field_175336_F.ironSize = (int)f;
                break;
            }
            case 191: {
                this.field_175336_F.ironCount = (int)f;
                break;
            }
            case 192: {
                this.field_175336_F.ironMinHeight = (int)f;
                break;
            }
            case 193: {
                this.field_175336_F.ironMaxHeight = (int)f;
                break;
            }
            case 194: {
                this.field_175336_F.goldSize = (int)f;
                break;
            }
            case 195: {
                this.field_175336_F.goldCount = (int)f;
                break;
            }
            case 196: {
                this.field_175336_F.goldMinHeight = (int)f;
                break;
            }
            case 197: {
                this.field_175336_F.goldMaxHeight = (int)f;
                break;
            }
            case 198: {
                this.field_175336_F.redstoneSize = (int)f;
                break;
            }
            case 199: {
                this.field_175336_F.redstoneCount = (int)f;
                break;
            }
            case 200: {
                this.field_175336_F.redstoneMinHeight = (int)f;
                break;
            }
            case 201: {
                this.field_175336_F.redstoneMaxHeight = (int)f;
                break;
            }
            case 202: {
                this.field_175336_F.diamondSize = (int)f;
                break;
            }
            case 203: {
                this.field_175336_F.diamondCount = (int)f;
                break;
            }
            case 204: {
                this.field_175336_F.diamondMinHeight = (int)f;
                break;
            }
            case 205: {
                this.field_175336_F.diamondMaxHeight = (int)f;
                break;
            }
            case 206: {
                this.field_175336_F.lapisSize = (int)f;
                break;
            }
            case 207: {
                this.field_175336_F.lapisCount = (int)f;
                break;
            }
            case 208: {
                this.field_175336_F.lapisCenterHeight = (int)f;
                break;
            }
            case 209: {
                this.field_175336_F.lapisSpread = (int)f;
            }
        }
        if (n >= 100 && n < 116 && (gui = this.field_175349_r.func_178061_c(n - 100 + 132)) != null) {
            ((GuiTextField)gui).setText(this.func_175330_b(n, f));
        }
        if (!this.field_175336_F.equals(this.field_175334_E)) {
            this.func_181031_a(true);
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.field_175349_r.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, this.field_175341_a, width / 2, 2, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, this.field_175333_f, width / 2, 12, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, this.field_175335_g, width / 2, 22, 0xFFFFFF);
        super.drawScreen(n, n2, f);
        if (this.field_175339_B != 0) {
            GuiCustomizeWorldScreen.drawRect(0.0, 0.0, width, height, Integer.MIN_VALUE);
            GuiCustomizeWorldScreen.drawHorizontalLine(width / 2 - 91, width / 2 + 90, 99, -2039584);
            GuiCustomizeWorldScreen.drawHorizontalLine(width / 2 - 91, width / 2 + 90, 185, -6250336);
            this.drawVerticalLine(width / 2 - 91, 99, 185, -2039584);
            this.drawVerticalLine(width / 2 + 90, 99, 185, -6250336);
            float f2 = 85.0f;
            float f3 = 180.0f;
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(optionsBackground);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            float f4 = 32.0f;
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldRenderer.pos(width / 2 - 90, 185.0, 0.0).tex(0.0, 2.65625).color(64, 64, 64, 64).endVertex();
            worldRenderer.pos(width / 2 + 90, 185.0, 0.0).tex(5.625, 2.65625).color(64, 64, 64, 64).endVertex();
            worldRenderer.pos(width / 2 + 90, 100.0, 0.0).tex(5.625, 0.0).color(64, 64, 64, 64).endVertex();
            worldRenderer.pos(width / 2 - 90, 100.0, 0.0).tex(0.0, 0.0).color(64, 64, 64, 64).endVertex();
            tessellator.draw();
            this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirmTitle", new Object[0]), width / 2, 105, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm1", new Object[0]), width / 2, 125, 0xFFFFFF);
            this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm2", new Object[0]), width / 2, 135, 0xFFFFFF);
            this.field_175352_x.drawButton(this.mc, n, n2);
            this.field_175351_y.drawButton(this.mc, n, n2);
        }
    }

    @Override
    public String getText(int n, String string, float f) {
        return String.valueOf(string) + ": " + this.func_175330_b(n, f);
    }

    private String func_175330_b(int n, float f) {
        switch (n) {
            case 100: 
            case 101: 
            case 102: 
            case 103: 
            case 104: 
            case 107: 
            case 108: 
            case 110: 
            case 111: 
            case 132: 
            case 133: 
            case 134: 
            case 135: 
            case 136: 
            case 139: 
            case 140: 
            case 142: 
            case 143: {
                return String.format("%5.3f", Float.valueOf(f));
            }
            case 105: 
            case 106: 
            case 109: 
            case 112: 
            case 113: 
            case 114: 
            case 115: 
            case 137: 
            case 138: 
            case 141: 
            case 144: 
            case 145: 
            case 146: 
            case 147: {
                return String.format("%2.3f", Float.valueOf(f));
            }
            default: {
                return String.format("%d", (int)f);
            }
            case 162: 
        }
        if (f < 0.0f) {
            return I18n.format("gui.all", new Object[0]);
        }
        if ((int)f >= BiomeGenBase.hell.biomeID) {
            BiomeGenBase biomeGenBase = BiomeGenBase.getBiomeGenArray()[(int)f + 2];
            return biomeGenBase != null ? biomeGenBase.biomeName : "?";
        }
        BiomeGenBase biomeGenBase = BiomeGenBase.getBiomeGenArray()[(int)f];
        return biomeGenBase != null ? biomeGenBase.biomeName : "?";
    }

    @Override
    public void func_175321_a(int n, boolean bl) {
        switch (n) {
            case 148: {
                this.field_175336_F.useCaves = bl;
                break;
            }
            case 149: {
                this.field_175336_F.useDungeons = bl;
                break;
            }
            case 150: {
                this.field_175336_F.useStrongholds = bl;
                break;
            }
            case 151: {
                this.field_175336_F.useVillages = bl;
                break;
            }
            case 152: {
                this.field_175336_F.useMineShafts = bl;
                break;
            }
            case 153: {
                this.field_175336_F.useTemples = bl;
                break;
            }
            case 154: {
                this.field_175336_F.useRavines = bl;
                break;
            }
            case 155: {
                this.field_175336_F.useWaterLakes = bl;
                break;
            }
            case 156: {
                this.field_175336_F.useLavaLakes = bl;
                break;
            }
            case 161: {
                this.field_175336_F.useLavaOceans = bl;
                break;
            }
            case 210: {
                this.field_175336_F.useMonuments = bl;
            }
        }
        if (!this.field_175336_F.equals(this.field_175334_E)) {
            this.func_181031_a(true);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        if (guiButton.enabled) {
            switch (guiButton.id) {
                case 300: {
                    this.field_175343_i.chunkProviderSettingsJson = this.field_175336_F.toString();
                    this.mc.displayGuiScreen(this.field_175343_i);
                    break;
                }
                case 301: {
                    int n = 0;
                    while (n < this.field_175349_r.getSize()) {
                        Gui gui;
                        GuiPageButtonList.GuiEntry guiEntry = this.field_175349_r.getListEntry(n);
                        Gui gui2 = guiEntry.func_178022_a();
                        if (gui2 instanceof GuiButton) {
                            gui = (GuiButton)gui2;
                            if (gui instanceof GuiSlider) {
                                float f = ((GuiSlider)gui).func_175217_d() * (0.75f + this.random.nextFloat() * 0.5f) + (this.random.nextFloat() * 0.1f - 0.05f);
                                ((GuiSlider)gui).func_175219_a(MathHelper.clamp_float(f, 0.0f, 1.0f));
                            } else if (gui instanceof GuiListButton) {
                                ((GuiListButton)gui).func_175212_b(this.random.nextBoolean());
                            }
                        }
                        if ((gui = guiEntry.func_178021_b()) instanceof GuiButton) {
                            Gui gui3 = gui;
                            if (gui3 instanceof GuiSlider) {
                                float f = ((GuiSlider)gui3).func_175217_d() * (0.75f + this.random.nextFloat() * 0.5f) + (this.random.nextFloat() * 0.1f - 0.05f);
                                ((GuiSlider)gui3).func_175219_a(MathHelper.clamp_float(f, 0.0f, 1.0f));
                            } else if (gui3 instanceof GuiListButton) {
                                ((GuiListButton)gui3).func_175212_b(this.random.nextBoolean());
                            }
                        }
                        ++n;
                    }
                    return;
                }
                case 302: {
                    this.field_175349_r.func_178071_h();
                    this.func_175328_i();
                    break;
                }
                case 303: {
                    this.field_175349_r.func_178064_i();
                    this.func_175328_i();
                    break;
                }
                case 304: {
                    if (!this.field_175338_A) break;
                    this.func_175322_b(304);
                    break;
                }
                case 305: {
                    this.mc.displayGuiScreen(new GuiScreenCustomizePresets(this));
                    break;
                }
                case 306: {
                    this.func_175331_h();
                    break;
                }
                case 307: {
                    this.field_175339_B = 0;
                    this.func_175331_h();
                }
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_175349_r.handleMouseInput();
    }

    private void func_175331_h() throws IOException {
        switch (this.field_175339_B) {
            case 300: {
                this.actionPerformed((GuiListButton)this.field_175349_r.func_178061_c(300));
                break;
            }
            case 304: {
                this.func_175326_g();
            }
        }
        this.field_175339_B = 0;
        this.field_175340_C = true;
        this.func_175329_a(false);
    }

    public String func_175323_a() {
        return this.field_175336_F.toString().replace("\n", "");
    }
}

