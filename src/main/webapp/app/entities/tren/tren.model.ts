export interface ITren {
  id?: number;
  asientos?: number | null;
}

export class Tren implements ITren {
  constructor(public id?: number, public asientos?: number | null) {}
}

export function getTrenIdentifier(tren: ITren): number | undefined {
  return tren.id;
}
