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
 * <i>UserInformation</i> describes other informations of a user
 * 
 * @author mirko perillo
 * 
 */
public class UserInformation {
	private String faculty;
	private String position;

	public String getFaculty() {
		return faculty;
	}

	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public static UserInformation toObject(String json) {
		try {
			JSONObject object = new JSONObject(json);
			UserInformation info = new UserInformation();
			info.setFaculty(object.getString("faculty"));
			info.setPosition(object.getString("position"));
			return info;
		} catch (JSONException e) {
			return null;
		}

	}
}
