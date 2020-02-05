import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'member',
        loadChildren: () => import('./member/member.module').then(m => m.ClubmasterMemberModule)
      },
      {
        path: 'address',
        loadChildren: () => import('./address/address.module').then(m => m.ClubmasterAddressModule)
      },
      {
        path: 'club',
        loadChildren: () => import('./club/club.module').then(m => m.ClubmasterClubModule)
      },
      {
        path: 'event',
        loadChildren: () => import('./event/event.module').then(m => m.ClubmasterEventModule)
      },
      {
        path: 'membership-fee',
        loadChildren: () => import('./membership-fee/membership-fee.module').then(m => m.ClubmasterMembershipFeeModule)
      },
      {
        path: 'food',
        loadChildren: () => import('./food/food.module').then(m => m.ClubmasterFoodModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ClubmasterEntityModule {}
