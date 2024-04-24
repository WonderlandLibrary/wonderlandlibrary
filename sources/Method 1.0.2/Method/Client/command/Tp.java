package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;

import java.util.Objects;

@Command.CommandFeatures(description = "Tp to position or player", Syntax = "tp <<x> <y> <z> [yaw] [pitch]|<player>>")

public class Tp extends Command {
    @Override
    public void runCommand(String s, String[] args) {

        if (args.length < 1) {
            ChatUtils.error("Invalid syntax.");
        } else if (args.length < 2) {
            EntityPlayer target = mc.world.getPlayerEntityByName(args[0]);
            if (target == null) {
                ChatUtils.error("Player \2477" + args[0] + " \247ccan not be found.");
                return;
            }
            double x = target.posX;
            double y = target.posY;
            double z = target.posZ;
            float pitch = target.rotationPitch;
            float yaw = target.rotationYaw;
            mc.player.setPositionAndRotation(x, y, z, yaw, pitch);
            ChatUtils.message("Teleported \2477" + mc.player.getName() + "\247e to \2479" + x
                    + "\247e, \2479" + y + "\247e, \2479" + z + "\247e.");
        } else if (args.length < 3) {
            ChatUtils.error("Invalid syntax.");
        } else {
            double x = mc.player.posX;
            double y = mc.player.posY;
            double z = mc.player.posZ;
            float pitch = mc.player.rotationPitch;
            float yaw = mc.player.rotationYaw;
            // CALC X
            try {
                x = parseMath(args[0], x);
            } catch (NullPointerException | NumberFormatException e) {
                ChatUtils.error("\2477" + args[0] + " \247cis not a valid number.");
                return;
            }
            // CALC Y
            try {
                y = parseMath(args[1], y);
            } catch (NullPointerException | NumberFormatException e) {
                ChatUtils.error("\2477" + args[1] + " \247cis not a valid number.");
                return;
            }
            // CALC Z
            try {
                z = parseMath(args[2], z);
            } catch (NullPointerException | NumberFormatException e) {
                ChatUtils.error("\2477" + args[2] + " \247cis not a valid number.");
                return;
            }
            if (args.length > 3) {
                // CALC YAW
                try {
                    yaw = (float) parseMath(args[3], yaw);
                } catch (NullPointerException | NumberFormatException e) {
                    ChatUtils.error("\2477" + args[3] + " \247cis not a valid number.");
                    return;
                }
            }
            if (args.length > 4) {
                // CALC PITCH
                try {
                    pitch = (float) parseMath(args[4], pitch);
                } catch (NullPointerException | NumberFormatException e) {
                    ChatUtils.error("\2477" + args[4] + " \247cis not a valid number.");
                    return;
                }
            }
            if (args.length > 5) {
                ChatUtils.warning("Too many arguments.");
            }
            mc.player.setPositionAndRotation(x, y, z, yaw, pitch);
            ChatUtils.message("Teleported \2477" + mc.player.getName() + "\247e to \2479" + x
                    + "\247e, \2479" + y + "\247e, \2479" + z + "\247e.");
        }

    }

    public static void updateSlot(int slot, ItemStack stack) {
        Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketCreativeInventoryAction(slot, stack));
    }

    private static double parseMath(String input, double old) {
        if (input.length() < 1) {
            throw new NumberFormatException();
        }
        if (input.charAt(0) == '~') {
            if (input.length() > 2 && input.charAt(1) == '+') {
                String coord = input.substring(2);

                return old + Double.parseDouble(coord);
            } else if (input.length() > 2 && input.charAt(1) == '-') {
                String coord = input.substring(2);

                return old - Double.parseDouble(coord);
            } else if (input.length() != 1) {
                throw new NumberFormatException();
            }
            return old;
        } else {
            return Double.parseDouble(input);
        }
    }


}