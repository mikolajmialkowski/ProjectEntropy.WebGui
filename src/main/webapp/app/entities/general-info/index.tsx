import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GeneralInfo from './general-info';
import GeneralInfoDetail from './general-info-detail';
import GeneralInfoUpdate from './general-info-update';
import GeneralInfoDeleteDialog from './general-info-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GeneralInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GeneralInfoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GeneralInfoDetail} />
      <ErrorBoundaryRoute path={match.url} component={GeneralInfo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GeneralInfoDeleteDialog} />
  </>
);

export default Routes;
