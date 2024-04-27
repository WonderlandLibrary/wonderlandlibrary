package net.ccbluex.liquidbounce.api.minecraft.client.settings;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\u0000\n\u0000\n\n\b\n\b\n\b\n\n\u0000\bf\u000020J\r020H&R0X¦¢\bR0X¦¢\b\bR\t0X¦¢\f\b\n\"\b\f¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "", "isKeyDown", "", "()Z", "keyCode", "", "getKeyCode", "()I", "pressed", "getPressed", "setPressed", "(Z)V", "onTick", "", "Pride"})
public interface IKeyBinding {
    public int getKeyCode();

    public boolean getPressed();

    public void setPressed(boolean var1);

    public boolean isKeyDown();

    public void onTick(int var1);
}
