package nl.hofmanr.jms.client.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JmsMessageBuilder {
    private String messageID;
    private String correlationID;
    private LocalDateTime timestamp;
    private LocalDateTime expiration;

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
                TimeZone.getDefault().toZoneId());
        return this;
    }

    public JmsMessageBuilder addExpiration(Long expiration) {
        if (expiration != null)
            this.expiration = LocalDateTime.ofInstant(Instant.ofEpochMilli(expiration),
                    TimeZone.getDefault().toZoneId());
        return this;
    }

    public JmsMessage build() {
        JmsMessage jmsMessage = new JmsMessage();
        jmsMessage.setMessageID(this.messageID);
        jmsMessage.setCorrelationID(this.correlationID);
        jmsMessage.setTimestamp(this.timestamp);
        jmsMessage.setExpiration(this.expiration);
        return jmsMessage;
    }
}
