package info.sigmaclient.sigma.utils.anticrack;

import info.sigmaclient.sigma.SigmaNG;

public class MiscAntiCrack {
    public boolean verify = true;
    public void verify(){
        String rice = SigmaNG.SigmaNG.getClass().getName();
//        String sigma = SigmaNG.SIGMA.getClass().getName();
        try {
            Class a = Class.forName(rice);
//            Class b = Class.forName(sigma);
        } catch (ClassNotFoundException e) {
            verify = false;
            return;
        }
        if(SigmaNG.getClientName().startsWith("Sigma-nextgen") && SigmaNG.getClientName().endsWith("Sigma-nextgen")){
        }else{
            verify = false;
        }
    }
}
