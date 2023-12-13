// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.io.IOException;
import com.elementars.eclient.module.render.ImageESP;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import javax.imageio.ImageIO;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import com.elementars.eclient.guirewrite.Element;

public class Logo extends Element
{
    private ResourceLocation logo;
    
    public Logo() {
        super("Logo");
        this.onLoad();
    }
    
    @Override
    public void onEnable() {
        this.width = 32.0;
        this.height = 32.0;
    }
    
    @Override
    public void onRender() {
        Logo.mc.renderEngine.bindTexture(this.logo);
        GlStateManager.color(255.0f, 255.0f, 255.0f);
        Gui.drawScaledCustomSizeModalRect((int)this.x + 4, (int)this.y + 4, 7.0f, 7.0f, (int)this.width - 7, (int)this.height - 7, (int)this.width, (int)this.height, (float)this.width, (float)this.height);
    }
    
    private void onLoad() {
        BufferedImage image = null;
        try {
            if (this.getFile("/images/xulutransparent.png") != null) {
                image = this.getImage(this.getFile("/images/xulutransparent.png"), ImageIO::read);
            }
            if (image == null) {
                this.LOGGER.warn("Failed to load image");
            }
            else {
                final DynamicTexture dynamicTexture = new DynamicTexture(image);
                dynamicTexture.loadTexture(Logo.mc.getResourceManager());
                this.logo = Logo.mc.getTextureManager().getDynamicTextureLocation("XULU_LOGO_TRANSPARENT", dynamicTexture);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private InputStream getFile(final String string) {
        return ImageESP.class.getResourceAsStream(string);
    }
    
    private <T> BufferedImage getImage(final T source, final ThrowingFunction<T, BufferedImage> readFunction) {
        try {
            return readFunction.apply(source);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    @FunctionalInterface
    private interface ThrowingFunction<T, R>
    {
        R apply(final T p0) throws IOException;
    }
}
