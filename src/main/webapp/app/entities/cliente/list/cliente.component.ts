import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ClienteDeleteDialogComponent } from '../delete/cliente-delete-dialog.component';

@Component({
  selector: 'jhi-cliente',
  templateUrl: './cliente.component.html',
})
export class ClienteComponent implements OnInit {
  clientes?: ICliente[];
  isLoading = false;
  nombreFiltro: string | undefined;

  constructor(protected clienteService: ClienteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    if (this.nombreFiltro && this.nombreFiltro.length > 2) {
      this.clienteService.filtro(this.nombreFiltro).subscribe(
        (res: HttpResponse<ICliente[]>) => {
          this.isLoading = false;
          this.clientes = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    } else if (this.nombreFiltro!.length === 0) {
      this.cargarFiltro();
    } else {
      this.isLoading = false;
    }
  }

  cargarFiltro(): void {
    if (!this.nombreFiltro) {
      this.nombreFiltro = 'undefined';
    }
    this.clienteService.filtro(this.nombreFiltro).subscribe(
      (res: HttpResponse<ICliente[]>) => {
        this.isLoading = false;
        this.clientes = res.body ?? [];
        this.nombreFiltro = '';
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.cargarFiltro();
  }

  trackId(index: number, item: ICliente): number {
    return item.id!;
  }

  delete(cliente: ICliente): void {
    const modalRef = this.modalService.open(ClienteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cliente = cliente;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
