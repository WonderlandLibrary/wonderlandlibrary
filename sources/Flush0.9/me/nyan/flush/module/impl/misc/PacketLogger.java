package me.nyan.flush.module.impl.misc;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketLogger extends Module {
    private FileWriter writer;

    public PacketLogger() {
        super("PacketLogger", Category.MISC);
    }

    @Override
    public void onEnable() {
        try {
            int i = 1;
            while (true) {
                File file = new File(Flush.getClientPath(), "packetlogger" + File.separator + "packet_logger_" + i + ".txt");
                if (!file.exists()) {
                    writer = new FileWriter(file, true);
                    break;
                }
                i++;
            }
        } catch (IOException e) {
            Flush.LOGGER.error("[" + name + "] Failed to create file: " + e.getMessage());
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
        }
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (writer == null) {
            return;
        }

        int ticksExisted = 0;
        if (mc.thePlayer != null) {
            ticksExisted = mc.thePlayer.ticksExisted;
        }

        try {
            Class<?> c = e.getPacket().getClass();

            String name = c.getName();
            writer.write("[" + ticksExisted + "] " + name.substring(name.lastIndexOf('.') + 1) + ": \n");

            List<Field> fields = new ArrayList<>();
            while (c.getSuperclass() != null) {
                fields.addAll(Arrays.asList(c.getDeclaredFields()));
                c = c.getSuperclass();
            }
            if (!fields.isEmpty()) {
                for (Field field : fields) {
                    field.setAccessible(true);

                    try {
                        writer.write("      " + field.getName() + ": ");

                        if (!field.getType().isArray()) {
                            writer.write(String.valueOf(field.get(e.getPacket())));
                        } else {
                            name = field.getType().getComponentType().getName();
                            writer.write(name.substring(name.lastIndexOf('.') + 1) + "[\n");

                            Object array = field.get(e.getPacket());
                            for (int i = 0; i < Array.getLength(array); i++) {
                                Object o = Array.get(array, i);
                                writer.write("            " + o + ",\n");
                            }

                            writer.write("      ]");
                        }
                        writer.write("\n");
                    } catch (IllegalAccessException ignored) {
                    }
                }
            } else {
                writer.write("      empty\n");
            }

            writer.flush();
        } catch (IOException ex) {
            Flush.LOGGER.error("[" + name + "] Failed to write to file: " + ex.getMessage());
            toggle();
        }
    }
}
