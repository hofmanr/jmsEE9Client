import Button from '@material-ui/core/Button';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import React from 'react';
import useStyles from '../Styles';
import { Queue } from '../common/types';
import CircularProgress from '@material-ui/core/CircularProgress';

interface SelectQueueProps {
    busy: boolean;
    queues: Queue[];
    onRefresh: () => void;
    onSelectQueue: (queue: Queue) => void;
}

function SelectQueue({ busy, queues, onRefresh, onSelectQueue }: SelectQueueProps) {
    const classes = useStyles();

    // Save params form previous call
    const [prevQueues, setPrevQueues] = React.useState<Queue[]>();

    const [queue, setQueue] = React.useState<string>('');

    const handleChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        let val: string = event.target.value as string;
        let que: Queue | undefined = queues.find(q => q.name === val);
        setQueue(val);
        onSelectQueue(que!);
    };

    if (prevQueues !== queues) {
        setPrevQueues(queues);
        setQueue('');
    }

    return (
        <div>
            <Grid container spacing={2} justify="flex-start">
                <Grid item>
                    <FormControl className={classes.formControl} style={{minWidth: 150}}>
                        <InputLabel id="queues-label">Queue</InputLabel>
                        <Select
                            labelId="queues-label"
                            id="queues-select"
                            value={queue}
                            onChange={handleChange}
                        >
                            <MenuItem value="">
                                <em>None</em>
                            </MenuItem>
                            {queues.map(q =>(
                                <MenuItem key={q.name} value={q.name}>{q.name}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item>
                    <div className={classes.wrapper} style={{marginTop: '10px'}}>
                        <Button
                            variant="outlined"
                            color="primary"
                            className={classes.button}
                            disabled={busy}
                            onClick={() => { onRefresh() }}
                        >
                            Refresh
                        </Button>
                        {busy && <CircularProgress size={24} className={classes.buttonProgress} />}
                    </div>
                </Grid>
            </Grid>
        </div>
    );
}

export default SelectQueue;