package tech.drainwalk.gui.menu.window.windows.menuwindow.components.type;

import tech.drainwalk.DrainWalk;
import tech.drainwalk.font.FontManager;
import tech.drainwalk.client.theme.ClientColor;
import tech.drainwalk.gui.menu.window.windows.menuwindow.MenuWindow;
import tech.drainwalk.gui.menu.window.windows.menuwindow.components.Component;
import tech.drainwalk.client.module.Module;
import tech.drainwalk.client.module.category.Type;
import tech.drainwalk.utility.render.RenderUtility;

import java.util.ArrayList;
import java.util.List;

public class ModuleTypeComponent extends Component {
    public ModuleTypeComponent(float x, float y, MenuWindow parent) {
        super(x, y, 311 / 2f, 33, parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float offset = 19;
        List<Module> mList = new ArrayList<>();
        for (Module module : DrainWalk.getInstance().getModuleManager().getModuleList()) {
            if (module.getCategory() == parent.getSelectedCategory()) {
                if (module.getType() == Type.MAIN) {
                    mList.add(module);
                }
            }
        }
    //    RenderUtility.drawRect(x+103.5f + (27 /2f), y + (79 / 2f), width, height + (mList.size() * offset),  MenuColor.object);
        RenderUtility.drawRoundedRect(x+103.5f + (27 /2f), y + (79 / 2f), width, height + (mList.size() * offset), 9, ClientColor.object);
        RenderUtility.drawRoundedOutlineRect(x+103.5f + (27 /2f), y + (79 / 2f), width, height + (mList.size() * offset), 9, 1.5f, ClientColor.panelLines);
        RenderUtility.drawRect(x+103.5f + (27 /2f) + 7, y + (79 / 2f) + 23, 283 / 2f, 1, ClientColor.panelLines);
        FontManager.SEMI_BOLD_18.drawString(Type.MAIN.getName(), x + (247 / 2f), y + (91 / 2f) + 2.5f, ClientColor.textMain);
        mList = new ArrayList<>();
        for (Module module : DrainWalk.getInstance().getModuleManager().getModuleList()) {
            if (module.getCategory() == parent.getSelectedCategory()) {
                if (module.getType() == Type.SECONDARY) {
                    mList.add(module);
                }
            }
        }
      //  RenderUtility.drawRect(x+103.5f + (27 /2f)+ (27 / 2f + width), y + (79 / 2f), width, height + (mList.size() * offset),  MenuColor.object);
        RenderUtility.drawRoundedRect(x+103.5f + (27 /2f)+ (27 / 2f + width), y + (79 / 2f), width, height + (mList.size() * offset), 9, ClientColor.object);
        RenderUtility.drawRoundedOutlineRect(x+103.5f + (27 /2f)+ (27 / 2f + width), y + (79 / 2f), width, height + (mList.size() * offset), 9, 1.5f, ClientColor.panelLines);
        RenderUtility.drawRect(x+103.5f + (27 /2f) + 7 + (27 / 2f + width), y + (79 / 2f) + 23, 283 / 2f, 1, ClientColor.panelLines);
        FontManager.SEMI_BOLD_18.drawString(Type.SECONDARY.getName(), x + (247 / 2f) + (29 / 2f + width), y + (91 / 2f) + 2.5f, ClientColor.textMain);
    }
}
