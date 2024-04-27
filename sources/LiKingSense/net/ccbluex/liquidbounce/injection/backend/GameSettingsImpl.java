/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.NoWhenBranchMatchedException
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.Set;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010/\u001a\u00020\u00062\b\u00100\u001a\u0004\u0018\u000101H\u0096\u0002J\u0010\u00102\u001a\u00020\u00062\u0006\u00103\u001a\u00020\u0013H\u0016J\u0018\u00104\u001a\u0002052\u0006\u0010&\u001a\u00020(2\u0006\u00106\u001a\u00020\u0006H\u0016R$\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR$\u0010\r\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\f8V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0015R\u0014\u0010\u0018\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0015R\u0014\u0010\u001a\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001b\u0010\u0015R\u0014\u0010\u001c\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u0015R\u0014\u0010\u001e\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u001f\u0010\u0015R\u0014\u0010 \u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b!\u0010\u0015R\u0014\u0010\"\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b#\u0010\u0015R\u0014\u0010$\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b%\u0010\u0015R\u001a\u0010&\u001a\b\u0012\u0004\u0012\u00020(0'8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b)\u0010*R\u0014\u0010+\u001a\u00020\f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b,\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.\u00a8\u00067"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/GameSettingsImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "wrapped", "Lnet/minecraft/client/settings/GameSettings;", "(Lnet/minecraft/client/settings/GameSettings;)V", "value", "", "entityShadows", "getEntityShadows", "()Z", "setEntityShadows", "(Z)V", "", "gammaSetting", "getGammaSetting", "()F", "setGammaSetting", "(F)V", "keyBindAttack", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "getKeyBindAttack", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "keyBindBack", "getKeyBindBack", "keyBindForward", "getKeyBindForward", "keyBindJump", "getKeyBindJump", "keyBindLeft", "getKeyBindLeft", "keyBindRight", "getKeyBindRight", "keyBindSneak", "getKeyBindSneak", "keyBindSprint", "getKeyBindSprint", "keyBindUseItem", "getKeyBindUseItem", "modelParts", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/WEnumPlayerModelParts;", "getModelParts", "()Ljava/util/Set;", "mouseSensitivity", "getMouseSensitivity", "getWrapped", "()Lnet/minecraft/client/settings/GameSettings;", "equals", "other", "", "isKeyDown", "key", "setModelPartEnabled", "", "enabled", "LiKingSense"})
public final class GameSettingsImpl
implements IGameSettings {
    @NotNull
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
    @NotNull
    public Set<WEnumPlayerModelParts> getModelParts() {
        Set set = this.wrapped.func_178876_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)set, (String)"wrapped.modelParts");
        return new WrappedSet(set, (Function1)modelParts.1.INSTANCE, (Function1)modelParts.2.INSTANCE);
    }

    @Override
    public float getMouseSensitivity() {
        return this.wrapped.field_74341_c;
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindAttack() {
        KeyBinding keyBinding = this.wrapped.field_74312_F;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindAttack");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindUseItem() {
        KeyBinding keyBinding = this.wrapped.field_74313_G;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindUseItem");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindJump() {
        KeyBinding keyBinding = this.wrapped.field_74314_A;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindJump");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindSneak() {
        KeyBinding keyBinding = this.wrapped.field_74311_E;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindSneak");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindForward() {
        KeyBinding keyBinding = this.wrapped.field_74351_w;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindForward");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindBack() {
        KeyBinding keyBinding = this.wrapped.field_74368_y;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindBack");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindRight() {
        KeyBinding keyBinding = this.wrapped.field_74366_z;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindRight");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindLeft() {
        KeyBinding keyBinding = this.wrapped.field_74370_x;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindLeft");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    @NotNull
    public IKeyBinding getKeyBindSprint() {
        KeyBinding keyBinding = this.wrapped.field_151444_V;
        Intrinsics.checkExpressionValueIsNotNull((Object)keyBinding, (String)"wrapped.keyBindSprint");
        KeyBinding $this$wrap$iv = keyBinding;
        boolean $i$f$wrap = false;
        return new KeyBindingImpl($this$wrap$iv);
    }

    @Override
    public boolean isKeyDown(@NotNull IKeyBinding key) {
        Intrinsics.checkParameterIsNotNull((Object)key, (String)"key");
        IKeyBinding $this$unwrap$iv = key;
        boolean $i$f$unwrap = false;
        return GameSettings.func_100015_a((KeyBinding)((KeyBindingImpl)$this$unwrap$iv).getWrapped());
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void setModelPartEnabled(@NotNull WEnumPlayerModelParts modelParts2, boolean enabled) {
        EnumPlayerModelParts enumPlayerModelParts;
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)((Object)modelParts2), (String)"modelParts");
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
        return other instanceof GameSettingsImpl && Intrinsics.areEqual((Object)((GameSettingsImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final GameSettings getWrapped() {
        return this.wrapped;
    }

    public GameSettingsImpl(@NotNull GameSettings wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

