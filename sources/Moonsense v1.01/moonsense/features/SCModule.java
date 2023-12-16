// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features;

import moonsense.config.ModuleConfig;
import moonsense.event.EventBus;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.utils.ColorObject;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import moonsense.enums.ModuleCategory;
import moonsense.settings.Setting;
import java.util.Set;

public abstract class SCModule
{
    public final String displayName;
    public final String description;
    private final String iconName;
    private final int textureIndex;
    public final Set<Setting> settings;
    private Boolean textureExists;
    private ModuleCategory moduleCategory;
    private SCModule parentModule;
    private boolean serverDisabled;
    public final Minecraft mc;
    
    public SCModule(final String displayName, final String description) {
        this(displayName, description, -1, true);
    }
    
    public SCModule(final String displayName, final String description, final int textureIndex) {
        this(displayName, description, textureIndex, true);
    }
    
    public SCModule(final String displayName, final String description, final int textureIndex, final boolean autoadd) {
        this.mc = Minecraft.getMinecraft();
        this.settings = (Set<Setting>)Sets.newLinkedHashSet();
        this.displayName = displayName;
        this.description = description;
        this.iconName = displayName.replace(" ", "").toLowerCase();
        this.textureIndex = textureIndex;
        if (autoadd) {
            ModuleManager.INSTANCE.modules.add(this);
        }
    }
    
    public String getKey() {
        return this.displayName.replace(" ", "").toUpperCase();
    }
    
    public ResourceLocation getIcon() {
        return new ResourceLocation("streamlined/icons/modules/" + this.iconName + ".png");
    }
    
    public boolean hasIcon() {
        if (this.textureExists == null) {
            this.textureExists = false;
            try {
                Minecraft.getMinecraft().getResourceManager().getAllResources(this.getIcon());
                this.textureExists = true;
            }
            catch (Exception ex) {}
        }
        return this.iconName != null && !this.iconName.isEmpty() && this.getIcon() != null && this.textureExists != null && this.textureExists;
    }
    
    public boolean isRender() {
        return this instanceof SCAbstractRenderModule;
    }
    
    public int getTextureIndex() {
        return this.textureIndex;
    }
    
    public static int getColor(final ColorObject colorObject) {
        if (!colorObject.isChroma()) {
            return colorObject.getColor();
        }
        final float v = colorObject.getActualChromaSpeed();
        final float[] i = new Color(Color.HSBtoRGB((float)((System.currentTimeMillis() - 0.0 - 0.0) % v) / v, 0.8f, 0.8f)).getRGBColorComponents(null);
        final Color c = new Color(i[0], i[1], i[2], (float)(colorObject.getAlpha() / 255));
        return c.getRGB();
    }
    
    public void drawString(final String text, final float xIn, final float y, final ColorObject colorObject, final boolean shadow) {
        if (colorObject.isChroma()) {
            float x = xIn;
            char[] charArray;
            for (int length = (charArray = text.toCharArray()).length, i = 0; i < length; ++i) {
                final char c = charArray[i];
                final long dif = (long)(x * 10.0f - y * 10.0f);
                final long l = System.currentTimeMillis() - dif;
                final float chromaSpeed = (float)((100 - colorObject.getChromaSpeed()) * 100);
                GuiUtils.drawString(String.valueOf(c), x, y, Color.HSBtoRGB(l % (int)chromaSpeed / chromaSpeed, 0.8f, 0.8f), shadow);
                x += this.mc.fontRendererObj.getStringWidth(String.valueOf(c));
            }
        }
        else {
            GuiUtils.drawString(text, xIn, y, colorObject.getColor(), shadow);
        }
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final ColorObject colorObject, final boolean shadow) {
        this.drawString(text, x - this.mc.fontRendererObj.getStringWidth(String.valueOf(text)) / 2.0f, y, colorObject, shadow);
    }
    
    public void setCategory(final ModuleCategory category) {
        this.moduleCategory = category;
    }
    
    public boolean isChildModule() {
        return this.parentModule != null;
    }
    
    public SCModule getParentModule() {
        return this.parentModule;
    }
    
    public void setParentModule(final SCModule parentModule) {
        this.parentModule = parentModule;
    }
    
    public abstract ModuleCategory getCategory();
    
    public String getAuthor() {
        return "Moonsense Client";
    }
    
    public void initMod() {
    }
    
    public void onEnable() {
        MoonsenseClient.INSTANCE.getEventManager();
        EventBus.register(this);
    }
    
    public void onDisable() {
        MoonsenseClient.INSTANCE.getEventManager();
        EventBus.unregister(this);
    }
    
    public void serverDisable() {
        this.serverDisabled = true;
        ModuleConfig.INSTANCE.setEnabled(this, false);
        this.onDisable();
    }
    
    public void serverEnable() {
        this.serverDisabled = false;
        ModuleConfig.INSTANCE.setEnabled(this, true);
        this.onEnable();
    }
    
    public boolean isServerDisabled() {
        return this.serverDisabled;
    }
    
    public boolean isEnabled() {
        return ModuleConfig.INSTANCE.isEnabled(this);
    }
    
    public void setEnabled(final boolean enabled) {
        ModuleConfig.INSTANCE.setEnabled(this, enabled);
    }
    
    public void toggleEnabled() {
        this.setEnabled(!this.isEnabled());
    }
    
    public boolean isNewModule() {
        return false;
    }
}
