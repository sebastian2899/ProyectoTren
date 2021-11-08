import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrenComponent } from '../list/tren.component';
import { TrenDetailComponent } from '../detail/tren-detail.component';
import { TrenUpdateComponent } from '../update/tren-update.component';
import { TrenRoutingResolveService } from './tren-routing-resolve.service';

const trenRoute: Routes = [
  {
    path: '',
    component: TrenComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrenDetailComponent,
    resolve: {
      tren: TrenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrenUpdateComponent,
    resolve: {
      tren: TrenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrenUpdateComponent,
    resolve: {
      tren: TrenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trenRoute)],
  exports: [RouterModule],
})
export class TrenRoutingModule {}
