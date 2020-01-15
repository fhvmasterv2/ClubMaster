import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IMembershipFee, MembershipFee } from 'app/shared/model/membership-fee.model';
import { MembershipFeeService } from './membership-fee.service';
import { IMember } from 'app/shared/model/member.model';
import { MemberService } from 'app/entities/member/member.service';

@Component({
  selector: 'jhi-membership-fee-update',
  templateUrl: './membership-fee-update.component.html'
})
export class MembershipFeeUpdateComponent implements OnInit {
  isSaving = false;

  members: IMember[] = [];
  dueDateDp: any;

  editForm = this.fb.group({
    id: [],
    amount: [],
    dueDate: [],
    isPaid: [],
    memberId: []
  });

  constructor(
    protected membershipFeeService: MembershipFeeService,
    protected memberService: MemberService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membershipFee }) => {
      this.updateForm(membershipFee);

      this.memberService
        .query()
        .pipe(
          map((res: HttpResponse<IMember[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IMember[]) => (this.members = resBody));
    });
  }

  updateForm(membershipFee: IMembershipFee): void {
    this.editForm.patchValue({
      id: membershipFee.id,
      amount: membershipFee.amount,
      dueDate: membershipFee.dueDate,
      isPaid: membershipFee.isPaid,
      memberId: membershipFee.memberId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const membershipFee = this.createFromForm();
    if (membershipFee.id !== undefined) {
      this.subscribeToSaveResponse(this.membershipFeeService.update(membershipFee));
    } else {
      this.subscribeToSaveResponse(this.membershipFeeService.create(membershipFee));
    }
  }

  private createFromForm(): IMembershipFee {
    return {
      ...new MembershipFee(),
      id: this.editForm.get(['id'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      dueDate: this.editForm.get(['dueDate'])!.value,
      isPaid: this.editForm.get(['isPaid'])!.value,
      memberId: this.editForm.get(['memberId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMembershipFee>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IMember): any {
    return item.id;
  }
}
