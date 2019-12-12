import com.google.gson.Gson;

// All objects representing Firebase documents will extend this Data class.
public abstract class Data {
	protected String id;
	protected static Gson gson = new Gson();

	protected String getID() {
		return id;
	}

	protected void setID(String id) {
		this.id = id;
	}

	protected String toJSON() {
		return gson.toJson(this);
	}

	protected static <T> T fromJSON(String json, Class<T> valueType) {
		return gson.fromJson(json, valueType);
	}
}