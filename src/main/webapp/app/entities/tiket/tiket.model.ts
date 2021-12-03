import * as dayjs from 'dayjs';

export interface ITiket {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  trenId?: number | null;
  clienteId?: number | null;
  nombreCliente?: string | null;
  puesto?: number | null;
  estado?: string | null;
  jordana?: string | null;
  precioTiket?: number | null;
  precioTotal?: number | null;
}

export class Tiket implements ITiket {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public trenId?: number | null,
    public clienteId?: number | null,
    public nombreCliente?: string | null,
    public puesto?: number | null,
    public estado?: string | null,
    public jordana?: string | null,
    public precioTiket?: number | null,
    public precioTotal?: number | null
  ) {}
}

export function getTiketIdentifier(tiket: ITiket): number | undefined {
  return tiket.id;
}
