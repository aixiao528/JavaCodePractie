package proxy;

public interface DataAccess {

	Object read();
	void write(Object o);
}
