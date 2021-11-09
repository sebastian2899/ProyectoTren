import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TiketComponent } from './list/tiket.component';
import { TiketDetailComponent } from './detail/tiket-detail.component';
import { TiketUpdateComponent } from './update/tiket-update.component';
import { TiketDeleteDialogComponent } from './delete/tiket-delete-dialog.component';
import { TiketRoutingModule } from './route/tiket-routing.module';

@NgModule({
  imports: [SharedModule, TiketRoutingModule],
  declarations: [TiketComponent, TiketDetailComponent, TiketUpdateComponent, TiketDeleteDialogComponent],
  entryComponents: [TiketDeleteDialogComponent],
})
export class TiketModule {}
