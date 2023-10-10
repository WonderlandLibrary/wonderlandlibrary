package tech.atani.client.feature.module.impl.hud;

import net.minecraft.client.gui.FontRenderer;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.impl.element.modulelist.CustomModuleList;
import tech.atani.client.feature.theme.impl.element.ModuleListElement;
import tech.atani.client.feature.theme.storage.ThemeStorage;
import tech.atani.client.feature.value.impl.MultiStringBoxValue;
import tech.atani.client.listener.event.client.module.DisableModuleEvent;
import tech.atani.client.listener.event.client.module.EnableModuleEvent;
import tech.atani.client.listener.event.minecraft.render.Render2DEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.impl.hud.clientOverlay.IClientOverlayComponent;
import tech.atani.client.utility.interfaces.ColorPalette;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.value.interfaces.ValueChangeListener;

import java.util.*;

@ModuleData(name = "ModuleList", description = "Shows a list of enabled modules", category = Category.HUD)
public class ModuleList extends Module implements ColorPalette, IClientOverlayComponent {
    public StringBoxValue moduleListMode = new StringBoxValue("Module List Mode", "Which module list will be displayed?", this, new String[]{"None", "Atani Modern", "Atani Simple", "Atani Golden", "Augustus 2.6", "Xave", "Ryu", "Icarus", "Fatality", "Koks", "Tarasande", "OHareWare", "Custom"}, new ValueChangeListener[]{new ValueChangeListener() {
        @Override
        public void onChange(Stage stage, Value value, Object oldValue, Object newValue) {
            try {
                if(stage == Stage.PRE) {
                    moduleHashMap.clear();
                } else if(stage == Stage.POST) {
                    if(oldValue != null && !((String) oldValue).equalsIgnoreCase("None"))
                        ThemeStorage.getInstance().getThemeObject(((String) oldValue), ElementType.MODULE_LIST).onDisable();
                    if(newValue != null && !((String) newValue).equalsIgnoreCase("None"))
                        ThemeStorage.getInstance().getThemeObject(((String) newValue), ElementType.MODULE_LIST).onEnable();
                }
            } catch (Exception e) {
                // ignored
            }
        }
    }});
    private MultiStringBoxValue ignoredCategories = new MultiStringBoxValue("Hide Categories", "Which categories should the module list hide?", this, new String[] {"Hud", "Chat", "Options", "Render"}, new String[] {"Hud", "Chat", "Options", "Render", "Movement", "Combat", "Player", "Others"});


    private LinkedHashMap<Module, DecelerateAnimation> moduleHashMap = new LinkedHashMap<>();

    @Override
    public void draw(Render2DEvent render2DEvent, AtomicFloat leftY, AtomicFloat rightY) {
        ModuleListElement moduleListElement = ThemeStorage.getInstance().getThemeObject(moduleListMode.getValue(), ElementType.MODULE_LIST);
        if(this.isEnabled() && moduleListElement != null) {
            List<Module> modulesToShow = new ArrayList<>();

            for (Module module : ModuleStorage.getInstance().getList()) {
                Category moduleCategory = module.getCategory();
                if (!isIgnoredCategory(moduleCategory)) {
                    modulesToShow.add(module);
                }
            }

            for (Module module : modulesToShow) {
                if (!moduleHashMap.containsKey(module)) {
                    if(moduleListElement.shouldAnimate()) {
                        moduleHashMap.put(module, new DecelerateAnimation(200, 1, module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS));
                    } else {
                        moduleHashMap.put(module, new DecelerateAnimation(1, 1, module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS));
                    }
                }
            }

            List<Module> sortedModules = new ArrayList<>(modulesToShow);
            Collections.sort(sortedModules, (mod1, mod2) -> {
                FontRenderer fontRenderer = moduleListElement.getFontRenderer();
                String name1 = moduleListMode.is("Custom") ? ((CustomModuleList) moduleListElement).getModuleName(mod1, false) : mod1.getName();
                String name2 = moduleListMode.is("Custom") ? ((CustomModuleList) moduleListElement).getModuleName(mod2, false) : mod2.getName();
                return fontRenderer.getStringWidthInt(name2) - fontRenderer.getStringWidthInt(name1);
            });
            LinkedHashMap<Module, DecelerateAnimation> sortedMap = new LinkedHashMap<>();
            for (Module module : sortedModules) {
                sortedMap.put(module, moduleHashMap.get(module));
            }
            moduleHashMap = sortedMap;

            moduleListElement.onDraw(render2DEvent.getScaledResolution(), render2DEvent.getPartialTicks(), leftY, rightY, moduleHashMap);
        }
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Listen
    public final void onModuleEnable(EnableModuleEvent enableModuleEvent) {
        if (enableModuleEvent.getType() == EnableModuleEvent.Type.PRE)
            return;

        ModuleListElement moduleListElement = ThemeStorage.getInstance().getThemeObject(moduleListMode.getValue(), ElementType.MODULE_LIST);

        if(moduleListElement != null) {
            if (this.moduleHashMap.containsKey(enableModuleEvent.getModule())) {
                this.moduleHashMap.get(enableModuleEvent.getModule()).setDirection(Direction.FORWARDS);
            } else {
                if(moduleListElement.shouldAnimate()) {
                    moduleHashMap.put(enableModuleEvent.getModule(), new DecelerateAnimation(200, 1, Direction.FORWARDS));
                } else {
                    moduleHashMap.put(enableModuleEvent.getModule(), new DecelerateAnimation(1, 1, Direction.FORWARDS));
                }
            }
        }
    }

    @Listen
    public final void onModuleDisable(DisableModuleEvent disableModuleEvent) {
        if (disableModuleEvent.getType() == DisableModuleEvent.Type.PRE)
            return;

        ModuleListElement moduleListElement = ThemeStorage.getInstance().getThemeObject(moduleListMode.getValue(), ElementType.MODULE_LIST);

        if(moduleListElement != null) {
            if (this.moduleHashMap.containsKey(disableModuleEvent.getModule())) {
                this.moduleHashMap.get(disableModuleEvent.getModule()).setDirection(Direction.BACKWARDS);
            } else {
                if(moduleListElement.shouldAnimate()) {
                    moduleHashMap.put(disableModuleEvent.getModule(), new DecelerateAnimation(200, 1, Direction.BACKWARDS));
                } else {
                    moduleHashMap.put(disableModuleEvent.getModule(), new DecelerateAnimation(1, 1, Direction.BACKWARDS));
                }
            }
        }

    }

    private boolean isIgnoredCategory(Category moduleCategory) {
        String ignoredCategory = ignoredCategories.getValueAsString().toUpperCase();
        return ignoredCategory.contains(moduleCategory.name());
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
