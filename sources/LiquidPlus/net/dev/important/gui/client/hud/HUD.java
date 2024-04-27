/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.jetbrains.annotations.NotNull
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.gui.client.hud;

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
import net.dev.important.gui.client.hud.designer.GuiHudDesigner;
import net.dev.important.gui.client.hud.element.Border;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.elements.Armor;
import net.dev.important.gui.client.hud.element.elements.Arraylist;
import net.dev.important.gui.client.hud.element.elements.Effects;
import net.dev.important.gui.client.hud.element.elements.Hotbar;
import net.dev.important.gui.client.hud.element.elements.Inventory;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.gui.client.hud.element.elements.Notifications;
import net.dev.important.gui.client.hud.element.elements.ScoreboardElement;
import net.dev.important.gui.client.hud.element.elements.SessionInfo;
import net.dev.important.gui.client.hud.element.elements.Target;
import net.dev.important.gui.client.hud.element.elements.Text;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010\b\n\u0002\b\r\b\u0017\u0018\u0000 &2\u00020\u0001:\u0001&B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nJ\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u001e\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001aJ\u0016\u0010\u001f\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001aJ\u0006\u0010 \u001a\u00020\u0012J\u000e\u0010!\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010\"\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\nJ\u000e\u0010#\u001a\u00020\u00122\u0006\u0010$\u001a\u00020\u000fJ\u0006\u0010%\u001a\u00020\u0012R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\n\n\u0002\b\b\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007\u00a8\u0006'"}, d2={"Lnet/dev/important/gui/client/hud/HUD;", "Lnet/dev/important/utils/MinecraftInstance;", "()V", "elements", "", "Lnet/dev/important/gui/client/hud/element/Element;", "getElements", "()Ljava/util/List;", "elements$1", "notifications", "Lnet/dev/important/gui/client/hud/element/elements/Notification;", "getNotifications", "addElement", "element", "addNotification", "", "notification", "clearElements", "", "handleDamage", "ent", "Lnet/minecraft/entity/player/EntityPlayer;", "handleKey", "c", "", "keyCode", "", "handleMouseClick", "mouseX", "mouseY", "button", "handleMouseMove", "handleMouseReleased", "removeElement", "removeNotification", "render", "designer", "update", "Companion", "LiquidBounce"})
public class HUD
extends MinecraftInstance {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final List<Element> elements$1 = new ArrayList();
    @NotNull
    private final List<Notification> notifications = new ArrayList();
    @NotNull
    private static final Class<? extends Element>[] elements;

    @NotNull
    public final List<Element> getElements() {
        return this.elements$1;
    }

    @NotNull
    public final List<Notification> getNotifications() {
        return this.notifications;
    }

    public final void render(boolean designer) {
        for (Element element : this.elements$1) {
            GL11.glPushMatrix();
            if (!element.getInfo().disableScale() && !(element.getScale() == 1.0f)) {
                GL11.glScalef((float)element.getScale(), (float)element.getScale(), (float)element.getScale());
            }
            GL11.glTranslated((double)element.getRenderX(), (double)element.getRenderY(), (double)0.0);
            try {
                element.setBorder(element.drawElement());
                if (designer) {
                    Border border = element.getBorder();
                    if (border != null) {
                        border.draw();
                    }
                }
            }
            catch (Exception ex) {
                ClientUtils.getLogger().error("Something went wrong while drawing " + element.getName() + " element in HUD.", (Throwable)ex);
            }
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }

    public final void update() {
        for (Element element : this.elements$1) {
            element.updateElement();
        }
    }

    public final void handleDamage(@NotNull EntityPlayer ent) {
        Intrinsics.checkNotNullParameter(ent, "ent");
        for (Element element : this.elements$1) {
            if (!element.getInfo().retrieveDamage()) continue;
            element.handleDamage(ent);
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
                List<Element> $this$sortBy$iv = this.elements$1;
                boolean $i$f$sortBy = false;
                if ($this$sortBy$iv.size() <= 1) break;
                CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                    public final int compare(T a, T b) {
                        Element it = (Element)a;
                        boolean bl = false;
                        Comparable comparable = Integer.valueOf(-it.getInfo().priority());
                        it = (Element)b;
                        Comparable comparable2 = comparable;
                        bl = false;
                        return ComparisonsKt.compareValues(comparable2, -it.getInfo().priority());
                    }
                });
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
        if (!(MinecraftInstance.mc.field_71462_r instanceof GuiHudDesigner)) {
            return;
        }
        ScaledResolution scaledResolution = new ScaledResolution(MinecraftInstance.mc);
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
            if (moveX == 0.0f && moveY == 0.0f || element.getBorder() == null) continue;
            float minX = Math.min(border.getX(), border.getX2()) + 1.0f;
            float minY = Math.min(border.getY(), border.getY2()) + 1.0f;
            float maxX = Math.max(border.getX(), border.getX2()) - 1.0f;
            float maxY = Math.max(border.getY(), border.getY2()) - 1.0f;
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
        Intrinsics.checkNotNullParameter(element, "element");
        this.elements$1.add(element);
        List<Element> $this$sortBy$iv = this.elements$1;
        boolean $i$f$sortBy = false;
        if ($this$sortBy$iv.size() > 1) {
            CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                public final int compare(T a, T b) {
                    Element it = (Element)a;
                    boolean bl = false;
                    Comparable comparable = Integer.valueOf(-it.getInfo().priority());
                    it = (Element)b;
                    Comparable comparable2 = comparable;
                    bl = false;
                    return ComparisonsKt.compareValues(comparable2, -it.getInfo().priority());
                }
            });
        }
        element.updateElement();
        return this;
    }

    @NotNull
    public final HUD removeElement(@NotNull Element element) {
        Intrinsics.checkNotNullParameter(element, "element");
        element.destroyElement();
        this.elements$1.remove(element);
        List<Element> $this$sortBy$iv = this.elements$1;
        boolean $i$f$sortBy = false;
        if ($this$sortBy$iv.size() > 1) {
            CollectionsKt.sortWith($this$sortBy$iv, new Comparator(){

                public final int compare(T a, T b) {
                    Element it = (Element)a;
                    boolean bl = false;
                    Comparable comparable = Integer.valueOf(-it.getInfo().priority());
                    it = (Element)b;
                    Comparable comparable2 = comparable;
                    bl = false;
                    return ComparisonsKt.compareValues(comparable2, -it.getInfo().priority());
                }
            });
        }
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
            Intrinsics.checkNotNullParameter(notification, "notification");
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
        Intrinsics.checkNotNullParameter(notification, "notification");
        return this.notifications.remove(notification);
    }

    @JvmStatic
    @NotNull
    public static final HUD createDefault() {
        return Companion.createDefault();
    }

    static {
        Class[] classArray = new Class[]{Armor.class, Arraylist.class, Effects.class, Notifications.class, Text.class, ScoreboardElement.class, Target.class, Inventory.class, SessionInfo.class, Hotbar.class};
        elements = classArray;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\n\u001a\u00020\u000bH\u0007R!\u0010\u0003\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\f"}, d2={"Lnet/dev/important/gui/client/hud/HUD$Companion;", "", "()V", "elements", "", "Ljava/lang/Class;", "Lnet/dev/important/gui/client/hud/element/Element;", "getElements", "()[Ljava/lang/Class;", "[Ljava/lang/Class;", "createDefault", "Lnet/dev/important/gui/client/hud/HUD;", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final Class<? extends Element>[] getElements() {
            return elements;
        }

        @JvmStatic
        @NotNull
        public final HUD createDefault() {
            return new HUD().addElement(new ScoreboardElement(0.0, 0.0, 0.0f, null, 15, null));
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

