package wtf.diablo.utils.animations;

public class Animation {

    //Start Time
    private long startTime = 0;

    //How many pixels to move
    private int amount;

    //Speed is how fast it should go (times normal speed by) eg: 0.5 or 2.0
    private double speed;

    //getPercent of finish
    private double percent;

    //Reverse the animation
    private boolean reversed;

    //Value [X]
    private double value;

    //Called to restart/start the animation
    public void start(){
        this.startTime = System.currentTimeMillis();
    }

    //Call this always
    public void updateAnimation(){
        double percentage = getPercentage(amount / speed,startTime);
        if(reversed)
            percentage = 100 - percentage;
        value = getDoubleFromPercentage(percentage,amount);
        this.percent = percentage;
    }

    /*
        Implemented from my old animation Utils
    */

    public static double getPercentage(double animationLengthMS, long startSysMS){
        double time = System.currentTimeMillis() - startSysMS;
        double result = (time / animationLengthMS) * 100;
        return result <= 100 ? result : 100;
    }

    public boolean hasFinished(){
        return reversed ? getPercent() == 0 : getPercent() == 100 || startTime == 0;
    }

    public static double getDoubleFromPercentage(double percentage, double size) {
        return (size / 100 * percentage);
    }

    public double getValue() {
        return value;
    }

    public double getSpeed(){
        return speed;
    }

    public int getAmount(){
        return amount;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPercent() {
        return percent;
    }

    public void setReverse(boolean reverse) {
        this.reversed = reverse;
    }

    public boolean isReversed() {
        return reversed;
    }
}
