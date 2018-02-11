package keyserver;

public class Settings {
	public enum ServerType { UDP, UDP_MULTI, TCP }
	public static final ServerType TYPE = ServerType.UDP_MULTI;
	public static final int PORT = 5000;
	public static final int SERVER_PORTS = 16;
}
