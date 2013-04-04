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

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;
import eu.trentorise.smartcampus.socialservice.model.Community;
import eu.trentorise.smartcampus.socialservice.model.Concept;
import eu.trentorise.smartcampus.socialservice.model.Entity;
import eu.trentorise.smartcampus.socialservice.model.EntityType;
import eu.trentorise.smartcampus.socialservice.model.Group;
import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;
import eu.trentorise.smartcampus.socialservice.model.SharedContent;
import eu.trentorise.smartcampus.socialservice.model.Topic;

/**
 * Service APIs
 * 
 * @author mirko perillo
 * 
 */
public class SocialService {

	/** Service path */
	private static final String SERVICE = "/smartcampus.vas.community-manager.web/";

	private static final String GROUP = "eu.trentorise.smartcampus.cm.model.Group/";

	private static final String COMMUNITY = "eu.trentorise.smartcampus.cm.model.Community/";

	private static final String TOPIC = "eu.trentorise.smartcampus.cm.model.Topic/";

	private static final String VISIBILITY = "assignments/";

	private static final String MY_CONTENTS = "content/";

	private static final String SHARED_CONTENT = "sharedcontent/";

	private static final String SHARE = "share/";

	private static final String UNSHARE = "unshare/";

	private static final String CREATE_ENTITY_TYPES = "entitytype/";

	private static final String GET_ENTITY_TYPE_BY_ID = "entitytype-by-id/";

	private static final String GET_ENTITY_TYPE_BY_CONCEPT_ID = "entitytype-by-conceptid/";

	private static final String GET_ENTITY_TYPE_BY_PREFIX = "entitytype-by-prefix/";

	private static final String GET_CONCEPTS = "suggestion/";

	private static final String ENTITY = "entity/";

	private String serviceUrl;

	private final static String ENCODE_FORMAT = "utf8";

