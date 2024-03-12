// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.clickguis.modern.components;

import dev.tenacity.utils.tuples.Pair;
import org.lwjgl.input.Keyboard;
import dev.tenacity.module.Category;
import net.minecraft.client.renderer.GlStateManager;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.render.ColorUtil;
import org.lwjgl.opengl.GL11;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.render.RoundedUtil;
import java.awt.Color;
import dev.tenacity.utils.animations.impl.ElasticAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.module.Module;

public class ModuleRect extends Component
{
    private int searchScore;
    public final Module module;
    public Animation settingAnimation;
    public Animation hoverDescriptionAnimation;
    public Module binding;
    public float rectOffset;
    public boolean rightClicked;
    public boolean drawSettingThing;
    private Animation rectScaleAnimation;
    private Animation checkScaleAnimation;
    
    public ModuleRect(final Module module) {
        this.searchScore = 0;
        this.rectOffset = 0.0f;
        this.rightClicked = false;
        this.drawSettingThing = false;
        this.module = module;
    }
    
    @Override
    public void initGui() {
        (this.hoverDescriptionAnimation = new DecelerateAnimation(250, 1.0)).setDirection(Direction.BACKWARDS);
        (this.settingAnimation = new DecelerateAnimation(400, 1.0)).setDirection(Direction.BACKWARDS);
        (this.rectScaleAnimation = new DecelerateAnimation(250, 1.0)).setDirection(Direction.BACKWARDS);
        (this.checkScaleAnimation = new ElasticAnimation(550, 1.0, 3.8f, 2.0f, false)).setDirection(Direction.BACKWARDS);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.binding != null) {
            if (keyCode == 57 || keyCode == 1 || keyCode == 211) {
                this.binding.getKeybind().setCode(0);
            }
            else {
                this.binding.getKeybind().setCode(keyCode);
            }
            this.binding = null;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        RoundedUtil.drawRound(this.x + 0.5f, this.y + 0.5f, this.rectWidth - 1.0f, 34.0f, 5.0f, new Color(47, 49, 54));
        if (this.rectScaleAnimation == null) {
            System.out.println("CRAZXy " + this.module.getName());
        }
        this.rectScaleAnimation.setDirection(this.module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        this.checkScaleAnimation.setDirection(this.module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
        this.checkScaleAnimation.setDuration(this.module.isEnabled() ? 550 : 250);
        final Color clickModColor = Color.WHITE;
        final Color clickModColor2 = Color.WHITE;
        final HUDMod hudMod = Tenacity.INSTANCE.getModuleCollection().getModule(HUDMod.class);
        final Pair<Color, Color> colors = HUDMod.getClientColors();
        RoundedUtil.drawRound(this.x + 0.5f, this.y + 0.5f, 34.0f, 34.0f, 5.0f, new Color(68, 71, 78));
        RenderUtil.drawGoodCircle(this.x + 17.5f, this.y + 17.5f, 5.0f, new Color(47, 49, 54).getRGB());
        final float rectScale = this.rectScaleAnimation.getOutput().floatValue();
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x + 17.0f, this.y + 17.0f, 0.0f);
        GL11.glScaled((double)rectScale, (double)rectScale, 0.0);
        GL11.glTranslatef(-(this.x + 17.0f), -(this.y + 17.0f), 0.0f);
        GL11.glEnable(3042);
        final float width = 35.0f;
        final float height = 35.0f;
        RoundedUtil.drawGradientCornerLR(this.x + 0.5f, this.y + 0.5f, width - 1.0f, height - 1.0f, 5.0f, ColorUtil.applyOpacity(colors.getFirst(), rectScale), ColorUtil.applyOpacity(colors.getSecond(), rectScale));
        GL11.glPopMatrix();
        final float textX = this.x + (18.0f - FontUtil.iconFont35.getStringWidth("o") / 2.0f);
        final float textY = (float)(this.y + 18.5 - FontUtil.iconFont35.getHeight() / 2.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef(textX + 9.0f, textY + 9.0f, 0.0f);
        GL11.glScaled((double)Math.max(0.0f, this.checkScaleAnimation.getOutput().floatValue()), (double)Math.max(0.0f, this.checkScaleAnimation.getOutput().floatValue()), 0.0);
        GL11.glTranslatef(-(textX + 9.0f), -(textY + 9.0f), 0.0f);
        FontUtil.iconFont35.drawSmoothString("o", textX, textY, ColorUtil.applyOpacity(-1, this.checkScaleAnimation.getOutput().floatValue()));
        GL11.glPopMatrix();
        final boolean hoverModule = HoveringUtil.isHovering(this.x, this.y, this.rectWidth, 35.0f, mouseX, mouseY);
        this.hoverDescriptionAnimation.setDirection(hoverModule ? Direction.FORWARDS : Direction.BACKWARDS);
        this.hoverDescriptionAnimation.setDuration(hoverModule ? 300 : 400);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final float xStart = this.x + 55.0f + ModuleRect.tenacityFont24.getStringWidth(this.module.getName());
        float yVal = this.y + 17.5f - ModuleRect.tenacityFont18.getHeight() / 2.0f;
        if (this.module.getCategory().equals(Category.SCRIPTS)) {
            yVal -= 6.0f;
        }
        if (this.binding != this.module && (!this.hoverDescriptionAnimation.isDone() || this.hoverDescriptionAnimation.finished(Direction.FORWARDS))) {
            final float hover = this.hoverDescriptionAnimation.getOutput().floatValue();
            final float descWidth = 305.0f - (55.0f + ModuleRect.tenacityFont24.getStringWidth(this.module.getName()) + 15.0f);
            ModuleRect.tenacityFont18.drawWrappedText(this.module.getDescription(), xStart, yVal, new Color(128, 134, 141, (int)(255.0f * hover)).getRGB(), descWidth, 3.0f);
        }
        else if (this.binding == this.module) {
            ModuleRect.tenacityFont18.drawString("Currently bound to " + Keyboard.getKeyName(this.module.getKeybind().getCode()), xStart, yVal, new Color(128, 134, 141).getRGB());
        }
        if (this.module.getCategory().equals(Category.SCRIPTS)) {
            ModuleRect.tenacityFont24.drawString(this.module.getName(), this.x + 42.0f, this.y + (17.5f - ModuleRect.tenacityFont24.getHeight() / 2.0f) - 6.0f, -1);
            ModuleRect.tenacityFont18.drawString(this.module.getAuthor(), this.x + 42.0f, this.y + (17.5f - ModuleRect.tenacityFont24.getHeight() / 2.0f) + 9.0f, new Color(255, 65, 65).getRGB());
        }
        else {
            ModuleRect.tenacityFont24.drawString(this.module.getName(), this.x + 42.0f, this.y + 17.5f - ModuleRect.tenacityFont24.getHeight() / 2.0f, -1);
        }
        this.settingAnimation.setDirection(this.drawSettingThing ? Direction.FORWARDS : Direction.BACKWARDS);
        final int interpolateColorr = ColorUtil.interpolateColorsBackAndForth(40, 1, colors.getFirst(), colors.getSecond(), false).getRGB();
        RoundedUtil.drawRound(this.x + this.rectWidth - 14.5f, this.y + 0.5f, 14.0f, 34.0f, 5.0f, ColorUtil.interpolateColorC(new Color(47, 49, 54), new Color(interpolateColorr), this.settingAnimation.getOutput().floatValue()));
        RenderUtil.drawGoodCircle(this.x + this.rectWidth - 7.5, this.y + 7.5, 2.5f, -1);
        RenderUtil.drawGoodCircle(this.x + this.rectWidth - 7.5, this.y + 17.5, 2.5f, -1);
        RenderUtil.drawGoodCircle(this.x + this.rectWidth - 7.5, this.y + 27.5, 2.5f, -1);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (HoveringUtil.isHovering(this.x, this.y, this.rectWidth, 35.0f, mouseX, mouseY) && this.y > this.bigRecty + this.rectOffset && this.y < this.bigRecty + 255.0f) {
            if (button == 0) {
                this.module.toggleSilent();
            }
            if (button == 2) {
                this.binding = this.module;
            }
            this.rightClicked = (button == 1);
        }
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int button) {
        if (button == 0) {
            this.binding = null;
        }
    }
    
    public int getSearchScore() {
        return this.searchScore;
    }
    
    public void setSearchScore(final int searchScore) {
        this.searchScore = searchScore;
    }
}
