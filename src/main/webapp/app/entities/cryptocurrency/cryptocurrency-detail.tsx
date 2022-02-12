import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './cryptocurrency.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CryptocurrencyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const cryptocurrencyEntity = useAppSelector(state => state.cryptocurrency.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cryptocurrencyDetailsHeading">
          <Translate contentKey="entropyApp.cryptocurrency.detail.title">Cryptocurrency</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cryptocurrencyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="entropyApp.cryptocurrency.name">Name</Translate>
            </span>
          </dt>
          <dd>{cryptocurrencyEntity.name}</dd>
          <dt>
            <span id="pair">
              <Translate contentKey="entropyApp.cryptocurrency.pair">Pair</Translate>
            </span>
          </dt>
          <dd>{cryptocurrencyEntity.pair}</dd>
          <dt>
            <span id="symbol">
              <Translate contentKey="entropyApp.cryptocurrency.symbol">Symbol</Translate>
            </span>
          </dt>
          <dd>{cryptocurrencyEntity.symbol}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="entropyApp.cryptocurrency.price">Price</Translate>
            </span>
          </dt>
          <dd>{cryptocurrencyEntity.price}</dd>
        </dl>
        <Button tag={Link} to="/cryptocurrency" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cryptocurrency/${cryptocurrencyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CryptocurrencyDetail;
