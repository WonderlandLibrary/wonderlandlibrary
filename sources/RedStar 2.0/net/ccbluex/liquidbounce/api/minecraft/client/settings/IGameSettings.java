package net.ccbluex.liquidbounce.api.minecraft.client.settings;

import java.util.Set;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.WEnumPlayerModelParts;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.minecraft.client.util.ITooltipFlag;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\n\b\n\"\n\n\b\n\n\b\bf\u000020J-02.0H&J/002&0(210H&R0X¦¢\bR0X¦¢\f\b\b\t\"\b\nR\f0\rX¦¢\f\b\"\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR 0X¦¢\b!R\"0X¦¢\b#R$0X¦¢\b%R&\b0(0'X¦¢\b)*R+0\rX¦¢\b,¨2"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IGameSettings;", "", "advancedItemTooltips", "Lnet/minecraft/client/util/ITooltipFlag;", "getAdvancedItemTooltips", "()Lnet/minecraft/client/util/ITooltipFlag;", "entityShadows", "", "getEntityShadows", "()Z", "setEntityShadows", "(Z)V", "gammaSetting", "", "getGammaSetting", "()F", "setGammaSetting", "(F)V", "keyBindAttack", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "getKeyBindAttack", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "keyBindBack", "getKeyBindBack", "keyBindForward", "getKeyBindForward", "keyBindJump", "getKeyBindJump", "keyBindLeft", "getKeyBindLeft", "keyBindRight", "getKeyBindRight", "keyBindSneak", "getKeyBindSneak", "keyBindSprint", "getKeyBindSprint", "keyBindUseItem", "getKeyBindUseItem", "modelParts", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/player/WEnumPlayerModelParts;", "getModelParts", "()Ljava/util/Set;", "mouseSensitivity", "getMouseSensitivity", "isKeyDown", "key", "setModelPartEnabled", "", "enabled", "Pride"})
public interface IGameSettings {
    @NotNull
    public ITooltipFlag getAdvancedItemTooltips();

    public boolean getEntityShadows();

    public void setEntityShadows(boolean var1);

    public float getGammaSetting();

    public void setGammaSetting(float var1);

    @NotNull
    public Set<WEnumPlayerModelParts> getModelParts();

    public float getMouseSensitivity();

    @NotNull
    public IKeyBinding getKeyBindAttack();

    @NotNull
    public IKeyBinding getKeyBindUseItem();

    @NotNull
    public IKeyBinding getKeyBindJump();

    @NotNull
    public IKeyBinding getKeyBindSneak();

    @NotNull
    public IKeyBinding getKeyBindForward();

    @NotNull
    public IKeyBinding getKeyBindBack();

    @NotNull
    public IKeyBinding getKeyBindRight();

    @NotNull
    public IKeyBinding getKeyBindLeft();

    @NotNull
    public IKeyBinding getKeyBindSprint();

    public boolean isKeyDown(@NotNull IKeyBinding var1);

    public void setModelPartEnabled(@NotNull WEnumPlayerModelParts var1, boolean var2);
}
