import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClubmasterSharedModule } from 'app/shared/shared.module';
import { MemberComponent } from './member.component';
import { MemberDetailComponent } from './member-detail.component';
import { MemberUpdateComponent } from './member-update.component';
import { MemberDeleteDialogComponent } from './member-delete-dialog.component';
import { memberRoute } from './member.route';

@NgModule({
  imports: [ClubmasterSharedModule, RouterModule.forChild(memberRoute)],
  declarations: [MemberComponent, MemberDetailComponent, MemberUpdateComponent, MemberDeleteDialogComponent],
  entryComponents: [MemberDeleteDialogComponent]
})
export class ClubmasterMemberModule {}
