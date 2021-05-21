import React, { useEffect } from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route
  } from "react-router-dom";
import Layout from './components/Layout';
import ToolBar from './components/ToolBar';
import AboutPg from './pages/About';
import QueuePg from './pages/QueuePg';
import SwaggerPg from './pages/SwaggerPg';

const App = () => {
    const [title, setTitle] = React.useState<string>('');
    const [basePath, setBasePath] = React.useState<string>('');

    const handleSetTitle = (title: string) => setTitle(title);

    useEffect(() => {
        let pathname = window.location.pathname; // e.g. /jmsRestClient/ if the URL is http://loclahost:8080/jmsRestClient/
        // Remove everything after last '/'
         setBasePath(pathname.substr(0, pathname.lastIndexOf('/')));
    }, []); // run this only once

    return (
        <div>
           <Router>
                <ToolBar title={title} />
                <Layout title={title} basePath={basePath} onChangeTitle={handleSetTitle}>
                    <Switch>
                        {/* original <Route exact path="/"> */}
                        <Route exact path={`${basePath}/`}>
                            <QueuePg />
                        </Route>
                        <Route exact path={`${basePath}/swagger`}>
                            <SwaggerPg />
                        </Route>
                        <Route exact path={`${basePath}/about`}>
                            <AboutPg />
                        </Route>
                    </Switch>    
                </Layout>
            </Router>
        </div>
    );
}

export default App;