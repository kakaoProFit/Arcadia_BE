//package profit.login.database;
//
//import jakarta.activation.DataSource;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static profit.login.database.DataSourceConfig.MASTER_DATASOURCE;
//import static profit.login.database.DataSourceConfig.SLAVE_DATASOURCE;
//
//public class RoutingDataSource extends AbstractRoutingDataSource {
//    @Override
//    protected Object determineCurrentLookupKey() { // (1)
//        return (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) ? "slave" : "master"; //(2)
//    }
//
//    @Bean
//    @Primary
//    @DependsOn({MASTER_DATASOURCE, SLAVE_DATASOURCE})
//    public DataSource routingDataSource(
//            @Qualifier(MASTER_DATASOURCE) DataSource masterDataSource,
//            @Qualifier(SLAVE_DATASOURCE) DataSource slaveDataSource) {
//
//        RoutingDataSource routingDataSource = new RoutingDataSource();
//
//        Map<Object, Object> datasourceMap = new HashMap<>() {
//            {
//                put("master", masterDataSource);
//                put("slave", slaveDataSource);
//            }
//        };
//
//        routingDataSource.setTargetDataSources(datasourceMap);
//        routingDataSource.setDefaultTargetDataSource(masterDataSource);
//
//        return (DataSource) routingDataSource;
//    }
//}
