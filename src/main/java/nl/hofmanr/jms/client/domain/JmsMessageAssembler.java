package nl.hofmanr.jms.client.domain;

import nl.hofmanr.jms.client.exception.DataAccessException;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;

public class JmsMessageAssembler {

    public JmsMessage toJmsMessage(TextMessage textMessage) {
        try {
            return JmsMessageBuilder.newBuilder()
                    .addMessageID(textMessage.getJMSMessageID())
                    .addCorrelationID(textMessage.getJMSCorrelationID())
                    .addTimestamp(textMessage.getJMSTimestamp())
                    .build();
        } catch (JMSException e) {
            throw new DataAccessException("Error assembling JMS Message", e);
        }

    }
}
