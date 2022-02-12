import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './api-settings.reducer';
import { IApiSettings } from 'app/shared/model/api-settings.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ApiSettingsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const apiSettingsEntity = useAppSelector(state => state.apiSettings.entity);
  const loading = useAppSelector(state => state.apiSettings.loading);
  const updating = useAppSelector(state => state.apiSettings.updating);
  const updateSuccess = useAppSelector(state => state.apiSettings.updateSuccess);
  const handleClose = () => {
    props.history.push('/api-settings');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...apiSettingsEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...apiSettingsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="entropyApp.apiSettings.home.createOrEditLabel" data-cy="ApiSettingsCreateUpdateHeading">
            <Translate contentKey="entropyApp.apiSettings.home.createOrEditLabel">Create or edit a ApiSettings</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="api-settings-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('entropyApp.apiSettings.apiUri1')}
                id="api-settings-apiUri1"
                name="apiUri1"
                data-cy="apiUri1"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.apiSettings.apiUri2')}
                id="api-settings-apiUri2"
                name="apiUri2"
                data-cy="apiUri2"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.apiSettings.apiUri3')}
                id="api-settings-apiUri3"
                name="apiUri3"
                data-cy="apiUri3"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.apiSettings.apiToken')}
                id="api-settings-apiToken"
                name="apiToken"
                data-cy="apiToken"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/api-settings" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ApiSettingsUpdate;
