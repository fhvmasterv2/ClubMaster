import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMember, Member } from 'app/shared/model/member.model';
import { MemberService } from './member.service';
import { MemberComponent } from './member.component';
import { MemberDetailComponent } from './member-detail.component';
import { MemberUpdateComponent } from './member-update.component';

@Injectable({ providedIn: 'root' })
export class MemberResolve implements Resolve<IMember> {
  constructor(private service: MemberService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMember> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((member: HttpResponse<Member>) => {
          if (member.body) {
            return of(member.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Member());
  }
}

export const memberRoute: Routes = [
  {
    path: '',
    component: MemberComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'clubmasterApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MemberDetailComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MemberUpdateComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MemberUpdateComponent,
    resolve: {
      member: MemberResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.member.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
