import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Prediction from './prediction';
import PredictionDetail from './prediction-detail';
import PredictionUpdate from './prediction-update';
import PredictionDeleteDialog from './prediction-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PredictionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PredictionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PredictionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Prediction} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PredictionDeleteDialog} />
  </>
);

export default Routes;
