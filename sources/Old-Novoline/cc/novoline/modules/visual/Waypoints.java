package cc.novoline.modules.visual;

import cc.novoline.Novoline;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.Render2DEvent;
import cc.novoline.events.events.Render3DEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.utils.RotationUtil;
import cc.novoline.utils.ScaleUtils;
import cc.novoline.utils.Timer;
import cc.novoline.utils.fonts.api.FontRenderer;
import cc.novoline.utils.fonts.impl.Fonts;
import cc.novoline.utils.java.Checks;
import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.util.Objects.requireNonNull;
import static net.minecraft.client.gui.Gui.drawRect;
import static net.minecraft.client.renderer.GlStateManager.*;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static org.lwjgl.opengl.GL11.*;

public final class Waypoints extends AbstractModule {

    /* fields */
    private final List<Waypoint> waypoints = new ObjectArrayList<>();
    private static FontRenderer SF_16 = Fonts.SF.SF_16.SF_16;
    private static Timer tpTimer = new Timer();

    /* constructors */
    public Waypoints(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Waypoints", EnumModuleType.VISUALS, null, "waypoints");
        addWaypointsFromConfig();
    }

    /* methods */
    private void addWaypointsFromConfig() {
        try {
            this.waypoints.addAll(this.config.getRootNode().getNode("waypoints").getList(new TypeToken<List<Waypoint>>() {
            }, new ArrayList<>()));
        } catch (ObjectMappingException e) {
            throw new RuntimeException("An error occurred while deserialization of the waypoints file", e);
        }
    }

    @Override
    protected void addCustomSerializers(@NonNull TypeSerializerCollection collection) {
        collection.registerType(Serializer.TYPE_TOKEN, new Serializer());
    }

