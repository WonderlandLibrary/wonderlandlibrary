package info.sigmaclient.sigma.config.alts;

import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;

public class Alt {
    public String token = "";
    public String name;
    public String uuid;
    public long time;
    public boolean offline;

    public float slideTrans;
    public Sigma5AnimationUtil animationUtil = new Sigma5AnimationUtil(500, 500);
    public Sigma5AnimationUtil select = new Sigma5AnimationUtil(500, 500);
    public Alt(String name, String uuid){
        offline = true;
        this.name = name;
        this.uuid = uuid;
        this.time = System.currentTimeMillis();
    }
    public Alt(String name, String uuid, String token){
        offline = false;
        this.name = name;
        this.uuid = uuid;
        this.token = token;
        this.time = System.currentTimeMillis();
    }
}
