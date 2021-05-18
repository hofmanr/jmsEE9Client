import React, { useEffect } from 'react';
import useStyles from '../Styles';
import { ClassNameMap } from '@material-ui/styles';
import { Container } from '@material-ui/core';
import Info from '../components/Info';
import { Queue, Message } from '../common/types';
import { fetchQueues, fetchMessages, postPayload, deleteMessage } from '../services/dbServices';
import SelectQueue from '../components/SelectQueue';
import QueueContent from '../components/content/QueueContent';

function QueuePg() {
    const classes: ClassNameMap = useStyles();
    // State
    const [queues, setQueues] = React.useState<Queue[]>([]);
    const [queue, setQueue] = React.useState<Queue>(); // current selected queue
    const [messages, setMessages] = React.useState<Message[]>([]); // records in the selected queue
    const [busySelect, setBusySelect] = React.useState(false);
    const [busyContent, setBusyContent] = React.useState(false);

    const timerBusySelect = () => {
        setTimeout(() => setBusySelect(false), 500);
    };

    const timerBusyContent = () => {
        setTimeout(() => setBusyContent(false), 500);
    };

    // run when first load the component in the browser
    useEffect(() => {
        setBusySelect(true);
        fetchQueues().then(qs => {
            setQueues(qs);
            timerBusySelect();
        });
    }, []); // run it once

    const handleRefreshQueues = () => {
        setBusySelect(true);
        fetchQueues().then(qs => {
            // Reset state
            setQueues(qs);
            setQueue(undefined);
            setMessages([]);
            timerBusySelect();
        });
    };

    const onSelectQueue = (seletedQueue: Queue) => {
        setQueue(seletedQueue);
        if (seletedQueue) {
            setBusyContent(true);
            fetchMessages(seletedQueue).then(rs => {
                setMessages(rs);
                timerBusyContent();
            });
        } else {
            setMessages([]);
        }
    };

    const handleAddMessage = (payload: string) => {
        console.log('post new record to queue', queue, payload);
        postPayload(queue!, payload);
    };


    // Make sure the records are sequential processed (else you get DB errors from json-server)
    async function executeDelete(records: (Message | undefined)[]): Promise<string[]> {
        const errors: string[] = []; 
        for (const rec of records) {
            try {
                // wait for item to be processed or failed with error
                await deleteMessage(queue!, rec!);
            } catch (error) {
                // one of param handlers failed with 'error'
                errors.push(error);
            }
        }
        return errors;
    };

    const handleDeleteMessages = (ids: string[]) => {
        setBusyContent(true);
        let messagesToDelete: (Message | undefined)[] = ids.map((id: string) => messages.find((value: Message) => value.messageID === id));
        let promiss = executeDelete(messagesToDelete);
        // Refresh the content in the QueueContent component
        promiss.then((value: string[]) => handleRefreshContent());
    };

    const handleRefreshContent = () => {
        if (queue) {
            setBusyContent(true);
            fetchMessages(queue).then(rs => {
                setMessages(rs);
                timerBusyContent();
            });
        } else {
            setMessages([]);
        }
    };

    return (
        <main>
            <div className={classes.container}>
                <Container maxWidth="sm">
                    <Info title="Queue Details" message="Show, add and delete messages from queues." />
                    <SelectQueue busy={busySelect} queues={queues} onRefresh={handleRefreshQueues} onSelectQueue={onSelectQueue} />
                </Container>
            </div>
            <div className={classes.container}>
                <Container maxWidth="md">
                    <QueueContent busy={busyContent} queue={queue} messages={messages} onRefresh={handleRefreshContent} onAddMessage={handleAddMessage} onDeleteMessages={handleDeleteMessages} />
                </Container>
            </div>
        </main>
    );

}

export default QueuePg;