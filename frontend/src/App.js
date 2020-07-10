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
import { DetailsTeacher } from './pages/Details/DetailsTeacher/DetailsTeacher';
import { DetailsDiscipline } from './pages/Details/DetailsDisciplines/DetailsDiscipline';
import { DetailsClasse } from './pages/Details/DetailsClasse/DetailsClasse';
import { Options } from './pages/Options/Options';
import { DetailsTimeSlot } from './pages/Details/DetailsTimeSlot/DetailsTimeSlot';
import { DetailsPlanning } from './pages/Details/DetailsPlanning/DetailsPlanning';
import { DetailsSlot } from './pages/Details/DetailsSlot/DetailsSlot';
import { DetailsRoom } from './pages/Details/DetailsRoom/DetailsRoom';
import { FormClasse } from './pages/Classes/FormClasse';
import { FormTeachers } from './pages/Teachers/FormTeachers';
import { FormRooms } from './pages/Rooms/FormRooms';
import { FormPlanning } from './pages/Plannings/FormPlanning';
import { FormMatiere } from './pages/Matieres/FormMatiere';
import { FormTimeSlot } from './pages/TimeSlots/FormTimeSlots';
import { FormSlot } from './pages/Slots/FormSlot';

/**
 * Entrée de l'application + Gestion des routes
 */
function App() {
  return (
    <div className="App">
      <Header />
      <Router>
        <Switch>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_CLASSE}/:id`}>
            <DetailsClasse />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_CLASSES}`}>
            <Classes />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_FORM_CLASSE}/:id?`}>
            <FormClasse />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_TIMESLOT}/:id`}>
            <DetailsTimeSlot />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_TIMESLOTS}`}>
            <TimeSlots />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_FORM_TIMESLOT}/:id?`}>
            <FormTimeSlot />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_PLANNING}/:id`}>
            <DetailsPlanning />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_PLANNINGS}`}>
            <Plannings />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_FORM_PLANNING}/:id?`}>
            <FormPlanning />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_SLOT}/:id`}>
            <DetailsSlot />
          </Route>
          <Route path ={`${process.env.REACT_APP_ENDPOINT_FORM_SLOT}/:id?`}>
            <FormSlot />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_SLOTS}`}>
            <Slots />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_ROOM}/:id`}>
            <DetailsRoom />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_ROOMS}`}>
            <Rooms />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_FORM_ROOM}/:id?`}>
            <FormRooms />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_DISCIPLINE}/:id`}>
            <DetailsDiscipline />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DISCIPLINES}`}>
            <Disciplines />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_FORM_DISCIPLINE}/:id?`}>
            <FormMatiere />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_DETAILS_TEACHER}/:id`}>
            <DetailsTeacher />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_TEACHERS}`}>
            <Teachers />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_FORM_TEACHER}/:id?`}>
            <FormTeachers />
          </Route>
          <Route path={`${process.env.REACT_APP_ENDPOINT_OPTIONS}`}>
            <Options />
          </Route>
          <Route path='/'>
            <Home />
          </Route>
        </Switch>
      </Router>
    </div>
  );
}

export default App;
