package Method.Client.clickgui.component.components;

import Method.Client.clickgui.component.Component;
import Method.Client.clickgui.component.Frame;
import Method.Client.clickgui.component.components.sub.*;
import Method.Client.managers.Setting;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.misc.GuiModule;
import Method.Client.utils.font.CFontRenderer;
import Method.Client.utils.visual.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Objects;

import static Method.Client.Main.setmgr;
import static Method.Client.utils.font.CFont.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glEnable;

public class Button extends Component {

    public Module mod;
    public Frame parent;
    private boolean saveisHovered;
    private boolean allmodsSaveHovered;
    private double Animate;
    private final ArrayList<Component> subcomponents;
    private final ArrayList<Component> Inviscomponents;
    public static CFontRenderer buttonFont;
    public static CFontRenderer subcomponentFont;
    public boolean open;
    protected Minecraft mc = Minecraft.getMinecraft();

    public Button(Module mod, Frame parent, int offset) {
        this.hovered = false;
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.saveisHovered = false;
        this.allmodsSaveHovered = false;
        this.subcomponents = new ArrayList<>();
        this.Inviscomponents = new ArrayList<>();
        this.open = false;
        int opY = offset + 12;
        if (setmgr.getSettingsByMod(mod) != null)
            for (Setting s : setmgr.getSettingsByMod(mod)) {
                if (s.isCombo()) {
                    this.subcomponents.add(new ModeButton(s, this, opY));
                    opY += 12;
                }
                if (s.isSlider()) {
                    this.subcomponents.add(new Slider(s, this, opY));
                    opY += 12;
                }
                if (s.isCheck()) {
                    this.subcomponents.add(new Checkbox(s, this, opY));
                    opY += 12;
                }
                if (s.isColor()) {
                    this.subcomponents.add(new ColorPicker(s, this, opY));
                    opY += 12;
                }
                if (s.isGui()) {
                    this.subcomponents.add(new Guibutton(s, this, opY, s.getScreen()));
                    opY += 12;
                }
                if (s.isSub()) {
                    this.subcomponents.add(new Guibutton(s, this, opY, s.getSubGui()));
                    opY += 12;
                }
            }
        this.subcomponents.add(new Keybind(this, opY));
        this.subcomponents.add(new VisibleButton(this, mod, opY));
    }

