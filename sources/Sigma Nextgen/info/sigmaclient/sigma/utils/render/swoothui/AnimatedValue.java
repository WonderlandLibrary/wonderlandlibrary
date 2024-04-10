package info.sigmaclient.sigma.utils.render.swoothui;

public abstract class AnimatedValue<T> {
    public T from;
    public T to;

    private T current;

    private Target target;

    public AnimatedValue(final T from, final T to) {
        this.from = from;
        this.to = to;
        this.target = Target.FROM;
        this.current = this.from;
    }

    public void switchToFrom() {
        this.target = Target.FROM;
    }

    public void switchToTo() {
        this.target = Target.TO;
    }

    abstract protected T update(T current, Target target);

    public T getValue() {
        this.current = update(this.current, this.target);
        return this.current;
    }

    public enum Target {
        FROM, TO
    }
}
