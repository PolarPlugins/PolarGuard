package it.polar.polarguard.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import it.polar.polarguard.PolarGuardApplication;
import it.polar.polarguard.database.utils.StringToUUIDConverter;
import it.polar.polarguard.database.utils.UUIDToStringConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        String beforeQuestionMark = PolarGuardApplication.getInstance().getMongoConnectionUri().split("\\?")[0];
        return beforeQuestionMark.substring(beforeQuestionMark.lastIndexOf("/") + 1);
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.applyConnectionString(new ConnectionString(PolarGuardApplication.getInstance().getMongoConnectionUri()));
    }

    @Override
    protected void configureConverters(MongoCustomConversions.MongoConverterConfigurationAdapter adapter) {
        adapter.registerConverter(new UUIDToStringConverter());
        adapter.registerConverter(new StringToUUIDConverter());
    }
}
