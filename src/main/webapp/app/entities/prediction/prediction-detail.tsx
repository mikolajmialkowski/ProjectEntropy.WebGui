import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './prediction.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PredictionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const predictionEntity = useAppSelector(state => state.prediction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="predictionDetailsHeading">
          <Translate contentKey="entropyApp.prediction.detail.title">Prediction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.id}</dd>
          <dt>
            <span id="dateFrom">
              <Translate contentKey="entropyApp.prediction.dateFrom">Date From</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.dateFrom}</dd>
          <dt>
            <span id="dateTo">
              <Translate contentKey="entropyApp.prediction.dateTo">Date To</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.dateTo}</dd>
          <dt>
            <span id="duration">
              <Translate contentKey="entropyApp.prediction.duration">Duration</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.duration}</dd>
          <dt>
            <span id="currentPrice">
              <Translate contentKey="entropyApp.prediction.currentPrice">Current Price</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.currentPrice}</dd>
          <dt>
            <span id="predictedPraice">
              <Translate contentKey="entropyApp.prediction.predictedPraice">Predicted Praice</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.predictedPraice}</dd>
          <dt>
            <span id="probability">
              <Translate contentKey="entropyApp.prediction.probability">Probability</Translate>
            </span>
          </dt>
          <dd>{predictionEntity.probability}</dd>
          <dt>
            <Translate contentKey="entropyApp.prediction.cryptocurrency">Cryptocurrency</Translate>
          </dt>
          <dd>{predictionEntity.cryptocurrency ? predictionEntity.cryptocurrency.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/prediction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/prediction/${predictionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PredictionDetail;
