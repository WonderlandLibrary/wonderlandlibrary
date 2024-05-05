package Velo.api.Util.Other;

import java.util.ArrayList;
import java.util.Random;

public class RandomObjectArraylist<E> extends ArrayList<E> {

    public RandomObjectArraylist(E... things) {

        for (E thing: things) {
            this.add(thing);
        }

    }

    public E getRandomObject() {

        if (this.size() == 0) {
            return null;
        }else {
            return this.get(new Random().nextInt(this.size()));
        }

    }

}