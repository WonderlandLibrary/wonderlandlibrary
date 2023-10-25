/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.text2speech;

import com.mojang.text2speech.Narrator;

public class Text2Speech {
    public static void main(String[] args) {
        System.setProperty("jna.library.path", "./src/natives/resources/");
        Narrator narrator = Narrator.getNarrator();
        narrator.say("This is a test");
        while (true) {
            try {
                while (true) {
                    Thread.sleep(100L);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }
            break;
        }
    }
}

