import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { AlertService } from 'app/core/util/alert.service';
import { Cliente } from 'app/entities/cliente/cliente.model';
import * as dayjs from 'dayjs';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Conductor, IConductor } from '../conductor.model';
import { ServiceService } from '../service/service.service';

@Component({
  selector: 'jhi-update',
  templateUrl: './update.component.html',
})
export class UpdateComponent implements OnInit {
  isSaving = false;
  conductor?: IConductor | null;
  idConductor?: number | undefined;

  editForm = this.fb.group({
    id: [],
    nombres: [],
    apellidos: [],
    telefono: [],
    sueldo: [],
    fechaNacimiento: [],
    foto: [],
  });

  constructor(
    private alertService: AlertService,
    protected conductorService: ServiceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.conductor?.id === undefined) {
      const today = dayjs().startOf('day');
      this.conductor!.fechaNacimiento = today;
    }

    this.activatedRoute.data.subscribe(({ conductor }) => {
      this.conductor = conductor;
      if (this.conductor) {
        this.idConductor = this.conductor.id;
      }
      this.updateForm(conductor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conductor = this.createFromForm();
    if (conductor.id !== undefined) {
      this.subscribeToSaveResponse(this.conductorService.update(conductor));
    } else {
      this.subscribeToSaveResponse(this.conductorService.create(conductor));
    }
  }

  onFileSelected(archi: any): void {
    const file = archi.target.files[0];
    if (this.idConductor !== undefined) {
      this.conductorService.actualizarFoto(file, this.idConductor).subscribe(
        (res: any) => {
          if (this.conductor) {
            this.conductor.foto = res;
          }
          this.alertService.addAlert({
            type: 'success',
            message: 'Foto actualizada exitosamente',
          });
        },
        () => {
          //error no es
          this.alertService.addAlert({
            type: 'success',
            message: 'Foto actualizada exitosamente',
          });
        }
      );
    } else {
      this.conductorService.cargarFoto(file).subscribe(
        (res: HttpResponse<number>) => {
          if (res.body) {
            this.conductor = new Conductor();
            this.conductor.id = res.body;
          }
        },
        () => {
          this.conductor = null;
        }
      );
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConductor>>): void {
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

  protected updateForm(conductor: IConductor): void {
    this.editForm.patchValue({
      id: conductor.id,
      nombres: conductor.nombres,
      apellidos: conductor.apellidos,
      telefono: conductor.telefono,
      sueldo: conductor.sueldo,
      foto: conductor.foto,
      fechaNacimiento: conductor.fechaNacimiento ? conductor.fechaNacimiento.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IConductor {
    return {
      ...new Cliente(),
      id: this.conductor?.id,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      sueldo: this.editForm.get(['sueldo'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value
        ? dayjs(this.editForm.get(['fechaNacimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
