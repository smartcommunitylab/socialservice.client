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
 * 
 * Minimal information for a user visualization
 * 
 * @author Mirko Perillo
 * 
 */
public class MinimalProfile extends SCUser {

	/**
	 * name
	 */
	private String name;
	/**
	 * surname
	 */
	private String surname;
	/**
	 * other information about the user
	 */
	private UserInformation userInformation;
	/**
	 * profile image URL
	 */
	private String pictureUrl;
	/**
	 * flag if user is known
	 */
	private boolean known;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public UserInformation getUserInformation() {
		return userInformation;
	}

	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public boolean isKnown() {
		return known;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public static MinimalProfile toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			MinimalProfile profile = new MinimalProfile();
			profile.setName(object.getString("name"));
			profile.setPictureUrl(object.getString("pictureUrl"));
			profile.setSocialId(object.getLong("socialId"));
			profile.setSurname(object.getString("surname"));
			profile.setUserId(object.getLong("userId"));
			profile.setUserInformation(UserInformation.toObject(object
					.getString("userInformation")));
			return profile;
		} catch (JSONException e) {
			return null;
		}
	}

	public static List<MinimalProfile> toList(String json) {
		try {
			JSONArray array = new JSONArray(json);
			List<MinimalProfile> listElements = new ArrayList<MinimalProfile>();
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

	public static String toJson(MinimalProfile m) {
		try {
			StringWriter writer = new StringWriter();
			writer.write("{");
			writer.write(JSONObject.quote("userId") + ":"
					+ JsonUtils.toJson(m.getUserId()) + ",");
			writer.write(JSONObject.quote("socialId") + ":"
					+ JsonUtils.toJson(m.getSocialId()) + ",");
			writer.write(JSONObject.quote("name")
					+ ":"
					+ JSONObject.quote((m.getName() != null ? m.getName() : ""))
					+ ",");
			writer.write(JSONObject.quote("surname")
					+ ":"
					+ JSONObject.quote((m.getSurname() != null ? m.getSurname()
							: "")) + ",");
			writer.write(JSONObject.quote("pictureUrl") + ":"
					+ JsonUtils.toJson(m.getPictureUrl()) + ",");
			writer.write(JSONObject.quote("known") + ":"
					+ JsonUtils.toJson(m.isKnown()) + ",");
			writer.write(JSONObject.quote("userInformation") + ":"
					+ UserInformation.toJson(m.getUserInformation()));
			writer.write("}");
			return writer.toString();
		} catch (NullPointerException e) {
			return null;
		}

	}
}
