import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITiket, Tiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ITren } from 'app/entities/tren/tren.model';
import { TrenService } from 'app/entities/tren/service/tren.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-tiket-update',
  templateUrl: './tiket-update.component.html',
})
export class TiketUpdateComponent implements OnInit {
  isSaving = false;
  clientes: ICliente[] = [];
  trenes: ITren[] = [];
  tren?: ITren | null;

  estadoTiket = ['Activo', 'EN ESPERA', 'NO DISPONIBLE'];
  tipoJornada = ['MAÑANA', 'TARDE', 'NOCHE'];

  editForm = this.fb.group({
    id: [],
    fecha: [],
    trenId: [],
    asiento: [],
    clienteId: [],
    puesto: [],
    estado: [],
    jordana: [],
    precioTiket: [],
    precioTotal: [],
  });

  constructor(
    private alertSerice: AlertService,
    private router: Router,
    protected clienteService: ClienteService,
    protected trenService: TrenService,
    protected tiketService: TiketService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiket }) => {
      if (tiket.id === undefined) {
        const today = dayjs().startOf('day');
        tiket.fecha = today;
      }

      this.updateForm(tiket);
      this.cargarClientes();
      this.cargarTren();
    });
  }

  previousState(): void {
    window.history.back();
  }

  cargarClientes(): void {
    this.clienteService.query().subscribe(
      (res: HttpResponse<ICliente[]>) => {
        this.clientes = res.body ?? [];
      },
      () => {
        this.clientes = [];
      }
    );
  }

  calcularPrecioTotal(): void {
    const jornada = this.editForm.get(['jordana'])!.value;
    const precioNoche = 1500;
    const precioTiket = 4000;
    const estadoTiquete = this.editForm.get(['estado'])!.value;
    let precioTotal = 0;

    this.editForm.get(['precioTiket'])?.setValue(precioTiket);

    if (
      (jornada === 'MAÑANA' || jornada === 'TARDE' || jornada === 'NOCHE') &&
      (estadoTiquete === 'NO DISPONIBLE' || estadoTiquete === 'EN ESPERA')
    ) {
      this.editForm.get(['precioTotal'])?.setValue(0);
      this.editForm.get(['precioTiket'])?.setValue(0);
    } else if (jornada === 'NOCHE' && estadoTiquete === 'Activo') {
      precioTotal = precioTiket - precioNoche;
      this.editForm.get(['precioTotal'])?.setValue(precioTotal);
    } else {
      this.editForm.get(['precioTotal'])?.setValue(precioTiket);
    }
  }

  cargarPuesto(): void {
    const trenSeleccionado = this.editForm.get(['trenId'])!.value;

    this.tiketService.puesto(trenSeleccionado).subscribe((res: HttpResponse<ITren>) => {
      this.tren = res.body ?? null;
    });

    this.editForm.get(['asiento'])!.setValue(this.tren?.asientos);
  }

  cargarTren(): void {
    this.trenService.query().subscribe(
      (res: HttpResponse<ITren[]>) => {
        this.trenes = res.body ?? [];
      },
      () => {
        this.trenes = [];
      }
    );
  }

  save(): void {
    this.isSaving = true;
    const tiket = this.createFromForm();

    if (tiket.id !== undefined) {
      this.subscribeToSaveResponse(this.tiketService.update(tiket));
    } else {
      this.subscribeToSaveResponse(this.tiketService.create(tiket));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITiket>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    this.alertSerice.addAlert({
      type: 'warning',
      message: 'Puesto ya seleccionado.',
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tiket: ITiket): void {
    this.editForm.patchValue({
      id: tiket.id,
      fecha: tiket.fecha ? tiket.fecha.format(DATE_TIME_FORMAT) : null,
      trenId: tiket.trenId,
      clienteId: tiket.clienteId,
      puesto: tiket.puesto,
      estado: tiket.estado,
      jordana: tiket.jordana,
      precioTiket: tiket.precioTiket,
      precioTotal: tiket.precioTotal,
    });
  }

  protected createFromForm(): ITiket {
    return {
      ...new Tiket(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
      trenId: this.editForm.get(['trenId'])!.value,
      clienteId: this.editForm.get(['clienteId'])!.value,
      puesto: this.editForm.get(['puesto'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      jordana: this.editForm.get(['jordana'])!.value,
      precioTiket: this.editForm.get(['precioTiket'])!.value,
      precioTotal: this.editForm.get(['precioTotal'])!.value,
    };
  }
}
