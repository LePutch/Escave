package info3.game.assets;

import java.util.Hashtable;

public class AssetServer {
	static AssetServer instance;
	static String baseDir = "src/main/resources/";

	boolean client;
	Hashtable<String, Asset> cache = new Hashtable<>();

	public static void init(boolean client) {
		AssetServer.instance = new AssetServer();
		AssetServer.instance.client = client;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Asset> T load(T request) {
		String path = request.getClass().getName() + "/" + request.getPath();
		AssetServer server = AssetServer.instance;
		if (server == null) {
			System.out.println("[WARN] Can't load asset '" + path + "' because server is not ready");
			return null;
		}

		T asset = (T) server.cache.get(path);
		if (asset == null) {
			if (server.client) {
				request.load();
			}
			server.cache.put(path, request);
			return request;
		}
		// TODO: find a better solution to avoid this special case
		if (asset instanceof AnimatedImage) {
			return (T) ((AnimatedImage) asset).clone();
		} else {
			return asset;
		}
	}
}
