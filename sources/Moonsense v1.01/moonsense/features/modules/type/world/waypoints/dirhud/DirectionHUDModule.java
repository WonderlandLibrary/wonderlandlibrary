// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints.dirhud;

import moonsense.config.utils.AnchorPoint;
import moonsense.enums.ModuleCategory;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import moonsense.features.modules.type.world.waypoints.WaypointsModule;
import moonsense.features.modules.type.world.waypoints.Waypoint;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.modules.type.world.waypoints.DirectionHUDCompassChild;
import moonsense.config.ModuleConfig;
import java.awt.Color;
import moonsense.features.SCModule;
import moonsense.MoonsenseClient;
import moonsense.settings.Setting;
import moonsense.utils.CustomFontRenderer;
import moonsense.features.SCAbstractRenderModule;

public class DirectionHUDModule extends SCAbstractRenderModule
{
    public static DirectionHUDModule INSTANCE;
    private CustomFontRenderer titleRenderer;
    private CustomFontRenderer textRenderer;
    private final Setting showWaypoints;
    private final Setting overrideWaypointMarkerColor;
    private final Setting waypointMarkerColor;
    private final Setting showDirectionMarker;
    private final Setting directionMarkerColor;
    private final Setting showMarkerAngle;
    private final Setting markerAngleColor;
    private final Setting showMainAngleValues;
    private final Setting mainAngleValueColor;
    private final Setting showMainAngleBars;
    private final Setting mainAngleBarColor;
    private final Setting showSecondaryAngleValues;
    private final Setting secondaryAngleValueColor;
    private final Setting showSmallAngleValues;
    private final Setting smallAngleValueColor;
    private final Setting showSmallAngleBars;
    private final Setting smallAngleBarColor;
    public final Setting compassGroup;
    public final Setting compassEnabled;
    public final Setting compassShowWhileTyping;
    public final Setting compassSize;
    public final Setting compassShowWaypoints;
    public final Setting compassShowPlayers;
    public final Setting compassShowMainMarkings;
    public final Setting compassMainMarkingColor;
    public final Setting compassShowSecondaryMarkings;
    public final Setting compassSecondaryMarkingColor;
    public final Setting compassShowTertiaryMarkings;
    public final Setting compassTertiaryMarkingColor;
    public final Setting compassShowDirection;
    public final Setting compassDirectionColor;
    public final Setting compassShowEdge;
    public final Setting compassEdgeColor;
    public final Setting compassShowMagnet;
    public final Setting compassMagnetNorthColor;
    public final Setting compassMagnetSouthColor;
    
