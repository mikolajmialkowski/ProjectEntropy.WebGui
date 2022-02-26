import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './scheduler-settings.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SchedulerSettingsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const schedulerSettingsEntity = useAppSelector(state => state.schedulerSettings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schedulerSettingsDetailsHeading">
          <Translate contentKey="entropyApp.schedulerSettings.detail.title">SchedulerSettings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schedulerSettingsEntity.id}</dd>
          <dt>
            <span id="interval">
              <Translate contentKey="entropyApp.schedulerSettings.interval">Interval</Translate>
            </span>
          </dt>
          <dd>{schedulerSettingsEntity.interval}</dd>
          <dt>
            <span id="limit">
              <Translate contentKey="entropyApp.schedulerSettings.limit">Limit</Translate>
            </span>
          </dt>
          <dd>{schedulerSettingsEntity.limit}</dd>
        </dl>
        <Button tag={Link} to="/scheduler-settings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/scheduler-settings/${schedulerSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SchedulerSettingsDetail;
