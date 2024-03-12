// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.sidegui.forms;

import dev.tenacity.module.impl.render.HUDMod;
import java.awt.Color;
import dev.tenacity.Tenacity;
import dev.tenacity.utils.misc.HoveringUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.utils.tuples.Triplet;
import java.util.function.BiConsumer;
import dev.tenacity.ui.Screen;

public abstract class Form implements Screen
{
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;
    private final String title;
    private BiConsumer<String, String> uploadAction;
    private Triplet.TriConsumer<String, String, String> triUploadAction;
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        RoundedUtil.drawRound(this.x, this.y, this.width, this.height, 5.0f, ColorUtil.tripleColor(37, this.alpha));
        Form.tenacityBoldFont40.drawString(this.title, this.x + 5.0f, this.y + 3.0f, this.getTextColor());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
        if (!HoveringUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            Tenacity.INSTANCE.getSideGui().displayForm(null);
        }
    }
    
    public float getSpacing() {
        return 8.0f;
    }
    
    public Color getTextColor() {
        return ColorUtil.applyOpacity(Color.WHITE, this.alpha);
    }
    
    public Color getAccentColor() {
        return ColorUtil.applyOpacity(HUDMod.getClientColors().getFirst(), this.alpha);
    }
    
    public abstract void clear();
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getWidth() {
        return this.width;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public float getAlpha() {
        return this.alpha;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public BiConsumer<String, String> getUploadAction() {
        return this.uploadAction;
    }
    
    public Triplet.TriConsumer<String, String, String> getTriUploadAction() {
        return this.triUploadAction;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public void setWidth(final float width) {
        this.width = width;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
    
    public void setAlpha(final float alpha) {
        this.alpha = alpha;
    }
    
    public void setUploadAction(final BiConsumer<String, String> uploadAction) {
        this.uploadAction = uploadAction;
    }
    
    public void setTriUploadAction(final Triplet.TriConsumer<String, String, String> triUploadAction) {
        this.triUploadAction = triUploadAction;
    }
    
    public Form(final String title) {
        this.title = title;
    }
}
