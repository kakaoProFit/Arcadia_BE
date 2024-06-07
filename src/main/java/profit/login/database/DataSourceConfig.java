//package profit.login.database;
//
//import javax.sql.DataSource;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//@Configuration
//@EnableConfigurationProperties
//public class DataSourceConfig {
//
//    public static final String MASTER_DATASOURCE = "masterDataSource";
//    public static final String SLAVE_DATASOURCE = "slaveDataSource";
//
//    @Bean(MASTER_DATASOURCE)
//    @ConfigurationProperties(prefix = "spring.datasource.master") // (1)
//    public DataSource masterDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
//
//    @Bean(SLAVE_DATASOURCE)
//    @ConfigurationProperties(prefix = "spring.datasource.slave1")
//    public DataSource slaveDataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
//
//
//}
