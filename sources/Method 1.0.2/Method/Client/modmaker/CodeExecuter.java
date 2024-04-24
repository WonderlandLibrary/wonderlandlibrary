package Method.Client.modmaker;


import Method.Client.clickgui.component.Component;
import Method.Client.modmaker.Windows.Blocks.Headers.OnPacketReceived;
import Method.Client.modmaker.Windows.Blocks.Headers.OnPacketSent;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;
import Method.Client.utils.Patcher.Events.*;
import Method.Client.utils.system.Connection;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("UnstableApiUsage")
public class CodeExecuter implements Serializable {
    public ArrayList<DragableBlock> HeaderBlocks = new ArrayList<>();
    public ArrayList<DragableBlock> PacketHeaderBlocks = new ArrayList<>();
    public Module module;

    public Object[] loopObj = new Object[10];
    public boolean breakloop;

    {
        Arrays.fill(loopObj, null);
    }

    public CodeExecuter(Module module) {
        this.module = module;
    }

    public void loadBlocks() {
        this.HeaderBlocks.clear();
        this.PacketHeaderBlocks.clear();
        for (DragableBlock activeBlock : module.ActiveBlocks) {
            if (activeBlock.thisblock != null) {
                if (activeBlock.thisblock.MainBlockTypeGiven != null) {
                    if (activeBlock.thisblock.MainBlockTypeGiven.equals(MainBlockType.Header))
                        this.HeaderBlocks.add(activeBlock);
                    if (activeBlock.thisblock.MainBlockTypeGiven.equals(MainBlockType.PacketHeader))
                        this.PacketHeaderBlocks.add(activeBlock);
                }
            }
        }
    }


    public double solveNumerical(DragableBlock insertableBubble, int index, Object event) {
        if (insertableBubble.inputBoxes.get(index).isFull) {
            return insertableBubble.inputBoxes.get(index).containedBlock.thisblock.runCodeSolve(insertableBubble.inputBoxes.get(index).containedBlock, event);
        }
        if (insertableBubble.inputBoxes.get(index).inputBox.getText().length() > 0) {
            return fastParseDouble(insertableBubble.inputBoxes.get(index).inputBox.getText());
        }
        return 0;
    }

    // create a fast String to double converter
    public static double fastParseDouble(String s) {
        int i = 0;
        int len = s.length();
        double d = 0;
        double p = 0.1;
        // if first char is a minus sign, set the sign
        boolean isNeg = (s.charAt(0) == '-');
        // if first char is a minus sign, skip it
        if (isNeg) i++;
        // process each char
        while (i < len && s.charAt(i) != '.') {
            d = d * 10 + (s.charAt(i++) - '0');
        }
        if (i < len) {
            i++;
        }

        while (i < len) {
            d = d + (s.charAt(i++) - '0') * p;
            p = p / 10;
        }
        // if the number is negative, negate it
        if (isNeg) d = -d;
        return d;
    }


    public boolean solveBoolean(DragableBlock insertableBubble, int index, Object event) {
        if (insertableBubble.inputBoxes.get(index).isFull) {
            return insertableBubble.inputBoxes.get(index).containedBlock.thisblock.runCodeBoolean(insertableBubble.inputBoxes.get(index).containedBlock, event);
        }
        if (insertableBubble.inputBoxes.get(index).inputBox.getText().length() > 0) {
            return Boolean.parseBoolean(insertableBubble.inputBoxes.get(index).inputBox.getText());
        }
        return false;
    }

    public Object solveObject(DragableBlock insertableBubble, int index, Object event) {
        if (insertableBubble.inputBoxes.get(index).isFull) {
            return insertableBubble.inputBoxes.get(index).containedBlock.thisblock.runCodeObject(insertableBubble.inputBoxes.get(index).containedBlock, event);
        }
        if (insertableBubble.inputBoxes.get(index).inputBox.getText().length() > 0) {
            return insertableBubble.inputBoxes.get(index).inputBox.getText();
        }
        return null;
    }

    public void headerRun(Headers headers, net.minecraftforge.fml.common.eventhandler.Event event) {
        for (DragableBlock activeBlock : HeaderBlocks) {
            if (activeBlock.thisblock.header.equals(headers)) {
                activeBlock.runCode(event);
            }
        }
    }

