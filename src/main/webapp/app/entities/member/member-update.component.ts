import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from './member.service';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address/address.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';

type SelectableEntity = IAddress | IClub;

@Component({
  selector: 'jhi-member-update',
  templateUrl: './member-update.component.html'
})
export class MemberUpdateComponent implements OnInit {
  isSaving = false;

  addresses: IAddress[] = [];

  clubs: IClub[] = [];
  dobDp: any;

  editForm = this.fb.group({
    id: [],
    fname: [null, [Validators.required]],
    lname: [null, [Validators.required]],
    dob: [],
    addressId: [],
    clubId: []
  });

  constructor(
    protected memberService: MemberService,
    protected addressService: AddressService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ member }) => {
      this.updateForm(member);

      this.addressService
        .query({ filter: 'member-is-null' })
        .pipe(
          map((res: HttpResponse<IAddress[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IAddress[]) => {
          if (!member.addressId) {
            this.addresses = resBody;
          } else {
            this.addressService
              .find(member.addressId)
              .pipe(
                map((subRes: HttpResponse<IAddress>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAddress[]) => {
                this.addresses = concatRes;
              });
          }
        });

      this.clubService
        .query()
        .pipe(
          map((res: HttpResponse<IClub[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IClub[]) => (this.clubs = resBody));
    });
  }

  updateForm(member: IMember): void {
    this.editForm.patchValue({
      id: member.id,
      fname: member.fname,
      lname: member.lname,
      dob: member.dob,
      addressId: member.addressId,
      clubId: member.clubId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const member = this.createFromForm();
    if (member.id !== undefined) {
      this.subscribeToSaveResponse(this.memberService.update(member));
    } else {
      this.subscribeToSaveResponse(this.memberService.create(member));
    }
  }

  private createFromForm(): IMember {
    return {
      ...new Member(),
      id: this.editForm.get(['id'])!.value,
      fname: this.editForm.get(['fname'])!.value,
      lname: this.editForm.get(['lname'])!.value,
      dob: this.editForm.get(['dob'])!.value,
      addressId: this.editForm.get(['addressId'])!.value,
      clubId: this.editForm.get(['clubId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMember>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
