import { ICryptocurrency } from 'app/shared/model/cryptocurrency.model';

export interface IPrediction {
  id?: number;
  dateFrom?: string | null;
  dateTo?: string | null;
  duration?: string | null;
  currentPrice?: string | null;
  predictedPraice?: string | null;
  probability?: string | null;
  cryptocurrency?: ICryptocurrency | null;
}

export const defaultValue: Readonly<IPrediction> = {};
