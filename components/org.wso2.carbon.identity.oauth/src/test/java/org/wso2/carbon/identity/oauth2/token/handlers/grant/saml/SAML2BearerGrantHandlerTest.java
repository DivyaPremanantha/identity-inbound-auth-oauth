/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.oauth2.token.handlers.grant.saml;

import org.joda.time.DateTime;
import org.mockito.Mock;
import org.opensaml.saml2.core.Assertion;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockObjectFactory;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.common.model.Claim;
import org.wso2.carbon.identity.application.common.model.ClaimMapping;
import org.wso2.carbon.identity.application.common.model.FederatedAuthenticatorConfig;
import org.wso2.carbon.identity.application.common.model.IdentityProvider;
import org.wso2.carbon.identity.application.common.model.LocalAndOutboundAuthenticationConfig;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.application.common.model.ServiceProvider;
import org.wso2.carbon.identity.application.common.util.IdentityApplicationConstants;
import org.wso2.carbon.identity.core.model.SAMLSSOServiceProviderDO;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.oauth.config.OAuthServerConfiguration;
import org.wso2.carbon.identity.oauth2.TestConstants;
import org.wso2.carbon.identity.oauth2.dto.OAuth2AccessTokenReqDTO;
import org.wso2.carbon.identity.oauth2.token.OAuthTokenReqMessageContext;
import org.wso2.carbon.identity.oauth2.token.OauthTokenIssuer;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import org.wso2.carbon.identity.sso.saml.SSOServiceProviderConfigManager;
import org.wso2.carbon.identity.sso.saml.dto.SAMLSSOAuthnReqDTO;
import org.wso2.carbon.identity.sso.saml.util.SAMLSSOUtil;
import org.wso2.carbon.idp.mgt.IdentityProviderManager;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.TenantManager;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@PowerMockIgnore({"javax.net.*"})

@PrepareForTest({IdentityUtil.class, IdentityTenantUtil.class, IdentityProviderManager.class, OAuth2Util.class,
        OAuthServerConfiguration.class, OAuth2AccessTokenReqDTO.class, SSOServiceProviderConfigManager.class})

public class SAML2BearerGrantHandlerTest extends PowerMockTestCase {

    private SAML2BearerGrantHandler saml2BearerGrantHandler;


    @ObjectFactory
    public IObjectFactory getObjectFactory() {

        return new PowerMockObjectFactory();
    }

    @Mock
    private OauthTokenIssuer oauthIssuer;

    @Mock
    public RealmService realmService;

    @Mock
    public TenantManager tenantManager;

    @Mock
    private IdentityProviderManager identityProviderManager;

    @Mock
    private IdentityProvider identityProvider;

    @Mock
    private FederatedAuthenticatorConfig federatedAuthenticatorConfig;

    @Mock
    private SSOServiceProviderConfigManager ssoServiceProviderConfigManager;

    @Mock
    private OAuthTokenReqMessageContext tokReqMsgCtx;

    @Mock
    private OAuth2AccessTokenReqDTO oAuth2AccessTokenReqDTO;

    @Mock
    private OAuthServerConfiguration oAuthServerConfiguration;

