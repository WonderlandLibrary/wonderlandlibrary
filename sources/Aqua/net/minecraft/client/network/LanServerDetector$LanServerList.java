package net.minecraft.client.network;

import com.google.common.collect.Lists;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.client.network.LanServerDetector;

public static class LanServerDetector.LanServerList {
    private List<LanServerDetector.LanServer> listOfLanServers = Lists.newArrayList();
    boolean wasUpdated;

    public synchronized boolean getWasUpdated() {
        return this.wasUpdated;
    }

    public synchronized void setWasNotUpdated() {
        this.wasUpdated = false;
    }

    public synchronized List<LanServerDetector.LanServer> getLanServers() {
        return Collections.unmodifiableList(this.listOfLanServers);
    }

    public synchronized void func_77551_a(String p_77551_1_, InetAddress p_77551_2_) {
        String s = ThreadLanServerPing.getMotdFromPingResponse((String)p_77551_1_);
        String s1 = ThreadLanServerPing.getAdFromPingResponse((String)p_77551_1_);
        if (s1 != null) {
            s1 = p_77551_2_.getHostAddress() + ":" + s1;
            boolean flag = false;
            for (LanServerDetector.LanServer lanserverdetector$lanserver : this.listOfLanServers) {
                if (!lanserverdetector$lanserver.getServerIpPort().equals((Object)s1)) continue;
                lanserverdetector$lanserver.updateLastSeen();
                flag = true;
                break;
            }
            if (!flag) {
                this.listOfLanServers.add((Object)new LanServerDetector.LanServer(s, s1));
                this.wasUpdated = true;
            }
        }
    }
}
