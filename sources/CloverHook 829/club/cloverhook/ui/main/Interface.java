package club.cloverhook.ui.main;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.ui.main.components.Component;
import club.cloverhook.ui.main.components.tab.main.MainButtonTab;
import club.cloverhook.ui.main.tab.Tab;
import club.cloverhook.ui.main.tab.TabConfiguration;
import club.cloverhook.ui.main.tab.cheat.TabDefaultCheat;
import club.cloverhook.ui.main.utility.ExpandAnimation;
import club.cloverhook.utils.*;
import club.cloverhook.utils.font.Fonts;

import java.util.ArrayList;

/**
 * @author antja03
 */
public class Interface {
    private Screen screen;

    private double positionX, positionY, width = 300, height = 200;

    private double currentFrameMouseX, currentFrameMouseY, prevFrameMouseX, prevFrameMouseY;

    private boolean moving = false;

    private boolean closing = false;

    private final ArrayList<Component> components = new ArrayList<>();

    private final ArrayList<Tab> tabs = new ArrayList<>();

    private Tab currentTab;

    private ExpandAnimation expandAnimation = new ExpandAnimation(0, 0);

    private double alphaModifer;

    private Stopwatch alphaStopwatch;

    private Stopwatch tickStopwatch = new Stopwatch();

    public Interface(Screen screen) {
        this.screen = screen;
        positionX = (screen.getResolution().getScaledWidth() >> 1) - this.width / 2;
        positionY = (screen.getResolution().getScaledHeight() >> 1) - this.height / 2;
        alphaModifer = 0.0;
        alphaStopwatch = new Stopwatch();

        tabs.add(new TabDefaultCheat(this, CheatCategory.COMBAT));
        tabs.add(new TabDefaultCheat(this, CheatCategory.MOVEMENT));
        tabs.add(new TabDefaultCheat(this, CheatCategory.VISUAL));
        tabs.add(new TabDefaultCheat(this, CheatCategory.PLAYER));
        tabs.add(new TabDefaultCheat(this, CheatCategory.MISC));
        tabs.add(new TabConfiguration(this));

        double inc = (height - 32) / 7;
        components.add(new MainButtonTab(this, tabs.get(0), new ResourceLocation("client/gui/icon/interface/combat_icon.png"), 4, 32 + (inc * 0),
                20, inc, button -> currentTab = tabs.get(0)));
        components.add(new MainButtonTab(this, tabs.get(1), new ResourceLocation("client/gui/icon/interface/movement_icon.png"), 4, 32 + (inc * 1),
                20, inc, button -> currentTab = tabs.get(1)));
        components.add(new MainButtonTab(this, tabs.get(2), new ResourceLocation("client/gui/icon/interface/visuals_icon.png"), 4, 32 + (inc * 2),
                20, inc, button -> currentTab = tabs.get(2)));
        components.add(new MainButtonTab(this, tabs.get(3), new ResourceLocation("client/gui/icon/interface/player_icon.png"), 4, 32 + (inc * 3),
                20, inc, button -> currentTab = tabs.get(3)));
        components.add(new MainButtonTab(this, tabs.get(4), new ResourceLocation("client/gui/icon/interface/misc_icon.png"), 4, 32 + (inc * 4),
                20, inc, button -> currentTab = tabs.get(4)));
        components.add(new MainButtonTab(this, tabs.get(5), new ResourceLocation("client/gui/icon/interface/configs_icon.png"), 4, 32 + (inc * 5),
                20, inc, button -> currentTab = tabs.get(5)));
    }

    void initializeInterface() {
        moving = false;
        closing = false;
        expandAnimation.setX(0);
        expandAnimation.setY(0);
        tickStopwatch.reset();
    }

