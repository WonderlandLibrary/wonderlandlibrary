package summer.ui.clickui.component;

/**
 * @author : AmirCC
 * @created : 7:23 PM, 10/27/2020, Tuesday
 **/
public abstract class Parentable<O, P> extends Component<O> {

    private P parent;
    private boolean expand;

    public Parentable(O object, P parent, float staticX, float staticY, float width, float height, float offsetX, float offsetY) {
        super(object, staticX, staticY, width, height, offsetX, offsetY);
        this.parent = parent;
    }

    @Override
    public abstract void updatePosition(float x, float y);

    public P getParent() {
        return parent;
    }

    public void setParent(P parent) {
        this.parent = parent;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isVisible(){
        return true;
    }

}
