import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITren } from '../tren.model';
import { TrenService } from '../service/tren.service';
import { TrenDeleteDialogComponent } from '../delete/tren-delete-dialog.component';

@Component({
  selector: 'jhi-tren',
  templateUrl: './tren.component.html',
})
export class TrenComponent implements OnInit {
  trens?: ITren[];
  isLoading = false;

  constructor(protected trenService: TrenService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trenService.query().subscribe(
      (res: HttpResponse<ITren[]>) => {
        this.isLoading = false;
        this.trens = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITren): number {
    return item.id!;
  }

  delete(tren: ITren): void {
    const modalRef = this.modalService.open(TrenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tren = tren;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
