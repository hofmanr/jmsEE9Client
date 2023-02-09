package nl.hofmanr.jms.client.service;

import nl.hofmanr.jms.client.domain.*;
import nl.hofmanr.jms.client.exception.DataAccessException;
import nl.hofmanr.jms.client.exception.ServiceException;
import nl.hofmanr.jms.client.repository.ConnectionFactoryRepository;
import nl.hofmanr.jms.client.repository.QueueRepository;

import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import java.util.List;
import java.util.stream.Collectors;

public class QueueService {

    @Inject
    private QueueRepository queueRepository;

//    @Inject
//    private QueueAssembler queueAssembler;

    @Inject
    private JmsQueueAssembler jmsQueueAssembler;

    @Inject
    private JmsMessageAssembler jmsMessageAssembler;

    @Inject
    ConnectionFactoryRepository connectionFactoryRepository;

    public List<JmsQueue> getQueues() {
        try {
            connectionFactoryRepository.findFirst();
            return queueRepository.getQueues().stream()
                    .map(jmsQueueAssembler::toJmsQueue)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new ServiceException("Error getting queues", e);
        }
    }

    public List<JmsMessage> getMessages(String queueName) {
        try {
            Queue queue = queueRepository.getQueue(queueName);
            List<TextMessage> textMessages = queueRepository.findAllMessages(queue);
            return textMessages.stream()
                    .map(jmsMessageAssembler::toJmsMessage)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new ServiceException("Error getting messages from queue " + queueName, e);
        }
    }

    public JmsPayload getMessage(String queueName, String messageID) {
        try {
            Queue queue = queueRepository.getQueue(queueName);
            TextMessage textMessage = queueRepository.findMessageByID(queue, messageID);
            return new JmsPayload(textMessage.getText());
        } catch (DataAccessException | JMSException e) {
            throw new ServiceException("Error getting message from queue " + queueName + " with ID " + messageID, e);
        }
    }

    public String addMessage(String queueName, String message) {
        try {
            Queue queue = queueRepository.getQueue(queueName);
            return queueRepository.insertMessage(queue, message);
        } catch (DataAccessException e) {
            throw new ServiceException("Error add message to queue " + queueName, e);
        }
    }

    public void deleteMessage(String queueName, String message) {
        try {
            Queue queue = queueRepository.getQueue(queueName);
            queueRepository.deleteMessage(queue, message);
        } catch (DataAccessException e) {
            throw new ServiceException("Error deleting message from queue " + queueName, e);
        }
    }

    public int countMessages(String queueName) {
        try {
            Queue queue = queueRepository.getQueue(queueName);
            return queueRepository.count(queue);
        } catch (DataAccessException e) {
            throw new ServiceException("Error counting messages on queue " + queueName, e);
        }
    }

    public void deleteMessages(String queueName) {
        try {
            Queue queue = queueRepository.getQueue(queueName);
            queueRepository.deleteMessages(queue);
        } catch (DataAccessException e) {
            throw new ServiceException("Error deleting all messages on queue " + queueName, e);
        }
    }

}
