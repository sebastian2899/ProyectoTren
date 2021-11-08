import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';
import { TiketDeleteDialogComponent } from '../delete/tiket-delete-dialog.component';

@Component({
  selector: 'jhi-tiket',
  templateUrl: './tiket.component.html',
})
export class TiketComponent implements OnInit {
  tikets?: ITiket[];
  isLoading = false;

  constructor(protected tiketService: TiketService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tiketService.query().subscribe(
      (res: HttpResponse<ITiket[]>) => {
        this.isLoading = false;
        this.tikets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITiket): number {
    return item.id!;
  }

  delete(tiket: ITiket): void {
    const modalRef = this.modalService.open(TiketDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tiket = tiket;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
