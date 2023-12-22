package hrahman.ddp.rest.config;

import com.bettercloud.vault.VaultException;
import hrahman.ddp.hibernate.util.Constants;
import org.hrahman.ddp.vault.VaultPropertyRetriever;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DataSourceConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VaultPropertyRetriever vault() throws VaultException {
        return new VaultPropertyRetriever();
    }

    @Bean(name = "dataSource")
    public DataSource ddpDataSourceBean() throws VaultException {

        Properties properties = vault().getVaultProperties(Constants.DDP_DB_PATH);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(properties.getProperty(Constants.DDP_DB_DRIVER));
        dataSource.setUrl(properties.getProperty(Constants.DDP_DB_URL));
        dataSource.setUsername(properties.getProperty(Constants.DDP_DB_USER));
        dataSource.setPassword(properties.getProperty(Constants.DDP_DB_PASS));

        return dataSource;
    }
}
