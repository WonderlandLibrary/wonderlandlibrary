/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.ui.client;

import com.google.gson.Gson;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public final class GuiServerStatus
extends WrappedGuiScreen {
    private final HashMap<String, String> status;
    private final IGuiScreen prevGui;

    @Override
    public void initGui() {
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() / 4 + 145, "Back"));
        ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this){
            final /* synthetic */ GuiServerStatus this$0;

            public final void invoke() {
                GuiServerStatus.access$loadInformations(this.this$0);
            }
            {
                this.this$0 = guiServerStatus;
                super(0);
            }
        }), (int)31, null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        int i = this.getRepresentedScreen().getHeight() / 4 + 40;
        RenderUtils.drawRect((float)this.getRepresentedScreen().getWidth() / 2.0f - (float)115, (float)i - 5.0f, (float)this.getRepresentedScreen().getWidth() / 2.0f + (float)115, (float)this.getRepresentedScreen().getHeight() / 4.0f + (float)43 + (float)(this.status.keySet().isEmpty() ? 10 : this.status.keySet().size() * Fonts.font40.getFontHeight()), Integer.MIN_VALUE);
        if (this.status.isEmpty()) {
            Fonts.font40.drawCenteredString("Loading...", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 4.0f + (float)40, Color.WHITE.getRGB());
        } else {
            for (String server : this.status.keySet()) {
                String color = this.status.get(server);
                Fonts.font40.drawCenteredString("\u00a7c\u00a7l" + server + ": " + (StringsKt.equals((String)color, (String)"red", (boolean)true) ? "\u00a7c" : (StringsKt.equals((String)color, (String)"yellow", (boolean)true) ? "\u00a7e" : "\u00a7a")) + (StringsKt.equals((String)color, (String)"red", (boolean)true) ? "Offline" : (StringsKt.equals((String)color, (String)"yellow", (boolean)true) ? "Slow" : "Online")), (float)this.getRepresentedScreen().getWidth() / 2.0f, i, Color.WHITE.getRGB());
                i += Fonts.font40.getFontHeight();
            }
        }
        Fonts.fontBold180.drawCenteredString("Server Status", (float)this.getRepresentedScreen().getWidth() / 2.0f, (float)this.getRepresentedScreen().getHeight() / 8.0f + 5.0f, 4673984, true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private final void loadInformations() {
        this.status.clear();
        try {
            Object object = new Gson().fromJson(HttpUtils.get("https://status.mojang.com/check"), List.class);
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.List<kotlin.collections.Map<kotlin.String, kotlin.String>>");
            }
            List linkedTreeMaps = (List)object;
            Iterator iterator = linkedTreeMaps.iterator();
            while (iterator.hasNext()) {
                Map linkedTreeMap;
                Map map = linkedTreeMap = (Map)iterator.next();
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

    @Override
    public void actionPerformed(IGuiButton button) {
        if (button.getId() == 1) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    public GuiServerStatus(IGuiScreen prevGui) {
        this.prevGui = prevGui;
        this.status = new HashMap();
    }

    public static final /* synthetic */ void access$loadInformations(GuiServerStatus $this) {
        $this.loadInformations();
    }
}

