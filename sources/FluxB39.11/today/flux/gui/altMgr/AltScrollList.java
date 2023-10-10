package today.flux.gui.altMgr;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import today.flux.gui.clickgui.classic.GuiRenderUtils;
import today.flux.gui.clickgui.classic.RenderUtil;
import today.flux.gui.fontRenderer.FontManager;
import today.flux.utility.AnimationUtils;

import java.awt.*;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11.*;

public class AltScrollList {

    public float x;
    public float y;
    public float width;
    public float height;
    public int mouseX;
    public int mouseY;

    public Consumer<Alt> onSelected;
    public Consumer<Alt> onDoubleClicked;

    public int selectedAlt;

    public long lastClicked;

    public GuiAltMgr parent;

    //Scroller
    public float scrollY = 0;
    public float scrollAni = 0;
    public float minY = -100;

    public AltScrollList(GuiAltMgr parent, Consumer<Alt> onSelected, Consumer<Alt> onDoubleClicked) {
        this.parent = parent;
        this.onSelected = onSelected;
        this.onDoubleClicked = onDoubleClicked;
    }

    public void doDraw(float x, float y, float width, float height, int mouseX, int mouseY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        GuiRenderUtils.drawRoundedRect(x, y, width, height, 2f, new Color(0, 0, 0, 88).getRGB(), .5f, 0xff2f3136);

        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            minY = height - 4;
        }

        this.scrollAni = AnimationUtils.smoothAnimation(this.scrollAni, scrollY, 50, .3f);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor((int) x + 2, (int) y + 4, width - 4, height - 8);
        float startY = y + 4 + this.scrollAni;
        float buttonHeight = 30;
        float totalY = 0;
        for (int i = 0; i < parent.alts.size(); ++i) {
            Alt alt = parent.alts.get(i);
            //Save Performance
            if(y < startY + buttonHeight + 5 && startY < y + height) {
                boolean highlight = this.selectedAlt == i;
                boolean drawHover = RenderUtil.isHoveringBound(mouseX, mouseY, x + 4, startY, width - 8, buttonHeight);
                GuiRenderUtils.drawRoundedRect(x + 4, startY, width - 8, buttonHeight, 2f, highlight ? 0xff4286f5 : 0xff32353b, .5f, highlight ? 0xff4286f5 : 0xff32353b);

                drawAltFace(alt.getNameOrEmail(), x + 8, startY + 3, 24, 24);
                FontManager.sans18.drawLimitedString(alt.getNameOrEmail(), x + 34, startY + 2, 0xffffffff, 375);
                FontManager.sans18.drawLimitedString((alt.isCracked() ? "Cracked" : "Premium") + (alt.isStarred() ? ", Starred" : ""), x + 34, startY + 14, 0xffffffff, 375);

                if(drawHover) {
                    GuiRenderUtils.drawRoundedRect(x + 4, startY, width - 8, buttonHeight, 2f, 0x33000000, .5f, 0x33000000);
                }
            }

            startY += buttonHeight + 5;
            totalY += buttonHeight + 5;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            minY -= totalY;
        }

        if(totalY > this.height - 8) {
            float viewable = this.height;
            float progress = MathHelper.clamp_float(-this.scrollAni / -this.minY, 0, 1);
            float ratio = (viewable / totalY) * viewable;
            float barHeight = Math.max(ratio, 20f);
            float position = progress * (viewable - barHeight);

            GuiRenderUtils.drawRoundedRect(this.x + this.width + 4, this.y, 4, this.height, 2, 0xff2e3338, .5f, 0xff2e3338);
            GuiRenderUtils.drawRoundedRect(this.x + this.width + 4, this.y + position, 4, barHeight, 2, 0xff202225, .5f, 0xff202225);
        }
    }

    public void handleMouseInput() {
        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            scrollY += Mouse.getEventDWheel() / 5f;

            if (scrollY <= minY)
                scrollY = minY;
            if (scrollY >= 0f)
                scrollY = 0f;
        }
    }

    public void onClick(int mouseX, int mouseY, int mouseButton) {
        float startY = y + 4 + this.scrollAni;
        if(RenderUtil.isHoveringBound(mouseX, mouseY, x, y, width, height)) {
            float buttonHeight = 30;
            for (int i = 0; i < parent.alts.size(); ++i) {
                Alt alt = parent.alts.get(i);

                boolean isHovered = RenderUtil.isHoveringBound(mouseX, mouseY, x + 4, startY, width - 8, buttonHeight);
                if(isHovered) {
                    boolean isDoubleClicked = i == this.selectedAlt && Minecraft.getSystemTime() - this.lastClicked < 250L;

                    this.onSelected.accept(alt);
                    if(isDoubleClicked) {
                        this.onDoubleClicked.accept(alt);
                    }

                    this.selectedAlt = i;
                    this.lastClicked = Minecraft.getSystemTime();
                    break;
                }
                startY += buttonHeight + 5;
            }
        }
    }

    public int getSelectedIndex() {
        return selectedAlt;
    }

    public void drawAltFace(String name, float x, float y, float w, float h) {
        try {
            AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(Minecraft.getMinecraft().getResourceManager());
            Minecraft.getMinecraft().getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
            Tessellator var3 = Tessellator.getInstance();
            WorldRenderer var4 = var3.getWorldRenderer();
            glEnable(GL_BLEND);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            // Face
            double fw = 32;
            double fh = 32;
            double u = 32;
            double v = 32;
            var4.begin(7, DefaultVertexFormats.POSITION_TEX);
            var4.pos((double) x + 0, (double) y + h, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + fh) * 0.00390625F).endVertex();
            var4.pos((double) x + w, (double) y + h, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + fh) * 0.00390625F).endVertex();
            var4.pos((double) x + w, (double) y + 0, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + 0) * 0.00390625F).endVertex();
            var4.pos((double) x + 0, (double) y + 0, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + 0) * 0.00390625F).endVertex();
            var3.draw();
            // Hat
            fw = 32;
            fh = 32;
            u = 160;
            v = 32;
            var4.begin(7, DefaultVertexFormats.POSITION_TEX);
            var4.pos((double) x + 0, (double) y + h, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + fh) * 0.00390625F).endVertex();
            var4.pos((double) x + w, (double) y + h, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + fh) * 0.00390625F).endVertex();
            var4.pos((double) x + w, (double) y + 0, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + 0) * 0.00390625F).endVertex();
            var4.pos((double) x + 0, (double) y + 0, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + 0) * 0.00390625F).endVertex();
            var3.draw();
            glDisable(GL_BLEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
