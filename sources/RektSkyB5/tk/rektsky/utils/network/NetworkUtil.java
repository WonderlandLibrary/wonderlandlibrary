/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.network;

import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.utils.Timer;

public class NetworkUtil {
    Timer lastPacket = new Timer();
    ArrayList<Long> timings = new ArrayList();
    String lastTPS = "Unknown";

    public String getTPS() {
        try {
            DecimalFormat ff = new DecimalFormat("#.##");
            if (this.lastPacket.hasTimeElapsed(30000L, true)) {
                this.timings.add(30000L);
            }
            this.lastTPS = ff.format(this.avg(this.timings)) + " (" + this.lastPacket.getEllapsedTime() + ")";
            return this.lastTPS;
        }
        catch (Exception ex) {
            return this.lastTPS;
        }
    }

    private double avg(ArrayList<Long> timings) {
        double _avg = 0.0;
        for (long timing : timings) {
            _avg += (double)timing;
        }
        _avg /= (double)timings.size();
        if ((_avg = 1000.0 / _avg * 20.0) > 20.0) {
            _avg = 20.0;
        }
        return _avg;
    }

    @Subscribe
    public void onPacket(PacketReceiveEvent event) {
        try {
            if (event.getPacket() instanceof S03PacketTimeUpdate) {
                this.timings.add(this.lastPacket.getEllapsedTime());
                if (this.timings.size() > 250) {
                    this.timings.remove(this.timings.size() - 1);
                }
                this.lastPacket.reset();
            }
            if (event.getPacket() instanceof S01PacketJoinGame) {
                this.timings.clear();
                this.lastPacket.reset();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

