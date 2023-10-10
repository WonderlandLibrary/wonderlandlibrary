package summer.base.manager;

public interface Command {

	boolean run(String[] args);

	String usage();

}
