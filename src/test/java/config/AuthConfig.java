package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "classpath:properties/auth.properties"})
public interface AuthConfig extends Config {
    @Key("email")
    String email();

    @Key("password")
    String password();
}
