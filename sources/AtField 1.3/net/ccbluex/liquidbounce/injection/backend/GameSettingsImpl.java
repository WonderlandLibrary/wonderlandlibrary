/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.functions.Function1
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.WEnumPlayerModelParts;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IGameSettings;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.ccbluex.liquidbounce.api.util.WrappedSet;
import net.ccbluex.liquidbounce.injection.backend.GameSettingsImpl;
import net.ccbluex.liquidbounce.injection.backend.KeyBindingImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.BackendExtentionsKt$WhenMappings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.jetbrains.annotations.Nullable;

public final class GameSettingsImpl
implements IGameSettings {
    private final GameSettings wrapped;

    @Override
    public IKeyBinding getKeyBindBack() {
        KeyBinding keyBinding = this.wrapped.field_74368_y;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public void setGammaSetting(float f) {
        this.wrapped.field_74333_Y = f;
    }

    public final GameSettings getWrapped() {
        return this.wrapped;
    }

    @Override
    public void setModelPartEnabled(WEnumPlayerModelParts wEnumPlayerModelParts, boolean bl) {
        EnumPlayerModelParts enumPlayerModelParts;
        WEnumPlayerModelParts wEnumPlayerModelParts2 = wEnumPlayerModelParts;
        GameSettings gameSettings = this.wrapped;
        boolean bl2 = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[wEnumPlayerModelParts2.ordinal()]) {
            case 1: {
                enumPlayerModelParts = EnumPlayerModelParts.CAPE;
                break;
            }
            case 2: {
                enumPlayerModelParts = EnumPlayerModelParts.JACKET;
                break;
            }
            case 3: {
                enumPlayerModelParts = EnumPlayerModelParts.LEFT_SLEEVE;
                break;
            }
            case 4: {
                enumPlayerModelParts = EnumPlayerModelParts.RIGHT_SLEEVE;
                break;
            }
            case 5: {
                enumPlayerModelParts = EnumPlayerModelParts.LEFT_PANTS_LEG;
                break;
            }
            case 6: {
                enumPlayerModelParts = EnumPlayerModelParts.RIGHT_PANTS_LEG;
                break;
            }
            case 7: {
                enumPlayerModelParts = EnumPlayerModelParts.HAT;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        EnumPlayerModelParts enumPlayerModelParts2 = enumPlayerModelParts;
        gameSettings.func_178878_a(enumPlayerModelParts2, bl);
    }

    public GameSettingsImpl(GameSettings gameSettings) {
        this.wrapped = gameSettings;
    }

    @Override
    public Set getModelParts() {
        return new WrappedSet(this.wrapped.func_178876_d(), (Function1)modelParts.1.INSTANCE, (Function1)modelParts.2.INSTANCE);
    }

    @Override
    public int getGuiScale() {
        return (int)this.wrapped.field_74333_Y;
    }

    public void setGuiScale(int n) {
        this.wrapped.field_74333_Y = n;
    }

    @Override
    public boolean isKeyDown(IKeyBinding iKeyBinding) {
        IKeyBinding iKeyBinding2 = iKeyBinding;
        boolean bl = false;
        return GameSettings.func_100015_a((KeyBinding)((KeyBindingImpl)iKeyBinding2).getWrapped());
    }

    @Override
    public IKeyBinding getKeyBindSprint() {
        KeyBinding keyBinding = this.wrapped.field_151444_V;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public IKeyBinding getKeyBindRight() {
        KeyBinding keyBinding = this.wrapped.field_74366_z;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public float getMouseSensitivity() {
        return this.wrapped.field_74341_c;
    }

    @Override
    public boolean getEntityShadows() {
        return this.wrapped.field_181151_V;
    }

    @Override
    public IKeyBinding getKeyBindJump() {
        KeyBinding keyBinding = this.wrapped.field_74314_A;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public float getGammaSetting() {
        return this.wrapped.field_74333_Y;
    }

    @Override
    public IKeyBinding getKeyBindSneak() {
        KeyBinding keyBinding = this.wrapped.field_74311_E;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public IKeyBinding getKeyBindAttack() {
        KeyBinding keyBinding = this.wrapped.field_74312_F;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public IKeyBinding getKeyBindUseItem() {
        KeyBinding keyBinding = this.wrapped.field_74313_G;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public void setEntityShadows(boolean bl) {
        this.wrapped.field_181151_V = bl;
    }

    @Override
    public IKeyBinding getKeyBindLeft() {
        KeyBinding keyBinding = this.wrapped.field_74370_x;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    @Override
    public IKeyBinding getKeyBindForward() {
        KeyBinding keyBinding = this.wrapped.field_74351_w;
        boolean bl = false;
        return new KeyBindingImpl(keyBinding);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof GameSettingsImpl && ((GameSettingsImpl)object).wrapped.equals(this.wrapped);
    }
}

