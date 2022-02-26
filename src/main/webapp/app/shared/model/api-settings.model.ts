export interface IApiSettings {
  id?: number;
  apiUri1?: string | null;
  apiUri2?: string | null;
  apiUri3?: string | null;
  apiToken?: string | null;
}

export const defaultValue: Readonly<IApiSettings> = {};
