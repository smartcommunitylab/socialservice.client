/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.socialservice.model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.trentorise.smartcampus.network.JsonUtils;

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
	 * type of the entity. It MUST be setted name or id field or either
	 */
	private String type;

	private long typeId;
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
			e.setTypeId(object.getLong("typeId"));
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
		try {
			StringWriter writer = new StringWriter();
			writer.write("{");
			writer.write(JSONObject.quote("id") + ":"
					+ JsonUtils.toJson(e.getId()) + ",");
			writer.write(JSONObject.quote("name") + ":"
					+ JsonUtils.toJson(e.getName()) + ",");
			writer.write(JSONObject.quote("description") + ":"
					+ JsonUtils.toJson(e.getDescription()) + ",");
			writer.write(JSONObject.quote("creatorId") + ":"
					+ JsonUtils.toJson(e.getCreatorId()) + ",");
			writer.write(JSONObject.quote("type") + ":"
					+ JsonUtils.toJson(e.getType()) + ",");
			writer.write(JSONObject.quote("typeId") + ":"
					+ JsonUtils.toJson(e.getTypeId()) + ",");
			writer.write(JSONObject.quote("relations") + ":"
					+ JsonUtils.toJson(e.getRelations()) + ",");
			writer.write(JSONObject.quote("tags") + ":"
					+ JsonUtils.toJson(e.getTags()));
			writer.write("}");
			return writer.toString();
		} catch (NullPointerException npe) {
			return null;
		}
	}

	public long getTypeId() {
		return typeId;
	}

	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
}
