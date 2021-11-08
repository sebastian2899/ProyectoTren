import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITiket, getTiketIdentifier } from '../tiket.model';

export type EntityResponseType = HttpResponse<ITiket>;
export type EntityArrayResponseType = HttpResponse<ITiket[]>;

@Injectable({ providedIn: 'root' })
export class TiketService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tikets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tiket: ITiket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tiket);
    return this.http
      .post<ITiket>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tiket: ITiket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tiket);
    return this.http
      .put<ITiket>(`${this.resourceUrl}/${getTiketIdentifier(tiket) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tiket: ITiket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tiket);
    return this.http
      .patch<ITiket>(`${this.resourceUrl}/${getTiketIdentifier(tiket) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITiket>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITiket[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTiketToCollectionIfMissing(tiketCollection: ITiket[], ...tiketsToCheck: (ITiket | null | undefined)[]): ITiket[] {
    const tikets: ITiket[] = tiketsToCheck.filter(isPresent);
    if (tikets.length > 0) {
      const tiketCollectionIdentifiers = tiketCollection.map(tiketItem => getTiketIdentifier(tiketItem)!);
      const tiketsToAdd = tikets.filter(tiketItem => {
        const tiketIdentifier = getTiketIdentifier(tiketItem);
        if (tiketIdentifier == null || tiketCollectionIdentifiers.includes(tiketIdentifier)) {
          return false;
        }
        tiketCollectionIdentifiers.push(tiketIdentifier);
        return true;
      });
      return [...tiketsToAdd, ...tiketCollection];
    }
    return tiketCollection;
  }

  protected convertDateFromClient(tiket: ITiket): ITiket {
    return Object.assign({}, tiket, {
      fecha: tiket.fecha?.isValid() ? tiket.fecha.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tiket: ITiket) => {
        tiket.fecha = tiket.fecha ? dayjs(tiket.fecha) : undefined;
      });
    }
    return res;
  }
}
