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
 * <i>Group</i> represents a social group
 * 
 * @author mirko perillo
 * 
 */
public class Group {

	/**
	 * id of the group
	 */
	private long socialId;
	/**
	 * name of the group
	 */
	private String name;
	/**
	 * users belong to the group
	 */
	private List<MinimalProfile> users;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MinimalProfile> getUsers() {
		return users;
	}

	public void setUsers(List<MinimalProfile> users) {
		this.users = users;
	}

	public long getSocialId() {
		return socialId;
	}

	public void setSocialId(long id) {
		this.socialId = id;
	}

	public String getId() {
		return "" + getSocialId();
	}

	public static Group toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Group group = new Group();
			group.setName(object.getString("name"));
			group.setSocialId(object.getLong("socialId"));
			group.setUsers(MinimalProfile.toList(object.getString("users")));
			return group;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<Group> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<Group> listElements = new ArrayList<Group>();
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

	public static String toJson(Group g) {
		try {
			StringWriter writer = new StringWriter();
			writer.write("{");
			writer.write(JSONObject.quote("name") + ":"
					+ JsonUtils.toJson(g.getName()) + ",");
			writer.write(JSONObject.quote("socialId") + ":"
					+ JsonUtils.toJson(g.getSocialId()) + ",");

			writer.write(JSONObject.quote("users") + ":"
					+ JsonUtils.toJson(g.getUsers()));
			writer.write("}");
			return writer.toString();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
