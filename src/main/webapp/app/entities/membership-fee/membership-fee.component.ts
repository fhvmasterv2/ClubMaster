import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembershipFee } from 'app/shared/model/membership-fee.model';
import { MembershipFeeService } from './membership-fee.service';
import { MembershipFeeDeleteDialogComponent } from './membership-fee-delete-dialog.component';

@Component({
  selector: 'jhi-membership-fee',
  templateUrl: './membership-fee.component.html'
})
export class MembershipFeeComponent implements OnInit, OnDestroy {
  membershipFees?: IMembershipFee[];
  eventSubscriber?: Subscription;

  constructor(
    protected membershipFeeService: MembershipFeeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.membershipFeeService.query().subscribe((res: HttpResponse<IMembershipFee[]>) => {
      this.membershipFees = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMembershipFees();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMembershipFee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMembershipFees(): void {
    this.eventSubscriber = this.eventManager.subscribe('membershipFeeListModification', () => this.loadAll());
  }

  delete(membershipFee: IMembershipFee): void {
    const modalRef = this.modalService.open(MembershipFeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.membershipFee = membershipFee;
  }
}
