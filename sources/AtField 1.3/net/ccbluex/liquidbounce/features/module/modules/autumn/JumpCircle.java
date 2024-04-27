/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.autumn;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MathUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="JumpCircle", description="Autumn jump", category=ModuleCategory.AUTUMN)
public final class JumpCircle
extends Module {
    private final List circles;
    private final FloatValue end;
    private final FloatValue radius;
    private final IntegerValue disappearTime = new IntegerValue("Time", 1000, 1000, 3000);
    private final FloatValue start;
    private final BoolValue rainbow;

    public final List getCircles() {
        return this.circles;
    }

    @EventTarget
    public final void onJump(JumpEvent jumpEvent) {
        long l = System.currentTimeMillis();
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d = iEntityPlayerSP.getPosX();
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d2 = iEntityPlayerSP2.getPosY();
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        this.circles.add(new Circle(l, d, d2, iEntityPlayerSP3.getPosZ()));
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        this.circles.removeIf(new Predicate(this){
            final JumpCircle this$0;

            public final boolean test(Circle circle) {
                return System.currentTimeMillis() > circle.getTime() + ((Number)this.this$0.getDisappearTime().get()).longValue();
            }

            public boolean test(Object object) {
                return this.test((Circle)object);
            }
            {
                this.this$0 = jumpCircle;
            }

            static {
            }
        });
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2884);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)3008);
        GL11.glShadeModel((int)7425);
        Iterable iterable = this.circles;
        boolean bl = false;
        for (Object t : iterable) {
            Circle circle = (Circle)t;
            boolean bl2 = false;
            circle.draw();
        }
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)3008);
        GL11.glShadeModel((int)7424);
        GL11.glPopMatrix();
    }

    public JumpCircle() {
        List list;
        this.radius = new FloatValue("Radius", 2.0f, 1.0f, 5.0f);
        this.rainbow = new BoolValue("Rainbow", false);
        this.start = new FloatValue("Start", 0.5f, 0.0f, 1.0f);
        this.end = new FloatValue("End", 0.3f, 0.0f, 1.0f);
        JumpCircle jumpCircle = this;
        boolean bl = false;
        jumpCircle.circles = list = (List)new ArrayList();
    }

    public final FloatValue getEnd() {
        return this.end;
    }

    public final FloatValue getRadius() {
        return this.radius;
    }

    public final IntegerValue getDisappearTime() {
        return this.disappearTime;
    }

    public final BoolValue getRainbow() {
        return this.rainbow;
    }

    public final FloatValue getStart() {
        return this.start;
    }

    public static final class Circle {
        private final double z;
        private final double x;
        private final JumpCircle jumpModule;
        private final long time;
        private final double y;

        public final JumpCircle getJumpModule() {
            return this.jumpModule;
        }

        public final double getX() {
            return this.x;
        }

        public final long getTime() {
            return this.time;
        }

        public final double getZ() {
            return this.z;
        }

        public final void draw() {
            if (this.jumpModule == null) {
                return;
            }
            long l = System.currentTimeMillis() - this.time;
            float f = (float)255 - (float)l / (float)((Number)this.jumpModule.getDisappearTime().get()).intValue() * (float)255;
            GL11.glPushMatrix();
            GL11.glTranslated((double)(this.x - MinecraftInstance.mc.getRenderManager().getViewerPosX()), (double)(this.y - MinecraftInstance.mc.getRenderManager().getViewerPosY()), (double)(this.z - MinecraftInstance.mc.getRenderManager().getViewerPosZ()));
            GL11.glBegin((int)5);
            int n = 360;
            for (int i = 0; i <= n; ++i) {
                Color color = (Boolean)this.jumpModule.getRainbow().get() != false ? Color.getHSBColor((float)i / 360.0f, 1.0f, 1.0f) : ColorUtils.hsbTransition$default(ColorUtils.INSTANCE, ((Number)this.jumpModule.getStart().get()).floatValue(), ((Number)this.jumpModule.getEnd().get()).floatValue(), i, 0.0f, 0.0f, 24, null);
                double d = MathUtils.INSTANCE.toRadians(i);
                double d2 = (double)((float)l * ((Number)this.jumpModule.getRadius().get()).floatValue()) * 0.001;
                boolean bl = false;
                double d3 = Math.sin(d);
                double d4 = d2 * d3;
                double d5 = MathUtils.INSTANCE.toRadians(i);
                d2 = (double)((float)l * ((Number)this.jumpModule.getRadius().get()).floatValue()) * 0.001;
                boolean bl2 = false;
                d3 = Math.cos(d5);
                d = d2 * d3;
                RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), 0);
                GL11.glVertex3d((double)(d4 / (double)2), (double)0.0, (double)(d / (double)2));
                RenderUtils.glColor(color.getRed(), color.getGreen(), color.getBlue(), (int)f);
                GL11.glVertex3d((double)d4, (double)0.0, (double)d);
            }
            GL11.glEnd();
            GL11.glPopMatrix();
        }

        public final double getY() {
            return this.y;
        }

        public Circle(long l, double d, double d2, double d3) {
            this.time = l;
            this.x = d;
            this.y = d2;
            this.z = d3;
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(JumpCircle.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.autumn.JumpCircle");
            }
            this.jumpModule = (JumpCircle)module;
        }
    }
}

