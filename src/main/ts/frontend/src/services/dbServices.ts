import { Queue, Message, Payload } from '../common/types';

async function fetchQueues() {
    return await fetch('http://localhost:9082/jmsRestClient/api/queues')
    .then((res: Response) => res.json())
    .then((data: Queue[]) => data);
}

async function fetchMessages(queue: Queue): Promise<Message[]> {
    const response = await fetch(`http://localhost:9082/jmsRestClient/api/queues/${queue?.name}/messages`);
    if (response.status === 204) {
        return new Promise(resolve => resolve([]));
    }

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    return await response.json() as Promise<Message[]>;
}

async function deleteMessage(queue: Queue, message: Message) {
    return await fetch(`http://localhost:9082/jmsRestClient/api/queues/${queue?.name}/messages/${message.messageID}`,
    {
        method: "DELETE"
    })
    .then((res: Response) => "OK")
    .catch(function(res){ console.log(res) })
}

async function fetchPayload(queue: Queue, message: Message) {
    return await fetch(`http://localhost:9082/jmsRestClient/api/queues/${queue?.name}/messages/${message.messageID}`)
    .then((res: Response) => res.json())
    .then((data: Payload) => data);
}

async function postPayload(queue: Queue, payload: string): Promise<string> {
    const response = await fetch(`http://localhost:9082/jmsRestClient/api/queues/${queue?.name}/messages`,
    {
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'text/plain'
        },
        method: "POST",
        body: `${payload}`
    });

    if (!response.ok) {
        throw new Error(response.statusText);
    }

    return new Promise(resolve => resolve("OK"));
}

export { fetchQueues, fetchMessages, deleteMessage, fetchPayload, postPayload }