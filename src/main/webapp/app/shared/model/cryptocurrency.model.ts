import { IPrediction } from 'app/shared/model/prediction.model';

export interface ICryptocurrency {
  id?: number;
  name?: string | null;
  pair?: string | null;
  symbol?: string | null;
  price?: string | null;
  predictions?: IPrediction[] | null;
}

export const defaultValue: Readonly<ICryptocurrency> = {};
