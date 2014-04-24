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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import eu.trentorise.smartcampus.socialservice.beans.Comment;
import eu.trentorise.smartcampus.socialservice.beans.Community;
import eu.trentorise.smartcampus.socialservice.beans.Entity;
import eu.trentorise.smartcampus.socialservice.beans.EntityInfo;
import eu.trentorise.smartcampus.socialservice.beans.EntityType;
import eu.trentorise.smartcampus.socialservice.beans.Group;
import eu.trentorise.smartcampus.socialservice.beans.Visibility;

public class TestClient {

	private SocialService socialService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

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
		Assert.assertTrue(socialService.addUsersToGroup(
				Constants.USER_AUTH_TOKEN, g.getId(),
				Collections.singletonList(Constants.OTHER_USER_SOCIAL_ID)));
		Assert.assertEquals(1,
				socialService
						.getUserGroup(Constants.USER_AUTH_TOKEN, g.getId())
						.getTotalMembers());

		// delete user from group
		Assert.assertTrue(socialService.removeUsersFromGroup(
				Constants.USER_AUTH_TOKEN, g.getId(),
				Collections.singletonList(Constants.OTHER_USER_SOCIAL_ID)));
		Assert.assertEquals(0,
				socialService
						.getUserGroup(Constants.USER_AUTH_TOKEN, g.getId())
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
		newC = socialService.createCommunity(Constants.CLIENT_AUTH_TOKEN,
				Constants.APPID, newC);
		Assert.assertNotNull(newC);

		// add user to community
		Assert.assertTrue(socialService.addUserToCommunity(
				Constants.USER_AUTH_TOKEN, newC.getId()));
		newC = socialService.getCommunity(Constants.USER_AUTH_TOKEN,
				newC.getId());
		Assert.assertEquals(1, newC.getTotalMembers());

		// remove user from community
		Assert.assertTrue(socialService.removeUserFromCommunity(
				Constants.USER_AUTH_TOKEN, newC.getId()));

		newC = socialService.getCommunity(Constants.USER_AUTH_TOKEN,
				newC.getId());
		Assert.assertEquals(0, newC.getTotalMembers());
	}

	@Test
	public void communities() throws SecurityException, SocialServiceException {

		int communityNumber = socialService.getCommunities(
				Constants.USER_AUTH_TOKEN).size();

		Community newC = new Community();
		newC.setName("SocialService Client Community "
				+ System.currentTimeMillis());
		newC = socialService.createCommunity(Constants.CLIENT_AUTH_TOKEN,
				Constants.APPID, newC);
		Assert.assertNotNull(newC);

		newC = socialService.getCommunity(Constants.USER_AUTH_TOKEN,
				newC.getId());
		Assert.assertNotNull(newC);
		Assert.assertEquals(communityNumber + 1,
				socialService.getCommunities(Constants.USER_AUTH_TOKEN).size());

		Assert.assertTrue(socialService.deleteCommunity(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID, newC.getId()));
	}

	@Test
	public void entityInfo() throws SecurityException, SocialServiceException {
		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/png");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);

		Entity e = new Entity();
		e.setLocalId(UUID.randomUUID().toString());
		e.setName("name");
		e.setType(entityType.getId());
		// create
		e = socialService.createOrUpdateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, e);
		Assert.assertNotNull(e);

		EntityInfo info = socialService.getEntityInfoByApp(
				Constants.CLIENT_AUTH_TOKEN, e.getUri());
		Assert.assertEquals(Constants.OWNER_ID, info.getUserOwner());
		Assert.assertEquals(Constants.APPID, info.getAppId());

