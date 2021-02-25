package nl.hofmanr.jms.client.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JmsMessageBuilder {
    private String messageID;
    private String correlationID;
    private LocalDateTime timestamp;
    private String message;

    public static JmsMessageBuilder newBuilder() {
        return new JmsMessageBuilder();
    }

    public JmsMessageBuilder addMessageID(String messageID) {
        this.messageID = messageID;
        return this;
    }

    public JmsMessageBuilder addCorrelationID(String correlationID) {
        this.correlationID = correlationID;
        return this;
    }

    public JmsMessageBuilder addTimestamp(Long timestamp) {
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId());;
        return this;
    }

    public JmsMessageBuilder addMessage(String message) {
        this.message = message;
        return this;
    }

    public JmsMessage build() {
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageID(this.messageID);
        jmsMessage.setCorrelationID(this.correlationID);
        jmsMessage.setTimestamp(this.timestamp);
        jmsMessage.setMessage(this.message);
        return jmsMessage;
    }
}
