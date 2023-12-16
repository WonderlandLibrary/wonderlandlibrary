// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules.type.world.waypoints;

import moonsense.enums.ModuleCategory;
import moonsense.config.utils.AnchorPoint;
import net.minecraft.client.gui.Gui;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import moonsense.config.ModuleConfig;
import moonsense.ui.utils.GuiUtils;
import moonsense.MoonsenseClient;
import moonsense.features.SCModule;
import moonsense.features.modules.type.world.waypoints.dirhud.DirectionHUDModule;
import moonsense.features.SCAbstractRenderModule;

public class DirectionHUDCompassChild extends SCAbstractRenderModule
{
    public static DirectionHUDCompassChild INSTANCE;
    private DirectionHUDModule module;
    
    public DirectionHUDCompassChild() {
        super("DirectionHUD Compass", "");
        this.setParentModule(DirectionHUDModule.INSTANCE);
        this.module = DirectionHUDModule.INSTANCE;
        DirectionHUDCompassChild.INSTANCE = this;
    }
    
    @Override
    public int getWidth() {
        if (this.module == null) {
            this.module = DirectionHUDModule.INSTANCE;
        }
        return this.module.compassSize.getInt();
    }
    
    @Override
    public int getHeight() {
        if (this.module == null) {
            this.module = DirectionHUDModule.INSTANCE;
        }
        int baseHeight = this.module.compassSize.getInt();
        if (this.module.compassShowDirection.getBoolean()) {
            final int n = baseHeight;
            final int n2 = 2;
            MoonsenseClient.titleRenderer.getClass();
            baseHeight = n + (n2 + 9);
        }
        return baseHeight;
    }
    
    @Override
    public void render(final float x, final float y) {
        if (this.module == null) {
            this.module = DirectionHUDModule.INSTANCE;
        }
        if (this.getParentModule() == null) {
            this.setParentModule(DirectionHUDModule.INSTANCE);
        }
        final EntityPlayerSP player = this.mc.thePlayer;
        final float directionFacing = (player == null) ? 0.0f : this.normalizePlayerFacing((int)player.rotationYaw);
        final float halfWidth = this.getWidth() / 2.0f;
        final float var7 = -2.0f;
        if (this.module.compassShowDirection.getBoolean()) {
            MoonsenseClient.titleRenderer.drawCenteredString(String.format("%d", (int)directionFacing), x + halfWidth + 0.5f, y, 805306368);
            MoonsenseClient.titleRenderer.drawCenteredString(String.format("%d", (int)directionFacing), x + halfWidth, y, this.module.compassDirectionColor.getColor());
        }
        if (this.module.compassShowEdge.getBoolean()) {
            GuiUtils.drawTorus((int)x + this.getWidth() / 2, (int)y + this.getWidth() / 2 + 9, (float)(this.module.compassSize.getInt() / 2 - 3), (float)(this.module.compassSize.getInt() / 2 - 2), this.module.compassEdgeColor.getColor(), true);
        }
        for (int angle = 0; angle <= 360; angle += 15) {
            this.renderAngle(x, y, angle, directionFacing, halfWidth);
        }
        if (this.module.compassShowMagnet.getBoolean()) {
            final int magnetSize = Math.min(this.getWidth() / 6, 20);
            GuiUtils.drawDownwardsTriangle((float)((int)x + this.getWidth() / 2), (float)((int)y + this.getWidth() / 2 + 9), (float)(-magnetSize), SCModule.getColor(this.module.compassMagnetNorthColor.getColorObject()));
            GuiUtils.drawDownwardsTriangle((float)((int)x + this.getWidth() / 2), (float)((int)y + this.getWidth() / 2 + 9), (float)magnetSize, SCModule.getColor(this.module.compassMagnetSouthColor.getColorObject()));
        }
        final Iterator<Waypoint> waypointIterator = MoonsenseClient.INSTANCE.getWaypointManager().iterator();
        if (this.module.compassShowWaypoints.getBoolean()) {
            while (waypointIterator.hasNext()) {
                final Waypoint waypoint = waypointIterator.next();
                if (waypoint.isVisible() && waypoint.canRenderWaypoint() && ModuleConfig.INSTANCE.isEnabled(WaypointsModule.INSTANCE)) {
                    this.renderWaypointMarker(player, x, y, waypoint, directionFacing);
                }
            }
        }
        if (this.module.compassShowPlayers.getBoolean()) {
            for (final Object o : this.mc.theWorld.loadedEntityList) {
                if (o instanceof EntityPlayer && !o.equals(player)) {
                    this.renderPlayerHead(player, x, y, (EntityPlayer)o, directionFacing);
                }
            }
        }
    }
    
