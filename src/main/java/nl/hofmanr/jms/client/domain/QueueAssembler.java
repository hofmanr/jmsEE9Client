package nl.hofmanr.jms.client.domain;

import nl.hofmanr.jms.client.exception.DataAccessException;

import javax.jms.JMSException;
import javax.jms.Queue;

public class QueueAssembler {

    public String toName(Queue queue) {
        try {
            return queue.getQueueName();
        } catch (JMSException e) {
            throw new DataAccessException("Error getting queue name", e);
        }
    }
}
