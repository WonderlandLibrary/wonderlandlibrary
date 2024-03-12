// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.render.blur.KawaseBloom;
import dev.tenacity.utils.render.blur.KawaseBlur;
import dev.tenacity.event.Event;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.ui.clickguis.modern.ModernClickGUI;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.Tenacity;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.Category;
import net.minecraft.client.shader.Framebuffer;
import dev.tenacity.module.settings.impl.MultipleBoolSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.Module;

public class PostProcessing extends Module
{
    public final BooleanSetting blur;
    private final NumberSetting iterations;
    private final NumberSetting offset;
    private final BooleanSetting bloom;
    public static MultipleBoolSetting glowOptions;
    private final NumberSetting shadowRadius;
    private final NumberSetting shadowOffset;
    private Framebuffer stencilFramebuffer;
    
    public PostProcessing() {
        super("PostProcessing", "Post Processing", Category.RENDER, "blurs shit");
        this.blur = new BooleanSetting("Blur", true);
        this.iterations = new NumberSetting("Blur Iterations", 2.0, 8.0, 1.0, 1.0);
        this.offset = new NumberSetting("Blur Offset", 3.0, 10.0, 1.0, 1.0);
        this.bloom = new BooleanSetting("Bloom", true);
        this.shadowRadius = new NumberSetting("Bloom Iterations", 2.0, 8.0, 1.0, 1.0);
        this.shadowOffset = new NumberSetting("Bloom Offset", 1.0, 10.0, 1.0, 1.0);
        this.stencilFramebuffer = new Framebuffer(1, 1, false);
        this.shadowRadius.addParent(this.bloom, ParentAttribute.BOOLEAN_CONDITION);
        this.shadowOffset.addParent(this.bloom, ParentAttribute.BOOLEAN_CONDITION);
        PostProcessing.glowOptions.addParent(this.bloom, ParentAttribute.BOOLEAN_CONDITION);
        this.addSettings(this.blur, this.iterations, this.offset, this.bloom, PostProcessing.glowOptions, this.shadowRadius, this.shadowOffset);
    }
    
    public void stuffToBlur(final boolean bloom) {
        final ScaledResolution sr = new ScaledResolution(PostProcessing.mc);
        if (PostProcessing.mc.currentScreen instanceof GuiChat) {
            Gui.drawRect2(2.0, sr.getScaledHeight() - 14.0f * GuiChat.openingAnimation.getOutput().floatValue(), sr.getScaledWidth() - 4, 12.0, Color.BLACK.getRGB());
        }
        if (PostProcessing.mc.currentScreen == ClickGUIMod.dropdownClickGUI) {
            ClickGUIMod.dropdownClickGUI.renderEffects();
        }
        if (PostProcessing.mc.currentScreen == ClickGUIMod.dropdownClickGUI || PostProcessing.mc.currentScreen == ClickGUIMod.modernClickGUI || PostProcessing.mc.currentScreen == ClickGUIMod.compactClickGUI) {
            Tenacity.INSTANCE.getSideGui().drawForEffects(bloom);
            Tenacity.INSTANCE.getSearchBar().drawEffects();
        }
        RenderUtil.resetColor();
        PostProcessing.mc.ingameGUI.getChatGUI().renderChatBox();
        RenderUtil.resetColor();
        PostProcessing.mc.ingameGUI.renderScoreboardBlur(sr);
        PostProcessing.mc.ingameGUI.renderEffects();
        RenderUtil.resetColor();
        final NotificationsMod notificationsMod = Tenacity.INSTANCE.getModuleCollection().getModule(NotificationsMod.class);
        if (notificationsMod.isEnabled()) {
            notificationsMod.renderEffects(PostProcessing.glowOptions.getSetting("Notifications").isEnabled());
        }
        if (bloom && PostProcessing.mc.currentScreen instanceof ModernClickGUI) {
            ClickGUIMod.modernClickGUI.drawBigRect();
        }
    }
    
    public void blurScreen() {
        if (!this.enabled) {
            return;
        }
        if (this.blur.isEnabled()) {
            (this.stencilFramebuffer = RenderUtil.createFrameBuffer(this.stencilFramebuffer)).framebufferClear();
            this.stencilFramebuffer.bindFramebuffer(false);
            Tenacity.INSTANCE.getEventProtocol().handleEvent(new ShaderEvent(false, PostProcessing.glowOptions));
            this.stuffToBlur(false);
            this.stencilFramebuffer.unbindFramebuffer();
            KawaseBlur.renderBlur(this.stencilFramebuffer.framebufferTexture, this.iterations.getValue().intValue(), this.offset.getValue().intValue());
        }
        if (this.bloom.isEnabled()) {
            (this.stencilFramebuffer = RenderUtil.createFrameBuffer(this.stencilFramebuffer)).framebufferClear();
            this.stencilFramebuffer.bindFramebuffer(false);
            Tenacity.INSTANCE.getEventProtocol().handleEvent(new ShaderEvent(true, PostProcessing.glowOptions));
            this.stuffToBlur(true);
            this.stencilFramebuffer.unbindFramebuffer();
            KawaseBloom.renderBlur(this.stencilFramebuffer.framebufferTexture, this.shadowRadius.getValue().intValue(), this.shadowOffset.getValue().intValue());
        }
    }
    
    static {
        PostProcessing.glowOptions = new MultipleBoolSetting("Glow Options", new BooleanSetting[] { new BooleanSetting("ArrayList", true), new BooleanSetting("ClickGUI", false), new BooleanSetting("Watermark", true), new BooleanSetting("Statistics", true), new BooleanSetting("Radar", true), new BooleanSetting("TargetHUD", true), new BooleanSetting("Spotify", true), new BooleanSetting("Notifications", false), new BooleanSetting("Keystrokes", false) });
    }
}
