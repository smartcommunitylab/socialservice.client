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

import java.util.Arrays;
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
import eu.trentorise.smartcampus.socialservice.beans.Visibility;

public class TestClient {

	private SocialService socialService;

	@Before
	public void init() {
		socialService = new SocialService(Constants.SERVICE_ENDPOINT);
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
		// TODO fix delete community with created entity throws Exception
		// Assert.assertTrue(socialService.deleteCommunity(c.getId(),
		// Constants.APPID, Constants.CLIENT_AUTH_TOKEN));
	}

	@Test
	public void userSharing() throws SecurityException, SocialServiceException {

		Community c = new Community();
		c.setName("SocialService Client Community "
				+ System.currentTimeMillis());

		c = socialService.createCommunity(Constants.APPID, c,
				Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(c.getId());

		List<Entity> entities = socialService.getUserEntities(
				Constants.USER_AUTH_TOKEN, null);
		Assert.assertNotNull(entities);
		int size = entities.size();

		int communitySharingSize = socialService
				.getEntitiesSharedWithCommunity(Constants.APPID, c.getId(),
						Constants.CLIENT_AUTH_TOKEN, null).size();

		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/png");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);

		String localId = UUID.randomUUID().toString();
		String entityName = "SocialService Client Entity "
				+ System.currentTimeMillis();
		Entity req = new Entity();
		req.setName(entityName);
		req.setLocalId(localId);
		req.setType(entityType.getId());
		req.setVisibility(new Visibility(null, Arrays.asList(c.getId()), null));

		// create
		req = socialService.createOrUpdateUserEntity(Constants.USER_AUTH_TOKEN,
				Constants.APPID, req);

		Assert.assertEquals(size + 1,
				socialService.getUserEntities(Constants.USER_AUTH_TOKEN, null)
						.size());
		Assert.assertEquals(
				entityName,
				socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
						Constants.APPID, localId).getName());
		Assert.assertNull(socialService.getUserEntity(
				Constants.USER_AUTH_TOKEN, Constants.APPID, "dkfjskdfsjdlfj"));

		Assert.assertEquals(
				communitySharingSize + 1,
				socialService.getEntitiesSharedWithCommunity(Constants.APPID,
						c.getId(), Constants.CLIENT_AUTH_TOKEN, null).size());

		Assert.assertEquals(
				entityName,
				socialService.getEntitySharedWithCommunity(Constants.APPID,
						c.getId(), Constants.CLIENT_AUTH_TOKEN, localId)
						.getName());

	}

	@Test
	public void communitySharing() throws SecurityException,
			SocialServiceException {

		Community c = new Community();
		c.setName("SocialService Client Community "
				+ System.currentTimeMillis());
		c = socialService.createCommunity(Constants.APPID, c,
				Constants.CLIENT_AUTH_TOKEN);
		Assert.assertNotNull(c.getId());

		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/jpg");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);
		Assert.assertNotNull(entityType);

		String localId = UUID.randomUUID().toString();
		String entityName = "SocialService Client Entity "
				+ System.currentTimeMillis();
		Entity req = new Entity();

		req.setName(entityName);
		req.setLocalId(localId);
		req.setType(entityType.getId());
		req.setVisibility(new Visibility(Arrays.asList("1"), null, null));

		int sharedSize = socialService.getEntitiesSharedWithUser(
				Constants.USER_AUTH_TOKEN, null).size();

		int size = socialService.getCommunityEntities(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, null).size();
		req = socialService.createOrUpdateCommunityEntity(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, req);

		Assert.assertEquals(sharedSize + 1, socialService
				.getEntitiesSharedWithUser(Constants.USER_AUTH_TOKEN, null)
				.size());

		Assert.assertEquals(
				entityName,
				socialService.getEntitySharedWithUser(
						Constants.USER_AUTH_TOKEN, Constants.APPID, localId)
						.getName());

		req.setVisibility(new Visibility());
		req = socialService.createOrUpdateCommunityEntity(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, req);

		Assert.assertEquals(sharedSize, socialService
				.getEntitiesSharedWithUser(Constants.USER_AUTH_TOKEN, null)
				.size());

		Assert.assertNull(socialService.getEntitySharedWithUser(
				Constants.USER_AUTH_TOKEN, Constants.APPID, localId));
		Assert.assertEquals(
				0,
				socialService.getEntitiesSharedWithCommunity(Constants.APPID,
						c.getId(), Constants.CLIENT_AUTH_TOKEN, null).size());

		Assert.assertEquals(
				size + 1,
				socialService.getCommunityEntities(c.getId(),
						Constants.CLIENT_AUTH_TOKEN, null).size());
		Assert.assertNotNull(socialService.getCommunityEntity(c.getId(),
				Constants.CLIENT_AUTH_TOKEN, localId));
	}

	@Test
	public void entityTypes() throws SecurityException, SocialServiceException {

		int size = socialService
				.getEntityTypes(Constants.USER_AUTH_TOKEN, null).size();

		String name = "SocialService Client Type " + System.currentTimeMillis();
		EntityType type = new EntityType(name, "image/jpg");

		type = socialService.createEntityType(Constants.USER_AUTH_TOKEN, type);
		Assert.assertNotNull(type);
		Assert.assertEquals(size + 1,
				socialService.getEntityTypes(Constants.USER_AUTH_TOKEN, null)
						.size());

		Assert.assertEquals(
				name,
				socialService.getEntityType(Constants.USER_AUTH_TOKEN,
						type.getId()).getName());

	}
}
