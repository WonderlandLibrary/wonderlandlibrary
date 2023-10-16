package com.enjoytheban.api.events.misc;

import com.enjoytheban.api.Event;
import com.enjoytheban.api.Type;

import tv.twitch.chat.Chat;

public class EventChat extends Event {

    /**
     * @author Purity
     * @desc An event that handles chat related things
     * @called EntityPlayerSP sendChatMessage
     */

    //Holds the message
    private String message;
    private ChatType type;

    //construcccctorsssssss
    public EventChat(ChatType type, String message) {
        this.type = type;
        this.message = message;
        setType(Type.PRE);
    }

    //get the message
    public String getMessage() {
        return message;
    }

    //set the shit
    public void setMessage(String message) {
        this.message = message;
    }

    public ChatType getChatType() {
        return this.type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public enum ChatType {
        Send, Receive
    }
}
