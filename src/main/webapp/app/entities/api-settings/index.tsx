import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ApiSettings from './api-settings';
import ApiSettingsDetail from './api-settings-detail';
import ApiSettingsUpdate from './api-settings-update';
import ApiSettingsDeleteDialog from './api-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApiSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApiSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApiSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={ApiSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ApiSettingsDeleteDialog} />
  </>
);

export default Routes;
