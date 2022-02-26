import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import cryptocurrency from 'app/entities/cryptocurrency/cryptocurrency.reducer';
// prettier-ignore
import prediction from 'app/entities/prediction/prediction.reducer';
// prettier-ignore
import schedulerSettings from 'app/entities/scheduler-settings/scheduler-settings.reducer';
// prettier-ignore
import apiSettings from 'app/entities/api-settings/api-settings.reducer';
// prettier-ignore
import aiSettings from 'app/entities/ai-settings/ai-settings.reducer';
// prettier-ignore
import generalInfo from 'app/entities/general-info/general-info.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  cryptocurrency,
  prediction,
  schedulerSettings,
  apiSettings,
  aiSettings,
  generalInfo,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
