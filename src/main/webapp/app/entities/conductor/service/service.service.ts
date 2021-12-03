import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import * as dayjs from 'dayjs';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { getConductorIdentifier, IConductor } from '../conductor.model';

export type EntityResponseType = HttpResponse<IConductor>;
export type EntityArrayResponseType = HttpResponse<IConductor[]>;
export type EntityResponseTypeFile = HttpResponse<number>;

@Injectable({
  providedIn: 'root',
})
export class ServiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/conductores');
  protected resourceUrlFiltro = this.applicationConfigService.getEndpointFor('api/filtroConductor');
  protected resourceUrlCargarFoto = this.applicationConfigService.getEndpointFor('api/cargarFoto');
  protected resourceUrlConsutlarFoto = this.applicationConfigService.getEndpointFor('api/consultarFotoConductor');
  protected resourceUrlActualizarFoto = this.applicationConfigService.getEndpointFor('api/actualizarFotoConductor');

  constructor(protected applicationConfigService: ApplicationConfigService, protected httpClient: HttpClient) {}

  create(conductor: IConductor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(conductor);
    return this.httpClient
      .post<IConductor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(conductor: IConductor): Observable<EntityResponseType> {
    return this.httpClient.put<IConductor>(`${this.resourceUrl}/${getConductorIdentifier(conductor) as number}`, conductor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.httpClient
      .get<IConductor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findAll(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.httpClient
      .get<IConductor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  cargarFoto(archivo?: any): Observable<EntityResponseTypeFile> {
    const formData: FormData = new FormData();
    formData.append('file', archivo);
    return this.httpClient.post<number>(this.resourceUrlCargarFoto, formData, { observe: 'response' });
  }

  consultarFoto(id: number): any {
    return this.httpClient.get<any>(`${this.resourceUrlConsutlarFoto}/${id}`);
  }

  actualizarFoto(archivo: any, idConductor: number): any {
    const formData: FormData = new FormData();
    formData.append('file', archivo);
    return this.httpClient.post<any>(`${this.resourceUrlActualizarFoto}/${idConductor}`, formData, { observe: 'response' });
  }

  filtro(nombreApellido: string): Observable<EntityArrayResponseType> {
    return this.httpClient.get<IConductor[]>(`${this.resourceUrlFiltro}/${nombreApellido}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.httpClient.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(conductor: IConductor): IConductor {
    return Object.assign({}, conductor, {
      fechanacimiento: conductor.fechaNacimiento?.isValid() ? conductor.fechaNacimiento.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((conductor: IConductor) => {
        conductor.fechaNacimiento = conductor.fechaNacimiento ? dayjs(conductor.fechaNacimiento) : undefined;
      });
    }
    return res;
  }
}
