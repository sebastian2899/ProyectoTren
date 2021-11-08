import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrenComponent } from './list/tren.component';
import { TrenDetailComponent } from './detail/tren-detail.component';
import { TrenUpdateComponent } from './update/tren-update.component';
import { TrenDeleteDialogComponent } from './delete/tren-delete-dialog.component';
import { TrenRoutingModule } from './route/tren-routing.module';

@NgModule({
  imports: [SharedModule, TrenRoutingModule],
  declarations: [TrenComponent, TrenDetailComponent, TrenUpdateComponent, TrenDeleteDialogComponent],
  entryComponents: [TrenDeleteDialogComponent],
})
export class TrenModule {}
