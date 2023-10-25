package fun.expensive.client.ui.element.imp.module;

import fun.expensive.client.ui.element.Element;
import fun.expensive.client.ui.element.imp.panel.ElementCategory;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.alone.module.imp.render.HUD;
import ru.alone.ui.element.Element;
import ru.alone.ui.element.imp.panel.ElementCategory;
import ru.alone.utils.AnimationUtils;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.Alone;
import ru.alone.module.Module;
import ru.alone.ui.element.imp.panel.ElementSettings;
import ru.alone.utils.other.TextureEngine;

import java.awt.*;

public class ElementModule extends Element {

    public Module module;
    private ElementCategory category;
    int checkbox_width = 15;
    int checkbox_height = 5;
    private double animation;
    private int rotateIndex = 0;

    public ElementModule(Module module, ElementCategory category) {
        this.module = module;
        this.category = category;
        this.setWidth(category.getWidth());
        this.setHeight(15);
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        String display = module.getName();

        Fonts.Monstserrat.drawString(display, (int) this.x + 2, (int) this.y + 5, 0x7C7C7C, false);
        if (module.getSettings().size() > 0) {
            float xpos = (int) this.getX() + (int) this.getWidth() - 34;
            float ypos = (int) this.getY() + 2;
            GL11.glPushMatrix();
            // GL11.glTranslatef((float)xpos, (float)ypos, 0);
            //   GL11.glRotated(this.rotateIndex, 0, 0, 1);

            // GlStateManager.rotate(rotateIndex, 0, 0,1);

            //GL11.glTranslatef((float)-xpos, (float)-ypos, 0f);

            Alone.gearTexture.bind((int) xpos, (int) ypos);
            GlStateManager.popMatrix();
        }
        this.rotateIndex++;

        double max = module.isEnabled() ? 5 : checkbox_width;
        this.animation = AnimationUtils.animate(max, this.animation, 0.05);

        HUD hud = (HUD) Alone.moduleManager.getModule(HUD.class);

        RenderUtils.drawRect(this.x + this.width - checkbox_width, this.y + checkbox_height, this.x + this.width - 4, this.y + checkbox_height + 3, new Color(0x2D2D2D).getRGB());
        RenderUtils.roundedBorder((float) this.x + (float) this.width - (float) checkbox_width, (float) this.y + (float) checkbox_height, (float) this.x + (float) this.width - 4, (float) this.y + (float) checkbox_height + 3, 1f, new Color(0x2D2D2D).getRGB());
        RenderUtils.drawCircle((float) this.x + (float) this.width - (float) this.animation, (float) this.y + 6.5f, 0, 360, 3f, true, new Color(0x777777));
        if (module.isEnabled()) {
            RenderUtils.drawCircle((float) this.x + (float) this.width - (float) this.animation, (float) this.y + 6.5f, 0, 360, 3f, true, new Color(hud.color.color.getRed(), hud.color.color.getGreen(), hud.color.color.getBlue()));
            RenderUtils.drawBlurredShadowCircle((float) this.x + (float) this.width - (float) this.animation - 3.4f, (float) this.y + 3.8f, 6, 6, 8, new Color(hud.color.color.getRed(), hud.color.color.getGreen(), hud.color.color.getBlue()));

        }

        super.render(width, height, x, y, ticks);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {

        if (x >= this.x + this.width - checkbox_width - 3 && x <= this.x + this.width - 3 && y >= this.y + checkbox_height - 2 && y <= this.y + checkbox_height + 8 && button == 0) {
            module.toggle();
        }

        if (x >= (float) this.getX() + (int) this.getWidth() - 32 && x <= (float) this.getX() + (int) this.getWidth() - 22 && y >= (float) this.getY() + 2.5f && y <= (float) this.getY() + 10 && button == 0 && module.getSettings().size() > 0) {
            this.category.flow.settings = new ElementSettings(this.category.flow, module);
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        super.mouseRealesed(x, y, button);
    }
}
