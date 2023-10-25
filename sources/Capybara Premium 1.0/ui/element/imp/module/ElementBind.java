package fun.expensive.client.ui.element.imp.module;

import org.lwjgl.input.Keyboard;
import ru.alone.ui.element.Element;
import ru.alone.ui.element.imp.panel.ElementSettings;
import ru.alone.utils.other.font.Fonts;

import java.awt.*;

public class ElementBind extends Element {

    private ElementSettings settings;

    private boolean listening;

    public ElementBind(ElementSettings settings){
        this.settings = settings;
        this.setWidth(settings.getWidth());
        this.setHeight(20);
    }

    @Override
    public void render(int width, int height, int x, int y, float ticks) {
        String display = "Keybind: " + Keyboard.getKeyName(settings.module.getKey());
        if(listening) display = "Keybind: Press Any key...";
        Fonts.Monstserrat.drawString(display, (int)this.x, (int)this.y+5, new Color(0x7C7C7C).getRGB(), false);
        super.render(width, height, x, y, ticks);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        if(collided(x, y) && button == 0){
            this.listening = true;
        }
        super.mouseClicked(x, y, button);
    }

    @Override
    public void keypressed(char c, int key) {
        if(listening){
            if(key != Keyboard.KEY_DELETE){
                settings.module.setKey(key);
            }else settings.module.setKey(Keyboard.KEY_NONE);
            listening = false;
        }
        super.keypressed(c, key);
    }

    @Override
    public void mouseRealesed(int x, int y, int button) {
        super.mouseRealesed(x, y, button);
    }
}
