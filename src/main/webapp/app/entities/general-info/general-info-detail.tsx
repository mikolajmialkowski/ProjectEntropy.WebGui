import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './general-info.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GeneralInfoDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const generalInfoEntity = useAppSelector(state => state.generalInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="generalInfoDetailsHeading">
          <Translate contentKey="entropyApp.generalInfo.detail.title">GeneralInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{generalInfoEntity.id}</dd>
          <dt>
            <span id="recordsInDataBaseAmount">
              <Translate contentKey="entropyApp.generalInfo.recordsInDataBaseAmount">Records In Data Base Amount</Translate>
            </span>
          </dt>
          <dd>{generalInfoEntity.recordsInDataBaseAmount}</dd>
          <dt>
            <span id="apiCallsAmount">
              <Translate contentKey="entropyApp.generalInfo.apiCallsAmount">Api Calls Amount</Translate>
            </span>
          </dt>
          <dd>{generalInfoEntity.apiCallsAmount}</dd>
          <dt>
            <span id="apiStatistics">
              <Translate contentKey="entropyApp.generalInfo.apiStatistics">Api Statistics</Translate>
            </span>
          </dt>
          <dd>{generalInfoEntity.apiStatistics}</dd>
        </dl>
        <Button tag={Link} to="/general-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/general-info/${generalInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GeneralInfoDetail;
