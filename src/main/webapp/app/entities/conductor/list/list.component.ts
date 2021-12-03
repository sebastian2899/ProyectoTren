import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IConductor } from '../conductor.model';
import { DeleteComponent } from '../delete/delete.component';
import { ServiceService } from '../service/service.service';

@Component({
  selector: 'jhi-list',
  templateUrl: './list.component.html',
})
export class ListComponent implements OnInit {
  conductores?: IConductor[] | null;
  isLoading = false;
  filtroNombreApellido?: string;

  constructor(protected conductorService: ServiceService, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.loadAll();
  }

  filtroIterativo(): void {
    this.isLoading = true;
    if (this.filtroNombreApellido && this.filtroNombreApellido.length > 2) {
      this.conductorService.filtro(this.filtroNombreApellido).subscribe((res: HttpResponse<IConductor[]>) => {
        this.isLoading = false;
        this.conductores = res.body ?? null;
      }),
        () => {
          this.isLoading = false;
        };
    } else if (this.filtroNombreApellido?.length === 0) {
      this.loadAll();
    } else {
      this.isLoading = false;
    }
  }

  loadAll(): void {
    if (!this.filtroNombreApellido) {
      this.filtroNombreApellido = 'undefined';
    }
    this.conductorService.filtro(this.filtroNombreApellido).subscribe((res: HttpResponse<IConductor[]>) => {
      this.isLoading = false;
      this.conductores = res.body ?? [];
      this.filtroNombreApellido = '';
    }),
      () => {
        this.isLoading = false;
      };
  }

  trackId(index: number, item: IConductor): number {
    return item.id!;
  }

  delete(conductor: IConductor): void {
    const modalRef = this.modalService.open(DeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.conductor = conductor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
