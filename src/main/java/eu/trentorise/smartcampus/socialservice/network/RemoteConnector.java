/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either   express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package eu.trentorise.smartcampus.socialservice.network;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.socialservice.model.Community;
import eu.trentorise.smartcampus.socialservice.model.Group;
import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;
import eu.trentorise.smartcampus.socialservice.model.SharedContent;
import eu.trentorise.smartcampus.socialservice.model.Topic;

/**
 * Utility class to perform REST service invocation
 * 
 * @author raman
 * 
 */
public class RemoteConnector {

	/** */
	private static final String RH_ACCEPT = "Accept";
	/** */
	private static final String RH_AUTH_TOKEN = "AUTH_TOKEN";
	/** Service path */
	private static final String SERVICE = "/smartcampus.vas.community-manager.web/";
	/** Basic profile path */
	private static final String GROUP = "eu.trentorise.smartcampus.cm.model.Group/";

	private static final String COMMUNITY = "eu.trentorise.smartcampus.cm.model.Community/";

	private static final String TOPIC = "eu.trentorise.smartcampus.cm.model.Topic/";

	private static final String VISIBILITY = "assignments/";

	private static final String MY_CONTENTS = "content/";

	private static final String SHARED_CONTENT = "sharedcontent/";

	private static final String SHARE = "share/";

	private static final String UNSHARE = "unshare/";

	/** Timeout (in ms) we specify for each http request */
	public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params,
				HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		return httpClient;
	}

	public static List<Group> getGroups(String host, String token)
			throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + GROUP, token, null, null, null);
		return Group.toList(json);
	}

	public static Group getGroup(String host, String token, String groupId)
			throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + GROUP + groupId, token, null,
				null, null);
		return Group.toObject(json);
	}

	public static Community getCommunity(String host, String token,
			String communityId) throws SecurityException,
			SocialServiceException {
		String json = getJSON(host, SERVICE + COMMUNITY + communityId, token,
				null, null, null);
		return Community.toObject(json);
	}

	public static List<Community> getCommunities(String host, String token)
			throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + COMMUNITY, token, null, null,
				null);
		return Community.toList(json);
	}

	public static List<Topic> getTopics(String host, String token)
			throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + TOPIC, token, null, null, null);
		return Topic.toList(json);
	}

	public static Topic getTopic(String host, String token, String topicId)
			throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + TOPIC + topicId, token, null,
				null, null);
		return Topic.toObject(json);
	}

	public static ShareVisibility getShareVisibility(String host, String token,
			long entityId) throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + VISIBILITY + entityId, token,
				null, null, null);
		return ShareVisibility.toObject(json);
	}

	public static List<SharedContent> getMyContents(String host, String token,
			Integer position, Integer size, String type)
			throws SecurityException, SocialServiceException {
		String json = getJSON(host, SERVICE + MY_CONTENTS, token, position,
				size, type);
		return SharedContent.toList(json);
	}

	public static List<SharedContent> getSharedContents(String host,
			String token, ShareVisibility shareVisibility, Integer position,
			Integer size, String type) throws SecurityException,
			SocialServiceException {
		String json = postJSON(host, SERVICE + SHARED_CONTENT,
				ShareVisibility.toJson(shareVisibility), token, position, size,
				type);
		return SharedContent.toList(json);
	}

	public static boolean share(String host, String token,
			ShareOperation shareOperation) throws SecurityException,
			SocialServiceException {
		String json = postJSON(host, SERVICE + SHARE,
				ShareOperation.toJson(shareOperation), token, null, null, null);
		return new Boolean(json);
	}

	public static boolean unshare(String host, String token, long entityId)
			throws SecurityException, SocialServiceException {
		String json = putJSON(host, SERVICE + UNSHARE + entityId, "", token);
		return new Boolean(json);
	}

	private static String getJSON(String host, String service, String token,
			Integer position, Integer size, String type)
			throws SecurityException, SocialServiceException {
		final HttpResponse resp;
		try {
			String queryString = "?position="
					+ (position != null ? position : -1);

			queryString += "&size=" + (size != null ? size : -1);

			queryString += "&type=" + (type != null ? type : "");

			final HttpGet get = new HttpGet(host + service + queryString);
			get.setHeader(RH_ACCEPT, "application/json");
			get.setHeader(RH_AUTH_TOKEN, token);

			resp = getHttpClient().execute(get);
			String response = EntityUtils.toString(resp.getEntity());
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				throw new SecurityException();
			}
			throw new SocialServiceException("Error validating "
					+ resp.getStatusLine());
		} catch (ClientProtocolException e) {
			throw new SocialServiceException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new SocialServiceException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SocialServiceException(e.getMessage(), e);
		}
	}

	private static String postJSON(String host, String service, String body,
			String token, Integer position, Integer size, String type)
			throws SecurityException, SocialServiceException {

		String queryString = "?position=" + (position != null ? position : -1);

		queryString += "&size=" + (size != null ? size : -1);

		queryString += "&type=" + (type != null ? type : "");
		final HttpResponse resp;
		final HttpPost post = new HttpPost(host + service + queryString);

		post.setHeader(RH_ACCEPT, "application/json");
		post.setHeader(RH_AUTH_TOKEN, token);

		try {
			StringEntity input = new StringEntity(body);
			input.setContentType("application/json");
			post.setEntity(input);

			resp = getHttpClient().execute(post);
			String response = EntityUtils.toString(resp.getEntity());
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				throw new SecurityException();
			}
			throw new SocialServiceException("Error validating "
					+ resp.getStatusLine());
		} catch (ClientProtocolException e) {
			throw new SocialServiceException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new SocialServiceException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SocialServiceException(e.getMessage(), e);
		}
	}

	private static String putJSON(String host, String service, String body,
			String token) throws SecurityException, SocialServiceException {
		final HttpResponse resp;
		final HttpPut put = new HttpPut(host + service);

		put.setHeader(RH_ACCEPT, "application/json");
		put.setHeader(RH_AUTH_TOKEN, token);

		try {
			StringEntity input = new StringEntity(body);
			input.setContentType("application/json");
			put.setEntity(input);

			resp = getHttpClient().execute(put);
			String response = EntityUtils.toString(resp.getEntity());
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				throw new SecurityException();
			}
			throw new SocialServiceException("Error validating "
					+ resp.getStatusLine());
		} catch (final Exception e) {
			throw new SocialServiceException(e.getMessage(), e);
		}
	}

	private static String deleteJSON(String host, String service, String token)
			throws SecurityException, SocialServiceException {
		final HttpResponse resp;
		final HttpDelete delete = new HttpDelete(host + service);

		delete.setHeader(RH_ACCEPT, "application/json");
		delete.setHeader(RH_AUTH_TOKEN, token);

		try {
			resp = getHttpClient().execute(delete);
			String response = EntityUtils.toString(resp.getEntity());
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			}
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				throw new SecurityException();
			}
			throw new SocialServiceException("Error validating "
					+ resp.getStatusLine());
		} catch (ClientProtocolException e) {
			throw new SocialServiceException(e.getMessage(), e);
		} catch (ParseException e) {
			throw new SocialServiceException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SocialServiceException(e.getMessage(), e);
		}
	}

}
