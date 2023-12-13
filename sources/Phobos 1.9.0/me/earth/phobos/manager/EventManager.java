package me.earth.phobos.manager;

import com.google.common.base.Strings;
import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.*;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.client.Managers;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.modules.combat.AutoCrystal;
import me.earth.phobos.util.GLUProjection;
import me.earth.phobos.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager
        extends Feature {
    private final Timer timer = new Timer();
    private final Timer logoutTimer = new Timer();
    private final Timer switchTimer = new Timer();
    private boolean keyTimeout;
    private final AtomicBoolean tickOngoing = new AtomicBoolean(false);

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!EventManager.fullNullCheck() && event.getEntity().getEntityWorld().isRemote && event.getEntityLiving().equals(EventManager.mc.player)) {
            Phobos.potionManager.update();
            Phobos.totemPopManager.onUpdate();
            Phobos.inventoryManager.update();
            Phobos.holeManager.update();
            Phobos.safetyManager.onUpdate();
            Phobos.moduleManager.onUpdate();
            Phobos.timerManager.update();
            if (this.timer.passedMs(Managers.getInstance().moduleListUpdates.getValue().intValue())) {
                Phobos.moduleManager.sortModules(true);
                Phobos.moduleManager.alphabeticallySortModules();
                this.timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && mc.getConnection() != null && ServerModule.getInstance().isConnected() && EventManager.mc.world != null) {
            String command = "@Server" + ServerModule.getInstance().getServerPrefix() + "module " + event.getSetting().getFeature().getName() + " set " + event.getSetting().getName() + " " + event.getSetting().getPlannedValue().toString();
            CPacketChatMessage cPacketChatMessage = new CPacketChatMessage(command);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTickHighest(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.tickOngoing.set(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTickLowest(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tickOngoing.set(false);
            AutoCrystal.getInstance().postTick();
        }
    }

    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        Phobos.moduleManager.onLogin();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Phobos.moduleManager.onLogout();
        Phobos.totemPopManager.onLogout();
        Phobos.potionManager.onLogout();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        Phobos.moduleManager.onTick();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            Phobos.baritoneManager.onUpdateWalkingPlayer();
            Phobos.speedManager.updateValues();
            Phobos.rotationManager.updateRotations();
            Phobos.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            Phobos.rotationManager.restoreRotations();
            Phobos.positionManager.restorePosition();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.switchTimer.reset();
        }
    }

    public boolean isOnSwitchCoolDown() {
        return !this.switchTimer.passedMs(500L);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }
        Phobos.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(EventManager.mc.world) instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) packet.getEntity(EventManager.mc.world);
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player));
                Phobos.totemPopManager.onTotemPop(player);
                Phobos.potionManager.onTotemPop(player);
            }
        } else if (event.getPacket() instanceof SPacketPlayerListItem && !EventManager.fullNullCheck() && this.logoutTimer.passedS(1.0)) {
            SPacketPlayerListItem packet = event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction())) {
                return;
            }
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null).forEach(data -> {
                UUID id = data.getProfile().getId();
                switch (packet.getAction()) {
                    case ADD_PLAYER: {
                        String name = data.getProfile().getName();
                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = EventManager.mc.world.getPlayerEntityByUUID(id);
                        if (entity != null) {
                            String logoutName = entity.getName();
                            MinecraftForge.EVENT_BUS.post(new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        MinecraftForge.EVENT_BUS.post(new ConnectionEvent(2, id, null));
                    }
                }
            });
        } else if (event.getPacket() instanceof SPacketTimeUpdate) {
            Phobos.serverManager.update();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.profiler.startSection("phobos");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        GLUProjection projection = GLUProjection.getInstance();
        IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projectionPort);
        GL11.glGetInteger(2978, viewPort);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        projection.updateMatrices(viewPort, modelView, projectionPort, (double) scaledResolution.getScaledWidth() / (double) Minecraft.getMinecraft().displayWidth, (double) scaledResolution.getScaledHeight() / (double) Minecraft.getMinecraft().displayHeight);
        Phobos.moduleManager.onRender3D(render3dEvent);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        EventManager.mc.profiler.endSection();
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Phobos.textManager.updateResolution();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            Phobos.moduleManager.onRender2D(render2DEvent);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    Phobos.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage("\u00a7cAn error occurred while running this command. Check the log!");
            }
            event.setMessage("");
        }
    }
}

