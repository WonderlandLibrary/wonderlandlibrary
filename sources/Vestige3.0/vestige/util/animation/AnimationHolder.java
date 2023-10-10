package vestige.util.animation;

public class AnimationHolder<T> extends Animation {

    private T t;

    public AnimationHolder(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

}