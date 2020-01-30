import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembershipFee } from 'app/shared/model/membership-fee.model';
import { MembershipFeeService } from './membership-fee.service';
import { MembershipFeeDeleteDialogComponent } from './membership-fee-delete-dialog.component';
import { Member } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';

@Component({
  selector: 'jhi-membership-fee',
  templateUrl: './membership-fee.component.html'
})
export class MembershipFeeComponent implements OnInit, OnDestroy {
  membershipFees?: IMembershipFee[];
  eventSubscriber?: Subscription;
  members: FeeMember[] = [];

  constructor(
    protected membershipFeeService: MembershipFeeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected memberService: MemberService
  ) {}

  loadAll(): void {
    this.membershipFeeService.query().subscribe((res: HttpResponse<IMembershipFee[]>) => {
      this.membershipFees = res.body ? res.body : [];

      this.membershipFees.forEach(fee => {
        if (fee.memberId) {
          this.memberService.find(fee.memberId).subscribe(member => {
            const id = fee.id;
            this.members.push({ feeId: id, member: member.body });
          });
        }
      });
    });
  }

  ngOnInit(): void {
    this.members = [];
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

  memberToString(fId: number): string {
    const a = this.members.find(x => x.feeId === fId).member;
    return a.fname + ' ' + a.lname;
  }
}

interface FeeMember {
  feeId?: number;
  member?: Member;
}
