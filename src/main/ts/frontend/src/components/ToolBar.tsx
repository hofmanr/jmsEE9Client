import React from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import QueueIcon from '@material-ui/icons/Queue';
import useStyles from '../Styles';
import { ClassNameMap } from '@material-ui/styles';

interface ToolBarProps {
    title: string;
}

const ToolBar = ({ title }: ToolBarProps ) => {
    const classes: ClassNameMap = useStyles();
    return (
        <div className={classes.toolBar}>
            <AppBar elevation={0} position="static">
                <Toolbar>
                    <QueueIcon className={classes.toolbarIcon} />
                    <Typography variant="h6" className={classes.toolbarTitle}>
                        {title}
                    </Typography>
                </Toolbar>
            </AppBar>
        </div>
    )
};

export default ToolBar;