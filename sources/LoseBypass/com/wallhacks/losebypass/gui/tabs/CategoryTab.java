/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui.tabs;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.ModuleComponent;
import com.wallhacks.losebypass.gui.tabs.ClickGuiTab;
import com.wallhacks.losebypass.manager.SystemManager;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MathUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CategoryTab
extends ClickGuiTab {
    private final Animation animation = new Animation(50.0f, 0.015f);
    public Module.Category category;
    ArrayList<ModuleComponent> modules = new ArrayList();
    boolean flag = true;
    double scroll = 0.0;
    double smoothScroll = 0.0;
    double height;
    boolean dragging = false;
    int draggingOffset;

    public CategoryTab(Module.Category category) {
        super(category.getName(), new ResourceLocation("textures/categoryicons/" + category.getName().toLowerCase() + ".png"));
        this.category = category;
        SystemManager.getModules().forEach(module -> {
            if (module.getCategory() != category) return;
            this.modules.add(new ModuleComponent((Module)module));
        });
    }

    @Override
    public void init() {
        this.animation.forceValue(50.0f);
    }

    @Override
    public void drawTab(int mouseX, int mouseY, int click, int posX, int posY, double deltaTime) {
        if (mouseX > posX && mouseY > posY && mouseY < posX + 400 && mouseX < posX + 440 && !this.dragging) {
            this.scroll = -((double)Mouse.getDWheel() * 0.3) + this.scroll;
        }
        this.scroll = Math.max(0.0, Math.min(this.scroll, this.height - 380.0));
        if (this.height > 380.0) {
            double h = 380.0 / this.height;
            double space = this.height - 380.0;
            if (this.dragging) {
                if (Mouse.isButtonDown((int)0)) {
                    int x = (int)(380.0 * (380.0 / Math.max(380.0, this.height + 0.01)));
                    this.scroll = (double)(mouseY - posY - this.draggingOffset - x / 2) / (double)(380 - x) * (this.height - 380.0);
                    this.scroll = Math.max(0.0, Math.min(this.scroll, this.height - 380.0));
                } else {
                    this.dragging = false;
                }
            }
            double c = this.scroll / space;
            int center = (int)(c * space);
            center = (int)((double)center + (200.0 - space / 2.0));
            int size = (int)(h * 380.0 / 2.0);
            GuiUtil.rounded(posX + 425, posY + center - size, posX + 435, posY + center + size, ClickGui.mainColor(), 3);
            if (click == 0 && mouseX > posX + 425 && mouseX < posX + 435 && mouseY > posY + center - size && mouseY < posY + center + size && !this.dragging) {
                this.draggingOffset = mouseY - posY - center;
                LoseBypass.sendInfo(this.draggingOffset + "");
                this.dragging = true;
            }
        }
        this.smoothScroll = MathUtil.lerp(this.smoothScroll, this.scroll, deltaTime * 0.02);
        this.animation.update(0.0f, deltaTime);
        double heightL = 10.0 + (double)posY - (double)this.animation.value();
        double heightR = 10.0 + (double)posY - (double)this.animation.value();
        GL11.glEnable((int)3089);
        Iterator<ModuleComponent> iterator = this.modules.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                GL11.glDisable((int)3089);
                this.height = Math.max(heightL, heightR);
                this.height -= (double)(10 + posY);
                this.flag = false;
                return;
            }
            ModuleComponent module = iterator.next();
            ClickGui.maxOffset = 380;
            ClickGui.minOffset = posY + 10;
            GuiUtil.glScissor(posX, ClickGui.minOffset, 440, ClickGui.maxOffset);
            if (heightL <= heightR) {
                if (this.flag || !ClickGui.animation.done()) {
                    module.setPosition(posX + 10, heightL);
                }
                heightL += (double)module.drawComponent(posX + 10, heightL, (int)this.smoothScroll, deltaTime, click, mouseX, mouseY);
                continue;
            }
            if (this.flag || !ClickGui.animation.done()) {
                module.setPosition(posX + 220, heightR);
            }
            heightR += (double)module.drawComponent(posX + 220, heightR, (int)this.smoothScroll, deltaTime, click, mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        this.modules.forEach(moduleComponent -> moduleComponent.keyTyped(typedChar, keyCode));
    }
}

