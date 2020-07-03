/*
 * Copyright 2020 Hicham AZIMANI, Yassine AZIMANI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from 'react';
import { Header } from './components/Header/Header';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import { Home } from './pages/Home/Home';
import { Classes } from './pages/Classes/Classes';
import { TimeSlots } from './pages/TimeSlots/TimeSlots';
import { Plannings } from './pages/Plannings/Plannings';
import { Slots } from './pages/Slots/Slots';
import { Rooms } from './pages/Rooms/Rooms';
import { Disciplines } from './pages/Matieres/Matieres';
import { Teachers } from './pages/Teachers/Teachers';

/**
 * Entrée de l'application + Gestion des routes
 */
function App() {
  return (
    <div className="App">
      <Header />
      <Router>
        <Switch>
          <Route path="/classes">
            <Classes />
          </Route>
          <Route path="/timeslots">
            <TimeSlots />
          </Route>
          <Route path="/plannings">
            <Plannings />
          </Route>
          <Route path="/slots">
            <Slots />
          </Route>
          <Route path="/rooms">
            <Rooms />
          </Route>
          <Route path="/disciplines">
            <Disciplines />
          </Route>
          <Route path="/teachers">
            <Teachers />
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
