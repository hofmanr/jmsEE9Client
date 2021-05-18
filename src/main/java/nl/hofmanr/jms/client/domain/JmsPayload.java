package nl.hofmanr.jms.client.domain;

public class JmsPayload {
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
