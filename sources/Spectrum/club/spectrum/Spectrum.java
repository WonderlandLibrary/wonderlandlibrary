package club.spectrum;

import club.spectrum.modules.ModuleManager;
import club.spectrum.utils.render.ColorUtils;
import org.lwjgl.opengl.Display;

import javax.vecmath.Vector2d;
import java.awt.*;

/**
 * The main class for Spectrum.
 * Loads the module manager, sets versioning, among other things
 *
 * @author v4n1ty
 * @since 27/09/2023
 */

public enum Spectrum {

    INSTANCE;

    public ModuleManager moduleManager;

    public final String Prefix = "§bSpectrum »";

    public static String NAME = "Spectrum";
    public static String VERSION = "0.0.1";

    public static final Color firstColor = new Color(0, 255, 248, 200);
    public static final Color secondColor = new Color(0, 139, 255, 200);

    public static Color getHudColor(float y){
        return ColorUtils.blend(firstColor, secondColor, new Vector2d(0, y));
    }

    public final void initialize() {
        moduleManager = new ModuleManager();
    }
}
