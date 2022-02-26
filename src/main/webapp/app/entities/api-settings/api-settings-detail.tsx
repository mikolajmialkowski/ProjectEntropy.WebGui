import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './api-settings.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ApiSettingsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const apiSettingsEntity = useAppSelector(state => state.apiSettings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="apiSettingsDetailsHeading">
          <Translate contentKey="entropyApp.apiSettings.detail.title">ApiSettings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{apiSettingsEntity.id}</dd>
          <dt>
            <span id="apiUri1">
              <Translate contentKey="entropyApp.apiSettings.apiUri1">Api Uri 1</Translate>
            </span>
          </dt>
          <dd>{apiSettingsEntity.apiUri1}</dd>
          <dt>
            <span id="apiUri2">
              <Translate contentKey="entropyApp.apiSettings.apiUri2">Api Uri 2</Translate>
            </span>
          </dt>
          <dd>{apiSettingsEntity.apiUri2}</dd>
          <dt>
            <span id="apiUri3">
              <Translate contentKey="entropyApp.apiSettings.apiUri3">Api Uri 3</Translate>
            </span>
          </dt>
          <dd>{apiSettingsEntity.apiUri3}</dd>
          <dt>
            <span id="apiToken">
              <Translate contentKey="entropyApp.apiSettings.apiToken">Api Token</Translate>
            </span>
          </dt>
          <dd>{apiSettingsEntity.apiToken}</dd>
        </dl>
        <Button tag={Link} to="/api-settings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/api-settings/${apiSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApiSettingsDetail;
