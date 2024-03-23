package xyz.northclient.features;

import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.util.font.FontUtil;
import xyz.northclient.util.shader.RectUtil;

import java.awt.*;

public class ModernSlider {
    public DoubleValue s = null;

    public int posX = 0, posY = 0;
    public void render(int x, int y) {
        this.posX = x;
        this.posY = y;

        RectUtil.drawRoundedRect(x+2.5f,y-1.5f, (float) ((float) (round(s.get().doubleValue(), (float) s.getInc()) * 113)/s.getMax()),13,3,new Color(48,48,48));

        String value = String.valueOf((float) round(s.get().doubleValue(), (float) s.getInc()));

        if (s.getInc() == 1) {
            value = value.replace(".0", "");
        }

        FontUtil.SFProMedium.drawStringWithShadow(value,x+113 - FontUtil.SFProMedium.getWidth(value),y+2,-1);
    }

    public void move(int x, int y) {
        int oldPosx = posX;
        if(x >= posX && y >= posY && x <= posX+113 && y <= posY+13) {
            final double percent = (double) (x - (posX)) / (double) (100);
            double value = s.getMin() - percent * (s.getMin() - s.getMax());

            if (value > s.getMax()) value = s.getMax();
            if (value < s.getMin()) value = s.getMin();

            s.set(value);

            if (s.getInc() != 0)
                s.set(round(value, (float) s.getInc()));
            else s.set(value);
        }
        posX = oldPosx;
    }

    public void click(int x, int y) {
        move(x,y);
    }

    private static double round(final double value, final float places) {
        if (places < 0) throw new IllegalArgumentException();

        final double precision = 1 / places;
        return Math.round(value * precision) / precision;
    }

}
