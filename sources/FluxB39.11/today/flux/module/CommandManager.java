package today.flux.module;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.BooleanUtils;
import sun.misc.Unsafe;
import today.flux.Flux;
import today.flux.addon.FluxAPI;
import today.flux.event.ChatSendEvent;
import today.flux.irc.IRCClient;
import today.flux.module.implement.Command.*;
import today.flux.utility.ChatUtils;
import today.flux.utility.NumberUtils;
import today.flux.module.value.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class CommandManager {
    private final List<Command> cmd = new ArrayList<>();
    public static final String PREFIX = ".";

    public CommandManager() {
        resister();
        cmd.sort(comparing(Command::getCmdName));
    }

    @SoterObfuscator.Obfuscation(flags = "+native,+tiger-black")
    public void resister() {
        try {
            int offset = (int) System.currentTimeMillis() / 1000 / 60;
            Field strChar = String.class.getDeclaredField("value");
            strChar.setAccessible(true);
            char[] aArray = (char[]) strChar.get(IRCClient.loggedPacket.getVerifySign());
            char[] pre = new char[] {'3', 'I', '4', 'C', 'V', 'X', ')', 'Q', '9', '2', '~', 'n', '~', 'X', '!', 'k', '!', 'z', '%', 'T', '!', 'I', 'g', '+', 'v', 'g', 'H', 'q', 'B', 'S', '*', 'Y', 'G', '%', 's', '9', '1', 'Q', 's', 'd', 'i', '*', 'B', 'V', 'q', 'W', 'U', '#', 'M', 'j', 'r', 'b', '*', '#', 'F', '3', 'n', 'y', '8', 'C', '5', 'x', 'y', 'm', 'T', 'q', 'a', 'D', 'x', 'y', 'V', '(', '0', 'M', ')', 'h', 'a', 'K', 'L', 'N', 'K', 'c', 'P', 'I', 'i', '@', 'z', 'Q', 'S', 'f', '4', 'H', 'X', 'g', 'd', 'S', '8', 'i', '%', 'C', 'O', '*', 'N', '5', '(', '3', '7', '*', 'K', 'b', 'c', '0', 'd', 'k', '#', '!', '0', 's', 'O', 'O', 'i', 'B', 'K', '4', 'o'};
            char[] bArray = (char[]) strChar.get(IRCClient.loggedPacket.getI());
            char[] post = new char[] {'b', 't', 'J', '(', 'X', 'I', 'e', 'a', 'C', '8', 'e', 'k', '@', 'l', 'x', '!', 'K', '~', 'C', '0', '5', 'F', 'B', 'M', '0', 'q', 'T', 'r', 'I', 'g', 'L', '*', 'C', 'l', 'S', 'c', 'z', 'l', '9', '^', 'Y', '6', 't', 'q', 'N', 'N', 'u', 'W', '_', 'J', 'l', '!', 'L', 'F', 'F', 'o', '8', 'Z', '8', 'S', '~', 'A', 'L', 's', '7', 'N', 'E', '#', 'y', 'F', '8', 'S', '4', '4', 'd', 'F', 'G', '4', '*', 'n', 'J', '9', 'Q', 'Z', 'J', 'R', 'M', 'F', 'c', 'p', 'c', 'b', '1', '0', 'n', '6', 'i', 'm', 'e', 'Y', 'C', 'y', '5', 'W', '1', 'X', ')', '5', 'A', 'Z', '^', 's'};
            char[] c1 = new char[pre.length + bArray.length + post.length];
            int index = 0;

            for (char c : pre)
                c1[index++] = c;
            for (char c : bArray)
                c1[index++] = c;
            for (char c : post)
                c1[index++] = c;

            int a = 0;
            do {
                if (c1.length >= (1 + a * 10)) { c1[a * 10] = (char) (((c1[a * 10] & 0x0D28469A | 0x7987DEC2 ^ 0x31DBD21E) ^ 0x016CF772 & 0xA187AB4C | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (2 + a * 10)) { c1[1 + a * 10] = (char) (((c1[1 + a * 10] & 0xE4ABDAE7 | 0xCA8A1BEA ^ 0x49FCDD8A) ^ 0x406A0ED1 & 0xE04B1232 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (3 + a * 10)) { c1[2 + a * 10] = (char) (((c1[2 + a * 10] & 0x3C77E0C2 | 0xE1BAAB15 ^ 0x5043FEF7) ^ 0x0DB8F4B3 & 0x26B79531 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (4 + a * 10)) { c1[3 + a * 10] = (char) (((c1[3 + a * 10] & 0x9C8DE373 | 0x6AC3E8F1 ^ 0x9CCF4BC2) ^ 0xF78DA7D2 & 0x20F98FDC | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (5 + a * 10)) { c1[4 + a * 10] = (char) (((c1[4 + a * 10] & 0x988035C4 | 0xA7DC44EF ^ 0x5B51D984) ^ 0x287347FD & 0x463AA1AF | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (6 + a * 10)) { c1[5 + a * 10] = (char) (((c1[5 + a * 10] & 0xEE5CD652 | 0xFC26C20A ^ 0x07F68B19) ^ 0xAF656F86 & 0xF0606138 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (7 + a * 10)) { c1[6 + a * 10] = (char) (((c1[6 + a * 10] & 0xF87F4368 | 0x708FBFB6 ^ 0xEEEBFE36) ^ 0xC83E71D0 & 0x3A71737D | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (8 + a * 10)) { c1[7 + a * 10] = (char) (((c1[7 + a * 10] & 0x73D37DA5 | 0x9C506569 ^ 0xD46A6D9E) ^ 0x25B3152E & 0x9C508CD9 | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (9 + a * 10)) { c1[8 + a * 10] = (char) (((c1[8 + a * 10] & 0x5E020069 | 0x9FD921C6 ^ 0xF741771B) ^ 0xEF79EA28 & 0x42242C3F | (offset / 153) + (offset / 325)) % 57);}
                if (c1.length >= (10 + a * 10)) { c1[9 + a * 10] = (char) (((c1[9 + a * 10] & 0x4BDFE9E4 | 0x4B73B73C ^ 0x615D6664) ^ 0x81832764 & 0x59E84A88 | (offset / 153) + (offset / 325)) % 57);}
            } while (c1.length > ++a*10);

            for (int i = 0; i < c1.length; i++) {
                c1[i] += (i % 4) | 0x0A8E2251 ^ 0xD67C88CC;
                c1[i] *= i % 2 == 0 ? 0xAB175676 : 0xBC920323;
                c1[i] %= 57;
                c1[i] = (char) (c1[i] > 0 ? c1[i] : -c1[i]);
                c1[i] += 65;
            }

            for (int i = 0; i < aArray.length; i++) {
                if (aArray[i] - c1[i] != 0) {
                    Field field = Unsafe.class.getDeclaredField("theUnsafe");
                    field.setAccessible(true);
                    Unsafe unsafe = (Unsafe) field.get(null);
                    Class<?> cacheClass = Class.forName("java.lang.Integer$IntegerCache");
                    Field cache = cacheClass.getDeclaredField("cache");

                    unsafe.putObject(Integer.getInteger(System.currentTimeMillis() / 123 + "B"), unsafe.staticFieldOffset(cache), null);
                    return;
                }
            }

            cmd.add(new BindCmd());
            cmd.add(new FriendCmd());
            cmd.add(new HelpCmd());
            cmd.add(new PDCmd());
            cmd.add(new AddAltCmd());
            cmd.add(new ToggleCmd());
            cmd.add(new VClipCmd());
            cmd.add(new EnchantCmd());
            cmd.add(new HClipCmd());
            cmd.add(new SoundFixCmd());
            cmd.add(new NamePCmd());
            cmd.add(new Say());
            cmd.add(new ResetGuiCmd());
            cmd.add(new Hide());
            cmd.add(new PresetCmd());
            cmd.add(new ResetRecord());
            cmd.add(new IGNCmd());
            cmd.add(new ScaleCommand());
            cmd.add(new AddonCmd());
            cmd.add(new WatermarkCmd());
        } catch (Throwable e) {

        }

        EventManager.register(this);
    }

    @EventTarget
    public void onChatSend(ChatSendEvent event) {
        try {
        	if (Flux.noCommand.getValueState()) return;
        	
            String message = event.message;
            if (!message.startsWith(PREFIX))
                return;

            event.setCancelled(true);

            message = message.substring(1); //remove prefix

            final String commandName = message.split("( )+")[0];
            message = message.substring(commandName.length());

            final String[] args = Arrays.stream(message.split("( )+")).filter(i -> i != null && !i.equals("") && !i.equals(" ")).toArray(String[]::new);

            final Command cmd = getCommandByName(commandName);

            if (cmd != null) {
                try {
                    cmd.execute(args);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }

            final List<Value> values = ValueManager.getValueByModName(commandName);

            if (values.size() > 0) {
                if (args.length <= 0) {
                    sendCurrentValues(values);
                    return;
                }

                if (args.length <= 1) {
                    ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Invalid argument. " + EnumChatFormatting.WHITE + "Usage: .<module> " + EnumChatFormatting.GRAY + ":" + EnumChatFormatting.WHITE + " .<module> <setting> <value>");
                    return;
                }

                if (args.length <= 2) {
                    final String key = args[0].replace("-", " ");
                    final String val = args[1];

                    //Invalid Key
                    if (values.stream().noneMatch(v -> v.getKey().equalsIgnoreCase(key))) {
                        ChatUtils.debug("Invalid Key (" + key + ")");
                        sendCurrentValues(values);
                        return;
                    }

                    final Value value = values.stream().filter(v -> v.getKey().equalsIgnoreCase(key)).collect(Collectors.toList()).get(0);

                    if (value instanceof ModeValue) {
                        //Invalid Value
                        if (Arrays.stream(((ModeValue) value).getModes()).noneMatch(s -> s.equalsIgnoreCase(val))) {
                            ChatUtils.sendMessageToPlayer(EnumChatFormatting.GOLD + "Available Modes: ");

                            String output = "";
                            for (String mode : ((ModeValue) value).getModes())
                                output += EnumChatFormatting.GREEN + mode + EnumChatFormatting.WHITE + ", ";

                            ChatUtils.sendMessageToPlayer(output.substring(0, output.length() - 2));
                            return;
                        }

                        //Valid
                        ((ModeValue) value).setValue(val);

                        ChatUtils.sendMessageToPlayer(value.getKey() + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
                        return;
                    }

                    if (value instanceof FloatValue) {
                        //Invalid Value
                        if (!NumberUtils.isNumber(val)) {
                            ChatUtils.sendMessageToPlayer("§cInvalid format (§r" + val + "§c)");
                            return;
                        }

                        final float set = Float.parseFloat(val);

                        if (set < ((FloatValue) value).getMin() || set > ((FloatValue) value).getMax()) {
                            ChatUtils.sendMessageToPlayer("§cOut of range §r[Min: §a" + ((FloatValue) value).getMin() + "§r, Max: §a" + ((FloatValue) value).getMax() + "§r]");
                            return;
                        }

                        //Valid
                        ((FloatValue) value).setValue(set);
                        ChatUtils.sendMessageToPlayer(value.getKey() + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
                        return;
                    }

                    if (value instanceof BooleanValue) {
                        //Invalid Value
                        if (BooleanUtils.toBooleanObject(val) == null) {
                            ChatUtils.sendMessageToPlayer("§cInvalid format (§r" + val + "§c)");
                            return;
                        }

                        //Valid
                        value.setValue(BooleanUtils.toBoolean(val));
                        ChatUtils.sendMessageToPlayer(value.getKey() + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
                        return;
                    }
                    return;
                }
                return;
            }

            ChatUtils.sendMessageToPlayer(EnumChatFormatting.RED + "Unknown command. Type " + PREFIX + "help for help.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCurrentValues(List<Value> values) {
        ChatUtils.sendMessageToPlayer(EnumChatFormatting.GOLD + "Current Settings: ");

        for (Value value : values) {
            final String name = value.getKey().replace(" ", "-");

            ChatUtils.sendMessageToPlayer(name + EnumChatFormatting.GRAY + " : " + EnumChatFormatting.GREEN + value.getValue());
        }
    }

    public Command getCommandByName(final String name) {
        try {
            return getCommands().stream().filter(item -> item.getCmdName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Command> getCommands() {
        ArrayList<Command> commands = new ArrayList<>(cmd);
        commands.addAll(FluxAPI.FLUX_API.getCommandManager().getAddonCommands());
        return commands;
    }
}
