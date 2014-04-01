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
package eu.trentorise.smartcampus.socialservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.socialservice.beans.Community;
import eu.trentorise.smartcampus.socialservice.beans.Entity;
import eu.trentorise.smartcampus.socialservice.beans.EntityType;
import eu.trentorise.smartcampus.socialservice.beans.Group;
import eu.trentorise.smartcampus.socialservice.beans.Limit;
import eu.trentorise.smartcampus.socialservice.beans.Result;

/**
 * Service APIs
 * 
 * @author mirko perillo
 * 
 */
public class SocialService {

	private String serviceUrl;

	public SocialService(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		if (!serviceUrl.endsWith("/")) {
			this.serviceUrl += '/';
		}
	}

	/**
	 * retrieves all user groups
	 * 
	 * @param token
	 *            user access token
	 * @return the list of user groups, or a empty list
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Group> getUserGroups(String token) throws SecurityException,
			SocialServiceException {
		try {
			String relativePath = "user/group";
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, null);
			return JsonUtils.toObjectList(extractResultData(json), Group.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Creates a group for authenticated user
	 * 
	 * @param token
	 *            user access token
	 * @param name
	 *            name of the new group
	 * @return group created
	 * @throws SocialServiceException
	 */
	public Group createUserGroup(String token, String name)
			throws SocialServiceException {
		try {
			Group group = new Group();
			group.setName(name);
			String relativePath = "user/group";
			String json = RemoteConnector.postJSON(serviceUrl, relativePath,
					JsonUtils.toJSON(group), token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Group.class);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Updates a group
	 * 
	 * @param token
	 *            user access token
	 * @param group
	 *            new group data
	 * @return the group updated
	 * @throws SocialServiceException
	 */
	public Group updateUserGroup(String token, Group group)
			throws SocialServiceException {
		try {
			String relativePath = String.format("user/group/%s", group.getId());
			String json = RemoteConnector.putJSON(serviceUrl, relativePath,
					JsonUtils.toJSON(group), token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Group.class);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Removes a group of authenticated user
	 * 
	 * @param token
	 *            user access token
	 * @param groupId
	 *            id of the group to delete
	 * @return true if operation gone fine, false otherwise
	 * @throws SocialServiceException
	 */
	public boolean deleteUserGroup(String token, String groupId)
			throws SocialServiceException {
		try {
			String relativePath = String.format("user/group/%s", groupId);
			String json = RemoteConnector.deleteJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves informations about a specific group
	 * @param token
	 *            user access token
	 * @param groupId
	 *            id of the group
	 * 
	 * @return group informations
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Group getUserGroup(String token, String groupId)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("user/group/%s", groupId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, null);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Group.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Add the specified users to a group
	 * @param token
	 *            user access token
	 * @param groupId
	 *            id of the group
	 * @param userIds
	 *            ids of the users to add
	 * 
	 * @return true if the operation succeeded
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean addUsersToGroup(String token, String groupId,
			List<String> userIds) throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("user/group/%s/members",
					groupId);
			String json = RemoteConnector.putJSON(serviceUrl, relativePath,
					null, token, Collections.<String, Object> singletonMap(
							"userIds", userIds));
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}

	}

	/**
	 * Remove the specified users from a group
	 * @param token
	 *            user access token
	 * @param groupId
	 *            id of the group
	 * @param userIds
	 *            ids of the users to remove
	 * 
	 * @return true if the operation succeeded
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean removeUsersFromGroup(String token, String groupId,
			List<String> userIds) throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("user/group/%s/members",
					groupId);
			String json = RemoteConnector.deleteJSON(serviceUrl, relativePath,
					token, Collections.<String, Object> singletonMap("userIds",
							userIds));
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}

	}

	/**
	 * retrieves informations about a specific community
	 * @param token
	 *            user or client access token
	 * @param communityId
	 *            community id
	 * 
	 * @return community information
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Community getCommunity(String token, String communityId)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("community/%s", communityId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, null);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Community.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves all communities which user belongs to
	 * 
	 * @param token
	 *            user access token
	 * @return the {@link Communities} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	// public Communities getUserCommunities(String token)
	// throws SecurityException, SocialServiceException {
	// try {
	// String json = RemoteConnector.getJSON(serviceUrl, USER_COMMUNITY,
	// token, null);
	// return JsonUtils.toObject(json, Communities.class);
	// } catch (RemoteException e) {
	// throw new SocialServiceException(e);
	// }
	// }

	/**
	 * retrieves all communities of the platform
	 * 
	 * @param token
	 *            user or client access token
	 * @return the {@link Communities} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Community> getCommunities(String token)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = "community";
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, null);
			json = extractResultData(json);
			return JsonUtils.toObjectList(json, Community.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Adds authenticated user to the given community
	 * 
	 * @param token
	 *            user access token
	 * @param communityId
	 *            id of community which add user to
	 * @return true if operation gone fine, false otherwise
	 * @throws SocialServiceException
	 */
	public boolean addUserToCommunity(String token, String communityId)
			throws SocialServiceException, SecurityException {
		try {
			String relativePath = String.format("user/community/%s/member",
					communityId);
			String json = RemoteConnector.putJSON(serviceUrl, relativePath,
					null, token);
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Removes authenticated user from the given community
	 * 
	 * @param token
	 *            user access token
	 * @param communityId
	 *            id of community which remove user from
	 * @return true if operation gone fine, false otherwise
	 * @throws SocialServiceException
	 */
	public boolean removeUserFromCommunity(String token, String communityId)
			throws SocialServiceException, SecurityException {
		try {
			String relativePath = String.format("user/community/%s/member",
					communityId);
			String json = RemoteConnector.deleteJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Create a new community data structure for the specified community id.
	 * @param token
	 *            client access token
	 * @param appId
	 *            appId manager of the community
	 * @param community
	 *            community data
	 * 
	 * @return created {@link Community} instance
	 * @throws SocialServiceException
	 * @throws SecurityException
	 */
	public Community createCommunity(String token, String appId,
			Community community) throws SocialServiceException, SecurityException {
		try {
			String relativePath = String.format("app/%s/community",
					Constants.APPID);
			String json = RemoteConnector.postJSON(serviceUrl, relativePath,
					JsonUtils.toJSON(community), token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Community.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Deletes community data structure for the specified community id.
	 * @param token
	 *            client access token
	 * @param communityId
	 *            community id
	 * @param appId
	 *            appId manager of community
	 * 
	 * @return true if the community has been deleted
	 * @throws SocialServiceException
	 * @throws SecurityException
	 */
	public boolean deleteCommunity(String token, String communityId,
			String appId) throws SocialServiceException, SecurityException {
		try {
			String relativePath = String.format("app/%s/community/%s", appId,
					communityId);
			String json = RemoteConnector.deleteJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entities created by the user
	 * 
	 * @param token
	 *            user access token
	 * @param limit
	 *            some filter criteria
	 * 
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Entity> getUserEntities(String token, Limit limit)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = "user/entity";
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, convertLimit(limit));
			json = extractResultData(json);
			return JsonUtils.toObjectList(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity created by the user
	 * 
	 * @param token
	 *            user access token
	 * @param appId
	 *            application social space containing the entity
	 * @param localId
	 *            entity ID
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getUserEntity(String token, String appId, String localId)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("user/%s/entity/%s", appId,
					localId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates or update a user entity. Use this method to change visibility of
	 * an entity (share/unshare)
	 * 
	 * @param token
	 *            user access token
	 * @param appId
	 *            social application space in which create the entity
	 * @param entity
	 *            entity to create
	 * @return {@link Entity} object representing entity created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity createOrUpdateUserEntity(String token, String appId,
			Entity entity) throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("user/%s/entity", appId);
			String json = RemoteConnector.postJSON(serviceUrl, relativePath,
					JsonUtils.toJSON(entity), token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Entity.class);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * deletes a user entity
	 * 
	 * @param token
	 *            user access token
	 * @param appId
	 *            application social space containing the entity
	 * @param localId
	 *            id of the entity to delete
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	// public boolean deleteUserEntity(String token, String appId, String
	// localId)
	// throws SecurityException, SocialServiceException {
	// try {
	// String relativePath = String.format("user/%s/entity/%s", appId,
	// localId);
	// String json = RemoteConnector.deleteJSON(serviceUrl, relativePath,
	// token);
	// json = extractResultData(json);
	// return new Boolean(json);
	// } catch (Exception e) {
	// throw new SocialServiceException(e);
	// }
	// }

	/**
	 * retrieves the entities created by the community
	 * @param token
	 *            client access token
	 * @param communityId
	 *            community ID
	 * @param limit
	 *            filter and pagination criteria
	 * 
	 * @return the {@link Entities} object with list of resources created by the
	 *         community
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Entity> getCommunityEntities(String token, String communityId,
			Limit limit) throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("app/%s/community/%s/entity",
					Constants.APPID, communityId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, convertLimit(limit));
			json = extractResultData(json);
			return JsonUtils.toObjectList(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity created by the community
	 * @param token
	 *            client access token
	 * @param communityId
	 *            community ID
	 * @param localId
	 *            entity ID
	 * 
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getCommunityEntity(String token, String communityId,
			String localId) throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format(
					"app/%s/community/%s/entity/%s", Constants.APPID,
					communityId, localId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates a community entity
	 * @param token
	 *            client access token
	 * @param communityId
	 *            community ID
	 * @param entity
	 *            entity to create
	 * 
	 * @return {@link Entity} object representing entity created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity createOrUpdateCommunityEntity(String token,
			String communityId, Entity entity) throws SecurityException,
			SocialServiceException {
		try {
			String relativePath = String.format("app/%s/community/%s/entity",
					Constants.APPID, communityId);
			String json = RemoteConnector.postJSON(serviceUrl, relativePath,
					JsonUtils.toJSON(entity), token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Entity.class);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entities shared with the user
	 * 
	 * @param token
	 *            user access token
	 * @param limit
	 *            filter and pagination criteria
	 * @return the {@link Entities} object with list of resources shared with
	 *         the user
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Entity> getEntitiesSharedWithUser(String token, Limit limit)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = "user/shared";
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, convertLimit(limit));
			json = extractResultData(json);
			return JsonUtils.toObjectList(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity shared with the user
	 * 
	 * @param token
	 *            user access token
	 * @param appId
	 *            social application space containing entity
	 * @param localId
	 *            entity ID
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getEntitySharedWithUser(String token, String appId,
			String localId) throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("user/%s/shared/%s", appId,
					localId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entities shared with the community
	 * @param token
	 *            client access token
	 * @param appId
	 *            application that manage community
	 * @param communityId
	 *            community ID
	 * @param limit
	 *            filter and pagination criteria
	 * @param shareVisibility
	 *            {@link ShareVisibility} object defining the visibility filter
	 * 
	 * @return the {@link Entities} object with list of resources shared with
	 *         the community
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Entity> getEntitiesSharedWithCommunity(String token,
			String appId, String communityId, Limit limit)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("app/%s/community/%s/shared",
					appId, communityId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, convertLimit(limit));
			json = extractResultData(json);
			return JsonUtils.toObjectList(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity shared with the community
	 * @param token
	 *            client access token
	 * @param appId
	 *            application that manage community
	 * @param communityId
	 *            community ID
	 * @param localId
	 *            entity ID
	 * 
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getEntitySharedWithCommunity(String token,
			String appId, String communityId, String localId)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format(
					"app/%s/community/%s/shared/%s", appId, communityId,
					localId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates a new entity type
	 * 
	 * @param token
	 *            access token
	 * @param entityType
	 *            entity type to create
	 * @return the entity type created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType createEntityType(String token, EntityType entityType)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = "type";
			String json = RemoteConnector.postJSON(serviceUrl, relativePath,
					JsonUtils.toJSON(entityType), token);
			json = extractResultData(json);
			return JsonUtils.toObject(json, EntityType.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves entity type by its id
	 * 
	 * @param token
	 *            access token
	 * @param entityTypeId
	 *            entity type id
	 * @return the entity type
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType getEntityType(String token, String entityTypeId)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("type/%s", entityTypeId);
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, null);
			json = extractResultData(json);
			return JsonUtils.toObject(json, EntityType.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves all entity types defined in the system
	 * 
	 * @param token
	 *            access token
	 * @param limit
	 *            filter and pagination criteria
	 * @return list of entity types
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<EntityType> getEntityTypes(String token, Limit limit)
			throws SecurityException, SocialServiceException {
		try {
			String relativePath = String.format("type");
			String json = RemoteConnector.getJSON(serviceUrl, relativePath,
					token, convertLimit(limit));
			json = extractResultData(json);
			return JsonUtils.toObjectList(json, EntityType.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	private String extractResultData(String response) {
		Result result = JsonUtils.toObject(response, Result.class);
		return JsonUtils.toJSON(result.getData());
	}

	private Map<String, Object> convertLimit(Limit limit) {
		Map<String, Object> parameters = null;
		if (limit != null) {
			parameters = new HashMap<String, Object>();
			parameters.put("pagNum", limit.getPage());
			parameters.put("pageSize", limit.getPageSize());
			parameters.put("fromDate", limit.getFromDate());
			parameters.put("toDate", limit.getToDate());
			parameters.put("sortDirection", limit.getDirection());
			parameters.put("sortList", limit.getSortList());

		}
		return parameters;
	}
}
