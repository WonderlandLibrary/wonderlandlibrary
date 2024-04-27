/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraftforge.fml.client.GuiModList
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.gui.client;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.ClientPresence;
import net.dev.important.gui.client.GuiBackground;
import net.dev.important.gui.client.GuiScripts;
import net.dev.important.utils.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.GuiModList;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0014J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J \u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\u0005H\u0016J\u0018\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nH\u0014R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/dev/important/gui/client/GuiModsMenu;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "(Lnet/minecraft/client/gui/GuiScreen;)V", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "changeDisplayState", "buttonId", "", "state", "", "drawScreen", "mouseX", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "LiquidBounce"})
public final class GuiModsMenu
extends GuiScreen {
    @NotNull
    private final GuiScreen prevGui;

    public GuiModsMenu(@NotNull GuiScreen prevGui) {
        Intrinsics.checkNotNullParameter(prevGui, "prevGui");
        this.prevGui = prevGui;
    }

    public void func_73866_w_() {
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48, "Forge Mods"));
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 25, "Scripts"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 50, Intrinsics.stringPlus("Rich Presence: ", Client.INSTANCE.getClientPresence().getShowRichPresenceValue() ? "\u00a7aON" : "\u00a7cOFF")));
        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 75, "Background Settings"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 48 + 100, "Back"));
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkNotNullParameter(button, "button");
        switch (button.field_146127_k) {
            case 0: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiModList((GuiScreen)this));
                break;
            }
            case 1: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiScripts(this));
                break;
            }
            case 2: {
                boolean bl;
                boolean state;
                ClientPresence rpc = Client.INSTANCE.getClientPresence();
                boolean bl2 = state = !rpc.getShowRichPresenceValue();
                if (!state) {
                    rpc.shutdown();
                    this.changeDisplayState(2, state);
                    bl = false;
                } else if (state) {
                    Ref.BooleanRef value = new Ref.BooleanRef();
                    value.element = true;
                    ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(value, rpc){
                        final /* synthetic */ Ref.BooleanRef $value;
                        final /* synthetic */ ClientPresence $rpc;
                        {
                            this.$value = $value;
                            this.$rpc = $rpc;
                            super(0);
                        }

                        public final void invoke() {
                            boolean bl;
                            Ref.BooleanRef booleanRef;
                            Ref.BooleanRef booleanRef2 = this.$value;
                            try {
                                booleanRef = booleanRef2;
                                this.$rpc.setup();
                                bl = true;
                            }
                            catch (Throwable throwable) {
                                booleanRef = booleanRef2;
                                ClientUtils.getLogger().error("Failed to setup Discord RPC.", throwable);
                                bl = false;
                            }
                            booleanRef.element = bl;
                        }
                    }, 31, null);
                    this.changeDisplayState(2, value.element);
                    bl = value.element;
                } else {
                    throw new NoWhenBranchMatchedException();
                }
                rpc.setShowRichPresenceValue(bl);
                break;
            }
            case 3: {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiBackground(this));
                break;
            }
            case 4: {
                this.field_146297_k.func_147108_a(this.prevGui);
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    private final void changeDisplayState(int buttonId, boolean state) {
        String string;
        GuiButton button = (GuiButton)this.field_146292_n.get(buttonId);
        String displayName = button.field_146126_j;
        boolean bl = state;
        if (!bl) {
            Intrinsics.checkNotNullExpressionValue(displayName, "displayName");
            string = StringsKt.replace$default(displayName, "\u00a7aON", "\u00a7cOFF", false, 4, null);
        } else if (bl) {
            Intrinsics.checkNotNullExpressionValue(displayName, "displayName");
            string = StringsKt.replace$default(displayName, "\u00a7cOFF", "\u00a7aON", false, 4, null);
        } else {
            throw new NoWhenBranchMatchedException();
        }
        button.field_146126_j = string;
    }
}

