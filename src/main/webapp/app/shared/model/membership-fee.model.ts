import { Moment } from 'moment';

export interface IMembershipFee {
  id?: number;
  amount?: number;
  dueDate?: Moment;
  isPaid?: boolean;
  memberId?: number;
}

export class MembershipFee implements IMembershipFee {
  constructor(public id?: number, public amount?: number, public dueDate?: Moment, public isPaid?: boolean, public memberId?: number) {
    this.isPaid = this.isPaid || false;
  }
}
