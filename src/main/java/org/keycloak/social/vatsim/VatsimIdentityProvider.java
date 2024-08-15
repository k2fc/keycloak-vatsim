/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.social.vatsim;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.ErrorPageException;
import org.keycloak.services.messages.Messages;

import java.util.Set;

/**
 * @author <a href="mailto:dennis.graiani@gmail.com">Dennis Graiani</a>
 */
public class VatsimIdentityProvider extends AbstractOAuth2IdentityProvider<VatsimIdentityProviderConfig>
        implements SocialIdentityProvider<VatsimIdentityProviderConfig> {

    private static final Logger log = Logger.getLogger(VatsimIdentityProvider.class);

    public static String AUTH_URL = "https://auth.vatsim.net/oauth/authorize";
    public static String TOKEN_URL = "https://auth.vatsim.net/oauth/token";
    public static String PROFILE_URL = "https://auth.vatsim.net/api/user";
    public static final String DEFAULT_SCOPE = "full_name email";

    public VatsimIdentityProvider(KeycloakSession session, VatsimIdentityProviderConfig config) {
        super(session, config);
        AUTH_URL = config.targetSandbox() ? "https://auth-dev.vatsim.net/oauth/authorize" : AUTH_URL;
        TOKEN_URL = config.targetSandbox() ? "https://auth-dev.vatsim.net/oauth/token" : TOKEN_URL;
        PROFILE_URL = config.targetSandbox() ? "https://auth-dev.vatsim.net/api/user" : PROFILE_URL;
        config.setAuthorizationUrl(AUTH_URL);
        config.setTokenUrl(TOKEN_URL);
        config.setUserInfoUrl(PROFILE_URL);
    }

    @Override
    protected boolean supportsExternalExchange() {
        return true;
    }

    @Override
    protected String getProfileEndpointForValidation(EventBuilder event) {
        return PROFILE_URL;
    }

    @Override
    protected BrokeredIdentityContext extractIdentityFromProfile(EventBuilder event, JsonNode profile) {
        var userData = profile.get("data");
        var personalData = userData.get("personal");
        BrokeredIdentityContext user = new BrokeredIdentityContext(getJsonProperty(userData, "cid"), getConfig());


        user.setUsername(getJsonProperty(userData, "email"));
        user.setEmail(getJsonProperty(personalData, "email"));
        user.setFirstName(getJsonProperty(personalData, "name_first"));
        user.setLastName(getJsonProperty(personalData, "name_last"));
        user.setIdp(this);

        AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, getConfig().getAlias());

        return user;
    }

    @Override
    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
        log.debug("doGetFederatedIdentity()");
        JsonNode profile = null;
        try {
            profile = SimpleHttp.doGet(PROFILE_URL, session).header("Authorization", "Bearer " + accessToken).asJson();
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not obtain user profile from vatsim.", e);
        }

        return extractIdentityFromProfile(null, profile);
    }


    @Override
    protected String getDefaultScopes() {
        return DEFAULT_SCOPE;
    }
}