    public void drawInterface(double mouseX, double mouseY) {
        this.currentFrameMouseX = mouseX;
        this.currentFrameMouseY = mouseY;

        if (moving) {
            final double differenceX = this.currentFrameMouseX - this.prevFrameMouseX;
            final double differenceY = this.currentFrameMouseY - this.prevFrameMouseY;
            if ((this.positionX + differenceX > 0 || differenceX > 0) &&
                    (this.positionX + width + differenceX < screen.getResolution().getScaledWidth() || differenceX < 0)) {
                this.positionX += differenceX;

            }
            if ((this.positionY + differenceY > 0 || differenceY > 0)
                    && (this.positionY + height + differenceY < screen.getResolution().getScaledHeight() || differenceY < 0)) {
                this.positionY += differenceY;
            }
        }

        if (closing) {
            expandAnimation.expand(0f, 0f, 0.2685f, 0.2385f);
            if (alphaStopwatch.hasPassed(20)) {
                alphaModifer -= 0.1;
                if (alphaModifer <= 0) {
                    alphaModifer = 0;
                }
                alphaStopwatch.reset();
            }
        } else {
            if (alphaStopwatch.hasPassed(20)) {
                alphaModifer += 0.1;
                if (alphaModifer > 1) {
                    alphaModifer = 1;
                }
                alphaStopwatch.reset();
            }
            expandAnimation.expand((float) width * 2, (float) height * 4, 0.04385f, 0.06385f);
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        double scissorX = positionX + width / 2 - expandAnimation.getX();
        double scissorY = (screen.getResolution().getScaledHeight() - (positionY + height + 10)) + height / 2 + 10 / 2 - (expandAnimation.getY() / 2);

        GL11.glScissor(
                (int) (scissorX) * screen.getResolution().getScaleFactor(),
                (int) (scissorY) * screen.getResolution().getScaleFactor(),
                (int) (expandAnimation.getX() * 2) * screen.getResolution().getScaleFactor(),
                (int) (expandAnimation.getY()) * screen.getResolution().getScaleFactor());

        //Base
        {
            Draw.drawRectangle(positionX - 0.5, positionY - 0.5, positionX + width + 0.5, positionY + height + 0.5, getColor(15, 15, 15, 255));
            Draw.drawRectangle(positionX - 1, positionY - 1, positionX + width + 1, positionY + height + 1, getColor(15, 15, 15, 100));
            Draw.drawRectangle(positionX - 1.5, positionY - 1.5, positionX + width + 1.5, positionY + height + 1.5, getColor(15, 15, 15, 50));
        }

        //Side bar + watermark
        {
            Draw.drawRectangle(positionX, positionY, positionX + 24, positionY + height, getColor(25, 25, 25, 60));
            GL11.glColor3f(1f, 1f, 1f);
            Draw.drawImg(new ResourceLocation("client/gui/logo/32x32.png"), positionX + 4, positionY + 4, 16, 16);

            Draw.drawRectangle(positionX, positionY + 24, positionX + 24, positionY + 25, getColor(0, 0, 0, 50));

            Draw.drawRectangle(positionX + 24, positionY, positionX + 25.5, positionY + height, getColor(0, 0, 0, 50));
            Draw.drawRectangle(positionX + 24, positionY, positionX + 25, positionY + height, getColor(0, 0, 0, 50));
            Draw.drawRectangle(positionX + 24, positionY, positionX + 24.5, positionY + height, getColor(0, 0, 0, 50));

            for (Component component : components) {
                if (component instanceof  MainButtonTab) {
                    MainButtonTab tabButton = (MainButtonTab) component;
                    if (tabButton.getTab() == currentTab) {
                        Draw.drawRectangle(positionX + 24, positionY + tabButton.positionY - 7, positionX + 25, positionY + tabButton.positionY + 24, getColor(37, 231, 71));
                    }
                }
            }
        }

        components.forEach(component -> component.drawComponent(positionX + component.positionX, positionY + component.positionY));

        if (currentTab == null) {
            /*Fonts.f14.drawString("Warning: This client hasn't been fully released yet. This is a BETA build so there is bound to", positionX + 30, positionY + 6, getColor(215, 215, 215));
            Fonts.f14.drawString("be bugs. When you find them, report them in the Discord or on the forums @ ethereal.rip", positionX + 30, positionY + 16, getColor(215, 215, 215));
            Fonts.f14.drawString("Thank you.", positionX + 30, positionY + 26, getColor(215, 215, 215));*/

//            if (Ethereal.instance.getMember() != null) {
//                String unformattedGroupName = Ethereal.instance.getMember().getGroup().name();
//                String groupText = String.format(unformattedGroupName.substring(0, 1) + unformattedGroupName.toLowerCase().substring(1) + " (%s)",
//                        Ethereal.instance.getMember().getGroup().getId());
//
//                Fonts.f14.drawString("Beta Build #: " + "#070119", positionX + 30, positionY + 46, getColor(215, 215, 215));
//                Fonts.f14.drawString(String.format("Member Name: %s", Ethereal.instance.getMember().getName()), positionX + 30, positionY + 56, getColor(215, 215, 215));//Make username sync with auth server
//                Fonts.f14.drawString(String.format("Member Group: %s", groupText), positionX + 30, positionY + 66, getColor(215, 215, 215));//make UID sync with auth server
//            } else {
                /*Fonts.f14.drawString("Beta Build #: " + "#070119", positionX + 30, positionY + 46, getColor(215, 215, 215));
                Fonts.f14.drawString(String.format("Member Name: %s", "loading..."), positionX + 30, positionY + 56, getColor(215, 215, 215));//Make username sync with auth server
                Fonts.f14.drawString(String.format("Member Group: %s", "loading..."), positionX + 30, positionY + 66, getColor(215, 215, 215));//make UID sync with auth server*/
//            }
        } else {
            currentTab.components.forEach(component -> component.drawComponent(positionX + component.positionX, positionY + component.positionY));
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();

        if (tickStopwatch.hasPassed(50)) {
            if (currentTab != null) {
                currentTab.onTick();
            }
            tickStopwatch.reset();
        }

        this.prevFrameMouseX = this.currentFrameMouseX;
        this.prevFrameMouseY = this.currentFrameMouseY;
    }

    void mouseButtonClicked(int button) {
        if (isMouseInBounds(positionX, positionX + 24, positionY, positionY + 24)) {
            currentTab = null;
            return;
        }
        for (Component component : components) {
            if (component.mouseButtonClicked(button)) {
                return;
            }
        }
        if (currentTab != null) {
            for (Component component : currentTab.components) {
                if (component.mouseButtonClicked(button)) {
                    return;
                }
            }
        }
        moving = true;
    }

    void mouseButtonReleased(int state) {
        if (moving) {
            moving = false;
            return;
        }

        for (Component component : components) {
            component.mouseReleased();
        }

        if (currentTab != null) {
            for (Component component : currentTab.components) {
                component.mouseReleased();
            }
        }
    }

    void mouseScrolled(int scrollDirection) {
        for (Component component : components) {
            component.mouseScrolled(scrollDirection);
        }

        if (currentTab != null) {
            for (Component component : currentTab.components) {
                component.mouseScrolled(scrollDirection);
            }
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        for (Component component : components) {
            if (component.keyTyped(typedChar, keyCode)) {
                return true;
            }
        }

        if (currentTab != null) {
            for (Component component : currentTab.components) {
                if (component.keyTyped(typedChar, keyCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMouseInBounds(double x1, double x2, double y1, double y2) {
        if (x1 > x2) {
            double temp = x1;
            x1 = x2;
            x2 = temp;
        }

        if (y1 > y2) {
            double temp = y1;
            y1 = y2;
            y2 = temp;
        }

        return currentFrameMouseX > x1 && currentFrameMouseX < x2
                && currentFrameMouseY > y1 && currentFrameMouseY < y2;
    }

    public int getColor(int red, int green, int blue) {
        return ColorCreator.create(red, green, blue, (int) (255 * alphaModifer));
    }

    public int getColor(int red, int green, int blue, int alpha) {
        return ColorCreator.create(red, green, blue, (int) (alpha * alphaModifer));
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getWidth() {
        return width - 24;
    }

    public double getHeight() {
        return height;
    }

    public boolean isClosing() {
        return closing;
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
        if (closing) {
            components.forEach(Component::onGuiClose);
            if (currentTab != null)
                currentTab.components.forEach(Component::onGuiClose);
        }
    }

    public Tab getCurrentTab() {
        return currentTab;
    }

    public void setWidth(double width) {
        this.width = width + 24;
    }

    public double getCurrentFrameMouseX() {
        return currentFrameMouseX;
    }

    public double getCurrentFrameMouseY() {
        return currentFrameMouseY;
    }
}
