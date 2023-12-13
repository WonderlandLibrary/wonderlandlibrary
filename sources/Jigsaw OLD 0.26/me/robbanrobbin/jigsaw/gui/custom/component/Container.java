package me.robbanrobbin.jigsaw.gui.custom.component;

public interface Container extends Component {

	public Component[] getChildren();

	public void add(Component component);

	public void positionChildren();

}
