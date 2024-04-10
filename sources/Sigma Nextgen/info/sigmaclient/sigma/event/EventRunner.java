package info.sigmaclient.sigma.event;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.*;
import info.sigmaclient.sigma.event.render.*;
import info.sigmaclient.sigma.modules.Module;

public class EventRunner {
    public static void doEvent(Module module, Event event){
        switch (event.eventID){
            case 1:
                module.onRenderEvent((RenderEvent) event);
                break;
            case 2:
                module.onKeyEvent((KeyEvent) event);
                break;
            case 3:
                module.onClickEvent((ClickEvent) event);
                break;
            case 4:
                module.onRenderShaderEvent((RenderShaderEvent) event);
                break;
            case 5:
                module.onPacketEvent((PacketEvent) event);
                break;
            case 6:
                module.onWorldEvent((WorldEvent) event);
                break;
            case 7:
                module.onRender3DEvent((Render3DEvent) event);
                break;
            case 8:
                module.onMouseClickEvent((MouseClickEvent) event);
                break;
            case 9:
                module.onStepEvent((StepEvent) event);
                break;
            case 10:
                module.onRenderModelEvent((RenderModelEvent) event);
                break;
            case 11:
                module.onAttackEvent((AttackEvent) event);
                break;
            case 12:
                module.onWindowUpdateEvent((WindowUpdateEvent) event);
                break;
            case 13:
                module.onBlockColEvent((BlockColEvent) event);
                break;
            case 14:
                module.onRenderChestEvent((RenderChestEvent) event);
                break;
            case 15:
                module.onMoveEvent((MoveEvent) event);
                break;
            case 16:
                module.onUpdateEvent((UpdateEvent) event);
                break;
        }
    }
}
