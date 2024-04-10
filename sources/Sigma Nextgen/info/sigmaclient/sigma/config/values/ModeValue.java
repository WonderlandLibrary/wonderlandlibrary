package info.sigmaclient.sigma.config.values;

import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.premium.PremiumManager;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;

public class ModeValue extends Value<String> {
    public String[] values;
    public String[] premiums;
    public PartialTicksAnim[] hoverAnims;
    public void setPremiumModes(String[] premiums) {
        this.premiums = premiums;
    }

    @Override
    public void setValue(String value) {
        boolean pre = false;
        for(String str : premiums)
            if (str.equals(value)) {
                pre = true;
                break;
            }
        if(pre && !PremiumManager.isPremium){
            NotificationManager.notify("Premium", "Not yet available for free version");
            return;
        }
        super.setValue(value);
    }

    public ModeValue(String name, String mode, String[] modes){
        super(name);
        this.values = modes;
        this.hoverAnims = new PartialTicksAnim[this.values.length];
        premiums = new String[]{};
        for(int i = 0 ; i < this.values.length; i ++){
            this.hoverAnims[i] = new PartialTicksAnim(0);
        }
        this.setValue(modes[0]);
        for(String string : modes){
            if(mode.equals(string)) this.setValue(mode);
        }
    }
    public ModeValue(String name, String mode, Module[] modes){
        super(name);
        String[] strs = new String[modes.length];
        int i2 = 0;
        for(Module m : modes){
            strs[i2] = m.name;
            i2 ++;
        }
        this.values = strs;
        this.hoverAnims = new PartialTicksAnim[this.values.length];
        premiums = new String[]{};
        for(int i = 0 ; i < this.values.length; i ++){
            this.hoverAnims[i] = new PartialTicksAnim(0);
        }
        this.setValue(strs[0]);
        for(String string : strs){
            if(mode.equals(string)) this.setValue(mode);
        }
    }
    public boolean is(String mode){
        return this.getValue().equals(mode);
    }
}
