package tech.atani.client.feature.module.impl.render;

import com.google.common.base.Supplier;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import tech.atani.client.listener.event.minecraft.render.Render3DEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.render.color.ColorUtil;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.impl.StringBoxValue;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;

import java.awt.*;
import java.util.Calendar;

@ModuleData(name = "ChinaHat", description = "Gives you a hat from China.", category = Category.RENDER)
public class ChinaHat extends Module {

    private final SliderValue<Float> width = new SliderValue<>("Width", "What'll be the width of the hat?", this, 0.7f, 0.1f, 1f, 2);
    private final SliderValue<Float> height = new SliderValue<>("Height", "What'll be the width of the hat?", this, 0.4f, 0.1f, 1f, 2);
    private final CheckBoxValue color = new CheckBoxValue("Color", "Color the hat?", this, true);
    private final StringBoxValue customColorMode = new StringBoxValue("Color Mode", "How will the hat be colored?", this, new String[]{"Static", "Fade", "Gradient", "Rainbow", "Astolfo Sky"}, new Supplier[]{() -> color.getValue()});
    private final SliderValue<Integer> red = new SliderValue<Integer>("Red", "What'll be the red of the color?", this, 255, 0, 255, 0, new Supplier[]{() -> color.getValue() && customColorMode.is("Static") || customColorMode.is("Random") || customColorMode.is("Fade") || customColorMode.is("Gradient")});
    private final SliderValue<Integer> green = new SliderValue<Integer>("Green", "What'll be the green of the color?", this, 255, 0, 255, 0, new Supplier[]{() -> color.getValue() && customColorMode.is("Static") || customColorMode.is("Random") || customColorMode.is("Fade") || customColorMode.is("Gradient")});
    private final SliderValue<Integer> blue = new SliderValue<Integer>("Blue", "What'll be the blue of the color?", this, 255, 0, 255, 0, new Supplier[]{() -> color.getValue() && customColorMode.is("Static") || customColorMode.is("Random") || customColorMode.is("Fade") || customColorMode.is("Gradient")});
    private final SliderValue<Integer> red2 = new SliderValue<Integer>("Second Red", "What'll be the red of the second color?", this, 255, 0, 255, 0, new Supplier[]{() -> color.getValue() && customColorMode.is("Gradient")});
    private final SliderValue<Integer> green2 = new SliderValue<Integer>("Second Green", "What'll be the green of the second color?", this, 255, 0, 255, 0, new Supplier[]{() -> color.getValue() && customColorMode.is("Gradient")});
    private final SliderValue<Integer> blue2 = new SliderValue<Integer>("Second Blue", "What'll be the blue of the second color?", this, 255, 0, 255, 0, new Supplier[]{() -> color.getValue() && customColorMode.is("Gradient")});
    private final SliderValue<Float> darkFactor = new SliderValue<Float>("Dark Factor", "How much will the color be darkened?", this, 0.49F, 0F, 1F, 2, new Supplier[]{() -> color.getValue() && customColorMode.is("Fade")});

    private final Cylinder cylinder = new Cylinder();
    private final Disk disk = new Disk();

    final Calendar calendar = Calendar.getInstance();

    @Listen
    public void on3D(Render3DEvent event) {
        if (Methods.mc.gameSettings.thirdPersonView == 0) {
            return;
        }

        int color = 0;
        final int counter = 1;
        switch (this.customColorMode.getValue()) {
            case "Static":
                color = new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
                break;
            case "Fade": {
                int firstColor = new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
                color = ColorUtil.fadeBetween(firstColor, ColorUtil.darken(firstColor, darkFactor.getValue()), counter * 150L);
                break;
            }
            case "Gradient": {
                int firstColor = new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
                int secondColor = new Color(red2.getValue(), green2.getValue(), blue2.getValue()).getRGB();
                color = ColorUtil.fadeBetween(firstColor, secondColor, counter * 150L);
                break;
            }
            case "Rainbow":
                color = ColorUtil.getRainbow(3000, (int) (counter * 150L));
                break;
            case "Astolfo Sky":
                color = ColorUtil.blendRainbowColours(counter * 150L);
                break;
        }

        //I had to convert these colors to their own values because glColor4f doesn't allow one argument :/

        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        float redNormalized = red / 255.0f;
        float greenNormalized = green / 255.0f;
        float blueNormalized = blue / 255.0f;

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glColor4f(redNormalized, greenNormalized, blueNormalized, 0.7f);

        GL11.glPushMatrix();
        GL11.glRotated(-90, 1, 0, 0);
        GL11.glTranslatef(0, 0, 1.9F);
        cylinder.draw(width.getValue(), 0.0F, height.getValue(), 40, 15);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glRotated(90, 1, 0, 0);
        GL11.glTranslatef(0, 0, -1.9F);
        disk.draw(0, width.getValue(), 40, 15);
        GL11.glPopMatrix();

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}