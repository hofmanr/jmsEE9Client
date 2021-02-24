package nl.hofmanr.jms.client.repository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class QueueRepository extends BaseRepository<Queue> {
    private static final Logger LOGGER = Logger.getLogger(QueueRepository.class.getName());

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
                        LOGGER.severe(e.getMessage());
                    }
                    return false;
                })
                .findFirst().orElse(null);
    }

    public List<String> findMessages(Queue queue) {
        Connection connection = null;
        Session session = null;
        List<String> messages = new ArrayList<>();

        try {
            // Get connection and session
            connection = connectionFactoryRepository.findFirst().createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            try (MessageConsumer messageConsumer = session.createConsumer(queue)) {
                boolean found = true;
                while (found) {
                    Message jmsMessage = messageConsumer.receiveNoWait();
                    if (jmsMessage != null) {
                        if (jmsMessage instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage) jmsMessage;
                            System.out.println("Message: " + textMessage.getText());
                            messages.add(textMessage.getText());
                        }
                    } else {
                        found = false;
                    }
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            tearDown(connection, session);
        }

        return messages;
    }

    public List<String> findAllMessages(Queue queue) {
        Connection connection = null;
        Session session = null;

        try {
            // Get connection and session
            connection = connectionFactoryRepository.findFirst().createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            try (QueueBrowser queueBrowser = session.createBrowser(queue)) {
                Enumeration e = queueBrowser.getEnumeration();
                List<Message> result = Collections.list(e);
                return result.stream()
                        .map(m -> {
                            if (m instanceof TextMessage) {
                                TextMessage textMessage = (TextMessage) m;
                                try {
                                    return textMessage.getText();
                                } catch (JMSException jmsException) {
                                    jmsException.printStackTrace();
                                }
                            }
                            return null;
                        })
                        .collect(Collectors.toList());
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            tearDown(connection, session);
        }
        return Collections.emptyList();
    }

    public void insertMessage(Queue queue, String message) {
        Connection connection = null;
        Session session = null;

        // Get connection and session
        try {
            connection = connectionFactoryRepository.findFirst().createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // MessageProducer
            try (MessageProducer messageProducer = session.createProducer(queue)) {
                messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

                // Send message
                TextMessage textMessage = session.createTextMessage(message);
                messageProducer.send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            tearDown(connection, session);
        }
    }

    public int count(Queue queue) {
        Connection connection = null;
        Session session = null;

        // Get connection and session
        try {
            connection = connectionFactoryRepository.findFirst().createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            try (QueueBrowser queueBrowser = session.createBrowser(queue)) {
                Enumeration e = queueBrowser.getEnumeration();
                return Collections.list(e).size();
            } catch (JMSException e) {
                e.printStackTrace();
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            tearDown(connection, session);
        }
        return 0;
    }

    public void deleteMessages(Queue queue) {
        Connection connection = null;
        Session session = null;
        try {
            // Get connection and session
            connection = connectionFactoryRepository.findFirst().createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            try (MessageConsumer messageConsumer = session.createConsumer(queue)) {
                while (messageConsumer.receiveNoWait() != null);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            tearDown(connection, session);
        }
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
            e.printStackTrace();
        }
    }

}
