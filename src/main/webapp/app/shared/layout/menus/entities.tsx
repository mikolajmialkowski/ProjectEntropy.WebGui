import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/cryptocurrency">
      <Translate contentKey="global.menu.entities.cryptocurrency" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/prediction">
      <Translate contentKey="global.menu.entities.prediction" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheduler-settings">
      <Translate contentKey="global.menu.entities.schedulerSettings" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/api-settings">
      <Translate contentKey="global.menu.entities.apiSettings" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/ai-settings">
      <Translate contentKey="global.menu.entities.aiSettings" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/general-info">
      <Translate contentKey="global.menu.entities.generalInfo" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
