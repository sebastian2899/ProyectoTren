import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICliente, getClienteIdentifier } from '../cliente.model';

export type EntityResponseType = HttpResponse<ICliente>;
export type EntityArrayResponseType = HttpResponse<ICliente[]>;
export type NumberType = HttpResponse<number>;

@Injectable({ providedIn: 'root' })
export class ClienteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clientes');
  protected resourceUrlFiltro = this.applicationConfigService.getEndpointFor('api/filtro');
  protected resourceUrlCargarFoto = this.applicationConfigService.getEndpointFor('api/cargarFotoCliente');
  protected resourceUrlActualizarFoto = this.applicationConfigService.getEndpointFor('api/actualizarFotoCliente');
  protected resourceUrlConsultarFoto = this.applicationConfigService.getEndpointFor('api/consultarFotoCliente');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cliente: ICliente): Observable<EntityResponseType> {
    return this.http.post<ICliente>(this.resourceUrl, cliente, { observe: 'response' });
  }

  update(cliente: ICliente): Observable<EntityResponseType> {
    return this.http.put<ICliente>(`${this.resourceUrl}/${getClienteIdentifier(cliente) as number}`, cliente, { observe: 'response' });
  }

  partialUpdate(cliente: ICliente): Observable<EntityResponseType> {
    return this.http.patch<ICliente>(`${this.resourceUrl}/${getClienteIdentifier(cliente) as number}`, cliente, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICliente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICliente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  filtro(nombreFiltro: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICliente[]>(`${this.resourceUrlFiltro}/${nombreFiltro}`, { observe: 'response' });
  }

  cargarFoto(archivo: any): Observable<NumberType> {
    const formdata: FormData = new FormData();
    formdata.append('file', archivo);

    return this.http.post<number>(this.resourceUrlCargarFoto, formdata, { observe: 'response' });
  }

  actualizarFoto(archivo: any, idCliente: number): Observable<any> {
    const formdata: FormData = new FormData();
    formdata.append('file', archivo);

    return this.http.post<any>(`${this.resourceUrlActualizarFoto}/${idCliente}`, formdata, { observe: 'response' });
  }

  consultarFoto(idCliente: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrlFiltro}/${idCliente}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addClienteToCollectionIfMissing(clienteCollection: ICliente[], ...clientesToCheck: (ICliente | null | undefined)[]): ICliente[] {
    const clientes: ICliente[] = clientesToCheck.filter(isPresent);
    if (clientes.length > 0) {
      const clienteCollectionIdentifiers = clienteCollection.map(clienteItem => getClienteIdentifier(clienteItem)!);
      const clientesToAdd = clientes.filter(clienteItem => {
        const clienteIdentifier = getClienteIdentifier(clienteItem);
        if (clienteIdentifier == null || clienteCollectionIdentifiers.includes(clienteIdentifier)) {
          return false;
        }
        clienteCollectionIdentifiers.push(clienteIdentifier);
        return true;
      });
      return [...clientesToAdd, ...clienteCollection];
    }
    return clienteCollection;
  }
}