    @BeforeMethod
    public void setUp() throws Exception {

        mockStatic(OAuthServerConfiguration.class);
        when(OAuthServerConfiguration.getInstance()).thenReturn(oAuthServerConfiguration);

        when(oAuthServerConfiguration.getIdentityOauthTokenIssuer()).thenReturn(oauthIssuer);
        SAML2BearerGrantHandler saml2BearerGrantHandler = new SAML2BearerGrantHandler();
        saml2BearerGrantHandler.init();
    }

//    @Test
//    public void testInit() throws Exception {
//
//    }
//
//    @DataProvider(name = "ValidateGrantDataProvider")
//    public Object[][] buildScopeString() {
////        return new Object[][]{
////                {"randomUser", "wso2.com", "wso2.com", 1, true, "randomPassword", true, "DOMAIN", true, true},
////                {"randomUser", "wso2.com", "wso2.com", 1, true, "randomPassword", true, "DOMAIN", true, false}
////        };
//        return new Object[][]{
//                {"DOMAIN"},
//        };
//    }
//
//    @Test(dataProvider = "ValidateGrantDataProvider")
//    public void testValidateGrant(String domain) throws Exception {
//
//        oAuth2AccessTokenReqDTO = new OAuth2AccessTokenReqDTO();
//
//        tokReqMsgCtx = new OAuthTokenReqMessageContext(oAuth2AccessTokenReqDTO);
//        tokReqMsgCtx.setTenantID(-1234);
//        Assertion assertion = buildAssertion();
//        String ass = SAMLSSOUtil.marshall(assertion);
//        ass = new String(Base64.encodeBase64(ass.getBytes(Charsets.UTF_8)));
//        oAuth2AccessTokenReqDTO.setAssertion(ass);
//
//        saml2BearerGrantHandler = new SAML2BearerGrantHandler();
//        assertTrue(saml2BearerGrantHandler.validateGrant(tokReqMsgCtx));
//
//    }
//
//    @Test
//    public void testIssueRefreshToken() throws Exception {
//
//    }
//
//    @Test
//    public void testSetUser() throws Exception {
//
//    }
//
//    @Test
//    public void testSetFederatedUser() throws Exception {
//
//    }
//
//    @Test
//    public void testSetLocalUser() throws Exception {
//
//    }

    @Test
    public void testBuildLocalUser() throws Exception {

        tokReqMsgCtx = new OAuthTokenReqMessageContext(oAuth2AccessTokenReqDTO);
        Assertion assertion = buildAssertion();
        saml2BearerGrantHandler = new SAML2BearerGrantHandler();
        LocalAndOutboundAuthenticationConfig localAndOutboundAuthenticationConfig = new LocalAndOutboundAuthenticationConfig();
        localAndOutboundAuthenticationConfig.setUseTenantDomainInLocalSubjectIdentifier(false);
        localAndOutboundAuthenticationConfig.setUseUserstoreDomainInLocalSubjectIdentifier(true);
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setLocalAndOutBoundAuthenticationConfig(localAndOutboundAuthenticationConfig);
        AuthenticatedUser authenticatedUser = saml2BearerGrantHandler.buildLocalUser( tokReqMsgCtx, assertion,
                serviceProvider,TestConstants.CARBON_TENANT_DOMAIN);

        Assert.assertEquals(authenticatedUser.getTenantDomain(),TestConstants.CARBON_TENANT_DOMAIN);
        Assert.assertEquals(authenticatedUser.getUserName(),TestConstants.TEST_USER_NAME);

    }

    @Test
    public void testCreateLegacyUser() throws Exception {

        Assertion assertion = buildAssertion();
        mockStatic(OAuth2Util.class);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setAuthenticatedSubjectIdentifier(assertion.getSubject().getNameID().getValue());
        when(OAuth2Util.getUserFromUserName(anyString())).thenReturn(authenticatedUser);
        tokReqMsgCtx = new OAuthTokenReqMessageContext(new OAuth2AccessTokenReqDTO());
        saml2BearerGrantHandler = new SAML2BearerGrantHandler();
        saml2BearerGrantHandler.createLegacyUser(tokReqMsgCtx, assertion);
        String subject = tokReqMsgCtx.getAuthorizedUser().getAuthenticatedSubjectIdentifier();
        Assert.assertEquals(subject, authenticatedUser.getAuthenticatedSubjectIdentifier());
    }

    public void prepareForGetIssuer() throws Exception {

        when(tenantManager.getTenantId(anyString())).thenReturn(-1234);
        when(realmService.getTenantManager()).thenReturn(tenantManager);
        SAMLSSOUtil.setRealmService(realmService);

        Property property = new Property();
        property.setName(IdentityApplicationConstants.Authenticator.SAML2SSO.IDP_ENTITY_ID);
        property.setValue(TestConstants.LOACALHOST_DOMAIN);
        Property[] properties = {property};
        when(federatedAuthenticatorConfig.getProperties()).thenReturn(properties);
        when(federatedAuthenticatorConfig.getName()).thenReturn(
                IdentityApplicationConstants.Authenticator.SAML2SSO.NAME);
        FederatedAuthenticatorConfig[] fedAuthConfs = {federatedAuthenticatorConfig};
        when(identityProvider.getFederatedAuthenticatorConfigs()).thenReturn(fedAuthConfs);

        mockStatic(IdentityProviderManager.class);
        when(IdentityProviderManager.getInstance()).thenReturn(identityProviderManager);
        when(identityProviderManager.getResidentIdP(anyString())).thenReturn(identityProvider);
    }

