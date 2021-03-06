package org.soluvas.web.login.google;

import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soluvas.commons.WebAddress;
import org.soluvas.web.login.GoogleManager;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.PlusScopes;
import com.google.common.collect.ImmutableList;

public class GoogleLoginLink extends StatelessLink<Void> {
			
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(GoogleLoginLink.class);
	
	@SpringBean(name="googleMgr", required=false)
	private GoogleManager googleMgr;
	@SpringBean
	private WebAddress webAddress;

	public GoogleLoginLink(String id) {
		super(id);
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
//		setVisible( googleMgr != null );
		// TODO: always disabled for now. enable if Google login (ClientLogin, not Plus?) is working properly
		setVisible( false );
	}

	@Override
	public void onClick() {
		/** just config test **/
//					final String token_url = "https://api.twitter.com/oauth/request_token ";
		final String redirectUri = webAddress.getBaseUri() + "google_recipient/";
		final GoogleAuthorizationCodeFlow authorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(
				new NetHttpTransport(), new JacksonFactory(),
				googleMgr.getClientId(), googleMgr.getClientSecret(),
				ImmutableList.of(PlusScopes.PLUS_ME))
				.setAccessType("offline")
		        .build();
		final String authorizationUri = authorizationCodeFlow.newAuthorizationUrl().setRedirectUri(redirectUri).build();
		log.debug("Google Autorize url {}", authorizationUri);
		throw new RedirectToUrlException(authorizationUri);
		// .setCredentialStore(
	    // new JdoCredentialStore(JDOHelper.getPersistenceManagerFactory("transactions-optional")))
	}

}