		info = socialService.getEntityInfoByApp(Constants.CLIENT_AUTH_TOKEN,
				"dummie");
		Assert.assertNull(info);

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
		e = socialService.createOrUpdateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, e);
		Assert.assertNotNull(e);
		Assert.assertEquals(entitiesNumber + 1,
				socialService.getUserEntities(Constants.USER_AUTH_TOKEN, null)
						.size());

		// update
		e.setName("new name");

		Assert.assertNotNull(socialService.createOrUpdateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, e));

		// read
		e = socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
				Constants.APPID, e.getLocalId());
		Assert.assertEquals("new name", e.getName());

		e.setName("new name by user");

		Assert.assertNotNull(socialService.updateUserEntityByUser(
				Constants.USER_AUTH_TOKEN, Constants.APPID, e));

		e = socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
				Constants.APPID, e.getLocalId());
		Assert.assertEquals("new name by user", e.getName());

		e.setName("new name by app");

		Assert.assertNotNull(socialService.updateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, e));

		e = socialService.getUserEntity(Constants.USER_AUTH_TOKEN,
				Constants.APPID, e.getLocalId());
		Assert.assertEquals("new name by app", e.getName());

		// delete
		Assert.assertTrue(socialService.deleteEntityByUser(
				Constants.USER_AUTH_TOKEN, Constants.APPID, e.getLocalId()));

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
		c = socialService.createCommunity(Constants.CLIENT_AUTH_TOKEN,
				Constants.APPID, c);
		Assert.assertNotNull(c);

		List<Entity> entities = socialService.getCommunityEntities(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), null);
		int entitiesNumber = entities.size();

		Entity e = new Entity();
		e.setLocalId(UUID.randomUUID().toString());
		e.setName("name");
		e.setType(entityType.getId());

		// create
		e = socialService.createOrUpdateCommunityEntity(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), e);
		Assert.assertNotNull(e);

		entities = socialService.getCommunityEntities(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), null);
		Assert.assertEquals(entitiesNumber + 1, entities.size());

		// update
		e.setName("new name");
		Assert.assertEquals(
				"new name",
				socialService.createOrUpdateCommunityEntity(
						Constants.CLIENT_AUTH_TOKEN, c.getId(), e).getName());

		// read
		e = socialService.getCommunityEntity(Constants.CLIENT_AUTH_TOKEN,
				c.getId(), e.getLocalId());
		Assert.assertEquals("new name", e.getName());

		// delete entity
		Assert.assertTrue(socialService.deleteEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID, e.getLocalId()));

		// create
		e = socialService.createOrUpdateCommunityEntity(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), e);
		Assert.assertNotNull(e);

		// try delete entity by user
		try {
			socialService.deleteEntityByUser(Constants.USER_AUTH_TOKEN,
					Constants.APPID, e.getLocalId());
			Assert.fail("SecurityException not thrown");
		} catch (SecurityException e1) {

		}

		// delete entity
		Assert.assertTrue(socialService.deleteEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID, e.getLocalId()));

		// delete community
		Assert.assertTrue(socialService.deleteCommunity(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID, c.getId()));
	}

	@Test
	public void userSharing() throws SecurityException, SocialServiceException {

		Community c = new Community();
		c.setName("SocialService Client Community "
				+ System.currentTimeMillis());

		c = socialService.createCommunity(Constants.CLIENT_AUTH_TOKEN,
				Constants.APPID, c);
		Assert.assertNotNull(c.getId());

		List<Entity> entities = socialService.getUserEntities(
				Constants.USER_AUTH_TOKEN, null);
		Assert.assertNotNull(entities);
		int size = entities.size();

		int communitySharingSize = socialService
				.getEntitiesSharedWithCommunity(Constants.CLIENT_AUTH_TOKEN,
						Constants.APPID, c.getId(), null).size();

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
		req = socialService.createOrUpdateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, req);

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
				socialService.getEntitiesSharedWithCommunity(
						Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
						c.getId(), null).size());

		Assert.assertEquals(
				entityName,
				socialService.getEntitySharedWithCommunity(
						Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
						c.getId(), localId).getName());

		req.setUri("dummie");
		try {
			socialService.updateUserEntityByUser(Constants.USER_AUTH_TOKEN,
					Constants.APPID, req);
			Assert.fail("IllegalArgumentException not thrown");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void communitySharing() throws SecurityException,
			SocialServiceException {

		Community c = new Community();
		c.setName("SocialService Client Community "
				+ System.currentTimeMillis());
		c = socialService.createCommunity(Constants.CLIENT_AUTH_TOKEN,
				Constants.APPID, c);
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

		int size = socialService.getCommunityEntities(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), null).size();
		req = socialService.createOrUpdateCommunityEntity(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), req);

		Assert.assertEquals(sharedSize + 1, socialService
				.getEntitiesSharedWithUser(Constants.USER_AUTH_TOKEN, null)
				.size());

		Assert.assertEquals(
				entityName,
				socialService.getEntitySharedWithUser(
						Constants.USER_AUTH_TOKEN, Constants.APPID, localId)
						.getName());

		req.setVisibility(new Visibility());
		req = socialService.createOrUpdateCommunityEntity(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), req);

		Assert.assertEquals(sharedSize, socialService
				.getEntitiesSharedWithUser(Constants.USER_AUTH_TOKEN, null)
				.size());

		Assert.assertNull(socialService.getEntitySharedWithUser(
				Constants.USER_AUTH_TOKEN, Constants.APPID, localId));
		Assert.assertEquals(
				0,
				socialService.getEntitiesSharedWithCommunity(
						Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
						c.getId(), null).size());

		Assert.assertEquals(
				size + 1,
				socialService.getCommunityEntities(Constants.CLIENT_AUTH_TOKEN,
						c.getId(), null).size());
		Assert.assertNotNull(socialService.getCommunityEntity(
				Constants.CLIENT_AUTH_TOKEN, c.getId(), localId));
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

	@Test
	public void rating() throws SecurityException, SocialServiceException {
		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/png");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);

		String localId = UUID.randomUUID().toString();
		try {
			socialService.getRatingByUser(Constants.USER_AUTH_TOKEN,
					Constants.APPID, localId);
			Assert.fail("Exception not thrown");
		} catch (SecurityException e) {

		}
		Entity e = new Entity();
		e.setLocalId(localId);
		e.setName("name");
		e.setType(entityType.getId());

		e = socialService.createOrUpdateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, e);

		Assert.assertTrue(socialService.rateEntityByUser(
				Constants.USER_AUTH_TOKEN, Constants.APPID, localId, 2.0d));

		Assert.assertEquals(
				2d,
				socialService.getRatingByUser(Constants.USER_AUTH_TOKEN,
						Constants.APPID, localId).getRating());

		Assert.assertTrue(socialService.removeRatingByUser(
				Constants.USER_AUTH_TOKEN, Constants.APPID, localId));

		try {
			socialService.getRatingByUser(Constants.USER_AUTH_TOKEN,
					Constants.APPID, localId);
			Assert.fail("Exception not thrown");
		} catch (SecurityException e1) {

		}
	}

	@Test
	public void comments() throws Exception {
		EntityType entityType = new EntityType("SocialService Client Type "
				+ System.currentTimeMillis(), "image/png");
		entityType = socialService.createEntityType(Constants.USER_AUTH_TOKEN,
				entityType);

		Entity e = new Entity();
		e.setLocalId("" + System.currentTimeMillis());
		e.setName("name");
		e.setType(entityType.getId());

		e = socialService.createOrUpdateUserEntityByApp(
				Constants.CLIENT_AUTH_TOKEN, Constants.APPID,
				Constants.OWNER_ID, e);

		Assert.assertEquals("\"[]\"", socialService.getCommentJSONByEntity(
				Constants.USER_AUTH_TOKEN, Constants.APPID, e.getLocalId(),
				null, null, null));

		Comment comment = socialService.createComment(
				Constants.USER_AUTH_TOKEN, "my comment", Constants.APPID,
				e.getLocalId());
		Assert.assertNotNull(comment);

		Assert.assertNotSame("\"[]\"", socialService.getCommentJSONByEntity(
				Constants.USER_AUTH_TOKEN, Constants.APPID, e.getLocalId(),
				null, null, null));

		Assert.assertNotSame("\"[]\"", socialService.getCommentJSONById(
				Constants.USER_AUTH_TOKEN, comment.getId()));

	}
}
