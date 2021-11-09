import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITren, Tren } from '../tren.model';
import { TrenService } from '../service/tren.service';

@Component({
  selector: 'jhi-tren-update',
  templateUrl: './tren-update.component.html',
})
export class TrenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    asientos: [],
  });

  constructor(protected trenService: TrenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tren }) => {
      this.updateForm(tren);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tren = this.createFromForm();
    if (tren.id !== undefined) {
      this.subscribeToSaveResponse(this.trenService.update(tren));
    } else {
      this.subscribeToSaveResponse(this.trenService.create(tren));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITren>>): void {
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

  protected updateForm(tren: ITren): void {
    this.editForm.patchValue({
      id: tren.id,
      asientos: tren.asientos,
    });
  }

  protected createFromForm(): ITren {
    return {
      ...new Tren(),
      id: this.editForm.get(['id'])!.value,
      asientos: this.editForm.get(['asientos'])!.value,
    };
  }
}
