import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITren, Tren } from '../tren.model';
import { TrenService } from '../service/tren.service';

@Injectable({ providedIn: 'root' })
export class TrenRoutingResolveService implements Resolve<ITren> {
  constructor(protected service: TrenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITren> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tren: HttpResponse<Tren>) => {
          if (tren.body) {
            return of(tren.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tren());
  }
}
