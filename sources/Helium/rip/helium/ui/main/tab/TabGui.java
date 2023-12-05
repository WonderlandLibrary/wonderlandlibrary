package rip.helium.ui.main.tab;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.KeyPressEvent;
import rip.helium.event.minecraft.RenderOverlayEvent;
import rip.helium.utils.ColorCreator;
import rip.helium.utils.RenderingMethods;
import rip.helium.utils.property.impl.BooleanProperty;
import rip.helium.utils.property.impl.ColorProperty;
import rip.helium.utils.property.impl.DoubleProperty;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TabGui /*/extends Cheat/*/ {

    //public TabGui() {
    //	super("TabGui", "", CheatCategory.VISUAL);
    //	registerProperties(length, spacing, ypos, rainbow, color1);
    //}

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static int color = new Color(208, 0, 255).getRGB();
    static ColorProperty color1 = new ColorProperty("Color", "Changes the color", null, 255, 255, 255, 255);
    static BooleanProperty rainbow = new BooleanProperty("Rainbow", "Makes the tabgui gay.", null, false);
    //	    public static Integer length = new Value<Integer("Length", 25, 0, 400);
//	    public static Value<Integer> spacing = new Value<Integer>("Spacing", 2, 0, 100);
    private static int selected, moduleSelected, valueSelected;
    private static boolean isOpen;
    private static boolean isValueOpen;
    private static float extended;
    private static int bg = new Color(0, 0, 0, 150).getRGB();
    private static final DoubleProperty ypos = new DoubleProperty("Position Y", "TabGui Y position'.", null, 15, 0, 300, 1, null);
    private static final DoubleProperty length = new DoubleProperty("Length", "TabGui length.", null, 25, 0, 25, 400, null);
    private static final DoubleProperty spacing = new DoubleProperty("Spacing", "TabGui spacing.", null, 2, 0, 25, 100, null);


    public static void drawTabGui() {
        if (!mc.gameSettings.showDebugInfo) {
            for (int i = 0; i < CheatCategory.values().length; i++) {
                CheatCategory cat = CheatCategory.values()[i];
                int y;
                y = ypos.value.intValue();
                int index = 0;
                long x = 0L;
                int yRain = 0;
                if (rainbow.getValue()) {
                    color = ColorCreator.createRainbowFromOffset(3200, y * -15);
                } else {
                    color = color1.getValue().getRGB();
                }
                bg = new Color(0, 0, 0, 150).getRGB();
                length.setValue(25D);
                yRain -= 11;

                if (selected == i) {
                    RenderingMethods.drawBorderedRectReliant(3, y + i * 12, (float) (5 + length.getValue() + mc.fontRendererObj.getStringWidth("Movement")), y + 12 + i * 12, 2F, color, color);
                } else {
                    Gui.drawRecter(3D, y + i * 12, 5 + length.getValue() + mc.fontRendererObj.getStringWidth("Movement"), y + 12 + i * 12, bg);
                }

                String name = cat.name().charAt(0) + cat.name().substring(1).toLowerCase();
                //System.out.println(name);
                mc.fontRendererObj.drawStringWithShadow(name, (float) (8 + spacing.getValue()), y + i * 12 + 2, 0xFFFFFF);
                extended += 0.01F * (isOpen ? 1F : -1F);
                extended = Math.max(0, extended);
                extended = Math.min(1, extended);

                if ((isOpen || extended > 0) && i == selected) {
                    for (int j = 0; j < getModsForCategory(cat).size(); j++) {
                        Cheat mod = getModsForCategory(cat).get(j);
                        if (moduleSelected == j) {
//	                        RenderingMethods.drawBorderedRectReliant(3f + mc.fontRendererObj.getStringWidth("Movement") + 2f + length.getValue(), y + i * 12 + j * 12, 0 + mc.fontRendererObj.getStringWidth("Movement") + 5 + length.getValue() + mc.fontRendererObj.getStringWidth("SpeedyGonzales") + 2, y + 12 + i * 12 + j * 12, 1.5F, color, color);
                        } else {
                            Gui.drawRecter(3D + mc.fontRendererObj.getStringWidth("Movement") + 2 + length.getValue(), y + i * 12 + j * 12, 3 + mc.fontRendererObj.getStringWidth("Movement") + 2 + length.getValue() + mc.fontRendererObj.getStringWidth("SpeedyGonzales") + 2, y + 12 + i * 12 + j * 12, bg);
                        }
                        mc.fontRendererObj.drawStringWithShadow(mod.getId(), (float) (spacing.getValue() + mc.fontRendererObj.getStringWidth("Movement") + 3 + 1 + length.getValue() + 2), y + i * 12 + j * 12 + 2, Helium.instance.cheatManager.isCheatEnabled(mod.getId()) ? color : new Color(195, 195, 195, 255).getRGB());
                        //mc.fontRendererObj.drawStringWithShadow(mod.getId(), (float) (spacing.getValue() + mc.fontRendererObj.getStringWidth("Movement") + 3 + 1 + length.getValue() + 2), y + i * 12 + j * 12 + 2, color);
                    }
                }
            }
        }
    }

    public static void onKey(int key) {
        if (key == Keyboard.KEY_UP) {
            if (!isOpen) {
                selected--;
                if (selected == -1) {
                    selected = 0;
                }
            } else {
                moduleSelected--;
                if (moduleSelected <= 0) {
                    moduleSelected = 0;
                }
            }
        }
        if (key == Keyboard.KEY_DOWN) {
            if (!isOpen) {
                selected++;
                if (selected == 5) {
                    selected = 4;
                }
            } else {
                moduleSelected++;
                if (moduleSelected >= getModsForCategory(CheatCategory.values()[selected]).size() - 1) {
                    moduleSelected = getModsForCategory(CheatCategory.values()[selected]).size() - 1;
                }
            }
        }
        if (key == Keyboard.KEY_RIGHT) {
            isOpen = true;
        }
        if (key == Keyboard.KEY_LEFT) {
            isOpen = false;
            moduleSelected = 0;
        }
        if (key == Keyboard.KEY_RETURN) {
            if (getModsForCategory(CheatCategory.values()[selected]).get(moduleSelected).getState()) {
                getModsForCategory(CheatCategory.values()[selected]).get(moduleSelected).setState(false, false);
            } else {
                getModsForCategory(CheatCategory.values()[selected]).get(moduleSelected).setState(true, true);
            }
        }
    }

    private static List<Cheat> getModsForCategory(CheatCategory category) {
        List<Cheat> mods = new ArrayList<>();
        for (Cheat mod : Helium.instance.cheatManager.getCheatRegistry().values()) {
            if (mod.getCategory() == category) {
                mods.add(mod);
            }
        }
        return mods;
    }

    @Collect
    public void on(RenderOverlayEvent e) {
        //drawTabGui();
        if (selected == 5) {
            selected = 0;
        }

    }

    @Collect
    public void key(KeyPressEvent e) {
        //onKey(e.getKeyCode());
    }

}