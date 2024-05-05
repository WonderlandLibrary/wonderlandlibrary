/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ArmorHud;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Arraylist;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Effect;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Hotbar;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Image;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Inventory;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.KickWarn;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Model;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notifications;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.PacketCounter;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.PlayerInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.PlayerList;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Radar;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.ScoreboardElement;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.SpeedGraph;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.TabGUI;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Target2;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Textss;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0017\u0018\u0000 #2\u00020\u0001:\u0001#B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nJ\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u001e\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u001b\u001a\u00020\u0017J\u0016\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u0017J\u0006\u0010\u001d\u001a\u00020\u0012J\u000e\u0010\u001e\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nJ\u000e\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u000fJ\u0006\u0010\"\u001a\u00020\u0012R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\n\n\u0002\b\b\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007\u00a8\u0006$"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "elements", "", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "getElements", "()Ljava/util/List;", "elements$1", "notifications", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/elements/Notification;", "getNotifications", "addElement", "element", "addNotification", "", "notification", "clearElements", "", "handleKey", "c", "", "keyCode", "", "handleMouseClick", "mouseX", "mouseY", "button", "handleMouseMove", "handleMouseReleased", "removeElement", "removeNotification", "render", "designer", "update", "Companion", "KyinoClient"})
public class HUD
extends MinecraftInstance {
    @NotNull
    private final List<Element> elements$1;
    @NotNull
    private final List<Notification> notifications;
    @NotNull
    private static final Class<? extends Element>[] elements;
    public static final Companion Companion;

    @NotNull
    public final List<Element> getElements() {
        return this.elements$1;
    }

    @NotNull
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
                return ComparisonsKt.compareValues(comparable2, (Comparable)n);
            }
        };
        Iterable $this$forEach$iv = CollectionsKt.sortedWith(iterable, comparator);
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
        if (!(HUD.access$getMc$p$s1046033730().field_71462_r instanceof GuiHudDesigner)) {
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(HUD.access$getMc$p$s1046033730());
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
            float width = (float)scaledResolution.func_78326_a() / element.getScale();
            float height = (float)scaledResolution.func_78328_b() / element.getScale();
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

    @NotNull
    public final HUD addElement(@NotNull Element element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.elements$1.add(element);
        element.updateElement();
        return this;
    }

    @NotNull
    public final HUD removeElement(@NotNull Element element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
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

    public final boolean addNotification(@NotNull Notification notification) {
        boolean bl;
        block3: {
            Intrinsics.checkParameterIsNotNull(notification, "notification");
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

    public final boolean removeNotification(@NotNull Notification notification) {
        Intrinsics.checkParameterIsNotNull(notification, "notification");
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
        elements = new Class[]{Image.class, Model.class, TabGUI.class, PlayerList.class, Effect.class, Arraylist.class, PacketCounter.class, Notifications.class, Inventory.class, SpeedGraph.class, Target2.class, ArmorHud.class, Hotbar.class, Textss.class, Radar.class, ScoreboardElement.class, PlayerInfo.class, KickWarn.class};
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    @JvmStatic
    @NotNull
    public static final HUD createDefault() {
        return Companion.createDefault();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u000bH\u0007R!\u0010\u0003\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/HUD$Companion;", "", "()V", "elements", "", "Ljava/lang/Class;", "Lnet/ccbluex/liquidbounce/ui/client/hud/element/Element;", "getElements", "()[Ljava/lang/Class;", "[Ljava/lang/Class;", "createDefault", "Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "KyinoClient"})
    public static final class Companion {
        @NotNull
        public final Class<? extends Element>[] getElements() {
            return elements;
        }

        @JvmStatic
        @NotNull
        public final HUD createDefault() {
            return new HUD().addElement(Textss.Companion.defaultClient()).addElement(new Arraylist(0.0, 0.0, 0.0f, null, 15, null)).addElement(new ScoreboardElement(0.0, 0.0, 0.0f, null, 15, null)).addElement(new ArmorHud(0.0, 0.0, 0.0f, null, 15, null)).addElement(new Effect()).addElement(new Notifications(0.0, 0.0, 0.0f, null, 15, null)).addElement(new Target2());
        }

        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

