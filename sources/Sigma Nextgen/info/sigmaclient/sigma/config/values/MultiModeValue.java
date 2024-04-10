package info.sigmaclient.sigma.config.values;

public class MultiModeValue extends Value<BooleanValue[]> {
    public MultiModeValue(String name, BooleanValue[] modes){
        super(name);
        this.setValue(modes);
    }
    public BooleanValue get(String mode){
        for(BooleanValue booleanValue : this.getValue()){
            if(booleanValue.name.equals(mode)){
                return booleanValue;
            }
        }
        return new BooleanValue("", false);
    }
}
