package fun.expensive.client.feature.impl.visual;

import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.ui.settings.impl.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;

import java.awt.*;

public class Chams extends Feature {

    public static ColorSetting colorChams;
    public static BooleanSetting clientColor;
    public static ListSetting chamsMode;
    public static NumberSetting cosmoSpeed;
    private static ResourceLocation space;
    private static int shaderProgram;
    public static NumberSetting chamsAlpha;

    public Chams() {
        super("Chams", "Подсвечивает игроков", FeatureCategory.Visuals);
        chamsMode = new ListSetting("Chams Mode", "Fill", () -> true, "Fill", "Walls", "Cosmo");
        clientColor = new BooleanSetting("Client Colored", false, () -> !chamsMode.currentMode.equals("Walls"));
        chamsAlpha = new NumberSetting("Chams Aplha", 0.5f, 0.2f, 1.0f, 0.1f, () -> Chams.chamsMode.currentMode.equals("Fill"));
        colorChams = new ColorSetting("Chams Color", new Color(0xFFFFFF).getRGB(), () -> !chamsMode.currentMode.equals("Walls") && !clientColor.getBoolValue());
        cosmoSpeed = new NumberSetting("Cosmo Speed", 1.0f, 1.0f, 10.0f, 1.0f, () -> Chams.chamsMode.currentMode.equals("Cosmo"));
        addSettings(chamsMode, colorChams, clientColor, chamsAlpha, cosmoSpeed);
    }

    public static void shaderAttach(Entity entity) {
        if (shaderProgram == 0) {
            return;
        }
        ARBShaderObjects.glUseProgramObjectARB(shaderProgram);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shaderProgram, "time"), (float) entity.ticksExisted / -100.0f * cosmoSpeed.getNumberValue());
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(shaderProgram, "image"), 0);
        mc.getTextureManager().bindTexture(space);
    }

    public static void shaderDetach() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private static void parseShaderFromBytes(byte[] str) {
        shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
        if (shaderProgram == 0) {
            System.out.println("PC Issued");
            return;
        }
        int shader = ARBShaderObjects.glCreateShaderObjectARB(35632);
        ARBShaderObjects.glShaderSourceARB(shader, new String(str));
        ARBShaderObjects.glCompileShaderARB(shader);
        ARBShaderObjects.glAttachObjectARB(shaderProgram, shader);
        ARBShaderObjects.glLinkProgramARB(shaderProgram);
        ARBShaderObjects.glValidateProgramARB(shaderProgram);
    }

    static {
        space = new ResourceLocation("rich/space.jpg");
        Chams.parseShaderFromBytes(new byte[]{35, 118, 101, 114, 115, 105, 111, 110, 32, 49, 51, 48, 10, 10, 117, 110, 105, 102, 111, 114, 109, 32, 115, 97, 109, 112, 108, 101, 114, 50, 68, 32, 105, 109, 97, 103, 101, 59, 10, 117, 110, 105, 102, 111, 114, 109, 32, 102, 108, 111, 97, 116, 32, 116, 105, 109, 101, 59, 10, 10, 118, 111, 105, 100, 32, 109, 97, 105, 110, 40, 41, 10, 123, 10, 118, 101, 99, 52, 32, 114, 101, 115, 117, 108, 116, 32, 61, 32, 116, 101, 120, 116, 117, 114, 101, 50, 68, 40, 105, 109, 97, 103, 101, 44, 32, 118, 101, 99, 50, 40, 40, 45, 103, 108, 95, 70, 114, 97, 103, 67, 111, 111, 114, 100, 32, 47, 32, 49, 53, 48, 48, 41, 32, 43, 32, 116, 105, 109, 101, 32, 47, 32, 53, 41, 41, 59, 10, 114, 101, 115, 117, 108, 116, 46, 97, 32, 61, 32, 49, 59, 10, 103, 108, 95, 70, 114, 97, 103, 67, 111, 108, 111, 114, 32, 61, 32, 114, 101, 115, 117, 108, 116, 59, 10, 125});
    }
}