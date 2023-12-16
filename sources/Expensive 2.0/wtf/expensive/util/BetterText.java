package wtf.expensive.util;

import wtf.expensive.util.misc.TimerUtil;

import java.util.ArrayList;
import java.util.List;

public class BetterText {

    private List<String> texts = new ArrayList<>();


    public String output = "";

    private int delay = 0;

    public BetterText(List<String> texts, int delay) {
        this.texts = texts;
        this.delay = delay;
        start();
    }


    private TimerUtil timerUtil = new TimerUtil();

    public void start() {
        new Thread(() -> {
            try {
            int index = 0;
                while (true) {
                    for (int i = 0; i < texts.get(index).length(); i++) {
                        output += texts.get(index).charAt(i);
                        Thread.sleep(100);
                    }
                    Thread.sleep(delay);
                    for (int i = output.length(); i >= 0; i--) {
                        output = output.substring(0, i);
                        Thread.sleep(60);
                    }
                    if (index >= texts.size() - 1) {
                        index = 0;
                    }
                    index += 1;
                    Thread.sleep(400);
                }
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
