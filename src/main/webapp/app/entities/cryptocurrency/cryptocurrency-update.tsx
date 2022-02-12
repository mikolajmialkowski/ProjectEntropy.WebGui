import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './cryptocurrency.reducer';
import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CryptocurrencyUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const cryptocurrencyEntity = useAppSelector(state => state.cryptocurrency.entity);
  const loading = useAppSelector(state => state.cryptocurrency.loading);
  const updating = useAppSelector(state => state.cryptocurrency.updating);
  const updateSuccess = useAppSelector(state => state.cryptocurrency.updateSuccess);
  const handleClose = () => {
    props.history.push('/cryptocurrency');
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
      ...cryptocurrencyEntity,
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
          ...cryptocurrencyEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="entropyApp.cryptocurrency.home.createOrEditLabel" data-cy="CryptocurrencyCreateUpdateHeading">
            <Translate contentKey="entropyApp.cryptocurrency.home.createOrEditLabel">Create or edit a Cryptocurrency</Translate>
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
                  id="cryptocurrency-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('entropyApp.cryptocurrency.name')}
                id="cryptocurrency-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.cryptocurrency.pair')}
                id="cryptocurrency-pair"
                name="pair"
                data-cy="pair"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.cryptocurrency.symbol')}
                id="cryptocurrency-symbol"
                name="symbol"
                data-cy="symbol"
                type="text"
              />
              <ValidatedField
                label={translate('entropyApp.cryptocurrency.price')}
                id="cryptocurrency-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cryptocurrency" replace color="info">
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

export default CryptocurrencyUpdate;
