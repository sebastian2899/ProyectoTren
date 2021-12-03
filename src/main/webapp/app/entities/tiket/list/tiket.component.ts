import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';
import { TiketDeleteDialogComponent } from '../delete/tiket-delete-dialog.component';
import { IRegistroHistoricoTiket } from '../consultFech';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-tiket',
  templateUrl: './tiket.component.html',
})
export class TiketComponent implements OnInit {
  tikets?: ITiket[];
  isLoading = false;
  tiketFechas?: IRegistroHistoricoTiket[] | null;
  fechaI?: dayjs.Dayjs | null;
  fechaF?: dayjs.Dayjs | null;

  constructor(protected tiketService: TiketService, protected modalService: NgbModal) {
    this.fechaI = dayjs().startOf('day');
    this.fechaF = dayjs().startOf('day');
  }

  ngOnInit(): void {
    this.fechaI = dayjs().startOf('day');
    this.fechaF = dayjs().startOf('day');
    this.loadAll();
  }

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

  cargarTiketFechas(): void {
    if (this.fechaI && this.fechaF) {
      this.tiketService
        .findByFechas(this.fechaI.toString(), this.fechaF.toString())
        .subscribe((data: HttpResponse<IRegistroHistoricoTiket[]>) => {
          this.tiketFechas = data.body ?? null;
        });
    }
  }

  findAllTikets(): void {
    this.isLoading = true;
    this.tiketService.findTikets().subscribe(
      (data: HttpResponse<ITiket[]>) => {
        this.isLoading = false;
        this.tikets = data.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
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
