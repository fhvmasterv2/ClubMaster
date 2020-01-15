import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMembershipFee, MembershipFee } from 'app/shared/model/membership-fee.model';
import { MembershipFeeService } from './membership-fee.service';
import { MembershipFeeComponent } from './membership-fee.component';
import { MembershipFeeDetailComponent } from './membership-fee-detail.component';
import { MembershipFeeUpdateComponent } from './membership-fee-update.component';

@Injectable({ providedIn: 'root' })
export class MembershipFeeResolve implements Resolve<IMembershipFee> {
  constructor(private service: MembershipFeeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMembershipFee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((membershipFee: HttpResponse<MembershipFee>) => {
          if (membershipFee.body) {
            return of(membershipFee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MembershipFee());
  }
}

export const membershipFeeRoute: Routes = [
  {
    path: '',
    component: MembershipFeeComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.membershipFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MembershipFeeDetailComponent,
    resolve: {
      membershipFee: MembershipFeeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.membershipFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MembershipFeeUpdateComponent,
    resolve: {
      membershipFee: MembershipFeeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.membershipFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MembershipFeeUpdateComponent,
    resolve: {
      membershipFee: MembershipFeeResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'clubmasterApp.membershipFee.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
