// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.hud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import java.awt.image.BufferedImage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.apache.commons.lang3.Validate;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.TextureUtil;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.base64.Base64;
import io.netty.buffer.Unpooled;
import com.google.common.base.Charsets;
import net.minecraft.client.renderer.GlStateManager;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCServerJoinEvent;
import moonsense.ui.utils.GuiUtils;
import net.minecraft.client.resources.I18n;
import moonsense.features.SCModule;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.multiplayer.ServerData;
import moonsense.settings.Setting;
import moonsense.features.SCDefaultRenderModule;

public class ServerOverlayModule extends SCDefaultRenderModule
{
    private final Setting showIcon;
    private final Setting overrideDefaultTextColors;
    private final Setting borderSurroundsImage;
    public ServerData serverData;
    public ResourceLocation resource;
    public DynamicTexture texture;
    private static final ResourceLocation UNKNOWN_SERVER;
    
    static {
        UNKNOWN_SERVER = new ResourceLocation("textures/misc/unknown_server.png");
    }
    
    public ServerOverlayModule() {
        super("Server Display", "Show the name of the server you are connected to on the HUD.");
        new Setting(this, "Pack Display Settings");
        this.showIcon = new Setting(this, "Show Icon").setDefault(true);
        this.overrideDefaultTextColors = new Setting(this, "Override Default Text Colors").setDefault(false);
        this.borderSurroundsImage = new Setting(this, "Border Surrounds Image").setDefault(true);
        this.settings.remove(this.backgroundWidth);
        this.settings.remove(this.backgroundHeight);
        this.settings.remove(this.brackets);
    }
    
    @Override
    public int getWidth() {
        if (this.showIcon.getBoolean()) {
            try {
                return 16 + this.mc.fontRendererObj.getStringWidth(String.valueOf(this.mc.getCurrentServerData().serverIP) + "  ");
            }
            catch (NullPointerException e) {
                return 16 + this.mc.fontRendererObj.getStringWidth(String.valueOf(I18n.format("menu.singleplayer", new Object[0])) + "  ");
            }
        }
        try {
            return this.mc.fontRendererObj.getStringWidth(String.valueOf(this.mc.getCurrentServerData().serverIP) + "  ");
        }
        catch (NullPointerException e) {
            return this.mc.fontRendererObj.getStringWidth(String.valueOf(I18n.format("menu.singleplayer", new Object[0])) + "  ");
        }
    }
    
    @Override
    public int getHeight() {
        return 16;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.background.getBoolean()) {
            if (this.showIcon.getBoolean()) {
                GuiUtils.drawRect(x, y, x + this.getWidth(), y + this.getHeight(), this.backgroundColor.getColor());
            }
            else {
                GuiUtils.drawRect(x + 16.0f, y, x + this.getWidth() + 16.0f, y + this.getHeight(), this.backgroundColor.getColor());
            }
        }
        try {
            this.mc.fontRendererObj.drawString(" " + this.mc.getCurrentServerData().serverIP, x + 17.0f, y + 4.0f, this.textColor.getColor(), this.textShadow.getBoolean());
        }
        catch (NullPointerException e) {
            this.mc.fontRendererObj.drawString(" " + I18n.format("menu.singleplayer", new Object[0]), x + 17.0f, y + 4.0f, this.textColor.getColor(), this.textShadow.getBoolean());
        }
        if (this.showIcon.getBoolean()) {
            if (this.texture != null && this.mc.getCurrentServerData() != null) {
                this.drawIcon(x, y, this.resource);
            }
            else {
                this.drawIcon(x, y, ServerOverlayModule.UNKNOWN_SERVER);
            }
        }
        if (this.border.getBoolean()) {
            if (this.borderSurroundsImage.getBoolean()) {
                if (this.showIcon.getBoolean()) {
                    GuiUtils.drawRoundedOutline(x, y, x + this.getWidth(), y + 16.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                }
                else {
                    GuiUtils.drawRoundedOutline(x + 16.0f, y, x + this.getWidth() + 16.0f, y + 16.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
                }
            }
            else if (this.showIcon.getBoolean()) {
                GuiUtils.drawRoundedOutline(x + 16.0f, y, x + this.getWidth(), y + 16.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
            }
            else {
                GuiUtils.drawRoundedOutline(x + 16.0f, y, x + this.getWidth() + 16.0f, y + 16.0f, 0.0f, this.borderWidth.getFloat(), this.borderColor.getColor());
            }
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    @SubscribeEvent
    public void onServerJoin(final SCServerJoinEvent e) {
        this.serverData = this.mc.getCurrentServerData();
        this.resource = new ResourceLocation("servers/" + this.serverData.serverIP + "/icon");
        this.texture = (DynamicTexture)this.mc.getTextureManager().getTexture(this.resource);
        try {
            this.prepareServerIcon();
        }
        catch (RuntimeException ex) {}
    }
    
    @Override
    protected void renderBackground(final float x, final float y) {
    }
    
    private void drawIcon(final float x, final float y, final ResourceLocation r) {
        this.mc.getTextureManager().bindTexture(r);
        GlStateManager.enableBlend();
        GuiUtils.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 16, 16, 16.0f, 16.0f);
        GlStateManager.disableBlend();
    }
    
    private void prepareServerIcon() {
        if (this.serverData.getBase64EncodedIconData() == null) {
            this.mc.getTextureManager().deleteTexture(this.resource);
            this.texture = null;
        }
        else {
            final ByteBuf var2 = Unpooled.copiedBuffer(this.serverData.getBase64EncodedIconData(), Charsets.UTF_8);
            final ByteBuf var3 = Base64.decode(var2);
            BufferedImage var4 = null;
            Label_0163: {
                try {
                    var4 = TextureUtil.func_177053_a(new ByteBufInputStream(var3));
                    Validate.validState(var4.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(var4.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    break Label_0163;
                }
                catch (Exception var5) {
                    this.serverData.setBase64EncodedIconData(null);
                }
                finally {
                    var2.release();
                    var3.release();
                }
                return;
            }
            if (this.texture == null) {
                this.texture = new DynamicTexture(var4.getWidth(), var4.getHeight());
                this.mc.getTextureManager().loadTexture(this.resource, this.texture);
            }
            var4.getRGB(0, 0, var4.getWidth(), var4.getHeight(), this.texture.getTextureData(), 0, var4.getWidth());
            this.texture.updateDynamicTexture();
        }
    }
    
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_RIGHT;
    }
}
