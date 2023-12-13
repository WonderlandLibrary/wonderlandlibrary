// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import com.elementars.eclient.Xulu;
import com.elementars.eclient.friend.Friends;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.util.Plane;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.AngleHelper;
import java.awt.Color;
import com.elementars.eclient.util.VectorUtils;
import com.elementars.eclient.util.EntityUtil;
import java.util.function.Predicate;
import net.minecraft.entity.EntityLivingBase;
import java.util.Objects;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Arrows extends Module
{
    public final Value<Mode> mode;
    public final Value<Integer> sizeX;
    public final Value<Integer> sizeY;
    public final Value<Float> sizeT;
    public final Value<Boolean> rainbow;
    public final Value<String> colorMode;
    public final Value<Integer> red;
    public final Value<Integer> green;
    public final Value<Integer> blue;
    public final Value<Integer> Fr;
    public final Value<Integer> Fg;
    public final Value<Integer> Fb;
    public final Value<Integer> alpha;
    public final Value<Boolean> outline;
    public final Value<Boolean> black;
    public final Value<Boolean> antialias;
    public final Value<Boolean> players;
    public final Value<Boolean> hostile;
    public final Value<Boolean> friendly;
    
    public Arrows() {
        super("Arrows", "2d Tracers", 0, Category.RENDER, true);
        this.mode = this.register(new Value<Mode>("Mode", this, Mode.ARROWS, Mode.values()));
        this.sizeX = this.register(new Value<Integer>("Dimension X", this, 450, 0, 1000));
        this.sizeY = this.register(new Value<Integer>("Dimension Y", this, 285, 0, 1000));
        this.sizeT = this.register(new Value<Float>("Triangle Size", this, 2.0f, 1.0f, 5.0f));
        this.rainbow = this.register(new Value<Boolean>("Rainbow Players", this, false));
        this.colorMode = this.register(new Value<String>("Color Mode", this, "RGB", new String[] { "RGB", "Tracers" }));
        this.red = this.register(new Value<Integer>("Player Red", this, 0, 0, 255)).visibleWhen(t -> this.colorMode.getValue().equalsIgnoreCase("RGB"));
        this.green = this.register(new Value<Integer>("Player Green", this, 255, 0, 255)).visibleWhen(t -> this.colorMode.getValue().equalsIgnoreCase("RGB"));
        this.blue = this.register(new Value<Integer>("Player Blue", this, 0, 0, 255)).visibleWhen(t -> this.colorMode.getValue().equalsIgnoreCase("RGB"));
        this.Fr = this.register(new Value<Integer>("Friend Red", this, 0, 0, 255));
        this.Fg = this.register(new Value<Integer>("Friend Green", this, 200, 0, 255));
        this.Fb = this.register(new Value<Integer>("Friend Blue", this, 255, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 255, 0, 255));
        this.outline = this.register(new Value<Boolean>("Outline", this, false));
        this.black = this.register(new Value<Boolean>("Black Outline", this, true)).visibleWhen(aBoolean -> this.outline.getValue());
        this.antialias = this.register(new Value<Boolean>("Antialias", this, true));
        this.players = this.register(new Value<Boolean>("Players", this, true));
        this.hostile = this.register(new Value<Boolean>("Mobs", this, true));
        this.friendly = this.register(new Value<Boolean>("Animals", this, true));
    }
    
    @Override
    public void onRender() {
        final ScaledResolution sr = new ScaledResolution(Arrows.mc);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        if (this.antialias.getValue()) {
            GL11.glEnable(2881);
            GL11.glEnable(2848);
        }
        final Mode dm = this.mode.getValue();
        final double cx = sr.getScaledWidth_double() / 2.0;
        final double cy = sr.getScaledHeight_double() / 2.0;
        final Entity entity2;
        final RelationState relationship;
        final Vec3d entityPos;
        final Plane screenPos;
        final Color color;
        float distance;
        Color c;
        Color c2;
        final Enum enum1;
        final double n;
        double dx;
        final double n2;
        double dy;
        double ex;
        double ey;
        double m;
        double nx;
        double ny;
        double x;
        double y;
        double wx;
        double wy;
        final ScaledResolution scaledResolution;
        double ux;
        double uy;
        double mu;
        double mw;
        double ang;
        double ang2;
        int size;
        float distance2;
        Color c3;
        Color c4;
        Arrows.mc.world.loadedEntityList.stream().filter(entity -> !Objects.equals(entity, Arrows.mc.player)).filter(EntityLivingBase.class::isInstance).map(x$0 -> new EntityRelations(x$0)).filter(er -> !er.getRelationship().equals(RelationState.INVALID)).filter(EntityRelations::isOptionEnabled).forEach(er -> {
            entity2 = er.getEntity();
            relationship = er.getRelationship();
            entityPos = EntityUtil.getInterpolatedEyePos(entity2, Arrows.mc.getRenderPartialTicks());
            screenPos = VectorUtils.toScreen(entityPos);
            color = er.getColor();
            if (this.colorMode.getValue().equalsIgnoreCase("Tracers")) {
                distance = Arrows.mc.player.getDistance(entity2);
                if (distance <= 32.0f) {
                    c = new Color(1.0f - distance / 32.0f / 2.0f, distance / 32.0f, 0.0f);
                    GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
                }
                else {
                    c2 = new Color(0.0f, 0.9f, 0.0f);
                    GL11.glColor4f(c2.getRed() / 255.0f, c2.getGreen() / 255.0f, c2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
                }
            }
            else {
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            }
            GlStateManager.translate(0.0f, 0.0f, er.getDepth());
            if ((enum1.equals(Mode.BOTH) || enum1.equals(Mode.ARROWS)) && !screenPos.isVisible()) {
                dx = n - this.sizeX.getValue();
                dy = n2 - this.sizeY.getValue();
                ex = (screenPos.getX() - n) / dx;
                ey = (screenPos.getY() - n2) / dy;
                m = Math.abs(Math.sqrt(ex * ex + ey * ey));
                nx = ex / m;
                ny = ey / m;
                x = n + nx * dx;
                y = n2 + ny * dy;
                wx = x - n;
                wy = y - n2;
                ux = scaledResolution.getScaledWidth_double();
                uy = 0.0;
                mu = Math.sqrt(ux * ux + uy * uy);
                mw = Math.sqrt(wx * wx + wy * wy);
                ang = Math.toDegrees(Math.acos((ux * wx + uy * wy) / (mu * mw)));
                if (ang == Double.NaN) {
                    ang = 0.0;
                }
                if (y < n2) {
                    ang *= -1.0;
                }
                ang2 = (float)AngleHelper.normalizeInDegrees(ang);
                size = (relationship.equals(RelationState.PLAYER) ? 8 : 5);
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, 0.0);
                GlStateManager.rotate((float)ang2, 0.0f, 0.0f, size / 2.0f);
                if (this.colorMode.getValue().equalsIgnoreCase("Tracers")) {
                    distance2 = Arrows.mc.player.getDistance(entity2);
                    if (distance2 <= 32.0f) {
                        c3 = new Color(1.0f - distance2 / 32.0f / 2.0f, distance2 / 32.0f, 0.0f);
                        GL11.glColor4f(c3.getRed() / 255.0f, c3.getGreen() / 255.0f, c3.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
                    }
                    else {
                        c4 = new Color(0.0f, 0.9f, 0.0f);
                        GL11.glColor4f(c4.getRed() / 255.0f, c4.getGreen() / 255.0f, c4.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
                    }
                }
                else {
                    GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
                }
                GlStateManager.glBegin(4);
                GL11.glVertex2d(0.0, 0.0);
                GL11.glVertex2d((double)(-size), (double)(-size / this.sizeT.getValue()));
                GL11.glVertex2d((double)(-size), (double)(size / this.sizeT.getValue()));
                GlStateManager.glEnd();
                if (this.outline.getValue()) {
                    GL11.glPushAttrib(1048575);
                    GL11.glPolygonMode(1032, 6913);
                    GL11.glLineWidth(1.5f);
                    if (this.black.getValue()) {
                        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
                    }
                    GlStateManager.glBegin(4);
                    GL11.glVertex2d(0.0, 0.0);
                    GL11.glVertex2d((double)(-size), (double)(-size / this.sizeT.getValue()));
                    GL11.glVertex2d((double)(-size), (double)(size / this.sizeT.getValue()));
                    GlStateManager.glEnd();
                    GL11.glPopAttrib();
                }
                GlStateManager.popMatrix();
            }
            if (enum1.equals(Mode.BOTH) || enum1.equals(Mode.LINES)) {
                GlStateManager.glBegin(1);
                GL11.glVertex2d(n, n2);
                GL11.glVertex2d(screenPos.getX(), screenPos.getY());
                GlStateManager.glEnd();
            }
            GlStateManager.translate(0.0f, 0.0f, -er.getDepth());
            return;
        });
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glDisable(2881);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    enum Mode
    {
        ARROWS, 
        LINES, 
        BOTH;
    }
    
    public enum RelationState
    {
        PLAYER, 
        HOSTILE, 
        FRIENDLY, 
        INVALID;
    }
    
    private class EntityRelations implements Comparable<EntityRelations>
    {
        private final Entity entity;
        private final RelationState relationship;
        
        public EntityRelations(final Entity entity) {
            Objects.requireNonNull(entity);
            this.entity = entity;
            if (EntityUtil.isFakeLocalPlayer(entity)) {
                this.relationship = RelationState.INVALID;
            }
            else if (entity instanceof EntityPlayer) {
                this.relationship = RelationState.PLAYER;
            }
            else if (EntityUtil.isPassive(entity)) {
                this.relationship = RelationState.FRIENDLY;
            }
            else {
                this.relationship = RelationState.HOSTILE;
            }
        }
        
        public Entity getEntity() {
            return this.entity;
        }
        
        public RelationState getRelationship() {
            return this.relationship;
        }
        
        public Color getColor() {
            switch (this.relationship) {
                case PLAYER: {
                    if (Friends.isFriend(this.getEntity().getName())) {
                        return new Color(Arrows.this.Fr.getValue(), Arrows.this.Fg.getValue(), Arrows.this.Fb.getValue());
                    }
                    if (Arrows.this.rainbow.getValue()) {
                        return new Color(Xulu.rgb);
                    }
                    return new Color(Arrows.this.red.getValue(), Arrows.this.green.getValue(), Arrows.this.blue.getValue());
                }
                case HOSTILE: {
                    return Color.RED;
                }
                default: {
                    return Color.YELLOW;
                }
            }
        }
        
        public float getDepth() {
            switch (this.relationship) {
                case PLAYER: {
                    return 15.0f;
                }
                case HOSTILE: {
                    return 10.0f;
                }
                default: {
                    return 0.0f;
                }
            }
        }
        
        public boolean isOptionEnabled() {
            switch (this.relationship) {
                case PLAYER: {
                    return Arrows.this.players.getValue();
                }
                case HOSTILE: {
                    return Arrows.this.hostile.getValue();
                }
                default: {
                    return Arrows.this.friendly.getValue();
                }
            }
        }
        
        @Override
        public int compareTo(final EntityRelations o) {
            return this.getRelationship().compareTo(o.getRelationship());
        }
    }
}
