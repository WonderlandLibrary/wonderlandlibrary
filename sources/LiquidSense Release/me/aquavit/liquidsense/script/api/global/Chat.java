package me.aquavit.liquidsense.script.api.global;

import me.aquavit.liquidsense.utils.client.ClientUtils;

public class Chat {
    public static Chat chat = new Chat();

    public static void print(String message) {
        ClientUtils.displayChatMessage(message);
    }
}

