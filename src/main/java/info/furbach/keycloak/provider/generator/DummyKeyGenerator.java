package info.furbach.keycloak.provider.generator;

import org.jboss.logging.Logger;
import org.keycloak.common.util.RandomString;

public class  DummyKeyGenerator implements KeyGenerator {

  private static final Logger LOG = Logger.getLogger(DummyKeyGenerator.class);
  private String url;

  public DummyKeyGenerator(String url) {
    LOG.info("Created DummyKeyGenerator with url: " + url);
    this.url = url;
  }

  @Override
  public String generateKey(String username) {
    String key = username + ":" + RandomString.randomCode(8);
    LOG.info("Generated key: " + key);
    return key;
  }
}
