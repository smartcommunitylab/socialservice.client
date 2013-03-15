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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <i>SharedContent</i> defines a shared resource
 * 
 * @author mirko perillo
 * 
 */
public class SharedContent implements Comparable<SharedContent> {
	/**
	 * social entity id
	 */
	private long entityId;
	/**
	 * type of resource core entityTypes : social, community, event, experience,
	 * computer file, journey, person, location, portfolio, narrative
	 */
	private String entityType;
	/**
	 * name of the resource
	 */
	private String title;
	/**
	 * a set of tags
	 */
	private String[] tags;
	/**
	 * id of the creator of the resource
	 */
	private long ownerId;

	private Date creationDate;

	private MinimalProfile user;

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public MinimalProfile getUser() {
		return user;
	}

	public void setUser(MinimalProfile user) {
		this.user = user;
	}

	@Override
	public int compareTo(SharedContent o) {
		return (creationDate != null) ? creationDate.compareTo(o
				.getCreationDate()) : (o.getCreationDate() != null) ? -1 : 0;
	}

	public static SharedContent toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			SharedContent content = new SharedContent();
			content.setEntityId(object.getLong("entityId"));
			content.setEntityType(object.getString("entityType"));
			content.setCreationDate(new Date(object.getLong("creationDate")));
			content.setOwnerId(object.getLong("ownerId"));

			boolean isNull = object.isNull("tags");
			if (!isNull) {
				List<String> elements = new ArrayList<String>();
				for (int i = 0; object.getJSONArray("tags").optString(i)
						.length() > 0; i++) {
					elements.add(object.getJSONArray("tags").getString(i));
				}
				content.setTags(elements.toArray(new String[1]));
			}
			content.setTitle(object.getString("title"));
			content.setUser(MinimalProfile.toObject(object.getString("user")));
			return content;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<SharedContent> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<SharedContent> listElements = new ArrayList<SharedContent>();
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
}
