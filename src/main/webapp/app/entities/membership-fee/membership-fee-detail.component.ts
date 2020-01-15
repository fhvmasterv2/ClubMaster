import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMembershipFee } from 'app/shared/model/membership-fee.model';

@Component({
  selector: 'jhi-membership-fee-detail',
  templateUrl: './membership-fee-detail.component.html'
})
export class MembershipFeeDetailComponent implements OnInit {
  membershipFee: IMembershipFee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membershipFee }) => {
      this.membershipFee = membershipFee;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
