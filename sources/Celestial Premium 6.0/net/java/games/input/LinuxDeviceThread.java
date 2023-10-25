/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.java.games.input.LinuxDeviceTask;

final class LinuxDeviceThread
extends Thread {
    private final List tasks = new ArrayList();

    public LinuxDeviceThread() {
        this.setDaemon(true);
        this.start();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final synchronized void run() {
        while (true) {
            if (!this.tasks.isEmpty()) {
                LinuxDeviceTask task = (LinuxDeviceTask)this.tasks.remove(0);
                task.doExecute();
                LinuxDeviceTask linuxDeviceTask = task;
                synchronized (linuxDeviceTask) {
                    task.notify();
                }
            }
            try {
                this.wait();
            }
            catch (InterruptedException interruptedException) {
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final Object execute(LinuxDeviceTask task) throws IOException {
        Object object = this;
        synchronized (object) {
            this.tasks.add(task);
            this.notify();
        }
        object = task;
        synchronized (object) {
            while (task.getState() == 1) {
                try {
                    task.wait();
                }
                catch (InterruptedException interruptedException) {}
            }
        }
        switch (task.getState()) {
            case 2: {
                return task.getResult();
            }
            case 3: {
                throw task.getException();
            }
        }
        throw new RuntimeException("Invalid task state: " + task.getState());
    }
}

