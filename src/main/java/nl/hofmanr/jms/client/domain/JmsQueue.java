package nl.hofmanr.jms.client.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Queue", description = "JMS Queue")
public class JmsQueue {
    @ApiModelProperty("Name of the queue")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
