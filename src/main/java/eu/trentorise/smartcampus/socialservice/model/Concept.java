/**
 *    Copyright 2012-2013 Trento RISE
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
 */

package eu.trentorise.smartcampus.socialservice.model;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <i>Concept</i> represents a semantic tag
 * 
 * @author mirko perillo
 * 
 */
public class Concept {

	private Long id;
	private String name;
	private String description;
	private String summary;

	public Concept(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Concept() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public static Concept toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Concept concept = new Concept();
			concept.setDescription(object.getString("description"));
			concept.setId(object.getLong("id"));
			concept.setName(object.getString("name"));
			concept.setSummary(object.getString("summary"));
			return concept;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<Concept> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<Concept> listElements = new ArrayList<Concept>();
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

	public static String toJson(Concept c) {
		StringWriter writer = new StringWriter();
		writer.write("{");
		writer.write("\"id\":" + c.getId() + ",");
		writer.write("\"description\":"
				+ JSONObject.quote((c.getDescription() != null ? c
						.getDescription() : "")) + ",");
		writer.write("\"name\":"
				+ JSONObject.quote((c.getName() != null ? c.getName() : ""))
				+ ",");
		writer.write("\"summary\":"
				+ JSONObject.quote((c.getSummary() != null ? c.getSummary()
						: "")));
		writer.write("}");
		return writer.toString();
	}
}