    public boolean onPacket(Object packet, Connection.Side side) {
        for (DragableBlock activeBlock : PacketHeaderBlocks) {
            if (side == Connection.Side.OUT && activeBlock.thisblock instanceof OnPacketSent) {
                activeBlock.runCode(packet);
                if (module.cancelPacket) {
                    module.cancelPacket = false;
                    return false;
                }
            }
            if (side == Connection.Side.IN && activeBlock.thisblock instanceof OnPacketReceived) {
                activeBlock.runCode(packet);
                if (module.cancelPacket) {
                    module.cancelPacket = false;
                    return false;
                }
            }

            if (activeBlock.thisblock.packetclasses.contains(packet.getClass())) {
                activeBlock.runCode(packet);
                if (module.cancelPacket) {
                    module.cancelPacket = false;
                    return false;
                }
            }
        }
        return true;
    }

    public boolean onDisablePacket(Object packet) {
        if (packet instanceof C00Handshake || packet instanceof CPacketResourcePackStatus || packet instanceof FMLProxyPacket|| packet instanceof CPacketCustomPayload) {
            for (DragableBlock activeBlock : PacketHeaderBlocks) {
                if (activeBlock.thisblock.packetclasses.contains(packet.getClass())) {
                    activeBlock.runCode(packet);
                }
            }

        }
        return true;
    }

