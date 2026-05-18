package thread;

public class shutdownHook {

	public static void main(String [] arg) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("end of JVM");
			}
		}));
	}
}
