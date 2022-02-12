import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { getEntities as getCryptocurrencies } from 'app/entities/cryptocurrency/cryptocurrency.reducer';
import { getEntity, updateEntity, createEntity, reset } from './prediction.reducer';
import { IPrediction } from 'app/shared/model/prediction.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PredictionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cryptocurrencies = useAppSelector(state => state.cryptocurrency.entities);
  const predictionEntity = useAppSelector(state => state.prediction.entity);
  const loading = useAppSelector(state => state.prediction.loading);
  const updating = useAppSelector(state => state.prediction.updating);
  const updateSuccess = useAppSelector(state => state.prediction.updateSuccess);
  const handleClose = () => {
    props.history.push('/prediction');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCryptocurrencies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...predictionEntity,
      ...values,
      cryptocurrency: cryptocurrencies.find(it => it.id.toString() === values.cryptocurrency.toString()),
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
          ...predictionEntity,
          cryptocurrency: predictionEntity?.cryptocurrency?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="entropyApp.prediction.home.createOrEditLabel" data-cy="PredictionCreateUpdateHeading">
            <Translate contentKey="entropyApp.prediction.home.createOrEditLabel">Create or edit a Prediction</Translate>
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
                  id="prediction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('entropyApp.prediction.dateFrom')}
                id="prediction-dateFrom"
                name="dateFrom"
                data-cy="dateFrom"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.prediction.dateTo')}
                id="prediction-dateTo"
                name="dateTo"
                data-cy="dateTo"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.prediction.duration')}
                id="prediction-duration"
                name="duration"
                data-cy="duration"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.prediction.currentPrice')}
                id="prediction-currentPrice"
                name="currentPrice"
                data-cy="currentPrice"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.prediction.predictedPraice')}
                id="prediction-predictedPraice"
                name="predictedPraice"
                data-cy="predictedPraice"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.prediction.probability')}
                id="prediction-probability"
                name="probability"
                data-cy="probability"
                type="text"
              />
              <ValidatedField
                id="prediction-cryptocurrency"
                name="cryptocurrency"
                data-cy="cryptocurrency"
                label={translate('entropyApp.prediction.cryptocurrency')}
                type="select"
              >
                <option value="" key="0" />
                {cryptocurrencies
                  ? cryptocurrencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/prediction" replace color="info">
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

export default PredictionUpdate;
