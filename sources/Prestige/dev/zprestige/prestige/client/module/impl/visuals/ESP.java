package dev.zprestige.prestige.client.module.impl.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zprestige.prestige.api.mixin.IWorld;
import dev.zprestige.prestige.api.mixin.IWorldRenderer;
import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.FrustrumEvent;
import dev.zprestige.prestige.client.event.impl.PacketReceiveEvent;
import dev.zprestige.prestige.client.event.impl.Render3DEvent;
import dev.zprestige.prestige.client.managers.AntiBotManager;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.ColorSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.setting.impl.MultipleSetting;
import dev.zprestige.prestige.client.ui.font.FontRenderer;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.render.BlockBreakingInfo;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ESP extends Module {

    public MultipleSetting tracers;
    public BooleanSetting players;
    public BooleanSetting drops;
    public BooleanSetting brokenBlocks;
    public IntSetting radius;
    public BooleanSetting pearlTrajectories;
    public BooleanSetting storages;
    public BooleanSetting donutBypass;
    public ColorSetting chests;
    public ColorSetting enderChests;
    public ColorSetting enchantingTable;
    public ColorSetting beacon;
    public ColorSetting spawners;
    public ColorSetting others;
    public HashMap<Entity, Map<Long, Vec3d>> entities;
    public Frustum frustrum;

    public ESP() {
        super("ESP", Category.Visual, "Renders various things through walls");
        tracers = setting("Tracers", new String[]{"Drops", "Broken Blocks", "Pearl Trajectories", "Storages", "Players"}, new Boolean[]{false, false, false, false, false}).description("Renders tracers to various things");
        players = setting("Players", false).description("Renders players");
        drops = setting("Drops", false).description("Renders dropped items");
        brokenBlocks = setting("Broken Blocks", false).description("Renders broken blocks");
        radius = setting("Radius", 10, 1, 20).invokeVisibility(arg_0 -> brokenBlocks.getObject()).description("Radius to render broken blocks at");
        pearlTrajectories = setting("Pearl Trajectories", false).description("Renders pearl trajectories");
        storages = setting("Storages", false).description("Renders storages");
        donutBypass = setting("Donut SMP Bypass", false).invokeVisibility(arg_0 -> storages.getObject()).description("Bypasses Donut SMP");
        chests = setting("Chests", new Color(150, 75, 0, 150)).invokeVisibility(arg_0 -> storages.getObject()).description("Color for chests");
        enderChests = setting("Ender Chests", new Color(50, 72, 72, 150)).invokeVisibility(arg_0 -> storages.getObject()).description("Color for ender chests");
        enchantingTable = setting("Enchanting Table", new Color(36, 111, 255, 150)).invokeVisibility(arg_0 -> storages.getObject()).description("Color for enchanting tables");
        beacon = setting("Beacon", new Color(156, 240, 255, 150)).invokeVisibility(arg_0 -> storages.getObject()).description("Color for beacons");
        spawners = setting("Spawners", new Color(24, 84, 24, 150)).invokeVisibility(arg_0 -> storages.getObject()).description("Color for spawners");
        others = setting("Others", new Color(255, 255, 255, 150)).invokeVisibility(arg_0 -> storages.getObject()).description("Color for other storages");
        entities = new HashMap();
    }

    @EventListener
    public void event(Render3DEvent event) {
        MatrixStack matrixStack = event.getMatrixStack();
        RenderHelper.setMatrixStack(matrixStack);
        Prestige.Companion.getFontManager().setMatrixStack(matrixStack);
        Vec3d vec3d = this.getRenderPosition();
        if (players.getObject()) {
            renderPlayers(vec3d);
        }
        if (drops.getObject()) {
            renderItems(vec3d);
        }
        if (brokenBlocks.getObject()) {
            renderBrokenBlocks(matrixStack, vec3d);
        }
        if (pearlTrajectories.getObject()) {
            renderPearlTrajectories(vec3d);
        }
        if (storages.getObject()) {
            renderStorages(vec3d);
        }
    }

    @EventListener
    public void event(FrustrumEvent event) {
        frustrum = event.getFrustrum();
    }

    @EventListener
    public void event(PacketReceiveEvent event) {
        if (donutBypass.getObject() && event.getPacket() instanceof ChunkDeltaUpdateS2CPacket) {
            event.setCancelled();
        }
    }

    private void renderPlayers(Vec3d vec3d) {
        for (PlayerEntity player : getMc().world.getPlayers()) {
            AntiBotManager antiBotManager = Prestige.Companion.getAntiBotManager();
            if (antiBotManager.isNotBot(player)) {
                if (this.frustrum != null) {
                    if (!frustrum.isVisible(player.getVisibilityBoundingBox())) continue;
                }
                String string = player.getEntityName();
                Color color;
                if (Prestige.Companion.getSocialsManager().isFriend(string)) {
                    color = new Color(0, 255, 255);
                } else {
                    Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
                    color = Prestige.Companion.getSocialsManager().isEnemy(player.getEntityName()) ? new Color(255, 0, 0) : new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 100);
                }
                Vec3d pos = RenderUtil.getEntityPos(player);
                RenderUtil.setCameraAction();
                RenderUtil.renderFilledBox((float) pos.x - player.getWidth() / 2, (float) pos.y, (float) pos.z - player.getWidth() / 2, (float) pos.x + player.getWidth() / 2, (float) pos.y + player.getHeight(), (float) pos.z + player.getWidth() / 2, RenderUtil.getColor(color, 0.5f));
                RenderUtil.renderOutlinedBox((float) pos.x - player.getWidth() / 2, (float) pos.y, (float) pos.z - player.getWidth() / 2, (float) pos.x + player.getWidth() / 2, (float) pos.y + player.getHeight(), (float) pos.z + player.getWidth() / 2, color);
                MatrixStack matrixStack = RenderHelper.getMatrixStack();
                matrixStack.pop();
                if (tracers.getValue("Players")) {
                    ArrayList<Vec3d> arrayList = new ArrayList<>();
                    arrayList.add(vec3d);
                    arrayList.add(player.getPos());
                    RenderUtil.setCameraAction();
                    RenderUtil.renderLines(arrayList, color);
                    matrixStack.pop();
                }
            }
        }
    }

    private void renderItems(Vec3d vec3d) {
        RenderUtil.setCameraAction();
        for (Entity entity : getMc().world.getEntities()) {
            if (entity instanceof ItemEntity itemEntity) {
                Item item = itemEntity.getStack().getItem();
                Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
                Color color = item == Items.TOTEM_OF_UNDYING ? new Color(255, 255, 0, 100) : item == Items.ENDER_PEARL ? new Color(45, 90, 60, 100) : (item instanceof ArmorItem ? new Color(63, 63, 63, 100) : new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 100));
                Vec3d itemPos = RenderUtil.getEntityPos(entity);
                float f = entity.getWidth() / 2;
                RenderUtil.renderFilledBox((float) itemPos.x - f, (float) itemPos.y - f, (float) itemPos.z - f, (float) itemPos.x + f, (float) itemPos.y + f, (float) itemPos.z + f, color);
                RenderUtil.renderOutlinedBox((float) itemPos.x - f, (float) itemPos.y - f, (float) itemPos.z - f, (float) itemPos.x + f, (float) itemPos.y + f, (float) itemPos.z + f, RenderUtil.getColor(color, 1));
                if (tracers.getValue("Drops")) {
                    ArrayList<Vec3d> arrayList = new ArrayList<>();
                    arrayList.add(vec3d);
                    arrayList.add(itemEntity.getPos());
                    RenderUtil.renderLines(arrayList, RenderUtil.getColor(color, 1));
                }
            }
        }
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.pop();
    }

    private void renderBrokenBlocks(MatrixStack matrixStack, Vec3d vec3d) {
        Quaternionf quaternionf = getMc().getEntityRenderDispatcher().getRotation();
        if (quaternionf != null) {
            Int2ObjectMap<BlockBreakingInfo> int2ObjectMap = ((IWorldRenderer)getMc().worldRenderer).getBlockBreakingInfos();
            RenderUtil.setCameraAction();
            for (Map.Entry<Integer, BlockBreakingInfo> entry : int2ObjectMap.entrySet()) {
                BlockPos blockPos = entry.getValue().getPos();
                if (Math.sqrt(getMc().player.squaredDistanceTo(blockPos.toCenterPos())) < radius.getObject()) {
                    Color themeColor = RenderUtil.getColor(Prestige.Companion.getModuleManager().getMenu().getColor().getObject(), 0.5f);
                    Color color = new Color(themeColor.getRed(), themeColor.getGreen(), themeColor.getBlue(), 50);
                    float f = (float) entry.getValue().getStage() / 8;
                    if (f > 0.0f) {
                        RenderUtil.renderFilledBox(blockPos.getX() + 0.5f - 0.5f * f, blockPos.getY() + 0.5f - 0.5f * f, blockPos.getZ() + 0.5f - 0.5f * f, blockPos.getX() + 0.5f + 0.5f * f, blockPos.getY() + 0.5f + 0.5f * f, blockPos.getZ() + 0.5f + 0.5f * f, color);
                        RenderUtil.renderOutlinedBox(blockPos.getX() + 0.5f - 0.5f * f, blockPos.getY() + 0.5f - 0.5f * f, blockPos.getZ() + 0.5f - 0.5f * f, blockPos.getX() + 0.5f + 0.5f * f, blockPos.getY() + 0.5f + 0.5f * f, blockPos.getZ() + 0.5f + 0.5f * f, themeColor);
                        if (tracers.getValue("Broken Blocks")) {
                            ArrayList<Vec3d> arrayList = new ArrayList<>();
                            arrayList.add(vec3d);
                            arrayList.add(blockPos.toCenterPos());
                            RenderUtil.renderLines(arrayList, themeColor);
                        }
                        matrixStack.push();
                        matrixStack.translate(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
                        matrixStack.multiply(quaternionf);
                        matrixStack.scale(-1, -1, 1);
                        RenderSystem.disableDepthTest();
                        matrixStack.scale(0.015f, 0.015f, 0.015f);
                        Entity entity = getMc().world.getEntityById(entry.getValue().getActorId());
                        if (entity != null) {
                            FontRenderer font = Prestige.Companion.getFontManager().getFontRenderer();
                            String string = entity.getEntityName() + " " + (int) (f * 100) + "%";
                            font.drawString(entity.getEntityName(), -font.getStringWidth(string) / 2, -font.getStringHeight() / 2, themeColor);
                            String string2 = (int) (f * 100) + "%";
                            font.drawString(string2, -font.getStringWidth(string) / 2 + font.getStringWidth(entity.getEntityName()) / 2 - font.getStringWidth(string2) / 2, font.getStringHeight() / 2, themeColor);
                        }
                        RenderSystem.enableDepthTest();
                        matrixStack.pop();
                    }
                }
            }
            matrixStack.pop();
        }
    }

    private void renderPearlTrajectories(Vec3d vec3d) {
        for (Entity entity : getMc().world.getEntities()) {
            if (entity instanceof EnderPearlEntity) {
                if (!entities.containsKey(entity)) {
                    entities.put(entity, new HashMap());
                }
                entities.get(entity).put(System.currentTimeMillis(), entity.getPos());
            }
        }
        for (Map.Entry<Entity, Map<Long, Vec3d>> entry : entities.entrySet()) {
            for (Map.Entry<Long, Vec3d> entry2 : entry.getValue().entrySet()) {
                if (System.currentTimeMillis() - entry2.getKey() > 2000L) {
                    entities.get(entry.getKey()).remove(entry2.getKey());
                }
                Color themeColor = Prestige.Companion.getModuleManager().getMenu().getColor().getObject();
                RenderUtil.setCameraAction();
                RenderUtil.renderLines(new ArrayList<>(entry.getValue().values()), themeColor);
                if (tracers.getValue("Pearl Trajectories")) {
                    ArrayList<Vec3d> arrayList = new ArrayList<>();
                    arrayList.add(vec3d);
                    arrayList.add(entry.getKey().getPos());
                    RenderUtil.renderLines(arrayList, themeColor);
                }
                if (!entry.getKey().isAlive()) {
                    if (entry.getValue().isEmpty()) {
                        entities.remove(entry.getKey());
                    }
                    for (Map.Entry<Long, Vec3d> entry3 : new HashMap<>(entry.getValue()).entrySet()) {
                        if (System.currentTimeMillis() - entry3.getKey() > 2000L) {
                            entities.get(entry.getKey()).remove(entry3.getKey());
                        }
                    }
                }
            }
        }
    }

    private void renderStorages(Vec3d vec3d) {
        RenderUtil.setCameraAction();
        for (BlockEntityTickInvoker blockEntityTickInvoker : ((IWorld)getMc().world).getBlockEntityTickers()) {
            BlockPos blockPos = blockEntityTickInvoker.getPos();
            Color color = getColor(blockEntityTickInvoker.getName().replace("minecraft:", ""));
            if (color == null) {
                color = others.getObject();
            }
            if (blockEntityTickInvoker.getPos() != null) {
                RenderUtil.renderFilledBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1, color);
                RenderUtil.renderOutlinedBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1, color);
                if (tracers.getValue("Storages")) {
                    ArrayList<Vec3d> arrayList = new ArrayList<>();
                    arrayList.add(vec3d);
                    arrayList.add(blockPos.toCenterPos());
                    RenderUtil.renderLines(arrayList, RenderUtil.getColor(color, 1));
                }
            }
        }
        MatrixStack matrixStack = RenderHelper.getMatrixStack();
        matrixStack.pop();
    }

    private Vec3d getRenderPosition() {
        Vector3f vector3f = new Vector3f(0, 0, 1);
        if (getMc().options.getBobView().getValue()) {
            MatrixStack matrixStack = new MatrixStack();
            setMatrix(matrixStack);
            vector3f.mulPosition(matrixStack.peek().getPositionMatrix().invert());
        }
        return new Vec3d(vector3f.x, -vector3f.y, vector3f.z).rotateX(-(float)Math.toRadians(getMc().gameRenderer.getCamera().getPitch())).rotateY(-(float)Math.toRadians(getMc().gameRenderer.getCamera().getYaw())).add(getMc().gameRenderer.getCamera().getPos());
    }

    private void setMatrix(MatrixStack matrixStack) {
        Entity entity = getMc().cameraEntity;
        if (entity instanceof PlayerEntity player) {
            float f = getMc().getTickDelta();
            float f2 = entity.horizontalSpeed - entity.prevHorizontalSpeed;
            float f3 = -(entity.horizontalSpeed + f2 * f);
            float f4 = MathHelper.lerp(f, player.prevStrideDistance, player.strideDistance);
            matrixStack.translate(-((MathHelper.sin(f3 * (float)Math.PI) * f4) * 0.5), Math.abs(MathHelper.cos(f3 * (float)Math.PI) * f4), 0.0);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(f3 * (float)Math.PI) * f4 * 3));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.abs(MathHelper.cos(f3 * (float)Math.PI - 0.2f) * f4) * 5));
        }
    }

    private Color getColor(String string) {
        switch (string) {
            case "chest": {
                return chests.getObject();
            }
            case "beacon": {
                return beacon.getObject();
            }
            case "enchanting_table": {
                return enchantingTable.getObject();
            }
            case "ender_chest": {
                return enderChests.getObject();
            }
            case "mob_spawner": {
                return spawners.getObject();
            }
        }
        return null;
    }
}
