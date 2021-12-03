export interface ICliente {
  id?: number;
  nombre?: string | null;
  correo?: string | null;
  apellido?: string | null;
  tipoCliente?: string | null;
  foto?: any | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public correo?: string | null,
    public tipoCliente?: string | null,
    public apellido?: string | null,
    public foto?: any | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