    public DirectionHUDModule() {
        super("DirectionHUD", "Shows a compass that lists your current direction and waypoints on the HUD.");
        DirectionHUDModule.INSTANCE = this;
        this.titleRenderer = MoonsenseClient.textRenderer;
        this.textRenderer = MoonsenseClient.tinyTextRenderer;
        new Setting(this, "Marker Options");
        this.showWaypoints = new Setting(this, "Show Waypoint Markers").setDefault(true);
        this.overrideWaypointMarkerColor = new Setting(this, "Override Waypoint Marker Colors").setDefault(false);
        this.waypointMarkerColor = new Setting(this, "Waypoint Marker Color").setDefault(Color.white.getRGB(), 0);
        new Setting(this, "Compass Options");
        this.showDirectionMarker = new Setting(this, "Direction Marker").setDefault(true);
        this.directionMarkerColor = new Setting(this, "Direction Marker Color").setDefault(Color.white.getRGB(), 0);
        this.showMarkerAngle = new Setting(this, "Marker Angle").setDefault(true);
        this.markerAngleColor = new Setting(this, "Marker Angle Color").setDefault(Color.white.getRGB(), 0);
        new Setting(this, "Style Options");
        this.showMainAngleValues = new Setting(this, "Main Angle Values").setDefault(true);
        this.mainAngleValueColor = new Setting(this, "Main Angle Value Color").setDefault(Color.white.getRGB(), 0);
        this.showMainAngleBars = new Setting(this, "Main Angle Bars").setDefault(true);
        this.mainAngleBarColor = new Setting(this, "Main Angle Bar Color").setDefault(Color.white.getRGB(), 0);
        this.showSecondaryAngleValues = new Setting(this, "Secondary Angle Values").setDefault(true);
        this.secondaryAngleValueColor = new Setting(this, "Secondary Angle Color").setDefault(Color.white.getRGB(), 0);
        this.showSmallAngleValues = new Setting(this, "Small Angle Values").setDefault(true);
        this.smallAngleValueColor = new Setting(this, "Small Angle Value Color").setDefault(Color.white.getRGB(), 0);
        this.showSmallAngleBars = new Setting(this, "Small Angle Value Bars").setDefault(true);
        this.smallAngleBarColor = new Setting(this, "Small Angle Bar Color").setDefault(Color.white.getRGB(), 0);
        new Setting(this, "Compass");
        this.compassGroup = new Setting(this, "Compass Options").setDefault(new Setting.CompoundSettingGroup("Compass", new Setting[] { this.compassEnabled = new Setting(null, "Enabled").setDefault(false).compound(true).onValueChanged(setting -> ModuleConfig.INSTANCE.setEnabled(DirectionHUDCompassChild.INSTANCE, setting.getBoolean())), new Setting(null, "General Options").compound(true), this.compassShowWhileTyping = new Setting(null, "Show While Typing").setDefault(true).compound(true).onValueChanged(setting -> DirectionHUDCompassChild.INSTANCE.showWhileTyping.setValue(setting.getBoolean())), this.compassSize = new Setting(null, "Size").setDefault(50).setRange(50, 100, 1).compound(true), new Setting(null, "Style Options").compound(true), this.compassShowWaypoints = new Setting(null, "Show Waypoints").setDefault(true).compound(true), this.compassShowPlayers = new Setting(null, "Show Players").setDefault(false).compound(true), this.compassShowMainMarkings = new Setting(null, "Main Markings").setDefault(true).compound(true), this.compassMainMarkingColor = new Setting(null, "Main Marking Color").setDefault(Color.white.getRGB(), 0).compound(true), this.compassShowSecondaryMarkings = new Setting(null, "Secondary Markings").setDefault(true).compound(true), this.compassSecondaryMarkingColor = new Setting(null, "Secondary Marking Color").setDefault(Color.white.getRGB(), 0).compound(true), this.compassShowTertiaryMarkings = new Setting(null, "Tertiary Markings").setDefault(true).compound(true), this.compassTertiaryMarkingColor = new Setting(null, "Tertiary Marking Color").setDefault(Color.white.getRGB(), 0).compound(true), this.compassShowDirection = new Setting(null, "Show Direction").setDefault(true).compound(true), this.compassDirectionColor = new Setting(null, "Direction Color").setDefault(Color.white.getRGB(), 0).compound(true), this.compassShowEdge = new Setting(null, "Compass Edge").setDefault(true).compound(true), this.compassEdgeColor = new Setting(null, "Compass Edge Color").setDefault(Color.black.brighter().getRGB(), 0).compound(true), this.compassShowMagnet = new Setting(null, "Compass Magnet").setDefault(true).compound(true), this.compassMagnetNorthColor = new Setting(null, "Magnet North Color").setDefault(Color.red.darker().getRGB(), 0).compound(true), this.compassMagnetSouthColor = new Setting(null, "Magnet South Color").setDefault(Color.black.brighter().getRGB(), 0).compound(true) }));
    }
    
    @Override
    public int getWidth() {
        return 300;
    }
    
    @Override
    public int getHeight() {
        return 34;
    }
    