    /* events */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        this.waypoints.forEach(Waypoint::render);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        for (final Waypoint waypoint : this.waypoints) {
            final float x = (float) (waypoint.getX() + 0 * event.getPartialTicks() - this.mc.getRenderManager().renderPosX), // @off
                    y = (float) (waypoint.getY() + 0 * event.getPartialTicks() - this.mc.getRenderManager().renderPosY),
                    z = (float) (waypoint.getZ() + 0 * event.getPartialTicks() - this.mc
                            .getRenderManager().renderPosZ); // @on

            waypoint.setPositions(waypoint.convertTo2D(x, y, z));
        }
    }

    /* unimportant shit */
    public void addWaypoint(@NonNull Waypoint waypoint) {
        this.waypoints.add(waypoint);

        try {
            updateConfigNode();
        } catch (ObjectMappingException e) {
            this.logger.warn("An error occurred while saving friends list", e);
        }
    }

    public boolean removeWaypoint(String name) {
        Checks.notBlank(name, "name");

        final boolean b = this.waypoints.removeIf(w -> w.getName().equalsIgnoreCase(name));

        if (b) {
            try {
                updateConfigNode();
            } catch (ObjectMappingException e) {
                this.logger.warn("An error occurred while saving friends list", e);
            }
        }

        return b;
    }

    private void updateConfigNode() throws ObjectMappingException {
        this.config.getRootNode().getNode("waypoints").setValue(new TypeToken<List<Waypoint>>() {
        }, this.waypoints);
    }

    @Nullable
    public Waypoint getWaypointByName(String name) {
        return this.waypoints.stream().filter(waypoint -> waypoint.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    //region Lombok
    private final List<Waypoint> waypointsView = Collections.unmodifiableList(this.waypoints);

    public List<Waypoint> getWaypoints() {
        return this.waypointsView;
    }
    //endregion

    /**
     * @author xDelsy
     */
    public static final class Waypoint {

        private transient double[] positions = {0, 0, 0};

        private final String name;
        private final int x;
        private final int y;
        private final int z;

        public Waypoint(String name, int x, int y, int z) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public static Waypoints.Waypoint of(String name, int x, int y, int z) {
            Checks.notBlank(name, "name");
            return new Waypoint(name, x, y, z);
        }

        public void render() {
            final ScaledResolution resolution = new ScaledResolution(Minecraft.getInstance());

            if (isInView()) {
                glPushMatrix();
                glTranslated(this.positions[0] / resolution.getScaleFactor(),
                        this.positions[1] / resolution.getScaleFactor(),
                        this.positions[2] / resolution.getScaleFactor());
                ScaleUtils.scale(Minecraft.getInstance());

                float amp = 1;
                switch (Minecraft.getInstance().gameSettings.guiScale) {
                    case 0:
                        amp = 0.5F;
                        break;
                    case 1:
                        amp = 2.0F;
                        break;
                    case 3:
                        amp = 0.6666666666666667F;
                }

                float width = resolution.getScaledWidth() / 2F;
                float height = resolution.getScaledHeight() / 2F;

                float sizePerPixelX = SF_16.stringWidth(this.name + " " + getDistance() + "m") / 2F;
                float sizePerPixelY = 15 * 0.75F;
                float xBnd1 = width / amp + sizePerPixelX;
                float xBnd2 = width / amp - sizePerPixelX;
                float yBnd1 = height / amp - sizePerPixelY;
                float yBnd2 = height / amp + sizePerPixelY;


/*                if (Minecraft.getInstance().gameSettings.thirdPersonView == 0 && this.positions[0] >= xBnd2 * 2 && this.positions[0] <= xBnd1 * 2 &&
                        this.positions[1] >= yBnd1 * 2 && this.positions[1] <= yBnd2 * 2) {
                    SF_16.drawString("Middle click to teleport!", -(SF_16.stringWidth("Middle click to teleport") / 2F), -18, 0xffffffff, true);
                    if (Mouse.isButtonDown(2)) {
                        if (tpTimer.delay(1000)) {
                            String command = Minecraft.getInstance().isSingleplayer() ? "/tp" : ".tp";
                            String coords = Minecraft.getInstance().isSingleplayer() ? " " + x + " " + y + " " + z : " " + x + " " + z;
                            Minecraft.getInstance().player.sendChatMessage(command + coords);
                            tpTimer.reset();
                        }
                    }
                }*/

                translate(0.0D, -2.5D, 0.0D);
                disableDepth();
                drawRect(-(SF_16.stringWidth(this.name + " " + getDistance() + "m") / 2) - 2, -5,
                        SF_16.stringWidth(this.name + " " + getDistance() + "m") / 2 + 2, 10,
                        new Color(0, 0, 0, 100).getRGB());
                drawRect(-(SF_16.stringWidth(this.name + " " + getDistance() + "m") / 2) - 2, -6,
                        SF_16.stringWidth(this.name + " " + getDistance() + "m") / 2 + 2, -5,
                        requireNonNull(
                                Novoline.getInstance().getModuleManager().getModule(HUD.class).getColor())
                                .getRGB());
                SF_16.drawString(this.name + " " + GRAY + getDistance() + "m",
                        -(SF_16.stringWidth(this.name + " " + getDistance() + "m") / 2F), 0, 0xffffffff);
                enableDepth();
                glPopMatrix();
            }
        }

        public int getDistance() {
            final int diffX = (int) abs(Minecraft.getInstance().player.posX - this.getX()), // @off
                    diffY = (int) abs(Minecraft.getInstance().player.posY - this.getY()),
                    diffZ = (int) abs(Minecraft.getInstance().player.posZ - this.getZ()); // @on

            return (int) sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
        }

        public boolean isInView() {
            Minecraft mc = Minecraft.getInstance();
            final float angle = RotationUtil.getRotationFromPosition(this.getX(), this.getZ(), this.getY())[0], // @off
                    angle2 = RotationUtil.getRotationFromPosition(this.getX(), this.getZ(), this.getY())[1],
                    cameraYaw = (mc.gameSettings.thirdPersonView == 0 ? mc.getRenderViewEntity().rotationYaw : mc.getRenderViewEntity().cameraRotationYaw) + (float) (mc.gameSettings.thirdPersonView == 2 ? 180 : 0);

            return RotationUtil.getDistanceBetweenAngles(angle, RotationUtil.getNewAngle(cameraYaw)) < 90.0F
                    && RotationUtil
                    .getDistanceBetweenAngles(angle2, mc.gameSettings.thirdPersonView == 0 ? mc.player.rotationPitch : mc.player.cameraRotationPitch) < 90.0F; // @on
        }

        public double[] convertTo2D(double x, double y, double z) {
            final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3), // @off
                    modelView = BufferUtils.createFloatBuffer(16),
                    projection = BufferUtils.createFloatBuffer(16);
            final IntBuffer viewport = BufferUtils.createIntBuffer(16); // @on

            glGetFloat(2982, modelView);
            glGetFloat(2983, projection);
            glGetInteger(2978, viewport);

            final boolean result = GLU
                    .gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
            return result ?
                    new double[]{(double) screenCoords.get(0), (double) ((float) Display.getHeight() - screenCoords
                            .get(1)), (double) screenCoords.get(2)} :
                    null;
        }

        //region Lombok-alternative
        public void setPositions(double[] positions) {
            this.positions = positions;
        }

        public String getName() {
            return this.name;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getZ() {
            return this.z;
        }
        //endregion

    }

    public static class Serializer implements TypeSerializer<Waypoint> {

        public static final TypeToken<Waypoint> TYPE_TOKEN = TypeToken.of(Waypoint.class);

        @Override
        public Waypoint deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode node) {
            final String name = node.getNode("name").getString(); // @off
            final int x = node.getNode("x").getInt(),
                    y = node.getNode("y").getInt(),
                    z = node.getNode("z").getInt(); // @on

            return Waypoint.of(name, x, y, z);
        }

        @Override
        public void serialize(@NonNull TypeToken<?> type, Waypoint obj, @NonNull ConfigurationNode node) {
            if (obj == null) return;

            node.getNode("name").setValue(obj.getName());
            node.getNode("x").setValue(obj.getX());
            node.getNode("y").setValue(obj.getY());
            node.getNode("z").setValue(obj.getZ());
        }
    }

    public List<Waypoint> getWaypointsList() {
        return waypoints;
    }
}
