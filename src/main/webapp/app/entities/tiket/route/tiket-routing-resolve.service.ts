import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITiket, Tiket } from '../tiket.model';
import { TiketService } from '../service/tiket.service';

@Injectable({ providedIn: 'root' })
export class TiketRoutingResolveService implements Resolve<ITiket> {
  constructor(protected service: TiketService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITiket> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tiket: HttpResponse<Tiket>) => {
          if (tiket.body) {
            return of(tiket.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tiket());
  }
}
