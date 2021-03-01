package nl.hofmanr.jms.client.repository;

import nl.hofmanr.jms.client.exception.DataAccessException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class QueueRepository extends BaseRepository<Queue> {

    @Inject
    ConnectionFactoryRepository connectionFactoryRepository;

    private List<Queue> queues = new ArrayList<>();

    public List<Queue> getQueues() {
        if (queues.isEmpty()) {
            queues = this.findByType(Queue.class);
        }
        return queues;
    }

    public Queue getQueue(String queueName) {
        return this.getQueues().stream()
                .filter(q -> {
                    try {
                        return q.getQueueName().equalsIgnoreCase(queueName);
                    } catch (JMSException e) {
                        throw new DataAccessException("Error retrieving queue", e);
                    }
                })
                .findFirst().orElseThrow(() -> new DataAccessException("No queue with name " + queueName));
    }

    private <T extends Session, R> R execute(Function<T, R> function) {
        Connection connection = null;
        Session session = null;

        try {
            // Get connection and session
            connection = connectionFactoryRepository.findFirst().createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            return function.apply((T) session);

        } catch (JMSException e) {
            throw new DataAccessException("Error executing query", e);
        } finally {
            tearDown(connection, session);
        }
    }

    public List<TextMessage> findAllMessages(Queue queue) {
        Function<Session, List<TextMessage>> browseQueue = (session) -> {
            try (QueueBrowser queueBrowser = session.createBrowser(queue)) {
                Enumeration e = queueBrowser.getEnumeration();
                List<Message> result = Collections.list(e);
                return result.stream()
                        .filter(m -> m instanceof TextMessage)
                        .map(m -> (TextMessage) m)
                        .collect(Collectors.toList());
            } catch (JMSException e) {
                throw new DataAccessException("Error retrieving messages from queue", e);
            }
        };

        return execute(browseQueue);
    }

    public TextMessage findMessageByID(Queue queue, String messageID) {
        Function<Session, TextMessage> browseQueue = (session) -> {
            try (QueueBrowser queueBrowser = session.createBrowser(queue)) {
                Enumeration e = queueBrowser.getEnumeration();
                List<Message> result = Collections.list(e);
                return result.stream()
                        .filter(m -> m instanceof TextMessage)
                        .filter(m -> {
                            try {
                                return m.getJMSMessageID().equals(messageID);
                            } catch (JMSException jmsException) {
                                jmsException.printStackTrace();
                                return false;
                            }
                        })
                        .map(m -> (TextMessage) m)
                        .findFirst().orElse(null);
            } catch (JMSException e) {
                throw new DataAccessException("Error retrieving message with ID " + messageID + " from queue", e);
            }
        };

        return execute(browseQueue);
    }

    public String insertMessage(Queue queue, String message) {
        Function<Session, String> sendMessage = (session) -> {
            // MessageProducer
            try (MessageProducer messageProducer = session.createProducer(queue)) {
                messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
                final AtomicReference<Message> atomicReference = new AtomicReference<>();

                // Send message
                TextMessage textMessage = session.createTextMessage(message);
                atomicReference.set(textMessage);
                messageProducer.send(textMessage);
                return atomicReference.get().getJMSMessageID();
            } catch (JMSException e) {
                throw new DataAccessException("Error sending message to queue", e);
            }
        };

        return execute(sendMessage);
    }

    public boolean deleteMessage(Queue queue, String jmsMessageID) {
        Function<Session, Boolean> deleteMessage = (session) -> {
            // MessageConsumer with filter
            try (MessageConsumer messageConsumer = session.createConsumer(queue, "JMSMessageID='"+jmsMessageID+"'")) {
                // Recieve Message
                Message message = messageConsumer.receiveNoWait();
                if (message == null)
                    return false;
            } catch (JMSException e) {
                throw new DataAccessException("Error receiving message from queue", e);
            }
            return true;
        };

        return execute(deleteMessage);
    }

    public int count(Queue queue) {
        Function<Session, Integer> getQueueSize = (session) -> {
            try (QueueBrowser queueBrowser = session.createBrowser(queue)) {
                Enumeration e = queueBrowser.getEnumeration();
                return Collections.list(e).size();
            } catch (JMSException e) {
                throw new DataAccessException("Error count messages", e);
            }
        };

        return execute(getQueueSize);
    }

    public boolean deleteMessages(Queue queue) {
        Function<Session, Boolean> readAllMessages = (session) -> {
            try (MessageConsumer messageConsumer = session.createConsumer(queue)) {
                while (messageConsumer.receiveNoWait() != null) ;
            } catch (JMSException e) {
                throw new DataAccessException("Error deleting messages from queue", e);
            }
            return true;
        };

        return execute(readAllMessages);
    }

    private void tearDown(Connection connection, Session session) {
        try {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
            throw new DataAccessException("Error closing resources", e);
        }
    }

}
