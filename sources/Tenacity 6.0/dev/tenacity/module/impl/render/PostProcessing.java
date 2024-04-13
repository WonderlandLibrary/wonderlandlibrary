package dev.tenacity.module.impl.render;

import dev.tenacity.Tenacity;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.BooleanSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.shader.impl.GaussianBlur;
import dev.tenacity.util.render.shader.impl.KawaseBloom;
import net.minecraft.client.shader.Framebuffer;

public class PostProcessing extends Module {

    public final BooleanSetting blur = new BooleanSetting("Blur", true);
    public final NumberSetting blurRadius = new NumberSetting("Blur Radius", 10, 1, 40, 1);
    public final NumberSetting blurCompression = new NumberSetting("Blur Compression", 2, 1, 10, 1);

    private final BooleanSetting bloom = new BooleanSetting("Bloom", true);
    private final NumberSetting shadowRadius = new NumberSetting("Bloom Iterations", 3, 1, 8, 1);
    private final NumberSetting shadowOffset = new NumberSetting("Bloom Offset", 1, 1, 10, 1);

    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    public PostProcessing() {
        super("PostProcessing", "Adds post processing effects to the client", ModuleCategory.RENDER);
        blurRadius.addParent(blur, BooleanSetting::isEnabled);
        blurCompression.addParent(blur, BooleanSetting::isEnabled);

        shadowRadius.addParent(bloom, BooleanSetting::isEnabled);
        shadowOffset.addParent(bloom, BooleanSetting::isEnabled);

        initializeSettings(blur, blurRadius, blurCompression, bloom, shadowRadius, shadowOffset);
    }

    public void process() {
        if (!isEnabled()) return;

        if (blur.isEnabled()) {
            GaussianBlur.startBlur();
            Tenacity.getInstance().getEventBus().dispatch(new ShaderEvent(false));
            GaussianBlur.endBlur((float) blurRadius.getCurrentValue(), (float) blurCompression.getCurrentValue());
        }

        if (bloom.isEnabled()) {
            stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(false);
            Tenacity.getInstance().getEventBus().dispatch(new ShaderEvent(true));
            stencilFramebuffer.unbindFramebuffer();

            KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, (int) shadowRadius.getCurrentValue(), (int) shadowOffset.getCurrentValue());
        }
    }

}
