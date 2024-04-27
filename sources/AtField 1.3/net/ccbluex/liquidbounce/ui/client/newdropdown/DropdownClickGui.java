/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.newdropdown;

import java.util.ArrayList;
import java.util.List;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.newdropdown.MainScreen;
import net.ccbluex.liquidbounce.ui.client.newdropdown.impl.SettingComponents;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Animation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.Direction;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.DecelerateAnimation;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.animations.impl.EaseBackIn;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.normal.Main;
import net.ccbluex.liquidbounce.ui.client.newdropdown.utils.render.DrRenderUtils;
import net.ccbluex.liquidbounce.utils.ClientMain;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class DropdownClickGui
extends WrappedGuiScreen {
    private final IResourceLocation hudIcon = classProvider.createResourceLocation("atfield".toLowerCase() + "/custom_hud_icon.png");
    private Animation openingAnimation;
    private DecelerateAnimation configHover;
    public List categoryPanels;
    private EaseBackIn fadeAnimation;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        boolean bl = ClientMain.getInstance().getSideGui().focused;
        ClientMain.getInstance().getSideGui().mouseClicked(n, n2, n3);
        if (!bl) {
            this.categoryPanels.forEach(arg_0 -> DropdownClickGui.lambda$mouseClicked$2(n, n2, n3, arg_0));
        }
    }

    private static void lambda$mouseReleased$3(int n, int n2, int n3, MainScreen mainScreen) {
        mainScreen.mouseReleased(n, n2, n3);
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        boolean bl = ClientMain.getInstance().getSideGui().focused;
        ClientMain.getInstance().getSideGui().mouseReleased(n, n2, n3);
        if (!bl) {
            this.categoryPanels.forEach(arg_0 -> DropdownClickGui.lambda$mouseReleased$3(n, n2, n3, arg_0));
        }
    }

    private static void lambda$mouseClicked$2(int n, int n2, int n3, MainScreen mainScreen) {
        mainScreen.mouseClicked(n, n2, n3);
    }

    private void lambda$drawScreen$1(int n, int n2, int n3, int n4, float f, int n5) {
        for (MainScreen mainScreen : this.categoryPanels) {
            mainScreen.drawScreen(n, n2);
        }
        ClientMain.getInstance().getSideGui().drawScreen(n3, n4, f, n5);
    }

    @Override
    public void keyTyped(char c, int n) {
        if (n == 1) {
            this.openingAnimation.setDirection(Direction.BACKWARDS);
            ClientMain.getInstance().getSideGui().focused = false;
            this.fadeAnimation.setDirection(this.openingAnimation.getDirection());
        }
        ClientMain.getInstance().getSideGui().keyTyped(c, n);
        this.categoryPanels.forEach(arg_0 -> DropdownClickGui.lambda$keyTyped$0(c, n, arg_0));
    }

    @Override
    public void initGui() {
        if (this.categoryPanels == null || Main.reloadModules) {
            this.categoryPanels = new ArrayList(this){
                final DropdownClickGui this$0;
                {
                    this.this$0 = dropdownClickGui;
                    for (ModuleCategory moduleCategory : ModuleCategory.values()) {
                        this.add(new MainScreen(moduleCategory));
                    }
                }
            };
            Main.reloadModules = false;
        }
        ClientMain.getInstance().getSideGui().initGui();
        this.fadeAnimation = new EaseBackIn(400, 1.0, 2.0f);
        this.openingAnimation = new EaseBackIn(400, (double)0.4f, 2.0f);
        this.configHover = new DecelerateAnimation(250, 1.0);
        for (MainScreen mainScreen : this.categoryPanels) {
            mainScreen.animation = this.fadeAnimation;
            mainScreen.openingAnimation = this.openingAnimation;
            mainScreen.initGui();
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        if (Mouse.isButtonDown((int)0) && n >= 5 && n <= 50 && n2 <= this.representedScreen.getHeight() - 5 && n2 >= this.representedScreen.getHeight() - 50) {
            mc.displayGuiScreen(classProvider.wrapGuiScreen(new GuiHudDesigner()));
        }
        RenderUtils.drawImage(this.hudIcon, 9, this.representedScreen.getHeight() - 41, 32, 32);
        if (Main.reloadModules) {
            this.initGui();
        }
        if (this.openingAnimation.isDone() && this.openingAnimation.getDirection().equals((Object)Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            return;
        }
        boolean bl = ClientMain.getInstance().getSideGui().focused;
        int n3 = bl ? 0 : n;
        int n4 = bl ? 0 : n2;
        IScaledResolution iScaledResolution = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createScaledResolution(mc);
        boolean bl2 = DrRenderUtils.isHovering(this.representedScreen.getWidth() - 120, this.representedScreen.getHeight() - 65, 75.0f, 25.0f, n3, n4);
        this.configHover.setDirection(bl2 ? Direction.FORWARDS : Direction.BACKWARDS);
        int n5 = Math.max(0, Math.min(255, (int)(255.0 * this.fadeAnimation.getOutput())));
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        SettingComponents.scale = (float)(this.openingAnimation.getOutput() + (double)0.6f);
        DrRenderUtils.scale((float)iScaledResolution.getScaledWidth() / 2.0f, (float)iScaledResolution.getScaledHeight() / 2.0f, (float)this.openingAnimation.getOutput() + 0.6f, () -> this.lambda$drawScreen$1(n3, n4, n, n2, f, n5));
    }

    private static void lambda$keyTyped$0(char c, int n, MainScreen mainScreen) {
        mainScreen.keyTyped(c, n);
    }
}

