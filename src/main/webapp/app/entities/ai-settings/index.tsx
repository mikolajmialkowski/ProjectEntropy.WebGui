import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AiSettings from './ai-settings';
import AiSettingsDetail from './ai-settings-detail';
import AiSettingsUpdate from './ai-settings-update';
import AiSettingsDeleteDialog from './ai-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AiSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AiSettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AiSettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={AiSettings} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AiSettingsDeleteDialog} />
  </>
);

export default Routes;
