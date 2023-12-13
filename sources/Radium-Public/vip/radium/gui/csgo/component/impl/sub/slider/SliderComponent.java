package vip.radium.gui.csgo.component.impl.sub.slider;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.ButtonComponent;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.property.impl.Representation;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.RenderingUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public abstract class SliderComponent extends ButtonComponent implements PredicateComponent {

    private boolean sliding;

    public SliderComponent(Component parent, float x, float y, float width, float height) {
        super(parent, x, y, width, height);
    }

    @Override
    public void drawComponent(LockedResolution resolution, int mouseX, int mouseY) {
        final float x = getX();
        final float y = getY();
        final float width = getWidth();
        final float height = getHeight();

        double min = getMin();
        double max = getMax();
        double dValue = getValue();
        Representation representation = getRepresentation();
        boolean isInt = representation == Representation.INT || representation == Representation.MILLISECONDS;
        double value;
        if (isInt)
            value = (int) dValue;
        else
            value = dValue;
        boolean hovered = isHovered(mouseX, mouseY);

        if (sliding) {
            // TODO: Shitty fix but works
            if (mouseX >= x - 0.5F && mouseY >= y - 0.5F && mouseX <= x + width + 0.5F && mouseY <= y + height + 0.5F)
                setValue(
                        MathHelper.clamp_double(
                                roundToIncrement((mouseX - x) * (max - min) / (width - 1) + min),
                                min,
                                max));
            else
                sliding = false;
        }

        double sliderPercentage = ((value - min) / (max - min));

        String valueString;

        if (isInt)
            valueString = Integer.toString((int) value);
        else {
            final DecimalFormat format = new DecimalFormat("####.##");
            valueString = format.format(value);
        }

        switch (representation) {
            case PERCENTAGE:
                valueString += '%';
                break;
            case MILLISECONDS:
                valueString += "ms";
                break;
            case DISTANCE:
                valueString += 'm';
        }

        Gui.drawRect(x, y, x + width, y + height, SkeetUI.getColor(0x0D0D0D));
        RenderingUtils.drawGradientRect(x + 0.5F, y + 0.5F, x + width - 0.5F, y + height - 0.5F, false,
                SkeetUI.getColor(hovered ?
                        RenderingUtils.darker(0x494949, 1.4F) :
                        0x494949),
                SkeetUI.getColor(hovered ?
                        RenderingUtils.darker(0x303030, 1.4F) :
                        0x303030));

        RenderingUtils.drawGradientRect(x + 0.5F, y + 0.5F, x + width * sliderPercentage - 0.5F, y + height - 0.5F,
                false,
                SkeetUI.getColor(),
                RenderingUtils.darker(SkeetUI.getColor(), 0.8F));

        if (SkeetUI.shouldRenderText()) {
            float stringWidth = SkeetUI.GROUP_BOX_HEADER_RENDERER.getWidth(valueString);
            GL11.glTranslatef(0.0F, 0.0F, 1.0F);
            if (SkeetUI.getAlpha() > 120)
                RenderingUtils.drawOutlinedString(SkeetUI.GROUP_BOX_HEADER_RENDERER,
                        valueString,
                        x + width * (float) sliderPercentage - stringWidth / 2,
                        y + height / 2,
                        SkeetUI.getColor(0xFFFFFF), 0x78000000);
            else
                SkeetUI.GROUP_BOX_HEADER_RENDERER.drawString(valueString,
                        x + width * (float) sliderPercentage - stringWidth / 2, y + height / 2,
                        SkeetUI.getColor(0xFFFFFF));
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        }
    }

    @Override
    public void onPress(int mouseButton) {
        if (!sliding && mouseButton == 0)
            sliding = true;
    }

    @Override
    public void onMouseRelease(int button) {
        sliding = false;
    }

    private double roundToIncrement(double value) {
        double inc = getIncrement();
        double halfOfInc = inc / 2.0D;
        double floored = StrictMath.floor(value / inc) * inc;
        if (value >= floored + halfOfInc)
            return new BigDecimal(StrictMath.ceil(value / inc) * inc)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        else return new BigDecimal(floored)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    public abstract double getValue();

    public abstract void setValue(double value);

    public abstract Representation getRepresentation();

    public abstract double getMin();

    public abstract double getMax();

    public abstract double getIncrement();
}
