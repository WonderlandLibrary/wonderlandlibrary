package best.azura.scripting.event;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventClickMouse;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventSentPacket;
import best.azura.client.util.player.MovementUtil;
import best.azura.eventbus.core.Event;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Boolean;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;
import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.mcscript.utils.FunctionLambda;
import fr.ducouscous.mcscript.utils.PacketTables;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class EventTable extends Table {
    private NamedEvent event;
    public EventTable(final Event event) {
        if (!(event instanceof NamedEvent)) {
            Client.INSTANCE.getLogger().error("Event not instance of NamedEvent! Aborting!", new Exception());
            return;
        }
        final NamedEvent named = ((NamedEvent) event);
        this.event = named;
        setValue("name", new Variable(new StringVar(named.name())));
        switch (named.name()) {
            case "update":
            case "worldChange":
            case "fastTick":
                break;
            case "move":
                final EventMove eventMove = (EventMove) event;
                setValue("x", new Variable(new Number(eventMove.getX())));
                setValue("y", new Variable(new Number(eventMove.getY())));
                setValue("z", new Variable(new Number(eventMove.getZ())));
                setValue("setSpeed", new Variable(new FunctionLambda(variables -> {
                    MovementUtil.setSpeed(((Number)variables[0].getValue()).getValue(), eventMove);
                    setValue("x", new Variable(new Number(eventMove.getX())));
                    setValue("z", new Variable(new Number(eventMove.getZ())));
                    return null;
                })));
                break;
            case "motion":
                final EventMotion eventMotion = (EventMotion) event;
                setValue("isPre", new Variable(new FunctionLambda((args) -> new Variable(new Boolean(eventMotion.isPre())))));
                setValue("isUpdate", new Variable(new FunctionLambda((args) -> new Variable(new Boolean(eventMotion.isUpdate())))));
                setValue("isPost", new Variable(new FunctionLambda((args) -> new Variable(new Boolean(eventMotion.isPost())))));
                setValue("x", new Variable(new Number(eventMotion.x)));
                setValue("y", new Variable(new Number(eventMotion.y)));
                setValue("z", new Variable(new Number(eventMotion.z)));
                setValue("yaw", new Variable(new Number(eventMotion.yaw)));
                setValue("pitch", new Variable(new Number(eventMotion.pitch)));
                setValue("onGround", new Variable(new Boolean(eventMotion.onGround)));
                setValue("sneaking", new Variable(new Boolean(eventMotion.sneaking)));
                setValue("sprinting", new Variable(new Boolean(eventMotion.sprinting)));
                setValue("state", new Variable(new StringVar(eventMotion.getState())));
                break;
            case "clickMouse":
                final EventClickMouse eventClickMouse = (EventClickMouse) event;
                setValue("button", new Variable(new Number(eventClickMouse.getButton())));
                break;
            case "sentPacket":
                final EventSentPacket eventSentPacket = (EventSentPacket) event;
                switch (eventSentPacket.getPacket().getPacketName()) {
                    case "C03PacketPlayer":
                        setValue("packet", new Variable(new PacketTables.C03PacketPlayer(((C03PacketPlayer)eventSentPacket.getPacket()).onGround)));
                        break;
                    case "C04PacketPlayerPosition":
                        setValue("packet", new Variable(new PacketTables.C04PacketPlayerPosition(
                                ((C03PacketPlayer)eventSentPacket.getPacket()).x,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).y,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).z,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).onGround)));
                        break;
                    case "C05PacketPlayerLook":
                        setValue("packet", new Variable(new PacketTables.C05PacketPlayerLook(
                                ((C03PacketPlayer)eventSentPacket.getPacket()).yaw,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).pitch,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).onGround)));
                        break;
                    case "C06PacketPlayerPosLook":
                        setValue("packet", new Variable(new PacketTables.C06PacketPlayerPosLook(
                                ((C03PacketPlayer)eventSentPacket.getPacket()).x,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).y,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).z,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).yaw,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).pitch,
                                ((C03PacketPlayer)eventSentPacket.getPacket()).onGround)));
                        break;
                    case "C00PacketKeepAlive":
                        setValue("packet", new Variable(new PacketTables.C00PacketKeepAlive(((C00PacketKeepAlive)eventSentPacket.getPacket()).key)));
                        break;
                    case "C0FPacketConfirmTransaction":
                        setValue("packet", new Variable(new PacketTables.C0FPacketConfirmTransaction(((C0FPacketConfirmTransaction)eventSentPacket.getPacket()).windowId,
                                ((C0FPacketConfirmTransaction)eventSentPacket.getPacket()).uid,
                                ((C0FPacketConfirmTransaction)eventSentPacket.getPacket()).accepted)));
                        break;
                }
                setValue("cancelled", new Variable(new Boolean(eventSentPacket.isCancelled())));
                break;
        }
    }

    public void post() {
        switch (event.name()) {
            case "move":
                final EventMove eventMove = (EventMove) event;
                eventMove.setX(((Number)getValue("x").getValue()).getValue());
                eventMove.setY(((Number)getValue("y").getValue()).getValue());
                eventMove.setZ(((Number)getValue("z").getValue()).getValue());
                break;
            case "motion":
                final EventMotion eventMotion = (EventMotion) event;
                eventMotion.x = ((Number)getValue("x").getValue()).getValue();
                eventMotion.y = ((Number)getValue("y").getValue()).getValue();
                eventMotion.z = ((Number)getValue("z").getValue()).getValue();
                eventMotion.yaw = (float) ((Number)getValue("yaw").getValue()).getValue();
                eventMotion.pitch = (float) ((Number)getValue("pitch").getValue()).getValue();
                eventMotion.onGround = ((Boolean)getValue("onGround").getValue()).getValue();
                eventMotion.sneaking = ((Boolean)getValue("sneaking").getValue()).getValue();
                eventMotion.sprinting = ((Boolean)getValue("sprinting").getValue()).getValue();
                eventMotion.setState(((StringVar)getValue("state").getValue()).getValue());
                break;
            case "sentPacket":
                final EventSentPacket eventSentPacket = (EventSentPacket) event;
                eventSentPacket.setCancelled(((Boolean)(getValue("cancelled")).getValue()).getValue());
                switch (eventSentPacket.getPacket().getPacketName()) {
                    case "C03PacketPlayer":
                    case "C04PacketPlayerPosition":
                    case "C05PacketPlayerLook":
                    case "C06PacketPlayerPosLook":
                        final C03PacketPlayer c03 = eventSentPacket.getPacket();
                        c03.x = ((Number)(((Table)getValue("packet").getValue()).getValue("x").getValue())).getValue();
                        c03.x = ((Number)(((Table)getValue("packet").getValue()).getValue("y").getValue())).getValue();
                        c03.x = ((Number)(((Table)getValue("packet").getValue()).getValue("z").getValue())).getValue();
                        c03.yaw = (float) ((Number)(((Table)getValue("packet").getValue()).getValue("yaw").getValue())).getValue();
                        c03.pitch = (float) ((Number)(((Table)getValue("packet").getValue()).getValue("pitch").getValue())).getValue();
                        c03.onGround = ((Boolean)(((Table)getValue("packet").getValue()).getValue("onGround").getValue())).getValue();
                    break;
                    case "C00PacketKeepAlive":
                        final C00PacketKeepAlive c00 = eventSentPacket.getPacket();
                        c00.key = (int) ((Number)(((Table)getValue("packet").getValue()).getValue("key").getValue())).getValue();
                        break;
                    case "C0FPacketConfirmTransaction":
                        final C0FPacketConfirmTransaction c0F = eventSentPacket.getPacket();
                        c0F.windowId = (int) ((Number)(((Table)getValue("packet").getValue()).getValue("windowId").getValue())).getValue();
                        c0F.uid = (short) ((Number)(((Table)getValue("packet").getValue()).getValue("uid").getValue())).getValue();
                        c0F.accepted = ((Boolean)(((Table)getValue("packet").getValue()).getValue("accepted").getValue())).getValue();
                        break;
                }
                break;
        }
    }

}
