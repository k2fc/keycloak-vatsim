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

import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;

/**
 * @author <a href="mailto:dennis.graiani@gmail.com">Dennis Graiani</a>
 */
public class VatsimIdentityProviderFactory extends AbstractIdentityProviderFactory<VatsimIdentityProvider>
        implements SocialIdentityProviderFactory<VatsimIdentityProvider> {

    public static final String PROVIDER_ID = "vatsim";

    @Override
    public String getName() {
        return "VATSIM";
    }

    @Override
    public VatsimIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new VatsimIdentityProvider(session, new VatsimIdentityProviderConfig(model));
    }

    @Override
    public VatsimIdentityProviderConfig createConfig() {
        return new VatsimIdentityProviderConfig();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ProviderConfigurationBuilder.create()
                .property().name("sandbox")
                .type(ProviderConfigProperty.BOOLEAN_TYPE)
                .label("Target Sandbox")
                .helpText("Target VATSIM's sandbox environment")
                .add().build();
    }
}