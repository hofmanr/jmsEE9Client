package nl.hofmanr.jms.client.domain;

import nl.hofmanr.jms.client.exception.DataAccessException;

import javax.jms.JMSException;
import javax.jms.Queue;

public class JmsQueueAssembler {
    public JmsQueue toJmsQueue(Queue queue) {
        JmsQueue jmsQueue = new JmsQueue();
        try {
            jmsQueue.setName(queue.getQueueName());
        } catch (JMSException e) {
            throw new DataAccessException("Error assembling JMS Queue", e);
        }
        return jmsQueue;
    }
}
