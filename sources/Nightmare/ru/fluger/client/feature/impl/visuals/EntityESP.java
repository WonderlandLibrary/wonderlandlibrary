// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector3d;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.ui.font.MCFontRenderer;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.feature.Feature;

public class EntityESP extends Feature
{
    public static ListSetting espMode;
    private final int black;
    private final ColorSetting colorEsp;
    private final BooleanSetting friendHighlight;
    private final ColorSetting colorItemEsp;
    private final BooleanSetting fullBox;
    private final BooleanSetting armor;
    private final BooleanSetting border;
    public BooleanSetting healRect;
    public BooleanSetting hpDetails;
    public static BooleanSetting glow;
    public static NumberSetting glowRadius;
    public ListSetting healcolorMode;
    private final ColorSetting healColor;
    public ListSetting csgoMode;
    public ListSetting colorMode;
    public static ListSetting glowMode;
    public static ColorSetting glowColor;
    public BooleanSetting itemESP;
    public ListSetting itemColorMode;
    public BooleanSetting itemsHighlight;
    public ColorSetting itemsHighlightColor;
    public BooleanSetting itemBoxes;
    public BooleanSetting itemTags;
    public BooleanSetting itemCircles;
    public NumberSetting segments;
    public BooleanSetting mobESP;
    public static vg clipEntity;
    
