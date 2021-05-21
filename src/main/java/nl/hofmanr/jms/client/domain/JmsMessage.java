package nl.hofmanr.jms.client.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "Message", description = "Message on a queue")
public class JmsMessage {
    @ApiModelProperty("Identifier")
    private String messageID;
    @ApiModelProperty("Reference to messageID")
    private String correlationID;
    @ApiModelProperty("Creation date/time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "CET")
    // @JsonDeserialize: use ObjectMapperContextResolver and add jackson-datatype-jsr310 dependency
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime timestamp;
    @ApiModelProperty("Expiration date/time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime expiration;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getCorrelationID() {
        return correlationID;
    }

    public void setCorrelationID(String correlationID) {
        this.correlationID = correlationID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
