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
    const [title, setTitle] = React.useState<string>('');

    const handleSetTitle = (title: string) => setTitle(title);

    return (
        <div>
           <Router>
                <ToolBar title={title} />
                <Layout title={title} onChangeTitle={handleSetTitle}>
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