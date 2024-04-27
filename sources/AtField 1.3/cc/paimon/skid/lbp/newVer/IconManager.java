/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmField
 */
package cc.paimon.skid.lbp.newVer;

import kotlin.jvm.JvmField;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;

public final class IconManager {
    private static final IResourceLocation reload;
    @JvmField
    public static final IResourceLocation removeIcon;
    public static final IconManager INSTANCE;
    private static final IResourceLocation add;
    private static final IResourceLocation docs;
    private static final IResourceLocation search;
    private static final IResourceLocation online;
    private static final IResourceLocation download;
    private static final IResourceLocation folder;
    private static final IResourceLocation back;

    static {
        IconManager iconManager;
        INSTANCE = iconManager = new IconManager();
        removeIcon = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/error.png");
        add = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/import.png");
        back = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/back.png");
        docs = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/docs.png");
        download = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/download.png");
        folder = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/folder.png");
        online = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/online.png");
        reload = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/reload.png");
        search = LiquidBounce.INSTANCE.getWrapper().getClassProvider().createResourceLocation("paimon/clickgui/search.png");
    }

    public final IResourceLocation getDocs() {
        return docs;
    }

    public final IResourceLocation getSearch() {
        return search;
    }

    public final IResourceLocation getReload() {
        return reload;
    }

    public final IResourceLocation getAdd() {
        return add;
    }

    public final IResourceLocation getDownload() {
        return download;
    }

    public final IResourceLocation getFolder() {
        return folder;
    }

    private IconManager() {
    }

    public final IResourceLocation getOnline() {
        return online;
    }

    public final IResourceLocation getBack() {
        return back;
    }
}

