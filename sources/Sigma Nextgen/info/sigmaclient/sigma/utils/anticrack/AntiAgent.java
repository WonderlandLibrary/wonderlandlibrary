package info.sigmaclient.sigma.utils.anticrack;

import info.sigmaclient.sigma.SigmaNG;

public class AntiAgent {
    boolean h1 = false;
    boolean h2 = false;
    public void hook(){
        h1 = true;
    }
    public void hook2(){
        h2 = true;
    }
    public void doOneCheck(){
        h1 = false;
        h2 = false;
        hook();
        hook2();
        if(!h1 || !h2){
            SigmaNG.SigmaNG.verify.verify = false;
        }
    }
}
