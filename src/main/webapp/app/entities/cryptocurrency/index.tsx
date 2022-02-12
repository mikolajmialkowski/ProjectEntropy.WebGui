import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Cryptocurrency from './cryptocurrency';
import CryptocurrencyDetail from './cryptocurrency-detail';
import CryptocurrencyUpdate from './cryptocurrency-update';
import CryptocurrencyDeleteDialog from './cryptocurrency-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CryptocurrencyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CryptocurrencyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CryptocurrencyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Cryptocurrency} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CryptocurrencyDeleteDialog} />
  </>
);

export default Routes;
