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
import eu.trentorise.smartcampus.socialservice.beans.Result;

/**
 * Service APIs
 * 
 * @author mirko perillo
 * 
 */
public class SocialService {

	private static final String GROUP = "user/group/";
	private static final String GROUP_MEMBERS = "/members/";

	private static final String USER_COMMUNITY = "user/community/";

	private static final String COMMUNITY = "community/";

	private static final String USER_CONTENTS = "user/entities/";

	private static final String COMMUNITY_CONTENTS = "/entities/";

	private static final String USER_SHARED = "user/shared/";

	private static final String COMMUNITY_SHARED = "/shared/";

	private static final String TYPES = "type/";
	private static final String TYPES_BY_CONCEPT = "type/concept/";
	private static final String CONCEPTS = "concept/";

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
			String json = RemoteConnector.getJSON(serviceUrl, GROUP, token,
					null);
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
			String json = RemoteConnector.postJSON(serviceUrl, GROUP,
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
			String json = RemoteConnector.putJSON(serviceUrl,
					GROUP + group.getId(), JsonUtils.toJSON(group), token);
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
			String json = RemoteConnector.deleteJSON(serviceUrl, GROUP
					+ groupId, token);
			json = extractResultData(json);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves informations about a specific group
	 * 
	 * @param groupId
	 *            id of the group
	 * @param token
	 *            user access token
	 * @return group informations
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Group getUserGroup(String groupId, String token)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, GROUP + groupId,
					token, null);
			json = extractResultData(json);
			return JsonUtils.toObject(json, Group.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Add the specified users to a group
	 * 
	 * @param groupId
	 *            id of the group
	 * @param userIds
	 *            ids of the users to add
	 * @param token
	 *            user access token
	 * @return true if the operation succeeded
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean addUsersToGroup(String groupId, List<String> userIds,
			String token) throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, GROUP + groupId
					+ GROUP_MEMBERS, null, token, Collections
					.<String, Object> singletonMap("userIds", userIds));
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}

	}

	/**
	 * Remove the specified users from a group
	 * 
	 * @param groupId
	 *            id of the group
	 * @param userIds
	 *            ids of the users to remove
	 * @param token
	 *            user access token
	 * @return true if the operation succeeded
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean removeUsersFromGroup(String groupId, List<String> userIds,
			String token) throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, GROUP
					+ groupId + GROUP_MEMBERS, token, Collections
					.<String, Object> singletonMap("userIds", userIds));
			json = extractResultData(json);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}

	}

	/**
	 * retrieves informations about a specific community
	 * 
	 * @param communityId
	 *            community id
	 * @param token
	 *            user or client access token
	 * @return community information
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Community getCommunity(String communityId, String token)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, COMMUNITY
					+ communityId, token, null);
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
			String json = RemoteConnector.getJSON(serviceUrl, COMMUNITY, token,
					null);
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
			String relativePath = String.format("%s%s/member", USER_COMMUNITY,
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
			String relativePath = String.format("%s%s/member", USER_COMMUNITY,
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
	 * 
	 * @param appId
	 *            appId manager of the community
	 * @param community
	 *            community data
	 * @param token
	 *            client access token
	 * @return created {@link Community} instance
	 * @throws SocialServiceException
	 * @throws SecurityException
	 */
	public Community createCommunity(String appId, Community community,
			String token) throws SocialServiceException, SecurityException {
		try {
			String relativePath = String.format("app/%s/%s", Constants.APPID,
					COMMUNITY);
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
	 * 
	 * @param communityId
	 *            community id
	 * @param appId
	 *            appId manager of community
	 * @param token
	 *            client access token
	 * @return true if the community has been deleted
	 * @throws SocialServiceException
	 * @throws SecurityException
	 */
	public boolean deleteCommunity(String communityId, String appId,
			String token) throws SocialServiceException, SecurityException {
		try {
			String relativePath = String.format("app/%s/%s%s", appId,
					COMMUNITY, communityId);
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
	 * @param position
	 *            counter to buffering result, leave null to not use
	 * @param size
	 *            number of results to get, leave null to get all
	 * @param type
	 *            type of resources to get, leave null to get all the types
	 * @return the {@link Entities} object with list of resources created by the
	 *         user
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entities getUserEntities(String token, Integer position,
			Integer size, String typeId) throws SecurityException,
			SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (position == null) {
				position = -1;
			}
			if (size == null) {
				size = -1;
			}

			parameters.put("position", position);
			parameters.put("size", size);
			parameters.put("type", typeId);
			String json = RemoteConnector.getJSON(serviceUrl, USER_CONTENTS,
					token, parameters);
			return JsonUtils.toObject(json, Entities.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity created by the user
	 * 
	 * @param token
	 *            user access token
	 * @param entityId
	 *            entity ID
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getUserEntity(String token, String entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, USER_CONTENTS
					+ entityId, token);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates a user entity
	 * 
	 * @param token
	 *            user access token
	 * @param entity
	 *            entity to create
	 * @return {@link Entity} object representing entity created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity createUserEntity(String token, EntityRequest entity)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.postJSON(serviceUrl, USER_CONTENTS,
					JsonUtils.toJSON(entity), token);
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
	 * @param entityId
	 *            id of the entity to delete
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean deleteUserEntity(String token, String entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, USER_CONTENTS
					+ entityId, token);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * updates a user entity
	 * 
	 * @param token
	 *            user access token
	 * @param entity
	 *            entity to update
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean updateUserEntity(String token, EntityRequest entity)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, USER_CONTENTS
					+ entity.getId(), JsonUtils.toJSON(entity), token);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entities created by the community
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param position
	 *            counter to buffering result, leave null to not use
	 * @param size
	 *            number of results to get, leave null to get all
	 * @param type
	 *            type of resources to get, leave null to get all the types
	 * @return the {@link Entities} object with list of resources created by the
	 *         community
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entities getCommunityEntities(String communityId, String token,
			Integer position, Integer size, String typeId)
			throws SecurityException, SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (position == null) {
				position = -1;
			}
			if (size == null) {
				size = -1;
			}

			parameters.put("position", position);
			parameters.put("size", size);
			parameters.put("type", typeId);
			String json = RemoteConnector.getJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_CONTENTS, token, parameters);
			return JsonUtils.toObject(json, Entities.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity created by the community
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param entityId
	 *            entity ID
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getCommunityEntity(String communityId, String token,
			String entityId) throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_CONTENTS + entityId, token);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates a community entity
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param entity
	 *            entity to create
	 * @return {@link Entity} object representing entity created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity createCommunityEntity(String communityId, String token,
			EntityRequest entity) throws SecurityException,
			SocialServiceException {
		try {
			String json = RemoteConnector.postJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_CONTENTS,
					JsonUtils.toJSON(entity), token);
			return JsonUtils.toObject(json, Entity.class);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * deletes a community entity
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param entityId
	 *            id of the entity to delete
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean deleteCommunityEntity(String communityId, String token,
			String entityId) throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_CONTENTS + entityId, token);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * updates a community entity
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param entity
	 *            entity to update
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean updateCommunityEntity(String communityId, String token,
			EntityRequest entity) throws SecurityException,
			SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_CONTENTS + entity.getId(),
					JsonUtils.toJSON(entity), token);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entities shared with the user
	 * 
	 * @param token
	 *            user access token
	 * @param shareVisibility
	 *            {@link ShareVisibility} object defining the visibility filter
	 * @param position
	 *            counter to buffering result, leave null to not use
	 * @param size
	 *            number of results to get, leave null to get all
	 * @param type
	 *            type of resources to get, leave null to get all the types
	 * @return the {@link Entities} object with list of resources shared with
	 *         the user
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entities getEntitiesSharedWithUser(String token,
			ShareVisibility shareVisibility, Integer position, Integer size,
			String typeId) throws SecurityException, SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (position == null) {
				position = -1;
			}
			if (size == null) {
				size = -1;
			}

			parameters.put("position", position);
			parameters.put("size", size);
			parameters.put("type", typeId);
			String json = RemoteConnector.postJSON(serviceUrl, USER_SHARED,
					JsonUtils.toJSON(shareVisibility), token, parameters);
			return JsonUtils.toObject(json, Entities.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity shared with the user
	 * 
	 * @param token
	 *            user access token
	 * @param entityId
	 *            entity ID
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getEntitySharedWithUser(String token, String entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, USER_SHARED
					+ entityId, token);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entities shared with the community
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param shareVisibility
	 *            {@link ShareVisibility} object defining the visibility filter
	 * @param position
	 *            counter to buffering result, leave null to not use
	 * @param size
	 *            number of results to get, leave null to get all
	 * @param type
	 *            type of resources to get, leave null to get all the types
	 * @return the {@link Entities} object with list of resources shared with
	 *         the community
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entities getEntitiesSharedWithCommunity(String communityId,
			String token, ShareVisibility shareVisibility, Integer position,
			Integer size, String typeId) throws SecurityException,
			SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (position == null) {
				position = -1;
			}
			if (size == null) {
				size = -1;
			}

			parameters.put("position", position);
			parameters.put("size", size);
			parameters.put("type", typeId);
			String json = RemoteConnector.postJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_SHARED,
					JsonUtils.toJSON(shareVisibility), token, parameters);
			return JsonUtils.toObject(json, Entities.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the entity shared with the community
	 * 
	 * @param communityId
	 *            community ID
	 * @param token
	 *            client access token
	 * @param entityId
	 *            entity ID
	 * @return the {@link Entity} object
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity getEntitySharedWithCommunity(String communityId,
			String token, String entityId) throws SecurityException,
			SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_CONTENTS + entityId, token);
			return JsonUtils.toObject(json, Entity.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * shares a user resource with some visibility options
	 * 
	 * @param token
	 *            user access token
	 * @param entityId
	 *            entity ID
	 * @param shareVisibility
	 *            sharing informations
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean shareUserEntity(String token, String entityId,
			ShareVisibility shareVisibility) throws SecurityException,
			SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, USER_SHARED
					+ entityId, JsonUtils.toJSON(shareVisibility), token, null);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * makes private a shared resource
	 * 
	 * @param token
	 *            access token
	 * @param entityId
	 *            id of the entity to make private
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean unshareUserEntity(String token, String entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, USER_SHARED
					+ entityId, token);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * shares a community resource with some visibility options
	 * 
	 * @param token
	 *            client access token
	 * @param communityId
	 *            community ID
	 * @param entityId
	 *            entity ID
	 * @param shareVisibility
	 *            sharing informations
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean shareCommunityEntity(String communityId, String token,
			String entityId, ShareVisibility shareVisibility)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_SHARED + entityId,
					JsonUtils.toJSON(shareVisibility), token, null);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * makes private a shared resource
	 * 
	 * @param token
	 *            access token
	 * @param communityId
	 *            community ID
	 * @param entityId
	 *            id of the entity to make private
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean unshareCommnunityEntity(String communityId, String token,
			String entityId) throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, COMMUNITY
					+ communityId + COMMUNITY_SHARED + entityId, token);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates a new entity type
	 * 
	 * @param token
	 *            client or user access token
	 * @param conceptId
	 *            id of the concept relative to new entity type
	 * @return the entity type created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType createEntityType(String token, String conceptId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.postJSON(serviceUrl, TYPES, "",
					token, Collections.<String, Object> singletonMap(
							"conceptId", conceptId));
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
	public EntityType getEntityTypeById(String token, String entityTypeId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, TYPES
					+ entityTypeId, token, null);
			return JsonUtils.toObject(json, EntityType.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves entity type by related concept id
	 * 
	 * @param token
	 *            access token
	 * @param conceptId
	 *            id of the concept
	 * @return the entity type related with the concept
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType getEntityTypeByConceptId(String token, String conceptId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, TYPES_BY_CONCEPT
					+ conceptId, token, null);
			return JsonUtils.toObject(json, EntityType.class);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Retrieves a list of entity types that satisfy given prefix, sized by
	 * maxResults parameter (if set)
	 * 
	 * @param token
	 *            access token
	 * @param prefix
	 *            prefix of entity type name to search
	 * @param maxResults
	 *            max number of results, if you leave null default value is 20
	 * @return
	 * @throws SocialServiceException
	 */

	public EntityTypes getEntityTypeByPrefix(String token, String prefix,
			Integer maxResults) throws SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			if (maxResults != null && maxResults > 0)
				parameters.put("maxResults", maxResults);
			parameters.put("prefix", prefix);
			String json = RemoteConnector.getJSON(serviceUrl, TYPES, token,
					parameters);
			return JsonUtils.toObject(json, EntityTypes.class);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}

	}

	private String extractResultData(String response) {
		Result result = JsonUtils.toObject(response, Result.class);
		return JsonUtils.convert(result.getData());
	}

}
