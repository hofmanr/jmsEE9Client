
export interface Queue {
    name: string;
}

export interface Message {
    messageID: string;
    correlationID: string;
    timestamp: Date;
    expiration: Date;
}

export interface Payload {
    payload: string;
}
