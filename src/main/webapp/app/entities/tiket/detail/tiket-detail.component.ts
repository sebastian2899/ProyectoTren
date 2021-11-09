import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITiket } from '../tiket.model';

@Component({
  selector: 'jhi-tiket-detail',
  templateUrl: './tiket-detail.component.html',
})
export class TiketDetailComponent implements OnInit {
  tiket: ITiket | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiket }) => {
      this.tiket = tiket;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
