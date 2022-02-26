import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './cryptocurrency.reducer';
import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Cryptocurrency = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const cryptocurrencyList = useAppSelector(state => state.cryptocurrency.entities);
  const loading = useAppSelector(state => state.cryptocurrency.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="cryptocurrency-heading" data-cy="CryptocurrencyHeading">
        <Translate contentKey="entropyApp.cryptocurrency.home.title">Cryptocurrencies</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="entropyApp.cryptocurrency.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="entropyApp.cryptocurrency.home.createLabel">Create new Cryptocurrency</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cryptocurrencyList && cryptocurrencyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="entropyApp.cryptocurrency.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.cryptocurrency.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.cryptocurrency.pair">Pair</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.cryptocurrency.symbol">Symbol</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.cryptocurrency.price">Price</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cryptocurrencyList.map((cryptocurrency, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${cryptocurrency.id}`} color="link" size="sm">
                      {cryptocurrency.id}
                    </Button>
                  </td>
                  <td>{cryptocurrency.name}</td>
                  <td>{cryptocurrency.pair}</td>
                  <td>{cryptocurrency.symbol}</td>
                  <td>{cryptocurrency.price}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${cryptocurrency.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${cryptocurrency.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${cryptocurrency.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
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
              <Translate contentKey="entropyApp.cryptocurrency.home.notFound">No Cryptocurrencies found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Cryptocurrency;
