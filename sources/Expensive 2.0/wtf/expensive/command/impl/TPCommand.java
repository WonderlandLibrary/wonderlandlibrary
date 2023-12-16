package wtf.expensive.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.math.NumberUtils;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.util.misc.ServerUtil;

@CommandInfo(name = "tp", description = "Позволяет телепортироваться в любую точку")
public class TPCommand extends Command {

    @Override
    public void run(String[] args) throws Exception {
        if (!ServerUtil.isHW()) {
            sendMessage(TextFormatting.RED + "Данная функция работает только на HolyWorld!");
            return;
        }
        if (!NumberUtils.isNumber(args[1])) {
            PlayerEntity entityPlayer = mc.world.getPlayers()
                    .stream()
                    .filter(player -> player.getName().getString().equalsIgnoreCase(args[1]))
                    .findFirst().orElse(null);

            if (entityPlayer == null) {
                sendMessage(TextFormatting.RED + "Не удалось найти игрока с таким никнеймом!");
                return;
            }

            if (args[1].equals(entityPlayer.getName().getString())) {
                int x = (int) entityPlayer.getPosX();
                int y = (int) entityPlayer.getPosY();
                int z = (int) entityPlayer.getPosZ();

                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                for (int i = 0; i <= 10; i++) {
                    mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
                }
                mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), 0, mc.player.getPosZ(), false));
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                sendMessage("Телепортирую к игроку " + TextFormatting.GRAY + entityPlayer.getName().getString());

            }
        }
        if (NumberUtils.isNumber(args[1])) {
            if (args.length >= 2) {
                double x = 0, y = 0, z = 0;
                if (args.length == 4) {
                    x = Double.parseDouble(args[1]);
                    y = Double.parseDouble(args[2]);
                    z = Double.parseDouble(args[3]);
                    sendMessage("Пытаюсь телепортироваться на " + TextFormatting.LIGHT_PURPLE + args[1] + " " + args[2] + " " + args[3]);
                } else if (args.length == 3) {
                    x = Double.parseDouble(args[1]);
                    y = 150;
                    z = Double.parseDouble(args[2]);
                    sendMessage("Пытаюсь телепортироваться на " + TextFormatting.LIGHT_PURPLE + args[1] + " " + args[2]);
                } else if (args.length == 2) {
                    x = mc.player.getPosX();
                    y = mc.player.getPosY() + Double.parseDouble(args[1]);
                    z = mc.player.getPosZ();
                    sendMessage(TextFormatting.GREEN + "Вы успешно телепортировались на " + TextFormatting.WHITE + args[1] + TextFormatting.GREEN + " блоков вверх");
                } else {
                    error();
                }
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
                for (int i = 0; i <= 10; i++) {
                    mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, true));
                }
                mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(mc.player.getPosX(), 0, mc.player.getPosZ(), false));
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_SPRINTING));
                mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.STOP_SPRINTING));
            } else {
                error();
            }
        }
    }

    @Override
    public void error() {
        sendMessage(TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");

        sendMessage(".tp" + TextFormatting.GRAY + " <" + "x" + TextFormatting.GRAY + "> " + TextFormatting.GRAY + "<" + "y" + TextFormatting.GRAY + "> " + TextFormatting.GRAY + "<" + "z" + TextFormatting.GRAY + ">");

        sendMessage(".tp" + TextFormatting.GRAY + " <" + "x" + TextFormatting.GRAY + "> " + TextFormatting.GRAY + "<" + "z" + TextFormatting.GRAY + ">");

        sendMessage(".tp" + TextFormatting.GRAY + " <" + "y" + TextFormatting.GRAY + ">");
        sendMessage(".tp" + TextFormatting.GRAY + "<Никнейм игрока>");

    }
}
