/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.JvmField
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.newui;

import kotlin.Metadata;
import kotlin.jvm.JvmField;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0010\u0010\u0013\u001a\u00020\u00048\u0006X\u0087\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0006\u00a8\u0006\u0016"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/newui/IconManager;", "", "()V", "add", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "getAdd", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "back", "getBack", "docs", "getDocs", "download", "getDownload", "folder", "getFolder", "online", "getOnline", "reload", "getReload", "removeIcon", "search", "getSearch", "LiKingSense"})
public final class IconManager {
    @JvmField
    @NotNull
    public static final IResourceLocation removeIcon;
    @NotNull
    private static final IResourceLocation add;
    @NotNull
    private static final IResourceLocation back;
    @NotNull
    private static final IResourceLocation docs;
    @NotNull
    private static final IResourceLocation download;
    @NotNull
    private static final IResourceLocation folder;
    @NotNull
    private static final IResourceLocation online;
    @NotNull
    private static final IResourceLocation reload;
    @NotNull
    private static final IResourceLocation search;
    public static final IconManager INSTANCE;

    @NotNull
    public final IResourceLocation getAdd() {
        return add;
    }

    @NotNull
    public final IResourceLocation getBack() {
        return back;
    }

    @NotNull
    public final IResourceLocation getDocs() {
        return docs;
    }

    @NotNull
    public final IResourceLocation getDownload() {
        return download;
    }

    @NotNull
    public final IResourceLocation getFolder() {
        return folder;
    }

    @NotNull
    public final IResourceLocation getOnline() {
        return online;
    }

    @NotNull
    public final IResourceLocation getReload() {
        return reload;
    }

    @NotNull
    public final IResourceLocation getSearch() {
        return search;
    }

    private IconManager() {
    }

    static {
        IconManager iconManager;
        INSTANCE = iconManager = new IconManager();
        removeIcon = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/error.png");
        add = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/import.png");
        back = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/back.png");
        docs = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/docs.png");
        download = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/download.png");
        folder = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/folder.png");
        online = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/online.png");
        reload = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/reload.png");
        search = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("likingsense/clickgui/search.png");
    }
}

