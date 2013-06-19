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
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.socialservice.model.Concept;
import eu.trentorise.smartcampus.socialservice.model.Entity;
import eu.trentorise.smartcampus.socialservice.model.EntityType;
import eu.trentorise.smartcampus.socialservice.model.Group;
import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;
import eu.trentorise.smartcampus.socialservice.model.SharedContent;
import eu.trentorise.smartcampus.socialservice.model.Topic;
import eu.trentorise.smartcampus.socialservice.model.Topic.TopicStatus;

public class TestClient {

	private SocialService socialService;

	@Before
	public void init() {
		socialService = new SocialService(Constants.SERVICE_URL);
	}

	@Test
	public void groups() throws SecurityException, SocialServiceException {
		// get groups
		int size = socialService.getGroups(Constants.AUTH_TOKEN).size();

		// create group
		Group g = socialService.createGroup(Constants.AUTH_TOKEN,
				"example group");
		Assert.assertNotNull(g);
		Assert.assertEquals(size + 1,
				socialService.getGroups(Constants.AUTH_TOKEN).size());

		// update group
		String modName = "group MOD";
		g.setName(modName);
		Assert.assertTrue(socialService.updateGroup(Constants.AUTH_TOKEN, g));
		g = socialService.getGroup(g.getId(), Constants.AUTH_TOKEN);
		Assert.assertEquals(modName, g.getName());
		// delete group
		Assert.assertTrue(socialService.deleteGroup(Constants.AUTH_TOKEN,
				g.getId()));
	}

	@Test
	public void communities() throws SecurityException, SocialServiceException {
		// get communities
		Assert.assertNotNull(socialService.getCommunities(Constants.AUTH_TOKEN));

		// add user to community
		Assert.assertTrue(socialService.addUserToCommunity(
				Constants.AUTH_TOKEN, Constants.SC_COMMUNITY_ID));

		// remove user from community
		Assert.assertTrue(socialService.removeUserFromCommunity(
				Constants.AUTH_TOKEN, Constants.SC_COMMUNITY_ID));
	}

	@Test
	public void topics() throws SecurityException, SocialServiceException {
		// get topics
		Assert.assertNotNull(socialService.getTopics(Constants.AUTH_TOKEN));

		// create topic
		Topic t = new Topic();
		t.setAllCommunities(true);
		t.setName("test topic");
		t.setStatus(TopicStatus.ACTIVE.getValue());
		t = socialService.createTopic(Constants.AUTH_TOKEN, t);
		Assert.assertTrue(Long.valueOf(t.getId()) > 0);

		// update topic
		t.setName("topic MOD");
		Assert.assertTrue(socialService.updateTopic(Constants.AUTH_TOKEN, t));
		t = socialService.getTopic(Constants.AUTH_TOKEN, t.getId());
		Assert.assertEquals("topic MOD", t.getName());

		// change status
		Assert.assertEquals(TopicStatus.ACTIVE.getValue(), t.getStatus());
		Assert.assertTrue(socialService.changeTopicStatus(Constants.AUTH_TOKEN,
				t.getId(), TopicStatus.SUSPENDED));
		t = socialService.getTopic(Constants.AUTH_TOKEN, t.getId());
		Assert.assertEquals(TopicStatus.SUSPENDED.getValue(), t.getStatus());

		// delete topic
		Assert.assertTrue(socialService.deleteTopic(Constants.AUTH_TOKEN,
				t.getId()));
	}

	@Test
	public void sharing() throws SecurityException, SocialServiceException {

		ShareVisibility visibility = new ShareVisibility();
		visibility.setAllCommunities(true);
		visibility.setAllKnownCommunities(true);
		visibility.setAllKnownUsers(true);
		visibility.setAllUsers(true);

		socialService.getSharedContents(Constants.AUTH_TOKEN, visibility, 0,
				10, null);
		List<SharedContent> contents = socialService.getMyContents(
				Constants.AUTH_TOKEN, 0, 5, null);
		Assert.assertTrue(contents.size() > 0);

		ShareOperation shareOperation = new ShareOperation();
		shareOperation.setEntityId(contents.get(0).getEntityId());
		shareOperation.setVisibility(visibility);
		Assert.assertTrue(socialService.share(Constants.AUTH_TOKEN,
				shareOperation));

		Assert.assertNotNull(socialService.getShareVisibility(
				Constants.AUTH_TOKEN, contents.get(0).getEntityId()));
	}

