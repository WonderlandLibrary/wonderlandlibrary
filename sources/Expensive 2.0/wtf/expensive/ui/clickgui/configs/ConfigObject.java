package wtf.expensive.ui.clickgui.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import jhlabs.image.PixelUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFW;
import wtf.expensive.config.Config;
import wtf.expensive.managment.Managment;
import wtf.expensive.ui.midnight.Style;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.font.Fonts;
import wtf.expensive.util.misc.TimerUtil;
import wtf.expensive.util.render.BloomHelper;
import wtf.expensive.util.render.ColorUtil;
import wtf.expensive.util.render.GaussianBlur;
import wtf.expensive.util.render.RenderUtil;
import wtf.expensive.util.render.animation.AnimationMath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static wtf.expensive.config.ConfigManager.CONFIG_DIR;
import static wtf.expensive.config.ConfigManager.compressAndWrite;

public class ConfigObject {

    public float x, y, width, height;


    public String cfg;
    public String staticF;
    private String author;
    private String formattedDate;

    public float animationx;
    public float animationy;
    public float animationz;

    public TimerUtil timerUtil = new TimerUtil();

    public ConfigObject(String cfg) {
        this.staticF = cfg;
        this.cfg = cfg;
        author = "Unknown.";
        Config config = Managment.CONFIG_MANAGER.findConfig(this.cfg);
        JsonElement element = compressAndWrite(config.getFile());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = "2000-01-01";
        if (element != null && !element.isJsonNull() && element.isJsonObject()) {
            JsonObject object = element.getAsJsonObject();
            if (object.has("Others")) {
                JsonObject others = object.getAsJsonObject("Others");
                if (others.has("author"))
                    author = others.get("author").getAsString();
                if (others.has("time"))
                    formattedDate = dateFormat.format(new Date(others.get("time").getAsLong()));
            }
        } else {
            author = "Unknown.";
        }

    }



    public void draw(MatrixStack stack, int mouseX, int mouseY) {
        if (timerUtil.hasTimeElapsed(1000)) {
            clicked = 0;
        }
        RenderUtil.Render2D.drawRoundOutline(x, y, width, height, 5f, 0f, ColorUtil.rgba(25, 26, 33, 0), new Vector4i(
                ColorUtil.rgba(38, 40, 59, 255)
        ));
        Fonts.msSemiBold[20].drawString(stack, cfg + (nameChange ? System.currentTimeMillis() % 1000 > 500 ? "" : "_" : ""), x + 8, y + 8, -1);

        float offset = 20;
        animationx = AnimationMath.fast(animationx, RenderUtil.isInRegion(mouseX,mouseY,x + width - offset * 3, y + height - offset, 29 / 2f, 29 /2f) ? 1 : 0, 10);
        animationy = AnimationMath.fast(animationy, RenderUtil.isInRegion(mouseX,mouseY,x + width - offset * 2, y + height - offset, 29 / 2f, 29 /2f) ? 1 : 0, 10);
        animationz = AnimationMath.fast(animationz, RenderUtil.isInRegion(mouseX,mouseY,x + width - offset * 1, y + height - offset, 29 / 2f, 29 /2f) ? 1 : 0, 10);
        RenderUtil.Render2D.drawRoundedRect(x + width - offset, y + height - offset, 29 / 2f, 29 /2f, 4,ColorUtil.rgba(0,0,0,255 * 0.33F));
        RenderUtil.Render2D.drawRoundedRect(x + width - offset * 2, y + height - offset, 29 / 2f, 29 /2f, 4,ColorUtil.rgba(0,0,0,255 * 0.33F));
        RenderUtil.Render2D.drawRoundedRect(x + width - offset * 3, y + height - offset, 29 / 2f, 29 /2f, 4,ColorUtil.rgba(0,0,0,255 * 0.33F));
        Fonts.configIcon[16].drawString(stack, "J", x + width - offset * 3 + 3, y + height - offset + 6, -1);
        Fonts.configIcon[16].drawString(stack, "M", x + width - offset * 2 + 3.5f, y + height - offset + 6, -1);
        Fonts.configIcon[16].drawString(stack, "I", x + width - offset * 1 + 3, y + height - offset + 6, -1);
        BloomHelper.registerRenderCall(() -> {
            Fonts.configIcon[16].drawString(stack, "J", x + width - offset * 3 + 3, y + height - offset + 6, RenderUtil.reAlphaInt(-1, (int) (255 * animationx)));
            Fonts.configIcon[16].drawString(stack, "M", x + width - offset * 2 + 3.5f, y + height - offset + 6, RenderUtil.reAlphaInt(-1, (int) (255 * animationy)));
            Fonts.configIcon[16].drawString(stack, "I", x + width - offset * 1 + 3, y + height - offset + 6, RenderUtil.reAlphaInt(-1, (int) (255 * animationz)));
        });

        Fonts.msMedium[12].drawString(stack, "Author: ", x + 6,y + height - 10,ColorUtil.rgba(161, 164, 177, 255));
        Fonts.msMedium[12].drawString(stack, author, x + 6 + Fonts.msMedium[12].getWidth("Author: "),y + height - 10,ColorUtil.rgba(255, 255, 255, 255));
        Fonts.msMedium[12].drawString(stack, "Created: ", x + 6,y + height - 20,ColorUtil.rgba(161, 164, 177, 255));
        Fonts.msMedium[12].drawString(stack, formattedDate, x + 6 + Fonts.msMedium[12].getWidth("Created: "),y + height - 20,ColorUtil.rgba(255, 255, 255, 255));

    }


