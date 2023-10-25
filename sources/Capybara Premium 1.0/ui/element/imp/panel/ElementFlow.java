package fun.expensive.client.ui.element.imp.panel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.alone.module.imp.render.ESP;
import ru.alone.setting.imp.SettingBoolean;
import ru.alone.ui.Panel;
import ru.alone.ui.element.imp.module.ElementModule;
import ru.alone.userdata.Userdata;
import ru.alone.utils.ColorUtils;
import ru.alone.utils.RenderUtils;
import ru.alone.Alone;
import ru.alone.module.Module;
import ru.alone.ui.element.Element;
import ru.alone.utils.other.font.Fonts;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ElementFlow extends Element {

    public ru.alone.ui.Panel panel;

    private final List<ElementCategory> elementCategories = new ArrayList<ElementCategory>();

    public static List<ElementModule> searchModules = new ArrayList<ElementModule>();

    public ElementSettings settings;
    private float scroll = 0;

    public ElementFlow(Panel panel) {
        this.panel = panel;
        this.setWidth(panel.getWidth());
        this.setHeight(220);
        for (Module.Category c : Module.Category.values()) {
            elementCategories.add(new ElementCategory(this, c));
        }
    }

    @Override
    public void handleMouseInput() {
        if (Mouse.hasWheel()) {
            int mouse = Mouse.getDWheel();
            if (mouse > 0) {
                scroll += 15;
            } else {
                if (mouse < 0) {
                    scroll -= 15;
                }
            }
        }
        super.handleMouseInput();
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        ESP esp = (ESP) Alone.moduleManager.getModule(ESP.class);

        if (scroll >= 0) {
            scroll = 0;
        }
        RenderUtils.drawRect(this.x, this.y + 12, this.x + this.width, this.y + this.height - 20, new Color(0x131313).getRGB());

        RenderUtils.drawRoundedRect((int) this.x, (float) this.y + 12, (int) ((float) this.x + (float) this.width), (float) this.y + (float) this.height, 1.5f, new Color(0x131313).getRGB());

        if(settings == null) {

            String end_time_text = "Lisense end: " + Userdata.instance().getLicenseDate();

            Fonts.Monstserrat17.drawString(end_time_text, (int) this.getX() + (int) this.getWidth() - Fonts.Monstserrat17.getStringWidth(end_time_text) - 3, (int) this.getY() + (int) this.getHeight() - 10, -1);
            String uid_text = "UID: " + Userdata.instance().getUID();

            Fonts.Monstserrat17.drawString(uid_text, (int) this.getX() + (int) this.getWidth() - Fonts.Monstserrat17.getStringWidth(uid_text) - 3, (int) this.getY() + (int) this.getHeight() - 20, -1);


        }
        GL11.glPushMatrix();

        int element_x = 0;
        int element_y = 0;

        int offsetWide = 4;
        int offsetHeight = 20;

            for (ElementCategory elementCategory : elementCategories) {

                element_x += elementCategory.getWidth() + offsetWide;
                if (element_x / (elementCategory.getWidth() + offsetWide) > 2) {
                    element_x = (int) elementCategory.getWidth() + offsetWide;
                    element_y += elementCategory.getHeight() + offsetHeight;
                }

                elementCategory.x = this.x + 10 + element_x - (elementCategory.getWidth() + offsetWide);
                elementCategory.y = this.y + scroll + 15 + element_y;

        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int factor = Alone.scaleUtils.getScale();


        RenderUtils.prepareScissorBox(factor, Alone.scaleUtils.calc(sr.getScaledHeight()), (int) this.x, (int) this.y + 15, (int) this.x + (int) this.width, (int) this.y + (int) this.height);


        ElementHeader header = panel.getElements().stream().filter(e -> e instanceof ElementHeader).map(e -> (ElementHeader)e).findAny().get();

        String text = header.search.field.getText();

        for(ElementCategory elementCategory : elementCategories){
            for(ElementModule elementModule : elementCategory.elementModuleList){
                boolean flag = elementModule.module.getName().toLowerCase().startsWith(text.toLowerCase()) || elementModule.module.getName().toLowerCase().contains(text.toLowerCase());
                if(flag && text.length() > 0) {
                    ElementModule elementModule_new = new ElementModule(elementModule.module, elementCategory);
                    if(!contains(elementModule_new)) {
                        searchModules.add(elementModule_new);
                    }else searchModules.remove(elementModule_new);
                }
            }
        }

        if(text.length() == 0) {
            elementCategories.forEach(e -> e.render(width, height, x, y, ticks));
        }else {

            int element_x_m = 0;
            int element_y_m = 0;
            for (ElementModule elementModule : searchModules) {

                element_x_m += elementModule.getWidth() + offsetWide;

                if (element_x_m / (elementModule.getWidth() + offsetWide) > 2) {
                    element_x_m = (int) elementModule.getWidth() + offsetWide;
                    element_y_m += elementModule.getHeight();
                }
                elementModule.x = this.x + 10 + element_x_m - (elementModule.getWidth() + offsetWide);
                elementModule.y = this.y + 15 + element_y_m;
                elementModule.render(width, height, x, y, ticks);
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();


        int offset = 0;

        if(settings != null) offset = 0; else offset = -135;
        if(esp.isEnabled())  renderPlayer(esp, offset);
        if (settings != null) {
            settings.x = this.x + this.width - settings.getWidth() - 10;
            settings.y = this.y + 15;
            settings.render(width, height, x, y, ticks);
        }

      //  String endtime_text = "Лицензия до: " + Userdata.instance().getLicenseDate();


        super.render(width, height, x, y, ticks);
    }
    public boolean contains(ElementModule button) {
        for(ElementModule b : searchModules) {
            if(b.module.getName().toLowerCase().equalsIgnoreCase(button.module.getName())) {
                return true;
            }
        }
        return false;
    }
    public void renderPlayer(ESP esp, int offset){

        int offset_y = 10;

        RenderUtils.drawBlurredShadow((int) (this.x + this.width + 10) + offset, (float) (this.y + 10) + offset_y, (int) (110), (float) 171, 15, new Color(0x131313));
        RenderUtils.drawRoundedRect((int) (this.x + this.width + 10) + offset, (float) (this.y + 10)+ offset_y, (int) (this.x + this.width + 120) + offset, (float) (this.y + 180), 1.5f, new Color(0x131313).getRGB());

        ColorUtils.glColor(new Color(255,255,255),255);

        GuiInventory.drawEntityOnScreen((int) (this.x + this.width + 64) + offset, (int) this.y + 165 + offset_y, 75, 0, 0, mc.player);


        Gui.drawRect((int) (this.x + this.width + 28) + offset, (int) (this.y + 20) + offset_y, (int) (this.x + this.width + 29) + offset, (int) (this.y + 170) + offset_y, esp.color.color.getRGB());
        Gui.drawRect((int) (this.x + this.width + 100) + offset, (int) (this.y + 20) + offset_y, (int) (this.x + this.width + 101) + offset, (int) (this.y + 170) + offset_y, esp.color.color.getRGB());

        Gui.drawRect((int) (this.x + this.width + 28) + offset, (int) (this.y + 20) + offset_y, (int) (this.x + this.width + 101) + offset, (int) (this.y + 21) + offset_y, esp.color.color.getRGB());
        Gui.drawRect((int) (this.x + this.width + 28) + offset, (int) (this.y + 169) + offset_y, (int) (this.x + this.width + 101) + offset, (int) (this.y + 170) + offset_y, esp.color.color.getRGB());


        Gui.drawRect((int) (this.x + this.width + 25) + offset, (int) (this.y + 20) + offset_y, (int) (this.x + this.width + 26) + offset, (int) (this.y + 170) + offset_y, new Color(11, 161, 10).getRGB());
    }

    public static void drawEntityOnScreen(float x, float y, float scale, EntityLivingBase entityLivingBase) {
        GlStateManager.pushMatrix();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableColorMaterial();
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        ColorUtils.glColor(new Color(255,255,255),255);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (entityLivingBase.rotationPitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(entityLivingBase, 0.0D, 0.0D, 0.0D, entityLivingBase.rotationYaw, 1.0F, false);
        rendermanager.setRenderShadow(true);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.popMatrix();
    }
    @Override
    public void keypressed(char c, int key) {
        if (settings != null) settings.keypressed(c, key);
        searchModules.forEach(m -> m.keypressed(c, key));
        super.keypressed(c, key);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        elementCategories.forEach(e -> e.mouseClicked(x, y, button));
        searchModules.forEach(m -> m.mouseClicked(x, y, button));
        if (settings != null) settings.mouseClicked(x, y, button);
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        elementCategories.forEach(e -> e.mouseRealesed(x, y, button));
        searchModules.forEach(m -> m.mouseRealesed(x, y, button));

        if (settings != null) settings.mouseRealesed(x, y, button);
        super.mouseRealesed(x, y, button);
    }
}
