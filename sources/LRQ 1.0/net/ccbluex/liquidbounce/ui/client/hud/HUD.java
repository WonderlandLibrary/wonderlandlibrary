/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.comparisons.ComparisonsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import net.ccbluex.liquidbounce.api.minecraft.util.IScaledResolution;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Armor;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ArmorHud;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ArmorInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Arraylist;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Effects;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.HealthHud;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Image;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Inventory;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.KeyBinds;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Model;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NewEffects;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NovolineEffects;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Radar;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ScoreboardElement;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.SessionInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.SessionInfo2;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.lwjgl.opengl.GL11;

public class HUD
extends MinecraftInstance {
    private final List<Element> elements$1;
    private final List<Notification> notifications;
    private static final Class<? extends Element>[] elements;
    public static final Companion Companion;

    public final List<Element> getElements() {
        return this.elements$1;
    }

    public final List<Notification> getNotifications() {
        return this.notifications;
    }

    public final void render(boolean designer) {
        Iterable $this$sortedBy$iv = this.elements$1;
        boolean $i$f$sortedBy = false;
        Iterable iterable = $this$sortedBy$iv;
        boolean bl = false;
        Comparator comparator = new Comparator<T>(){

            public final int compare(T a, T b) {
                boolean bl = false;
                Element it = (Element)a;
                boolean bl2 = false;
                Comparable comparable = Integer.valueOf(-it.getInfo().priority());
                it = (Element)b;
                Comparable comparable2 = comparable;
                bl2 = false;
                Integer n = -it.getInfo().priority();
                return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
            }
        };
        Iterable $this$forEach$iv = CollectionsKt.sortedWith((Iterable)iterable, (Comparator)comparator);
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Element it = (Element)element$iv;
            boolean bl2 = false;
            GL11.glPushMatrix();
            if (!it.getInfo().disableScale()) {
                GL11.glScalef((float)it.getScale(), (float)it.getScale(), (float)it.getScale());
            }
            GL11.glTranslated((double)it.getRenderX(), (double)it.getRenderY(), (double)0.0);
            try {
                it.setBorder(it.drawElement());
                if (designer) {
                    Border border = it.getBorder();
                    if (border != null) {
                        border.draw();
                    }
                }
            }
            catch (Exception ex) {
                ClientUtils.getLogger().error("Something went wrong while drawing " + it.getName() + " element in HUD.", (Throwable)ex);
            }
            GL11.glPopMatrix();
        }
    }

    public final void update() {
        for (Element element : this.elements$1) {
            element.updateElement();
        }
    }

    public final void handleMouseClick(int mouseX, int mouseY, int button) {
        for (Element element : this.elements$1) {
            element.handleMouseClick((double)((float)mouseX / element.getScale()) - element.getRenderX(), (double)((float)mouseY / element.getScale()) - element.getRenderY(), button);
        }
        if (button == 0) {
            for (Element element : CollectionsKt.reversed((Iterable)this.elements$1)) {
                if (!element.isInBorder((double)((float)mouseX / element.getScale()) - element.getRenderX(), (double)((float)mouseY / element.getScale()) - element.getRenderY())) continue;
                element.setDrag(true);
                this.elements$1.remove(element);
                this.elements$1.add(element);
                break;
            }
        }
    }

    public final void handleMouseReleased() {
        for (Element element : this.elements$1) {
            element.setDrag(false);
        }
    }

    public final void handleMouseMove(int mouseX, int mouseY) {
        if (!MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        IScaledResolution scaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
        for (Element element : this.elements$1) {
            Border border;
            float scaledX = (float)mouseX / element.getScale();
            float scaledY = (float)mouseY / element.getScale();
            float prevMouseX = element.getPrevMouseX();
            float prevMouseY = element.getPrevMouseY();
            element.setPrevMouseX(scaledX);
            element.setPrevMouseY(scaledY);
            if (!element.getDrag()) continue;
            float moveX = scaledX - prevMouseX;
            float moveY = scaledY - prevMouseY;
            if (moveX == 0.0f && moveY == 0.0f) continue;
            if (element.getBorder() == null) {
                continue;
            }
            float f = border.getX();
            float f2 = border.getX2();
            boolean bl = false;
            float minX = Math.min(f, f2) + 1.0f;
            f2 = border.getY();
            float f3 = border.getY2();
            boolean bl2 = false;
            float minY = Math.min(f2, f3) + 1.0f;
            f3 = border.getX();
            float f4 = border.getX2();
            boolean bl3 = false;
            float maxX = Math.max(f3, f4) - 1.0f;
            f4 = border.getY();
            float f5 = border.getY2();
            boolean bl4 = false;
            float maxY = Math.max(f4, f5) - 1.0f;
            float width = (float)scaledResolution.getScaledWidth() / element.getScale();
            float height = (float)scaledResolution.getScaledHeight() / element.getScale();
            if ((element.getRenderX() + (double)minX + (double)moveX >= 0.0 || moveX > 0.0f) && (element.getRenderX() + (double)maxX + (double)moveX <= (double)width || moveX < 0.0f)) {
                element.setRenderX(moveX);
            }
            if (!(element.getRenderY() + (double)minY + (double)moveY >= 0.0) && !(moveY > 0.0f) || !(element.getRenderY() + (double)maxY + (double)moveY <= (double)height) && !(moveY < 0.0f)) continue;
            element.setRenderY(moveY);
        }
    }

    public final void handleKey(char c, int keyCode) {
        for (Element element : this.elements$1) {
            element.handleKey(c, keyCode);
        }
    }

    public final HUD addElement(Element element) {
        this.elements$1.add(element);
        element.updateElement();
        return this;
    }

    public final HUD removeElement(Element element) {
        element.destroyElement();
        this.elements$1.remove(element);
        return this;
    }

    public final void clearElements() {
        for (Element element : this.elements$1) {
            element.destroyElement();
        }
        this.elements$1.clear();
    }

    public final boolean addNotification(Notification notification) {
        boolean bl;
        block3: {
            Iterable $this$any$iv = this.elements$1;
            boolean $i$f$any = false;
            if ($this$any$iv instanceof Collection && ((Collection)$this$any$iv).isEmpty()) {
                bl = false;
            } else {
                for (Object element$iv : $this$any$iv) {
                    Element it = (Element)element$iv;
                    boolean bl2 = false;
                    if (!(it instanceof Notifications)) continue;
                    bl = true;
                    break block3;
                }
                bl = false;
            }
        }
        return bl && this.notifications.add(notification);
    }

    public final boolean removeNotification(Notification notification) {
        return this.notifications.remove(notification);
    }

    public HUD() {
        List list;
        HUD hUD = this;
        boolean bl = false;
        hUD.elements$1 = list = (List)new ArrayList();
        hUD = this;
        bl = false;
        hUD.notifications = list = (List)new ArrayList();
    }

    static {
        Companion = new Companion(null);
        elements = new Class[]{HealthHud.class, ArmorHud.class, KeyBinds.class, NovolineEffects.class, Inventory.class, ArmorInfo.class, SessionInfo2.class, SessionInfo.class, NewEffects.class, Armor.class, Arraylist.class, Effects.class, Image.class, Model.class, Notifications.class, ScoreboardElement.class, Target.class, Radar.class};
    }

    @JvmStatic
    public static final HUD createDefault() {
        return Companion.createDefault();
    }

    public static final class Companion {
        public final Class<? extends Element>[] getElements() {
            return elements;
        }

        @JvmStatic
        public final HUD createDefault() {
            return new HUD().addElement(new Arraylist(0.0, 0.0, 0.0f, null, 15, null)).addElement(new ScoreboardElement(0.0, 0.0, 0.0f, null, 15, null)).addElement(new Armor(0.0, 0.0, 0.0f, null, 15, null)).addElement(new Effects()).addElement(new Notifications(0.0, 0.0, 0.0f, null, 15, null));
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

