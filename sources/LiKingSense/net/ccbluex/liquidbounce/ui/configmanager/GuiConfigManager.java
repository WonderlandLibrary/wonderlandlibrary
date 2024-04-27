/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 */
package net.ccbluex.liquidbounce.ui.configmanager;

import java.io.File;
import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.ui.cnfont.FontDrawer;
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0016J \u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0006H\u0014\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/ui/configmanager/GuiConfigManager;", "Lnet/minecraft/client/gui/GuiScreen;", "()V", "drawScreen", "", "p_drawScreen_1_", "", "p_drawScreen_2_", "p_drawScreen_3_", "", "mouseClicked", "p_mouseClicked_1_", "p_mouseClicked_2_", "p_mouseClicked_3_", "LiKingSense"})
public final class GuiConfigManager
extends GuiScreen {
    public static final GuiConfigManager INSTANCE;

    public void func_73863_a(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        this.func_146270_b(0);
        ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        int y = 0;
        File[] fileArray = LiquidBounce.INSTANCE.getFileManager().configsDir.listFiles();
        int n = fileArray.length;
        for (int i = 0; i < n; ++i) {
            File listFile;
            File file = listFile = fileArray[i];
            Intrinsics.checkExpressionValueIsNotNull((Object)file, (String)"listFile");
            FontLoaders.F18.drawString(file.getName(), sr.func_78326_a() / 2 - FontLoaders.F18.getStringWidth(listFile.getName()) / 2, y + 5, -1);
            FontDrawer fontDrawer = FontLoaders.F18;
            Intrinsics.checkExpressionValueIsNotNull((Object)fontDrawer, (String)"FontLoaders.F18");
            y += fontDrawer.getHeight() + 2;
        }
    }

    protected void func_73864_a(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
    }

    private GuiConfigManager() {
    }

    static {
        GuiConfigManager guiConfigManager;
        INSTANCE = guiConfigManager = new GuiConfigManager();
    }
}

