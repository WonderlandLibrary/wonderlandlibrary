/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.ui.click.buttons;

import java.util.ArrayList;
import me.AveReborn.Client;
import me.AveReborn.ui.click.ClickMenu;
import me.AveReborn.ui.click.buttons.UIPopUPChooseButton;
import me.AveReborn.util.RenderUtil;
import me.AveReborn.util.fontRenderer.FontManager;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import me.AveReborn.util.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Timer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class UIPopUPButton {
    private float x;
    private float y;
    private float currentRadius;
    private float minRadius;
    private float maxRadius;
    private float menuSizeRadius;
    private boolean open;
    private boolean animateUp;
    private boolean animateDown;
    private ClientEventHandler mouseClickedPopUpMenu;
    private ArrayList<UIPopUPChooseButton> popUpButtons = new ArrayList();
    private UIPopUPChooseButton openButton = null;
    private ClickMenu menu;
    private boolean now;

    public UIPopUPButton(float x2, float y2, float minRadius, float maxRadius) {
        this.x = x2;
        this.y = y2;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.currentRadius = maxRadius;
        this.mouseClickedPopUpMenu = new ClientEventHandler();
        this.popUpButtons.add(new UIPopUPChooseButton("ClickGUI", (int)x2 + (int)maxRadius, 0, "Client/menu_icon.png"));
        int yAxis = (int)y2 - 60;
        for (UIPopUPChooseButton button : this.popUpButtons) {
            button.setY(yAxis);
            yAxis -= 40;
        }
        this.openButton = this.popUpButtons.get(0);
        this.menu = new ClickMenu();
    }

    public void draw(int mouseX, int mouseY) {
        ScaledResolution sc2 = new ScaledResolution(Minecraft.getMinecraft());
        UnicodeFontRenderer font = Client.instance.fontMgr.simpleton30;
        boolean hovering = this.isHovering(mouseX, mouseY) && this.currentRadius == this.maxRadius;
        this.y = sc2.getScaledHeight() - 10;
        int yAxis = (int)this.y - 50;
        for (UIPopUPChooseButton button : this.popUpButtons) {
            button.setY(yAxis);
            yAxis -= 30;
        }
        this.animate();
        GL11.glPushMatrix();
        float scale = this.currentRadius / this.maxRadius;
        float xMid = this.x + this.maxRadius - 0.5f;
        float yMid = (float)sc2.getScaledHeight() - this.maxRadius - 10.0f;
        GL11.glTranslated(xMid, yMid, 0.0);
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(- xMid, - yMid, 0.0);
        if (this.mouseClickedPopUpMenu.canExcecute(Mouse.isButtonDown(0)) && hovering) {
            this.animateDown = true;
        }
        if (this.currentRadius > this.maxRadius / 2.0f) {
            // empty if block
        }
        GL11.glPopMatrix();
        int rad = 0;
        for (UIPopUPChooseButton button : this.popUpButtons) {
            if (this.open) {
                button.draw(mouseX, mouseY);
                if (!button.clicked(mouseX, mouseY)) continue;
                this.openButton = button;
                continue;
            }
            if (rad == 0) {
                rad = (int)(- button.maxRadius);
            }
            button.currentRadius = rad;
            rad = (int)((float)rad - button.maxRadius);
        }
        if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
            float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
            this.menu.draw(mouseX, mouseY);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
            this.menu.mouseClick(mouseX, mouseY);
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
            this.menu.mouseRelease(mouseX, mouseY);
        }
    }

    private void animate() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        float add2 = RenderUtil.delta * 45.0f;
        float menuAdd = RenderUtil.delta * 1500.0f;
        int maxMenuSize = res.getScaledWidth() + (int)((float)res.getScaledWidth() * 0.25f);
        if (this.open && !this.animateDown) {
            float f2 = this.menuSizeRadius + menuAdd > (float)maxMenuSize ? (float)maxMenuSize : (this.menuSizeRadius = this.menuSizeRadius + menuAdd);
            this.menuSizeRadius = f2;
        } else {
            this.now = true;
            float f3 = this.menuSizeRadius = this.menuSizeRadius <= (float)maxMenuSize / 1.2631578f ? 0.0f : (this.menuSizeRadius = this.menuSizeRadius - menuAdd);
        }
        if (this.animateDown) {
            if (this.currentRadius - add2 > this.minRadius) {
                this.currentRadius -= add2;
            } else {
                this.currentRadius = this.minRadius;
                this.animateDown = false;
                this.animateUp = true;
                this.open = !this.open;
            }
        } else if (this.animateUp) {
            if (this.currentRadius + add2 < this.maxRadius) {
                this.currentRadius += add2;
            } else {
                this.currentRadius = this.maxRadius;
                this.animateUp = false;
            }
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        if ((float)mouseX >= this.x && (float)mouseX <= this.x + this.maxRadius * 2.0f && (float)mouseY >= this.y - this.maxRadius * 2.0f && (float)mouseY <= this.y) {
            return true;
        }
        return false;
    }
}

