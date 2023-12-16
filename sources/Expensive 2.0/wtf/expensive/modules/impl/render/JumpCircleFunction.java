package wtf.expensive.modules.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventJump;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * @author dedinside
 * @since 08.06.2023
 */
@FunctionAnnotation(name = "Jump Circle", type = Type.Render)
public class JumpCircleFunction extends Function {
    public List<Circle> circles = new ArrayList<>();

    public SliderSetting radius = new SliderSetting("Радиус", 1, 0.1f, 2, 0.01f);
    public SliderSetting shadow = new SliderSetting("Тень", 60, 10, 100, 0.01f);
    public SliderSetting speed = new SliderSetting("Скорость", 1, 1, 5, 0.01f);

    public JumpCircleFunction() {
        addSettings(radius, speed);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventJump) {
            addCircle();
        } else if (event instanceof EventRender render && render.isRender3D()) {
            updateCircles();
            renderCircles();
        }
    }

    private void addCircle() {
        circles.add(new Circle((float) mc.player.getPosX(), (float) mc.player.getPosY(), (float) mc.player.getPosZ()));
    }

    private void updateCircles() {
        for (Circle circle : circles) {
            circle.factor = AnimationMath.fast(circle.factor, radius.getValue().floatValue() + 0.1f, speed.getValue().floatValue());
            //circle.shadow = AnimationMath.fast(circle.shadow, shadow.getValue().floatValue(), speed.getValue().floatValue());
            circle.alpha = AnimationMath.fast(circle.alpha, 0, speed.getValue().floatValue());
        }
        if (circles.size() >= 1)
            circles.removeIf(circle -> circle.alpha <= 0.005f);
    }

    private void renderCircles() {
        setupRenderSettings();
        for (Circle circle : circles) {
            drawJumpCircle(circle, circle.factor, circle.alpha, 0);
        }
        restoreRenderSettings();
    }

    /**
     * Устанавливает настройки отрисовки кругов.
     */
    private void setupRenderSettings() {
        RenderSystem.pushMatrix();
        RenderSystem.disableLighting();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.shadeModel(7425);
        RenderSystem.disableCull();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFuncSeparate(770, 1, 0, 1);
        GlStateManager.translated(-mc.getRenderManager().info.getProjectedView().getX(),
                -mc.getRenderManager().info.getProjectedView().getY(),
                -mc.getRenderManager().info.getProjectedView().getZ());
    }

    /**
     * Восстанавливает настройки отрисовки.
     */
    private void restoreRenderSettings() {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableAlphaTest();
        RenderSystem.depthMask(true);
        RenderSystem.popMatrix();

    }

    /**
     * Рисует круг прыжка.
     *
     * @param circle     Круг прыжка.
     * @param radius     Радиус круга.
     * @param alpha      Прозрачность круга.
     * @param shadowSize Размер тени круга.
     */
    private void drawJumpCircle(Circle circle, float radius, float alpha, float shadowSize) {
        double x = circle.spawnX;
        double y = circle.spawnY + 0.1;
        double z = circle.spawnZ;
        GlStateManager.translated(x,y,z);
        GlStateManager.rotatef(circle.factor * 70,0,-1,0);

        mc.getTextureManager().bindTexture(new ResourceLocation("expensive/images/circle.png"));

        buffer.begin(GL_QUAD_STRIP, POSITION_COLOR_TEX);
        for (int i = 0; i <= 360F; i+=1) {
            float[] colors = RenderUtil.IntColor.rgb(ColorUtil.getColorStyle(i * 2));
            double sin = MathHelper.sin(Math.toRadians(i + 0.1F)) * radius;
            double cos = MathHelper.cos(Math.toRadians(i + 0.1F)) * radius;
            buffer.pos(0, 0, 0).color(colors[0], colors[1], colors[2], MathHelper.clamp(circle.alpha ,0,1)).tex(0.5f, 0.5f).endVertex();
            buffer.pos(sin, 0, cos).color(colors[0], colors[1], colors[2], MathHelper.clamp(circle.alpha ,0,1)).tex((float) ((sin / (2 * radius)) + 0.5f), (float) ((cos / (2 * radius)) + 0.5f)).endVertex();
        }
        tessellator.draw();
        GlStateManager.rotatef(-circle.factor * 70,0,-1,0);
        GlStateManager.translated(-x,-y,-z);
    }


    /**
     * Класс, представляющий круг прыжка.
     */
    class Circle {
        public final float spawnX;
        public final float spawnY;
        public final float spawnZ;
        public float factor = 0;
        public float alpha = 5;
        public float shadow = 40;
        public float ticks = 0;

        public Circle(float spawnX, float spawnY, float spawnZ) {
            this.spawnX = spawnX;
            this.spawnY = spawnY;
            this.spawnZ = spawnZ;
        }
    }
}
