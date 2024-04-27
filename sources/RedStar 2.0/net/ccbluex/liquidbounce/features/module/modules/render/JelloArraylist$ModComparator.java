package net.ccbluex.liquidbounce.features.module.modules.render;

import java.util.Comparator;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.CFont.FontLoaders;

class JelloArraylist$ModComparator
implements Comparator<Module> {
    JelloArraylist$ModComparator() {
    }

    @Override
    public int compare(Module e1, Module e2) {
        return FontLoaders.JelloList40.getStringWidth(e1.getName()) < FontLoaders.JelloList40.getStringWidth(e2.getName()) ? 1 : -1;
    }
}
