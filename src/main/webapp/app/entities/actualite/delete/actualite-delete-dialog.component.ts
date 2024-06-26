import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IActualite } from '../actualite.model';
import { ActualiteService } from '../service/actualite.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './actualite-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ActualiteDeleteDialogComponent {
  actualite?: IActualite;

  constructor(protected actualiteService: ActualiteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.actualiteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
