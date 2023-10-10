package client.module.impl.other;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.TickEvent;
import client.module.Category;
import client.module.Module;
import client.module.ModuleInfo;
import client.value.impl.NumberValue;
import client.value.impl.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ModuleInfo(name = "AutoGroomer", description = "", category = Category.OTHER)
public class AutoGroomer extends Module {

    private final NumberValue delay = new NumberValue("Delay", this, 1, 0, 4, 0.1);

    @EventLink
    public final Listener<TickEvent> onTick = event -> {
        String altFilePath = "insults.txt";
        try {
            List<String> alts = readAltsFromFile(altFilePath);
            String randomAlt = getRandomAlt(alts);

        if (mc.thePlayer.ticksExisted % (delay.getValue().doubleValue() * 20.0F) == 0) mc.thePlayer.sendChatMessage(randomAlt);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    private static List<String> readAltsFromFile(String filePath) throws IOException {
        List<String> alts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                alts.add(line);
            }
        }

        return alts;
    }

    private static String getRandomAlt(List<String> alts) {
        Random random = new Random();
        int randomIndex = random.nextInt(alts.size());
        return alts.get(randomIndex);
    }

}
