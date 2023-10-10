package vestige.module;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiChat;
import vestige.event.Listener;
import vestige.event.impl.RenderEvent;
import vestige.setting.impl.DoubleSetting;

public abstract class HUDModule extends Module {

    public final DoubleSetting posX;
    public final DoubleSetting posY;

    @Getter
    protected int width, height;

    @Getter
    @Setter
    private boolean holdingMouse;

    @Getter
    protected AlignType alignType;

    public HUDModule(String name, Category category, double defaultX, double defaultY, int width, int height, AlignType alignType) {
        super(name, category);

        posX = new DoubleSetting("Pos X", () -> false, defaultX, 0, 1000, 0.5);
        posY = new DoubleSetting("Pos Y", () -> false, defaultY, 0, 1000, 0.5);

        this.width = width;
        this.height = height;

        this.alignType = alignType;

        this.listenType = EventListenType.MANUAL;
        this.startListening();

        this.addSettings(posX, posY);
    }

    @Listener
    public final void onRender(RenderEvent event) {
        boolean inChat = mc.currentScreen instanceof GuiChat;

        if(this.isEnabled() || inChat) {
            renderModule(inChat);
        }
    }

    protected abstract void renderModule(boolean inChat);
}
