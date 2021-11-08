import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITren, getTrenIdentifier } from '../tren.model';

export type EntityResponseType = HttpResponse<ITren>;
export type EntityArrayResponseType = HttpResponse<ITren[]>;

@Injectable({ providedIn: 'root' })
export class TrenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tren: ITren): Observable<EntityResponseType> {
    return this.http.post<ITren>(this.resourceUrl, tren, { observe: 'response' });
  }

  update(tren: ITren): Observable<EntityResponseType> {
    return this.http.put<ITren>(`${this.resourceUrl}/${getTrenIdentifier(tren) as number}`, tren, { observe: 'response' });
  }

  partialUpdate(tren: ITren): Observable<EntityResponseType> {
    return this.http.patch<ITren>(`${this.resourceUrl}/${getTrenIdentifier(tren) as number}`, tren, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITren>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITren[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrenToCollectionIfMissing(trenCollection: ITren[], ...trensToCheck: (ITren | null | undefined)[]): ITren[] {
    const trens: ITren[] = trensToCheck.filter(isPresent);
    if (trens.length > 0) {
      const trenCollectionIdentifiers = trenCollection.map(trenItem => getTrenIdentifier(trenItem)!);
      const trensToAdd = trens.filter(trenItem => {
        const trenIdentifier = getTrenIdentifier(trenItem);
        if (trenIdentifier == null || trenCollectionIdentifiers.includes(trenIdentifier)) {
          return false;
        }
        trenCollectionIdentifiers.push(trenIdentifier);
        return true;
      });
      return [...trensToAdd, ...trenCollection];
    }
    return trenCollection;
  }
}
