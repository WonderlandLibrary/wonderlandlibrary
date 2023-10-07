package info.sigmaclient.management;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by cool1 on 3/9/2017.
 */
public class ColorManager {

    public static List<ColorObject> getColorObjectList() {
        return colorObjectList;
    }

    private static List<ColorObject> colorObjectList = new CopyOnWriteArrayList<>();

    public ColorObject getHudColor() {
        return hudColor;
    }

    public static ColorObject hudColor = new ColorObject(0,192,255,255);

    public ColorObject getXhairColor() {
        return ch;
    }

    public static ColorObject ch = new ColorObject(255,0,0,255);

    public ColorManager() {
        colorObjectList.add(hudColor);
        colorObjectList.add(ch);
    }

}
