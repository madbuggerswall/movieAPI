import com.google.gson.Gson;

// All objects representing Firebase documents will extend this Data class.
public abstract class Data {
	protected String id;
	private static Gson gson = new Gson();

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String toJSON() {
		return gson.toJson(this);
	}

	public static <T> T fromJSON(String json, Class<T> valueType) {
		return gson.fromJson(json, valueType);
	}
}