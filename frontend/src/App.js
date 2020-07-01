import React from 'react';
import { Header } from './components/Header/Header';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { Home } from './pages/Home/Home';
import { Classes } from './pages/Classes/Classes';

function App() {
  return (
    <div className="App">
      <Header />
      <Router>
        <Switch>
        <Route path="/classes">
            <Classes />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </Router>
    </div>
  );
}

export default App;
