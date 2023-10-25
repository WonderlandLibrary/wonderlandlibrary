/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.GameSettings;

public class GuiOptionButton
extends GuiButton {
    private final GameSettings.Options enumOptions;

    public GuiOptionButton(int n, int n2, int n3, int n4, int n5, String string) {
        super(n, n2, n3, n4, n5, string);
        this.enumOptions = null;
    }

    public GuiOptionButton(int n, int n2, int n3, String string) {
        this(n, n2, n3, null, string);
    }

    public GameSettings.Options returnEnumOptions() {
        return this.enumOptions;
    }

    public GuiOptionButton(int n, int n2, int n3, GameSettings.Options options, String string) {
        super(n, n2, n3, 150, 20, string);
        this.enumOptions = options;
    }
}

