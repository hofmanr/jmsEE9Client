package nl.hofmanr.jms.client.service;

import nl.hofmanr.jms.client.repository.ConnectionFactoryRepository;
import nl.hofmanr.jms.client.repository.QueueRepository;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Queue;
//import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
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

    public List<String> getAllMessages(String queueName) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return Collections.emptyList();
        }
//        return queueRepository.findMessages(queue);
        return queueRepository.findAllMessages(queue);
    }

    public boolean addMessage(String queueName, String message) {
        Queue queue = queueRepository.getQueue(queueName);
        if (queue == null) {
            return false;
        }
        queueRepository.insertMessage(queue, message);
        return true;
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
