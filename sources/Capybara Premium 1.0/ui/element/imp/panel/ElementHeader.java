package fun.expensive.client.ui.element.imp.panel;

import net.minecraft.client.renderer.texture.DynamicTexture;
import ru.alone.module.imp.render.HUD;
import ru.alone.ui.Panel;
import ru.alone.userdata.Userdata;
import ru.alone.utils.RenderUtils;
import ru.alone.utils.other.font.Fonts;
import ru.alone.ui.element.Element;
import ru.alone.Alone;
import ru.alone.utils.other.TextureEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ElementHeader extends Element {

    private ru.alone.ui.Panel panel;
    DynamicTexture texture;

    private List<ElementCategory> categoryList = new ArrayList<ElementCategory>();
    private TextureEngine logo = new TextureEngine("https://i.ibb.co/VSQZBJD/logo-1.png", Alone.scaleUtils, 50, 49);
    private TextureEngine user = new TextureEngine("https://i.ibb.co/s1bZ6Rx/image-1.png", Alone.scaleUtils, 30, 30);

    public ElementSearch search = new ElementSearch(this);

    public ElementHeader(Panel panel) {
        this.panel = panel;
        setWidth(panel.getWidth());
        setHeight(28);
    }

    public void updateScreen(){
        search.updateScreen();
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {

        RenderUtils.drawBlurredShadow((int) this.x - 2, (float) this.y + 13, (int) ((float) this.width) + 4, 240, 15, new Color(0x93131313, true));

        RenderUtils.drawRect(this.x + this.width - 2, this.y + this.height - 1+15, this.x + this.width - 1, this.y + this.height+10, new Color(0x1A1A1A).getRGB());
        RenderUtils.drawRoundedRect((int) this.x, (float) this.y+15, (int) ((float) this.x + (float) this.width), (float) this.y + (float) this.height + 20, 1.5f, new Color(0x181818).getRGB());
        String title = Alone.CLIENT_NAME;
        HUD hud = (HUD) Alone.moduleManager.getModule(HUD.class);
        Fonts.Esp.drawBlurredString(title, (float) this.x  + 15 , (float) this.y + 24.5f, 16, new Color(hud.color.color.getRed(), hud.color.color.getGreen(), hud.color.color.getBlue(), 130), new Color(hud.color.color.getRed(), hud.color.color.getGreen(), hud.color.color.getBlue()).getRGB());

        search.setX(this.getX() + 90);
        search.setY(this.getY() + getHeight() - (getHeight() / 2) + search.getHeight() / 2);

        search.render(width, height, x, y, ticks);

        //   logo.bind((int) this.getX() + 5, (int) this.getY() + 15);

        user.bind((int) (this.getX() + this.getWidth() -30), (int) this.getY() + 19);
        Fonts.Monstserrat17.drawString(Userdata.instance().getName(), (float) (this.getX() + this.getWidth() -30 - Fonts.Monstserrat17.getStringWidth(Userdata.instance().getName())), (float) (this.getY() + 25F),-1);

        int offset = 0;
        for (ElementCategory e : categoryList) {
            e.x = this.x + (this.width / 2 - 50) + offset;
            e.y = this.y + this.height / 2 - 6;
            offset += e.getWidth() + 4;
        }


        categoryList.forEach(c -> c.render(width, height, x, y, ticks));

        super.render(width, height, x, y, ticks);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        search.mouseClicked(x, y, button);

        if (collided(x, y-14)) {
            panel.x2 = panel.x - x;
            panel.y2 = panel.y - y;
            panel.dragging = true;
        }
        categoryList.forEach(c -> c.mouseClicked(x, y, button));
        super.mouseClicked(x, y, button);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        panel.dragging = false;
        categoryList.forEach(c -> c.mouseRealesed(x, y, button));
        super.mouseRealesed(x, y, button);
    }

    @Override
    public void keypressed(char c, int key) {
        search.keypressed(c, key);
        super.keypressed(c, key);
    }
}
