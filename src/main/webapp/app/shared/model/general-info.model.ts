export interface IGeneralInfo {
  id?: number;
  recordsInDataBaseAmount?: number | null;
  apiCallsAmount?: number | null;
  apiStatistics?: string | null;
}

export const defaultValue: Readonly<IGeneralInfo> = {};
