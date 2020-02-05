import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { ClubDeleteDialogComponent } from './club-delete-dialog.component';

@Component({
  selector: 'jhi-club',
  templateUrl: './club.component.html'
})
export class ClubComponent implements OnInit, OnDestroy {
  clubs?: IClub[];
  eventSubscriber?: Subscription;

  constructor(protected clubService: ClubService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.clubService.query().subscribe((res: HttpResponse<IClub[]>) => {
      this.clubs = res.body ? res.body : [];
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInClubs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IClub): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInClubs(): void {
    this.eventSubscriber = this.eventManager.subscribe('clubListModification', () => this.loadAll());
  }

  delete(club: IClub): void {
    const modalRef = this.modalService.open(ClubDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.club = club;
  }
}