    public EntityESP() {
        super("EntityESP", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432, \u043d\u0438\u043a \u0438 \u0438\u0445 \u0437\u0434\u043e\u0440\u043e\u0432\u044c\u0435 \u0441\u043a\u0432\u043e\u0437\u044c \u0441\u0442\u0435\u043d\u044b", Type.Visuals);
        this.black = Color.BLACK.getRGB();
        this.healRect = new BooleanSetting("Health Rect", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.hpDetails = new BooleanSetting("Health Details", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.healcolorMode = new ListSetting("Color Health Mode", "Client", () -> this.healRect.getCurrentValue() && EntityESP.espMode.currentMode.equals("2D"), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        this.healColor = new ColorSetting("Health Border Color", -1, () -> this.healcolorMode.currentMode.equals("Custom") && !EntityESP.espMode.currentMode.equals("Box"));
        this.colorMode = new ListSetting("Color Box Mode", "Client", () -> EntityESP.espMode.currentMode.equals("Box") || EntityESP.espMode.currentMode.equals("2D"), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        EntityESP.espMode = new ListSetting("ESP Mode", "2D", () -> true, new String[] { "2D", "Box" });
        this.csgoMode = new ListSetting("Border Mode", "Box", () -> EntityESP.espMode.currentMode.equals("2D"), new String[] { "Box", "Corner" });
        this.border = new BooleanSetting("Border Rect", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.armor = new BooleanSetting("Render Armor", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.colorEsp = new ColorSetting("ESP Color", new Color(16777215).getRGB(), () -> !this.colorMode.currentMode.equals("Client") && (EntityESP.espMode.currentMode.equals("2D") || EntityESP.espMode.currentMode.equals("Box")));
        this.friendHighlight = new BooleanSetting("Friend Highlight", false, () -> true);
        this.fullBox = new BooleanSetting("Full Box", false, () -> EntityESP.espMode.currentMode.equals("Box"));
        this.mobESP = new BooleanSetting("Mob ESP", false, () -> true);
        this.itemColorMode = new ListSetting("Item Color-Box Mode", "Client", () -> this.itemESP.getCurrentValue(), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        this.colorItemEsp = new ColorSetting("Item Box Color", Color.WHITE.getRGB(), () -> this.itemColorMode.currentMode.equals("Custom"));
        this.itemsHighlight = new BooleanSetting("Items Highlight", false, () -> true);
        this.itemsHighlightColor = new ColorSetting("Items Highlight Color", Color.WHITE.getRGB(), () -> this.itemsHighlight.getCurrentValue() && this.itemESP.getCurrentValue());
        this.itemESP = new BooleanSetting("Item ESP", true, () -> true);
        this.itemBoxes = new BooleanSetting("Item Boxes", true, () -> this.itemESP.getCurrentValue());
        this.itemTags = new BooleanSetting("Item Tags", true, () -> this.itemESP.getCurrentValue());
        this.itemCircles = new BooleanSetting("Item Circles", false, () -> this.itemESP.getCurrentValue());
        this.segments = new NumberSetting("Circle Segments", 50.0f, 3.0f, 50.0f, 1.0f, () -> this.itemESP.getCurrentValue() && this.itemCircles.getCurrentValue());
        EntityESP.glow = new BooleanSetting("Glow", false, () -> true);
        EntityESP.glowRadius = new NumberSetting("Glow Radius", 5.0f, 1.0f, 10.0f, 1.0f, () -> EntityESP.glow.getCurrentValue());
        EntityESP.glowColor = new ColorSetting("Glow Color", Color.WHITE.getRGB(), () -> EntityESP.glow.getCurrentValue() && EntityESP.glowMode.currentMode.equals("Custom"));
        this.addSettings(EntityESP.espMode, this.csgoMode, this.colorMode, this.healcolorMode, this.healColor, this.border, this.fullBox, this.healRect, this.hpDetails, this.armor, EntityESP.glow, EntityESP.glowMode, EntityESP.glowColor, EntityESP.glowRadius, this.colorEsp, this.friendHighlight, this.mobESP, this.itemESP, this.itemColorMode, this.itemsHighlight, this.itemsHighlightColor, this.colorItemEsp, this.itemBoxes, this.itemTags, this.itemCircles, this.segments);
    }
    
    private void drawScaledString(final String text, final double x, final double y, final double scale, final int color) {
        bus.G();
        bus.b(x, y, x);
        bus.a(scale, scale, scale);
        MCFontRenderer.drawStringWithOutline(EntityESP.mc.k, text, 0.0f, 0.0f, color);
        bus.H();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.setSuffix(EntityESP.espMode.getOptions() + " " + this.csgoMode.getOptions());
        if (EntityESP.glow.getCurrentValue()) {
            for (final aed entityPlayer : EntityESP.mc.f.i) {
                entityPlayer.g(true);
            }
        }
        else {
            for (final aed entityPlayer : EntityESP.mc.f.i) {
                entityPlayer.g(false);
            }
        }
    }
    
    @Override
    public void onDisable() {
        for (final aed entityPlayer : EntityESP.mc.f.i) {
            entityPlayer.g(false);
        }
        super.onDisable();
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event3D) {
        if (!this.getState()) {
            return;
        }
        final Color onecolor1 = new Color(this.colorEsp.getColor());
        final Color c1 = new Color(onecolor1.getRed(), onecolor1.getGreen(), onecolor1.getBlue(), 255);
        Color color = null;
        if (EntityESP.espMode.currentMode.equals("Box")) {
            bus.G();
            for (final vg entity : EntityESP.mc.f.e) {
                if (entity instanceof aed) {
                    if (entity == EntityESP.mc.h && EntityESP.mc.t.aw == 0) {
                        continue;
                    }
                    final String currentMode = this.colorMode.currentMode;
                    switch (currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor();
                            break;
                        }
                        case "Custom": {
                            color = c1;
                            break;
                        }
                        case "Astolfo": {
                            color = PaletteHelper.astolfo(false, (int)entity.H);
                            break;
                        }
                        case "Rainbow": {
                            color = PaletteHelper.rainbow(300, 1.0f, 1.0f);
                            break;
                        }
                    }
                    RenderHelper.drawEntityBox(entity, color, this.fullBox.getCurrentValue(), this.fullBox.getCurrentValue() ? 0.15f : 0.9f);
                }
            }
            bus.H();
        }
        if (this.itemCircles.getCurrentValue()) {
            final int oneColor = this.colorItemEsp.getColor();
            int color2 = 0;
            final String currentMode2 = this.itemColorMode.currentMode;
            switch (currentMode2) {
                case "Client": {
                    color2 = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color2 = oneColor;
                    break;
                }
                case "Astolfo": {
                    color2 = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color2 = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                    break;
                }
            }
            for (final vg entity2 : EntityESP.mc.f.e) {
                if (entity2 != null) {
                    if (!(entity2 instanceof acl)) {
                        continue;
                    }
                    RenderHelper.drawCircle3D(entity2, entity2.G + 0.1 - 0.001, event3D.getPartialTicks(), (int)this.segments.getCurrentValue(), 3.0f, Color.BLACK.getRGB());
                    RenderHelper.drawCircle3D(entity2, entity2.G + 0.1 + 0.001, event3D.getPartialTicks(), (int)this.segments.getCurrentValue(), 3.0f, Color.BLACK.getRGB());
                    RenderHelper.drawCircle3D(entity2, entity2.G + 0.1, event3D.getPartialTicks(), (int)this.segments.getCurrentValue(), 2.0f, color2);
                }
            }
        }
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        bus.G();
        final float partialTicks = EntityESP.mc.Y.renderPartialTicks;
        final int scaleFactor = event.getResolution().e();
        final double scaling = scaleFactor / Math.pow(scaleFactor, 2.0);
        bus.a(scaling, scaling, scaling);
        final int black = this.black;
        final float scale = 1.0f;
        final float upscale = 1.0f / scale;
        for (final vg entity : EntityESP.mc.f.e) {
            if (entity == null) {
                continue;
            }
            final Color onecolor = new Color(this.colorEsp.getColor());
            final Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
            int color = 0;
            final String currentMode = this.colorMode.currentMode;
            switch (currentMode) {
                case "Client": {
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color = c.getRGB();
                    break;
                }
                case "Astolfo": {
                    color = PaletteHelper.astolfo(false, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                    break;
                }
            }
            if (Fluger.instance.friendManager.isFriend(entity.h_()) && this.friendHighlight.getCurrentValue()) {
                color = Color.GREEN.getRGB();
            }
            if (!this.isValid(entity)) {
                continue;
            }
            if (!RenderHelper.isInViewFrustum(entity)) {
                continue;
            }
            final double x = RenderHelper.interpolate(entity.p, entity.M, partialTicks);
            final double y = RenderHelper.interpolate(entity.q, entity.N, partialTicks);
            final double z = RenderHelper.interpolate(entity.r, entity.O, partialTicks);
            final double width = entity.G / 1.5;
            final double height = entity.H + ((entity.aU() || (entity == EntityESP.mc.h && EntityESP.mc.h.aU())) ? -0.3 : ((Fluger.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (!CustomModel.onlyMe.getCurrentValue() || entity == EntityESP.mc.h) && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Amogus") || CustomModel.modelMode.currentMode.equals("Chinchilla") || CustomModel.modelMode.currentMode.equals("Red Panda"))) ? -0.5 : 0.2));
            final bhb axisAlignedBB = new bhb(x - width, y, z - width, x + width, y + height, z + width);
            final Vector3d[] vectors = { new Vector3d(axisAlignedBB.a, axisAlignedBB.b, axisAlignedBB.c), new Vector3d(axisAlignedBB.a, axisAlignedBB.e, axisAlignedBB.c), new Vector3d(axisAlignedBB.d, axisAlignedBB.b, axisAlignedBB.c), new Vector3d(axisAlignedBB.d, axisAlignedBB.e, axisAlignedBB.c), new Vector3d(axisAlignedBB.a, axisAlignedBB.b, axisAlignedBB.f), new Vector3d(axisAlignedBB.a, axisAlignedBB.e, axisAlignedBB.f), new Vector3d(axisAlignedBB.d, axisAlignedBB.b, axisAlignedBB.f), new Vector3d(axisAlignedBB.d, axisAlignedBB.e, axisAlignedBB.f) };
            EntityESP.mc.o.a(partialTicks, 0);
            Vector4d position = null;
            for (Vector3d vector : vectors) {
                vector = this.project2D(scaleFactor, vector.x - EntityESP.mc.ac().o, vector.y - EntityESP.mc.ac().p, vector.z - EntityESP.mc.ac().q);
                if (vector != null && vector.z > 0.0) {
                    if (vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }
            }
            if (position == null) {
                continue;
            }
            EntityESP.mc.o.j();
            final double posX = position.x;
            final double posY = position.y;
            final double endPosX = position.z;
            final double endPosY = position.w;
            final double finalValue = 0.5;
            if (!(entity instanceof acl)) {
                if (EntityESP.espMode.currentMode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Box") && this.border.getCurrentValue()) {
                    RectHelper.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                    RectHelper.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, color);
                    RectHelper.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color);
                    RectHelper.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, color);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, endPosY, color);
                }
                else if (EntityESP.espMode.currentMode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Corner") && this.border.getCurrentValue()) {
                    RectHelper.drawRect(posX + finalValue, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY, posX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, posX + (endPosX - posX) / 3.0 + finalValue, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.0, posY - finalValue, endPosX, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - 1.0, posY, endPosX + finalValue, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(endPosX - 1.0, endPosY, endPosX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - finalValue, endPosY - 1.0, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX, posY, posX - finalValue, posY + (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(posX, endPosY, posX - finalValue, endPosY - (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(posX - finalValue, posY, posX + (endPosX - posX) / 3.0, posY + finalValue, color);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + finalValue, color);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, posY + (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(endPosX - finalValue, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color);
                    RectHelper.drawRect(posX, endPosY - finalValue, posX + (endPosX - posX) / 3.0, endPosY, color);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - finalValue, endPosX - finalValue, endPosY, color);
                }
            }
            final boolean living;
            if (living = (entity instanceof vp)) {
                final vp entityLivingBase = (vp)entity;
                float targetHP = entityLivingBase.cd();
                targetHP = rk.a(targetHP, 0.0f, 24.0f);
                final float maxHealth = entityLivingBase.cj();
                final double hpPercentage = targetHP / maxHealth;
                final double hpHeight2 = (endPosY - posY) * hpPercentage;
                final float armorValue = (float)entityLivingBase.cg();
                if (living && this.armor.getCurrentValue() && !EntityESP.espMode.currentMode.equals("Box") && entity instanceof aed) {
                    final aed player = (aed)entity;
                    if (EntityESP.mc.h.g(player) < 10.0f) {
                        final double ydiff = (endPosY - posY) / 4.0;
                        final aip stack = player.getEquipmentInSlot(4);
                        if (stack != null) {
                            final double diff1 = posY + ydiff - 1.0 - (posY + 2.0);
                            final double percent = 1.0 - stack.i() / (double)stack.k();
                            RenderHelper.renderItem(stack, (int)endPosX + 4, (int)posY + (int)ydiff - 1 - (int)(diff1 / 2.0) - 18);
                        }
                        final aip stack2;
                        if ((stack2 = player.getEquipmentInSlot(3)) != null) {
                            final double diff1 = posY + ydiff * 2.0 - (posY + ydiff + 2.0);
                            final String stackname = stack.r().equalsIgnoreCase("Air") ? "0" : ((stack2.c() instanceof agv) ? (stack2.k() - stack2.i() + "") : stack2.r());
                            RenderHelper.renderItem(stack2, (int)endPosX + 4, (int)(posY + ydiff * 2.0) - (int)(diff1 / 2.0) - 18);
                        }
                        final aip stack3;
                        if ((stack3 = player.getEquipmentInSlot(2)) != null) {
                            final double diff1 = posY + ydiff * 3.0 - (posY + ydiff * 2.0 + 2.0);
                            RenderHelper.renderItem(stack3, (int)endPosX + 4, (int)(posY + ydiff * 3.0) - (int)(diff1 / 2.0) - 18);
                        }
                        final aip stack4 = player.getEquipmentInSlot(1);
                        final double diff2 = posY + ydiff * 4.0 - (posY + ydiff * 3.0 + 2.0);
                        RenderHelper.renderItem(stack4, (int)endPosX + 4, (int)(posY + ydiff * 4.0) - (int)(diff2 / 2.0) - 18);
                    }
                    final double armorWidth = (endPosX - posX) * armorValue / 20.0;
                    RectHelper.drawRect(posX - 0.5, endPosY + 1.0, posX - 0.5 + endPosX - posX, endPosY + 3.0, new Color(0, 0, 0, 120).getRGB());
                    if (armorValue > 0.0f) {
                        RectHelper.drawBorderedRect1(posX - 0.5, endPosY + 1.0, armorWidth, 2.0, 0.5, black, Color.CYAN.getRGB());
                    }
                }
                if (targetHP <= 0.0f) {
                    continue;
                }
                if (this.hpDetails.getCurrentValue() && !EntityESP.espMode.currentMode.equals("Box")) {
                    final String healthDisplay = MathematicHelper.round(((vp)entity).cd(), 1) + " §c?";
                    this.drawScaledString(healthDisplay, posX - 6.0 - EntityESP.mc.k.a(healthDisplay) * 0.5f, endPosY - hpHeight2, 0.5, -1);
                }
                if (this.healRect.getCurrentValue() && !EntityESP.espMode.currentMode.equals("Box")) {
                    int colorHeal = 0;
                    final String currentMode2 = this.healcolorMode.currentMode;
                    switch (currentMode2) {
                        case "Client": {
                            colorHeal = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            colorHeal = this.healColor.getColor();
                            break;
                        }
                        case "Astolfo": {
                            colorHeal = PaletteHelper.astolfo(false, (int)entity.H).getRGB();
                            break;
                        }
                        case "Rainbow": {
                            colorHeal = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                            break;
                        }
                    }
                    RectHelper.drawRect(posX - 4.0, posY - 0.5, posX - 1.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());
                    RectHelper.drawRect(posX - 3.5, endPosY, posX - 2.0, endPosY - hpHeight2, colorHeal);
                }
            }
            if (!(entity instanceof acl)) {
                continue;
            }
            final acl entityItem = (acl)entity;
            if (!this.itemESP.getCurrentValue()) {
                continue;
            }
            final double dif = (endPosX - posX) / 2.0;
            final double textWidth = EntityESP.mc.fontRenderer.getStringWidth(((acl)entity).k().r()) * scale;
            final float tagX = (float)((posX + dif - textWidth / 2.0) * upscale);
            final int oneColor = this.colorItemEsp.getColor();
            int color2 = 0;
            final String currentMode3 = this.itemColorMode.currentMode;
            switch (currentMode3) {
                case "Client": {
                    color2 = ClientHelper.getClientColor().getRGB();
                    break;
                }
                case "Custom": {
                    color2 = oneColor;
                    break;
                }
                case "Astolfo": {
                    color2 = PaletteHelper.astolfo(5000.0f, 1).getRGB();
                    break;
                }
                case "Rainbow": {
                    color2 = PaletteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                    break;
                }
            }
            if (this.itemsHighlight.getCurrentValue() && (entityItem.k().c() instanceof ajy || entityItem.k().c() instanceof ajd || entityItem.k().c() instanceof agv || entityItem.k().c() instanceof aie || entityItem.k().c() instanceof aik)) {
                color2 = this.itemsHighlightColor.getColor();
            }
            if (this.itemBoxes.getCurrentValue()) {
                if (this.csgoMode.currentMode.equalsIgnoreCase("Box")) {
                    RectHelper.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                    RectHelper.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, color2);
                    RectHelper.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color2);
                    RectHelper.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, color2);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, endPosY, color2);
                }
                else if (this.csgoMode.currentMode.equalsIgnoreCase("Corner")) {
                    RectHelper.drawRect(posX + finalValue, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY, posX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, posY - finalValue, posX + (endPosX - posX) / 3.0 + finalValue, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.0, posY - finalValue, endPosX, posY + 1.0, black);
                    RectHelper.drawRect(endPosX - 1.0, posY, endPosX + finalValue, posY + (endPosY - posY) / 4.0 + finalValue, black);
                    RectHelper.drawRect(endPosX - 1.0, endPosY, endPosX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                    RectHelper.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0 - finalValue, endPosY - 1.0, endPosX + finalValue, endPosY + finalValue, black);
                    RectHelper.drawRect(posX, posY, posX - finalValue, posY + (endPosY - posY) / 4.0, color2);
                    RectHelper.drawRect(posX, endPosY, posX - finalValue, endPosY - (endPosY - posY) / 4.0, color2);
                    RectHelper.drawRect(posX - finalValue, posY, posX + (endPosX - posX) / 3.0, posY + finalValue, color2);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + finalValue, color2);
                    RectHelper.drawRect(endPosX - finalValue, posY, endPosX, posY + (endPosY - posY) / 4.0, color2);
                    RectHelper.drawRect(endPosX - finalValue, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color2);
                    RectHelper.drawRect(posX, endPosY - finalValue, posX + (endPosX - posX) / 3.0, endPosY, color2);
                    RectHelper.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - finalValue, endPosX - finalValue, endPosY, color2);
                }
            }
            if (!this.itemTags.getCurrentValue()) {
                continue;
            }
            EntityESP.mc.fontRenderer.drawStringWithShadow(((acl)entity).k().r(), tagX + 1.0f, (float)(posY - 10.0), color2);
        }
        bus.H();
        GL11.glEnable(2929);
        bus.m();
        EntityESP.mc.o.j();
    }
    
    private boolean isValid(final vg entity) {
        return entity != null && (!(entity instanceof acl) || this.itemESP.getCurrentValue()) && (EntityESP.mc.t.aw != 0 || entity != EntityESP.mc.h) && !entity.F && (!(entity instanceof zv) || this.mobESP.getCurrentValue()) && (entity instanceof aed || (!(entity instanceof abz) && (!(entity instanceof vf) || this.mobESP.getCurrentValue()) && !(entity instanceof acb) && !(entity instanceof aeh) && !(entity instanceof aeu) && !(entity instanceof afe) && !(entity instanceof afd) && !(entity instanceof aei) && !(entity instanceof vm) && !(entity instanceof aff) && !(entity instanceof acm) && !(entity instanceof afm) && (!(entity instanceof ady) || this.mobESP.getCurrentValue()) && !(entity instanceof aey) && !(entity instanceof aci) && !(entity instanceof aez) && (!(entity instanceof vg) || entity instanceof ade || entity instanceof aal || entity instanceof vf || entity instanceof vq || entity instanceof acl) && ((!(entity instanceof ade) && !(entity instanceof adl) && !(entity instanceof abd) && !(entity instanceof zz)) || this.mobESP.getCurrentValue()) && entity != EntityESP.mc.h));
    }
    
    private Vector3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        final float xPos = (float)x;
        final float yPos = (float)y;
        final float zPos = (float)z;
        final IntBuffer viewport = bia.f(16);
        final FloatBuffer modelview = bia.h(16);
        final FloatBuffer projection = bia.h(16);
        final FloatBuffer vector = bia.h(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / scaleFactor, (Display.getHeight() - vector.get(1)) / scaleFactor, vector.get(2));
        }
        return null;
    }
    
    static {
        EntityESP.glowMode = new ListSetting("Glow Mode", "Custom", () -> EntityESP.glow.getCurrentValue(), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
    }
}
