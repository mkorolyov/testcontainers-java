package org.testcontainers.containers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.KeyspaceMetadata;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ScyllaDBDriver4Test {

    @Rule
    public ScyllaDBContainer<?> scyllaDB = new ScyllaDBContainer<>("scylladb/scylla:5.2.9");

    @Test
    public void testCassandraGetContactPoint() {
        try (
            // cassandra {
            CqlSession session = CqlSession
                .builder()
                .addContactPoint(this.scyllaDB.getContactPoint())
                .withLocalDatacenter(this.scyllaDB.getLocalDatacenter())
                .build()
            // }
        ) {
            session.execute(
                "CREATE KEYSPACE IF NOT EXISTS test WITH replication = \n" +
                "{'class':'SimpleStrategy','replication_factor':'1'};"
            );

            KeyspaceMetadata keyspace = session.getMetadata().getKeyspaces().get(CqlIdentifier.fromCql("test"));

            assertThat(keyspace).as("keyspace created").isNotNull();
        }
    }
}