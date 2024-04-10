package info.sigmaclient.sigma.modules;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.*;
import info.sigmaclient.sigma.event.render.*;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class EventBase {
    public void onMoveEvent(MoveEvent event) {}
    public void onUpdateEvent(UpdateEvent event) {}
    public void onRenderEvent(RenderEvent event) {}
    public void onKeyEvent(KeyEvent event) {}
    public void onClickEvent(ClickEvent event) {}
    public void onRenderShaderEvent(RenderShaderEvent event) {}
    public void onPacketEvent(PacketEvent event) {}
    public void onWorldEvent(WorldEvent event) {}
    public void onRender3DEvent(Render3DEvent event) {}
    public void onMouseClickEvent(MouseClickEvent event) {}
    public void onStepEvent(StepEvent event) {}
    public void onRenderModelEvent(RenderModelEvent event) {}
    public void onAttackEvent(AttackEvent event) {}
    public void onWindowUpdateEvent(WindowUpdateEvent event) {}
    public void onBlockColEvent(BlockColEvent event) {}
    public void onRenderChestEvent(RenderChestEvent event) {}
}
