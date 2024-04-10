package info.sigmaclient.sigma.scripts;

public class ReMethod {
    public ReturnMethod method;
    public Param param = new Param();
    public ReMethod(ReturnMethod obj){
        this.method = obj;
    }
    public ReMethod addPrame(String s){
        param.addPram(s);
        return this;
    }
}
