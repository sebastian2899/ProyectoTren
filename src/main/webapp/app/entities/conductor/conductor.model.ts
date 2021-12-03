import * as dayjs from 'dayjs';

export interface IConductor {
  id?: number;
  nombres?: string | null;
  apellidos?: string | null;
  telefono?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  edad?: number | null;
  sueldo?: number | null;
  foto?: any | null;
}

export class Conductor implements IConductor {
  constructor(
    public id?: number,
    public nombres?: string | null,
    public apellidos?: string | null,
    public telefono?: string | null,
    public sueldo?: number | null,
    public foto?: any | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public edad?: number | null
  ) {}
}

export function getConductorIdentifier(conductor: IConductor): number | undefined {
  return conductor.id;
}
