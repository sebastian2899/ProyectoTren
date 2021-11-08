import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITiket, Tiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';

@Component({
  selector: 'jhi-tiket-update',
  templateUrl: './tiket-update.component.html',
})
export class TiketUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fecha: [],
    trenId: [],
    clienteId: [],
    puesto: [],
    estado: [],
    jordana: [],
  });

  constructor(protected tiketService: TiketService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiket }) => {
      if (tiket.id === undefined) {
        const today = dayjs().startOf('day');
        tiket.fecha = today;
      }

      this.updateForm(tiket);
    });
  }

  previousState(): void {
    window.history.back();
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
    // Api for inheritance.
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
    };
  }
}
