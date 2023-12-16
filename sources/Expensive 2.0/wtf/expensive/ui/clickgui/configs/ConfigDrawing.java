package wtf.expensive.ui.clickgui.configs;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import org.apache.commons.lang3.RandomStringUtils;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.CallbackI;
import wtf.expensive.config.Config;
import wtf.expensive.config.ConfigManager;
import wtf.expensive.managment.Managment;
import wtf.expensive.ui.beta.ClickGui;
import wtf.expensive.ui.clickgui.Window;
import wtf.expensive.ui.clickgui.theme.ThemeObject;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.math.MathUtil;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.render.*;
import wtf.expensive.util.render.animation.AnimationMath;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ConfigDrawing {
    public static ConfigDrawing configDrawing = new ConfigDrawing();

    public CopyOnWriteArrayList<ConfigObject> objects = new CopyOnWriteArrayList<>();

    public ConfigDrawing() {
        objects.clear();
        for (String cfg : Managment.CONFIG_MANAGER.getAllConfigurations()) {
            objects.add(new ConfigObject(cfg));
        }
        configDrawing = this;
    }

    public TimerUtil refresh = new TimerUtil();


    float x,y,width,height;


    public String search = "";

    public boolean searching;

    public void draw(MatrixStack stack, int mouseX, int mouseY, float x,float y,float width ,float height) {
        if (refresh.hasTimeElapsed(1000)) {
            for (String cfg : Managment.CONFIG_MANAGER.getAllConfigurations()) {
                if (!objects.stream().map(o -> o.staticF).toList().contains(cfg))
                    objects.add(new ConfigObject(cfg));
            }
            objects.removeIf(object -> Managment.CONFIG_MANAGER.findConfig(object.staticF) == null);
            refresh.reset();
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        float offsetX = 10;
        float offsetY = 25 + 25 + Window.scrollingOut;

        RenderUtil.Render2D.drawRoundedCorner(x + 1, y, width - 2, 33, new Vector4f(0,5,5,5), new Vector4i(
                ColorUtil.rgba(63, 72, 103, 25),
                ColorUtil.rgba(19, 23, 39, 25),
                ColorUtil.rgba(63, 72, 103, 25),
                ColorUtil.rgba(19, 23, 39, 25)
        ));
        OutlineUtils.registerRenderCall(() -> { RenderUtil.Render2D.drawRoundedCorner(x + 1, y, width - 2, 33, new Vector4f(0,7,7,7),
                ColorUtil.rgba(38, 40, 59, 255)
        );});

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(x,y + 33, width, height - 33);


        GaussianBlur.startBlur();
        float size = 0;
        for (ConfigObject object : objects) {
                if (!object.cfg.toLowerCase().contains(search.toLowerCase())) {
                    continue;
            }
            object.x = x + offsetX;
            object.y = y + offsetY;
            object.width = 313 / 2f;
            object.height = 97 / 2f;
            RenderUtil.Render2D.drawRoundOutline(object.x, object.y, object.width, object.height, 5f, 0f, ColorUtil.rgba(25, 26, 33, 255), new Vector4i(
                    ColorUtil.rgba(38, 40, 59,255), ColorUtil.rgba(38, 40, 59,255), ColorUtil.rgba(38, 40, 59,255), ColorUtil.rgba(38, 40, 59,255)
            ));
            offsetX += object.width + 32 / 2f;
            if (offsetX > 450 - 100 - object.width + 32 / 2f) {
                size += 97 / 2f + (55 - 48);
                offsetX = 10;
                offsetY += 55;
            }
        }


        if (size < 400 - 33 - 25) {
            Window.scrolling = 0;
            Window.scrollingOut = 0;
        } else {
            Window.scrolling = MathHelper.clamp(Window.scrolling, -(size - 250), 0);
        }

        GaussianBlur.endBlur(20, 2);


        for (ConfigObject object : objects) {
                if (!object.cfg.toLowerCase().contains(search.toLowerCase())) {
                    continue;
            }
            object.draw(stack,mouseX,mouseY);
        }
        SmartScissor.unset();
        SmartScissor.pop();
        Fonts.configIcon[22].drawString(stack, "H", x + 15,y + 13, -1);
        Fonts.configIcon[22].drawString(stack, "L", x + 35,y + 13, -1);

        RenderUtil.Render2D.drawRoundedRect(x + 301 / 2f + 30 ,y + 10, 301 / 2f, 29 / 2f, 4, ColorUtil.rgba(0,0,0,128));

        if (!(searching || !search.isEmpty()))
            Fonts.msMedium[14].drawCenteredString(stack, "Поиск конфига", x + 301 / 2f + 30+ (301 / 2f) / 2f, y + 15, ColorUtil.rgba(121, 123, 134,255));

        OutlineUtils.draw(2);

        SmartScissor.push();
        SmartScissor.setFromComponentCoordinates(x + 301 / 2f + 30, y + 10, 301 / 2f, 29 / 2f);
        if (searching || !search.isEmpty()) {
            Fonts.msMedium[15].drawString(stack, search + (searching ? ((System.currentTimeMillis() % 1000 > 500) ? "" : "_") : ""), x + 301 / 2f + 35,y + 14, -1);
        }
        SmartScissor.unset();
        SmartScissor.pop();

    }


    public void click(int mouseX, int mouseY, int button) {
        for (ConfigObject object : objects) {
            object.click(mouseX,mouseY);
        }

        if (RenderUtil.isInRegion(mouseX,mouseY, x + 15,y + 10, 11, 11)) {
            Managment.CONFIG_MANAGER.saveConfiguration("NewConfig" + RandomStringUtils.randomAlphabetic(2));
            objects.clear();
            for (String cfg : Managment.CONFIG_MANAGER.getAllConfigurations()) {
                objects.add(new ConfigObject(cfg));
            }
        }

        if (RenderUtil.isInRegion(mouseX,mouseY,x + 301 / 2f + 30 ,y + 10, 301 / 2f, 29 / 2f)) {
            searching = !searching;
        }


        if (RenderUtil.isInRegion(mouseX,mouseY, x + 35,y + 10, 11, 11)) {
            try {
                Runtime.getRuntime().exec("explorer " + ConfigManager.CONFIG_DIR.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void charTyped(char chars) {
        for (ConfigObject object : objects) {
            object.charTyped(chars);
        }
    }

    public void keyTyped(int key) {
        for (ConfigObject object : objects) {
            object.keyTyped(key);
        }
    }

}
