export interface IAddress {
  id?: number;
  streetAddress?: string;
  postalCode?: string;
  city?: string;
}

export class Address implements IAddress {
  constructor(public id?: number, public streetAddress?: string, public postalCode?: string, public city?: string) {}

  toString(): string {
    return this.streetAddress + ', ' + this.postalCode + ', ' + this.city;
  }
}