    private void renderPlayerHead(final EntityPlayerSP player, final float x, final float y, final EntityPlayer entity, final float directionFacing) {
        final float xDiff = (float)(player.posX - entity.posX);
        final float yDiff = (float)(player.posZ - entity.posZ);
        final float waypointHeading = (float)(Math.atan2(yDiff, xDiff) * 180.0 / 3.141592653589793);
        final float trigX = (float)Math.cos((waypointHeading - directionFacing) / 57.3);
        final float trigY = (float)Math.sin((waypointHeading - directionFacing) / 57.3);
        final float circleX = trigX * (this.module.compassSize.getInt() / 2 - 1.5f);
        final float circleY = trigY * (this.module.compassSize.getInt() / 2 - 1.5f);
        final float w = (float)(this.getWidth() / 2);
        final float h = (float)(this.getHeight() / 2);
        this.mc.getTextureManager().bindTexture(GuiUtils.getHeadLocation(entity.getGameProfile().getName()));
        GuiUtils.setGlColor(-1);
        Gui.drawScaledCustomSizeModalRect((int)(x + circleX + w) - 4, (int)(y + circleY + h + 4.5f) - 4, 0.0f, 0.0f, 8, 8, 8, 8, 8.0f, 8.0f);
    }
    
    private void renderWaypointMarker(final EntityPlayerSP player, final float x, final float y, final Waypoint waypoint, final float directionFacing) {
        final float xDiff = (float)(player.posX - waypoint.getLocation().xCoord);
        final float yDiff = (float)(player.posZ - waypoint.getLocation().zCoord);
        final float waypointHeading = (float)(Math.atan2(yDiff, xDiff) * 180.0 / 3.141592653589793);
        final float trigX = (float)Math.cos((waypointHeading - directionFacing) / 57.3);
        final float trigY = (float)Math.sin((waypointHeading - directionFacing) / 57.3);
        final float circleX = trigX * (this.module.compassSize.getInt() / 2 - 1.5f);
        final float circleY = trigY * (this.module.compassSize.getInt() / 2 - 1.5f);
        final float w = (float)(this.getWidth() / 2);
        final float h = (float)(this.getHeight() / 2);
        GuiUtils.drawTorus(x + circleX + w, y + circleY + h + 4.5f, 0.0f, 2.0f, waypoint.getColor().getColor(), true);
    }
    
    private void renderAngle(final float x, final float y, final int angleToRender, final float facing, final float halfWidth) {
        final int var8 = angleToRender % 90;
        final int var9 = angleToRender % 45;
        final float w = halfWidth;
        final float h = (float)(this.getHeight() / 2);
        final float trigX = (float)Math.cos((angleToRender - facing) / 57.3);
        final float trigY = (float)Math.sin((angleToRender - facing) / 57.3);
        final float circleX = trigX * (this.module.compassSize.getInt() / 2 - 5);
        final float circleY = trigY * (this.module.compassSize.getInt() / 2 - 5);
        if (var8 == 0) {
            if (this.module.compassShowMainMarkings.getBoolean()) {
                GuiUtils.drawTorus(x + circleX + w, y + circleY + h + 4.5f, 0.0f, 3.0f, this.module.compassMainMarkingColor.getColor(), true);
                switch (angleToRender) {
                    case 0: {
                        MoonsenseClient.titleRenderer.drawCenteredString("W", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                    case 90: {
                        MoonsenseClient.titleRenderer.drawCenteredString("N", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                    case 180: {
                        MoonsenseClient.titleRenderer.drawCenteredString("E", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                    case 270: {
                        MoonsenseClient.titleRenderer.drawCenteredString("S", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                }
            }
        }
        else if (var9 == 0) {
            if (this.module.compassShowSecondaryMarkings.getBoolean()) {
                GuiUtils.drawTorus(x + circleX + w, y + circleY + h + 4.5f, 0.0f, 2.0f, this.module.compassSecondaryMarkingColor.getColor(), true);
                switch (angleToRender) {
                    case 45: {
                        MoonsenseClient.tinyTextRenderer.drawCenteredString("NW", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                    case 135: {
                        MoonsenseClient.tinyTextRenderer.drawCenteredString("NE", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                    case 225: {
                        MoonsenseClient.tinyTextRenderer.drawCenteredString("SE", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                    case 315: {
                        MoonsenseClient.tinyTextRenderer.drawCenteredString("SW", x + w + circleX - 8.0f * trigX, y + h + circleY - 8.0f * trigY, this.module.compassMainMarkingColor.getColor());
                        break;
                    }
                }
            }
        }
        else if (this.module.compassShowTertiaryMarkings.getBoolean()) {
            GuiUtils.drawTorus(x + circleX + w, y + circleY + h + 4.5f, 0.0f, 1.0f, this.module.compassTertiaryMarkingColor.getColor(), true);
        }
    }
    
    @Override
    public AnchorPoint getDefaultPosition() {
        return AnchorPoint.TOP_LEFT;
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.HUD;
    }
    
    public float normalizePlayerFacing(final int angle) {
        int var2 = angle;
        if (angle < 0) {
            while (var2 < 0) {
                var2 += 360;
            }
        }
        else if (angle > 360) {
            while (var2 > 360) {
                var2 -= 360;
            }
        }
        return (float)var2;
    }
}
