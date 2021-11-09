import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'trainProyectApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'tren',
        data: { pageTitle: 'trainProyectApp.tren.home.title' },
        loadChildren: () => import('./tren/tren.module').then(m => m.TrenModule),
      },
      {
        path: 'tiket',
        data: { pageTitle: 'trainProyectApp.tiket.home.title' },
        loadChildren: () => import('./tiket/tiket.module').then(m => m.TiketModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
