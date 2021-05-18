import React from 'react';
import useStyles from '../Styles';
import Drawer from '@material-ui/core/Drawer';
import Typography from '@material-ui/core/Typography';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { SubjectOutlined } from '@material-ui/icons';
import InfoOutlinedIcon from '@material-ui/icons/InfoOutlined';
import { useHistory, useLocation } from 'react-router';
import Divider from '@material-ui/core/Divider';

interface LayoutProps {
    children: JSX.Element;
}

export default function Layout( { children }: LayoutProps)  {
    const classes = useStyles();
    const history = useHistory();
    const location = useLocation();

    const menuItems = [
        {
            text: "Queues",
            icon: <SubjectOutlined color="primary" />,
            path: "/"
        },
        {
            text: "About",
            icon: <InfoOutlinedIcon color="primary" />,
            path: "/about"
        }
    ];

    return (
        <div className={classes.root}>
            <Drawer
                className={classes.drawer}
                variant="permanent"
                anchor="left"
                classes={{
                    paper: classes.drawerPaper,
                }}
            >
                <div style={{ margin: '10px 20px 10px'}}>
                    <Typography variant="h5">
                        Menu
                    </Typography>
                    <Typography variant="caption">
                        v1.0.0
                    </Typography>
                </div>
                <Divider />
                { /* list / links */ }
                <List>
                    { menuItems.map(item => (
                        <ListItem
                            button
                            key={item.text}
                            onClick={() => history.push(item.path)}
                            className={location.pathname === item.path ? classes.drawerItemActive : undefined}
                        >
                            <ListItemIcon>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.text} />
                        </ListItem>
                    )) }
                </List>
            </Drawer>
            <div className={classes.page}>
                {children}
            </div>
        </div>
    );
}