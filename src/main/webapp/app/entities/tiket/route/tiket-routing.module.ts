import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TiketComponent } from '../list/tiket.component';
import { TiketDetailComponent } from '../detail/tiket-detail.component';
import { TiketUpdateComponent } from '../update/tiket-update.component';
import { TiketRoutingResolveService } from './tiket-routing-resolve.service';

const tiketRoute: Routes = [
  {
    path: '',
    component: TiketComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TiketDetailComponent,
    resolve: {
      tiket: TiketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TiketUpdateComponent,
    resolve: {
      tiket: TiketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TiketUpdateComponent,
    resolve: {
      tiket: TiketRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tiketRoute)],
  exports: [RouterModule],
})
export class TiketRoutingModule {}
