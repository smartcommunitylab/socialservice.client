package eu.trentorise.smartcampus.socialservice.model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <i>Entity</i> represents an entity in the system. Every object present in the
 * system is an entity: a portfolio, a event, a story and so on.
 * 
 * @author mirko perillo
 * 
 */
public class Entity {
	/**
	 * id
	 */
	private long id;
	/**
	 * id of creator of entity
	 */
	private long creatorId;
	/**
	 * type of the entity. Core entity types are: social, community, event,
	 * experience, computer file, journey, person, location, portfolio,
	 * narrative
	 */
	private String type;
	/**
	 * name of the entity
	 */
	private String name;
	/**
	 * description
	 */
	private String description;
	/**
	 * tags binded with the entity
	 */
	private List<Concept> tags;
	/**
	 * entity ids in relation with this entity
	 */
	private List<Long> relations;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Concept> getTags() {
		return tags;
	}

	public void setTags(List<Concept> tags) {
		this.tags = tags;
	}

	public List<Long> getRelations() {
		return relations;
	}

	public void setRelations(List<Long> relations) {
		this.relations = relations;
	}

	public static Entity toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Entity e = new Entity();
			e.setCreatorId(object.getLong("creatorId"));
			e.setDescription(object.getString("description"));
			e.setId(object.getLong("id"));
			e.setName(object.getString("name"));
			boolean isNull = object.isNull("relations");
			if (!isNull) {
				List<Long> elements = new ArrayList<Long>();
				for (int i = 0; object.getJSONArray("relations").optString(i)
						.length() > 0; i++) {
					elements.add(object.getJSONArray("relations").getLong(i));
				}
				e.setRelations(elements);
			}
			e.setTags(Concept.toList(object.getString("tags")));

			e.setType(object.getString("type"));
			return e;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<Entity> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<Entity> listElements = new ArrayList<Entity>();
			for (int i = 0; array.optString(i).length() > 0; i++) {
				String subElement = array.getString(i);
				if (subElement != null) {
					listElements.add(toObject(subElement));
				}
			}
			return listElements;
		} catch (JSONException e) {
			return null;
		}
	}

	public static String toJson(Entity e) {
		StringWriter writer = new StringWriter();
		writer.write("{");
		writer.write("\"id\":" + e.getId() + ",");
		writer.write("\"name\":\"" + e.getName() + "\",");
		writer.write("\"description\":\"" + e.getDescription() + "\",");
		writer.write("\"creatorId\":" + e.getCreatorId() + ",");
		writer.write("\"type\":\"" + e.getType() + "\",");
		writer.write("\"relations\":[");
		if (e.getRelations() != null) {
			boolean isFirst = true;
			for (Long i : e.getRelations()) {
				if (!isFirst) {
					writer.write(",");
				}
				writer.write(i.toString());
				isFirst = false;
			}
		}
		writer.write("],");
		writer.write("\"tags\":[");
		if (e.getTags() != null) {
			boolean isFirst = true;
			for (Concept c : e.getTags()) {
				if (!isFirst) {
					writer.write(",");
				}
				writer.write(Concept.toJson(c));
				isFirst = false;
			}
		}
		writer.write("]");
		writer.write("}");
		return writer.toString();
	}
}
