package nl.hofmanr.jms.client.service;

import nl.hofmanr.jms.client.domain.JmsMessage;
import nl.hofmanr.jms.client.domain.JmsMessageBuilder;
import nl.hofmanr.jms.client.repository.ConnectionFactoryRepository;
import nl.hofmanr.jms.client.repository.QueueRepository;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QueueService {

    @Inject
    QueueRepository queueRepository;

    @Inject
    ConnectionFactoryRepository connectionFactoryRepository;

    public List<String> getQueues() {
        connectionFactoryRepository.findFirst();
        return queueRepository.getQueues().stream()
                .map(q -> {
                    try {
                        return q.getQueueName();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    public List<JmsMessage> getMessages(String queueName) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return Collections.emptyList();
        }
        List<TextMessage> textMessages = queueRepository.findAllMessages(queue);
        return textMessages.stream().map(t -> {
            try {
                return JmsMessageBuilder.newBuilder()
                        .addMessageID(t.getJMSMessageID())
                        .addCorrelationID(t.getJMSCorrelationID())
                        .addTimestamp(t.getJMSTimestamp())
                        .addMessage(t.getText())
                        .build();
            } catch (JMSException e) {
                e.printStackTrace();
                return null;
            }
        })
                .collect(Collectors.toList());
    }

    public JmsMessage getMessage(String queueName, String messageID) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return null;
        }
        TextMessage textMessage = queueRepository.findMessageByID(queue, messageID);
        try {
            return textMessage == null ? null :
                    JmsMessageBuilder.newBuilder()
                            .addMessageID(textMessage.getJMSMessageID())
                            .addCorrelationID(textMessage.getJMSCorrelationID())
                            .addTimestamp(textMessage.getJMSTimestamp())
                            .addMessage(textMessage.getText())
                            .build();
        } catch (JMSException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addMessage(String queueName, String message) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return null;
        }
        return queueRepository.insertMessage(queue, message);
    }

    public void deleteMessage(String queueName, String message) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return;
        }
        queueRepository.deleteMessage(queue, message);
    }

    public int countMessages(String queueName) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return 0;
        }
        return queueRepository.count(queue);
    }

    public void deleteMessages(String queueName) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return;
        }
        queueRepository.deleteMessages(queue);
    }

}