    public static void updateFont() {
        if (GuiModule.Button.getValString().equalsIgnoreCase("Arial"))
            buttonFont = afontRenderer22;
        if (GuiModule.Button.getValString().equalsIgnoreCase("Times"))
            buttonFont = tfontRenderer22;
        if (GuiModule.Button.getValString().equalsIgnoreCase("Impact"))
            buttonFont = ifontRenderer22;

        if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("Arial"))
            subcomponentFont = afontRenderer18;
        if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("Times"))
            subcomponentFont = tfontRenderer18;
        if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("Impact"))
            subcomponentFont = ifontRenderer18;
    }

    @Override
    public void setOff(int newOff) {
        offset = newOff;
        int opY = offset + 12;
        for (Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }

    @Override
    public void renderComponent() {
        glEnable(GL_BLEND);
        if (this.mod.getCategory().equals(Category.PROFILES)) {
            Gui.drawRect(parent.getX() + 50, this.parent.getY() + this.offset, parent.getX() + 60, this.parent.getY() + 12 + this.offset, 0x6612a212);
            Gui.drawRect(parent.getX() + 65, this.parent.getY() + this.offset, parent.getX() + 75, this.parent.getY() + 12 + this.offset, 0x66f83a32);
        }
        Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.hovered ? (GuiModule.Hover.getcolor()) : GuiModule.Background.getcolor());
        Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, (int) (parent.getX() + this.Animate), this.parent.getY() + 12 + this.offset, GuiModule.ColorAni.getcolor());
        GlStateManager.pushMatrix();
        if (this.mod.getCategory().equals(Category.PROFILES))
            fontSelect("S   A", (float) (parent.getX() + 2) + 49, (float) (parent.getY() + offset + 2.5), this.mod.isToggled() ? 0xA6a83a32 : -1);
        fontSelect(this.mod.getName(), (float) (parent.getX() + 2), (float) (parent.getY() + offset + 2.5) - 1, this.mod.isToggled() ? 0x96a83a32 : -1);
        if (this.subcomponents.size() > 2)
            fontSelect(this.open ? "-" : "+", (float) (parent.getX() + parent.getWidth() - 10), (float) (parent.getY() + offset + 2.5) - 1, -1);
        GlStateManager.popMatrix();
        if (this.open) {
            if (!this.subcomponents.isEmpty()) {
                this.subcomponents.forEach(Component::renderComponent);
                if (GuiModule.border.getValBoolean())
                    RenderUtils.drawRectOutline(parent.getX() + 4, parent.getY() + this.offset + 10, parent.getX() + parent.getWidth(), parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), 1, GuiModule.Highlight.getcolor());
            }
        }
    }

    @Override
    public void RenderTooltip() {
        if (!this.subcomponents.isEmpty())
            subcomponents.forEach(Component::RenderTooltip);
        if (this.hovered) {
            Gui.drawRect(0, (int) (mc.displayHeight / 2.085), (int) (this.mod.getTooltip().length() * 5.1), (int) (mc.displayHeight / 2.085) + 10, 0x4D222222);
            fontSelect(this.mod.getTooltip(), 0, (float) (mc.displayHeight / 2.085), this.mod.isToggled() ? 0xA6999999 : -1);
        }
    }

    @Override
    public int getHeight() {
        if (this.open) {
            return (12 * (this.subcomponents.size() + 1));
        }
        return 12;
    }


    @Override
    public int gety() {
        return this.parent.getY() + this.offset;
    }

    @Override
    public String getName() {
        return this.mod.getName();
    }

    @Override
    public String getCategory() {
        return this.mod.getCategory().toString();
    }

    @Override
    public void runAnimation() {
        if (this.hovered && this.Animate < parent.getWidth()) {
            this.Animate += GuiModule.Anispeed.getValDouble();
        }
        if (!this.hovered && this.Animate > 0)
            this.Animate -= GuiModule.Anispeed.getValDouble();
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        super.updateComponent(mouseX, mouseY);
        if (this.mod.getCategory().equals(Category.PROFILES)) {
            this.saveisHovered = ProfileisMouseOnButton(mouseX, mouseY, false);
            this.allmodsSaveHovered = ProfileisMouseOnButton(mouseX, mouseY, true);
        }
        if (!this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    public static void fontSelect(String name, float x, float y, int i) {
        if (GuiModule.Button.getValString().equalsIgnoreCase("MC"))
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(name, x, y, i);

        else {
            buttonFont.drawStringWithShadow(name, x, y, i);
        }
    }

    public static void fontSelectButton(String name, float x, float y, int color) {
        if (GuiModule.Subcomponents.getValString().equalsIgnoreCase("MC"))
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(name, x, y, color);

        else
            subcomponentFont.drawStringWithShadow(name, x, y, color);
    }


    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (this.hovered && button == 0) {
            if (this.mod.getCategory().equals(Category.PROFILES)) {
                if (this.saveisHovered) {
                    this.mod.setsave();
                    return;
                } else if (this.allmodsSaveHovered) {
                    this.mod.setAll();
                    return;
                }
            }
            this.mod.toggle();
        }
        if (this.hovered && button == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
        CheckInvis();
    }

    private void CheckInvis() {
        if (setmgr.getSettingsByMod(mod) != null) {
            for (Setting s : setmgr.getSettingsByMod(mod)) {
                if (s.getDependant() != null) {

                    int index = 0;
                    Component Compnt = null;

                    for (Component com : Objects.requireNonNull(s.getDependant().isCheck() ? s.getDependant().getValBoolean() ? this.Inviscomponents : this.subcomponents :
                            s.getDependant().isCombo() ? s.getDependant().getValString().equalsIgnoreCase(s.getselected()) ? this.Inviscomponents : this.subcomponents : null))
                        if (com.getName().equalsIgnoreCase(s.getName())) {
                            Compnt = com;
                            for (int i = 0; i < subcomponents.size(); i++) {
                                if (subcomponents.get(i).getName().equalsIgnoreCase(s.getDependant().getName())) {
                                    index = i + 1;
                                    break;
                                }
                            }
                            break;
                        }
                    if (Compnt != null) Updateinvis(Compnt, index);
                }
            }
        }
    }

    private void Updateinvis(Component compnt, double index) {
        if (this.subcomponents.contains(compnt)) this.subcomponents.remove(compnt);
        else subcomponents.add((int) index, compnt);

        if (this.Inviscomponents.contains(compnt)) this.Inviscomponents.remove(compnt);
        else this.Inviscomponents.add(compnt);

        this.parent.refresh();


    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }

    public boolean ProfileisMouseOnButton(int x, int y, boolean delete) {
        return x > this.parent.getX() + (delete ? 65 : 50) && x < this.parent.getX() + (delete ? 75 : 60) && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }
}
