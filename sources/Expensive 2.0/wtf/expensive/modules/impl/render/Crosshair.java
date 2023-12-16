package wtf.expensive.modules.impl.render;

import net.minecraft.client.MainWindow;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.math.MathHelper;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.render.EventRender;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.animations.Animation;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import static wtf.expensive.util.render.ColorUtil.*;
import static wtf.expensive.util.render.RenderUtil.Render2D.*;

/**
 * @author dedinside
 * @since 29.06.2023
 */
@FunctionAnnotation(name = "Crosshair", type = Type.Render)
public class Crosshair extends Function {
    private float circleAnimation = 0.0F;

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender e) {
            handleCrosshairRender();
        }
    }

    private void handleCrosshairRender() {
        if (mc.gameSettings.getPointOfView() != PointOfView.FIRST_PERSON) {
            return;
        }

        final MainWindow mainWindow = mc.getMainWindow();

        final float x = (float) mainWindow.scaledWidth() / 2.0F;
        final float y = (float) mainWindow.scaledHeight() / 2.0F;

        final float calculateCooldown = mc.player.getCooledAttackStrength(1.0F);
        final float endRadius = MathHelper.clamp(calculateCooldown * 360, 0, 360);

        this.circleAnimation = AnimationMath.lerp(this.circleAnimation, -endRadius, 4);

        final int mainColor = rgba(30, 30, 30, 255);
        Style style = Managment.STYLE_MANAGER.getCurrentStyle();

        drawCircle(x, y, 0, 360, 3.5f, 3, false, mainColor);
        drawCircle(x, y, 0, circleAnimation, 3.5f, 3, false, style);
    }
}