    @Subscribe
    public void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        headerRun(Headers.PlayerRespawnEvent, event);
    }

    @Subscribe
    public void onClientTick(TickEvent.ClientTickEvent event) {
        headerRun(Headers.OnClientTick, event);
    }

    @Subscribe
    public void GuiScreenEventInit(GuiScreenEvent.InitGuiEvent.Post event) {
        headerRun(Headers.GuiScreenEventInit, event);
    }

    @Subscribe
    public void onWorldUnload(WorldEvent.Unload event) {
        headerRun(Headers.WorldEventunload, event);
    }

    @Subscribe
    public void GuiScreenEventPost(GuiScreenEvent.ActionPerformedEvent.Post event) {
        headerRun(Headers.GuiScreenEventPost, event);
    }


    @Subscribe
    public void GuiOpen(GuiOpenEvent event) {
        headerRun(Headers.GuiOpenEvent, event);
    }

    @Subscribe
    public void onWorldLoad(WorldEvent.Load event) {
        headerRun(Headers.onWorldLoad, event);
    }

    @Subscribe
    public void renderNamePlate(@SuppressWarnings("rawtypes") RenderLivingEvent.Specials.Pre event) {
        headerRun(Headers.RenderLivingEvent, event);
    }

    @Subscribe
    public void PlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        headerRun(Headers.PlayerLoggedInEvent, event);
    }


    @Subscribe
    public void onKeyInput(InputEvent.KeyInputEvent event) {
    }


    ////////////////////////
    ////////Normal Events
    ////    ////    ////    ////

    @Subscribe
    public void Chunkevent(ChunkEvent.Load event) {
        headerRun(Headers.LoadChunk, event);
    }

    @Subscribe
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        headerRun(Headers.RenderGameOverlayEventText, event);
    }

    @Subscribe
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        headerRun(Headers.CameraSetup, event);
    }

    @Subscribe
    public void BackgroundDrawnEvent(GuiScreenEvent.BackgroundDrawnEvent event) {
        headerRun(Headers.BackgroundDrawnEvent, event);
    }

    @Subscribe
    public void onRenderPre(RenderGameOverlayEvent.Pre event) {
        headerRun(Headers.onRenderPre, event);
    }


    // THIS NEEDS WORK!!!
    @Subscribe
    public void ChatReceived(ClientChatReceivedEvent event) {
        headerRun(Headers.ClientChatReceivedEvent, event);
    }

    @Subscribe
    public void GetCollisionBoxesEvent(GetCollisionBoxesEvent event) {
        headerRun(Headers.GetCollisionBoxesEvent, event);
    }


    // COME BACK TO MEEEEE
    @Subscribe
    public void DrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        headerRun(Headers.DrawBlockHighlightEvent, event);
    }

    // ONCE AGAIN!!!
    @Subscribe
    public void ClientChatEvent(ClientChatEvent event) {
        headerRun(Headers.ClientChatEvent, event);
    }

    // MORE BACK TO THIS!!!!
    @Subscribe
    public void onLivingDeath(LivingDeathEvent event) {
        headerRun(Headers.LivingDeathEvent, event);
    }

    @Subscribe
    public void RenderBlockOverlayEvent(RenderBlockOverlayEvent event) {
        headerRun(Headers.RenderBlockOverlayEvent, event);
    }

    // COME BACK AGAIN!!!
    @Subscribe
    public void fogColor(EntityViewRenderEvent.FogColors event) {
        headerRun(Headers.FogColors, event);
    }

    @Subscribe
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        headerRun(Headers.FogDensity, event);
    }

    @Subscribe
    public void onTooltip(ItemTooltipEvent event) {
        headerRun(Headers.ItemTooltipEvent, event);
    }

    @Subscribe
    public void BedSleep(PlayerSleepInBedEvent event) {
        headerRun(Headers.PlayerSleepInBedEvent, event);
    }

    @Subscribe
    public void onItemPickup(EntityItemPickupEvent event) {
        headerRun(Headers.EntityItemPickupEvent, event);
    }

    @Subscribe
    public void onProjectileImpact(ProjectileImpactEvent event) {
        headerRun(Headers.ProjectileImpactEvent, event);
    }


    @Subscribe
    public void onAttackEntity(AttackEntityEvent event) {
        headerRun(Headers.AttackEntityEvent, event);
    }

    @Subscribe
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        headerRun(Headers.PlayerTickEvent, event);
    }

    @Subscribe
    public void Chunkevent(ChunkEvent.Unload event) {
        headerRun(Headers.UnLoadChunk, event);
    }

    @Subscribe
    public void LivingHurtEvent(LivingHurtEvent event) {
        headerRun(Headers.LivingHurt, event);
    }

    @Subscribe
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        headerRun(Headers.LivingUpdateEvent, event);
    }

    @Subscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        headerRun(Headers.RenderWorldLastEvent, event);
    }

    @Subscribe
    public void FOVModifier(EntityViewRenderEvent.FOVModifier event) {
        headerRun(Headers.FOVModifier, event);
    }

    @Subscribe
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        headerRun(Headers.LeftClickBlock, event);
    }

    @Subscribe
    public void WorldEvent(WorldEvent event) {
        headerRun(Headers.WorldEvent, event);
    }

    @Subscribe
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        headerRun(Headers.onRightClickBlock, event);
    }

    @Subscribe
    public void postBackgroundTooltipRender(RenderTooltipEvent.PostBackground event) {
        headerRun(Headers.PostBackground, event);
    }


    // COME BACK TO ME
    @Subscribe
    public void RendertooltipPre(RenderTooltipEvent.Pre event) {
        headerRun(Headers.RenderTooltipEvent, event);
    }

    @SubscribeEvent
    public void RenderTickEvent(TickEvent.RenderTickEvent event) {
        headerRun(Headers.RenderTickEvent, event);
    }


    @Subscribe
    public void postDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        headerRun(Headers.postDrawScreen, event);
    }


    /////////
    /// Custom Events
    ///////


    // Implemented from EntityPlayerSpPatch
    @Subscribe
    public void PreMotionEvent(PreMotionEvent event) {
        headerRun(Headers.PreMotionEvent, event);
    }

    // Implemented from EntityPlayerSpPatch
    @Subscribe
    public void PostMotionEvent(PostMotionEvent event) {
        headerRun(Headers.PostMotionEvent, event);
    }

    // Implemented from EntityPlayerSpPatch
    @Subscribe
    public void onPlayerMove(PlayerMoveEvent event) {
        headerRun(Headers.PlayerMoveEvent, event);
    }

    // Implemented from EntityPlayerPatch
    @Subscribe
    public void onPlayerJump(EntityPlayerJumpEvent event) {
        headerRun(Headers.EntityPlayerJumpEvent, event);
    }

    // Implemented from PlayerControllerMPPatch
    @Subscribe
    public void DamageBlock(PlayerDamageBlockEvent event) {
        headerRun(Headers.PlayerDamageBlockEvent, event);
    }

    @Subscribe
    public void onGetAmbientOcclusionLightValue(GetAmbientOcclusionLightValueEvent event) {
        headerRun(Headers.onGetAmbientOcclusionLightValue, event);
    }

    @Subscribe
    public void Liquidvisitor(EventCanCollide event) {
        headerRun(Headers.Liquidvisitor, event);
    }

    @Subscribe
    public void onRenderBlockModel(RenderBlockModelEvent event) {
        headerRun(Headers.onRenderBlockModel, event);
    }

    @Subscribe
    public void onRenderTileEntity(RenderTileEntityEvent event) {
        headerRun(Headers.onRenderTileEntity, event);
    }

    public void onToggle() {
        headerRun(Headers.OnToggle, null);
    }

    public void onEnable() {
        headerRun(Headers.OnEnable, null);
    }

    public void onDisable() {
        headerRun(Headers.OnDisable, null);
    }


    public void settingChanged(Component.ClickType visual) {
        for (DragableBlock activeBlock : HeaderBlocks) {
            if (activeBlock.thisblock.header.equals(Headers.SettingChanged)) {
                module.changedSetting = visual.name();
                activeBlock.runCode(null);
                module.changedSetting = null;
            }
        }
    }
}
