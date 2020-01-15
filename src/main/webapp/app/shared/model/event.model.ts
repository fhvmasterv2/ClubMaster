import { Moment } from 'moment';

export interface IEvent {
  id?: number;
  date?: Moment;
  eventName?: string;
  clubId?: number;
}

export class Event implements IEvent {
  constructor(public id?: number, public date?: Moment, public eventName?: string, public clubId?: number) {}
}