	public SocialService(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	/**
	 * retrieves all user groups
	 * 
	 * @param token
	 *            authentication token
	 * @return the list of user groups, or a empty list
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Group> getGroups(String token) throws SecurityException,
			SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE + GROUP,
					token, null);
			return Group.toList(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves informations about a specific group
	 * 
	 * @param groupId
	 *            id of the group
	 * @param token
	 *            authentication token
	 * @return group informations
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Group getGroup(String groupId, String token)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE + GROUP
					+ groupId, token, null);
			return Group.toObject(json);
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
	 *            authentication token
	 * @return community information
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Community getCommunity(String communityId, String token)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ COMMUNITY + communityId, token, null);
			return Community.toObject(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves all communities which user belongs to
	 * 
	 * @param token
	 *            authentication token
	 * @return the list of communities
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Community> getCommunities(String token)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ COMMUNITY, token, null);
			return Community.toList(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves all the topic created by the user
	 * 
	 * @param token
	 *            authentication token
	 * @return the list of user topics
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Topic> getTopics(String token) throws SecurityException,
			SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE + TOPIC,
					token, null);
			return Topic.toList(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves information about a specific topic
	 * 
	 * @param token
	 *            authentication token
	 * @param topicId
	 *            the id of the topic
	 * @return topic informations
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Topic getTopic(String token, String topicId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE + TOPIC
					+ topicId, token, null);
			return Topic.toObject(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * shares a resource with some visibility options
	 * 
	 * @param token
	 *            authentication token
	 * @param shareOperation
	 *            sharing informations
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean share(String token, ShareOperation shareOperation)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.postJSON(serviceUrl, SERVICE + SHARE,
					ShareOperation.toJson(shareOperation), token, null);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * makes private a shared resource
	 * 
	 * @param token
	 *            authentication token
	 * @param entityId
	 *            id of the entity to make private
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean unshare(String token, long entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, SERVICE + UNSHARE
					+ entityId, "", token);
			return new Boolean(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the resource shared with the user from some source defined in
	 * ShareVisibility argument
	 * 
	 * @param token
	 *            authentication token
	 * @param shareVisibility
	 *            sources that shared resource with the user
	 * @param position
	 *            counter for buffering result, leave null to not use
	 * 
	 * @param size
	 *            number of results to get, leave null to get all resource
	 * @param type
	 *            type of the resource to get, leave null to get all the types
	 * @return the list of shared resources from given sources with the user
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<SharedContent> getSharedContents(String token,
			ShareVisibility shareVisibility, Integer position, Integer size,
			Long typeId) throws SecurityException, SocialServiceException {
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
			String json = RemoteConnector.postJSON(serviceUrl, SERVICE
					+ SHARED_CONTENT, ShareVisibility.toJson(shareVisibility),
					token, parameters);
			return SharedContent.toList(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves the resource created by the user
	 * 
	 * @param token
	 *            authentication token
	 * @param position
	 *            counter to buffering result, leave null to not use
	 * @param size
	 *            number of results to get, leave null to get all
	 * @param type
	 *            type of resources to get, leave null to get all the types
	 * @return the list of resources created by the user
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<SharedContent> getMyContents(String token, Integer position,
			Integer size, Long typeId) throws SecurityException,
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
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ MY_CONTENTS, token, parameters);
			return SharedContent.toList(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves visibility options of a resource
	 * 
	 * @param token
	 *            authentication token
	 * @param entityId
	 *            id of the entity
	 * @return the visibility options
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public ShareVisibility getShareVisibility(String token, long entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ VISIBILITY + entityId, token, null);
			return ShareVisibility.toObject(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}

	}

	/**
	 * creates a new entity type
	 * 
	 * @param token
	 *            authentication token
	 * @param conceptId
	 *            id of the concept relative to new entity type
	 * @return the entity type created
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType createEntityType(String token, long conceptId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.postJSON(serviceUrl, SERVICE
					+ CREATE_ENTITY_TYPES + conceptId, "", token, null);
			return EntityType.toObject(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves entity type by its id
	 * 
	 * @param token
	 *            authentication token
	 * @param entityTypeId
	 *            entity type id
	 * @return the entity type
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType getEntityTypeById(String token, long entityTypeId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ GET_ENTITY_TYPE_BY_ID + entityTypeId, token, null);
			return EntityType.toObject(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * retrieves entity type by related concept id
	 * 
	 * @param token
	 *            authentication token
	 * @param conceptId
	 *            id of the concept
	 * @return the entity type related with the concept
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public EntityType getEntityTypeByConceptId(String token, long conceptId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ GET_ENTITY_TYPE_BY_CONCEPT_ID + conceptId, token, null);
			return EntityType.toObject(json);
		} catch (RemoteException e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * Retrieves a list of entity types that satisfy given prefix, sized by
	 * maxResults parameter (if it is setted)
	 * 
	 * @param token
	 *            authentication token
	 * @param prefix
	 *            prefix of entity type name to search
	 * @param maxResults
	 *            max number of results, if you leave null default value is 20
	 * @return
	 * @throws SocialServiceException
	 */

	public List<EntityType> getEntityTypeByPrefix(String token, String prefix,
			Integer maxResults) throws SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("maxResults", maxResults);
			prefix = URLEncoder.encode(prefix, ENCODE_FORMAT);
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ GET_ENTITY_TYPE_BY_PREFIX + prefix, token, parameters);
			return EntityType.toList(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}

	}

	/**
	 * retrieves a list of tags by a prefix for a maximum number of results
	 * 
	 * @param token
	 *            authentication token
	 * @param prefix
	 *            prefix to search in tag name
	 * @param maxResults
	 *            maximum number of results to retrieves, if you leave null
	 *            default number is 20
	 * @return the list of tags that contain the prefix
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public List<Concept> getConceptByPrefix(String token, String prefix,
			Integer maxResults) throws SecurityException,
			SocialServiceException {
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("maxResults", maxResults);
			prefix = URLEncoder.encode(prefix, ENCODE_FORMAT);
			String json = RemoteConnector.getJSON(serviceUrl, SERVICE
					+ GET_CONCEPTS + prefix, token, parameters);
			return Concept.toList(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * creates an entity
	 * 
	 * @param token
	 *            authentication token
	 * @param entity
	 *            entity to create
	 * @return entity created with id field populated
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public Entity createEntity(String token, Entity entity)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.postJSON(serviceUrl,
					SERVICE + ENTITY, Entity.toJson(entity), token, null);
			return Entity.toObject(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * deletes an entity
	 * 
	 * @param token
	 *            authentication token
	 * @param entityId
	 *            id of the entity to delete
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean deleteEntity(String token, long entityId)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.deleteJSON(serviceUrl, SERVICE
					+ ENTITY + entityId, token);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}

	/**
	 * updates an entity
	 * 
	 * @param token
	 *            authentication token
	 * @param entity
	 *            entity to update
	 * @return true if operation gone fine, false otherwise
	 * @throws SecurityException
	 * @throws SocialServiceException
	 */
	public boolean updateEntity(String token, Entity entity)
			throws SecurityException, SocialServiceException {
		try {
			String json = RemoteConnector.putJSON(serviceUrl, SERVICE + ENTITY,
					Entity.toJson(entity), token);
			return new Boolean(json);
		} catch (Exception e) {
			throw new SocialServiceException(e);
		}
	}
}
