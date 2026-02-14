import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { API } from '../../core/api';
import { RouterModule } from '@angular/router';
import { FormService } from '../../core/services/form.service';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './transfer.html'
})
export class Transfer {

  transferData = {
    fromAccountId: Number(localStorage.getItem('accountId')),
    toAccountId: 0,
    amount: 0,
    idempotencyKey: crypto.randomUUID()
  };

  message = '';
  messageType = '';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private form: FormService
  ) {}

  loading = false;

  transfer(): void {

    if (this.loading) return;

    this.loading = true;

    this.http.post(API.TRANSFERS.CREATE, this.transferData)
      .subscribe({
        next: () => {
          this.form.setSuccess(this, 'Transfer successful!');
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.form.setError(this, err, 'Transfer failed');
          this.cdr.detectChanges();
        }
      });
  }
}