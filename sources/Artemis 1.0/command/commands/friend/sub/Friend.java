package artemis.command.friend.sub;

public class Friend {

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Friend(String name, String alias) {
		super();
		this.name = name;
		this.alias = alias;
	}
	public String name;
	public String alias;
	
}
