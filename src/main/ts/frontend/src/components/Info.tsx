import { Grid, Typography } from '@material-ui/core';
import React from 'react';

interface InfoProps {
    message: string;
}

function Info({ message }: InfoProps) {
    return (
        <div style={{ marginTop: '10px' }}>
            <Grid container spacing={2} justify="flex-start" alignItems="flex-end">
                <Grid item>
                    <Typography variant="h6"  align="left" color="textSecondary" paragraph>
                        {message}
                    </Typography>
                </Grid>
            </Grid>
        </div>
    );
}

export default Info;