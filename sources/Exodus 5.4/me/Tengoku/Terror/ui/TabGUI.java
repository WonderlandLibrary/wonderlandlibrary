/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.font.FontUtil;

public class TabGUI {
    public static int categoryTab;
    private static int modTab;
    public static int ymod;
    private static int categoryTargetPosition;
    public static int baseCategoryWidth;
    public static int modPosition;
    private static int modTargetPosition;
    public static Section section;
    private static boolean transitionQuickly;
    private static long lastUpdateTime;
    public static int baseCategoryHeight;
    public static int categoryPosition;
    public static int baseModWidth;

    public static void updateBars() {
        long l = System.nanoTime() / 1000000L - lastUpdateTime;
        lastUpdateTime = System.nanoTime() / 1000000L;
        int n = transitionQuickly ? 100 : 20;
        n = Math.max(1, Math.round((long)n * l / 100L));
        if (categoryPosition < categoryTargetPosition) {
            if ((categoryPosition += n) >= categoryTargetPosition) {
                categoryPosition = categoryTargetPosition;
                transitionQuickly = false;
            }
        } else if (categoryPosition > categoryTargetPosition && (categoryPosition -= n) <= categoryTargetPosition) {
            categoryPosition = categoryTargetPosition;
            transitionQuickly = false;
        }
        if (modPosition < modTargetPosition) {
            if ((modPosition += n) >= modTargetPosition) {
                modPosition = modTargetPosition;
                transitionQuickly = false;
            }
        } else if (modPosition > modTargetPosition && (modPosition -= n) <= modTargetPosition) {
            modPosition = modTargetPosition;
            transitionQuickly = false;
        }
    }

    public static List getModsInCategory(Category category) {
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Module module : Exodus.INSTANCE.moduleManager.getModules()) {
            if (module.getCategory() != category) continue;
            arrayList.add(module);
        }
        arrayList.sort(new Comparator<Module>(){

            @Override
            public int compare(Module module, Module module2) {
                return module.getName().compareTo(module2.getName());
            }
        });
        return arrayList;
    }

    public static void init() {
        int n = 0;
        Category[] categoryArray = Category.values();
        int n2 = categoryArray.length;
        int n3 = 0;
        while (n3 < n2) {
            Category category = categoryArray[n3];
            String string = String.valueOf(Character.toUpperCase(category.name().charAt(0))) + category.name().substring(1);
            int n4 = (int)FontUtil.normal.getStringWidth(string);
            n = Math.max(n4, n);
            ++n3;
        }
        baseCategoryWidth = n + 25;
        baseCategoryHeight = Category.values().length * 14 + 2;
    }

    static {
        ymod = -2;
        section = Section.CATEGORY;
        categoryTab = 0;
        modTab = 0;
        categoryPosition = 22;
        categoryTargetPosition = 22;
        modPosition = 22;
        modTargetPosition = 22;
    }

    public static void keyPress(int n) {
        block17: {
            block16: {
                if (section != Section.CATEGORY) break block16;
                switch (n) {
                    default: {
                        break;
                    }
                    case 200: {
                        categoryTargetPosition -= 12;
                        if (--categoryTab < 0) {
                            transitionQuickly = true;
                            categoryTargetPosition = 22 + (Category.values().length - 1) * 12;
                            categoryTab = Category.values().length - 1;
                            break;
                        }
                        break block17;
                    }
                    case 205: {
                        int n2 = 0;
                        for (Module module : TabGUI.getModsInCategory(Category.values()[categoryTab])) {
                            String string = String.valueOf(String.valueOf(String.valueOf(Character.toUpperCase(module.getName().charAt(0))))) + module.getName().substring(1);
                            int n3 = (int)FontUtil.normal.getStringWidth(string);
                            n2 = Math.max(n3, n2);
                        }
                        baseModWidth = n2 + 25;
                        modTargetPosition = modPosition = categoryTargetPosition;
                        modTab = 0;
                        section = Section.MODS;
                        break;
                    }
                    case 208: {
                        categoryTargetPosition += 12;
                        if (++categoryTab > Category.values().length - 1) {
                            transitionQuickly = true;
                            categoryTargetPosition = 22;
                            categoryTab = 0;
                            break;
                        }
                        break block17;
                    }
                }
                break block17;
            }
            if (section == Section.MODS) {
                switch (n) {
                    case 28: {
                        Module module = (Module)TabGUI.getModsInCategory(Category.values()[categoryTab]).get(modTab);
                        module.toggle();
                        break;
                    }
                    case 200: {
                        modTargetPosition -= 12;
                        if (--modTab >= 0) break;
                        transitionQuickly = true;
                        modTargetPosition = categoryTargetPosition + (TabGUI.getModsInCategory(Category.values()[categoryTab]).size() - 1) * 12;
                        modTab = TabGUI.getModsInCategory(Category.values()[categoryTab]).size() - 1;
                        break;
                    }
                    case 203: {
                        section = Section.CATEGORY;
                        break;
                    }
                    case 205: {
                        Module module = (Module)TabGUI.getModsInCategory(Category.values()[categoryTab]).get(modTab);
                        module.toggle();
                        break;
                    }
                    case 208: {
                        modTargetPosition += 12;
                        if (++modTab <= TabGUI.getModsInCategory(Category.values()[categoryTab]).size() - 1) break;
                        transitionQuickly = true;
                        modTargetPosition = categoryTargetPosition;
                        modTab = 0;
                    }
                }
            }
        }
    }

    public static enum Section {
        CATEGORY,
        MODS;

    }
}

