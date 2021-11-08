import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITren } from '../tren.model';
import { TrenService } from '../service/tren.service';

@Component({
  templateUrl: './tren-delete-dialog.component.html',
})
export class TrenDeleteDialogComponent {
  tren?: ITren;

  constructor(protected trenService: TrenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
