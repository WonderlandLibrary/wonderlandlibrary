package best.azura.client.impl.module.impl.render;

import best.azura.client.impl.events.EventFastTick;
import best.azura.client.impl.events.EventRender2D;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.color.ColorUtil;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.BooleanValue;
import best.azura.client.impl.value.ColorValue;
import best.azura.client.impl.value.NumberValue;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;


@ModuleInfo(name = "Crosshair", category = Category.RENDER, description = "Render Azura crosshair not Minecrat")
public class Crosshair extends Module {

	public BooleanValue useHUDColor = new BooleanValue("Use HUD Color", "USE THE HUD COLOR", false);
	public final ColorValue colorValue = new ColorValue("Color", "Color of the Crosshair", () -> !useHUDColor.getObject(), HSBColor.fromColor(new Color(0xFFFFFF)));
	public final BooleanValue tShape = new BooleanValue("T-Shape", "Use a T-Shape", false);
	public final NumberValue<Float> size = new NumberValue<>("Size", "Sizing of the crosshair", 5.0F, 0.5F, 1.0F, 20F);
	public final BooleanValue dynamic = new BooleanValue("Dynamic", "Changes the size while moving", true);
	public final NumberValue<Float> offset = new NumberValue<>("Offset", "Sizing of the crosshair", 5.0F, 0.5F, 1.0F, 10F);
	public final NumberValue<Float> dynamicOffsetVal = new NumberValue<>("Dynamic Offset", "Sizing of the crosshair", 5.0F, 0.5F, 1.0F, 10F);
	public final NumberValue<Float> thickness = new NumberValue<>("Thickness", "Change the thickness", 1.0F, 0.5F, 0.5F, 20.0F);
	public double dynamicOffset;

	@EventHandler
	@SuppressWarnings("unused")
	public final Listener<EventRender2D> eventRender2DListener = eventRender2D -> {
		RenderUtil.INSTANCE.scaleFix(1.0);
		NumberValue<Float> offsetValue = offset;
		Color col = colorValue.getObject().getColor();
		final double length = size.getObject() * 2;
		final double height = size.getObject() * 2;
		final boolean dynamic = this.dynamic.getObject();
		float offset = this.offset.getObject() * 2;
		final double x = this.mc.displayWidth / 2.0;
		final double y = this.mc.displayHeight / 2.0;
		double thickness = this.thickness.getObject();
		if (this.useHUDColor.getObject()) col = new Color(ColorUtil.getLastHudColor().getRGB());
		if (dynamic) offset = (float) this.dynamicOffset;

		if (!tShape.getObject())
			RenderUtil.INSTANCE.drawRect(x - thickness - 1, (y - offset + height) - height + 1, x + thickness + 1, (y - offset) - height / 2 - 1, Color.BLACK.getRGB());
		RenderUtil.INSTANCE.drawRect((x - offset + length) - length + 1, y - thickness - 1, (x - offset) - length / 2 - 1, y + thickness + 1, Color.BLACK.getRGB());
		RenderUtil.INSTANCE.drawRect((x + offset + length) - length - 1, y - thickness - 1, (x + offset) + length / 2 + 1, y + thickness + 1, Color.BLACK.getRGB());
		RenderUtil.INSTANCE.drawRect(x - thickness - 1, (y + offset + height) - height - 1, x + thickness + 1, (y + offset) + height / 2 + 1, Color.BLACK.getRGB());

		if (!tShape.getObject())
			RenderUtil.INSTANCE.drawRect(x - thickness, (y - offset + height) - height, x + thickness, (y - offset) - height / 2, col);
		RenderUtil.INSTANCE.drawRect((x - offset + length) - length, y - thickness, (x - offset) - length / 2, y + thickness, col);
		RenderUtil.INSTANCE.drawRect((x + offset + length) - length, y - thickness, (x + offset) + length / 2, y + thickness, col);
		RenderUtil.INSTANCE.drawRect(x - thickness, (y + offset + height) - height, x + thickness, (y + offset) + height / 2, col);
		RenderUtil.INSTANCE.invertScaleFix(1.0);

		GlStateManager.resetColor();
	};
	@EventHandler
	@SuppressWarnings("unused")
	public final Listener<EventFastTick> eventFastTickListener = eventFastTick -> {
		if (mc.thePlayer != null && mc.theWorld != null) {
			double speed = Math.cosh(0.25);
			if (mc.thePlayer.isMoving() &&
					!mc.thePlayer.isCollidedHorizontally &&
					!mc.gameSettings.keyBindSneak.isKeyDown())
				dynamicOffset = Math.min(dynamicOffset + speed, offset.getObject() + (dynamicOffsetVal.getObject() * 5));
			else
				dynamicOffset = Math.max(dynamicOffset - speed, offset.getObject());
		}
	};
}

