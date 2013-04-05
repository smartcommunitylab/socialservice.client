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
 * <i>Topic</i> represents an argument of interest that a user follows from some
 * sources
 * 
 * @author mirko perillo
 * 
 */
public class Topic {

	public static enum TopicStatus {
		ACTIVE(1), SUSPENDED(2);

		private int value;

		private TopicStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * id of the topic
	 */
	private long socialId;
	/**
	 * name of the topic
	 */
	private String name;
	/**
	 * status of the topic
	 */
	private int status;
	/**
	 * set of semantic concept binded with the topic
	 */
	private List<Concept> concepts;
	/**
	 * set of semantic keyword binded with the topic
	 */
	private List<String> keywords;
	/**
	 * set of users sources of content of the topic
	 */
	private List<MinimalProfile> users;
	/**
	 * set of groups sources of content of the topic
	 */
	private List<Group> groups;
	/**
	 * set of groups sources of content of the topic
	 */
	private List<Community> communities;
	/**
	 * set of accepted types of contents for the topic
	 */
	private List<String> contentTypes;

	/**
	 * flag to receive news from all known users
	 */
	private boolean allKnownUsers;
	/**
	 * flag to receive news from all known communities
	 */
	private boolean allKnownCommunities;
	/**
	 * flag to receive news from all system users
	 */
	private boolean allUsers;
	/**
	 * flag to receive news from all system communities
	 */
	private boolean allCommunities;
	/**
	 * list of resources in relation with the topic
	 */
	private List<Concept> entities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Concept> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}

	public List<MinimalProfile> getUsers() {
		return users;
	}

	public void setUsers(List<MinimalProfile> users) {
		this.users = users;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Community> getCommunities() {
		return communities;
	}

	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}

	public List<String> getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(List<String> contentTypes) {
		this.contentTypes = contentTypes;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public long getSocialId() {
		return socialId;
	}

	public void setSocialId(long socialId) {
		this.socialId = socialId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getId() {
		return "" + getSocialId();
	}

	public boolean isAllKnownUsers() {
		return allKnownUsers;
	}

	public void setAllKnownUsers(boolean allKnownUsers) {
		this.allKnownUsers = allKnownUsers;
	}

	public boolean isAllKnownCommunities() {
		return allKnownCommunities;
	}

	public void setAllKnownCommunities(boolean allKnownCommunities) {
		this.allKnownCommunities = allKnownCommunities;
	}

	public boolean isAllUsers() {
		return allUsers;
	}

	public void setAllUsers(boolean allUsers) {
		this.allUsers = allUsers;
	}

	public boolean isAllCommunities() {
		return allCommunities;
	}

	public void setAllCommunities(boolean allCommunities) {
		this.allCommunities = allCommunities;
	}

	public List<Concept> getEntities() {
		return entities;
	}

	public void setEntities(List<Concept> entities) {
		this.entities = entities;
	}

	public static Topic toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			Topic topic = new Topic();
			topic.setAllCommunities(object.getBoolean("allCommunities"));
			topic.setAllKnownCommunities(object
					.getBoolean("allKnownCommunities"));
			topic.setAllKnownUsers(object.getBoolean("allKnownUsers"));
			topic.setAllUsers(object.getBoolean("allUsers"));
			topic.setCommunities(Community.toList(object
					.getString("communities")));
			topic.setConcepts(Concept.toList(object.getString("concepts")));
			boolean isNull = object.isNull("contentTypes");
			if (!isNull) {
				List<String> elements = new ArrayList<String>();
				for (int i = 0; object.getJSONArray("contentTypes")
						.optString(i).length() > 0; i++) {
					elements.add(object.getJSONArray("contentTypes").getString(
							i));
				}
				topic.setContentTypes(elements);
			}

			isNull = object.isNull("keywords");
			if (!isNull) {
				List<String> elements = new ArrayList<String>();
				for (int i = 0; object.getJSONArray("keywords").optString(i)
						.length() > 0; i++) {
					elements.add(object.getJSONArray("keywords").getString(i));
				}
				topic.setKeywords(elements);
			}

			topic.setEntities(Concept.toList(object.getString("entities")));
			topic.setGroups(Group.toList(object.getString("groups")));
			topic.setName(object.getString("name"));
			topic.setSocialId(object.getLong("socialId"));
			topic.setStatus(object.getInt("status"));
			topic.setUsers(MinimalProfile.toList(object.getString("users")));
			return topic;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<Topic> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<Topic> listElements = new ArrayList<Topic>();
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

	public static String toJson(Topic t) {
		try {
			StringWriter writer = new StringWriter();
			writer.write("{");
			writer.write(JSONObject.quote("communities") + ":"
					+ JsonUtils.toJson(t.getCommunities()) + ",");
			writer.write(JSONObject.quote("concepts") + ":"
					+ JsonUtils.toJson(t.getConcepts()) + ",");
			writer.write(JSONObject.quote("contentTypes") + ":"
					+ JsonUtils.toJson(t.getContentTypes()) + ",");
			writer.write(JSONObject.quote("entities") + ":"
					+ JsonUtils.toJson(t.getEntities()) + ",");
			writer.write(JSONObject.quote("groups") + ":"
					+ JsonUtils.toJson(t.getGroups()) + ",");
			writer.write(JSONObject.quote("id") + ":"
					+ JsonUtils.toJson(t.getId()) + ",");
			writer.write(JSONObject.quote("keywords") + ":"
					+ JsonUtils.toJson(t.getKeywords()) + ",");
			writer.write(JSONObject.quote("name") + ":"
					+ JsonUtils.toJson(t.getName()) + ",");
			writer.write(JSONObject.quote("socialId") + ":"
					+ JsonUtils.toJson(t.getSocialId()) + ",");
			writer.write(JSONObject.quote("status") + ":"
					+ JsonUtils.toJson(t.getStatus()) + ",");
			writer.write(JSONObject.quote("users") + ":"
					+ JsonUtils.toJson(t.getUsers()));
			writer.write("}");
			return writer.toString();
		} catch (NullPointerException e) {
			return null;
		}
	}

}
