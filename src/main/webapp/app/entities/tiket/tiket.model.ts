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
}

export class Tiket implements ITiket {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public trenId?: number | null,
    public clienteId?: number | null,
    public puesto?: number | null,
    public estado?: string | null,
    public jordana?: string | null
  ) {}
}

export function getTiketIdentifier(tiket: ITiket): number | undefined {
  return tiket.id;
}
