package requirement1.models;

import api.ripley.Ripley;

public class API {

	private static API api;
	
	private Ripley ripley;
	
	// Singleton
	public static Ripley getRipleyInstance() {
		if (api == null) {
			api = new API();
		}
		return api.ripley;
	}
	
	private API() {
		ripley = new Ripley("90tLI3CWsdCyVD6ql2OMtA==", "lBgm4pRo9QDVqL46EnH7ew==");
	}

}
