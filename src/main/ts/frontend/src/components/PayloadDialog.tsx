import React from 'react';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import TextareaAutosize from '@material-ui/core/TextareaAutosize';
import useStyles from '../Styles';
import { ClassNameMap } from '@material-ui/core/styles/withStyles';

interface PayloadDialogProps {
    title: string;
    payload?: string;
    open: boolean;
    editMode: boolean;
    setOpen: (state: boolean) => void;
    action: (payload: string) => void;
}

export default function PayloadDialog({ title, payload, open, editMode, setOpen, action }: PayloadDialogProps) {
    const classes: ClassNameMap = useStyles();
    const [content, setContent] = React.useState<string>('');

    const handleClose = () => {
        setContent('');
        setOpen(false);
    };
    const handleOK = () => {
        setOpen(false);
        if (action) {
            action(content!); // execute action when OK
            setContent('');
        }
    };

    const handleChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        let val: string = event.target.value as string;
        setContent(val);
    };


    const descriptionElementRef = React.useRef<HTMLElement>(null);
    React.useEffect(() => {
        if (open) {
            const { current: descriptionElement } = descriptionElementRef;
            if (descriptionElement !== null) {
                descriptionElement.focus();
            }
        }
    }, [open]);

    return (
        <Dialog
            open={open}
            onClose={handleClose}
            scroll="paper"
            fullWidth={false}
            maxWidth="lg"
            aria-labelledby="scroll-dialog-title"
            aria-describedby="scroll-dialog-description"
        >
            <DialogTitle id="scroll-dialog-title">{title}</DialogTitle>
            <DialogContent dividers={true}>
                <DialogContentText
                    id="scroll-dialog-description"
                    variant="body2"
                    ref={descriptionElementRef}
                    tabIndex={-1}
                >
                    {editMode ? (
                        <TextareaAutosize 
                            aria-label="textarea edit mode>" 
                            rowsMin={20}
                            rowsMax={20}
                            placeholder="Add new payload" 
                            className={classes.textarea}
                            value={content}
                            onChange={handleChange}
                        />
                    ) : (
                        <TextareaAutosize 
                            aria-label="textarea readonly" 
                            rowsMin={20}
                            disabled
                            rowsMax={20}
                            placeholder="No payload" 
                            className={classes.textarea}
                            defaultValue={payload + "\n"}
                        />
                    )}
                </DialogContentText>
            </DialogContent>
            {editMode ? (
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={handleOK} color="primary" autoFocus>
                        OK
                    </Button>
                </DialogActions>
            ) : (
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Close
                    </Button>
                </DialogActions>
            )}
        </Dialog>
    );
}