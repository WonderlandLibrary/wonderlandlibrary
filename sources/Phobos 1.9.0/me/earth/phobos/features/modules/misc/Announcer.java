package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.manager.FileManager;

import java.util.*;

public class Announcer
        extends Module {
    private static final String directory = "phobos/announcer/";
    private final Setting<Boolean> join = this.register(new Setting<Boolean>("Join", true));
    private final Setting<Boolean> leave = this.register(new Setting<Boolean>("Leave", true));
    private final Setting<Boolean> eat = this.register(new Setting<Boolean>("Eat", true));
    private final Setting<Boolean> walk = this.register(new Setting<Boolean>("Walk", true));
    private final Setting<Boolean> mine = this.register(new Setting<Boolean>("Mine", true));
    private final Setting<Boolean> place = this.register(new Setting<Boolean>("Place", true));
    private final Setting<Boolean> totem = this.register(new Setting<Boolean>("TotemPop", true));
    private final Setting<Boolean> random = this.register(new Setting<Boolean>("Random", true));
    private final Setting<Boolean> greentext = this.register(new Setting<Boolean>("Greentext", false));
    private final Setting<Boolean> loadFiles = this.register(new Setting<Boolean>("LoadFiles", false));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("SendDelay", 40));
    private final Setting<Integer> queueSize = this.register(new Setting<Integer>("QueueSize", 5, 1, 100));
    private final Setting<Integer> mindistance = this.register(new Setting<Integer>("Min Distance", 10, 1, 100));
    private final Setting<Boolean> clearQueue = this.register(new Setting<Boolean>("ClearQueue", false));
    private Map<Action, ArrayList<String>> loadedMessages = new HashMap<Action, ArrayList<String>>();
    private final Map<Action, Message> queue = new HashMap<Action, Message>();

    public Announcer() {
        super("Announcer", "How to get muted quick.", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onLoad() {
        this.loadMessages();
    }

    @Override
    public void onEnable() {
        this.loadMessages();
    }

    @Override
    public void onUpdate() {
        if (this.loadFiles.getValue().booleanValue()) {
            this.loadMessages();
            Command.sendMessage("<Announcer> Loaded messages.");
            this.loadFiles.setValue(false);
        }
    }

    public void loadMessages() {
        HashMap<Action, ArrayList<String>> newLoadedMessages = new HashMap<Action, ArrayList<String>>();
        for (Action action : Action.values()) {
            String fileName = directory + action.getName() + ".txt";
            List<String> fileInput = FileManager.readTextFileAllLines(fileName);
            Iterator<String> i = fileInput.iterator();
            ArrayList<String> msgs = new ArrayList<String>();
            while (i.hasNext()) {
                String string = i.next();
                if (string.replaceAll("\\s", "").isEmpty()) continue;
                msgs.add(string);
            }
            if (msgs.isEmpty()) {
                msgs.add(action.getStandartMessage());
            }
            newLoadedMessages.put(action, msgs);
        }
        this.loadedMessages = newLoadedMessages;
    }

    private String getMessage(Action action, int number, String info) {
        return "";
    }

    private Action getRandomAction() {
        Random rnd = new Random();
        int index = rnd.nextInt(7);
        int i = 0;
        for (Action action : Action.values()) {
            if (i == index) {
                return action;
            }
            ++i;
        }
        return Action.WALK;
    }

    public enum Action {
        JOIN("Join", "Welcome _!"),
        LEAVE("Leave", "Goodbye _!"),
        EAT("Eat", "I just ate % _!"),
        WALK("Walk", "I just walked % Blocks!"),
        MINE("Mine", "I mined % _!"),
        PLACE("Place", "I just placed % _!"),
        TOTEM("Totem", "_ just popped % Totems!");

        private final String name;
        private final String standartMessage;

        Action(String name, String standartMessage) {
            this.name = name;
            this.standartMessage = standartMessage;
        }

        public String getName() {
            return this.name;
        }

        public String getStandartMessage() {
            return this.standartMessage;
        }
    }

    public static class Message {
        public final Action action;
        public final String name;
        public final int amount;

        public Message(Action action, String name, int amount) {
            this.action = action;
            this.name = name;
            this.amount = amount;
        }
    }
}

