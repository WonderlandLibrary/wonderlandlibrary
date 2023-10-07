package com.ohare.client.module.modules.other;

import com.ohare.client.Client;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.server.S02PacketChat;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Xen for OhareWare
 * @since 7/31/2019
 **/
public class TrashTalker extends Module {
    private Random random = new Random();
    private List <String> niggers;
    private ArrayList <String> deathMessages = new ArrayList <> ();

    public TrashTalker() {
        super("TrashTalker", Category.OTHER, new Color(0x8DFFF0).getRGB());
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (deathMessages.size() < 1) return;
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat chat = (S02PacketChat) event.getPacket();
            String message = chat.getChatComponent().getUnformattedText();
            ArrayList <String> formattedDeathMessages = new ArrayList <>();
            deathMessages.forEach(m -> formattedDeathMessages.add(m.replace("PLAYER", mc.session.getUsername())));
            String[] compare = message.split(" ", 2);
            if (compare.length > 1) {
                formattedDeathMessages.stream().filter(m -> compare[1].equalsIgnoreCase(m)).forEach(m -> {
                    String trashMessage = niggers.get(random.nextInt(niggers.size() - 1));
                    mc.thePlayer.sendChatMessage(trashMessage.replace("%TARGET%", compare[0]));
				});
            }
        }
    }

    @Override
    public void onDisable() {
        deathMessages.clear();
    }

    @Override
    public void onEnable() {
        populateDeathMessages();
        try {
            File file = new File(Client.INSTANCE.getDirectory(), "trashtalker.txt");
            if (file.exists()) {
                List < String > lines = Files.readAllLines(file.toPath());
                if (lines.size() > 0) {
                    niggers = lines;
                    return;
                }
            }
            file.createNewFile();
            niggers = new ArrayList < >();
            niggers.add("ur trash");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void populateDeathMessages() {
        deathMessages.add("was slain by PLAYER.");
        deathMessages.add("was slain by PLAYER. FINAL KILL!");
        deathMessages.add("was shot by PLAYER.");
        deathMessages.add("was shot by PLAYER. FINAL KILL!");
        deathMessages.add("was thrown into the void by PLAYER.");
        deathMessages.add("was thrown into the void by PLAYER. FINAL KILL!");
        deathMessages.add("was doomed to fall by PLAYER.");
        deathMessages.add("was doomed to fall by PLAYER. FINAL KILL!");
        deathMessages.add("was toasted by PLAYER.");
        deathMessages.add("was toasted by PLAYER. FINAL KILL!");
        deathMessages.add("was killed by PLAYER.");
        deathMessages.add("was killed by PLAYER. FINAL KILL!");
        deathMessages.add("was bomberman'd by PLAYER.");
        deathMessages.add("was bomberman'd by PLAYER. FINAL KILL!");
        deathMessages.add("was Bomberman'd by PLAYER.");
        deathMessages.add("was Bomberman'd by PLAYER. FINAL KILL!");
        deathMessages.add("was thrown off a cliff by PLAYER.");
        deathMessages.add("was thrown off a cliff by PLAYER. FINAL KILL!");
        deathMessages.add("was shot and killed by PLAYER.");
        deathMessages.add("was shot and killed by PLAYER. FINAL KILL!");
        deathMessages.add("was snowballed to death by PLAYER.");
        deathMessages.add("was snowballed to death by PLAYER. FINAL KILL!");
        deathMessages.add("was killed with a potion by PLAYER.");
        deathMessages.add("was killed with a potion by PLAYER. FINAL KILL!");
        deathMessages.add("was killed with an explosion by PLAYER.");
        deathMessages.add("was killed with an explosion by PLAYER. FINAL KILL!");
        deathMessages.add("was killed with magic by PLAYER.");
        deathMessages.add("was killed with magic by PLAYER. FINAL KILL!");
        deathMessages.add("was filled full of lead by PLAYER.");
        deathMessages.add("was filled full of lead by PLAYER. FINAL KILL!");
        deathMessages.add("was iced by PLAYER.");
        deathMessages.add("was iced by PLAYER. FINAL KILL!");
        deathMessages.add("met their end by PLAYER.");
        deathMessages.add("met their end by PLAYER. FINAL KILL!");
        deathMessages.add("lost a drinking contest with PLAYER.");
        deathMessages.add("lost a drinking contest with PLAYER. FINAL KILL!");
        deathMessages.add("was killed with dynamite by PLAYER.");
        deathMessages.add("was killed with dynamite by PLAYER. FINAL KILL!");
        deathMessages.add("lost the draw to PLAYER.");
        deathMessages.add("lost the draw to PLAYER. FINAL KILL!");
        deathMessages.add("was struck down by PLAYER.");
        deathMessages.add("was struck down by PLAYER. FINAL KILL!");
        deathMessages.add("was turned to dust by PLAYER.");
        deathMessages.add("was turned to dust by PLAYER. FINAL KILL!");
        deathMessages.add("was turned to ash by PLAYER.");
        deathMessages.add("was turned to ash by PLAYER. FINAL KILL!");
        deathMessages.add("was melted by PLAYER.");
        deathMessages.add("was melted by PLAYER. FINAL KILL!");
        deathMessages.add("was incinerated by PLAYER.");
        deathMessages.add("was incinerated by PLAYER. FINAL KILL!");
        deathMessages.add("was vaporized by PLAYER.");
        deathMessages.add("was vaporized by PLAYER. FINAL KILL!");
        deathMessages.add("was struck with Cupid's arrow by PLAYER.");
        deathMessages.add("was struck with Cupid's arrow by PLAYER. FINAL KILL!");
        deathMessages.add("was given the cold shoulder by PLAYER.");
        deathMessages.add("was given the cold shoulder by PLAYER. FINAL KILL!");
        deathMessages.add("was hugged too hard by PLAYER.");
        deathMessages.add("was hugged too hard by PLAYER. FINAL KILL!");
        deathMessages.add("drank a love potion from PLAYER.");
        deathMessages.add("drank a love potion from PLAYER. FINAL KILL!");
        deathMessages.add("was hit by a love bomb from PLAYER.");
        deathMessages.add("was hit by a love bomb from PLAYER. FINAL KILL!");
        deathMessages.add("was no match for PLAYER.");
        deathMessages.add("was no match for PLAYER. FINAL KILL!");
        deathMessages.add("was smote from afar by PLAYER.");
        deathMessages.add("was smote from afar by PLAYER. FINAL KILL!");
        deathMessages.add("was justly ended by PLAYER.");
        deathMessages.add("was justly ended by PLAYER. FINAL KILL!");
        deathMessages.add("was purified by PLAYER.");
        deathMessages.add("was purified by PLAYER. FINAL KILL!");
        deathMessages.add("was killed with holy water by PLAYER.");
        deathMessages.add("was killed with holy water by PLAYER. FINAL KILL!");
        deathMessages.add("was dealt vengeful justice by PLAYER.");
        deathMessages.add("was dealt vengeful justice by PLAYER. FINAL KILL!");
        deathMessages.add("was returned to dust by PLAYER.");
        deathMessages.add("was returned to dust by PLAYER. FINAL KILL!");
        deathMessages.add("be shot and killed by PLAYER.");
        deathMessages.add("be shot and killed by PLAYER. FINAL KILL!");
        deathMessages.add("be snowballed to death by PLAYER.");
        deathMessages.add("be snowballed to death by PLAYER. FINAL KILL!");
        deathMessages.add("be sent to Davy Jones' locker by PLAYER.");
        deathMessages.add("be sent to Davy Jones' locker by PLAYER. FINAL KILL!");
        deathMessages.add("be killed with rum by PLAYER.");
        deathMessages.add("be killed with rum by PLAYER. FINAL KILL!");
        deathMessages.add("be shot with cannon by PLAYER.");
        deathMessages.add("be shot with cannon by PLAYER. FINAL KILL!");
        deathMessages.add("be killed with magic by PLAYER.");
        deathMessages.add("be killed with magic by PLAYER. FINAL KILL!");
        deathMessages.add("was glazed in BBQ sauce by PLAYER.");
        deathMessages.add("was glazed in BBQ sauce by PLAYER. FINAL KILL!");
        deathMessages.add("was sprinkled with chilli powder by PLAYER.");
        deathMessages.add("was sprinkled with chilli powder by PLAYER. FINAL KILL!");
        deathMessages.add("was sprinkled with chili powder by PLAYER.");
        deathMessages.add("was sprinkled with chili powder by PLAYER. FINAL KILL!");
        deathMessages.add("was sliced up by PLAYER.");
        deathMessages.add("was sliced up by PLAYER. FINAL KILL!");
        deathMessages.add("was overcooked by PLAYER.");
        deathMessages.add("was overcooked by PLAYER. FINAL KILL!");
        deathMessages.add("was deep fried by PLAYER.");
        deathMessages.add("was deep fried by PLAYER. FINAL KILL!");
        deathMessages.add("was boiled by PLAYER.");
        deathMessages.add("was boiled by PLAYER. FINAL KILL!");
        deathMessages.add("was spooked by PLAYER.");
        deathMessages.add("was spooked by PLAYER. FINAL KILL!");
        deathMessages.add("was spooked off the map by PLAYER.");
        deathMessages.add("was spooked off the map by PLAYER. FINAL KILL!");
        deathMessages.add("was totally spooked by PLAYER.");
        deathMessages.add("was totally spooked by PLAYER. FINAL KILL!");
        deathMessages.add("was remotely spooked by PLAYER.");
        deathMessages.add("was remotely spooked by PLAYER. FINAL KILL!");
    }
}