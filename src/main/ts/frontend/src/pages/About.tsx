import Container from '@material-ui/core/Container';
import { ClassNameMap } from '@material-ui/core/styles/withStyles';
import Typography from '@material-ui/core/Typography';
import React from 'react';
import Info from '../components/Info';
import useStyles from '../Styles';


export default function AboutPg() {
    const classes: ClassNameMap = useStyles();
    return (
        <main>
            <div className={classes.container}>
                <Container maxWidth="md">
                    <Info title="About" message="Demo app build with React." />
                    <Typography variant="h5">Run the App</Typography>
                    <Typography variant="body1" gutterBottom>
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quos blanditiis tenetur
        unde suscipit, quam beatae rerum inventore consectetur, neque doloribus, cupiditate numquam
        dignissimos laborum fugiat deleniti? Eum quasi quidem quibusdam.
                    </Typography>
             </Container>
            </div>
        </main>
    );
}