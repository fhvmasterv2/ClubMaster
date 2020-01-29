import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IClub, Club } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { IAddress } from 'app/shared/model/address.model';
import { AddressService } from 'app/entities/address/address.service';

@Component({
  selector: 'jhi-club-update',
  templateUrl: './club-update.component.html'
})
export class ClubUpdateComponent implements OnInit {
  isSaving = false;

  addresses: IAddress[] = [];
  creationDateDp: any;

  editForm = this.fb.group({
    id: [],
    clubName: [null, [Validators.required]],
    creationDate: [],
    type: [],
    addressId: []
  });

  constructor(
    protected clubService: ClubService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ club }) => {
      this.updateForm(club);

      this.addressService
        .query({ filter: 'club-is-null' })
        .pipe(
          map((res: HttpResponse<IAddress[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IAddress[]) => {
          if (!club.addressId) {
            this.addresses = resBody;
          } else {
            this.addressService
              .find(club.addressId)
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
    });
  }

  updateForm(club: IClub): void {
    this.editForm.patchValue({
      id: club.id,
      clubName: club.clubName,
      creationDate: club.creationDate,
      type: club.type,
      addressId: club.addressId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const club = this.createFromForm();
    if (club.id !== undefined) {
      this.subscribeToSaveResponse(this.clubService.update(club));
    } else {
      this.subscribeToSaveResponse(this.clubService.create(club));
    }
  }

  private createFromForm(): IClub {
    return {
      ...new Club(),
      id: this.editForm.get(['id'])!.value,
      clubName: this.editForm.get(['clubName'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      type: this.editForm.get(['type'])!.value,
      addressId: this.editForm.get(['addressId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClub>>): void {
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

  trackById(index: number, item: IAddress): any {
    return item.id;
  }

  addressToString(addressOption: IAddress): string {
    return addressOption.streetAddress + ', ' + addressOption.postalCode + ', ' + addressOption.city;
  }
}
