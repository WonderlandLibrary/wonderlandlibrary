/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0010\u0010\u0013\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0006\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/IconManager;", "", "()V", "add", "Lnet/minecraft/util/ResourceLocation;", "getAdd", "()Lnet/minecraft/util/ResourceLocation;", "back", "getBack", "docs", "getDocs", "download", "getDownload", "folder", "getFolder", "online", "getOnline", "reload", "getReload", "removeIcon", "search", "getSearch", "KyinoClient"})
public final class IconManager {
    @JvmField
    @NotNull
    public static final ResourceLocation removeIcon;
    @NotNull
    private static final ResourceLocation add;
    @NotNull
    private static final ResourceLocation back;
    @NotNull
    private static final ResourceLocation docs;
    @NotNull
    private static final ResourceLocation download;
    @NotNull
    private static final ResourceLocation folder;
    @NotNull
    private static final ResourceLocation online;
    @NotNull
    private static final ResourceLocation reload;
    @NotNull
    private static final ResourceLocation search;
    public static final IconManager INSTANCE;

    @NotNull
    public final ResourceLocation getAdd() {
        return add;
    }

    @NotNull
    public final ResourceLocation getBack() {
        return back;
    }

    @NotNull
    public final ResourceLocation getDocs() {
        return docs;
    }

    @NotNull
    public final ResourceLocation getDownload() {
        return download;
    }

    @NotNull
    public final ResourceLocation getFolder() {
        return folder;
    }

    @NotNull
    public final ResourceLocation getOnline() {
        return online;
    }

    @NotNull
    public final ResourceLocation getReload() {
        return reload;
    }

    @NotNull
    public final ResourceLocation getSearch() {
        return search;
    }

    private IconManager() {
    }

    static {
        IconManager iconManager;
        INSTANCE = iconManager = new IconManager();
        removeIcon = new ResourceLocation("liquidbounce/notification/new/error.png");
        add = new ResourceLocation("liquidbounce/clickgui/import.png");
        back = new ResourceLocation("liquidbounce/clickgui/back.png");
        docs = new ResourceLocation("liquidbounce/clickgui/docs.png");
        download = new ResourceLocation("liquidbounce/clickgui/download.png");
        folder = new ResourceLocation("liquidbounce/clickgui/folder.png");
        online = new ResourceLocation("liquidbounce/clickgui/online.png");
        reload = new ResourceLocation("liquidbounce/clickgui/reload.png");
        search = new ResourceLocation("liquidbounce/clickgui/search.png");
    }
}

