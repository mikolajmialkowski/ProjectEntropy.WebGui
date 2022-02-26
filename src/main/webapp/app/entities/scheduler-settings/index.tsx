import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SchedulerSettings from './scheduler-settings';
import SchedulerSettingsDetail from './scheduler-settings-detail';
import SchedulerSettingsUpdate from './scheduler-settings-update';
import SchedulerSettingsDeleteDialog from './scheduler-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchedulerSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchedulerSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchedulerSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={SchedulerSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchedulerSettingsDeleteDialog} />
  </>
);

export default Routes;
