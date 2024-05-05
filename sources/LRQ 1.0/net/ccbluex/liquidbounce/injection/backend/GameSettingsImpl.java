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
    public boolean getEntityShadows() {
        return this.wrapped.field_181151_V;
    }

    @Override
    public void setEntityShadows(boolean value) {
        this.wrapped.field_181151_V = value;
    }

    @Override
    public float getGammaSetting() {
        return this.wrapped.field_74333_Y;
    }

    @Override
    public void setGammaSetting(float value) {
        this.wrapped.field_74333_Y = value;
    }

    @Override
    public Set<WEnumPlayerModelParts> getModelParts() {
        return new WrappedSet(this.wrapped.func_178876_d(), (Function1)modelParts.1.INSTANCE, (Function1)modelParts.2.INSTANCE);
    }

    @Override
    public float getMouseSensitivity() {
        return this.wrapped.field_74341_c;
    }

    @Override
    public IKeyBinding getKeyBindAttack() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74312_F;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindUseItem() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74313_G;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindJump() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74314_A;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindSneak() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74311_E;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindForward() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74351_w;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindBack() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74368_y;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindRight() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74366_z;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindLeft() {
        KeyBinding $this$wrap$iv = this.wrapped.field_74370_x;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public IKeyBinding getKeyBindSprint() {
        KeyBinding $this$wrap$iv = this.wrapped.field_151444_V;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public boolean isKeyDown(IKeyBinding key) {
        IKeyBinding $this$unwrap$iv = key;
        boolean $i$f$unwrap = false;
        return GameSettings.func_100015_a((KeyBinding)((KeyBindingImpl)$this$unwrap$iv).getWrapped());
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setModelPartEnabled(WEnumPlayerModelParts modelParts2, boolean enabled) {
        EnumPlayerModelParts enumPlayerModelParts;
        void $this$unwrap$iv;
        WEnumPlayerModelParts wEnumPlayerModelParts = modelParts2;
        GameSettings gameSettings = this.wrapped;
        boolean $i$f$unwrap = false;
        switch (BackendExtentionsKt$WhenMappings.$EnumSwitchMapping$1[$this$unwrap$iv.ordinal()]) {
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
        gameSettings.func_178878_a(enumPlayerModelParts2, enabled);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof GameSettingsImpl && ((GameSettingsImpl)other).wrapped.equals(this.wrapped);
    }

    public final GameSettings getWrapped() {
        return this.wrapped;
    }

    public GameSettingsImpl(GameSettings wrapped) {
        this.wrapped = wrapped;
    }
}

