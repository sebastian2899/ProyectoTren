import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IConductor } from '../conductor.model';
import { ServiceService } from '../service/service.service';

@Component({
  templateUrl: './delete.component.html',
})
export class DeleteComponent {
  conductor?: IConductor;
  constructor(protected conductorService: ServiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.conductorService.delete(id).subscribe(() => this.activeModal.close('deleted'));
  }
}
