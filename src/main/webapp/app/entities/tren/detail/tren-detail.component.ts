import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITren } from '../tren.model';

@Component({
  selector: 'jhi-tren-detail',
  templateUrl: './tren-detail.component.html',
})
export class TrenDetailComponent implements OnInit {
  tren: ITren | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tren }) => {
      this.tren = tren;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
