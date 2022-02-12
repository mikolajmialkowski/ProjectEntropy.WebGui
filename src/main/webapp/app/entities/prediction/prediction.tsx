import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './prediction.reducer';
import { IPrediction } from 'app/shared/model/prediction.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Prediction = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const predictionList = useAppSelector(state => state.prediction.entities);
  const loading = useAppSelector(state => state.prediction.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="prediction-heading" data-cy="PredictionHeading">
        <Translate contentKey="entropyApp.prediction.home.title">Predictions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="entropyApp.prediction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="entropyApp.prediction.home.createLabel">Create new Prediction</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {predictionList && predictionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="entropyApp.prediction.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.dateFrom">Date From</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.dateTo">Date To</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.duration">Duration</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.currentPrice">Current Price</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.predictedPraice">Predicted Praice</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.probability">Probability</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.prediction.cryptocurrency">Cryptocurrency</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {predictionList.map((prediction, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${prediction.id}`} color="link" size="sm">
                      {prediction.id}
                    </Button>
                  </td>
                  <td>{prediction.dateFrom}</td>
                  <td>{prediction.dateTo}</td>
                  <td>{prediction.duration}</td>
                  <td>{prediction.currentPrice}</td>
                  <td>{prediction.predictedPraice}</td>
                  <td>{prediction.probability}</td>
                  <td>
                    {prediction.cryptocurrency ? (
                      <Link to={`cryptocurrency/${prediction.cryptocurrency.id}`}>{prediction.cryptocurrency.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${prediction.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${prediction.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${prediction.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="entropyApp.prediction.home.notFound">No Predictions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Prediction;
