import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Conductor, IConductor } from '../conductor.model';
import { ServiceService } from '../service/service.service';

@Component({
  selector: 'jhi-detail',
  templateUrl: './detail.component.html',
})
export class DetailComponent implements OnInit {
  conductor: Conductor | null = null;
  fotoUno: any;
  fotoDos: any;

  constructor(protected activationRouter: ActivatedRoute, protected conductorService: ServiceService) {}

  ngOnInit(): void {
    this.activationRouter.data.subscribe(({ conductor }) => {
      this.conductor = conductor;
      if (this.conductor?.id) {
        this.consultarFoto(this.conductor.id);
      }
    });
  }

  foto(conductor: IConductor): void {
    const file = new Blob([conductor.foto], { type: 'imagen/jpg' });
    const fileURL = URL.createObjectURL(file);
    window.open(fileURL);
  }

  consultarFoto(id: number): void {
    this.conductorService.consultarFoto(id).suscribe((res: any) => {
      this.fotoUno = new Blob([res], { type: 'image/jpg' });
      this.fotoDos = res.body;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
