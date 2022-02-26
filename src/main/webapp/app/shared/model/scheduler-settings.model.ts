export interface ISchedulerSettings {
  id?: number;
  interval?: string | null;
  limit?: string | null;
}

export const defaultValue: Readonly<ISchedulerSettings> = {};
