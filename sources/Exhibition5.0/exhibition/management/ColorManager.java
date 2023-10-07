// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class ColorManager
{
    private static List<ColorObject> colorObjectList;
    public static ColorObject fTeam;
    public static ColorObject eTeam;
    public static ColorObject fVis;
    public static ColorObject fInvis;
    public static ColorObject eVis;
    public static ColorObject eInvis;
    public static ColorObject hudColor;
    public static ColorObject xhair;
    
    public static List<ColorObject> getColorObjectList() {
        return ColorManager.colorObjectList;
    }
    
    public static ColorObject getFriendlyVisible() {
        return ColorManager.fVis;
    }
    
    public static ColorObject getFriendlyInvisible() {
        return ColorManager.fInvis;
    }
    
    public static ColorObject getEnemyVisible() {
        return ColorManager.eVis;
    }
    
    public static ColorObject getEnemyInvisible() {
        return ColorManager.eInvis;
    }
    
    public ColorObject getHudColor() {
        return ColorManager.hudColor;
    }
    
    public ColorManager() {
        ColorManager.colorObjectList.add(ColorManager.fVis);
        ColorManager.colorObjectList.add(ColorManager.fInvis);
        ColorManager.colorObjectList.add(ColorManager.eVis);
        ColorManager.colorObjectList.add(ColorManager.eInvis);
        ColorManager.colorObjectList.add(ColorManager.hudColor);
        ColorManager.colorObjectList.add(ColorManager.fTeam);
        ColorManager.colorObjectList.add(ColorManager.eTeam);
        ColorManager.colorObjectList.add(ColorManager.xhair);
    }
    
    static {
        ColorManager.colorObjectList = new CopyOnWriteArrayList<ColorObject>();
        ColorManager.fTeam = new ColorObject(0, 255, 0, 255);
        ColorManager.eTeam = new ColorObject(255, 0, 0, 255);
        ColorManager.fVis = new ColorObject(0, 0, 255, 255);
        ColorManager.fInvis = new ColorObject(0, 255, 0, 255);
        ColorManager.eVis = new ColorObject(255, 0, 0, 255);
        ColorManager.eInvis = new ColorObject(255, 255, 0, 255);
        ColorManager.hudColor = new ColorObject(0, 192, 255, 255);
        ColorManager.xhair = new ColorObject(0, 255, 0, 200);
    }
}
