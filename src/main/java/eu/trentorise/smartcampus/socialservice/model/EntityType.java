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
