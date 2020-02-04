import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClub } from 'app/shared/model/club.model';
import { AddressService } from 'app/entities/address/address.service';

@Component({
  selector: 'jhi-club-detail',
  templateUrl: './club-detail.component.html'
})
export class ClubDetailComponent implements OnInit {
  club: IClub | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected addressService: AddressService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ club }) => {
      this.club = club;

      if (this.club.addressId) {
        this.addressService.find(this.club.addressId).subscribe(addr => {
          this.club.address = addr.body;
        });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  addressToString(): string {
    return this.club.address.streetAddress + ', ' + this.club.address.postalCode + ', ' + this.club.address.city;
  }
}
