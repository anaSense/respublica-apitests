package config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:properties/base.properties"
})
public interface BaseConfig extends Config {
    @Key("baseUrl")
    String baseUrl();
    @Key("basePath")
    String basePath();
}
