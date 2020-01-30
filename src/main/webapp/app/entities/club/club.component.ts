import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClub } from 'app/shared/model/club.model';
import { ClubService } from './club.service';
import { ClubDeleteDialogComponent } from './club-delete-dialog.component';
import { AddressService } from 'app/entities/address/address.service';
import { Address } from 'app/shared/model/address.model';

@Component({
  selector: 'jhi-club',
  templateUrl: './club.component.html'
})
export class ClubComponent implements OnInit, OnDestroy {
  clubs?: IClub[];
  eventSubscriber?: Subscription;
  addresses: ClubAddress[];

  constructor(
    protected clubService: ClubService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected addressService: AddressService
  ) {}

  loadAll(): void {
    this.clubService.query().subscribe((res: HttpResponse<IClub[]>) => {
      this.clubs = res.body ? res.body : [];

      this.clubs.forEach(club => {
        if (club.addressId) {
          this.addressService.find(club.addressId).subscribe(addr => {
            const id = club.id;
            this.addresses.push({ clubId: id, address: addr.body });
          });
        }
      });
    });
  }

  ngOnInit(): void {
    this.addresses = [];
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

  addressToString(clubId: number): string {
    const a = this.addresses.find(x => x.clubId === clubId).address;
    return a.streetAddress + ', ' + a.postalCode + ', ' + a.city;
  }
}

interface ClubAddress {
  clubId?: number;
  address?: Address;
}
