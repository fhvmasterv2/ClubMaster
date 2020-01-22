import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMember } from 'app/shared/model/member.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { MemberService } from './member.service';
import { MemberDeleteDialogComponent } from './member-delete-dialog.component';
import { ClubService } from 'app/entities/club/club.service';

@Component({
  selector: 'jhi-member',
  templateUrl: './member.component.html'
})
export class MemberComponent implements OnInit, OnDestroy {
  members?: IMember[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  idNames: { id: number; name: string }[];

  constructor(
    protected memberService: MemberService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number): void {
    const pageToLoad: number = page ? page : this.page;
    this.memberService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IMember[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.ascending = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
      this.ngbPaginationPage = data.pagingParams.page;
      this.loadPage();
    });
    this.registerChangeInMembers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMember): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMembers(): void {
    this.eventSubscriber = this.eventManager.subscribe('memberListModification', () => this.loadPage());
  }

  delete(member: IMember): void {
    const modalRef = this.modalService.open(MemberDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.member = member;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IMember[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.router.navigate(['/member'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.members = data ? data : [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }

  getClubname(clubId: number): string {
    // let name: string | undefined = "a";

    // this.clubService.find(clubId).subscribe((res: HttpResponse<IClub>) => {
    //     name = res.body.clubName
    //   },
    //   () => this.onError());
    // return name;

    return 'a';
  }
}
