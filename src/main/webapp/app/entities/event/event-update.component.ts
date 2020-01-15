import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { IEvent, Event } from 'app/shared/model/event.model';
import { EventService } from './event.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html'
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;

  clubs: IClub[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    eventName: [null, [Validators.required]],
    clubId: []
  });

  constructor(
    protected eventService: EventService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      this.updateForm(event);

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

  updateForm(event: IEvent): void {
    this.editForm.patchValue({
      id: event.id,
      date: event.date,
      eventName: event.eventName,
      clubId: event.clubId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  private createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      eventName: this.editForm.get(['eventName'])!.value,
      clubId: this.editForm.get(['clubId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
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

  trackById(index: number, item: IClub): any {
    return item.id;
  }
}
