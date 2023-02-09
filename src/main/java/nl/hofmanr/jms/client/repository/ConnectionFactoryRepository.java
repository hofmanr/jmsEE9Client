package nl.hofmanr.jms.client.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jms.ConnectionFactory;

@ApplicationScoped
public class ConnectionFactoryRepository extends BaseRepository<ConnectionFactory> {

    private ConnectionFactory connectionFactory = null;

    public ConnectionFactory findFirst() {
        if (connectionFactory == null) {
            connectionFactory = this.findByType(ConnectionFactory.class).stream().findFirst().orElse(null);
        }
        return connectionFactory;
    }

}
