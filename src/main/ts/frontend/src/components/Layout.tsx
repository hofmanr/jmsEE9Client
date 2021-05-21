import React, { useEffect } from 'react';
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
    title: string;
    basePath: string;
    onChangeTitle: (title: string) => void;
    children: JSX.Element;
}

interface MenuItem {
    text: string;
    icon: JSX.Element;
    path: string;
}

export default function Layout({ title, basePath, onChangeTitle, children }: LayoutProps) {
    const classes = useStyles();
    const history = useHistory();
    const location = useLocation();

    const menuItems: MenuItem[] = [
        {
            text: "Queues",
            icon: <SubjectOutlined color="primary" />,
            path: `${basePath}/`
        },
        {
            text: "Swagger",
            icon: <SubjectOutlined color="primary" />,
            path: `${basePath}/swagger`
        },
        {
            text: "About",
            icon: <InfoOutlinedIcon color="primary" />,
            path: `${basePath}/about`
        }
    ];

    // run the first time and every time the user click on an item in the menu
    useEffect(() => {
        let item: MenuItem | undefined = menuItems.find((item: MenuItem) => item.path === location.pathname);
        if (item === undefined) {
            onChangeTitle("");
        } else if (item.text !== title) {
            onChangeTitle(item.text);
        }
    }, [title, location, onChangeTitle]); // eslint-disable-line react-hooks/exhaustive-deps

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
                <div style={{ margin: '10px 20px 10px' }}>
                    <Typography variant="h5">
                        JMS Client
                    </Typography>
                    <Typography variant="caption">
                        v1.0.0
                    </Typography>
                </div>
                <Divider />
                { /* list / links */}
                <List>
                    {menuItems.map(item => (
                        <ListItem
                            button
                            key={item.text}
                            onClick={() => history.push(item.path)}
                            style={ { background: `${location.pathname === item.path ? '#e4e4e4' : '#ffffff'}` } }
                            // does not work: className={ location.pathname === item.path ? classes.drawerItemActive : classes.drawerItemInactive }
                        >
                            <ListItemIcon>{item.icon}</ListItemIcon>
                            <ListItemText primary={item.text} />
                        </ListItem>
                    ))}
                </List>
            </Drawer>
            <div className={classes.page}>
                {children}
            </div>
        </div>
    );
}