/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client;

import com.google.gson.Gson;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0014J \u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\bH\u0016J\u0018\u0010\u0012\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rH\u0014J\b\u0010\u0016\u001a\u00020\bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiServerStatus;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "(Lnet/minecraft/client/gui/GuiScreen;)V", "status", "Ljava/util/HashMap;", "", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "initGui", "keyTyped", "typedChar", "", "keyCode", "loadInformations", "KyinoClient"})
public final class GuiServerStatus
extends GuiScreen {
    private final HashMap<String, String> status;
    private final GuiScreen prevGui;

    public void func_73866_w_() {
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m / 4 + 145, "Back"));
        ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
            final /* synthetic */ GuiServerStatus this$0;

            public final void invoke() {
                GuiServerStatus.access$loadInformations(this.this$0);
            }
            {
                this.this$0 = guiServerStatus;
                super(0);
            }
        }, 31, null);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        int i = this.field_146295_m / 4 + 40;
        Gui.func_73734_a((int)(this.field_146294_l / 2 - 115), (int)(i - 5), (int)(this.field_146294_l / 2 + 115), (int)(this.field_146295_m / 4 + 43 + (this.status.keySet().isEmpty() ? 10 : this.status.keySet().size() * Fonts.font40.field_78288_b)), (int)Integer.MIN_VALUE);
        if (this.status.isEmpty()) {
            FontRenderer fontRenderer = Fonts.font40;
            int n = this.field_146294_l / 2;
            int n2 = this.field_146295_m / 4 + 40;
            Color color = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
            this.func_73732_a(fontRenderer, "Loading...", n, n2, color.getRGB());
        } else {
            for (String server : this.status.keySet()) {
                String color = this.status.get(server);
                FontRenderer fontRenderer = Fonts.font40;
                String string = "\u00a7c\u00a7l" + server + ": " + (StringsKt.equals(color, "red", true) ? "\u00a7c" : (StringsKt.equals(color, "yellow", true) ? "\u00a7e" : "\u00a7a")) + (StringsKt.equals(color, "red", true) ? "Offline" : (StringsKt.equals(color, "yellow", true) ? "Slow" : "Online"));
                int n = this.field_146294_l / 2;
                Color color2 = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull(color2, "Color.WHITE");
                this.func_73732_a(fontRenderer, string, n, i, color2.getRGB());
                i += Fonts.font40.field_78288_b;
            }
        }
        Fonts.fontBold180.drawCenteredString("Server Status", (float)this.field_146294_l / 2.0f, (float)this.field_146295_m / 8.0f + 5.0f, 4673984, true);
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    private final void loadInformations() {
        this.status.clear();
        try {
            Object object = new Gson().fromJson(HttpUtils.get("https://status.mojang.com/check"), List.class);
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.List<kotlin.collections.Map<kotlin.String, kotlin.String>>");
            }
            List linkedTreeMaps = (List)object;
            Iterator iterator2 = linkedTreeMaps.iterator();
            while (iterator2.hasNext()) {
                Map linkedTreeMap;
                Map map = linkedTreeMap = (Map)iterator2.next();
                boolean bl = false;
                for (Map.Entry entry : map.entrySet()) {
                    ((Map)this.status).put(entry.getKey(), entry.getValue());
                }
            }
        }
        catch (IOException e) {
            ((Map)this.status).put("status.mojang.com/check", "red");
        }
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        if (button.field_146127_k == 1) {
            this.field_146297_k.func_147108_a(this.prevGui);
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    public GuiServerStatus(@NotNull GuiScreen prevGui) {
        Intrinsics.checkParameterIsNotNull(prevGui, "prevGui");
        this.prevGui = prevGui;
        this.status = new HashMap();
    }

    public static final /* synthetic */ void access$loadInformations(GuiServerStatus $this) {
        $this.loadInformations();
    }
}

