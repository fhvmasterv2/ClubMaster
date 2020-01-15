import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClubmasterSharedModule } from 'app/shared/shared.module';
import { MembershipFeeComponent } from './membership-fee.component';
import { MembershipFeeDetailComponent } from './membership-fee-detail.component';
import { MembershipFeeUpdateComponent } from './membership-fee-update.component';
import { MembershipFeeDeleteDialogComponent } from './membership-fee-delete-dialog.component';
import { membershipFeeRoute } from './membership-fee.route';

@NgModule({
  imports: [ClubmasterSharedModule, RouterModule.forChild(membershipFeeRoute)],
  declarations: [MembershipFeeComponent, MembershipFeeDetailComponent, MembershipFeeUpdateComponent, MembershipFeeDeleteDialogComponent],
  entryComponents: [MembershipFeeDeleteDialogComponent]
})
export class ClubmasterMembershipFeeModule {}