	@Test
	public void entityTypes() throws SecurityException, SocialServiceException {
		List<Concept> concepts = socialService.getConceptByPrefix(
				Constants.AUTH_TOKEN, "test", 1);
		Assert.assertNotNull(concepts);
		Assert.assertTrue(concepts.size() > 0);
		if (socialService.getEntityTypeByConceptId(Constants.AUTH_TOKEN,
				concepts.get(0).getId()) == null) {
			socialService.createEntityType(Constants.AUTH_TOKEN, concepts
					.get(0).getId());
		}
		Assert.assertNotNull(socialService.getEntityTypeByConceptId(
				Constants.AUTH_TOKEN, concepts.get(0).getId()));
	}

	@Test
	public void entities() throws SecurityException, SocialServiceException {

		Assert.assertEquals(
				1,
				socialService.getEntityTypeByPrefix(Constants.AUTH_TOKEN,
						"event", null).size());
		Assert.assertEquals(
				0,
				socialService.getEntityTypeByPrefix(Constants.AUTH_TOKEN,
						"dummie", null).size());

		List<Concept> concepts = socialService.getConceptByPrefix(
				Constants.AUTH_TOKEN, "test", null);
		Assert.assertNotNull(concepts);
		Assert.assertTrue(concepts.size() > 0);
		EntityType type = socialService.getEntityTypeByConceptId(
				Constants.AUTH_TOKEN, concepts.get(0).getId());
		if (type == null) {
			type = socialService.createEntityType(Constants.AUTH_TOKEN,
					concepts.get(0).getId());
		}
		Entity entity = new Entity();
		entity.setCreatorId(Constants.CREATOR_ID);
		entity.setDescription("entity description");
		entity.setName("entity test");
		entity.setType(type.getName());
		entity.setTags(Arrays.asList(concepts.get(0), concepts.get(1),
				concepts.get(2)));

		entity = socialService.createEntity(Constants.AUTH_TOKEN, entity);
		Assert.assertNotNull(entity);
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals("entity description", entity.getDescription());

		entity.setDescription("MODIFIED");
		Assert.assertTrue(socialService.updateEntity(Constants.AUTH_TOKEN,
				entity));
		Assert.assertTrue(socialService.deleteEntity(Constants.AUTH_TOKEN,
				entity.getId()));

		entity = new Entity();
		entity.setCreatorId(Constants.CREATOR_ID);
		entity.setDescription("entity description");
		entity.setName("entity test");
		entity.setTypeId(type.getId());

		entity = socialService.createEntity(Constants.AUTH_TOKEN, entity);
		Assert.assertNotNull(entity);
		Assert.assertTrue(socialService.deleteEntity(Constants.AUTH_TOKEN,
				entity.getId()));

	}

	@Test(expected = SocialServiceException.class)
	public void entitiesFailure() throws SecurityException,
			SocialServiceException {
		List<Concept> concepts = socialService.getConceptByPrefix(
				Constants.AUTH_TOKEN, "test", 1);
		Assert.assertNotNull(concepts);
		Assert.assertTrue(concepts.size() > 0);
		EntityType type = socialService.getEntityTypeByConceptId(
				Constants.AUTH_TOKEN, concepts.get(0).getId());
		if (type == null) {
			type = socialService.createEntityType(Constants.AUTH_TOKEN,
					concepts.get(0).getId());
		}
		Entity entity = new Entity();
		entity.setCreatorId(Constants.CREATOR_ID);
		entity.setDescription("entity description");
		entity.setName("entity test");
		entity.setTags(Arrays.asList(concepts.get(0), concepts.get(1),
				concepts.get(2)));

		Assert.assertNotNull(socialService.createEntity(Constants.AUTH_TOKEN,
				entity));
	}
}
