import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './scheduler-settings.reducer';
import { ISchedulerSettings } from 'app/shared/model/scheduler-settings.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SchedulerSettings = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const schedulerSettingsList = useAppSelector(state => state.schedulerSettings.entities);
  const loading = useAppSelector(state => state.schedulerSettings.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="scheduler-settings-heading" data-cy="SchedulerSettingsHeading">
        <Translate contentKey="entropyApp.schedulerSettings.home.title">Scheduler Settings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="entropyApp.schedulerSettings.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="entropyApp.schedulerSettings.home.createLabel">Create new Scheduler Settings</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {schedulerSettingsList && schedulerSettingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="entropyApp.schedulerSettings.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.schedulerSettings.interval">Interval</Translate>
                </th>
                <th>
                  <Translate contentKey="entropyApp.schedulerSettings.limit">Limit</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {schedulerSettingsList.map((schedulerSettings, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${schedulerSettings.id}`} color="link" size="sm">
                      {schedulerSettings.id}
                    </Button>
                  </td>
                  <td>{schedulerSettings.interval}</td>
                  <td>{schedulerSettings.limit}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${schedulerSettings.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${schedulerSettings.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${schedulerSettings.id}/delete`}
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
              <Translate contentKey="entropyApp.schedulerSettings.home.notFound">No Scheduler Settings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SchedulerSettings;
