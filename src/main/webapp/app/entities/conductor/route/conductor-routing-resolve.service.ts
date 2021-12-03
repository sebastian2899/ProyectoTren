import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConductor, Conductor } from '../conductor.model';
import { ServiceService } from '../service/service.service';

@Injectable({ providedIn: 'root' })
export class ConductorRoutingResolveService implements Resolve<IConductor> {
  constructor(protected service: ServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConductor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((conductor: HttpResponse<Conductor>) => {
          if (conductor.body) {
            return of(conductor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Conductor());
  }
}
