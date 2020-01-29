import { Moment } from 'moment';
import { IMembershipFee } from 'app/shared/model/membership-fee.model';

export interface IMember {
  id?: number;
  fname?: string;
  lname?: string;
  dob?: Moment;
  addressId?: number;
  fees?: IMembershipFee[];
  clubId?: number;
  address?: IAddress;
  club?: IClub;
}

export class Member implements IMember {
  constructor(
    public id?: number,
    public fname?: string,
    public lname?: string,
    public dob?: Moment,
    public addressId?: number,
    public fees?: IMembershipFee[],
    public clubId?: number,
    public address?: IAddress,
    public club?: IClub
  ) {}
}
