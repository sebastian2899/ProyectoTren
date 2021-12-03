export interface IRegistroHistoricoTiket {
  nombreCliente?: string | null;
  puesto?: number | null;
  precioTotal?: number | null;
  estado?: string | null;
  jornada?: string | null;
}

export class ResgistroHistoricoTiket implements IRegistroHistoricoTiket {
  public nombreCliente?: string | null;
  public puesto?: number | null;
  public precioTotal?: number | null;
  public estado?: string | null;
  public jornada?: string | null;
}
