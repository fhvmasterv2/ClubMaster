import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFood } from 'app/shared/model/food.model';
import { FoodService } from './food.service';

@Component({
  templateUrl: './food-delete-dialog.component.html'
})
export class FoodDeleteDialogComponent {
  food?: IFood;

  constructor(protected foodService: FoodService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.foodService.delete(id).subscribe(() => {
      this.eventManager.broadcast('foodListModification');
      this.activeModal.close();
    });
  }
}
