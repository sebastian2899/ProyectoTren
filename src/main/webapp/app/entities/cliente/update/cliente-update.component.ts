import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;
  cliente?: ICliente;
  idCliente?: number;
  tipoCliente = ['Premium', 'Premium Plus', 'Basic'];

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellido: [],
    tipoCliente: [],
    correo: [],
    foto: [],
  });

  constructor(
    protected clienteService: ClienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.cliente = cliente;
      if (this.cliente) {
        this.idCliente = this.cliente.id;
      }
      this.updateForm(cliente);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  onFileSelected(archivo: any): void {
    const file = archivo.target.files[0];
    if (this.idCliente !== undefined) {
      this.clienteService.actualizarFoto(file, this.idCliente).subscribe((res: HttpResponse<any>) => {
        if (this.cliente) {
          this.cliente.foto = res;
        }
        this.alertService.addAlert({
          type: 'success',
          message: 'Foto actualizada con exito.',
        });
      });
    } else {
      this.clienteService.cargarFoto(file).subscribe((res: HttpResponse<number>) => {
        if (res.body !== null) {
          this.cliente = new Cliente();
          this.cliente.id = res.body;
        }
      });
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      tipoCliente: cliente.tipoCliente,
      foto: cliente.foto,
      correo: cliente.correo,
    });
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.cliente?.id,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      tipoCliente: this.editForm.get(['tipoCliente'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      correo: this.editForm.get(['correo'])!.value,
    };
  }
}
