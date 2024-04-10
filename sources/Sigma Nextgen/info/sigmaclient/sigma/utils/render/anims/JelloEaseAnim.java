package info.sigmaclient.sigma.utils.render.anims;

public class JelloEaseAnim {
    // Author: IamFrozenMilk
    PartialTicksAnim alpha = new PartialTicksAnim(0);
    PartialTicksAnim scale = new PartialTicksAnim(80);
//    Smooth2DAnim alpha_scale = new Smooth2DAnim(0, 80);
    /*
    @Params speed
    @Output: 0 - 1
     */
    public void anim(int speed, boolean back){
        if(back) {
            // X: alpha Y : scale
            alpha.interpolate(0, speed);
            scale.interpolate(80f, (speed * 0.5f));
        }else{
            // X: alpha Y : scale
            alpha.interpolate(100F, (speed));
            scale.interpolate(100F, (speed * 0.5f));
        }
    }
    public void resetInit(){
    }
    public void reset(){
        alpha.setValue(0);
        scale.setValue(0);
    }
    public boolean isDone(){
        return alpha.getValue() == 0 || alpha.getValue() == 1;
    }
    public boolean isEnd(){
        return alpha.getValueNoTrans() == 0;
    }
    public float getAlpha(){
        return alpha.getValue() / 100f;
    }
    public float getScale(){
        return scale.getValue() / 100f;
    }
}
