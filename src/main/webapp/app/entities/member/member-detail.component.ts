import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { AddressService } from '.././address/address.service';
import { ClubService } from '.././club/club.service';

import { IMember } from 'app/shared/model/member.model';

@Component({
  selector: 'jhi-member-detail',
  templateUrl: './member-detail.component.html'
})
export class MemberDetailComponent implements OnInit {
  member: IMember | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected addressService: AddressService, protected clubService: ClubService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;

      if (this.member.addressId) {
        this.addressService.find(this.member.addressId).subscribe(addr => {
          this.member.address = addr.body;
        });
      }
      if (this.member.clubId) {
        this.clubService.find(this.member.clubId).subscribe(club => {
          this.member.club = club.body;
        });
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  addressToString(): string {
    return this.member.address.streetAddress + ', ' + this.member.address.postalCode + ', ' + this.member.address.city;
  }

  clubToString(): string {
    return this.member.club.clubName;
  }
}
