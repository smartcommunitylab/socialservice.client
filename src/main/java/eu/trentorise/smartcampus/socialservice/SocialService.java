package eu.trentorise.smartcampus.socialservice;

import java.util.List;

import eu.trentorise.smartcampus.socialservice.model.Community;
import eu.trentorise.smartcampus.socialservice.model.Group;
import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;
import eu.trentorise.smartcampus.socialservice.model.SharedContent;
import eu.trentorise.smartcampus.socialservice.model.Topic;
import eu.trentorise.smartcampus.socialservice.network.RemoteConnector;

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
		return RemoteConnector.getGroups(serviceUrl, token);
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
		return RemoteConnector.getGroup(serviceUrl, token, groupId);
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
		return RemoteConnector.getCommunity(serviceUrl, token, communityId);
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
		return RemoteConnector.getCommunities(serviceUrl, token);
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
		return RemoteConnector.getTopics(serviceUrl, token);
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
		return RemoteConnector.getTopic(serviceUrl, token, topicId);
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
		return RemoteConnector.share(serviceUrl, token, shareOperation);
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
		return RemoteConnector.unshare(serviceUrl, token, entityId);
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
			String type) throws SecurityException, SocialServiceException {
		return RemoteConnector.getSharedContents(serviceUrl, token,
				shareVisibility, position, size, type);
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
			Integer size, String type) throws SecurityException,
			SocialServiceException {
		return RemoteConnector.getMyContents(serviceUrl, token, position, size,
				type);
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
		return RemoteConnector.getShareVisibility(serviceUrl, token, entityId);
	}
}
