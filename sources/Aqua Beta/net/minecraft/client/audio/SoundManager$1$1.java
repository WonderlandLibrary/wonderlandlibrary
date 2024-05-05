package net.minecraft.client.audio;

import net.minecraft.client.audio.SoundManager;
import paulscode.sound.SoundSystemLogger;

/*
 * Exception performing whole class analysis ignored.
 */
class SoundManager.1
extends SoundSystemLogger {
    SoundManager.1() {
    }

    public void message(String p_message_1_, int p_message_2_) {
        if (!p_message_1_.isEmpty()) {
            SoundManager.access$000().info(p_message_1_);
        }
    }

    public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
        if (!p_importantMessage_1_.isEmpty()) {
            SoundManager.access$000().warn(p_importantMessage_1_);
        }
    }

    public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_) {
        if (!p_errorMessage_2_.isEmpty()) {
            SoundManager.access$000().error("Error in class '" + p_errorMessage_1_ + "'");
            SoundManager.access$000().error(p_errorMessage_2_);
        }
    }
}
