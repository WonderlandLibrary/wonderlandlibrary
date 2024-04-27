package me.aquavit.liquidsense.module.modules.client;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.realms.RealmsMth;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "Compass", description = "Can give your directions", category = ModuleCategory.CLIENT)
public class Compass extends Module {

    public ListValue mode = new ListValue("Type", new String[] {"Axis", "Direction"}, "Axis");
    public FloatValue scale = new FloatValue( "Scale", 1f, 1f, 5f);
    public IntegerValue yOffset = new IntegerValue("YOffset", 0, 0, 100);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ScaledResolution res = new ScaledResolution(mc);
        float x = res.getScaledWidth() / 2f;
        float y = res.getScaledHeight() - 100f - yOffset.get();

        double pitch = RealmsMth.clamp(mc.thePlayer.rotationPitch + 30, -90f, 90f);
        pitch = Math.toRadians(pitch);

        double yaw = MathHelper.wrapAngleTo180_double(mc.thePlayer.rotationYaw);
        yaw = Math.toRadians(yaw);

        for (Direction direction : Direction.values()) {
            String axis = mode.get().equalsIgnoreCase("Axis") ? direction.getAxis() : direction.name();
            Fonts.font16.drawStringWithShadow(axis, (x + getX(direction, yaw)) - (Fonts.font16.getStringWidth(axis) / 2f),
                    (y + getY(direction, yaw, pitch)) - (Fonts.font16.getHeight() / 2f), 0xffffffff);
        }
    }

    private float getX(Direction direction, double yaw) {
        return (float) (Math.sin(getPos(direction, yaw)) * scale.get() * 30);
    }

    private float getY(Direction direction, double yaw, double pitch) {
        return (float) (Math.cos(getPos(direction, yaw)) * Math.sin(pitch) * scale.get() * 30);
    }

    private double getPos(Direction direction, double yaw) {
        return yaw + direction.ordinal() * Math.PI / 2;
    }

    private enum Direction {
        N("Z-"),
        W("X-"),
        S("Z+"),
        E("X+");

        private final String axis;

        Direction(String axis) {
            this.axis = axis;
        }

        public String getAxis() {
            return axis;
        }
    }
}
