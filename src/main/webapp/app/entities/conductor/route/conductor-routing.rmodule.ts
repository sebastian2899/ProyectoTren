import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ListComponent } from '../list/list.component';
import { DetailComponent } from '../detail/detail.component';
import { UpdateComponent } from '../update/update.component';
import { ConductorRoutingResolveService } from '../route/conductor-routing-resolve.service';

const conductorRoute: Routes = [
  {
    path: '',
    component: ListComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DetailComponent,
    resolve: {
      conductor: ConductorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UpdateComponent,
    resolve: {
      conductor: ConductorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UpdateComponent,
    resolve: {
      conductor: ConductorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(conductorRoute)],
  exports: [RouterModule],
})
export class ConductorRoutingModule {}
