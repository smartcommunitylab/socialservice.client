package eu.trentorise.smartcampus.socialservice;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.trentorise.smartcampus.socialservice.model.ShareOperation;
import eu.trentorise.smartcampus.socialservice.model.ShareVisibility;
import eu.trentorise.smartcampus.socialservice.model.SharedContent;

public class TestClient {

	private static final String AUTH_TOKEN = "";

	private SocialService socialService;

	@Before
	public void init() {
		socialService = new SocialService("https://vas-dev.smartcampuslab.it");
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
}
