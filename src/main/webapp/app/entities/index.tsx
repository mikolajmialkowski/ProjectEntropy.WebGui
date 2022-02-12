import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cryptocurrency from './cryptocurrency';
import Prediction from './prediction';
import SchedulerSettings from './scheduler-settings';
import ApiSettings from './api-settings';
import AiSettings from './ai-settings';
import GeneralInfo from './general-info';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}cryptocurrency`} component={Cryptocurrency} />
      <ErrorBoundaryRoute path={`${match.url}prediction`} component={Prediction} />
      <ErrorBoundaryRoute path={`${match.url}scheduler-settings`} component={SchedulerSettings} />
      <ErrorBoundaryRoute path={`${match.url}api-settings`} component={ApiSettings} />
      <ErrorBoundaryRoute path={`${match.url}ai-settings`} component={AiSettings} />
      <ErrorBoundaryRoute path={`${match.url}general-info`} component={GeneralInfo} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
