package info.furbach.keycloak.provider;

import info.furbach.keycloak.provider.generator.DummyKeyGenerator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

public class KeyGenerationFormActionFactory implements FormAction, FormActionFactory,
    ServerInfoAwareProviderFactory {

  public static final String PROVIDER_ID = "key-generation-action";

  private static final Logger LOG = Logger.getLogger(KeyGenerationFormActionFactory.class);
  private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
      AuthenticationExecutionModel.Requirement.REQUIRED,
      AuthenticationExecutionModel.Requirement.DISABLED
  };
  private DummyKeyGenerator dummyKeyGenerator;
  private String url = "";

  @Override
  public String getDisplayType() {
    return "Generate F44 space key.";
  }

  @Override
  public String getReferenceCategory() {
    return "email";
  }

  @Override
  public boolean isConfigurable() {
    return false;
  }

  @Override
  public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
    return REQUIREMENT_CHOICES;
  }

  @Override
  public boolean isUserSetupAllowed() {
    return false;
  }

  @Override
  public String getHelpText() {
    return "";
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return null;
  }

  @Override
  public FormAction create(KeycloakSession session) {
    LOG.debug("KeyGenerationFormActionFactory created successfully.");
    return this;
  }

  @Override
  public void init(Config.Scope config) {
    LOG.info("KeyGenerationFormActionFactory init, config: " + config.toString());
    String url = config.get("url");
    dummyKeyGenerator = new DummyKeyGenerator(url);
    LOG.info("KeyGenerationFormActionFactory init, read api url: " + url);
  }

  @Override
  public void postInit(KeycloakSessionFactory sessionFactory) {
    LOG.debug("KeyGenerationFormActionFactory postInit, sessionFactory: " + sessionFactory);
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public void buildPage(FormContext context, LoginFormsProvider form) {
  }

  @Override
  public void validate(ValidationContext context) {
    context.success();
  }

  @Override
  public void success(FormContext context) {
    LOG.info("EmailFormActionFactory success");
    UserModel user = context.getUser();
    user.setSingleAttribute("f44_key", dummyKeyGenerator.generateKey(user.getUsername()));
  }

  @Override
  public boolean requiresUser() {
    return false;
  }

  @Override
  public boolean configuredFor(KeycloakSession keycloakSession, RealmModel realmModel,
      UserModel userModel) {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel,
      UserModel userModel) {

  }

  @Override
  public void close() {

  }

  /**
   * Return actual info about the provider. This info contains informations about providers
   * configuration and operational conditions (eg. errors in connection to remote systems etc) which
   * is shown on "Server Info" page then.
   *
   * @return Map with keys describing value and relevant values itself
   */
  @Override
  public Map<String, String> getOperationalInfo() {
    Map<String, String> ret = new LinkedHashMap<>();
    ret.put("url", "The api address " + url + "  configured to generate user keys.");
    return ret;
  }
}
