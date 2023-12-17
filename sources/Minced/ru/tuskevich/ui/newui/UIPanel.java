// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.newui;

import java.util.Iterator;
import ru.tuskevich.util.math.MathUtility;
import ru.tuskevich.util.font.Fonts;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.render.GlowUtility;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import ru.tuskevich.util.math.HoveringUtil;
import ru.tuskevich.ui.newui.elements.Element;
import ru.tuskevich.util.animations.AnimationMath;
import net.minecraft.client.Minecraft;
import ru.tuskevich.modules.Module;
import ru.tuskevich.Minced;
import java.util.ArrayList;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.newui.elements.ElementModule;
import java.util.List;
import ru.tuskevich.modules.Type;

public class UIPanel
{
    public Type type;
    public float x;
    public float y;
    public float width;
    public boolean open;
    public List<ElementModule> elements;
    public float prevX;
    public float prevY;
    public boolean dragging;
    public float scroll;
    public float aScroll;
    public float animatedOFFSET;
    public float animatedOFFSET2;
    public float alphaBar;
    public TimerUtility timer;
    public float piska;
    
    public UIPanel(final Type type, final float x, final float y, final float width) {
        this.open = true;
        this.elements = new ArrayList<ElementModule>();
        this.animatedOFFSET = 200.0f;
        this.animatedOFFSET2 = 200.0f;
        this.timer = new TimerUtility();
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        for (final Module m : Minced.getInstance().manager.getModulesFromCategory(type)) {
            if (m != null) {
                this.elements.add(new ElementModule(m, this));
            }
        }
    }
    
    public void draw(final int mouseX, final int mouseY) {
        final int panelHeight = (int)(Minecraft.getMinecraft().displayHeight * 0.2f);
        this.aScroll = AnimationMath.fast(this.aScroll, this.scroll, 15.0f);
        float offset = this.aScroll;
        if (this.open) {
            for (final ElementModule e : this.elements) {
                e.x = this.x;
                e.y = this.y + 18.0f + offset;
                e.width = this.width;
                if (e.extended) {
                    for (final Element element : e.elements) {
                        offset += (float)element.getHeight();
                    }
                }
                offset += (float)e.getHeight();
            }
        }
        float guiOffset = 0.0f;
        if (this.open) {
            for (final ElementModule e2 : this.elements) {
                if (e2.extended) {
                    for (final Element element2 : e2.elements) {
                        guiOffset += (float)element2.getHeight();
                    }
                }
                guiOffset += (float)e2.getHeight();
            }
        }
        this.animatedOFFSET2 = AnimationMath.fast(this.animatedOFFSET2, guiOffset, 15.0f);
        if (HoveringUtil.isHovering(this.x, this.y + 16.0f, this.width, 200.0f, mouseX, mouseY)) {
            final int scroolO = Mouse.getDWheel();
            if (scroolO != 0) {
                this.timer.reset();
            }
            if (scroolO < 0) {
                this.scroll -= 15.0f;
            }
            else if (scroolO > 0) {
                this.scroll += 15.0f;
            }
            if (guiOffset > panelHeight) {
                this.scroll = MathHelper.clamp(this.scroll, -guiOffset + panelHeight, 0.0f);
            }
            else {
                this.scroll = 0.0f;
            }
        }
        final float finalOffset = Math.min((float)panelHeight, guiOffset);
        this.animatedOFFSET = AnimationMath.fast(this.animatedOFFSET, finalOffset, 15.0f);
        GlowUtility.drawGlow(this.x, this.y, this.width, 16.0f + this.animatedOFFSET + 2.0f, 15, new Color(15, 15, 15, Math.round(255.0f * ClickScreen.scroll)));
        RenderUtility.drawRound(this.x, this.y, this.width, 18.0f + this.animatedOFFSET + 2.0f, 4.0f, new Color(20, 20, 20, Math.round(255.0f * ClickScreen.scroll)));
        RenderUtility.drawRound(this.x + 3.0f, this.y + 15.0f, this.width - 6.0f, this.animatedOFFSET + 2.0f, 4.0f, new Color(10, 10, 10, Math.round(255.0f * ClickScreen.scroll)));
        if (this.open) {}
        Fonts.MONTSERRAT16.drawCenteredString(this.type.name(), this.x + this.width / 2.0f, this.y + 6.0f, new Color(255, 255, 255, Math.round(255.0f)).getRGB());
        this.alphaBar = AnimationMath.fast(this.alphaBar, this.timer.hasTimeElapsed(500L) ? 0.0f : 50.0f, 15.0f);
        if (this.open) {
            for (final ElementModule e3 : this.elements) {
                SmartScissor.push();
                SmartScissor.setFromComponentCoordinates(Math.round(this.x), Math.round(this.y + 18.0f), Math.round(this.width), Math.round(this.animatedOFFSET));
                e3.draw(mouseX, mouseY);
                SmartScissor.unset();
                SmartScissor.pop();
            }
        }
        if (guiOffset > panelHeight) {
            final float barHeight = 50.0f;
            final float barY = MathUtility.map(this.aScroll, -this.animatedOFFSET2 + panelHeight, 0.0f, 0.0f, panelHeight - barHeight);
            RenderUtility.drawRect(this.x + this.width - 1.0f, this.y + 17.0f + panelHeight - barHeight - barY, 1.0, barHeight, new Color(255, 255, 255, (int)this.alphaBar).getRGB());
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        float offset = this.aScroll;
        if (this.open) {
            for (final ElementModule e : this.elements) {
                if (e.extended) {
                    for (final Element element : e.elements) {
                        offset += (float)element.getHeight();
                    }
                }
                offset += (float)e.getHeight();
            }
        }
        if (HoveringUtil.isHovering(this.x, this.y, this.width, 16.0f, mouseX, mouseY) && mouseButton == 1) {
            this.open = !this.open;
        }
        if (this.open) {
            for (final ElementModule e : this.elements) {
                if ((offset > Minecraft.getMinecraft().displayHeight * 0.2f && HoveringUtil.isHovering(this.x, this.y + 18.0f, this.width, Minecraft.getMinecraft().displayHeight * 0.2f, mouseX, mouseY)) || offset <= Minecraft.getMinecraft().displayHeight * 0.2f) {
                    e.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
        if (HoveringUtil.isHovering(this.x, this.y, this.width, 16.0f, mouseX, mouseY) && mouseButton == 0) {
            this.dragging = true;
            this.prevX = mouseX - this.x;
            this.prevY = mouseY - this.y;
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.open) {
            for (final ElementModule e : this.elements) {
                e.mouseReleased(mouseX, mouseY, state);
            }
        }
        this.dragging = false;
    }
    
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.open) {
            for (final ElementModule e : this.elements) {
                e.keypressed(typedChar, keyCode);
            }
        }
    }
}
