package me.aquavit.liquidsense.ui.client.hud;

import com.google.common.collect.Lists;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.ui.client.hud.designer.GuiHudDesigner;
import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.elements.*;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Print;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class HUD extends MinecraftInstance {
    public final List<Element> elements = new ArrayList<>();
    public final List<Notification> notifications = new ArrayList<>();
    public final List<Print> prints = new ArrayList<>();

    public final List<Notification> getNotifications() {
        return this.notifications;
    }

    public final List<Print> getPrints() {
        return this.prints;
    }

    public static final class Companion {

        public static Class<? extends Element>[] getElements() {
            return new Class[]{
                    Armor.class,
                    Arraylist.class,
                    Effects.class,
                    Image.class,
                    Model.class,
                    Notifications.class,
                    Text.class,
                    ScoreboardElement.class,
                    Target.class,
                    Radar.class,
                    Inventory.class,
                    RearView.class,
                    PlayerList.class,
                    SpeedGraph.class,
                    HeadLogo.class,
                    KeyBinds.class,
                    Prints.class,
                    Hotbar.class
            };
        }

        public static HUD createDefault() {
            return (new HUD())
                    .addElement(Text.defaultClient())
                    .addElement(new Arraylist())
                    .addElement(new ScoreboardElement())
                    .addElement(new Armor())
                    .addElement(new Effects())
                    .addElement(new Notifications())
                    .addElement(new SpeedGraph())
                    .addElement(new PlayerList());
        }
    }

    public void render(boolean designer) {
        elements.stream().sorted(Comparator.comparing(it -> -it.getInfo().priority())).forEach( it -> {
            GL11.glPushMatrix();

            if (!it.getInfo().disableScale())
                GL11.glScalef(it.getScale(), it.getScale(), it.getScale());

            GL11.glTranslated(it.getRenderX(), it.getRenderY(), 0.0);

            try {
                it.setBorder(it.drawElement());
                if (designer) {
                    if (it.getBorder() != null) {
                        it.getBorder().draw();
                    }
                }

            } catch (Exception ex) {
                ClientUtils.getLogger()
                        .error("Something went wrong while drawing " + it.getName() + " element in HUD.", ex);

            }


            GL11.glPopMatrix();
        });
    }

    public final void update() {
        for (Element element : elements)
            element.updateElement();

    }

    public final void livingupdate() {
        for (Element element : elements)
            element.livingupdateElement();

    }

    public void handleMouseClick(final int mouseX, final int mouseY, final int button) {
        for (final Element element : elements) {
            element.handleMouseClick((mouseX / element.getScale()) - element.getRenderX(), (mouseY / element.getScale()) - element.getRenderY(), button);
        }
        if(button == 0) {
            for (final Element element : Lists.reverse(elements)) {

                if (!element.isInBorder((mouseX / element.getScale()) - element.getRenderX(),
                        (mouseY / element.getScale()) - element.getRenderY()))
                    continue;

                element.setDrag(true);
                this.elements.remove(element);
                this.elements.add(element);
                break;
            }
        }
    }

    public final void handleMouseReleased() {
        for (Element element : elements)
            element.setDrag(false);
    }

    public final void handleMouseMove(int mouseX, int mouseY) {
        if (!(mc.currentScreen instanceof GuiHudDesigner)) return;

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        for (Element element : elements) {
            float scaledX = mouseX / element.getScale();
            float scaledY = mouseY / element.getScale();

            float prevMouseX = element.getPrevMouseX();
            float prevMouseY = element.getPrevMouseY();

            element.setPrevMouseX(scaledX);
            element.setPrevMouseY(scaledY);


            if (element.getDrag()) {
                float moveX = scaledX - prevMouseX;
                float moveY = scaledY - prevMouseY;


                if (moveX == 0F && moveY == 0F) continue;

                Border border = element.getBorder();

                if (border == null) continue;

                float minX = Math.min(border.x, border.x2) + 1;
                float minY = Math.min(border.y, border.y2) + 1;

                float maxX = Math.max(border.x, border.x2) - 1;
                float maxY = Math.max(border.y, border.y2) - 1;

                float width = scaledResolution.getScaledWidth() / element.getScale();
                float height = scaledResolution.getScaledHeight() / element.getScale();

                if ((element.getRenderX() + minX + moveX >= 0.0 || moveX > 0) && (element.getRenderX() + maxX + moveX <= width || moveX < 0))
                    element.setRenderX(moveX);

                if ((element.getRenderY() + minY + moveY >= 0.0 || moveY > 0) && (element.getRenderY() + maxY + moveY <= height || moveY < 0))
                    element.setRenderY(moveY);

            }
        }
    }

    public final void handleKey(char c, int keyCode) {
        for (Element element : elements)
            element.handleKey(c, keyCode);
    }

    public final HUD addElement(Element element) {
        elements.add(element);
        element.updateElement();
        return this;
    }

    public final void removeElement(Element element) {
        element.destroyElement();
        elements.remove(element);
    }

    public final void clearElements() {
        for (Element element : elements)
            element.destroyElement();

        elements.clear();
    }

    public final boolean addNotification(Notification notification) {
        return elements.stream().anyMatch(it -> it instanceof Notifications) && notifications.add(notification);
    }

    public final boolean removeNotification(Notification notification) {
        return notifications.remove(notification);
    }

    public final boolean addPrint(Print print) {
        return elements.stream().anyMatch(it -> it instanceof Prints) && prints.add(print);
    }

    public final boolean removePrint(Print print) {
        return prints.remove(print);
    }

    public static final HUD createDefault() {
        return Companion.createDefault();
    }

    public List<Element> getElements() {
        return elements;
    }
}
