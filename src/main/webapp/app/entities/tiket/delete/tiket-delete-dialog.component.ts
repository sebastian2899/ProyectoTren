import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';

@Component({
  templateUrl: './tiket-delete-dialog.component.html',
})
export class TiketDeleteDialogComponent {
  tiket?: ITiket;

  constructor(protected tiketService: TiketService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tiketService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