    @Override
    public void render(final float x, float y) {
        y -= 19.0f;
        if (this.titleRenderer == null) {
            this.titleRenderer = MoonsenseClient.textRenderer;
        }
        if (this.textRenderer == null) {
            this.textRenderer = MoonsenseClient.tinyTextRenderer;
        }
        final EntityPlayerSP player = this.mc.thePlayer;
        final float directionFacing = (player == null) ? 0.0f : this.normalizePlayerFacing((int)player.rotationYaw);
        final float halfWidth = this.getWidth() / 2.0f;
        float var7 = -2.0f;
        if (this.showMarkerAngle.getBoolean()) {
            if (this.showDirectionMarker.getBoolean()) {
                this.titleRenderer.drawCenteredString(String.format("%d", (int)directionFacing), x + halfWidth + 0.5f, y + var7 + 18.0f, 805306368);
                this.titleRenderer.drawCenteredString(String.format("%d", (int)directionFacing), x + halfWidth, y + var7 + 0.5f + 18.0f, this.markerAngleColor.getColor());
            }
            else {
                this.titleRenderer.drawCenteredString(String.format("%d", (int)directionFacing), x + halfWidth + 0.5f, y + var7 + 22.5f, 805306368);
                this.titleRenderer.drawCenteredString(String.format("%d", (int)directionFacing), x + halfWidth, y + var7 + 0.5f + 22.5f, this.markerAngleColor.getColor());
            }
        }
        final float n = var7;
        this.textRenderer.getClass();
        var7 = n + (9 * 3 + 1.0f);
        if (this.showDirectionMarker.getBoolean()) {
            GuiUtils.drawDownwardsTriangle(x + halfWidth, y + var7, 6.0f, SCModule.getColor(this.directionMarkerColor.getColorObject()));
        }
        final Iterator<Waypoint> var8 = MoonsenseClient.INSTANCE.getWaypointManager().iterator();
        if (this.showWaypoints.getBoolean()) {
            while (var8.hasNext()) {
                final Waypoint var9 = var8.next();
                if (var9.isVisible() && var9.canRenderWaypoint() && ModuleConfig.INSTANCE.isEnabled(WaypointsModule.INSTANCE)) {
                    this.renderWaypointMarker(player, var9, x + halfWidth, y + var7, directionFacing);
                }
            }
        }
        var7 += 8.0f;
        for (int var10 = 0; var10 <= 360; var10 += 15) {
            this.renderAngle(halfWidth, var7, (float)this.getWidth(), var10, directionFacing, x, y);
        }
    }
    
    @Override
    public void renderDummy(final float x, final float y) {
        this.render(x, y);
    }
    
    public void renderWaypointMarker(final EntityPlayerSP entity, final Waypoint waypoint, final float var3, final float var4, final float var5) {
        final float var6 = (float)(entity.posX - waypoint.getLocation().xCoord);
        final float var7 = (float)(entity.posZ - waypoint.getLocation().zCoord);
        final float var8 = (float)(Math.atan2(var7, var6) * 180.0 / 3.141592653589793 + 90.0);
        float var9 = this.getWidth() * var8 / 360.0f - this.getWidth() * var5 / 360.0f;
        if (var9 > 150.0f) {
            var9 -= this.getWidth();
        }
        if (var9 < -150.0f) {
            var9 += this.getWidth();
        }
        final int var10 = 255 - (int)(Math.abs(var9) / (this.getWidth() / 2.0f) * 255.0f);
        if (var10 >= 35) {
            GuiUtils.drawDownwardsTriangle(var3 + var9, var4, 6.0f, this.overrideWaypointMarkerColor.getBoolean() ? SCModule.getColor(this.waypointMarkerColor.getColorObject()) : (var10 << 24 | (SCModule.getColor(waypoint.getColor()) & 0xFFFFFF)));
        }
    }
    
