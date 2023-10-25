/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  ca.weblite.objc.Client
 *  ca.weblite.objc.NSObject
 *  ca.weblite.objc.Proxy
 *  ca.weblite.objc.annotations.Msg
 */
package com.mojang.text2speech;

import ca.weblite.objc.Client;
import ca.weblite.objc.NSObject;
import ca.weblite.objc.Proxy;
import ca.weblite.objc.annotations.Msg;
import com.google.common.collect.Queues;
import com.mojang.text2speech.Narrator;
import java.util.Queue;

public class NarratorOSX
extends NSObject
implements Narrator {
    private final Proxy synth = Client.getInstance().sendProxy("NSSpeechSynthesizer", "alloc", new Object[0]);
    private boolean speaking;
    private final Queue<String> queue = Queues.newConcurrentLinkedQueue();

    public NarratorOSX() {
        super("NSObject");
        this.synth.send("init", new Object[0]);
        this.synth.send("setDelegate:", new Object[]{this});
    }

    private void startSpeaking(String message) {
        this.synth.send("startSpeakingString:", new Object[]{message});
    }

    @Msg(selector="speechSynthesizer:didFinishSpeaking:", signature="v@:B")
    public void didFinishSpeaking(boolean naturally) {
        if (this.queue.isEmpty()) {
            this.speaking = false;
        } else {
            this.startSpeaking(this.queue.poll());
        }
    }

    @Override
    public void say(String msg) {
        if (this.speaking) {
            this.queue.offer(msg);
        } else {
            this.speaking = true;
            this.startSpeaking(msg);
        }
    }

    @Override
    public void clear() {
        this.queue.clear();
    }

    @Override
    public boolean active() {
        return true;
    }
}

