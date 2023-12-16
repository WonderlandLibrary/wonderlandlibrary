package wtf.expensive.modules.impl.util;

import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.network.play.client.CPlayerTryUseItemPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.network.play.server.SJoinGamePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.packet.EventPacket;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.modules.settings.imp.SliderSetting;
import wtf.expensive.util.ClientUtil;
import wtf.expensive.util.JoinerUtil;
import wtf.expensive.util.SoundUtil;
import wtf.expensive.util.misc.TimerUtil;

/**
 * @author dedinside
 * @since 02.07.2023
 */
@FunctionAnnotation(name = "RW Joiner", type = Type.Util)
public class GreifJoinerFunction extends Function {

    private final SliderSetting griefSelection = new SliderSetting("Номер грифа", 1, 1, 28, 1);
    private final TimerUtil timerUtil = new TimerUtil();

    public GreifJoinerFunction() {
        addSettings(griefSelection);
    }

    @Override
    protected void onEnable() {
        JoinerUtil.selectCompass();
        mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
        super.onEnable();
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            handleEventUpdate();
        }
        if (event instanceof EventPacket eventPacket) {
            if (eventPacket.getPacket() instanceof SJoinGamePacket) {
                try {
                    if (mc.ingameGUI.getTabList().header == null) {
                        return;
                    }
                    String string = TextFormatting.getTextWithoutFormattingCodes(mc.ingameGUI.getTabList().header.getString());
                    if (!string.contains("Lobby")) {
                        return;
                    }
                    String string2 = "Вы успешно зашли на " + griefSelection.getValue().intValue() + " гриф!";
                    ClientUtil.sendMesage(string2);
                    SoundUtil.playSound(1, 1);
                    this.toggle();
                } catch (Exception ignored) {
                }
            }
            if (eventPacket.getPacket() instanceof SChatPacket packet) {

                String message = TextFormatting.getTextWithoutFormattingCodes(packet.getChatComponent().getString());

                if (message.contains("К сожалению сервер переполнен")
                        || message.contains("Подождите 20 секунд!")
                        || message.contains("большой поток игроков")) {
                    JoinerUtil.selectCompass();
                    mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));

                }
            }
        }

    }

    private void handleEventUpdate() {
        if (mc.currentScreen == null) {
            if (mc.player.ticksExisted < 5)
                mc.player.connection.sendPacket(new CPlayerTryUseItemPacket(Hand.MAIN_HAND));
        } else if (mc.currentScreen instanceof ChestScreen) {
            try {
                int numberGrief = griefSelection.getValue().intValue();

                ContainerScreen container = (ContainerScreen) mc.currentScreen;
                for (int i = 0; i < container.getContainer().inventorySlots.size(); i++) {
                    String s = container.getContainer().inventorySlots.get(i).getStack().getDisplayName().getString();
                    if (ClientUtil.isConnectedToServer("reallyworld")) {
                        if (s.contains("ГРИФЕРСКОЕ ВЫЖИВАНИЕ")) {
                            if (timerUtil.hasTimeElapsed(50)) {
                                mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.PICKUP, mc.player);
                                timerUtil.reset();
                            }
                        }
                    }

                    if (s.contains("ГРИФ #" + numberGrief + " (1.16.5)")) {
                        if (timerUtil.hasTimeElapsed(50)) {
                            mc.playerController.windowClick(mc.player.openContainer.windowId, i, 0, ClickType.PICKUP, mc.player);
                            timerUtil.reset();
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
