package eu.trentorise.smartcampus.socialservice.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <i>EntityType</i> represents a type for the entity
 * 
 * @author mirko perillo
 * 
 */
public class EntityType {
	/**
	 * id
	 */
	private long id;
	/**
	 * name of the type
	 */
	private String name;
	/**
	 * concept relative to the entity type
	 */
	private Concept concept;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public static EntityType toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			EntityType type = new EntityType();
			type.setId(object.getLong("id"));
			type.setName(object.getString("name"));
			type.setConcept(Concept.toObject(object.getString("concept")));
			return type;
		} catch (JSONException e) {
			return null;
		}
	}
}
