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
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Arraylist;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Effects;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.GameInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Image;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Inventory;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.KeyBinds;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Model;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.QQLogo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Radar;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ScoreboardElement;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.TabGUI;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Text;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.lwjgl.opengl.GL11;

public class HUD
extends MinecraftInstance {
    public static final Companion Companion = new Companion(null);
    private final List notifications;
    private static final Class[] elements = new Class[]{Armor.class, Arraylist.class, Effects.class, Inventory.class, Image.class, Model.class, GameInfo.class, Notifications.class, KeyBinds.class, TabGUI.class, Text.class, ScoreboardElement.class, Target.class, Radar.class, QQLogo.class};
    private final List elements$1;

    public final void clearElements() {
        for (Element element : this.elements$1) {
            element.destroyElement();
        }
        this.elements$1.clear();
    }

    public final void update() {
        for (Element element : this.elements$1) {
            element.updateElement();
        }
    }

    public final boolean addNotification(Notification notification) {
        boolean bl;
        block3: {
            Iterable iterable = this.elements$1;
            boolean bl2 = false;
            if (iterable instanceof Collection && ((Collection)iterable).isEmpty()) {
                bl = false;
            } else {
                for (Object t : iterable) {
                    Element element = (Element)t;
                    boolean bl3 = false;
                    if (!(element instanceof Notifications)) continue;
                    bl = true;
                    break block3;
                }
                bl = false;
            }
        }
        return bl && this.notifications.add(notification);
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

    public final boolean removeNotification(Notification notification) {
        return this.notifications.remove(notification);
    }

    public final List getNotifications() {
        return this.notifications;
    }

    public final HUD addElement(Element element) {
        this.elements$1.add(element);
        element.updateElement();
        return this;
    }

    @JvmStatic
    public static final HUD createDefault() {
        return Companion.createDefault();
    }

    public final void handleMouseMove(int n, int n2) {
        if (!MinecraftInstance.classProvider.isGuiHudDesigner(MinecraftInstance.mc.getCurrentScreen())) {
            return;
        }
        IScaledResolution iScaledResolution = MinecraftInstance.classProvider.createScaledResolution(MinecraftInstance.mc);
        for (Element element : this.elements$1) {
            Border border;
            float f = (float)n / element.getScale();
            float f2 = (float)n2 / element.getScale();
            float f3 = element.getPrevMouseX();
            float f4 = element.getPrevMouseY();
            element.setPrevMouseX(f);
            element.setPrevMouseY(f2);
            if (!element.getDrag()) continue;
            float f5 = f - f3;
            float f6 = f2 - f4;
            if (f5 == 0.0f && f6 == 0.0f) continue;
            if (element.getBorder() == null) {
                continue;
            }
            float f7 = border.getX();
            float f8 = border.getX2();
            boolean bl = false;
            float f9 = Math.min(f7, f8) + 1.0f;
            f8 = border.getY();
            float f10 = border.getY2();
            boolean bl2 = false;
            f7 = Math.min(f8, f10) + 1.0f;
            f10 = border.getX();
            float f11 = border.getX2();
            boolean bl3 = false;
            f8 = Math.max(f10, f11) - 1.0f;
            f11 = border.getY();
            float f12 = border.getY2();
            boolean bl4 = false;
            f10 = Math.max(f11, f12) - 1.0f;
            f11 = (float)iScaledResolution.getScaledWidth() / element.getScale();
            f12 = (float)iScaledResolution.getScaledHeight() / element.getScale();
            if ((element.getRenderX() + (double)f9 + (double)f5 >= 0.0 || f5 > 0.0f) && (element.getRenderX() + (double)f8 + (double)f5 <= (double)f11 || f5 < 0.0f)) {
                element.setRenderX(f5);
            }
            if (!(element.getRenderY() + (double)f7 + (double)f6 >= 0.0) && !(f6 > 0.0f) || !(element.getRenderY() + (double)f10 + (double)f6 <= (double)f12) && !(f6 < 0.0f)) continue;
            element.setRenderY(f6);
        }
    }

    public final List getElements() {
        return this.elements$1;
    }

    public final void handleMouseClick(int n, int n2, int n3) {
        for (Element element : this.elements$1) {
            element.handleMouseClick((double)((float)n / element.getScale()) - element.getRenderX(), (double)((float)n2 / element.getScale()) - element.getRenderY(), n3);
        }
        if (n3 == 0) {
            for (Element element : CollectionsKt.reversed((Iterable)this.elements$1)) {
                if (!element.isInBorder((double)((float)n / element.getScale()) - element.getRenderX(), (double)((float)n2 / element.getScale()) - element.getRenderY())) continue;
                element.setDrag(true);
                this.elements$1.remove(element);
                this.elements$1.add(element);
                break;
            }
        }
    }

    public final void handleKey(char c, int n) {
        for (Element element : this.elements$1) {
            element.handleKey(c, n);
        }
    }

    public final HUD removeElement(Element element) {
        element.destroyElement();
        this.elements$1.remove(element);
        return this;
    }

    public final void render(boolean bl) {
        Iterable iterable = this.elements$1;
        boolean bl2 = false;
        Iterable iterable2 = iterable;
        boolean bl3 = false;
        Object object = new Comparator(){

            public final int compare(Object object, Object object2) {
                boolean bl = false;
                Element element = (Element)object;
                boolean bl2 = false;
                Comparable comparable = Integer.valueOf(-element.getInfo().priority());
                element = (Element)object2;
                Comparable comparable2 = comparable;
                bl2 = false;
                Integer n = -element.getInfo().priority();
                return ComparisonsKt.compareValues((Comparable)comparable2, (Comparable)n);
            }

            static {
            }
        };
        iterable = CollectionsKt.sortedWith((Iterable)iterable2, (Comparator)object);
        bl2 = false;
        for (Object e : iterable) {
            object = (Element)e;
            boolean bl4 = false;
            GL11.glPushMatrix();
            if (!((Element)object).getInfo().disableScale()) {
                GL11.glScalef((float)((Element)object).getScale(), (float)((Element)object).getScale(), (float)((Element)object).getScale());
            }
            GL11.glTranslated((double)((Element)object).getRenderX(), (double)((Element)object).getRenderY(), (double)0.0);
            try {
                ((Element)object).setBorder(((Element)object).drawElement());
                if (bl) {
                    Border border = ((Element)object).getBorder();
                    if (border != null) {
                        border.draw();
                    }
                }
            }
            catch (Exception exception) {
                ClientUtils.getLogger().error("Something went wrong while drawing " + ((Element)object).getName() + " element in HUD.", (Throwable)exception);
            }
            GL11.glPopMatrix();
        }
    }

    public final void handleMouseReleased() {
        for (Element element : this.elements$1) {
            element.setDrag(false);
        }
    }

    public static final Class[] access$getElements$cp() {
        return elements;
    }

    public static final class Companion {
        public final Class[] getElements() {
            return HUD.access$getElements$cp();
        }

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final HUD createDefault() {
            return new HUD().addElement(new Arraylist(0.0, 0.0, 0.0f, null, 15, null)).addElement(new ScoreboardElement(0.0, 0.0, 0.0f, null, 15, null)).addElement(new Armor(0.0, 0.0, 0.0f, null, 15, null)).addElement(new Effects(0.0, 0.0, 0.0f, 7, null)).addElement(new Notifications(0.0, 0.0, 0.0f, null, 15, null));
        }
    }
}

