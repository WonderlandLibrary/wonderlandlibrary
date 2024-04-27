/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 */
package net.ccbluex.liquidbounce.api.minecraft.client.settings;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u0006H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004R\u0012\u0010\u0005\u001a\u00020\u0006X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0018\u0010\t\u001a\u00020\u0003X\u00a6\u000e\u00a2\u0006\f\u001a\u0004\b\n\u0010\u0004\"\u0004\b\u000b\u0010\f\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "", "isKeyDown", "", "()Z", "keyCode", "", "getKeyCode", "()I", "pressed", "getPressed", "setPressed", "(Z)V", "onTick", "", "AtField"})
public interface IKeyBinding {
    public boolean isKeyDown();

    public boolean getPressed();

    public int getKeyCode();

    public void setPressed(boolean var1);

    public void onTick(int var1);
}