    private void prepareForUserAttributes(String attrConsumerIndex, String issuer, String spName) {

        mockStatic(SSOServiceProviderConfigManager.class);
        when(SSOServiceProviderConfigManager.getInstance()).thenReturn(ssoServiceProviderConfigManager);
        SAMLSSOServiceProviderDO samlssoServiceProviderDO = new SAMLSSOServiceProviderDO();
        samlssoServiceProviderDO.setAttributeConsumingServiceIndex(attrConsumerIndex);
        samlssoServiceProviderDO.setEnableAttributesByDefault(true);
        samlssoServiceProviderDO.setIssuer(issuer);
        ssoServiceProviderConfigManager.addServiceProvider(issuer, samlssoServiceProviderDO);
        when(ssoServiceProviderConfigManager.getServiceProvider(spName)).thenReturn(samlssoServiceProviderDO);
    }

    public Assertion buildAssertion() throws Exception {

        prepareForGetIssuer();
        mockStatic(IdentityUtil.class);
        mockStatic(IdentityTenantUtil.class);
        when(IdentityUtil.getServerURL(anyString(), anyBoolean(), anyBoolean()))
                .thenReturn(TestConstants.SAMPLE_SERVER_URL);
        prepareForUserAttributes(TestConstants.ATTRIBUTE_CONSUMER_INDEX, TestConstants.LOACALHOST_DOMAIN,
                TestConstants.LOACALHOST_DOMAIN);
        Map<String, String> inputAttributes = new HashMap<>();
        inputAttributes.put(TestConstants.CLAIM_URI1, TestConstants.CLAIM_VALUE1);
        inputAttributes.put(TestConstants.CLAIM_URI2, TestConstants.CLAIM_VALUE2);
        SAMLSSOAuthnReqDTO authnReqDTO = buildAuthnReqDTO(inputAttributes, TestConstants.SAMPLE_NAME_ID_FORMAT,
                TestConstants.LOACALHOST_DOMAIN, TestConstants.TEST_USER_NAME);
        authnReqDTO.setNameIDFormat(TestConstants.SAMPLE_NAME_ID_FORMAT);
        authnReqDTO.setIssuer(TestConstants.LOACALHOST_DOMAIN);
        Assertion assertion = SAMLSSOUtil.buildSAMLAssertion(authnReqDTO, new DateTime(00000000L), TestConstants
                .SESSION_ID);
        return assertion;
    }

    public ClaimMapping buildClaimMapping(String claimUri) {

        ClaimMapping claimMapping = new ClaimMapping();
        Claim claim = new Claim();
        claim.setClaimUri(claimUri);
        claimMapping.setRemoteClaim(claim);
        claimMapping.setLocalClaim(claim);
        return claimMapping;
    }

    public SAMLSSOAuthnReqDTO buildAuthnReqDTO(Map<String, String> attributes, String nameIDFormat, String issuer,
                                               String subjectName) {

        SAMLSSOAuthnReqDTO authnReqDTO = new SAMLSSOAuthnReqDTO();
        authnReqDTO.setUser(AuthenticatedUser.createFederateAuthenticatedUserFromSubjectIdentifier(subjectName));
        authnReqDTO.setNameIDFormat(nameIDFormat);
        authnReqDTO.setIssuer(issuer);
        Map<ClaimMapping, String> userAttributes = new HashMap<>();

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            userAttributes.put(buildClaimMapping(entry.getKey()), entry.getValue());
        }
        authnReqDTO.getUser().setUserAttributes(userAttributes);
        return authnReqDTO;
    }

}

