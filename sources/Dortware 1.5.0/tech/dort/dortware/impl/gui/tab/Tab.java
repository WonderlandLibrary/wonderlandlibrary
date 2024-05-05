package tech.dort.dortware.impl.gui.tab;

import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import skidmonke.Client;
import skidmonke.Minecraft;
import tech.dort.dortware.api.font.CustomFontRenderer;
import tech.dort.dortware.api.module.Module;
import tech.dort.dortware.api.module.enums.ModuleCategory;
import tech.dort.dortware.api.property.impl.BooleanValue;
import tech.dort.dortware.impl.events.KeyboardEvent;
import tech.dort.dortware.impl.modules.render.Hud;
import tech.dort.dortware.impl.utils.render.ColorUtil;

import java.awt.*;
import java.util.List;

/**
 * @author Auth
 * @author Intent (friend helped me and made me fucking use intent without me knowing)
 */

public class Tab {
    final CustomFontRenderer font1 = Client.INSTANCE.getFontManager().getFont("Chat").getRenderer();
    final Minecraft mc = Minecraft.getMinecraft();

    private int tab, index;
    private boolean expanded;

    public void render() {
        final Hud hud = Client.INSTANCE.getModuleManager().get(Hud.class);
        final BooleanValue tabGui = hud.tabGui;
        if (tabGui.getValue()) {
            switch (hud.mode.getValue()) {
                case MEME: {
                    Gui.drawRect(4, 23, 80, 6 + ModuleCategory.values().length * 16, new Color(0, 0, 0, 100).getRGB());
                    Gui.drawRect(4, 23 + tab * 16, 80, 28 + tab * 16 + 10, ColorUtil.getModeColor());

                    int y = 0;
                    for (ModuleCategory category : ModuleCategory.values()) {
                        if (category.equals(ModuleCategory.HIDDEN))
                            continue;

                        mc.fontRendererObj.drawStringWithShadow(category.getName(), 10, 26 + y * 16, -1);

                        y++;
                    }

                    // BELOW IS MODULES

                    if (expanded) {
                        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

                        Gui.drawRect(82, 23, 174, 6 + (modules.size() + 1) * 16, new Color(0, 0, 0, 100).getRGB());
                        Gui.drawRect(82, 23 + index * 16, 174, 28 + index * 16 + 10, ColorUtil.getModeColor());

                        y = 0;
                        for (Module module : modules) {
                            mc.fontRendererObj.drawStringWithShadow(module.getModuleData().getName(), 87, 26 + y * 16, module.isToggled() ? -1 : Color.LIGHT_GRAY.getRGB());

                            y++;
                        }
                    }
                }
                break;

                case VAZIAK: {
                    Gui.drawRect(82, 23, 300, 6 + ModuleCategory.values().length * 16, new Color(RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), RandomUtils.nextInt(0, 255), 255).getRGB());
                    Gui.drawRect(4, 23 + tab * 16, 450, 28 + tab * 16 + 10, ColorUtil.getRainbow(-6000, 0));

                    int y = 0;
                    for (ModuleCategory category : ModuleCategory.values()) {
                        if (category.equals(ModuleCategory.HIDDEN))
                            continue;

                        font1.drawStringWithShadow(category.getName(), 10, 60 + y * 16, Color.PINK.getRGB());

                        y++;
                    }

                    // BELOW IS MODULES

                    if (expanded) {
                        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

                        Gui.drawRect(82, 23, 174, 6 + (modules.size() + 1) * 16, new Color(0, 255, 0, 100).getRGB());
                        Gui.drawRect(0, 23 + index * 16, 200, 28 + index * 16 + 10, ColorUtil.getModeColor());

                        y = 0;
                        for (Module module : modules) {
                            mc.fontRendererObj.drawStringWithShadow(module.getModuleData().getName(), 125, 26 + y * 16, module.isToggled() ? -1 : Color.RED.getRGB());

                            y++;
                        }
                    }
                }
                break;

                case SKEET: {
                    Gui.drawRect(4, 43, 80, 26 + ModuleCategory.values().length * 16, new Color(0, 0, 0, 100).getRGB());
                    Gui.drawRect(4, 43 + tab * 16, 80, 48 + tab * 16 + 10, ColorUtil.getModeColor());

                    int y = 0;
                    for (ModuleCategory category : ModuleCategory.values()) {
                        if (category.equals(ModuleCategory.HIDDEN))
                            continue;

                        font1.drawStringWithShadow(category.getName(), 10, 46 + y * 16, -1);

                        y++;
                    }

                    // BELOW IS MODULES

                    if (expanded) {
                        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

                        Gui.drawRect(82, 43, 174, 26 + (modules.size() + 1) * 16, new Color(0, 0, 0, 100).getRGB());
                        Gui.drawRect(82, 43 + index * 16, 174, 48 + index * 16 + 10, ColorUtil.getModeColor());

                        y = 0;
                        for (Module module : modules) {
                            font1.drawStringWithShadow(module.getModuleData().getName(), 87, 46 + y * 16, module.isToggled() ? -1 : Color.LIGHT_GRAY.getRGB());

                            y++;
                        }
                    }
                }
                break;

                case SECTION_1_THE_MYSTERIOUS_PORT_3000:
                case DORTWARE_ICON: {
                    Gui.drawRect(4, 63, 80, 46 + ModuleCategory.values().length * 16, new Color(0, 0, 0, 100).getRGB());
                    Gui.drawRect(4, 63 + tab * 16, 80, 68 + tab * 16 + 10, ColorUtil.getModeColor());

                    int y = 0;
                    for (ModuleCategory category : ModuleCategory.values()) {
                        if (category.equals(ModuleCategory.HIDDEN))
                            continue;

                        font1.drawStringWithShadow(category.getName(), 10, 66 + y * 16, -1);

                        y++;
                    }

                    // BELOW IS MODULES

                    if (expanded) {
                        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

                        Gui.drawRect(82, 63, 174, 46 + (modules.size() + 1) * 16, new Color(0, 0, 0, 100).getRGB());
                        Gui.drawRect(82, 63 + index * 16, 174, 68 + index * 16 + 10, ColorUtil.getModeColor());

                        y = 0;
                        for (Module module : modules) {
                            font1.drawStringWithShadow(module.getModuleData().getName(), 87, 66 + y * 16, module.isToggled() ? -1 : Color.LIGHT_GRAY.getRGB());

                            y++;
                        }
                    }
                }
                break;

                case ASTOLFO: {
                    Gui.drawRect(0, 23, 70, 6 + ModuleCategory.values().length * 16, new Color(0, 0, 0, 100).getRGB());
                    Gui.drawRect(0, 23 + tab * 16, 70, 28 + tab * 16 + 10, ColorUtil.getModeColor());

                    int y = 0;
                    for (ModuleCategory category : ModuleCategory.values()) {
                        if (category.equals(ModuleCategory.HIDDEN))
                            continue;

                        font1.drawStringWithShadow(category.getName(), category == ModuleCategory.values()[tab] ? 16 : 6, 26 + y * 16, -1);

                        y++;
                    }

                    // BELOW IS MODULES

                    if (expanded) {
                        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

                        Gui.drawRect(72, 23, 162, 6 + (modules.size() + 1) * 16, new Color(0, 0, 0, 100).getRGB());
                        Gui.drawRect(72, 23 + index * 16, 162, 28 + index * 16 + 10, ColorUtil.getModeColor());

                        y = 0;
                        for (Module module : modules) {
                            font1.drawStringWithShadow(module.getModuleData().getName(), module == modules.get(index) ? 85 : 75, 26 + y * 16, module.isToggled() ? -1 : Color.LIGHT_GRAY.getRGB());

                            y++;
                        }
                    }
                }
                break;

                default: {
                    Gui.drawRect(4, 23, 80, 6 + ModuleCategory.values().length * 16, new Color(0, 0, 0, 100).getRGB());
                    Gui.drawRect(4, 23 + tab * 16, 80, 28 + tab * 16 + 10, ColorUtil.getModeColor());

                    int y = 0;
                    for (ModuleCategory category : ModuleCategory.values()) {
                        if (category.equals(ModuleCategory.HIDDEN))
                            continue;

                        font1.drawStringWithShadow(category.getName(), 10, 26 + y * 16, -1);

                        y++;
                    }

                    // BELOW IS MODULES

                    if (expanded) {
                        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

                        Gui.drawRect(82, 23, 174, 6 + (modules.size() + 1) * 16, new Color(0, 0, 0, 100).getRGB());
                        Gui.drawRect(82, 23 + index * 16, 174, 28 + index * 16 + 10, ColorUtil.getModeColor());

                        y = 0;
                        for (Module module : modules) {
                            font1.drawStringWithShadow(module.getModuleData().getName(), 87, 26 + y * 16, module.isToggled() ? -1 : Color.LIGHT_GRAY.getRGB());

                            y++;
                        }
                    }
                }
                break;
            }
        }
    }

    public void updateKeys(KeyboardEvent event) {
        int keyCode = event.getKey();

        Hud hud = Client.INSTANCE.getModuleManager().get(Hud.class);
        BooleanValue tabGui = hud.tabGui;

        List<Module> modules = Client.INSTANCE.getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

        if (!tabGui.getValue())
            return;

        switch (keyCode) {
            case Keyboard.KEY_UP:
                if (expanded) {
                    if (index <= 0) {
                        index = modules.size() - 1;
                    } else {
                        index--;
                    }
                } else {
                    if (tab <= 0) {
                        tab = ModuleCategory.values().length - 2;
                    } else {
                        tab--;
                    }
                }
                break;

            case Keyboard.KEY_DOWN:
                if (expanded) {
                    if (index >= modules.size() - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                } else {
                    if (tab >= ModuleCategory.values().length - 2) {
                        tab = 0;
                    } else {
                        tab++;
                    }
                }
                break;

            case Keyboard.KEY_RETURN:
            case Keyboard.KEY_RIGHT:
                if (expanded) {
                    modules.get(index).toggle();
                } else {
                    index = 0;
                    expanded = true;
                }
                break;


            case Keyboard.KEY_LEFT:
                expanded = false;
                break;
        }
    }
}
