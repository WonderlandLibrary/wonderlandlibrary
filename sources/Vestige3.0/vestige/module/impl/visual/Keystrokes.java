package vestige.module.impl.visual;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.module.AlignType;
import vestige.module.Category;
import vestige.module.HUDModule;
import vestige.setting.impl.ModeSetting;
import vestige.util.animation.AnimationHolder;
import vestige.util.animation.AnimationType;
import vestige.util.render.FontUtil;

import java.util.ArrayList;

public class Keystrokes extends HUDModule {

    private final ModeSetting font = FontUtil.getFontSetting();

    private final ArrayList<AnimationHolder<KeyBinding>> keys = new ArrayList<>();

    private boolean keysInitiated;

    private ClientTheme theme;

    public Keystrokes() {
        super("Keystrokes", Category.VISUAL, 15, 35, 70, 70, AlignType.LEFT);
        this.addSettings(font);
    }

    @Override
    public void onEnable() {
        if(!keysInitiated) {
            initKeys();
            keysInitiated = true;
        }
    }

    @Override
    public void onClientStarted() {
        theme = Vestige.instance.getModuleManager().getModule(ClientTheme.class);
    }

    private void initKeys() {
        keys.clear();

        keys.add(new AnimationHolder<>(mc.gameSettings.keyBindForward));
        keys.add(new AnimationHolder<>(mc.gameSettings.keyBindLeft));
        keys.add(new AnimationHolder<>(mc.gameSettings.keyBindBack));
        keys.add(new AnimationHolder<>(mc.gameSettings.keyBindRight));
        keys.add(new AnimationHolder<>(mc.gameSettings.keyBindJump));
    }

    @Override
    public void renderModule(boolean inChat) {
        int x = (int) posX.getValue();
        int y = (int) posY.getValue();

        if(!keysInitiated) {
            initKeys();
            keysInitiated = true;
        }

        int index = 0;

        int length = 20;
        int spacing = 5;

        int currentX = x + length + spacing;
        int currentY = y;

        for(AnimationHolder<KeyBinding> key : keys) {
            key.setAnimType(AnimationType.POP);
            key.setAnimDuration(150);

            if(index == 4) {
                currentX = x;
                currentY = y + (length + spacing) * 2;

                int totalLength = length * 3 + spacing * 2;

                Gui.drawRect(currentX, currentY, currentX + totalLength, currentY + length * 0.85, 0x60000000);

                int finalRenderX = currentX;
                int finalRenderY = currentY;

                key.updateState(key.get().isKeyDown());

                if(key.isRendered() || !key.isAnimDone()) {
                    key.render(() -> Gui.drawRect(finalRenderX, finalRenderY, finalRenderX + totalLength, finalRenderY + length * 0.85, theme.getColor(100 + finalRenderX + length + spacing)), finalRenderX, finalRenderY, finalRenderX + totalLength, finalRenderY + length * 0.85F);
                }

                String toRender = "___";

                FontUtil.drawStringWithShadow(font.getMode(), toRender, (float) (currentX + totalLength / 2 - FontUtil.getStringWidth(font.getMode(), toRender) / 2), currentY + 1, -1);
            } else {
                if(index == 1) {
                    currentX = x;
                    currentY = y + length + spacing;
                }

                Gui.drawRect(currentX, currentY, currentX + length, currentY + length, 0x60000000);

                int finalRenderX = currentX;
                int finalRenderY = currentY;

                key.updateState(key.get().isKeyDown());

                if(key.isRendered() || !key.isAnimDone()) {
                    key.render(() -> Gui.drawRect(finalRenderX, finalRenderY, finalRenderX + length, finalRenderY + length, theme.getColor(100 + finalRenderX * 3)), finalRenderX, finalRenderY, finalRenderX + length, finalRenderY + length);
                }

                String keyName = Keyboard.getKeyName(key.get().getKeyCode());

                FontUtil.drawStringWithShadow(font.getMode(), keyName, (float) (currentX + length / 2 - FontUtil.getStringWidth(font.getMode(), keyName) / 2), currentY + 6, -1);

                currentX += length + spacing;

                index++;
            }
        }
    }

}
