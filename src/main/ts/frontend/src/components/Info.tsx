import { Typography } from '@material-ui/core';
import React from 'react';

interface InfoProps {
    title: string;
    message: string;
}

function Info({ title, message }: InfoProps) {
    return (
        <div style={{marginTop: '10px'}}>
            <Typography variant="h4" align="center" color="textPrimary" gutterBottom>
                {title}
            </Typography>
            <Typography variant="h6" align="center" color="textSecondary" paragraph>
                {message}
            </Typography>
        </div>
    );
}

export default Info;