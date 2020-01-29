import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMember } from 'app/shared/model/member.model';

@Component({
  selector: 'jhi-member-detail',
  templateUrl: './member-detail.component.html'
})
export class MemberDetailComponent implements OnInit {
  member: IMember | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.member = member;
    });
  }

  previousState(): void {
    window.history.back();
  }

  addressToString(): string {
    return this.member.address.streetAddress + ', ' + this.member.address.postalCode + ', ' + this.member.address.city;
  }
}
