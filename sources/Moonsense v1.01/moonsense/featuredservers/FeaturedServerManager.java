// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.featuredservers;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import moonsense.utils.RequestUtils;
import java.net.URL;
import java.util.ArrayList;

public class FeaturedServerManager
{
    ArrayList<FeaturedServer> featuredServers;
    String featuredServersURL;
    
    public FeaturedServerManager() {
        this.featuredServers = new ArrayList<FeaturedServer>();
        this.featuredServersURL = "https://streamlined.hacker57.repl.co";
    }
    
    private void initFeaturedServers() {
        try {
            final String res = RequestUtils.get(new URL(this.featuredServersURL), "Mozilla/5.0");
            String[] split;
            for (int length = (split = res.split("\n")).length, i = 0; i < length; ++i) {
                final String line = split[i];
                if (!line.startsWith("#")) {
                    try {
                        this.featuredServers.add(new FeaturedServer(line.split(":")[1], line.split(":")[0]));
                    }
                    catch (Exception ex) {}
                }
            }
            LogManager.getLogger().info("Loaded featured servers! " + this.featuredServers.size());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public FeaturedServerManager init() {
        this.initFeaturedServers();
        return this;
    }
    
    public ArrayList<FeaturedServer> getFeaturedServers() {
        return this.featuredServers;
    }
}
