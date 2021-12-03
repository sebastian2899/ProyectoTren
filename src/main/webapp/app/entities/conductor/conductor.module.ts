import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ListComponent } from './list/list.component';
import { DetailComponent } from './detail/detail.component';
import { UpdateComponent } from './update/update.component';
import { DeleteComponent } from './delete/delete.component';
import { ConductorRoutingModule } from './route/conductor-routing.rmodule';

@NgModule({
  imports: [SharedModule, ConductorRoutingModule],
  declarations: [ListComponent, DetailComponent, UpdateComponent, DeleteComponent],
  entryComponents: [DeleteComponent],
})
export class ConductorModule {}
