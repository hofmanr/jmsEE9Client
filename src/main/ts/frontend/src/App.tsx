import React from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route
  } from "react-router-dom";
import Layout from './components/Layout';
import ToolBar from './components/ToolBar';
import AboutPg from './pages/About';
import QueuePg from './pages/QueuePg';

const App = () => {
    return (
        <div>
           <Router>
                <ToolBar />
                <Layout>
                    <Switch>
                        <Route exact path="/">
                            <QueuePg />
                        </Route>
                        <Route exact path="/about">
                            <AboutPg />
                        </Route>
                    </Switch>    
                </Layout>
            </Router>
        </div>
    );
}

export default App;