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
import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;
import eu.trentorise.smartcampus.socialservice.model.SharedContent;

public class TestClient {

	private static final String AUTH_TOKEN = "";
	private static final long CREATOR_ID = -1l;

	private SocialService socialService;

	@Before
	public void init() {
		socialService = new SocialService("http://localhost:8080");
	}

	@Test
	public void groups() throws SecurityException, SocialServiceException {
		Assert.assertNotNull(socialService.getGroups(AUTH_TOKEN));
	}

	@Test
	public void communities() throws SecurityException, SocialServiceException {
		Assert.assertNotNull(socialService.getCommunities(AUTH_TOKEN));
	}

	@Test
	public void topics() throws SecurityException, SocialServiceException {
		Assert.assertNotNull(socialService.getTopics(AUTH_TOKEN));
	}

	@Test
	public void sharing() throws SecurityException, SocialServiceException {

		ShareVisibility visibility = new ShareVisibility();
		visibility.setAllCommunities(true);
		visibility.setAllKnownCommunities(true);
		visibility.setAllKnownUsers(true);
		visibility.setAllUsers(true);

		socialService.getSharedContents(AUTH_TOKEN, visibility, 0, 10, null);
		List<SharedContent> contents = socialService.getMyContents(AUTH_TOKEN,
				0, 5, null);
		Assert.assertTrue(contents.size() > 0);

		ShareOperation shareOperation = new ShareOperation();
		shareOperation.setEntityId(contents.get(0).getEntityId());
		shareOperation.setVisibility(visibility);
		Assert.assertTrue(socialService.share(AUTH_TOKEN, shareOperation));

		Assert.assertNotNull(socialService.getShareVisibility(AUTH_TOKEN,
				contents.get(0).getEntityId()));
	}

	@Test
	public void entityTypes() throws SecurityException, SocialServiceException {
		List<Concept> concepts = socialService.getConceptByPrefix(AUTH_TOKEN,
				"test", 1);
		Assert.assertNotNull(concepts);
		Assert.assertTrue(concepts.size() > 0);
		Assert.assertNull(socialService.getEntityTypeByConceptId(AUTH_TOKEN,
				concepts.get(0).getId()));
		socialService.createEntityType(AUTH_TOKEN, concepts.get(0).getId());
		Assert.assertNotNull(socialService.getEntityTypeByConceptId(AUTH_TOKEN,
				concepts.get(0).getId()));
	}

	@Test
	public void entities() throws SecurityException, SocialServiceException {
		List<Concept> concepts = socialService.getConceptByPrefix(AUTH_TOKEN,
				"test", 1);
		Assert.assertNotNull(concepts);
		Assert.assertTrue(concepts.size() > 0);
		EntityType type = socialService.getEntityTypeByConceptId(AUTH_TOKEN,
				concepts.get(0).getId());
		if (type == null) {
			type = socialService.createEntityType(AUTH_TOKEN, concepts.get(0)
					.getId());
		}
		Entity entity = new Entity();
		entity.setCreatorId(CREATOR_ID);
		entity.setDescription("entity description");
		entity.setName("entity test");
		entity.setType(type.getName());
		entity.setTags(Arrays.asList(concepts.get(0), concepts.get(1),
				concepts.get(2)));

		entity = socialService.createEntity(AUTH_TOKEN, entity);
		Assert.assertNotNull(entity);
		Assert.assertNotNull(entity.getId());
		Assert.assertEquals("entity description", entity.getDescription());

		entity.setDescription("MODIFIED");
		Assert.assertTrue(socialService.updateEntity(AUTH_TOKEN, entity));
		Assert.assertTrue(socialService.deleteEntity(AUTH_TOKEN, entity.getId()));
	}
}