    public void renderAngle(final float var1, final float var2, final float var3, final int var4, final float var5, final float var6, final float var7) {
        final int var8 = var4 % 90;
        final int var9 = var4 % 45;
        float var10 = this.getWidth() * (float)var4 / 360.0f - 300.0f * var5 / 360.0f;
        if (var10 > 150.0f) {
            var10 -= this.getWidth();
        }
        if (var10 < -150.0f) {
            var10 += this.getWidth();
        }
        final int var11 = 255 - (int)(Math.abs(var10) / (var3 / 2.0f) * 255.0f);
        if (var11 >= 35) {
            final int primaryBarColor = Math.max(35, var11) << 24 | this.mainAngleBarColor.getColor();
            final int primaryTextColor = Math.max(35, var11) << 24 | this.mainAngleValueColor.getColor();
            final int secondaryTextColor = Math.max(35, var11) << 24 | this.secondaryAngleValueColor.getColor();
            final int smallBarColor = Math.max(35, var11) << 24 | this.smallAngleBarColor.getColor();
            final int smallTextColor = Math.max(35, var11) << 24 | this.smallAngleValueColor.getColor();
            if (var8 == 0) {
                if (this.showMainAngleBars.getBoolean()) {
                    GuiUtils.drawRect(var6 + var1 + var10 - 0.5f, var7 + var2, var6 + var1 + var10 + 0.5f, var7 + var2 + 8.0f, primaryBarColor);
                }
                if (this.showMainAngleValues.getBoolean()) {
                    int offset = 0;
                    if (!this.showMainAngleBars.getBoolean()) {
                        offset = 11;
                    }
                    switch (var4) {
                        case 0:
                        case 360: {
                            this.titleRenderer.drawCenteredString("S", var6 + var1 + var10, var7 + var2 + 10.0f - offset, primaryTextColor);
                            break;
                        }
                        case 90: {
                            this.titleRenderer.drawCenteredString("W", var6 + var1 + var10, var7 + var2 + 10.0f - offset, primaryTextColor);
                            break;
                        }
                        case 180: {
                            this.titleRenderer.drawCenteredString("N", var6 + var1 + var10, var7 + var2 + 10.0f - offset, primaryTextColor);
                            break;
                        }
                        case 270: {
                            this.titleRenderer.drawCenteredString("E", var6 + var1 + var10, var7 + var2 + 10.0f - offset, primaryTextColor);
                            break;
                        }
                    }
                }
            }
            else if (var9 == 0) {
                if (this.showSecondaryAngleValues.getBoolean()) {
                    switch (var4) {
                        case 45: {
                            this.titleRenderer.drawCenteredString("SW", var6 + var1 + var10, var7 + var2, secondaryTextColor);
                            break;
                        }
                        case 135: {
                            this.titleRenderer.drawCenteredString("NW", var6 + var1 + var10, var7 + var2, secondaryTextColor);
                            break;
                        }
                        case 225: {
                            this.titleRenderer.drawCenteredString("NE", var6 + var1 + var10, var7 + var2, secondaryTextColor);
                            break;
                        }
                        case 315: {
                            this.titleRenderer.drawCenteredString("SE", var6 + var1 + var10, var7 + var2, secondaryTextColor);
                            break;
                        }
                    }
                }
                else {
                    GuiUtils.drawPartialCircle(var6 + var1 + var10, var7 + var2 + 3.0f, 1.0f, 0, 360, 3.0f, this.secondaryAngleValueColor.getColor(), true);
                }
            }
            else {
                if (this.showSmallAngleBars.getBoolean()) {
                    GuiUtils.drawRect(var6 + var1 + var10 - 0.25f, var7 + var2, var6 + var1 + var10 + 0.25f, var7 + var2 + 5.0f, smallBarColor);
                }
                if (this.showSmallAngleValues.getBoolean()) {
                    int offset = 0;
                    if (!this.showSmallAngleBars.getBoolean()) {
                        offset = 5;
                    }
                    this.textRenderer.drawCenteredString(new StringBuilder().append(var4).toString(), var6 + var1 + var10 - 0.5f, var7 + var2 + 6.0f - offset, smallTextColor);
                }
            }
        }
    }
    
    public float normalizePlayerFacing(final int var1) {
        int var2 = var1;
        if (var1 < 0) {
            while (var2 < 0) {
                var2 += 360;
            }
        }
        else if (var1 > 360) {
            while (var2 > 360) {
                var2 -= 360;
            }
        }
        return (float)var2;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_CENTER;
    }
    
    @Override
    public boolean isNewModule() {
        return true;
    }
}
