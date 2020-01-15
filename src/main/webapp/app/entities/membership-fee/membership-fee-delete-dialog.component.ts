import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMembershipFee } from 'app/shared/model/membership-fee.model';
import { MembershipFeeService } from './membership-fee.service';

@Component({
  templateUrl: './membership-fee-delete-dialog.component.html'
})
export class MembershipFeeDeleteDialogComponent {
  membershipFee?: IMembershipFee;

  constructor(
    protected membershipFeeService: MembershipFeeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.membershipFeeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('membershipFeeListModification');
      this.activeModal.close();
    });
  }
}
