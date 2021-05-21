import React from 'react';
import useStyles from '../Styles';
import { ClassNameMap } from '@material-ui/styles';
import { Container } from '@material-ui/core';
import SwaggerUI from "swagger-ui-react"
import "swagger-ui-react/swagger-ui.css"

export default function SwaggerPg() {
    // see https://www.npmjs.com/package/swagger-ui-react
    // extra npm i --save-dev @types/swagger-ui-react
    // and enable CORS for static content in standalone.xml of Wildfly: https://forum.camunda.org/t/enable-cors-on-wildfly/673/2

    const classes: ClassNameMap = useStyles();
    return (
        <main>
            <div className={classes.container}>
                <Container maxWidth="md">
                    <SwaggerUI url="http://localhost:8080/jmsRestClient/swagger.json" />
                </Container>
            </div>
        </main>
    );
}

