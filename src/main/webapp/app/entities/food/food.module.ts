import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClubmasterSharedModule } from 'app/shared/shared.module';
import { FoodComponent } from './food.component';
import { FoodDetailComponent } from './food-detail.component';
import { FoodUpdateComponent } from './food-update.component';
import { FoodDeleteDialogComponent } from './food-delete-dialog.component';
import { foodRoute } from './food.route';

@NgModule({
  imports: [ClubmasterSharedModule, RouterModule.forChild(foodRoute)],
  declarations: [FoodComponent, FoodDetailComponent, FoodUpdateComponent, FoodDeleteDialogComponent],
  entryComponents: [FoodDeleteDialogComponent]
})
export class ClubmasterFoodModule {}
