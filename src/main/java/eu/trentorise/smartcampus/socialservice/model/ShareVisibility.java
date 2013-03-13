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
 * <i>ShareVisibility</i> represents the sharing visibility options.
 * 
 * @author mirko perillo
 * 
 */
public class ShareVisibility {

	/**
	 * set of user ids which share the resource with
	 */
	private List<Long> userIds;
	/**
	 * set of group ids which share the resource with
	 */
	private List<Long> groupIds;
	/**
	 * set of community ids which share the resource with
	 */
	private List<Long> communityIds;

	/**
	 * flag to share resource with all known users
	 */
	private boolean allKnownUsers;
	/**
	 * flag to share resource with all known communities
	 */
	private boolean allKnownCommunities;
	/**
	 * flag to share resource public with all system users
	 */
	private boolean allUsers;
	/**
	 * flag to share resource public with all system communities
	 */
	private boolean allCommunities;

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(List<Long> groupIds) {
		this.groupIds = groupIds;
	}

	public List<Long> getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}

	public boolean isAllKnownUsers() {
		return allKnownUsers;
	}

	public void setAllKnownUsers(boolean allKnownUsers) {
		this.allKnownUsers = allKnownUsers;
	}

	public boolean isAllCommunities() {
		return allCommunities;
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

	public void setAllCommunities(boolean allCommunities) {
		this.allCommunities = allCommunities;
	}

	public static ShareVisibility toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			ShareVisibility visibility = new ShareVisibility();
			visibility.setAllCommunities(object.getBoolean("allCommunities"));
			visibility.setAllKnownCommunities(object
					.getBoolean("allKnownCommunities"));
			visibility.setAllKnownUsers(object.getBoolean("allKnownUsers"));
			visibility.setAllUsers(object.getBoolean("allUsers"));

			boolean isNull = object.isNull("communityIds");
			if (!isNull) {
				List<Long> elements = new ArrayList<Long>();
				for (int i = 0; object.getJSONArray("communityIds")
						.optString(i).length() > 0; i++) {
					elements.add(object.getJSONArray("communityIds").getLong(i));
				}
				visibility.setCommunityIds(elements);
			}

			isNull = object.isNull("groupIds");
			if (!isNull) {
				List<Long> elements = new ArrayList<Long>();
				for (int i = 0; object.getJSONArray("groupIds").optString(i)
						.length() > 0; i++) {
					elements.add(object.getJSONArray("groupIds").getLong(i));
				}
				visibility.setGroupIds(elements);
			}

			isNull = object.isNull("userIds");
			if (!isNull) {
				List<Long> elements = new ArrayList<Long>();
				for (int i = 0; object.getJSONArray("userIds").optString(i)
						.length() > 0; i++) {
					elements.add(object.getJSONArray("userIds").getLong(i));
				}
				visibility.setUserIds(elements);
			}
			return visibility;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<ShareVisibility> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<ShareVisibility> listElements = new ArrayList<ShareVisibility>();
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

	public static String toJson(ShareVisibility visibility) {
		StringWriter writer = new StringWriter();
		writer.write("{");
		writer.write("\"allCommunities\":" + visibility.isAllCommunities()
				+ ",");
		writer.write("\"allKnownCommunities\":"
				+ visibility.isAllKnownCommunities() + ",");
		writer.write("\"allKnownUsers\":" + visibility.isAllKnownUsers() + ",");
		writer.write("\"allUsers\":" + visibility.isAllUsers() + ",");
		writer.write("\"communityIds\":[");
		if (visibility.getCommunityIds() != null) {
			boolean isFirst = true;
			for (Long i : visibility.getCommunityIds()) {
				if (!isFirst) {
					writer.write(",");
				}
				writer.write(i.toString());
				isFirst = false;
			}
		}
		writer.write("],");

		writer.write("\"groupIds\":[");
		if (visibility.getGroupIds() != null) {
			boolean isFirst = true;
			for (Long i : visibility.getGroupIds()) {
				if (!isFirst) {
					writer.write(",");
				}
				writer.write(i.toString());
				isFirst = false;
			}
		}
		writer.write("],");

		writer.write("\"userIds\":[");
		if (visibility.getUserIds() != null) {
			boolean isFirst = true;
			for (Long i : visibility.getUserIds()) {
				if (!isFirst) {
					writer.write(",");
				}
				writer.write(i.toString());
				isFirst = false;
			}
		}
		writer.write("]");
		writer.write("}");

		return writer.toString();
	}
}
