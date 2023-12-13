/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.settings.GameSettings;
import optifine.IOptionControl;

public class GuiOptionButtonOF
extends GuiOptionButton
implements IOptionControl {
    private GameSettings.Options option = null;

    public GuiOptionButtonOF(int id, int x, int y, GameSettings.Options option, String text) {
        super(id, x, y, option, text);
        this.option = option;
    }

    @Override
    public GameSettings.Options getOption() {
        return this.option;
    }
}

