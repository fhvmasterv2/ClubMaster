import { Moment } from 'moment';
import { IMember } from 'app/shared/model/member.model';
import { IEvent } from 'app/shared/model/event.model';
import { ClubType } from 'app/shared/model/enumerations/club-type.model';

export interface IClub {
  id?: number;
  clubName?: string;
  creationDate?: Moment;
  type?: ClubType;
  addressId?: number;
  members?: IMember[];
  events?: IEvent[];
}

export class Club implements IClub {
  constructor(
    public id?: number,
    public clubName?: string,
    public creationDate?: Moment,
    public type?: ClubType,
    public addressId?: number,
    public members?: IMember[],
    public events?: IEvent[]
  ) {}
}
