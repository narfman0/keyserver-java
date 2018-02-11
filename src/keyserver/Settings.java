package keyserver;

public class Settings {
	public enum ServerType { UDP, TCP }
	public static final ServerType TYPE = ServerType.UDP;
	public static final int PORT = 6789;
}
