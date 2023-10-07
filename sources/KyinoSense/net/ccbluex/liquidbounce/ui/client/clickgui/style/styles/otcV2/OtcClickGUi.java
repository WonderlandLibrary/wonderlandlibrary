/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import me.report.liquidware.utils.verify.WbxMain;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.CategoryScreen;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.OtcScroll;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.otcV2.Utils.animations.impl.EaseBackIn;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RoundedUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class OtcClickGUi
extends GuiScreen {
    private static float mainx;
    private Animation openingAnimation;
    private EaseBackIn fadeAnimation;
    private float x;
    private static float mainy;
    private float hight;
    private int x2;
    private int y2;
    private boolean dragging;
    private final List<CategoryScreen> tabs;

    public int sHeight() {
        return this.field_146295_m * 2;
    }

    public OtcClickGUi() {
        mainx = 320.0f;
        this.x = 0.0f;
        mainy = 130.0f;
        this.hight = 120.0f;
        this.tabs = new ArrayList<CategoryScreen>();
        for (ModuleCategory category : ModuleCategory.values()) {
            this.tabs.add(new CategoryScreen(category, this.x));
            this.x += (float)(Fonts.flux.func_78256_a(this.newcatename(category)) + 10);
        }
    }

    public String newcatename(ModuleCategory moduleCategory) {
        if (moduleCategory.getDisplayName().equals("Combat")) {
            return "a";
        }
        if (moduleCategory.getDisplayName().equals("Player")) {
            return "d";
        }
        if (moduleCategory.getDisplayName().equals("Movement")) {
            return "c";
        }
        if (moduleCategory.getDisplayName().equals("Render")) {
            return "J";
        }
        if (moduleCategory.getDisplayName().equals("World")) {
            return "e";
        }
        if (moduleCategory.getDisplayName().equals("Other")) {
            return "g";
        }
        return "";
    }

    public void func_73866_w_() {
        WbxMain.getInstance().getSideGui().initGui();
        super.func_73866_w_();
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        int guiColor = ClickGUI.generateColor().getRGB();
        try {
            if (this.dragging) {
                mainx = this.x2 + mouseX;
                mainy = this.y2 + mouseY;
            }
            ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
            RenderUtils.drawRect(0.0f, 0.0f, (float)scaledResolution.func_78326_a(), (float)scaledResolution.func_78328_b(), new Color(0, 0, 0, 120).getRGB());
            RoundedUtil.drawRound(mainx, mainy, 290.0f, this.hight + 180.0f, 3.0f, new Color(44, 47, 56));
            RoundedUtil.drawRound(mainx, mainy - 50.0f, 290.0f, this.hight - 80.0f, 3.0f, new Color(44, 47, 56));
            RoundedUtil.drawGradientHorizontal(mainx, mainy - 50.0f, 290.0f, this.hight - 116.0f, 3.0f, new Color(guiColor), new Color(guiColor));
            Fonts.fontTahoma.drawString("KyinoSense", mainx + 11.0f, mainy - 31.0f, new Color(255, 255, 255).getRGB());
            RoundedUtil.drawRound(mainx + 64.0f, mainy - 35.0f, 0.5f, this.hight - 105.0f, 1.0f, new Color(255, 255, 255, 150));
            CategoryScreen selectedTab = this.getSelectedTab();
            if (selectedTab == null) {
                this.field_146297_k.field_71466_p.func_78276_b("----------------", (int)mainx + 109, (int)mainy + 40, new Color(255, 255, 255).getRGB());
                this.field_146297_k.field_71466_p.func_78276_b(" Ready for skid", (int)mainx + 109, (int)mainy + 50, new Color(255, 255, 255).getRGB());
                this.field_146297_k.field_71466_p.func_78276_b("----------------", (int)mainx + 109, (int)mainy + 60, new Color(255, 255, 255).getRGB());
                this.field_146297_k.field_71466_p.func_78276_b("KyinoClient", (int)mainx + 107, (int)mainy + 75, new Color(255, 255, 255).getRGB());
            }
            this.tabs.forEach(s -> s.drawScreen(mouseX, mouseY));
            super.func_73863_a(mouseX, mouseY, partialTicks);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
        CategoryScreen selectedTab;
        if (mouseButton == 0) {
            for (CategoryScreen categoryScreen : this.tabs) {
                if (!categoryScreen.isHovered(mouseX, mouseY)) continue;
                for (CategoryScreen other : this.tabs) {
                    other.setSelected(false);
                }
                categoryScreen.setSelected(true);
            }
        }
        if (this.isHovered(mouseX, mouseY)) {
            this.x2 = (int)(mainx - (float)mouseX);
            this.y2 = (int)(mainy - (float)mouseY);
            this.dragging = true;
        }
        if ((selectedTab = this.getSelectedTab()) != null) {
            selectedTab.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public CategoryScreen getSelectedTab() {
        return this.tabs.stream().filter(CategoryScreen::isSelected).findAny().orElse(null);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isHovered(int mouseX, int mouseY) {
        if (!((float)mouseX >= mainx)) return false;
        if (!((float)mouseX <= mainx + 45.0f + 105.0f + 270.0f)) return false;
        if (!((float)mouseY >= mainy - 50.0f - 7.0f)) return false;
        if (!((float)mouseY <= mainy - 50.0f + 20.0f)) return false;
        return true;
    }

    public static OtcScroll scroll() {
        int mouse = Mouse.getDWheel();
        if (mouse > 0) {
            return OtcScroll.UP;
        }
        if (mouse < 0) {
            return OtcScroll.DOWN;
        }
        return null;
    }

    protected void func_146286_b(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.dragging = false;
        }
        this.tabs.forEach(e -> e.mouseReleased(mouseX, mouseY, state));
        super.func_146286_b(mouseX, mouseY, state);
    }

    protected void func_73869_a(char typedChar, int keyCode) throws IOException {
        this.tabs.forEach(e -> e.keyTyped(typedChar, keyCode));
        super.func_73869_a(typedChar, keyCode);
    }

    public static float getMainx() {
        return mainx;
    }

    public static float getMainy() {
        return mainy;
    }

    public float getX2() {
        return this.x2;
    }

    public float getY2() {
        return this.y2;
    }

    public int getHeight() {
        return (int)this.hight;
    }
}