    boolean nameChange;


    int clicked;



    public void charTyped(char chars) {
        if (nameChange &&cfg.length() < 15 ) {
            cfg += chars;
        }
    }

    public void keyTyped(int key) {
        if (key == GLFW.GLFW_KEY_ENTER) {
            if (nameChange) {
                nameChange = false;
                Config cfg = Managment.CONFIG_MANAGER.findConfig(staticF);
                if (cfg != null) {
                    cfg.getFile().renameTo(new File(CONFIG_DIR, this.cfg + ".cfg"));
                    staticF = this.cfg;
                }
            }
        }
        if (key == GLFW.GLFW_KEY_BACKSPACE) {
            if (nameChange) {
                if (!cfg.isEmpty()) {
                    cfg = cfg.substring(0, cfg.length() - 1);
                }
            }
        }
    }

    public void click(int mx, int my) {
        float offset = 20;
        if (RenderUtil.isInRegion(mx,my,x + width - offset * 3, y + height - offset, 29 / 2f, 29 /2f)) {
            try {
                Files.delete(Managment.CONFIG_MANAGER.findConfig(cfg).getFile().toPath());
            } catch (IOException e) {
                System.out.println("Config UI: " + e.getMessage() );
            }
            ConfigDrawing.configDrawing.objects.remove(this);
        }

        if (RenderUtil.isInRegion(mx, my, x,y, width,height)){
            timerUtil.reset();
            clicked++;
            if (clicked >= 2) {
                nameChange = true;
                ConfigDrawing.configDrawing.searching = false;
            }
        }
        if (RenderUtil.isInRegion(mx,my,x + width - offset * 2, y + height - offset, 29 / 2f, 29 /2f)) {
            Managment.CONFIG_MANAGER.saveConfiguration(cfg);
            ClientUtil.sendMesage("Сохранил конфиг " + cfg);
        }
        if (RenderUtil.isInRegion(mx,my,x + width - offset * 1, y + height - offset, 29 / 2f, 29 /2f)) {
            Managment.CONFIG_MANAGER.loadConfiguration(cfg, false);
            ClientUtil.sendMesage("Загрузил конфиг " + cfg);
        }
    }


}
