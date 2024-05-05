package best.azura.client.impl.module.impl.render;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventFastTick;
import best.azura.client.impl.events.EventRender2D;
import best.azura.client.impl.events.EventRenderHand;
import best.azura.client.impl.shader.BlurShader;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.other.GLTask;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import best.azura.eventbus.core.EventPriority;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@ModuleInfo(name = "Blur", category = Category.RENDER, description = "Modify blur settings halal")
public class BlurModule extends Module {

    public static final BlurShader blurShader = new BlurShader();
    public static final NumberValue<Float> radius = new NumberValue<>("Sigma", "Radius of the blur", 25F, 1F, 5F, 100F);
    public static final BooleanValue darkMode = new BooleanValue("Dark Mode", null, false);
    public static final BooleanValue blurArray = new BooleanValue("Blur Arraylist", null, true);
    public static final BooleanValue blurESP = new BooleanValue("Blur 2D ESP", null, false);
    public static final BooleanValue blurScoreboard = new BooleanValue("Blur Scoreboard", null, true);
    public static final BooleanValue blurTargetHud = new BooleanValue("Blur Target HUD", null, true);
    public static final BooleanValue blurMenu = new BooleanValue("Blur Menu", null, false);
    public static final BooleanValue blurNotifications = new BooleanValue("Blur Notifications", null, true);
    public static final BooleanValue blurHotbar = new BooleanValue("Blur Hotbar", null, false);
    public static final BooleanValue blurChat = new BooleanValue("Blur Chat", null, true);
    public static final BooleanValue blurHand = new BooleanValue("Blur Hand", null, false);
    public static final ArrayList<GLTask> blurTasks = new ArrayList<>(), postBlurTasks = new ArrayList<>();
    private Framebuffer framebuffer;

    public BlurModule() {
        super();
        framebuffer = new Framebuffer(1, 1, false);
    }

    @EventHandler(EventPriority.HIGHER)
    private final Listener<EventRenderHand> eventRender3DListener = e -> {
        if (framebuffer == null) return;
        if (!blurHand.getObject()) return;
        if (mc.thePlayer.isSpectator()) return;
        if (mc.gameSettings.showDebugInfo != 0) return;
        if (e.isPre()) {
            GlStateManager.pushMatrix();
            this.framebuffer.framebufferClear();
            this.framebuffer.bindFramebuffer(false);
        }
        if (e.isPost()) {
            GlStateManager.popMatrix();
            mc.getFramebuffer().bindFramebuffer(false);
        }
    };

    @EventHandler(EventPriority.HIGHER)
    public final Listener<EventFastTick> eventFastTickListener = e -> {
        blurShader.setRadius(radius.getObject());
        blurShader.setDarkMode(darkMode.getObject());
        if (!OpenGlHelper.shadersSupported) setEnabled(false);
    };

    @EventHandler(EventPriority.HIGHER)
    private final Listener<EventRender2D> handBlurListener = e -> {
        if (framebuffer == null) return;
        if (!blurHand.getObject()) return;
        if (mc.theWorld == null) return;
        if (mc.thePlayer.isSpectator()) return;
        if (mc.gameSettings.showDebugInfo != 0) return;
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        framebuffer = RenderUtil.INSTANCE.createFramebuffer(framebuffer);
        GL11.glPushMatrix();
        StencilUtil.initStencilToWrite();
        try {
            RenderUtil.INSTANCE.renderFrameBuffer(framebuffer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        StencilUtil.readStencilBuffer(1);
        GlStateManager.resetColor();
        if (radius.getObject() != 0) blurShader.blur();
        StencilUtil.uninitStencilBuffer();
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        mc.getFramebuffer().bindFramebuffer(true);
    };

    @EventHandler(EventPriority.HIGHER)
    public final Listener<EventRender2D> eventRender2DListener = e -> {
        GL11.glPushMatrix();
        StencilUtil.initStencilToWrite();
        try {
            blurTasks.forEach(task -> {
                try {
                    task.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        StencilUtil.readStencilBuffer(1);
        blurShader.blur();
        StencilUtil.uninitStencilBuffer();
        GL11.glPopMatrix();
        blurTasks.clear();
        try {
            postBlurTasks.forEach(task -> {
                try {
                    task.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        postBlurTasks.clear();
    };

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        blurTasks.clear();
        postBlurTasks.clear();
    }
}