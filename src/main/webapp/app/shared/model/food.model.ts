export interface IFood {
  id?: number;
  amount?: number;
}

export class Food implements IFood {
  constructor(public id?: number, public amount?: number) {}
}
