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
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.socialservice.beans.Community;
import eu.trentorise.smartcampus.socialservice.beans.Entity;
import eu.trentorise.smartcampus.socialservice.beans.EntityType;
import eu.trentorise.smartcampus.socialservice.beans.Group;

public class TestClient {

	private SocialService socialService;

	@Before
	public void init() {
		socialService = new SocialService("http://localhost:8080/core.social");
		try {

			// socialService.removeUserFromCommunity(Constants.USER_AUTH_TOKEN,
			// Constants.SC_COMMUNITY_ID);
			// socialService.deleteCommunity(Constants.PRIVATE_COMMUNITY,
			// Constants.APPID, Constants.CLIENT_AUTH_TOKEN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void groups() throws SecurityException, SocialServiceException {
		// get groups
		int size = socialService.getUserGroups(Constants.USER_AUTH_TOKEN)
				.size();

		// create group
		Group g = socialService.createUserGroup(Constants.USER_AUTH_TOKEN,
				"SocialService Client Group " + System.currentTimeMillis());
		Assert.assertNotNull(g);
		Assert.assertEquals(size + 1,
				socialService.getUserGroups(Constants.USER_AUTH_TOKEN).size());

		// update group
		String modName = g.getName() + " MOD";
		g.setName(modName);
		Assert.assertEquals(modName,
				socialService.updateUserGroup(Constants.USER_AUTH_TOKEN, g)
						.getName());

		// add user to group
		Assert.assertTrue(socialService.addUsersToGroup(g.getId(),
				Collections.singletonList(Constants.OTHER_USER_SOCIAL_ID),
				Constants.USER_AUTH_TOKEN));
		Assert.assertEquals(1,
				socialService
						.getUserGroup(g.getId(), Constants.USER_AUTH_TOKEN)
						.getTotalMembers());

		// delete user from group
		Assert.assertTrue(socialService.removeUsersFromGroup(g.getId(),
				Collections.singletonList(Constants.OTHER_USER_SOCIAL_ID),
				Constants.USER_AUTH_TOKEN));
		Assert.assertEquals(0,
				socialService
						.getUserGroup(g.getId(), Constants.USER_AUTH_TOKEN)
						.getTotalMembers());

		// delete group
		Assert.assertTrue(socialService.deleteUserGroup(
				Constants.USER_AUTH_TOKEN, g.getId()));
	}

	@Test
	public void userCommunities() throws SecurityException,
			SocialServiceException {
		// create community
		Community newC = new Community();
		newC.setName("SocialService Client Community "
				+ System.currentTimeMillis());
		newC = socialService.createCommunity(Constants.APPID, newC,
				Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(newC);

		// add user to community
		Assert.assertTrue(socialService.addUserToCommunity(
				Constants.USER_AUTH_TOKEN, newC.getId()));
		newC = socialService.getCommunity(newC.getId(),
				Constants.USER_AUTH_TOKEN);
		Assert.assertEquals(1, newC.getTotalMembers());

		// remove user from community
		Assert.assertTrue(socialService.removeUserFromCommunity(
				Constants.USER_AUTH_TOKEN, newC.getId()));

		newC = socialService.getCommunity(newC.getId(),
				Constants.USER_AUTH_TOKEN);
		Assert.assertEquals(0, newC.getTotalMembers());
	}

	@Test
	public void communities() throws SecurityException, SocialServiceException {

		int communityNumber = socialService.getCommunities(
				Constants.USER_AUTH_TOKEN).size();

		Community newC = new Community();
		newC.setName("SocialService Client Community "
				+ System.currentTimeMillis());
		newC = socialService.createCommunity(Constants.APPID, newC,
				Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(newC);

		newC = socialService.getCommunity(newC.getId(),
				Constants.USER_AUTH_TOKEN);
		Assert.assertNotNull(newC);
		Assert.assertEquals(communityNumber + 1,
				socialService.getCommunities(Constants.USER_AUTH_TOKEN).size());

		Assert.assertTrue(socialService.deleteCommunity(newC.getId(),
				Constants.APPID, Constants.CLIENT_AUTH_TOKEN));
	}

	@Test
	public void userData() throws SecurityException, SocialServiceException {
		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/png");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);

		List<Entity> entities = socialService.getUserEntities(
				Constants.USER_AUTH_TOKEN, null);
		int entitiesNumber = entities.size();

		Entity e = new Entity();
		e.setLocalId(UUID.randomUUID().toString());
		e.setName("name");
		e.setType(entityType.getId());
		// create
		e = socialService.createOrUpdateUserEntity(Constants.USER_AUTH_TOKEN,
				Constants.APPID, e);
		Assert.assertNotNull(e);
		Assert.assertEquals(entitiesNumber + 1,
				socialService.getUserEntities(Constants.USER_AUTH_TOKEN, null)
						.size());

		// update
		e.setName("new name");

		Assert.assertNotNull(socialService.createOrUpdateUserEntity(
				Constants.USER_AUTH_TOKEN, Constants.APPID, e));

		// read
		e = socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
				Constants.APPID, e.getLocalId());
		Assert.assertEquals("new name", e.getName());

	}

	@Test
	public void communityData() throws SecurityException,
			SocialServiceException {
		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/png");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);

		Community c = new Community();
		c.setName("MyCommunity " + System.currentTimeMillis());
		c = socialService.createCommunity(Constants.APPID, c,
				Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(c);

		List<Entity> entities = socialService.getCommunityEntities(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, null);
		int entitiesNumber = entities.size();

		Entity e = new Entity();
		e.setLocalId(UUID.randomUUID().toString());
		e.setName("name");
		e.setType(entityType.getId());

		// create
		e = socialService.createOrUpdateCommunityEntity(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, e);
		Assert.assertNotNull(e);

		entities = socialService.getCommunityEntities(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, null);
		Assert.assertEquals(entitiesNumber + 1, entities.size());

		// update
		e.setName("new name");
		Assert.assertEquals(
				"new name",
				socialService.createOrUpdateCommunityEntity(c.getId(),
						Constants.CLIENT_AUTH_TOKEN, e).getName());

		// read
		e = socialService.getCommunityEntity(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, e.getLocalId());
		Assert.assertEquals("new name", e.getName());

		// delete community
		// Assert.assertTrue(socialService.deleteCommunity(c.getId(),
		// Constants.APPID, Constants.CLIENT_AUTH_TOKEN));
	}

	@Test
	public void userSharing() throws SecurityException, SocialServiceException {

		ShareVisibility visibility = new ShareVisibility();
		visibility.setAllCommunities(true);
		visibility.setAllKnownCommunities(true);
		visibility.setAllKnownUsers(true);
		visibility.setAllUsers(true);

		Entities entities = socialService.getEntitiesSharedWithUser(
				Constants.USER_AUTH_TOKEN, visibility, 0, 10, null);
		Assert.assertNotNull(entities);

		Concepts concepts = socialService.getConceptByPrefix(
				Constants.USER_AUTH_TOKEN, "concert", 1);
		Concept test = concepts.getContent().get(0);
		EntityType entityType = socialService.getEntityTypeByConceptId(
				Constants.USER_AUTH_TOKEN, test.getId());

		if (entityType == null) {
			entityType = socialService.createEntityType(
					Constants.USER_AUTH_TOKEN, test.getId());
		}
		EntityRequest req = new EntityRequest();
		req.setDescription("descr");
		req.setName("name");
		req.setTags(Collections.singletonList(test));
		req.setTypeId(entityType.getId());
		// create
		Entity e = socialService.createUserEntity(Constants.USER_AUTH_TOKEN,
				req);

		// share
		Assert.assertTrue(socialService.shareUserEntity(
				Constants.USER_AUTH_TOKEN, e.getEntityId(), visibility));
		e = socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
				e.getEntityId());
		Assert.assertTrue(e.getVisibility().isAllKnownUsers());
		Assert.assertTrue(e.getVisibility().isAllUsers());
		Assert.assertEquals(0, e.getVisibility().getCommunityIds().size());
		Assert.assertTrue(e.getVisibility().getGroupIds().isEmpty());
		Assert.assertTrue(e.getVisibility().getUserIds().isEmpty());

		// unshare
		socialService.unshareUserEntity(Constants.USER_AUTH_TOKEN,
				e.getEntityId());
		e = socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
				e.getEntityId());
		Assert.assertFalse(e.getVisibility().isAllKnownUsers());
		Assert.assertFalse(e.getVisibility().isAllUsers());
		Assert.assertEquals(0, e.getVisibility().getCommunityIds().size());
		Assert.assertTrue(e.getVisibility().getGroupIds().isEmpty());
		Assert.assertTrue(e.getVisibility().getUserIds().isEmpty());

		socialService.deleteUserEntity(Constants.USER_AUTH_TOKEN,
				e.getEntityId());
	}

	@Test
	public void communitySharing() throws SecurityException,
			SocialServiceException {

		Community c = socialService.getCommunity(Constants.PRIVATE_COMMUNITY,
				Constants.USER_AUTH_TOKEN);
		if (c == null) {
			c = new Community();
			c.setName("MyCommunity");
			c = socialService.createCommunity(Constants.PRIVATE_COMMUNITY, c,
					Constants.CLIENT_AUTH_TOKEN);
			Assert.assertNotNull(c);
		}

		ShareVisibility visibility = new ShareVisibility();
		visibility.setAllCommunities(true);
		visibility.setAllKnownCommunities(true);
		visibility.setAllKnownUsers(true);
		visibility.setAllUsers(true);

		// Entities entities =
		// socialService.getEntitiesSharedWithUser(Constants.USER_AUTH_TOKEN,
		// visibility, 0, 10, null);
		// Assert.assertNotNull(entities);

		Concepts concepts = socialService.getConceptByPrefix(
				Constants.CLIENT_AUTH_TOKEN, "concert", 1);
		Concept test = concepts.getContent().get(0);
		EntityType entityType = socialService.getEntityTypeByConceptId(
				Constants.CLIENT_AUTH_TOKEN, test.getId());

		if (entityType == null) {
			entityType = socialService.createEntityType(
					Constants.CLIENT_AUTH_TOKEN, test.getId());
		}
		EntityRequest req = new EntityRequest();
		req.setDescription("descr");
		req.setName("name");
		req.setTags(Collections.singletonList(test));
		req.setTypeId(entityType.getId());
		// create
		Entity e = socialService.createCommunityEntity(
				Constants.PRIVATE_COMMUNITY, Constants.CLIENT_AUTH_TOKEN, req);

		// share
		Assert.assertTrue(socialService.shareCommunityEntity(
				Constants.PRIVATE_COMMUNITY, Constants.CLIENT_AUTH_TOKEN,
				e.getEntityId(), visibility));
		e = socialService.getCommunityEntity(Constants.PRIVATE_COMMUNITY,
				Constants.CLIENT_AUTH_TOKEN, e.getEntityId());
		Assert.assertNotNull(e.getVisibility());
		Assert.assertTrue(e.getVisibility().isAllKnownUsers());
		Assert.assertTrue(e.getVisibility().isAllUsers());
		Assert.assertEquals(0, e.getVisibility().getCommunityIds().size());
		Assert.assertTrue(e.getVisibility().getGroupIds().isEmpty());
		Assert.assertTrue(e.getVisibility().getUserIds().isEmpty());

		// unshare
		socialService.unshareCommnunityEntity(Constants.PRIVATE_COMMUNITY,
				Constants.CLIENT_AUTH_TOKEN, e.getEntityId());
		e = socialService.getCommunityEntity(Constants.PRIVATE_COMMUNITY,
				Constants.CLIENT_AUTH_TOKEN, e.getEntityId());
		Assert.assertFalse(e.getVisibility().isAllKnownUsers());
		Assert.assertFalse(e.getVisibility().isAllUsers());
		Assert.assertEquals(0, e.getVisibility().getCommunityIds().size());
		Assert.assertTrue(e.getVisibility().getGroupIds().isEmpty());
		Assert.assertTrue(e.getVisibility().getUserIds().isEmpty());

		socialService.deleteCommunityEntity(Constants.PRIVATE_COMMUNITY,
				Constants.CLIENT_AUTH_TOKEN, e.getEntityId());
	}

	@Test
	public void entityTypes() throws SecurityException, SocialServiceException {
		Concepts concepts = socialService.getConceptByPrefix(
				Constants.USER_AUTH_TOKEN, "test", 1);
		Assert.assertNotNull(concepts);
		Assert.assertTrue(concepts.getContent().size() > 0);
		EntityType et = null;
		if ((et = socialService
				.getEntityTypeByConceptId(Constants.USER_AUTH_TOKEN, concepts
						.getContent().get(0).getId())) == null) {
			et = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
					concepts.getContent().get(0).getId());
		}
		Assert.assertNotNull(et);
		Assert.assertNotNull(socialService.getEntityTypeByConceptId(
				Constants.USER_AUTH_TOKEN, et.getConcept().getId()));
		Assert.assertNotNull(socialService.getEntityTypeByPrefix(
				Constants.USER_AUTH_TOKEN, "test", 10));
		Assert.assertNotNull(socialService.getEntityTypeById(
				Constants.USER_AUTH_TOKEN, et.getId()));

	}
}
