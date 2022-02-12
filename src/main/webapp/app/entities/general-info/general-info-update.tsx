import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './general-info.reducer';
import { IGeneralInfo } from 'app/shared/model/general-info.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GeneralInfoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const generalInfoEntity = useAppSelector(state => state.generalInfo.entity);
  const loading = useAppSelector(state => state.generalInfo.loading);
  const updating = useAppSelector(state => state.generalInfo.updating);
  const updateSuccess = useAppSelector(state => state.generalInfo.updateSuccess);
  const handleClose = () => {
    props.history.push('/general-info');
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
      ...generalInfoEntity,
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
          ...generalInfoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="entropyApp.generalInfo.home.createOrEditLabel" data-cy="GeneralInfoCreateUpdateHeading">
            <Translate contentKey="entropyApp.generalInfo.home.createOrEditLabel">Create or edit a GeneralInfo</Translate>
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
                  id="general-info-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('entropyApp.generalInfo.recordsInDataBaseAmount')}
                id="general-info-recordsInDataBaseAmount"
                name="recordsInDataBaseAmount"
                data-cy="recordsInDataBaseAmount"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.generalInfo.apiCallsAmount')}
                id="general-info-apiCallsAmount"
                name="apiCallsAmount"
                data-cy="apiCallsAmount"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.generalInfo.apiStatistics')}
                id="general-info-apiStatistics"
                name="apiStatistics"
                data-cy="apiStatistics"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/general-info" replace color="info">
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

export default GeneralInfoUpdate;
