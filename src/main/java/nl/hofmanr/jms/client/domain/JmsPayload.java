package nl.hofmanr.jms.client.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Payload", description = "The payload of an entry")
public class JmsPayload {
    @ApiModelProperty("The payload itself")
    private String payload;

    public JmsPayload() {
    }

    public JmsPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}
