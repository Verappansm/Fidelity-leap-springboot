import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { API } from '../../core/api';
import { RouterModule } from '@angular/router';

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
    private cdr: ChangeDetectorRef
  ) {}

  loading = false;

transfer(): void {

  if (this.loading) return;

  this.loading = true;

  this.http.post(API.TRANSFERS.CREATE, this.transferData)
    .subscribe({
      next: () => {
        this.message = 'Transfer successful!';
        this.messageType = 'success';
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.message =
          err?.error?.message || 'Transfer failed';
        this.messageType = 'error';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}